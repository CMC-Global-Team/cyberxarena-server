use railway;

DELIMITER $$

CREATE TRIGGER trg_after_insert_refund_detail
AFTER INSERT ON refund_detail
FOR EACH ROW
BEGIN
    DECLARE itemId INT;
    DECLARE returnQty INT;

    SELECT sd.item_id, NEW.quantity
    INTO itemId, returnQty
    FROM sale_detail sd
    WHERE sd.sale_detail_id = NEW.sale_detail_id;

    UPDATE item
    SET stock = stock + returnQty
    WHERE item_id = itemId;
END$$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER trg_after_update_refund_detail
AFTER UPDATE ON refund_detail
FOR EACH ROW
BEGIN
    DECLARE itemId INT;
    DECLARE diff INT;

    SELECT sd.item_id
    INTO itemId
    FROM sale_detail sd
    WHERE sd.sale_detail_id = NEW.sale_detail_id;

    SET diff = NEW.quantity - OLD.quantity;

    UPDATE item
    SET stock = stock + diff
    WHERE item_id = itemId;
END$$

DELIMITER ;

