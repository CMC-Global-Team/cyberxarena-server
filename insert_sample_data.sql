use internet_cafe;
-- Insert 10 sample records into `account`
INSERT INTO `account` (customer_name, phone_number, membership_card, username, password, balance, registration_date, status)
VALUES
    ('Nguyễn Văn Hùng', '09051234567', 'Advanced', 'nguyenvanhung', 'hashed_password_123', 500000.00, '2025-10-14 09:00:00', 'Active'),
    ('Trần Thị Mai', '09123456789', 'Normal', 'tranthimai', 'hashed_password_456', 200000.00, '2025-10-13 14:30:00', 'Active'),
    ('Lê Hoàng Anh', '09334567890', 'Normal', 'lehoanganh', 'hashed_password_789', 75000.00, '2025-10-12 10:15:00', 'Active'),
    ('Phạm Minh Tuấn', '09876543210', 'VIP', 'phamminhtuan', 'hashed_password_101', 1000000.00, '2025-10-11 16:00:00', 'Inactive'),
    ('Đỗ Thị Hồng Ngọc', '09412345678', 'Advanced', 'dothihongngoc', 'hashed_password_234', 300000.00, '2025-10-10 08:45:00', 'Active'),
    ('Vũ Quốc Đạt', '09787654321', 'Normal', 'vuquocdat', 'hashed_password_567', 0.00, '2025-10-14 15:00:00', 'Active'),
	('Hoàng Thị Lan', '09267890123', 'Normal', 'hoangthilan', 'hashed_password_890', 150000.00, '2025-10-14 17:00:00', 'Active'),
    ('Ngô Minh Đức', '09543210987', 'Advanced', 'ngominhduc', 'hashed_password_901', 400000.00, '2025-10-13 09:30:00', 'Active'),
    ('Bùi Văn Nam', '09678901234', 'Normal', 'buivannam', 'hashed_password_012', 80000.00, '2025-10-12 11:45:00', 'Inactive'),
    ('Đặng Thị Thanh', '09321098765', 'Advanced', 'dangthithanh', 'hashed_password_345', 250000.00, '2025-10-11 14:20:00', 'Active');

-- Insert 10 sample records into `bill`
INSERT INTO `bill` (account_id, sale_date, discount, total, payment_method, note)
VALUES
    (1, '2025-10-14 15:30:00', 5000.00, 45000.00, 'Tiền mặt', ''),
    (2, '2025-10-14 12:00:00', 0.00, 30000.00, 'Tiền mặt', 'Snack purchase'),
    (3, '2025-10-13 17:45:00', 2000.00, 58000.00, 'Chuyển khoản', 'Gaming session and energy drink'),
    (4, '2025-10-12 14:20:00', 10000.00, 90000.00, 'Chuyển khoản', 'Extended gaming session'),
    (5, '2025-10-11 10:10:00', 0.00, 25000.00, 'Tiền mặt', 'Bought water and snacks'),
    (6, '2025-10-14 16:00:00', 3000.00, 67000.00, 'Chuyển khoản', 'VIP gaming session'),
	(7, '2025-10-14 16:45:00', 3000.00, 52000.00, 'Tiền mặt', 'Payment for snacks and gaming'),
    (8, '2025-10-14 10:15:00', 0.00, 35000.00, 'Chuyển khoản', 'Drink purchase'),
    (9, '2025-10-13 15:30:00', 1000.00, 60000.00, 'Tiền mặt', 'Gaming session and food'),
    (10, '2025-10-12 12:00:00', 2000.00, 78000.00, 'Chuyển khoản', 'Extended gaming and drinks');

-- Insert 6 sample records into `billdetail`
INSERT INTO `billdetail` (bill_id, item_id, quantity)
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

-- Insert 10 sample records into `session`
INSERT INTO `session` (account_id, computer_id, start_time, end_time)
VALUES
    (1, 1, '2025-10-14 14:00:00', '2025-10-14 16:00:00'),
    (2, 2, '2025-10-14 11:30:00', NULL),
    (3, 3, '2025-10-13 16:00:00', '2025-10-13 19:00:00'),
    (4, 5, '2025-10-12 13:00:00', NULL),
    (5, 1, '2025-10-11 09:00:00', '2025-10-11 10:30:00'),
    (6, 4, '2025-10-14 15:00:00', NULL),
    (7, 7, '2025-10-14 15:30:00', NULL),
    (8, 8, '2025-10-14 09:00:00', '2025-10-14 11:00:00'),
    (9, 5, '2025-10-13 14:00:00', '2025-10-13 16:30:00'),
    (10, 1, '2025-10-12 10:00:00', '2025-10-12 13:00:00');