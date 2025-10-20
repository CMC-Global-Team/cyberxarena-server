use internet_cafe;


INSERT INTO discount (discount_type, discount_value)
VALUES 
  ('Flat', 5000.00),
  ('Percentage', 10.00),
  ('Percentage', 5.00);
  delete from discount where discount_id = 0 LIMIT 3;
  select * from discount;
  
  INSERT INTO membership_card (membership_card_name, discount_id)
VALUES 
  ('Normal', NULL),
  ('Advanced', 2),  -- 10% discount
  ('VIP', 1);       -- Flat 5000 discount
  
  INSERT INTO customer (customer_name, phone_number, membership_card_id, balance, registration_date)
VALUES
  ('Nguyễn Văn Hùng', '09051234567', 2, 500000.00, '2025-10-14 09:00:00'),
  ('Trần Thị Mai', '09123456789', 1, 200000.00, '2025-10-13 14:30:00'),
  ('Lê Hoàng Anh', '09334567890', 1, 75000.00, '2025-10-12 10:15:00'),
  ('Phạm Minh Tuấn', '09876543210', 3, 1000000.00, '2025-10-11 16:00:00');
  
  INSERT INTO account (customer_id, username, password)
VALUES
  (1, 'nguyenhung', MD5('123456')),
  (2, 'tranmai', MD5('654321')),
  (3, 'lehoanganh', MD5('password')),
  (4, 'phamtuan', MD5('qwerty'));
  
  INSERT INTO item (item_name, item_category, price, stock, supplier_name)
VALUES
  ('Coca-Cola', 'Drink', 12000.00, 50, 'Công ty Nước Giải Khát'),
  ('Snack Lay’s', 'Snack', 15000.00, 40, 'Công ty Thực Phẩm Việt'),
  ('Aquafina', 'Drink', 8000.00, 60, 'Công ty Nước Tinh Khiết'),
  ('Red Bull', 'Energy Drink', 20000.00, 25, 'Công ty Năng Lượng');
  INSERT INTO sale (customer_id, sale_date, discount_id, payment_method, note)
VALUES
  (1, '2025-10-14 15:30:00', 3, 'Tiền mặt', 'Mua nước và snack'),
  (2, '2025-10-13 12:00:00', NULL, 'Chuyển khoản', 'Mua nước'),
  (3, '2025-10-12 17:45:00', 2, 'Tiền mặt', 'Mua nước tăng lực'),
  (4, '2025-10-11 14:20:00', 1, 'Chuyển khoản', 'Mua snack');
	select * from customer;
INSERT INTO sale_detail (sale_id, item_id, quantity)
VALUES
  (1, 1, 2),  -- Coca-Cola x2
  (1, 2, 1),  -- Snack Lay’s x1
  (2, 3, 3),  -- Aquafina x3
  (3, 4, 2),  -- Red Bull x2
  (4, 2, 2);  -- Snack Lay’s x2
SHOW TRIGGERS LIKE 'sale_detail';
SHOW CREATE PROCEDURE update_sale_total;
SELECT * FROM sale_total;

-- Kiểm tra dữ liệu đầu vào
SELECT * FROM sale;
SELECT * FROM sale_detail;
SELECT * FROM item;
CALL update_sale_total(1);
CALL update_sale_total(2);
CALL update_sale_total(3);

-- Kiểm tra bảng kết quả
SELECT * FROM sale_total;