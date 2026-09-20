-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 20, 2026 at 01:02 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET
SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET
time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ciicc_db_b11bank`
--

-- --------------------------------------------------------

--
-- Table structure for table `accounts`
--

CREATE TABLE `accounts`
(
    `account_id`     int(11) NOT NULL,
    `account_number` varchar(20)    NOT NULL,
    `account_name`   varchar(100)   NOT NULL,
    `balance`        decimal(15, 2) NOT NULL DEFAULT 0.00,
    `created_at`     timestamp      NOT NULL DEFAULT current_timestamp(),
    `updated_at`     timestamp      NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `accounts`
--

INSERT INTO `accounts` (`account_id`, `account_number`, `account_name`, `balance`, `created_at`, `updated_at`)
VALUES (1, '12321', 'michael salcedo', 50000.00, '2026-09-18 07:09:12', '2026-09-18 07:10:14'),
       (2, '232', 'doniel buccat', 4500.00, '2026-09-18 07:12:32', '2026-09-19 06:46:41'),
       (3, '123456', 'michael ampo', 100000.00, '2026-09-18 09:34:07', '2026-09-19 04:18:27'),
       (4, '56712323', 'Joshua Garcia', 56000.00, '2026-09-19 04:10:22', '2026-09-19 06:46:41');

-- --------------------------------------------------------

--
-- Table structure for table `transactions`
--

CREATE TABLE `transactions`
(
    `transaction_id`   bigint(20) NOT NULL,
    `account_number`   varchar(20)    NOT NULL,
    `transaction_type` enum('DEPOSIT','WITHDRAW','TRANSFER_OUT','TRANSFER_IN') NOT NULL,
    `amount`           decimal(15, 2) NOT NULL,
    `balance_after`    decimal(15, 2) NOT NULL,
    `reference_number` varchar(50)             DEFAULT NULL,
    `remarks`          varchar(255)            DEFAULT NULL,
    `created_at`       timestamp      NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `transactions`
--

INSERT INTO `transactions` (`transaction_id`, `account_number`, `transaction_type`, `amount`, `balance_after`,
                            `reference_number`, `remarks`, `created_at`)
VALUES (1, '12321', 'DEPOSIT', 50000.00, 50000.00, 'TXN-53849353', 'Initial deposit', '2026-09-18 07:09:12'),
       (2, '12321', 'DEPOSIT', 50000.00, 100000.00, 'TXN-0FF7B80F', 'Cash deposit', '2026-09-18 07:09:40'),
       (3, '12321', 'WITHDRAW', 50000.00, 50000.00, 'TXN-F53E6D43', 'Cash withdrawal', '2026-09-18 07:10:14'),
       (4, '232', 'DEPOSIT', 2000.00, 2000.00, 'TXN-A936DD9E', 'Initial deposit', '2026-09-18 07:12:32'),
       (5, '123456', 'DEPOSIT', 50000.00, 50000.00, 'BCP-D2B88D99', 'Initial deposit', '2026-09-18 09:34:07'),
       (6, '123456', 'DEPOSIT', 2000.00, 52000.00, 'BCP-97BDFFC7', 'Cash deposit', '2026-09-18 09:39:19'),
       (7, '123456', 'DEPOSIT', 2000.00, 54000.00, 'BCP-44D8A912', 'Cash deposit', '2026-09-18 09:42:16'),
       (8, '123456', 'WITHDRAW', 4000.00, 50000.00, 'BCP-DFB191F7', 'Cash withdrawal', '2026-09-18 09:42:55'),
       (9, '123456', 'WITHDRAW', 4000.00, 46000.00, 'BCP-7E371C4F', 'Cash withdrawal', '2026-09-18 09:44:15'),
       (10, '123456', 'WITHDRAW', 4000.00, 42000.00, 'BCP-2F9A7317', 'Cash withdrawal', '2026-09-18 09:44:33'),
       (11, '123456', 'TRANSFER_OUT', 5000.00, 37000.00, 'BCP-4418B136', 'Transfer to 232', '2026-09-18 09:46:13'),
       (12, '232', 'TRANSFER_IN', 5000.00, 7000.00, 'BCP-4418B136', 'Transfer from 123456', '2026-09-18 09:46:13'),
       (13, '232', 'TRANSFER_OUT', 2000.00, 5000.00, 'BCP-92B86310', 'Transfer to 123456', '2026-09-18 09:48:16'),
       (14, '123456', 'TRANSFER_IN', 2000.00, 39000.00, 'BCP-92B86310', 'Transfer from 232', '2026-09-18 09:48:16'),
       (15, '232', 'WITHDRAW', 5000.00, 0.00, 'BCP-90CD3A68', 'Cash withdrawal', '2026-09-18 10:22:22'),
       (16, '232', 'DEPOSIT', 5000.00, 5000.00, 'BCP-3F2815E3', 'Cash deposit', '2026-09-18 10:27:31'),
       (17, '232', 'WITHDRAW', 500.00, 4500.00, 'BCP-2F73009B', 'Cash withdrawal', '2026-09-18 10:27:43'),
       (18, '232', 'WITHDRAW', 500.00, 4000.00, 'BCP-B8FDCEA4', 'Cash withdrawal', '2026-09-18 10:28:09'),
       (19, '232', 'WITHDRAW', 500.00, 3500.00, 'BCP-88AA9C29', 'Cash withdrawal', '2026-09-18 10:37:43'),
       (20, '232', 'WITHDRAW', 3000.00, 500.00, 'BCP-28540737', 'Cash withdrawal', '2026-09-18 10:38:19'),
       (21, '56712323', 'DEPOSIT', 10000.00, 10000.00, 'BCP-605B6282', 'Initial deposit', '2026-09-19 04:10:22'),
       (22, '123456', 'DEPOSIT', 61000.00, 100000.00, 'BCP-7DAD7C98', 'Cash deposit', '2026-09-19 04:18:27'),
       (23, '56712323', 'DEPOSIT', 20000.00, 30000.00, 'BCP-C9FBA0B3', 'Cash deposit', '2026-09-19 04:26:06'),
       (24, '56712323', 'DEPOSIT', 100000.00, 130000.00, 'BCP-A3DED4A5', 'Cash deposit', '2026-09-19 04:29:09'),
       (25, '56712323', 'WITHDRAW', 70000.00, 60000.00, 'BCP-437789B6', 'Cash withdrawal', '2026-09-19 04:34:59'),
       (26, '56712323', 'TRANSFER_OUT', 4000.00, 56000.00, 'BCP-778CC638', 'Transfer to 232', '2026-09-19 06:46:41'),
       (27, '232', 'TRANSFER_IN', 4000.00, 4500.00, 'BCP-778CC638', 'Transfer from 56712323', '2026-09-19 06:46:41');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `accounts`
--
ALTER TABLE `accounts`
    ADD PRIMARY KEY (`account_id`),
  ADD UNIQUE KEY `account_number` (`account_number`);

--
-- Indexes for table `transactions`
--
ALTER TABLE `transactions`
    ADD PRIMARY KEY (`transaction_id`),
  ADD KEY `idx_transactions_account_number` (`account_number`),
  ADD KEY `idx_transactions_created_at` (`created_at`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `accounts`
--
ALTER TABLE `accounts`
    MODIFY `account_id` int (11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `transactions`
--
ALTER TABLE `transactions`
    MODIFY `transaction_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `transactions`
--
ALTER TABLE `transactions`
    ADD CONSTRAINT `fk_transactions_account_number` FOREIGN KEY (`account_number`) REFERENCES `accounts` (`account_number`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
