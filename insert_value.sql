use internet_cafe;
-- Insert 10 sample records into `account`
INSERT INTO `customer` (customer_name, phone_number, membership_card, balance, registration_date)
VALUES
    ('Nguyễn Văn Hùng', '09051234567', 'Advanced', 500000.00, '2025-10-14 09:00:00'),
    ('Trần Thị Mai', '09123456789', 'Normal', 200000.00, '2025-10-13 14:30:00'),
    ('Lê Hoàng Anh', '09334567890', 'Normal', 75000.00, '2025-10-12 10:15:00'),
    ('Phạm Minh Tuấn', '09876543210', 'VIP', 1000000.00, '2025-10-11 16:00:00'),
    ('Đỗ Thị Hồng Ngọc', '09412345678', 'Advanced', 300000.00, '2025-10-10 08:45:00'),
    ('Vũ Quốc Đạt', '09787654321', 'Normal', 0.00, '2025-10-14 15:00:00'),
	('Hoàng Thị Lan', '09267890123', 'Normal', 150000.00, '2025-10-14 17:00:00'),
    ('Ngô Minh Đức', '09543210987', 'Advanced', 400000.00, '2025-10-13 09:30:00'),
    ('Bùi Văn Nam', '09678901234', 'Normal', 80000.00, '2025-10-12 11:45:00'),
    ('Đặng Thị Thanh', '09321098765', 'Advanced', 250000.00, '2025-10-11 14:20:00');

select * from customer;
INSERT INTO account (customer_id, username, password)
VALUES
(1, 'nguyenhung', MD5('123456')),
(2, 'tranmai', MD5('654321')),
(3, 'lehoanganh', MD5('password')),
(4, 'phamtuan', MD5('qwerty')),
(5, 'hongngoc', MD5('abc123')),
(6, 'vuquocdat', MD5('123abc')),
(7, 'hoanglan', MD5('lan789')),
(8, 'ngominhduc', MD5('duc123')),
(9, 'buinam', MD5('nam456')),
(10, 'dangthithanh', MD5('thanh999'));

    -- Insert 10 sample records into `item`
INSERT INTO `item` (item_name, item_category, price, stock, supplier_name)
VALUES
    ('Nước ngọt Coca-Cola', 'Drink', 12000.00, 50, 'Công ty TNHH Nước Giải Khát'),
    ('Trà xanh C2', 'Drink', 10000.00, 30, 'Công ty TNHH Nước Giải Khát'),
    ('Bánh snack Lay’s', 'Snack', 15000.00, 40, 'Công ty Thực Phẩm Việt'),
    ('Nước suối Aquafina', 'Drink', 8000.00, 60, 'Công ty Nước Uống Tinh Khiết'),
    ('Red Bull', 'Energy Drink', 20000.00, 25, 'Công ty Năng Lượng Quốc Tế'),
    ('Mì gói Hảo Hảo', 'Food', 7000.00, 100, 'Công ty Mì Ăn Liền'),
    ('Trà sữa trân châu', 'Drink', 25000.00, 20, 'Công ty TNHH Đồ Uống Việt'),
    ('Bánh mì pate', 'Food', 20000.00, 15, 'Công ty Thực Phẩm Việt'),
    ('Sting dâu', 'Energy Drink', 15000.00, 30, 'Công ty Năng Lượng Quốc Tế'),
    ('Kem ốc quế', 'Dessert', 10000.00, 25, 'Công ty Kem');
-- Insert 10 sample records into `computer`
INSERT INTO `computer` (computer_name, specifications, ip_address, price_per_hour, status)
VALUES
    ('PC01', 'Standard', '192.168.1.101', 12000.00, 'Available'),
    ('PC02', 'Standard', '192.168.1.102', 12000.00, 'In Use'),
    ('PC03', 'Esport Training', '192.168.1.103', 12000.00, 'Available'),
    ('PC04', 'Esport Training', '192.168.1.104', 12000.00, 'In use'),
    ('PC05', 'Esport Pro', '192.168.1.105', 15000.00, 'In Use'),
    ('PC06', 'Esport Pro', '192.168.1.106', 15000.00, 'Available'),
    ('PC07', 'Stream', '192.168.1.107', 25000.00, 'In Use'),
    ('PC08', 'Stream', '192.168.1.108', 25000.00, 'Available'),
    ('PC09', 'Esport Competitive', '192.168.1.109', 18000.00, 'Broken'),
    ('PC10', 'Esport Competitive', '192.168.1.110', 18000.00, 'Available');
select * from computer;
-- Insert 10 sample records into `sale`
INSERT INTO `sale` (customer_id, sale_date, discount_type, discount, payment_method, note)
VALUES
    (1, '2025-10-14 15:30:00', 'Flat', 5000.00, 'Tiền mặt', ''),
    (2, '2025-10-14 12:00:00', 'Flat' , 0.00,'Tiền mặt', 'Snack purchase'),
    (3, '2025-10-13 17:45:00', 'Percentage','5.00', 'Chuyển khoản', 'Gaming session and energy drink'),
    (4, '2025-10-12 14:20:00', 'Percentage',50.00, 'Chuyển khoản', 'Extended gaming session'),
    (5, '2025-10-11 10:10:00', 'Flat', 0.00, 'Tiền mặt', 'Bought water and snacks'),
    (8, '2025-10-14 16:00:00', 'Flat',3000.00, 'Chuyển khoản', 'VIP gaming session'),
	(7, '2025-10-14 16:45:00', 'Flat',3000.00, 'Tiền mặt', 'Payment for snacks and gaming'),
    (8, '2025-10-14 10:15:00', 'Flat' ,0.00, 'Chuyển khoản', 'Drink purchase'),
    (9, '2025-10-13 15:30:00', 'Percentage', 10.00, 'Tiền mặt', 'Gaming session and food'),
    (10, '2025-10-12 12:00:00', 'Percentage' , 10.00, 'Chuyển khoản', 'Extended gaming and drinks');
SHOW TRIGGERS LIKE 'sale%';

-- Insert 10 sample records into `billdetail`
INSERT INTO `sale_detail` (sale_id, item_id, quantity)
VALUES
    (1, 1, 2),
    (2, 2, 1),
    (3, 3, 3),
    (4, 1, 2),
    (5, 4, 1),
    (6, 5, 2),
	(7, 6, 3),
    (8, 1, 1),
    (9, 2, 2),
    (10, 4, 2);

-- Insert 10 sample records into `session`
INSERT INTO session (customer_id, computer_id, start_time, end_time)
VALUES
(1, 1, '2025-10-14 14:00:00', '2025-10-14 16:00:00'),
(2, 2, '2025-10-14 11:30:00', '2025-10-14 12:45:00'),
(3, 3, '2025-10-13 16:00:00', '2025-10-13 19:00:00'),
(4, 5, '2025-10-12 13:00:00', '2025-10-12 15:00:00'),
(5, 1, '2025-10-11 09:00:00', '2025-10-11 10:30:00'),
(6, 4, '2025-10-14 15:00:00', '2025-10-14 16:30:00'),
(7, 7, '2025-10-14 15:30:00', '2025-10-14 17:00:00'),
(8, 8, '2025-10-14 09:00:00', '2025-10-14 11:00:00'),
(9, 5, '2025-10-13 14:00:00', '2025-10-13 16:30:00'),
(10, 1, '2025-10-12 10:00:00', '2025-10-12 13:00:00');

select * from session_price;

UPDATE session
SET end_time = '2025-10-14 16:00:00'
WHERE session_id = 1;

UPDATE session
SET end_time = '2025-10-14 12:45:00'
WHERE session_id = 2;

UPDATE session
SET end_time = '2025-10-13 19:00:00'
WHERE session_id = 3;

UPDATE session
SET end_time = '2025-10-12 15:00:00'
WHERE session_id = 4;

UPDATE session
SET end_time = '2025-10-11 10:30:00'
WHERE session_id = 5;
 
