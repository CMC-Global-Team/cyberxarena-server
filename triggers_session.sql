USE internet_cafe;

DELIMITER $$
DROP TRIGGER IF EXISTS trg_session_update $$
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
