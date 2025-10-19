/*sắp xếp danh sách khách hàng theo tên khách hàng*/
-- TẠO BẢNG PHỤ SẮP XẾP THEO TÊN KHÁCH HÀNG 
CREATE TABLE IF NOT EXISTS customer_sorted_asc AS
SELECT * FROM customer ORDER BY customer_name ASC;

CREATE TABLE IF NOT EXISTS customer_sorted_desc AS
SELECT * FROM customer ORDER BY customer_name DESC;

-- XOÁ TRIGGER CŨ (VALIDATION)-
DROP TRIGGER IF EXISTS trg_customer_after_insert_sort;
DROP TRIGGER IF EXISTS trg_customer_after_update_sort;
DROP TRIGGER IF EXISTS trg_customer_after_delete_sort;

DELIMITER $$

-- TRIGGER: SAU KHI THÊM KHÁCH HÀNG--
CREATE TRIGGER trg_customer_after_insert_sort
AFTER INSERT ON customer
FOR EACH ROW
BEGIN
  -- Cập nhật bảng tăng dần (A → Z)
  DELETE FROM customer_sorted_asc;
  INSERT INTO customer_sorted_asc
  SELECT * FROM customer ORDER BY customer_name ASC;

  -- Cập nhật bảng giảm dần (Z → A)
  DELETE FROM customer_sorted_desc;
  INSERT INTO customer_sorted_desc
  SELECT * FROM customer ORDER BY customer_name DESC;
END$$
-- TRIGGER: SAU KHI CẬP NHẬT KHÁCH HÀNG--
CREATE TRIGGER trg_customer_after_update_sort
AFTER UPDATE ON customer
FOR EACH ROW
BEGIN
  DELETE FROM customer_sorted_asc;
  INSERT INTO customer_sorted_asc
  SELECT * FROM customer ORDER BY customer_name ASC;

  DELETE FROM customer_sorted_desc;
  INSERT INTO customer_sorted_desc
  SELECT * FROM customer ORDER BY customer_name DESC;
END$$

-- TRIGGER: SAU KHI XOÁ KHÁCH HÀNG-
CREATE TRIGGER trg_customer_after_delete_sort
AFTER DELETE ON customer
FOR EACH ROW
BEGIN
  DELETE FROM customer_sorted_asc;
  INSERT INTO customer_sorted_asc
  SELECT * FROM customer ORDER BY customer_name ASC;

  DELETE FROM customer_sorted_desc;
  INSERT INTO customer_sorted_desc
  SELECT * FROM customer ORDER BY customer_name DESC;
END$$

DELIMITER ;


/*  SẮP XẾP THEO NGÀY ĐĂNG KÝ  */

CREATE TABLE IF NOT EXISTS customer_sorted_asc AS
SELECT * FROM customer ORDER BY register_date ASC;

CREATE TABLE IF NOT EXISTS customer_sorted_desc AS
SELECT * FROM customer ORDER BY register_date DESC;

DROP TRIGGER IF EXISTS trg_customer_after_insert_sort;
DROP TRIGGER IF EXISTS trg_customer_after_update_sort;
DROP TRIGGER IF EXISTS trg_customer_after_delete_sort;

DELIMITER $$

  --TRIGGER: SAU KHI THÊM KHÁCH HÀNG--
CREATE TRIGGER trg_customer_after_insert_sort
AFTER INSERT ON customer
FOR EACH ROW
BEGIN
  -- Bảng tăng dần (cũ → mới)
  DELETE FROM customer_sorted_asc;
  INSERT INTO customer_sorted_asc
  SELECT * FROM customer ORDER BY register_date ASC;

  -- Bảng giảm dần (mới → cũ)
  DELETE FROM customer_sorted_desc;
  INSERT INTO customer_sorted_desc
  SELECT * FROM customer ORDER BY register_date DESC;
END$$

-- TRIGGER: SAU KHI CẬP NHẬT KHÁCH HÀNG--
CREATE TRIGGER trg_customer_after_update_sort
AFTER UPDATE ON customer
FOR EACH ROW
BEGIN
  DELETE FROM customer_sorted_asc;
  INSERT INTO customer_sorted_asc
  SELECT * FROM customer ORDER BY register_date ASC;

  DELETE FROM customer_sorted_desc;
  INSERT INTO customer_sorted_desc
  SELECT * FROM customer ORDER BY register_date DESC;
END$$

-- TRIGGER: SAU KHI XOÁ KHÁCH HÀNG-
CREATE TRIGGER trg_customer_after_delete_sort
AFTER DELETE ON customer
FOR EACH ROW
BEGIN
  DELETE FROM customer_sorted_asc;
  INSERT INTO customer_sorted_asc
  SELECT * FROM customer ORDER BY register_date ASC;

  DELETE FROM customer_sorted_desc;
  INSERT INTO customer_sorted_desc
  SELECT * FROM customer ORDER BY register_date DESC;
END$$

DELIMITER ;

