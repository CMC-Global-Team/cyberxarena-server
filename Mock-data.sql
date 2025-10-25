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
