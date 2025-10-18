use internet_cafe;

USE internet_cafe;

DELIMITER $$
CREATE TRIGGER trg_update_total_amount
AFTER UPDATE ON session
FOR EACH ROW
BEGIN
    DECLARE hourly_rate DECIMAL(10,2);
    DECLARE hourly_used DECIMAL(10,2);
    DECLARE total DECIMAL(10,2);

    -- Chỉ xử lý khi end_time được cập nhật 
    IF NEW.end_time IS NOT NULL THEN
        -- Lấy giá theo giờ của máy tính
        SELECT price_per_hour INTO hourly_rate
        FROM computer
        WHERE computer_id = NEW.computer_id;

        -- Tính số giờ
        SET hourly_used = TIMESTAMPDIFF(SECOND, NEW.start_time, NEW.end_time) / 3600;

        -- Tính tổng tiền
        SET total = ROUND(hourly_used * hourly_rate, 2);

        -- Thêm hoặc cập nhật bảng session_price
        INSERT INTO session_price (session_id, total_amount)
        VALUES (NEW.session_id, total)
        ON DUPLICATE KEY UPDATE total_amount = total;
    END IF;
END$$
DELIMITER ;

DROP TRIGGER IF EXISTS trg_update_total_amount;
