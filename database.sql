-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th10 13, 2025 lúc 03:47 PM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `netgame`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `account`
--

CREATE TABLE `account` (
  `account_id` int(11) NOT NULL,
  `customer_name` varchar(100) NOT NULL,
  `phone_number` varchar(15) DEFAULT NULL,
  `membership_card` varchar(50) DEFAULT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `balance` decimal(12,2) DEFAULT 0.00,
  `registration_date` date DEFAULT curdate(),
  `status` enum('Active','Inactive') DEFAULT 'Active'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `bill`
--

CREATE TABLE `bill` (
  `bill_id` int(11) NOT NULL,
  `account_id` int(11) NOT NULL,
  `sale_date` datetime DEFAULT current_timestamp(),
  `discount` decimal(10,2) DEFAULT 0.00,
  `total` decimal(12,2) DEFAULT 0.00,
  `payment_method` enum('Cash','Bank transfer') DEFAULT 'Available',
  `note` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `billdetail`
--

CREATE TABLE `billdetail` (
  `bill_detail_id` int(11) NOT NULL,
  `bill_id` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL CHECK (`quantity` > 0)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `computer`
--

CREATE TABLE `computer` (
  `computer_id` int(11) NOT NULL,
  `computer_name` varchar(50) NOT NULL,
  `specifications` varchar(200) DEFAULT NULL,
  `ip_address` varchar(20) DEFAULT NULL,
  `price_per_hour` decimal(10,2) NOT NULL,
  `status` enum('Available','In Use','Broken') DEFAULT 'Available'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `item`
--

CREATE TABLE `item` (
  `item_id` int(11) NOT NULL,
  `item_name` varchar(100) NOT NULL,
  `item_category` varchar(50) DEFAULT NULL,
  `price` decimal(10,2) NOT NULL,
  `stock` int(11) DEFAULT 0,
  `supplier_name` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `session`
--

CREATE TABLE `session` (
  `session_id` int(11) NOT NULL,
  `account_id` int(11) NOT NULL,
  `computer_id` int(11) NOT NULL,
  `start_time` datetime NOT NULL,
  `end_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `account`
--
ALTER TABLE `account`
  ADD PRIMARY KEY (`account_id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Chỉ mục cho bảng `bill`
--
ALTER TABLE `bill`
  ADD PRIMARY KEY (`bill_id`),
  ADD KEY `account_id` (`account_id`);

--
-- Chỉ mục cho bảng `billdetail`
--
ALTER TABLE `billdetail`
  ADD PRIMARY KEY (`bill_detail_id`),
  ADD KEY `bill_id` (`bill_id`),
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
  ADD KEY `account_id` (`account_id`),
  ADD KEY `computer_id` (`computer_id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `account`
--
ALTER TABLE `account`
  MODIFY `account_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `bill`
--
ALTER TABLE `bill`
  MODIFY `bill_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `billdetail`
--
ALTER TABLE `billdetail`
  MODIFY `bill_detail_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `computer`
--
ALTER TABLE `computer`
  MODIFY `computer_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `item`
--
ALTER TABLE `item`
  MODIFY `item_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `session`
--
ALTER TABLE `session`
  MODIFY `session_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `bill`
--
ALTER TABLE `bill`
  ADD CONSTRAINT `bill_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`);

--
-- Các ràng buộc cho bảng `billdetail`
--
ALTER TABLE `billdetail`
  ADD CONSTRAINT `billdetail_ibfk_1` FOREIGN KEY (`bill_id`) REFERENCES `bill` (`bill_id`),
  ADD CONSTRAINT `billdetail_ibfk_2` FOREIGN KEY (`item_id`) REFERENCES `item` (`item_id`);

--
-- Các ràng buộc cho bảng `session`
--
ALTER TABLE `session`
  ADD CONSTRAINT `session_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`),
  ADD CONSTRAINT `session_ibfk_2` FOREIGN KEY (`computer_id`) REFERENCES `computer` (`computer_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
