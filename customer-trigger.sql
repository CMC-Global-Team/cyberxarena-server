 -- TẠO BẢNG PHỤ SẮP XẾP THEO TÊN KHÁCH HÀNG
CREATE TABLE IF NOT EXISTS customer_sorted AS
SELECT * FROM customer ORDER BY customer_name ASC;

-- XOÁ TRIGGER CŨ (VALIDATION)
DROP TRIGGER IF EXISTS trg_customer_after_insert_sort;
DROP TRIGGER IF EXISTS trg_customer_after_update_sort;
DROP TRIGGER IF EXISTS trg_customer_after_delete_sort;

DELIMITER $$
-- TRIGGER: SAU KHI THÊM KHÁCH HÀNG
CREATE TRIGGER trg_customer_after_insert_sort
AFTER INSERT ON customer
FOR EACH ROW
BEGIN
  -- Cập nhật lại bảng phụ sắp xếp tăng dần theo tên
  DELETE FROM customer_sorted;
  INSERT INTO customer_sorted
  SELECT * FROM customer ORDER BY customer_name ASC;
END$$

-- TRIGGER: SAU KHI CẬP NHẬT KHÁCH HÀNG
CREATE TRIGGER trg_customer_after_update_sort
AFTER UPDATE ON customer
FOR EACH ROW
BEGIN
  DELETE FROM customer_sorted;
  INSERT INTO customer_sorted
  SELECT * FROM customer ORDER BY customer_name ASC;
END$$

-- TRIGGER: SAU KHI XOÁ KHÁCH HÀNG
CREATE TRIGGER trg_customer_after_delete_sort
AFTER DELETE ON customer
FOR EACH ROW
BEGIN
  DELETE FROM customer_sorted;
  INSERT INTO customer_sorted
  SELECT * FROM customer ORDER BY customer_name ASC;
END$$

DELIMITER ;
