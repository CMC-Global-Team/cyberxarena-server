USE railway;

INSERT INTO discount (discount_name, discount_type, discount_value) VALUES
('Premium Membership', 'Percentage', 5.00),
('Platinum Membership', 'Percentage', 10.00);

INSERT INTO membership_card (membership_card_name, discount_id, recharge_threshold, is_default) VALUES
('Member', 1, 0.00, TRUE),     
('Premium ', 2, 10000000.00, FALSE),    
('Platinum ', 3, 15000000.00, FALSE);     

INSERT INTO customer (customer_name, phone_number, membership_card_id, balance, registration_date) VALUES
('Nguyễn Thiện Phúc', '0912345001', 1, 150000.00, '2025-02-01 09:00:00'),
('Phạm Như Thắng', '0394971667', 2, 5000000.00, '2025-01-15 11:30:00'),
('Phùng Văn Linh', '0968703132', 3, 18000000.00, '2025-03-18 14:20:00'),
('Đặng Tiến Đạt', '0909871663', 1, 50000, '2025-03-03 16:45:00'),
('Trần Tiến Thành', '0971600519', 1, 100000, '2025-04-03 18:36:00'),
('Hoàng Cát Tường', '0967357026', 1, 300000, '2025-04-03 18:36:00'),

  

INSERT INTO recharge_history (customer_id, amount, recharge_date) VALUES
(1, 150000.00, '2025-02-01 09:01:00'),
(2, 5000000.00, '2025-01-15 11:32:00'),
(3, 18000000.00, '2025-03-18 14:24:00'),
(4, 50000, '2025-03-03 16:48:00');

INSERT INTO account (customer_id, username, password) VALUES
(1, 'nguyenthienphuc', 'p123456'),
(2, 'thangpham', 'thang123'),
(3, 'linhpv', 'abc16589'),
(4, 'dtdat', 'def59874851');

INSERT INTO item (item_name, item_category, price, stock, supplier_name) VALUES
('Coca-Cola 330ml', 'Đồ uống', 15000.00, 120, 'Coca-Cola Vietnam'),
('Trà sữa', 'Đồ uống', 25000.00, 80, 'Local Tea Co.'),
('Sting', 'Đồ uống', 20000.00, 60, 'Local Coffee Co.'),
('Snack Oishi', 'Đồ ăn nhanh', 10000.00, 200, 'Oishi Vietnam'),
('Nước suối Lavie', 'Đồ uống', 10000.00, 150, 'La Vie'),
('Mì Hảo Hảo', 'Đồ ăn nhanh', 12000.00, 180, 'Acecook'),
('Nem chua', 'Đồ ăn nhanh', 36000.00, 180, '36food');

INSERT INTO computer (computer_name, specifications, ip_address, price_per_hour, status) VALUES
('PC01', 'i5 / 32GB / RTX3070', '192.168.1.101', 15000.00, 'Available'),
('PC02', 'i7 / 32GB / RTX3060', '192.168.1.102', 15000.00, 'Available'),
('PC03', 'i9 / 64GB / RTX4070', '192.168.1.103', 20000.00, 'Available'),
('PC04', 'i9 / 32GB / RTX4080', '192.168.1.104', 18000.00, 'Available');

INSERT INTO sale (customer_id, sale_date, discount_id, payment_method, note, status) VALUES
(1, '2025-03-01 09:40:00', 1, 'Cash', 'Mua nước', 'Paid'),
(2, '2025-02-15 10:00:00', 1, 'Cash', 'Mua trà sữa', 'Paid'),
(3, '2025-04-18 10:30:00', 3, 'Card', 'Sting', 'Paid'),
(4, '2025-03-03 17:00:00', 1, 'Cash', 'Mua snack', 'Paid');

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
