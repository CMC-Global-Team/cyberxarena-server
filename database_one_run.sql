-- ======================
-- XÓA & TẠO LẠI DATABASE
-- ======================
DROP DATABASE IF EXISTS railway;
CREATE DATABASE railway;
USE railway;

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET GLOBAL time_zone = '+07:00';
SET NAMES utf8mb4;

START TRANSACTION;

-- ======================
--  BẢNG DISCOUNT
-- ======================
CREATE TABLE discount (
  discount_id INT NOT NULL AUTO_INCREMENT,
  discount_name VARCHAR(100) NOT NULL,
  discount_type ENUM('Flat', 'Percentage') NOT NULL,
  discount_value DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (discount_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ======================
--  BẢNG MEMBERSHIP_CARD
-- ======================
CREATE TABLE membership_card (
  membership_card_id INT NOT NULL AUTO_INCREMENT,
  membership_card_name VARCHAR(100) NOT NULL,
  discount_id INT,
  recharge_threshold DECIMAL(12,2) DEFAULT 0.00, -- Ngưỡng tổng tiền nạp để đạt thẻ này
  is_default BOOLEAN DEFAULT FALSE,
  PRIMARY KEY (membership_card_id),
  CONSTRAINT fk_membership_discount FOREIGN KEY (discount_id)
    REFERENCES discount(discount_id)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- ======================
--  BẢNG CUSTOMER
-- ======================
CREATE TABLE customer (
  customer_id INT NOT NULL AUTO_INCREMENT,
  customer_name VARCHAR(100) NOT NULL,
  phone_number VARCHAR(15),
  membership_card_id INT,
  balance DECIMAL(12,2) DEFAULT 0.00,
  registration_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (customer_id),
  CONSTRAINT fk_customer_membership FOREIGN KEY (membership_card_id)
    REFERENCES membership_card(membership_card_id)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ======================
--  BẢNG RECHARGE_HISTORY (LỊCH SỬ NẠP TIỀN)
-- ======================
CREATE TABLE recharge_history (
  recharge_id INT NOT NULL AUTO_INCREMENT,
  customer_id INT NOT NULL,
  amount DECIMAL(12,2) NOT NULL,
  recharge_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (recharge_id),
  CONSTRAINT fk_recharge_customer FOREIGN KEY (customer_id)
    REFERENCES customer(customer_id)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ======================
--  BẢNG ACCOUNT
-- ======================
CREATE TABLE account (
  account_id INT NOT NULL AUTO_INCREMENT,
  customer_id INT NOT NULL,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  PRIMARY KEY (account_id),
  CONSTRAINT fk_account_customer FOREIGN KEY (customer_id)
    REFERENCES customer(customer_id)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ======================
--  BẢNG ITEM
-- ======================
CREATE TABLE item (
  item_id INT NOT NULL AUTO_INCREMENT,
  item_name VARCHAR(100) NOT NULL UNIQUE,
  item_category VARCHAR(50),
  price DECIMAL(10,2) NOT NULL,
  stock INT DEFAULT 0,
  supplier_name VARCHAR(100),
  PRIMARY KEY (item_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ======================
--  BẢNG COMPUTER
-- ======================
CREATE TABLE computer (
  computer_id INT NOT NULL AUTO_INCREMENT,
  computer_name VARCHAR(50) NOT NULL UNIQUE,
  specifications VARCHAR(200) NOT NULL,
  ip_address VARCHAR(20) NOT NULL,
  price_per_hour DECIMAL(10,2) NOT NULL,
  status ENUM('Available','In Use','Broken') DEFAULT 'Available',
  PRIMARY KEY (computer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ======================
--  BẢNG SALE
-- ======================
CREATE TABLE sale (
  sale_id INT NOT NULL AUTO_INCREMENT,
  customer_id INT NOT NULL,
  sale_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  discount_id INT,
  payment_method VARCHAR(50) NOT NULL DEFAULT 'Cash',
  note VARCHAR(200),
  status ENUM('Pending', 'Paid', 'Cancelled') DEFAULT 'Pending',
  PRIMARY KEY (sale_id),
  CONSTRAINT fk_sale_customer FOREIGN KEY (customer_id)
    REFERENCES customer(customer_id)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_sale_discount FOREIGN KEY (discount_id)
    REFERENCES discount(discount_id)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ======================
--  BẢNG SALE_TOTAL
-- ======================
CREATE TABLE sale_total (
  sale_id INT NOT NULL,
  total_amount DECIMAL(10,2),
  PRIMARY KEY (sale_id),
  CONSTRAINT fk_sale_total FOREIGN KEY (sale_id)
    REFERENCES sale(sale_id)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ======================
--  BẢNG SALE_DETAIL
-- ======================
CREATE TABLE sale_detail (
  sale_detail_id INT NOT NULL AUTO_INCREMENT,
  sale_id INT NOT NULL,
  item_id INT NOT NULL,
  quantity INT NOT NULL CHECK (quantity > 0),
  PRIMARY KEY (sale_detail_id),
  CONSTRAINT fk_detail_sale FOREIGN KEY (sale_id)
    REFERENCES sale(sale_id)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_detail_item FOREIGN KEY (item_id)
    REFERENCES item(item_id)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ======================
--  BẢNG SESSION
-- ======================
CREATE TABLE session (
  session_id INT NOT NULL AUTO_INCREMENT,
  customer_id INT NOT NULL,
  computer_id INT NOT NULL,
  start_time DATETIME NOT NULL,
  end_time DATETIME DEFAULT NULL,
  PRIMARY KEY (session_id),
  CONSTRAINT fk_session_customer FOREIGN KEY (customer_id)
    REFERENCES customer(customer_id)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_session_computer FOREIGN KEY (computer_id)
    REFERENCES computer(computer_id)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ======================
--  BẢNG SESSION_USAGE
-- ======================
CREATE TABLE session_usage (
  session_id INT NOT NULL,
  duration_hours DECIMAL(10,2) DEFAULT 0.00,
  remaining_hours DECIMAL(10,2) DEFAULT 0.00,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (session_id),
  CONSTRAINT fk_usage_session FOREIGN KEY (session_id)
    REFERENCES session(session_id)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ======================
--  BẢNG SESSION_PRICE
-- ======================
CREATE TABLE session_price (
  session_id INT NOT NULL,
  total_amount DECIMAL(10,2),
  PRIMARY KEY (session_id),
  CONSTRAINT fk_price_session FOREIGN KEY (session_id)
    REFERENCES session(session_id)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ======================
--  BẢNG REVENUE
-- ======================
CREATE TABLE revenue (
  revenue_id INT NOT NULL AUTO_INCREMENT,
  date DATETIME NOT NULL,
  computer_usage_revenue DECIMAL(10,2) NOT NULL,
  sales_revenue DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (revenue_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ======================
--  BẢNG REFUND (HOÀN TIỀN)
-- ======================
CREATE TABLE refund (
  refund_id INT NOT NULL AUTO_INCREMENT,
  sale_id INT NOT NULL,
  refund_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  refund_amount DECIMAL(10,2) NOT NULL,
  refund_reason VARCHAR(200),
  refund_type ENUM('Full', 'Partial') DEFAULT 'Full',
  processed_by VARCHAR(100), -- Nhân viên xử lý
  status ENUM('Pending', 'Approved', 'Rejected', 'Completed') DEFAULT 'Pending',
  PRIMARY KEY (refund_id),
  CONSTRAINT fk_refund_sale FOREIGN KEY (sale_id)
    REFERENCES sale(sale_id)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ======================
--  BẢNG REFUND_DETAIL (CHI TIẾT HOÀN TIỀN)
-- ======================
CREATE TABLE refund_detail (
  refund_detail_id INT NOT NULL AUTO_INCREMENT,
  refund_id INT NOT NULL,
  sale_detail_id INT NOT NULL,
  quantity INT NOT NULL CHECK (quantity > 0),
  PRIMARY KEY (refund_detail_id),
  CONSTRAINT fk_refund_detail_refund FOREIGN KEY (refund_id)
    REFERENCES refund(refund_id)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_refund_detail_sale FOREIGN KEY (sale_detail_id)
    REFERENCES sale_detail(sale_detail_id)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

COMMIT;
