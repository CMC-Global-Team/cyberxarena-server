Create Database internet_cafe;
use internet_cafe;
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
set global time_zone = '+07:00'; -- Đặt múi giờ Việt Nam


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `internet_cafe`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `customer`
--

CREATE TABLE `customer` (
  `customer_id` int NOT NULL,
  `customer_name` varchar(100) NOT NULL,
  `phone_number` varchar(15) DEFAULT NULL,
  `membership_card_id` int,
  `balance` decimal(12,2) DEFAULT 0.00,
  `registration_date` datetime DEFAULT CURRENT_TIMESTAMP()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `account`
--

CREATE TABLE `account` (
  `account_id` INT NOT NULL,
  `customer_id` INT NOT NULL,
  `username` VARCHAR(50) NOT NULL UNIQUE,
  `password` VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `membership_card`
--

CREATE TABLE membership_card (
  membership_card_id INT NOT NULL AUTO_INCREMENT,
  membership_card_name VARCHAR(100) NOT NULL,
  discount_id INT,
  PRIMARY KEY (membership_card_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sale`
--

CREATE TABLE `sale` (
  `sale_id` int NOT NULL,
  `customer_id` int NOT NULL,
  `sale_date` datetime DEFAULT current_timestamp(),	
  `discount_id` int,
  `payment_method` varchar(50) not null default 'Cash',
  `note` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sale_total`
--

CREATE TABLE `sale_total` (
  `sale_id` int NOT NULL,
  `total_amount` decimal(10,2)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `discount`
--

CREATE TABLE discount (
  discount_id INT NOT NULL AUTO_INCREMENT,
  discount_name VARCHAR(100) NOT NULL,
  discount_type ENUM('Flat', 'Percentage') NOT NULL,
  discount_value DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (discount_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sale_detail`
--

CREATE TABLE `sale_detail` (
  `sale_detail_id` int NOT NULL,
  `sale_id` int NOT NULL,
  `item_id` int NOT NULL,
  `quantity` int NOT NULL CHECK (`quantity` > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- --------------------------------------------------------
 
--
-- Cấu trúc bảng cho bảng `computer`
--

CREATE TABLE `computer` (
  `computer_id` int NOT NULL,
  `computer_name` varchar(50) NOT NULL,
  `specifications` varchar(200) NOT NULL,
  `ip_address` varchar(20) NOT NULL,
  `price_per_hour` decimal(10,2) NOT NULL,
  `status` enum('Available','In Use','Broken') DEFAULT 'Available'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `item`
--

CREATE TABLE `item` (
  `item_id` int NOT NULL,
  `item_name` varchar(100) NOT NULL,
  `item_category` varchar(50) DEFAULT NULL,
  `price` decimal(10,2) NOT NULL,
  `stock` int DEFAULT 0,
  `supplier_name` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `session`
--

CREATE TABLE `session` (
  `session_id` int NOT NULL,
  `customer_id` int NOT NULL,
  `computer_id` int NOT NULL,
  `start_time` datetime NOT NULL,
  `end_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `revenue`
--

CREATE TABLE `revenue` (
  `revenue_id` int NOT NULL,
  `date` datetime NOT NULL,
  `computer_usage_revenue` decimal(10,2) NOT NULL,
  `sales_revenue` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Cấu trúc bảng cho bảng `session_price`
--
create table session_price(
    session_id int,
    total_amount DECIMAL(10,2)
);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`customer_id`);

--
-- Chỉ mục cho bảng `membership_card_id`
--
ALTER TABLE `membership_card`
  ADD PRIMARY KEY (`membership_card_id`);
  
--
-- Chỉ mục cho bảng `account`
--
ALTER TABLE `account`
   ADD PRIMARY KEY (`account_id`);

--
-- Chỉ mục cho bảng `sale`
--
ALTER TABLE `sale`
  ADD PRIMARY KEY (`sale_id`),
  ADD KEY `customer_id` (`customer_id`);

--
-- Chỉ mục cho bảng `discount`
--
ALTER TABLE `discount`
  ADD PRIMARY KEY (`discount_id`);

--
-- Chỉ mục cho bảng `sale_total`
--
ALTER TABLE `sale_total`
  ADD PRIMARY KEY (`sale_id`);

--
-- Chỉ mục cho bảng `sale_detail`
--
ALTER TABLE `sale_detail`
  ADD PRIMARY KEY (`sale_detail_id`),
  ADD KEY `bill_id` (`sale_id`),
  ADD KEY `item_id` (`item_id`);

--
-- Chỉ mục cho bảng `computer`
--
ALTER TABLE `computer`
  ADD PRIMARY KEY (`computer_id`);

--
-- Chỉ mục cho bảng `item`
--
ALTER TABLE `item`
  ADD PRIMARY KEY (`item_id`);

--
-- Chỉ mục cho bảng `session`
--
ALTER TABLE `session`
  ADD PRIMARY KEY (`session_id`),
  ADD KEY `customer_id` (`customer_id`),
  ADD KEY `computer_id` (`computer_id`);

--
-- Chỉ mục cho bảng `revenue`
--
ALTER TABLE `revenue`
  ADD PRIMARY KEY (`revenue_id`);

--
-- Chỉ mục cho bảng `session_price`
--
ALTER TABLE `session_price`
  ADD PRIMARY KEY (`session_id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `customer`
--
ALTER TABLE `customer`
  MODIFY `customer_id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `membership_card`
--
ALTER TABLE `membership_card`
  MODIFY `membership_card_id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `account`
--
ALTER TABLE `account`
  MODIFY `account_id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `sale`
--
ALTER TABLE `sale`
  MODIFY `sale_id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `discount`
--
ALTER TABLE `discount`
  MODIFY `discount_id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `sale_detail`
--
ALTER TABLE `sale_detail`
  MODIFY `sale_detail_id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `computer`
--
ALTER TABLE `computer`
  MODIFY `computer_id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `item`
--
ALTER TABLE `item`
  MODIFY `item_id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `session`
--
ALTER TABLE `session`
  MODIFY `session_id` int NOT NULL AUTO_INCREMENT;

-- AUTO_INCREMENT cho bảng `revenue`
--
ALTER TABLE `revenue`
  MODIFY `revenue_id` int NOT NULL AUTO_INCREMENT;

--
-- Các ràng buộc cho các bảng đã đổ
--
  
--  
-- Các ràng buộc cho bảng `computer`
--
ALTER TABLE `computer`
  ADD UNIQUE (computer_name);

--  
-- Các ràng buộc cho bảng `customer`
--
ALTER TABLE `customer`
  ADD CONSTRAINT `customer_ibfk_1` FOREIGN KEY (`membership_card_id`) REFERENCES `membership_card` (`membership_card_id`);

--  
-- Các ràng buộc cho bảng `membership_card`
--
ALTER TABLE `membership_card`
  ADD CONSTRAINT `membership_card_ibfk_1` FOREIGN KEY (`discount_id`) REFERENCES `discount` (`discount_id`);

--  
-- Các ràng buộc cho bảng `account`
--
ALTER TABLE `account`
  ADD CONSTRAINT `account_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`)
    ON DELETE CASCADE ON UPDATE CASCADE;
  
--  
-- Các ràng buộc cho bảng `item`
--
ALTER TABLE `item`
  ADD UNIQUE (item_name);

--
-- Các ràng buộc cho bảng `sale`
--
ALTER TABLE `sale`
  ADD CONSTRAINT `sale_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`),
  ADD CONSTRAINT `sale_ibfk_2` FOREIGN KEY (`discount_id`) REFERENCES `discount` (`discount_id`);

--
-- Các ràng buộc cho bảng `sale_total`
--
ALTER TABLE `sale_total`
  ADD CONSTRAINT `sale_total_ibfk_1` FOREIGN KEY (`sale_id`) REFERENCES `sale` (`sale_id`);

--
-- Các ràng buộc cho bảng `sale_detail`
--
ALTER TABLE `sale_detail`
  ADD CONSTRAINT `sale_detail_ibfk_1` FOREIGN KEY (`sale_id`) REFERENCES `sale` (`sale_id`),
  ADD CONSTRAINT `sale_detail_ibfk_2` FOREIGN KEY (`item_id`) REFERENCES `item` (`item_id`);


--
-- Các ràng buộc cho bảng `session`
--
ALTER TABLE `session`
  ADD CONSTRAINT `session_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`),
  ADD CONSTRAINT `session_ibfk_2` FOREIGN KEY (`computer_id`) REFERENCES `computer` (`computer_id`);

--
-- Các ràng buộc cho bảng `session_price`
--
ALTER TABLE `session_price`
  ADD CONSTRAINT `session_price_ibfk_1` FOREIGN KEY (`session_id`) REFERENCES `session` (`session_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
