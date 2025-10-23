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
