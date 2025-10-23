USE internet_cafe;

DROP PROCEDURE IF EXISTS update_sale_total;

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
