-- ======================
-- TRIGGER CẬP NHẬT SỐ LƯỢNG KHO - VERSION FIXED
-- ======================

USE railway;

-- Xóa trigger cũ nếu tồn tại
DROP TRIGGER IF EXISTS update_stock_on_sale_insert;
DROP TRIGGER IF EXISTS update_stock_on_sale_update;
DROP TRIGGER IF EXISTS update_stock_on_sale_delete;
DROP TRIGGER IF EXISTS check_stock_before_sale_insert;
DROP TRIGGER IF EXISTS check_stock_before_sale_update;

DELIMITER $$

-- ======================
-- TRIGGER KIỂM TRA SỐ LƯỢNG KHO TRƯỚC KHI THÊM SẢN PHẨM
-- ======================
CREATE TRIGGER check_stock_before_sale_insert
    BEFORE INSERT ON sale_detail
    FOR EACH ROW
BEGIN
    DECLARE current_stock INT DEFAULT 0;
    
    -- Lấy số lượng hiện tại trong kho
    SELECT stock INTO current_stock 
    FROM item 
    WHERE item_id = NEW.item_id;
    
    -- Kiểm tra nếu không đủ hàng
    IF current_stock < NEW.quantity THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Khong du hang trong kho!';
    END IF;
END$$

-- ======================
-- TRIGGER KIỂM TRA SỐ LƯỢNG KHO TRƯỚC KHI CẬP NHẬT SẢN PHẨM
-- ======================
CREATE TRIGGER check_stock_before_sale_update
    BEFORE UPDATE ON sale_detail
    FOR EACH ROW
BEGIN
    DECLARE current_stock INT DEFAULT 0;
    DECLARE quantity_change INT DEFAULT NEW.quantity - OLD.quantity;
    
    -- Lấy số lượng hiện tại trong kho
    SELECT stock INTO current_stock 
    FROM item 
    WHERE item_id = NEW.item_id;
    
    -- Kiểm tra nếu không đủ hàng (chỉ khi tăng số lượng)
    IF quantity_change > 0 AND current_stock < quantity_change THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Khong du hang trong kho!';
    END IF;
END$$

-- ======================
-- TRIGGER CẬP NHẬT SỐ LƯỢNG KHO KHI THÊM SẢN PHẨM VÀO HÓA ĐƠN
-- ======================
CREATE TRIGGER update_stock_on_sale_insert
    AFTER INSERT ON sale_detail
    FOR EACH ROW
BEGIN
    -- Trừ số lượng sản phẩm trong kho
    UPDATE item 
    SET stock = stock - NEW.quantity 
    WHERE item_id = NEW.item_id;
END$$

-- ======================
-- TRIGGER CẬP NHẬT SỐ LƯỢNG KHO KHI CẬP NHẬT SẢN PHẨM TRONG HÓA ĐƠN
-- ======================
CREATE TRIGGER update_stock_on_sale_update
    AFTER UPDATE ON sale_detail
    FOR EACH ROW
BEGIN
    -- Tính toán sự thay đổi số lượng
    DECLARE quantity_change INT DEFAULT NEW.quantity - OLD.quantity;
    
    -- Cập nhật số lượng kho
    UPDATE item 
    SET stock = stock - quantity_change 
    WHERE item_id = NEW.item_id;
END$$

-- ======================
-- TRIGGER HOÀN TRẢ SỐ LƯỢNG KHO KHI XÓA SẢN PHẨM KHỎI HÓA ĐƠN
-- ======================
CREATE TRIGGER update_stock_on_sale_delete
    AFTER DELETE ON sale_detail
    FOR EACH ROW
BEGIN
    -- Hoàn trả số lượng sản phẩm vào kho
    UPDATE item 
    SET stock = stock + OLD.quantity 
    WHERE item_id = OLD.item_id;
END$$

DELIMITER ;

-- ======================
-- KIỂM TRA TRIGGER ĐÃ TẠO THÀNH CÔNG
-- ======================
SHOW TRIGGERS LIKE 'sale_detail';
