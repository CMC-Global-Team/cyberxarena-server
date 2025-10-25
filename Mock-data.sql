USE railway;

INSERT INTO discount (discount_name, discount_type, discount_value) VALUES
('Premium Membership', 'Percentage', 5.00),
('Platinum Membership', 'Percentage', 10.00),

INSERT INTO membership_card (membership_card_name, discount_id, recharge_threshold, is_default) VALUES
('Member', 1, 0.00, TRUE),     
('Premium ', 2, 10000000.00, FALSE),    
  

