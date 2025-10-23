use internet_cafe;

DELIMITER $$

CREATE TRIGGER trg_after_insert_sale_detail
AFTER INSERT ON sale_detail
FOR EACH ROW
BEGIN
    DECLARE total DECIMAL(12,2);

    -- Tính tổng tiền của hóa đơn sau khi thêm
    SELECT SUM(i.price * d.quantity)
    INTO total
    FROM sale_detail d
    JOIN item i ON d.item_id = i.item_id
    WHERE d.sale_id = NEW.sale_id;

    -- Cập nhật vào bảng sale_total
    INSERT INTO sale_total (sale_id, total_amount)
    VALUES (NEW.sale_id, total)
    ON DUPLICATE KEY UPDATE total_amount = total;
END$$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER trg_after_update_sale_detail
AFTER UPDATE ON sale_detail
FOR EACH ROW
BEGIN
    DECLARE total DECIMAL(12,2);

    SELECT SUM(i.price * d.quantity)
    INTO total
    FROM sale_detail d
    JOIN item i ON d.item_id = i.item_id
    WHERE d.sale_id = NEW.sale_id;

    UPDATE sale_total
    SET total_amount = total
    WHERE sale_id = NEW.sale_id;
END$$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER trg_after_delete_sale_detail
AFTER DELETE ON sale_detail
FOR EACH ROW
BEGIN
    DECLARE total DECIMAL(12,2);

    SELECT SUM(i.price * d.quantity)
    INTO total
    FROM sale_detail d
    JOIN item i ON d.item_id = i.item_id
    WHERE d.sale_id = OLD.sale_id;

    IF total IS NULL THEN
        DELETE FROM sale_total WHERE sale_id = OLD.sale_id;
    ELSE
        UPDATE sale_total SET total_amount = total WHERE sale_id = OLD.sale_id;
    END IF;
END$$

DELIMITER ;

