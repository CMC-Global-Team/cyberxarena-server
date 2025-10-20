USE internet_cafe;
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
  DECLARE total_discount_amount DECIMAL(12,2) DEFAULT 0;	

  -- Tổng tiền hàng
  SELECT IFNULL(SUM(sd.quantity * i.price), 0)
  INTO subtotal
  FROM sale_detail sd
  JOIN item i ON sd.item_id = i.item_id
  WHERE sd.sale_id = sale_id_ref;

  -- Thông tin sale & customer
  SELECT s.customer_id, s.discount_id
  INTO customer_id, sale_discount_id
  FROM sale s
  WHERE s.sale_id = sale_id_ref;

  -- Lấy thẻ thành viên
  SELECT c.membership_card_id
  INTO membership_card_id
  FROM customer c
  WHERE c.customer_id = customer_id;

  -- Discount của thẻ
  IF membership_card_id IS NOT NULL THEN
    SELECT m.discount_id
    INTO membership_discount_id
    FROM membership_card m
    WHERE m.membership_card_id = membership_card_id;
  END IF;

  -- Lấy thông tin discount của sale
  IF sale_discount_id IS NOT NULL THEN
    SELECT discount_value, discount_type
    INTO sale_discount_value, sale_discount_type
    FROM discount WHERE discount_id = sale_discount_id;
  END IF;

  -- Lấy thông tin discount của membership
  IF membership_discount_id IS NOT NULL THEN
    SELECT discount_value, discount_type
    INTO membership_discount_value, membership_discount_type
    FROM discount WHERE discount_id = membership_discount_id;
  END IF;

  -- Tính tổng số tiền giảm
  IF sale_discount_type = 'Percentage' THEN
    SET total_discount_amount = total_discount_amount + subtotal * (sale_discount_value / 100);
  ELSEIF sale_discount_type = 'Flat' THEN
    SET total_discount_amount = total_discount_amount + sale_discount_value;
  END IF;

  IF membership_discount_type = 'Percentage' THEN
    SET total_discount_amount = total_discount_amount + subtotal * (membership_discount_value / 100);
  ELSEIF membership_discount_type = 'Flat' THEN
    SET total_discount_amount = total_discount_amount + membership_discount_value;
  END IF;

  IF total_discount_amount > subtotal THEN
    SET total_discount_amount = subtotal;
  END IF;

  -- Tổng sau giảm giá
  SET subtotal = subtotal - total_discount_amount;

  IF EXISTS (SELECT 1 FROM sale_total WHERE sale_id = sale_id_ref) THEN
    UPDATE sale_total
    SET total_amount = subtotal
    WHERE sale_id = sale_id_ref;
  ELSE
    INSERT INTO sale_total (sale_id, total_amount)
    VALUES (sale_id_ref, subtotal);
  END IF;
END$$


DELIMITER ;
DROP PROCEDURE IF EXISTS update_sale_total;

DELIMITER $$

CREATE TRIGGER trg_sale_total_after_insert
AFTER INSERT ON sale_detail
FOR EACH ROW
BEGIN 
	CALL update_sale_total(NEW.sale_id);
END$$

CREATE TRIGGER trg_sale_total_after_update
AFTER UPDATE ON sale_detail
FOR EACH ROW
BEGIN 
	CALL update_sale_total(NEW.sale_id);
END$$

CREATE TRIGGER trg_sale_total_after_delete
AFTER DELETE ON sale_detail
FOR EACH ROW
BEGIN 
	CALL update_sale_total(NEW.sale_id);
END$$

DELIMITER ;

