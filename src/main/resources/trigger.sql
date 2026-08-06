-- ============================================================================
-- CYBERXARENA INTERNET CAFE MANAGEMENT SYSTEM - DATABASE TRIGGERS
-- File: trigger.sql
-- Description: Full set of production business logic triggers for MySQL 8.0+
-- ============================================================================

USE `cyberxarena`;

DELIMITER $$

-- ----------------------------------------------------------------------------
-- 1. TRIGGER: Tự động cộng tiền tài khoản & nâng hạng thẻ khi nạp tiền (Recharge)
-- ----------------------------------------------------------------------------
DROP TRIGGER IF EXISTS `trg_after_recharge_update_balance_and_membership`$$

CREATE TRIGGER `trg_after_recharge_update_balance_and_membership`
AFTER INSERT ON `recharge_history`
FOR EACH ROW
BEGIN
    DECLARE total_recharged DECIMAL(12, 2);
    DECLARE new_card_id INT;

    -- Cộng số tiền vừa nạp vào số dư tài khoản khách hàng
    UPDATE `customer` 
    SET `balance` = `balance` + NEW.`amount`
    WHERE `customer_id` = NEW.`customer_id`;

    -- Tính tổng số tiền đã nạp của khách hàng này từ trước tới nay
    SELECT COALESCE(SUM(`amount`), 0.00) INTO total_recharged
    FROM `recharge_history`
    WHERE `customer_id` = NEW.`customer_id`;

    -- Tìm hạng thẻ thành viên cao nhất phù hợp với tổng số tiền nạp
    SELECT `membership_card_id` INTO new_card_id
    FROM `membership_card`
    WHERE total_recharged >= `recharge_threshold`
    ORDER BY `recharge_threshold` DESC
    LIMIT 1;

    -- Nếu tìm thấy hạng thẻ cao hơn, cập nhật cho khách hàng
    IF new_card_id IS NOT NULL THEN
        UPDATE `customer`
        SET `membership_card_id` = new_card_id
        WHERE `customer_id` = NEW.`customer_id`;
    END IF;
END$$


-- ----------------------------------------------------------------------------
-- 2. TRIGGER: Cập nhật trạng thái máy thành 'In Use' & tạo khởi tạo phiên khi bật máy
-- ----------------------------------------------------------------------------
DROP TRIGGER IF EXISTS `trg_after_session_start`$$

CREATE TRIGGER `trg_after_session_start`
AFTER INSERT ON `session`
FOR EACH ROW
BEGIN
    -- Chuyển trạng thái máy trạm sang 'In Use'
    UPDATE `computer`
    SET `status` = 'In Use'
    WHERE `computer_id` = NEW.`computer_id`;

    -- Khởi tạo bản ghi thành tiền bằng 0.00
    INSERT INTO `session_price` (`session_id`, `total_amount`)
    VALUES (NEW.`session_id`, 0.00)
    ON DUPLICATE KEY UPDATE `total_amount` = 0.00;

    -- Khởi tạo bản ghi thời gian sử dụng
    INSERT INTO `session_usage` (`session_id`, `duration_hours`, `remaining_hours`, `updated_at`)
    VALUES (NEW.`session_id`, 0.0, 0.0, NEW.`start_time`)
    ON DUPLICATE KEY UPDATE `updated_at` = NEW.`start_time`;
END$$


-- ----------------------------------------------------------------------------
-- 3. TRIGGER: Tự động tính tiền & trả máy về 'Available' khi kết thúc phiên chơi
-- ----------------------------------------------------------------------------
DROP TRIGGER IF EXISTS `trg_after_session_end`$$

CREATE TRIGGER `trg_after_session_end`
AFTER UPDATE ON `session`
FOR EACH ROW
BEGIN
    DECLARE duration_hrs DOUBLE;
    DECLARE hourly_price DECIMAL(10, 2);
    DECLARE total_cost DECIMAL(10, 2);

    -- Kiểm tra nếu kết thúc phiên chơi (end_time được cập nhật)
    IF NEW.`end_time` IS NOT NULL AND OLD.`end_time` IS NULL THEN
        -- Chuyển máy trạm về lại trạng thái 'Available'
        UPDATE `computer`
        SET `status` = 'Available'
        WHERE `computer_id` = NEW.`computer_id`;

        -- Tính thời gian chơi theo giờ (chính xác tới giây)
        SET duration_hrs = ROUND(TIMESTAMPDIFF(SECOND, NEW.`start_time`, NEW.`end_time`) / 3600.0, 2);

        -- Lấy giá giờ chơi của máy trạm
        SELECT `price_per_hour` INTO hourly_price
        FROM `computer`
        WHERE `computer_id` = NEW.`computer_id`;

        IF hourly_price IS NULL THEN
            SET hourly_price = 0.00;
        END IF;

        SET total_cost = ROUND(duration_hrs * hourly_price, 2);

        -- Cập nhật tổng tiền phiên chơi
        UPDATE `session_price`
        SET `total_amount` = total_cost
        WHERE `session_id` = NEW.`session_id`;

        -- Cập nhật thời gian sử dụng
        UPDATE `session_usage`
        SET `duration_hours` = duration_hrs,
            `remaining_hours` = 0.0,
            `updated_at` = NEW.`end_time`
        WHERE `session_id` = NEW.`session_id`;
    END IF;
END$$


-- ----------------------------------------------------------------------------
-- 4. TRIGGER: Kiểm tra tồn kho trước khi đặt mua món ăn/dịch vụ
-- ----------------------------------------------------------------------------
DROP TRIGGER IF EXISTS `trg_before_sale_detail_check_stock`$$

CREATE TRIGGER `trg_before_sale_detail_check_stock`
BEFORE INSERT ON `sale_detail`
FOR EACH ROW
BEGIN
    DECLARE current_stock INT;
    DECLARE item_name_val VARCHAR(100);

    SELECT `stock`, `item_name` INTO current_stock, item_name_val
    FROM `item`
    WHERE `item_id` = NEW.`item_id`;

    IF current_stock IS NULL OR current_stock < NEW.`quantity` THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Lỗi: Số lượng tồn kho không đủ để thực hiện đơn hàng!';
    END IF;
END$$


-- ----------------------------------------------------------------------------
-- 5. TRIGGER: Trừ tồn kho sản phẩm & Cập nhật tổng tiền đơn hàng (Sale Detail)
-- ----------------------------------------------------------------------------
DROP TRIGGER IF EXISTS `trg_after_sale_detail_insert`$$

CREATE TRIGGER `trg_after_sale_detail_insert`
AFTER INSERT ON `sale_detail`
FOR EACH ROW
BEGIN
    DECLARE item_unit_price DECIMAL(10, 2);
    DECLARE line_total DECIMAL(10, 2);

    -- Trừ số lượng tồn kho của mặt hàng
    UPDATE `item`
    SET `stock` = `stock` - NEW.`quantity`
    WHERE `item_id` = NEW.`item_id`;

    -- Lấy đơn giá món
    SELECT `price` INTO item_unit_price
    FROM `item`
    WHERE `item_id` = NEW.`item_id`;

    SET line_total = item_unit_price * NEW.`quantity`;

    -- Cập nhật hoặc thêm mới tổng tiền hóa đơn vào sale_total
    INSERT INTO `sale_total` (`sale_id`, `total_amount`)
    VALUES (NEW.`sale_id`, line_total)
    ON DUPLICATE KEY UPDATE `total_amount` = `total_amount` + line_total;
END$$


-- ----------------------------------------------------------------------------
-- 6. TRIGGER: Tự động trừ số dư tài khoản khi đơn hàng ở trạng thái 'Paid' qua 'Balance'
-- ----------------------------------------------------------------------------
DROP TRIGGER IF EXISTS `trg_after_sale_paid_deduct_balance`$$

CREATE TRIGGER `trg_after_sale_paid_deduct_balance`
AFTER UPDATE ON `sale`
FOR EACH ROW
BEGIN
    DECLARE total_order DECIMAL(10, 2);

    -- Khi chuyển trạng thái sang Paid và phương thức thanh toán là trừ tài khoản (Balance)
    IF NEW.`status` = 'Paid' AND OLD.`status` != 'Paid' AND NEW.`payment_method` = 'Balance' THEN
        SELECT `total_amount` INTO total_order
        FROM `sale_total`
        WHERE `sale_id` = NEW.`sale_id`;

        IF total_order IS NOT NULL AND total_order > 0 THEN
            UPDATE `customer`
            SET `balance` = `balance` - total_order
            WHERE `customer_id` = NEW.`customer_id`;
        END IF;
    END IF;
END$$


-- ----------------------------------------------------------------------------
-- 7. TRIGGER: Hoàn trả số lượng kho & Hoàn tiền khi đơn Refund chuyển trạng thái 'Completed'
-- ----------------------------------------------------------------------------
DROP TRIGGER IF EXISTS `trg_after_refund_completed`$$

CREATE TRIGGER `trg_after_refund_completed`
AFTER UPDATE ON `refund`
FOR EACH ROW
BEGIN
    DECLARE target_customer_id INT;
    DECLARE pay_method VARCHAR(50);

    IF NEW.`status` = 'Completed' AND OLD.`status` != 'Completed' THEN
        -- Hoàn trả lại số lượng tồn kho sản phẩm từ refund_detail
        UPDATE `item` i
        JOIN `sale_detail` sd ON i.`item_id` = sd.`item_id`
        JOIN `refund_detail` rd ON sd.`sale_detail_id` = rd.`sale_detail_id`
        SET i.`stock` = i.`stock` + rd.`quantity`
        WHERE rd.`refund_id` = NEW.`refund_id`;

        -- Lấy thông tin phương thức thanh toán của đơn bán
        SELECT s.`customer_id`, s.`payment_method` 
        INTO target_customer_id, pay_method
        FROM `sale` s
        WHERE s.`sale_id` = NEW.`sale_id`;

        -- Nếu đơn hàng gốc được trả bằng tài khoản (Balance), cộng lại số tiền refund vào tài khoản khách
        IF pay_method = 'Balance' AND target_customer_id IS NOT NULL THEN
            UPDATE `customer`
            SET `balance` = `balance` + NEW.`refund_amount`
            WHERE `customer_id` = target_customer_id;
        END IF;
    END IF;
END$$

DELIMITER ;
