use internet_cafe;

 /*--tạo triger cho việc sắp xếp sản phẩm theo tên mỗi khi xóa thêm hay cập nhật--*/
-- Tạo bảng sắp xếp sản phẩm
CREATE TABLE IF NOT EXISTS item_sorted AS
SELECT * FROM item ORDER BY item_name ASC;

-- Xóa trigger cũ nếu tồn tại
DROP TRIGGER IF EXISTS trg_item_after_insert;
DROP TRIGGER IF EXISTS trg_item_after_update;
DROP TRIGGER IF EXISTS trg_item_after_delete;

DELIMITER $$

-- Trigger: sau khi thêm sản phẩm
CREATE TRIGGER trg_item_after_insert
AFTER INSERT ON item
FOR EACH ROW
BEGIN
  DELETE FROM item_sorted;
  INSERT INTO item_sorted
  SELECT * FROM item ORDER BY item_name ASC;
END$$

-- Trigger: sau khi cập nhật sản phẩm
CREATE TRIGGER trg_item_after_update
AFTER UPDATE ON item
FOR EACH ROW
BEGIN
  DELETE FROM item_sorted;
  INSERT INTO item_sorted
  SELECT * FROM item ORDER BY item_name ASC;
END$$

--  Trigger: sau khi xóa sản phẩm
CREATE TRIGGER trg_item_after_delete
AFTER DELETE ON item
FOR EACH ROW
BEGIN
  DELETE FROM item_sorted;
  INSERT INTO item_sorted
  SELECT * FROM item ORDER BY item_name ASC;
END$$

DELIMITER ;


/*tạo trigger cho việc sắp xếp danh sách sản phẩm theo giá--*/
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
