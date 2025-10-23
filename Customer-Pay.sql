-- ======================
-- TRIGGER TỰ ĐỘNG NÂNG CẤP MEMBERSHIP CARD
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
