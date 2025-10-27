USE railway;

INSERT INTO discount (discount_name, discount_type, discount_value) VALUES
('No Discount', 'FLAT', 0.00),  
('Bronze 5%', 'Percentage', 5.00),  
('Silver 10%', 'Percentage', 10.00),  
('Gold 15%', 'Percentage', 15.00),  
('Platinum 20%', 'Percentage', 20.00);

INSERT INTO membership_card (membership_card_name, discount_id, recharge_threshold, is_default) VALUES
('Basic', 1, 0.00, TRUE),     
('Bronze ', 2, 100000.00, FALSE),  
('Silver ', 3, 500000.00, FALSE),    
('Gold ', 4, 1000000.00, FALSE),    
('Platinum ', 5, 2000000.00, FALSE);     

INSERT INTO customer (customer_name, phone_number, membership_card_id, balance, registration_date) VALUES
('Nguyễn Thiện Phúc', '0912345001', 2, 150000.00, '2025-02-01 09:00:00'),
('Phạm Như Thắng', '0394971667', 5, 5000000.00, '2025-01-15 11:30:00'),
('Phùng Văn Linh', '0968703132', 5, 18000000.00, '2025-03-18 14:20:00'),
('Đặng Tiến Đạt', '0909871663', 1, 50000.00, '2025-03-03 16:45:00'),
('Trần Tiến Thành', '0971600519', 2, 100000.00, '2025-04-03 18:36:00'),
('Hoàng Cát Tường', '0967357026', 2, 300000.00, '2025-04-03 19:30:00'),
('Đỗ Trí Kiên', '0332928948', 1, 90000.00, '2025-04-03 09:46:00'),
('Nguyễn Duy Anh', '0336211413', 1, 40000.00, '2025-06-03 13:39:00'),
('Nguyễn Quang Hà', '0342925648', 5, 15500000.00, '2025-04-03 22:07:00'),
('Trần Quang Chung', '0383804724', 5, 7000000.00, '2025-10-11 14:17:00');


INSERT INTO recharge_history (customer_id, amount, recharge_date) VALUES
(1, 150000.00, '2025-02-01 09:01:00'),
(2, 5000000.00, '2025-01-15 11:32:00'),
(3, 18000000.00, '2025-03-18 14:24:00'),
(4, 50000, '2025-03-03 16:48:00'),
(5, 100000,'2025-04-03 18:37:00'),
(6, 300000,'2025-04-03 19:36:00'),
(7, 90000, '2025-04-03 9:50:00'),
(8, 40000, '2025-06-03 13:40:00'),
(9, 15500000.00, '2025-04-03 22:10:00'),
(10, 7000000.00, '2025-10-11 14:19:00');
INSERT INTO account (customer_id, username, password) VALUES
(1, 'nguyenthienphuc', 'p123456'),
(2, 'thangpham', 'thang123'),
(3, 'linhpv', 'abc16589'),
(4, 'dtdat', 'def59874851'),
(5, 'thanhtt', 'ttht1004'),
(6, 'tuonghc', 'tuong3004'),
(7, 'kien36', 'kien12345'),
(8, 'duyanh', 'anh0406'),
(9, 'quangha', 'ha155000'),
(10, 'quangchung', 'chung1011');  
INSERT INTO item (item_name, item_category, price, stock, supplier_name) VALUES
('Coca-Cola 330ml', 'Đồ uống', 15000.00, 120, 'Coca-Cola Vietnam'),
('Trà sữa', 'Đồ uống', 25000.00, 80, 'Local Tea Co.'),
('Sting', 'Đồ uống', 20000.00, 60, 'Local Coffee Co.'),
('Snack Oishi', 'Đồ ăn nhanh', 10000.00, 200, 'Oishi Vietnam'),
('Nước suối Lavie', 'Đồ uống', 10000.00, 150, 'La Vie'),
('Nem chua', 'Đồ ăn nhanh', 36000.00, 180, '36food'),
('Mì xào trứng', 'Đồ ăn nhanh', 30000.00, 100, 'HOAKYFOOD'),
('Cơm chiên trứng', 'Đồ ăn nhanh', 35000.00, 100, 'HOAKYFOOD'),
('Cơm chiên xúc xích', 'Đồ ăn nhanh', 40000.00, 80, 'HOAKYFOOD'),
('Cơm chiên gà', 'Đồ ăn nhanh', 45000.00, 70, 'HOAKYFOOD'),  
('Xúc xích chiên', 'Đồ ăn nhanh', 20000.00, 150, 'HOAKYFOOD'),
('Khoai tây chiên', 'Đồ ăn nhanh', 25000.00, 120, 'HOAKYFOOD'),
('Bánh mì trứng', 'Đồ ăn nhanh', 20000.00, 100, 'HOAKYFOOD'),
('Bánh mì pate', 'Đồ ăn nhanh', 22000.00, 100, 'HOAKYFOOD'),
('Bánh bao nhân thịt', 'Đồ ăn nhanh', 15000.00, 150, 'HOAKYFOOD'),
('Cá viên chiên', 'Đồ ăn nhanh', 25000.00, 100, 'HOAKYFOOD'),  
('Bánh gạo cay', 'Đồ ăn nhanh', 30000.00, 80, 'HOAKYFOOD'),
('Mì ly Hảo Hảo', 'Đồ ăn nhanh', 12000.00, 200, 'Acecook Vietnam'),
('Xôi gà', 'Đồ ăn nhanh', 35000.00, 60, 'HOAKYFOOD'),
('Cơm tấm sườn', 'Đồ ăn nhanh', 45000.00, 50, 'HOAKYFOOD'),
('Bánh tráng trộn', 'Đồ ăn nhanh', 20000.00, 90, 'HOAKYFOOD');
INSERT INTO computer (computer_name, specifications, ip_address, price_per_hour, status) VALUES
('PC01', 'i5 / 32GB / RTX3070', '192.168.1.101', 15000.00, 'Available'),
('PC02', 'i7 / 32GB / RTX3060', '192.168.1.102', 15000.00, 'Available'),
('PC03', 'i9 / 64GB / RTX4070', '192.168.1.103', 20000.00, 'Available'),
('PC04', 'i9 / 32GB / RTX4080', '192.168.1.104', 18000.00, 'Available'),
('PC05', 'i9 / 64GB / RTX4080', '192.168.1.105', 20000.00, 'Available'),
('PC06', 'i9 / 64GB / RTX4090', '192.168.1.106', 25000.00, 'Available'),
('PC07', 'Ryzen 9 7950X / 64GB / RTX4080', '192.168.1.107', 22000.00, 'Available'),
('PC08', 'Ryzen 9 7950X / 64GB / RTX4090', '192.168.1.108', 26000.00, 'Available'),
('PC09', 'i9 / 128GB / RTX4090', '192.168.1.109', 28000.00, 'Available'),
('PC10', 'i7 / 32GB / RTX4070Ti', '192.168.1.110', 18000.00, 'Available'),  
('PC11', 'i9 / 64GB / RTX4080 Super', '192.168.1.111', 23000.00, 'Available'),
('PC12', 'Ryzen 9 7900X / 64GB / RTX4070Ti', '192.168.1.112', 20000.00, 'Available'),
('PC13', 'i9 / 64GB / RTX4090 / SSD 2TB', '192.168.1.113', 27000.00, 'Available'),
('PC14', 'Ryzen 9 7950X3D / 128GB / RTX4090', '192.168.1.114', 30000.00, 'Available'),
('PC15', 'i9 / 128GB / RTX4090Ti / SSD 2TB', '192.168.1.115', 32000.00, 'Available'),
('PC16', 'Ryzen 9 9950X / 128GB / RTX5090', '192.168.1.116', 35000.00, 'Available'),
('PC17', 'i9 / 128GB / RTX5090 / SSD 4TB', '192.168.1.117', 40000.00, 'Available'),
('PC18', 'Ryzen Threadripper 7980X / 256GB / RTX4090', '192.168.1.118', 45000.00, 'Available'),
('PC19', 'i9 / 64GB / RTX4080 / Water Cooling', '192.168.1.119', 24000.00, 'Available'),
('PC20', 'Ryzen 9 7950X / 64GB / RTX4090 / Dual Monitor', '192.168.1.120', 26000.00, 'Available');
INSERT INTO sale (customer_id, sale_date, discount_id, payment_method, note, status) VALUES
(1, '2025-03-01 09:40:00', 1, 'Cash', 'Mua nước', 'Paid'),
(2, '2025-02-15 10:00:00', 1, 'Cash', 'Mua trà sữa', 'Paid'),
(3, '2025-04-18 10:30:00', 3, 'Card', 'Sting', 'Paid'),
(4, '2025-03-03 17:00:00', 1, 'Cash', 'Mua snack', 'Paid'),
(5, '2025-04-05 14:20:00', 2, 'Cash', 'Mua mì xào trứng', 'Paid'),
(6, '2025-04-07 16:45:00', 2, 'Card', 'Mua cơm chiên gà + nước suối', 'Paid'),
(7, '2025-05-02 11:15:00', 1, 'Cash', 'Mua khoai tây chiên', 'Paid'),
(8, '2025-05-09 09:30:00', 3, 'Card', 'Mua trà sữa + snack Oishi', 'Paid'),
(9, '2025-06-01 20:45:00', 2, 'Card', 'Mua combo cơm chiên xúc xích + Coca', 'Paid'),
(10, '2025-06-15 19:00:00', 1, 'Cash', 'Mua mì ly Hảo Hảo', 'Paid'),
INSERT INTO sale_detail (sale_id, item_id, quantity) VALUES
(1, 1, 2), 
(2, 2, 1),  
(3, 3, 1),  
(4, 4, 3);

INSERT INTO sale_total (sale_id, total_amount) VALUES
(1, 30000.00),     
(2, 23750.00),      
(3, 18000.00),      
(4, 30000.00);

INSERT INTO session (customer_id, computer_id, start_time, end_time) VALUES
(1, 1, '2025-02-01 09:00:00', '2025-02-01 09:30:00'),
(2, 2, '2025-01-15 11:30:00', '2025-01-15 23:30:00'),
(3, 3, '2025-03-18 14:20:00', '2025-03-18 19:20:00'),
(4, 4, '2025-03-03 16:45:00', '2025-03-03 18:45:00');
INSERT INTO session_usage (session_id, duration_hours, remaining_hours) VALUES
(1, 0.50, 0.00),
(2, 12.00, 0.00),
(3, 3.00, 0.00),
(4, 2.00, 0.00);

INSERT INTO session_price (session_id, total_amount) VALUES
(1, 22500.00),  
(2, 30000.00),   
(3, 50000.00),   
(4, 36000.00);

INSERT INTO revenue (date, computer_usage_revenue, sales_revenue) VALUES
('2025-10-20 00:00:00', 102500.00, 291750.00),
('2025-10-21 00:00:00', 56000.00, 50000.00),
('2025-10-22 00:00:00', 109000.00, 330000.00),
('2025-10-23 00:00:00', 115000.00, 1160000.00);

INSERT INTO refund (sale_id, refund_date, refund_amount, refund_reason, refund_type, processed_by, status) VALUES
(1, '2025-10-20 10:00:00', 30000.00, 'Khách trả hàng', 'Full', 'NV_LeHai', 'Completed'),

INSERT INTO refund_detail (refund_id, sale_detail_id, quantity) VALUES
(1, 1, 2);
