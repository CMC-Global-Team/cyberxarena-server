USE internet_cafe;

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

-- ======================
-- PROCEDURE: CẬP NHẬT TỔNG TIỀN HÓA ĐƠN
-- ======================
DELIMITER $$

CREATE PROCEDURE update_sale_total(IN sale_id_ref INT)
BEGIN
  -- Khai báo biến
  DECLARE subtotal DECIMAL(12,2) DEFAULT 0;
  DECLARE sale_discount_id INT DEFAULT NULL;
  DECLARE sale_discount_value DECIMAL(10,2) DEFAULT 0;
  DECLARE sale_discount_type VARCHAR(20) DEFAULT NULL;
  DECLARE customer_id INT DEFAULT NULL;
  DECLARE membership_card_id INT DEFAULT NULL;
  DECLARE membership_discount_id INT DEFAULT NULL;
  DECLARE membership_discount_value DECIMAL(10,2) DEFAULT 0;
  DECLARE membership_discount_type VARCHAR(20) DEFAULT NULL;
  
  -- Biến tính discount riêng biệt
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
    -- Set total = 0 và kết thúc
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
      
      -- Đảm bảo không vượt quá subtotal
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
      
      -- Đảm bảo không vượt quá subtotal
      IF membership_discount_amount > subtotal THEN
        SET membership_discount_amount = subtotal;
      END IF;
    END IF;
    
    -- 8. CHỌN DISCOUNT CAO NHẤT (TỰ ĐỘNG)
    SET applied_discount_amount = GREATEST(sale_discount_amount, membership_discount_amount);
    
    -- 9. Tính tổng cuối cùng
    SET final_total = subtotal - applied_discount_amount;
    
    -- Đảm bảo final_total không âm
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
  
  -- Con trỏ duyệt qua các khách hàng đang dùng thẻ bị xóa
  DECLARE cur CURSOR FOR
    SELECT customer_id
    FROM customer
    WHERE membership_card_id = OLD.membership_card_id;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
  
  -- Lấy ID thẻ mặc định
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
    
    -- Tính tổng tiền nạp của khách hàng này
    SELECT COALESCE(SUM(amount), 0) INTO total_recharge
    FROM recharge_history
    WHERE customer_id = c_id;
    
    -- Tìm thẻ phù hợp nhất (ngưỡng <= tổng nạp, lớn nhất có thể)
    SELECT membership_card_id INTO suitable_card_id
    FROM membership_card
    WHERE recharge_threshold <= total_recharge
    ORDER BY recharge_threshold DESC
    LIMIT 1;
    
    -- Nếu không có thẻ phù hợp thì gán thẻ mặc định
    IF suitable_card_id IS NULL THEN
      SET suitable_card_id = default_card_id;
    END IF;
    
    -- Cập nhật thẻ mới cho khách hàng
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
    
    -- Tính tổng tiền đã nạp của customer
    SELECT IFNULL(SUM(amount), 0) INTO total_recharged
    FROM recharge_history
    WHERE customer_id = NEW.customer_id;
    
    -- Tìm membership card phù hợp nhất (rank cao nhất mà customer đủ điều kiện)
    SELECT membership_card_id INTO new_membership_id
    FROM membership_card
    WHERE recharge_threshold <= total_recharged
    ORDER BY recharge_threshold DESC
    LIMIT 1;
    
    -- Cập nhật membership card cho customer nếu tìm thấy
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
    
    -- Lấy số lượng hiện tại trong kho
    SELECT stock INTO current_stock 
    FROM item 
    WHERE item_id = NEW.item_id;
    
    -- Kiểm tra nếu không đủ hàng
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
    
    -- Lấy số lượng hiện tại trong kho
    SELECT stock INTO current_stock 
    FROM item 
    WHERE item_id = NEW.item_id;
    
    -- Kiểm tra nếu không đủ hàng (chỉ khi tăng số lượng)
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
    -- Trừ số lượng sản phẩm trong kho
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
    -- Tính toán sự thay đổi số lượng
    DECLARE quantity_change INT DEFAULT NEW.quantity - OLD.quantity;
    
    -- Cập nhật số lượng kho
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
    -- Hoàn trả số lượng sản phẩm vào kho
    UPDATE item 
    SET stock = stock + OLD.quantity 
    WHERE item_id = OLD.item_id;
END$$

DELIMITER ;

-- ======================
-- TRIGGER: CẬP NHẬT PHIÊN SỬ DỤNG MÁY TÍNH
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
-- KIỂM TRA CÁC TRIGGER ĐÃ TẠO THÀNH CÔNG
-- ======================
SELECT 
    TRIGGER_NAME,
    EVENT_MANIPULATION,
    EVENT_OBJECT_TABLE,
    ACTION_TIMING
FROM information_schema.TRIGGERS
WHERE TRIGGER_SCHEMA = 'internet_cafe'
ORDER BY EVENT_OBJECT_TABLE, ACTION_TIMING, EVENT_MANIPULATION;
