use internet_cafe;

 /*--tạo triger cho việc sắp xếp sản phẩm theo tên mỗi khi xóa thêm hay cập nhật--*/
CREATE TABLE IF NOT EXISTS item_sorted_name_asc AS
SELECT * FROM item ORDER BY item_name ASC;

CREATE TABLE IF NOT EXISTS item_sorted_name_desc AS
SELECT * FROM item ORDER BY item_name DESC;

DROP TRIGGER IF EXISTS trg_item_after_insert_sort;
DROP TRIGGER IF EXISTS trg_item_after_update_sort;
DROP TRIGGER IF EXISTS trg_item_after_delete_sort;

DELIMITER $$

CREATE TRIGGER trg_item_after_insert_sort
AFTER INSERT ON item
FOR EACH ROW
BEGIN
  DELETE FROM item_sorted_name_asc;
  INSERT INTO item_sorted_name_asc
  SELECT * FROM item ORDER BY item_name ASC;

  DELETE FROM item_sorted_name_desc;
  INSERT INTO item_sorted_name_desc
  SELECT * FROM item ORDER BY item_name DESC;
END$$

CREATE TRIGGER trg_item_after_update_sort
AFTER UPDATE ON item
FOR EACH ROW
BEGIN
  DELETE FROM item_sorted_name_asc;
  INSERT INTO item_sorted_name_asc
  SELECT * FROM item ORDER BY item_name ASC;

  DELETE FROM item_sorted_name_desc;
  INSERT INTO item_sorted_name_desc
  SELECT * FROM item ORDER BY item_name DESC;
END$$

CREATE TRIGGER trg_item_after_delete_sort
AFTER DELETE ON item
FOR EACH ROW
BEGIN
  DELETE FROM item_sorted_name_asc;
  INSERT INTO item_sorted_name_asc
  SELECT * FROM item ORDER BY item_name ASC;

  DELETE FROM item_sorted_name_desc;
  INSERT INTO item_sorted_name_desc
  SELECT * FROM item ORDER BY item_name DESC;
END$$

DELIMITER ;


/*tạo trigger cho việc sắp xếp danh sách sản phẩm theo giá tăng dần và giảm dần--*/
/* --- Tạo 2 bảng phụ --- */
CREATE TABLE IF NOT EXISTS item_sorted_price_desc LIKE item;
CREATE TABLE IF NOT EXISTS item_sorted_price_asc  LIKE item;

/* Nạp dữ liệu ban đầu */
DELETE FROM item_sorted_price_desc;
INSERT INTO item_sorted_price_desc
SELECT * FROM item ORDER BY price DESC;

DELETE FROM item_sorted_price_asc;
INSERT INTO item_sorted_price_asc
SELECT * FROM item ORDER BY price ASC;

/* Xóa trigger cũ (nếu có) */
DROP TRIGGER IF EXISTS trg_item_after_insert_sort_price;
DROP TRIGGER IF EXISTS trg_item_after_update_sort_price;
DROP TRIGGER IF EXISTS trg_item_after_delete_sort_price;

DELIMITER $$

/* --- Trigger chung cho INSERT --- */
CREATE TRIGGER trg_item_after_insert_sort_price
AFTER INSERT ON item
FOR EACH ROW
BEGIN
  DELETE FROM item_sorted_price_desc;
  INSERT INTO item_sorted_price_desc
  SELECT * FROM item ORDER BY price DESC;

  DELETE FROM item_sorted_price_asc;
  INSERT INTO item_sorted_price_asc
  SELECT * FROM item ORDER BY price ASC;
END$$

/* --- Trigger chung cho UPDATE --- */
CREATE TRIGGER trg_item_after_update_sort_price
AFTER UPDATE ON item
FOR EACH ROW
BEGIN
  DELETE FROM item_sorted_price_desc;
  INSERT INTO item_sorted_price_desc
  SELECT * FROM item ORDER BY price DESC;

  DELETE FROM item_sorted_price_asc;
  INSERT INTO item_sorted_price_asc
  SELECT * FROM item ORDER BY price ASC;
END$$

/* --- Trigger chung cho DELETE --- */
CREATE TRIGGER trg_item_after_delete_sort_price
AFTER DELETE ON item
FOR EACH ROW
BEGIN
  DELETE FROM item_sorted_price_desc;
  INSERT INTO item_sorted_price_desc
  SELECT * FROM item ORDER BY price DESC;

  DELETE FROM item_sorted_price_asc;
  INSERT INTO item_sorted_price_asc
  SELECT * FROM item ORDER BY price ASC;
END$$

DELIMITER ;


/*trigger cho việc sắp xếp danh sách bán hàng theo ngày bán tăng dần và giảm dần */
-- TẠO BẢNG PHỤ LƯU DANH SÁCH ĐÃ SẮP XẾP 
CREATE TABLE IF NOT EXISTS sale_sorted_date_asc AS
SELECT * FROM sale ORDER BY sale_date ASC;

CREATE TABLE IF NOT EXISTS sale_sorted_date_desc AS
SELECT * FROM sale ORDER BY sale_date DESC;

-- XOÁ TRIGGER CŨ (VALIDATION) 
DROP TRIGGER IF EXISTS trg_sale_after_insert_sort;
DROP TRIGGER IF EXISTS trg_sale_after_update_sort;
DROP TRIGGER IF EXISTS trg_sale_after_delete_sort;

DELIMITER $$

-- TRIGGER: KHI THÊM HÓA ĐƠN 
CREATE TRIGGER trg_sale_after_insert_sort
AFTER INSERT ON sale
FOR EACH ROW
BEGIN
  -- Cập nhật bảng tăng dần (cũ → mới)
  DELETE FROM sale_sorted_date_asc;
  INSERT INTO sale_sorted_date_asc
  SELECT * FROM sale ORDER BY sale_date ASC;

  -- Cập nhật bảng giảm dần (mới → cũ)
  DELETE FROM sale_sorted_date_desc;
  INSERT INTO sale_sorted_date_desc
  SELECT * FROM sale ORDER BY sale_date DESC;
END$$
 
  -- TRIGGER: KHI CẬP NHẬT HÓA ĐƠN 
CREATE TRIGGER trg_sale_after_update_sort
AFTER UPDATE ON sale
FOR EACH ROW
BEGIN
  DELETE FROM sale_sorted_date_asc;
  INSERT INTO sale_sorted_date_asc
  SELECT * FROM sale ORDER BY sale_date ASC;

  DELETE FROM sale_sorted_date_desc;
  INSERT INTO sale_sorted_date_desc
  SELECT * FROM sale ORDER BY sale_date DESC;
END$$
 
 -- TRIGGER: KHI XOÁ HÓA ĐƠN 
CREATE TRIGGER trg_sale_after_delete_sort
AFTER DELETE ON sale
FOR EACH ROW
BEGIN
  DELETE FROM sale_sorted_date_asc;
  INSERT INTO sale_sorted_date_asc
  SELECT * FROM sale ORDER BY sale_date ASC;

  DELETE FROM sale_sorted_date_desc;
  INSERT INTO sale_sorted_date_desc
  SELECT * FROM sale ORDER BY sale_date DESC;
END$$

DELIMITER ;

/*trigger Sắp xếp danh sách bán hàng theo Tổng tiền*/
-- TẠO BẢNG PHỤ THEO TỔNG TIỀN 
CREATE TABLE IF NOT EXISTS sale_sorted_total_asc AS
SELECT 
    s.*,
    COALESCE(SUM(i.price * sd.quantity) - s.discount, 0) AS total_amount
FROM sale s
LEFT JOIN sale_detail sd ON s.sale_id = sd.sale_id
LEFT JOIN item i ON sd.item_id = i.item_id
GROUP BY s.sale_id
ORDER BY total_amount ASC;

CREATE TABLE IF NOT EXISTS sale_sorted_total_desc AS
SELECT 
    s.*,
    COALESCE(SUM(i.price * sd.quantity) - s.discount, 0) AS total_amount
FROM sale s
LEFT JOIN sale_detail sd ON s.sale_id = sd.sale_id
LEFT JOIN item i ON sd.item_id = i.item_id
GROUP BY s.sale_id
ORDER BY total_amount DESC;

-- XOÁ TRIGGER CŨ (VALIDATION) 
DROP TRIGGER IF EXISTS trg_sale_total_after_insert;
DROP TRIGGER IF EXISTS trg_sale_total_after_update;
DROP TRIGGER IF EXISTS trg_sale_total_after_delete;

DELIMITER $$

-- TRIGGER: SAU KHI THÊM HÓA ĐƠN 
CREATE TRIGGER trg_sale_total_after_insert
AFTER INSERT ON sale
FOR EACH ROW
BEGIN
  DELETE FROM sale_sorted_total_asc;
  INSERT INTO sale_sorted_total_asc
  SELECT 
      s.*,
      COALESCE(SUM(i.price * sd.quantity) - s.discount, 0) AS total_amount
  FROM sale s
  LEFT JOIN sale_detail sd ON s.sale_id = sd.sale_id
  LEFT JOIN item i ON sd.item_id = i.item_id
  GROUP BY s.sale_id
  ORDER BY total_amount ASC;

  DELETE FROM sale_sorted_total_desc;
  INSERT INTO sale_sorted_total_desc
  SELECT 
      s.*,
      COALESCE(SUM(i.price * sd.quantity) - s.discount, 0) AS total_amount
  FROM sale s
  LEFT JOIN sale_detail sd ON s.sale_id = sd.sale_id
  LEFT JOIN item i ON sd.item_id = i.item_id
  GROUP BY s.sale_id
  ORDER BY total_amount DESC;
END$$

-- TRIGGER: SAU KHI CẬP NHẬT HÓA ĐƠN 
CREATE TRIGGER trg_sale_total_after_update
AFTER UPDATE ON sale
FOR EACH ROW
BEGIN
  DELETE FROM sale_sorted_total_asc;
  INSERT INTO sale_sorted_total_asc
  SELECT 
      s.*,
      COALESCE(SUM(i.price * sd.quantity) - s.discount, 0) AS total_amount
  FROM sale s
  LEFT JOIN sale_detail sd ON s.sale_id = sd.sale_id
  LEFT JOIN item i ON sd.item_id = i.item_id
  GROUP BY s.sale_id
  ORDER BY total_amount ASC;

  DELETE FROM sale_sorted_total_desc;
  INSERT INTO sale_sorted_total_desc
  SELECT 
      s.*,
      COALESCE(SUM(i.price * sd.quantity) - s.discount, 0) AS total_amount
  FROM sale s
  LEFT JOIN sale_detail sd ON s.sale_id = sd.sale_id
  LEFT JOIN item i ON sd.item_id = i.item_id
  GROUP BY s.sale_id
  ORDER BY total_amount DESC;
END$$

-- TRIGGER: SAU KHI XOÁ HÓA ĐƠN 
CREATE TRIGGER trg_sale_total_after_delete
AFTER DELETE ON sale
FOR EACH ROW
BEGIN
  DELETE FROM sale_sorted_total_asc;
  INSERT INTO sale_sorted_total_asc
  SELECT 
      s.*,
      COALESCE(SUM(i.price * sd.quantity) - s.discount, 0) AS total_amount
  FROM sale s
  LEFT JOIN sale_detail sd ON s.sale_id = sd.sale_id
  LEFT JOIN item i ON sd.item_id = i.item_id
  GROUP BY s.sale_id
  ORDER BY total_amount ASC;

  DELETE FROM sale_sorted_total_desc;
  INSERT INTO sale_sorted_total_desc
  SELECT 
      s.*,
      COALESCE(SUM(i.price * sd.quantity) - s.discount, 0) AS total_amount
  FROM sale s
  LEFT JOIN sale_detail sd ON s.sale_id = sd.sale_id
  LEFT JOIN item i ON sd.item_id = i.item_id
  GROUP BY s.sale_id
  ORDER BY total_amount DESC;
END$$

DELIMITER ;
/*tạo trigger cho Sắp xếp chi tiết bán hàng theo Số lượng(Chi tiết bán hàng)*/
-- TẠO BẢNG PHỤ SẮP XẾP THEO SỐ LƯỢNG
CREATE TABLE IF NOT EXISTS sale_detail_sorted_qua_asc AS
SELECT * FROM sale_detail ORDER BY quantity ASC;

CREATE TABLE IF NOT EXISTS sale_detail_sorted_qua_desc AS
SELECT * FROM sale_detail ORDER BY quantity DESC;

-- XOÁ TRIGGER CŨ (VALIDATION)
DROP TRIGGER IF EXISTS trg_sale_detail_after_insert_sort;
DROP TRIGGER IF EXISTS trg_sale_detail_after_update_sort;
DROP TRIGGER IF EXISTS trg_sale_detail_after_delete_sort;

DELIMITER $$

-- TRIGGER: SAU KHI THÊM CHI TIẾT BÁN
CREATE TRIGGER trg_sale_detail_after_insert_sort
AFTER INSERT ON sale_detail
FOR EACH ROW
BEGIN
  -- Cập nhật bảng tăng dần (ít → nhiều)
  DELETE FROM sale_detail_sorted_qua_asc;
  INSERT INTO sale_detail_sorted_qua_asc
  SELECT * FROM sale_detail ORDER BY quantity ASC;

  -- Cập nhật bảng giảm dần (nhiều → ít)
  DELETE FROM sale_detail_sorted_qua_desc;
  INSERT INTO sale_detail_sorted_qua_desc
  SELECT * FROM sale_detail ORDER BY quantity DESC;
END$$

-- TRIGGER: SAU KHI CẬP NHẬT CHI TIẾT BÁN
CREATE TRIGGER trg_sale_detail_after_update_sort
AFTER UPDATE ON sale_detail
FOR EACH ROW
BEGIN
  DELETE FROM sale_detail_sorted_qua_asc;
  INSERT INTO sale_detail_sorted_qua_asc
  SELECT * FROM sale_detail ORDER BY quantity ASC;

  DELETE FROM sale_detail_sorted_qua_desc;
  INSERT INTO sale_detail_sorted_qua_desc
  SELECT * FROM sale_detail ORDER BY quantity DESC;
END$$

-- TRIGGER: SAU KHI XOÁ CHI TIẾT BÁN
CREATE TRIGGER trg_sale_detail_after_delete_sort
AFTER DELETE ON sale_detail
FOR EACH ROW
BEGIN
  DELETE FROM sale_detail_sorted_qua_asc;
  INSERT INTO sale_detail_sorted_qua_asc
  SELECT * FROM sale_detail ORDER BY quantity ASC;

  DELETE FROM sale_detail_sorted_qua_desc;
  INSERT INTO sale_detail_sorted_qua_desc
  SELECT * FROM sale_detail ORDER BY quantity DESC;
END$$

DELIMITER ;
/*tạo trigger cho Sắp xếp chi tiết bán hàng theo Thành tiền(Chi tiết bán hàng)*/
-- TẠO BẢNG PHỤ SẮP XẾP THEO THÀNH TIỀN
CREATE TABLE IF NOT EXISTS sale_detail_sorted_total_asc AS
SELECT 
    sd.*,
    (sd.quantity * i.price) AS total_amount
FROM sale_detail sd
LEFT JOIN item i ON sd.item_id = i.item_id
ORDER BY total_amount ASC;

CREATE TABLE IF NOT EXISTS sale_detail_sorted_total_desc AS
SELECT 
    sd.*,
    (sd.quantity * i.price) AS total_amount
FROM sale_detail sd
LEFT JOIN item i ON sd.item_id = i.item_id
ORDER BY total_amount DESC;

-- XOÁ TRIGGER CŨ (VALIDATION)-
DROP TRIGGER IF EXISTS trg_sale_detail_total_after_insert;
DROP TRIGGER IF EXISTS trg_sale_detail_total_after_update;
DROP TRIGGER IF EXISTS trg_sale_detail_total_after_delete;

DELIMITER $$

-- TRIGGER: SAU KHI THÊM CHI TIẾT BÁN-
CREATE TRIGGER trg_sale_detail_total_after_insert
AFTER INSERT ON sale_detail
FOR EACH ROW
BEGIN
  -- Cập nhật bảng tăng dần (thành tiền thấp → cao)
  DELETE FROM sale_detail_sorted_total_asc;
  INSERT INTO sale_detail_sorted_total_asc
  SELECT 
      sd.*,
      (sd.quantity * i.price) AS total_amount
  FROM sale_detail sd
  LEFT JOIN item i ON sd.item_id = i.item_id
  ORDER BY total_amount ASC;

  -- Cập nhật bảng giảm dần (thành tiền cao → thấp)
  DELETE FROM sale_detail_sorted_total_desc;
  INSERT INTO sale_detail_sorted_total_desc
  SELECT 
      sd.*,
      (sd.quantity * i.price) AS total_amount
  FROM sale_detail sd
  LEFT JOIN item i ON sd.item_id = i.item_id
  ORDER BY total_amount DESC;
END$$
-- TRIGGER: SAU KHI CẬP NHẬT CHI TIẾT BÁN-
CREATE TRIGGER trg_sale_detail_total_after_update
AFTER UPDATE ON sale_detail
FOR EACH ROW
BEGIN
  DELETE FROM sale_detail_sorted_total_asc;
  INSERT INTO sale_detail_sorted_total_asc
  SELECT 
      sd.*,
      (sd.quantity * i.price) AS total_amount
  FROM sale_detail sd
  LEFT JOIN item i ON sd.item_id = i.item_id
  ORDER BY total_amount ASC;

  DELETE FROM sale_detail_sorted_total_desc;
  INSERT INTO sale_detail_sorted_total_desc
  SELECT 
      sd.*,
      (sd.quantity * i.price) AS total_amount
  FROM sale_detail sd
  LEFT JOIN item i ON sd.item_id = i.item_id
  ORDER BY total_amount DESC;
END$$

-- TRIGGER: SAU KHI XOÁ CHI TIẾT BÁN-
CREATE TRIGGER trg_sale_detail_total_after_delete
AFTER DELETE ON sale_detail
FOR EACH ROW
BEGIN
  DELETE FROM sale_detail_sorted_total_asc;
  INSERT INTO sale_detail_sorted_total_asc
  SELECT 
      sd.*,
      (sd.quantity * i.price) AS total_amount
  FROM sale_detail sd
  LEFT JOIN item i ON sd.item_id = i.item_id
  ORDER BY total_amount ASC;

  DELETE FROM sale_detail_sorted_total_desc;
  INSERT INTO sale_detail_sorted_total_desc
  SELECT 
      sd.*,
      (sd.quantity * i.price) AS total_amount
  FROM sale_detail sd
  LEFT JOIN item i ON sd.item_id = i.item_id
  ORDER BY total_amount DESC;
END$$

DELIMITER ;
