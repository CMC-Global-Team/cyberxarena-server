use internet_cafe;

 --tạo triger cho việc sắp xếp sản phẩm theo tên mỗi khi xóa thêm hay cập nhật--
CREATE TABLE item_sorted AS
SELECT * FROM item ORDER BY item_name ASC;
CREATE TRIGGER trg_item_after_insert
AFTER INSERT ON item
FOR EACH ROW
BEGIN
  DELETE FROM item_sorted;
  INSERT INTO item_sorted
  SELECT * FROM item ORDER BY item_name ASC;
END;
CREATE TRIGGER trg_item_after_update
AFTER UPDATE ON item
FOR EACH ROW
BEGIN
  DELETE FROM item_sorted;
  INSERT INTO item_sorted
  SELECT * FROM item ORDER BY item_name ASC;
END;
CREATE TRIGGER trg_item_after_delete
AFTER DELETE ON item
FOR EACH ROW
BEGIN
  DELETE FROM item_sorted;
  INSERT INTO item_sorted
  SELECT * FROM item ORDER BY item_name ASC;
END;

--tạo trigger cho việc sắp xếp danh sách sản phẩm theo giá--
