USE railway;

-- ======================
-- XÓA CÁC TRIGGER VÀ PROCEDURE CŨ
-- ======================
DROP PROCEDURE IF EXISTS update_sale_total;
DROP TRIGGER IF EXISTS before_delete_membership_card;
DROP TRIGGER IF EXISTS trg_upgrade_membership_after_recharge;
DROP TRIGGER IF EXISTS update_stock_on_sale_insert;
DROP TRIGGER IF EXISTS update_stock_on_sale_update;
DROP TRIGGER IF EXISTS update_stock_on_sale_delete;
DROP TRIGGER IF EXISTS check_stock_before_sale_insert;
DROP TRIGGER IF EXISTS check_stock_before_sale_update;
DROP TRIGGER IF EXISTS trg_session_update;
DROP TRIGGER IF EXISTS trg_auto_deduct_on_session_start;
DROP TRIGGER IF EXISTS trg_auto_end_session_on_insufficient_balance;
DROP TRIGGER IF EXISTS trg_update_computer_status_on_session_start;
DROP TRIGGER IF EXISTS trg_update_computer_status_on_session_change;
DROP TRIGGER IF EXISTS trg_update_computer_status_on_session_end;
DROP TRIGGER IF EXISTS trg_restock_on_refund_completed;
DROP TRIGGER IF EXISTS trg_exclude_refunded_sales_from_revenue;
DROP TRIGGER IF EXISTS trg_restock_on_refund_cancelled;
DROP EVENT IF EXISTS evt_auto_deduct_realtime;

-- ======================
-- PROCEDURE: CẬP NHẬT TỔNG TIỀN HÓA ĐƠN
-- ======================
DELIMITER $$

CREATE PROCEDURE update_sale_total(IN sale_id_ref INT)
BEGIN
  DECLARE subtotal DECIMAL(12,2) DEFAULT 0;
  DECLARE sale_discount_id INT DEFAULT NULL;
  DECLARE sale_discount_value DECIMAL(10,2) DEFAULT 0;
  DECLARE sale_discount_type VARCHAR(20) DEFAULT NULL;
  DECLARE customer_id INT DEFAULT NULL;
  DECLARE membership_card_id INT DEFAULT NULL;
  DECLARE membership_discount_id INT DEFAULT NULL;
  DECLARE membership_discount_value DECIMAL(10,2) DEFAULT 0;
  DECLARE membership_discount_type VARCHAR(20) DEFAULT NULL;
  DECLARE sale_discount_amount DECIMAL(12,2) DEFAULT 0;
  DECLARE membership_discount_amount DECIMAL(12,2) DEFAULT 0;
  DECLARE applied_discount_amount DECIMAL(12,2) DEFAULT 0;
  DECLARE final_total DECIMAL(12,2) DEFAULT 0;
  
  -- 1. Tính tổng tiền hàng (subtotal)
  SELECT IFNULL(SUM(sd.quantity * i.price), 0)
  INTO subtotal
  FROM sale_detail sd
  JOIN item i ON sd.item_id = i.item_id
  WHERE sd.sale_id = sale_id_ref;
  
  -- 2. Xử lý trường hợp không có sản phẩm
  IF subtotal = 0 THEN
    IF EXISTS (SELECT 1 FROM sale_total WHERE sale_id = sale_id_ref) THEN
      UPDATE sale_total SET total_amount = 0 WHERE sale_id = sale_id_ref;
    ELSE
      INSERT INTO sale_total (sale_id, total_amount) VALUES (sale_id_ref, 0);
    END IF;
  ELSE
    -- 3. Lấy thông tin sale & customer
    SELECT s.customer_id, s.discount_id
    INTO customer_id, sale_discount_id
    FROM sale s
    WHERE s.sale_id = sale_id_ref;
    
    -- 4. Lấy thẻ thành viên của customer
    IF customer_id IS NOT NULL THEN
      SELECT c.membership_card_id
      INTO membership_card_id
      FROM customer c
      WHERE c.customer_id = customer_id;
    END IF;
    
    -- 5. Lấy discount_id từ thẻ thành viên
    IF membership_card_id IS NOT NULL THEN
      SELECT m.discount_id
      INTO membership_discount_id
      FROM membership_card m
      WHERE m.membership_card_id = membership_card_id;
    END IF;
    
    -- 6. TÍNH SALE DISCOUNT (nếu có)
    IF sale_discount_id IS NOT NULL THEN
      SELECT discount_value, discount_type
      INTO sale_discount_value, sale_discount_type
      FROM discount 
      WHERE discount_id = sale_discount_id;
      
      IF sale_discount_type = 'Percentage' THEN
        SET sale_discount_amount = subtotal * (sale_discount_value / 100);
      ELSEIF sale_discount_type = 'Flat' THEN
        SET sale_discount_amount = sale_discount_value;
      END IF;
      
      IF sale_discount_amount > subtotal THEN
        SET sale_discount_amount = subtotal;
      END IF;
    END IF;
    
    -- 7. TÍNH MEMBERSHIP DISCOUNT (nếu có)
    IF membership_discount_id IS NOT NULL THEN
      SELECT discount_value, discount_type
      INTO membership_discount_value, membership_discount_type
      FROM discount 
      WHERE discount_id = membership_discount_id;
      
      IF membership_discount_type = 'Percentage' THEN
        SET membership_discount_amount = subtotal * (membership_discount_value / 100);
      ELSEIF membership_discount_type = 'Flat' THEN
        SET membership_discount_amount = membership_discount_value;
      END IF;
      
      IF membership_discount_amount > subtotal THEN
        SET membership_discount_amount = subtotal;
      END IF;
    END IF;
    
    -- 8. CHỌN DISCOUNT CAO NHẤT
    SET applied_discount_amount = GREATEST(sale_discount_amount, membership_discount_amount);
    
    -- 9. Tính tổng cuối cùng
    SET final_total = subtotal - applied_discount_amount;
    
    IF final_total < 0 THEN
      SET final_total = 0;
    END IF;
    
    -- 10. Cập nhật hoặc thêm mới vào sale_total
    IF EXISTS (SELECT 1 FROM sale_total WHERE sale_id = sale_id_ref) THEN
      UPDATE sale_total
      SET total_amount = final_total
      WHERE sale_id = sale_id_ref;
    ELSE
      INSERT INTO sale_total (sale_id, total_amount)
      VALUES (sale_id_ref, final_total);
    END IF;
  END IF;
  
END$$

DELIMITER ;

-- ======================
-- TRIGGER: TỰ ĐỘNG CHUYỂN THẺ THÀNH VIÊN KHI XÓA THẺ
-- ======================
DELIMITER $$

CREATE TRIGGER before_delete_membership_card
BEFORE DELETE ON membership_card
FOR EACH ROW
BEGIN
  DECLARE default_card_id INT;
  DECLARE suitable_card_id INT;
  DECLARE done INT DEFAULT 0;
  DECLARE c_id INT;
  DECLARE total_recharge DECIMAL(12,2);
  
  DECLARE cur CURSOR FOR
    SELECT customer_id
    FROM customer
    WHERE membership_card_id = OLD.membership_card_id;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
  
  SELECT membership_card_id INTO default_card_id
  FROM membership_card
  WHERE is_default = TRUE
  LIMIT 1;
  
  OPEN cur;
  read_loop: LOOP
    FETCH cur INTO c_id;
    IF done = 1 THEN
      LEAVE read_loop;
    END IF;
    
    SELECT COALESCE(SUM(amount), 0) INTO total_recharge
    FROM recharge_history
    WHERE customer_id = c_id;
    
    SELECT membership_card_id INTO suitable_card_id
    FROM membership_card
    WHERE recharge_threshold <= total_recharge
    ORDER BY recharge_threshold DESC
    LIMIT 1;
    
    IF suitable_card_id IS NULL THEN
      SET suitable_card_id = default_card_id;
    END IF;
    
    UPDATE customer
    SET membership_card_id = suitable_card_id
    WHERE customer_id = c_id;
  END LOOP;
  
  CLOSE cur;
END$$

DELIMITER ;

-- ======================
-- TRIGGER: TỰ ĐỘNG NÂNG CẤP THẺ THÀNH VIÊN SAU KHI NẠP TIỀN
-- ======================
DELIMITER $$

CREATE TRIGGER trg_upgrade_membership_after_recharge
AFTER INSERT ON recharge_history
FOR EACH ROW
BEGIN
    DECLARE total_recharged DECIMAL(12,2);
    DECLARE new_membership_id INT;
    
    SELECT IFNULL(SUM(amount), 0) INTO total_recharged
    FROM recharge_history
    WHERE customer_id = NEW.customer_id;
    
    SELECT membership_card_id INTO new_membership_id
    FROM membership_card
    WHERE recharge_threshold <= total_recharged
    ORDER BY recharge_threshold DESC
    LIMIT 1;
    
    IF new_membership_id IS NOT NULL THEN
        UPDATE customer
        SET membership_card_id = new_membership_id
        WHERE customer_id = NEW.customer_id;
    END IF;
    
END$$

DELIMITER ;

-- ======================
-- TRIGGER: KIỂM TRA SỐ LƯỢNG KHO TRƯỚC KHI THÊM SẢN PHẨM
-- ======================
DELIMITER $$

CREATE TRIGGER check_stock_before_sale_insert
BEFORE INSERT ON sale_detail
FOR EACH ROW
BEGIN
    DECLARE current_stock INT DEFAULT 0;
    
    SELECT stock INTO current_stock 
    FROM item 
    WHERE item_id = NEW.item_id;
    
    IF current_stock < NEW.quantity THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Khong du hang trong kho!';
    END IF;
END$$

DELIMITER ;

-- ======================
-- TRIGGER: KIỂM TRA SỐ LƯỢNG KHO TRƯỚC KHI CẬP NHẬT SẢN PHẨM
-- ======================
DELIMITER $$

CREATE TRIGGER check_stock_before_sale_update
BEFORE UPDATE ON sale_detail
FOR EACH ROW
BEGIN
    DECLARE current_stock INT DEFAULT 0;
    DECLARE quantity_change INT DEFAULT NEW.quantity - OLD.quantity;
    
    SELECT stock INTO current_stock 
    FROM item 
    WHERE item_id = NEW.item_id;
    
    IF quantity_change > 0 AND current_stock < quantity_change THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Khong du hang trong kho!';
    END IF;
END$$

DELIMITER ;

-- ======================
-- TRIGGER: CẬP NHẬT SỐ LƯỢNG KHO KHI THÊM SẢN PHẨM VÀO HÓA ĐƠN
-- ======================
DELIMITER $$

CREATE TRIGGER update_stock_on_sale_insert
AFTER INSERT ON sale_detail
FOR EACH ROW
BEGIN
    UPDATE item 
    SET stock = stock - NEW.quantity 
    WHERE item_id = NEW.item_id;
END$$

DELIMITER ;

-- ======================
-- TRIGGER: CẬP NHẬT SỐ LƯỢNG KHO KHI CẬP NHẬT SẢN PHẨM TRONG HÓA ĐƠN
-- ======================
DELIMITER $$

CREATE TRIGGER update_stock_on_sale_update
AFTER UPDATE ON sale_detail
FOR EACH ROW
BEGIN
    DECLARE quantity_change INT DEFAULT NEW.quantity - OLD.quantity;
    
    UPDATE item 
    SET stock = stock - quantity_change 
    WHERE item_id = NEW.item_id;
END$$

DELIMITER ;

-- ======================
-- TRIGGER: HOÀN TRẢ SỐ LƯỢNG KHO KHI XÓA SẢN PHẨM KHỎI HÓA ĐƠN
-- ======================
DELIMITER $$

CREATE TRIGGER update_stock_on_sale_delete
AFTER DELETE ON sale_detail
FOR EACH ROW
BEGIN
    UPDATE item 
    SET stock = stock + OLD.quantity 
    WHERE item_id = OLD.item_id;
END$$

DELIMITER ;

-- ======================
-- TRIGGER: CẬP NHẬT PHIÊN SỬ DỤNG MÁY TÍNH
-- Xử lý cả: đổi máy và kết thúc phiên
-- ======================
DELIMITER $$

CREATE TRIGGER trg_session_update
AFTER UPDATE ON session
FOR EACH ROW
BEGIN
    DECLARE hourly_rate DECIMAL(10,2);
    DECLARE hourly_used DECIMAL(10,2);
    DECLARE total DECIMAL(10,2);
    DECLARE current_balance DECIMAL(10,2);
    DECLARE remaining DECIMAL(10,2) DEFAULT 0;
    
    -- =============================================
    -- TRƯỜNG HỢP 1: ĐỔI MÁY (computer_id thay đổi)
    -- =============================================
    IF OLD.computer_id != NEW.computer_id THEN
        -- Máy cũ → Available
        UPDATE computer
        SET status = 'Available'
        WHERE computer_id = OLD.computer_id;
        
        -- Máy mới → In Use
        UPDATE computer
        SET status = 'In Use'
        WHERE computer_id = NEW.computer_id;
    END IF;
    
    -- =============================================
    -- TRƯỜNG HỢP 2: KẾT THÚC PHIÊN (end_time được set)
    -- =============================================
    IF OLD.end_time IS NULL AND NEW.end_time IS NOT NULL THEN
        
        -- 1. Cập nhật trạng thái máy tính thành 'Available'
        UPDATE computer
        SET status = 'Available'
        WHERE computer_id = NEW.computer_id;
        
        -- 2. Lấy giá theo giờ của máy tính
        SELECT price_per_hour INTO hourly_rate
        FROM computer
        WHERE computer_id = NEW.computer_id;
        
        -- 3. Tính số giờ đã sử dụng
        SET hourly_used = TIMESTAMPDIFF(SECOND, NEW.start_time, NEW.end_time) / 3600;
        
        -- 4. Tính tổng tiền phải trả
        SET total = ROUND(hourly_used * hourly_rate, 2);
        
        -- 5. Lấy số dư hiện tại của khách hàng
        SELECT balance INTO current_balance
        FROM customer
        WHERE customer_id = NEW.customer_id
        LIMIT 1
        FOR UPDATE;
        
        -- 6. Trừ tiền trong tài khoản khách hàng
        UPDATE customer
        SET balance = ROUND(current_balance - total, 2)
        WHERE customer_id = NEW.customer_id;
        
        -- 7. Lấy remaining_hours hiện tại
        SELECT remaining_hours INTO remaining
        FROM session_usage
        WHERE session_id = NEW.session_id
        LIMIT 1;
        
        -- 8. Cập nhật session_price
        INSERT INTO session_price (session_id, total_amount)
        VALUES (NEW.session_id, total)
        ON DUPLICATE KEY UPDATE total_amount = total;
        
        -- 9. Cập nhật session_usage
        UPDATE session_usage
        SET 
            duration_hours = hourly_used,
            remaining_hours = GREATEST(remaining - hourly_used, 0)
        WHERE session_id = NEW.session_id;
        
    END IF;
END$$

DELIMITER ;

-- ======================
-- TRIGGER: CẬP NHẬT TRẠNG THÁI MÁY KHI BẮT ĐẦU PHIÊN
-- ======================
DELIMITER $$

CREATE TRIGGER trg_update_computer_status_on_session_insert
AFTER INSERT ON session
FOR EACH ROW
BEGIN
    UPDATE computer
    SET status = 'In Use'
    WHERE computer_id = NEW.computer_id;
END$$

DELIMITER ;

-- ======================
-- TRIGGER: TỰ ĐỘNG HOÀN TRẢ KHO KHI HOÀN TIỀN THÀNH CÔNG
-- ======================
DELIMITER $$

CREATE TRIGGER trg_restock_on_refund_completed
AFTER UPDATE ON refund
FOR EACH ROW
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE v_item_id INT;
    DECLARE v_quantity INT;
    
    DECLARE refund_cursor CURSOR FOR
        SELECT rd.item_id, rd.quantity
        FROM refund_detail rd
        WHERE rd.refund_id = NEW.refund_id;
    
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    -- Chỉ xử lý khi trạng thái hoàn tiền chuyển thành 'Completed'
    IF OLD.status != 'Completed' AND NEW.status = 'Completed' THEN
        OPEN refund_cursor;
        
        refund_loop: LOOP
            FETCH refund_cursor INTO v_item_id, v_quantity;
            
            IF done THEN
                LEAVE refund_loop;
            END IF;
            
            -- Hoàn trả số lượng vào kho
            UPDATE item
            SET stock = stock + v_quantity
            WHERE item_id = v_item_id;
            
        END LOOP;
        
        CLOSE refund_cursor;
    END IF;
END$$

DELIMITER ;

-- ======================
-- TRIGGER: LOẠI TRỪ HÓA ĐƠN ĐÃ HOÀN KHỎI BÁO CÁO DOANH THU
-- ======================
DELIMITER $$

CREATE TRIGGER trg_exclude_refunded_sales_from_revenue
AFTER UPDATE ON refund
FOR EACH ROW
BEGIN
    DECLARE v_sale_id INT;
    DECLARE v_refund_amount DECIMAL(10,2);
    
    -- Chỉ xử lý khi trạng thái hoàn tiền chuyển thành 'Completed'
    IF OLD.status != 'Completed' AND NEW.status = 'Completed' THEN
        -- Lấy sale_id từ refund
        SELECT sale_id INTO v_sale_id
        FROM refund
        WHERE refund_id = NEW.refund_id;
        
        -- Lấy số tiền hoàn
        SET v_refund_amount = NEW.refund_amount;
        
        -- Cập nhật trạng thái hóa đơn thành 'Refunded'
        UPDATE sale
        SET status = 'Refunded'
        WHERE sale_id = v_sale_id;
        
        -- Cập nhật lại tổng tiền hóa đơn (trừ đi số tiền hoàn)
        UPDATE sale_total
        SET total_amount = GREATEST(total_amount - v_refund_amount, 0)
        WHERE sale_id = v_sale_id;
    END IF;
END$$

DELIMITER ;

-- ======================
-- EVENT SCHEDULER: TỰ ĐỘNG TRỪ TIỀN THEO THỜI GIAN THỰC
-- ======================
DELIMITER $$

CREATE EVENT evt_auto_deduct_realtime
ON SCHEDULE EVERY 1 MINUTE
DO
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE v_session_id INT;
    DECLARE v_customer_id INT;
    DECLARE v_computer_id INT;
    DECLARE v_start_time DATETIME;
    DECLARE v_hourly_rate DECIMAL(10,2);
    DECLARE v_current_balance DECIMAL(12,2);
    DECLARE v_elapsed_hours DECIMAL(10,4);
    DECLARE v_deduct_amount DECIMAL(12,2);
    DECLARE v_remaining_hours DECIMAL(10,2);
    
    DECLARE session_cursor CURSOR FOR
        SELECT s.session_id, s.customer_id, s.computer_id, s.start_time, c.price_per_hour
        FROM session s
        JOIN computer c ON s.computer_id = c.computer_id
        WHERE s.end_time IS NULL;
    
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    OPEN session_cursor;
    
    session_loop: LOOP
        FETCH session_cursor INTO v_session_id, v_customer_id, v_computer_id, v_start_time, v_hourly_rate;
        
        IF done THEN
            LEAVE session_loop;
        END IF;
        
        -- Tính thời gian đã trôi qua (tính bằng giờ)
        SET v_elapsed_hours = TIMESTAMPDIFF(SECOND, v_start_time, NOW()) / 3600;
        
        -- Tính số tiền cần trừ (mỗi phút = 1/60 giờ)
        SET v_deduct_amount = (v_hourly_rate / 60);
        
        -- Lấy số dư hiện tại
        SELECT balance INTO v_current_balance
        FROM customer
        WHERE customer_id = v_customer_id;
        
        -- Kiểm tra nếu đủ tiền để trừ
        IF v_current_balance >= v_deduct_amount THEN
            -- Trừ tiền
            UPDATE customer
            SET balance = balance - v_deduct_amount
            WHERE customer_id = v_customer_id;
            
            -- Cập nhật session_usage
            SELECT remaining_hours INTO v_remaining_hours
            FROM session_usage
            WHERE session_id = v_session_id;
            
            UPDATE session_usage
            SET 
                duration_hours = v_elapsed_hours,
                remaining_hours = GREATEST(v_remaining_hours - (1.0/60), 0),
                updated_at = NOW()
            WHERE session_id = v_session_id;
            
            -- Cập nhật session_price
            UPDATE session_price
            SET total_amount = total_amount + v_deduct_amount
            WHERE session_id = v_session_id;
            
        ELSE
            -- Nếu không đủ tiền, kết thúc phiên
            UPDATE session
            SET end_time = NOW()
            WHERE session_id = v_session_id;
            
            -- Cập nhật trạng thái máy tính
            UPDATE computer
            SET status = 'Available'
            WHERE computer_id = v_computer_id;
            
            -- Cập nhật session_usage lần cuối
            UPDATE session_usage
            SET 
                duration_hours = v_elapsed_hours,
                remaining_hours = 0,
                updated_at = NOW()
            WHERE session_id = v_session_id;
        END IF;
        
    END LOOP;
    
    CLOSE session_cursor;
END$$

DELIMITER ;

-- ======================
-- BẬT EVENT SCHEDULER
-- ======================
SET GLOBAL event_scheduler = ON;

-- ======================
-- KIỂM TRA CÁC TRIGGER ĐÃ TẠO THÀNH CÔNG
-- ======================
SELECT 
    TRIGGER_NAME,
    EVENT_MANIPULATION,
    EVENT_OBJECT_TABLE,
    ACTION_TIMING
FROM information_schema.TRIGGERS
WHERE TRIGGER_SCHEMA = 'railway'
ORDER BY EVENT_OBJECT_TABLE, ACTION_TIMING, EVENT_MANIPULATION;
