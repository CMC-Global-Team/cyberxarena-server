	use internet_cafe;


	DELIMITER $$

	CREATE TRIGGER trg_update_total_amount_and_usage
	AFTER UPDATE ON session
	FOR EACH ROW
	BEGIN
		DECLARE hourly_rate DECIMAL(10,2);
		DECLARE hourly_used DECIMAL(10,2);
		DECLARE total DECIMAL(10,2);
		DECLARE current_balance DECIMAL(10,2);
		DECLARE remaining_hours DECIMAL(10,2);

		-- Chỉ xử lý khi end_time được cập nhật 
		IF NEW.end_time IS NOT NULL THEN
			
			-- Lấy giá theo giờ của máy tính
			SELECT price_per_hour INTO hourly_rate
			FROM computer
			WHERE computer_id = NEW.computer_id;

			-- Tính số giờ đã chơi
			SET hourly_used = TIMESTAMPDIFF(SECOND, NEW.start_time, NEW.end_time) / 3600;

			-- Tính tổng tiền phải trả
			SET total = ROUND(hourly_used * hourly_rate, 2);

			-- Lấy số dư hiện tại của khách hàng
			SELECT balance INTO current_balance
			FROM customer
			WHERE customer_id = NEW.customer_id
			LIMIT 1
			FOR UPDATE;

			UPDATE customer
			SET balance = ROUND(current_balance - total, 2)
			WHERE customer_id = NEW.customer_id;

			-- Cập nhật hoặc thêm vào bảng session_price
			INSERT INTO session_price (session_id, total_amount)
			VALUES (NEW.session_id, total)
			ON DUPLICATE KEY UPDATE total_amount = total;

			-- Tính lại số giờ còn lại
			SELECT balance INTO current_balance
			FROM customer
			WHERE customer_id = NEW.customer_id
			LIMIT 1;

			SET remaining_hours = ROUND(current_balance / hourly_rate, 2);

			-- Cập nhật bảng session_usage
			INSERT INTO session_usage (session_id, duration_hours, remaining_hours)
			VALUES (NEW.session_id, hourly_used, remaining_hours)
			ON DUPLICATE KEY UPDATE
				duration_hours = hourly_used,
				remaining_hours = remaining_hours,
				updated_at = CURRENT_TIMESTAMP;
		END IF;
	END$$
	DELIMITER ;

	DROP TRIGGER IF EXISTS trg_update_total_amount_and_usage 
