-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 03, 2025 at 08:34 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.0.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `defaultdb`
--

-- --------------------------------------------------------

--
-- Table structure for table `bmi`
--

CREATE TABLE `bmi`
(
    `id`             int(11) NOT NULL,
    `weight`         double      DEFAULT NULL,
    `recorded_date`  date        DEFAULT NULL,
    `fk_customer_id` int(11)     DEFAULT NULL,
    `recorded_month` varchar(20) DEFAULT NULL,
    `height`         double      DEFAULT NULL,
    `bmi_value`      double      DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;


-- --------------------------------------------------------

--
-- Table structure for table `customers`
--

CREATE TABLE `customers`
(
    `id`             int(11)                NOT NULL,
    `first_name`     varchar(50)            NOT NULL,
    `last_name`      varchar(50)            NOT NULL,
    `email`          varchar(100)           NOT NULL,
    `phone_number`   varchar(20)  DEFAULT NULL,
    `password`       varchar(255)           NOT NULL,
    `username`       varchar(50)            NOT NULL,
    `gender`         enum ('MALE','FEMALE') NOT NULL,
    `weight`         varchar(10)  DEFAULT NULL,
    `dob`            date         DEFAULT NULL,
    `monthly_plan`   int(11)      DEFAULT NULL,
    `cin`            varchar(20)            NOT NULL,
    `is_active`      tinyint(1)   DEFAULT 1,
    `address`        varchar(255) DEFAULT NULL,
    `current_status` tinyint(1)   DEFAULT 1
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;


-- --------------------------------------------------------

--
-- Table structure for table `employees`
--

CREATE TABLE `employees`
(
    `id`             int(11)                NOT NULL,
    `first_name`     varchar(50)            NOT NULL,
    `last_name`      varchar(50)            NOT NULL,
    `designation`    VARCHAR(50)            NOT NULL,
    `cin_number`     varchar(20)            NOT NULL,
    `salary`         int(11)     DEFAULT NULL,
    `gender`         enum ('MALE','FEMALE') NOT NULL,
    `phone_number`   varchar(20) DEFAULT NULL,
    `joining_date`   date        DEFAULT NULL,
    `username`       varchar(50)            NOT NULL,
    `password`       varchar(255)           NOT NULL,
    `access`         int(11)     DEFAULT NULL,
    `email`          varchar(100)           NOT NULL,
    `current_status` tinyint(1)  DEFAULT 1
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;


--
-- Table structure for table `expenses`
--

CREATE TABLE `expenses`
(
    `id`             int(11) NOT NULL,
    `description`    text        DEFAULT NULL,
    `created_date`   date        DEFAULT NULL,
    `amount`         int(11)     DEFAULT NULL,
    `month`          varchar(20) DEFAULT NULL,
    `year`           varchar(4)  DEFAULT NULL,
    `fk_employee_id` int(11)     DEFAULT NULL,
    `selected_date`  date        DEFAULT NULL,
    `current_status` tinyint(1)  DEFAULT 1
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
-- --------------------------------------------------------

--
-- Table structure for table `packages`
--

CREATE TABLE `packages`
(
    `id`          int(11)      NOT NULL,
    `title`       varchar(100) NOT NULL,
    `amount`      int(11)      NOT NULL,
    `description` text DEFAULT NULL,
    `PACKAGE_NO`  int(11)      NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

--
-- Dumping data for table `packages`
--

INSERT INTO `packages` (`id`, `title`, `amount`, `description`, `PACKAGE_NO`)
VALUES (1, 'Beginner Plan', 50, 'Suitable for beginners with basic access.', 1),
       (2, 'Starter Plan', 70, 'Ideal for those who want to get started.', 2),
       (3, 'Advanced Plan', 100, 'For advanced users with full access.', 3);

-- --------------------------------------------------------

--
-- Table structure for table `queries`
--

CREATE TABLE `queries`
(
    `id`             int(11) NOT NULL,
    `heading`        varchar(255) DEFAULT NULL,
    `email`          varchar(100) DEFAULT NULL,
    `description`    text         DEFAULT NULL,
    `created_date`   date         DEFAULT NULL,
    `username`       varchar(50)  DEFAULT NULL,
    `reply`          text         DEFAULT NULL,
    `status`         tinyint(1)   DEFAULT 0,
    `current_status` tinyint(1)   DEFAULT 1
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

--
-- Dumping data for table `queries`
--

INSERT INTO `queries` (`id`, `heading`, `email`, `description`, `created_date`, `username`, `reply`, `status`,
                       `current_status`)
VALUES (1, 'Membership Inquiry', 'alice.brown@example.com', 'I would like to know more about the Starter Plan.',
        '2025-01-15', 'alicebrown', NULL, 0, 1),
       (2, 'Feedback', 'bob.johnson@example.com', 'Great facilities and trainers!', '2025-01-16', 'bobjohnson', NULL, 0,
        1);

-- --------------------------------------------------------

--
-- Table structure for table `revenues`
--

CREATE TABLE `revenues`
(
    `id`           int(11) NOT NULL,
    `for_month`    varchar(20) DEFAULT NULL,
    `for_year`     varchar(4)  DEFAULT NULL,
    `updated_date` date        DEFAULT NULL,
    `amount`       int(11)     DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

--
-- Dumping data for table `revenues`
--

INSERT INTO `revenues` (`id`, `for_month`, `for_year`, `updated_date`, `amount`)
VALUES (1, 'January', '2025', '2025-01-31', 12000);

-- --------------------------------------------------------

--
-- Table structure for table `transactions`
--

CREATE TABLE `transactions`
(
    `id`                 int(11)     NOT NULL,
    `created_date`       date         DEFAULT NULL,
    `amount`             int(11)      DEFAULT NULL,
    `transaction_number` varchar(50) NOT NULL,
    `bank_name`          varchar(100) DEFAULT NULL,
    `account_owner_name` varchar(100) DEFAULT NULL,
    `fk_customer_id`     int(11)      DEFAULT NULL,
    `status`             tinyint(1)   DEFAULT 0
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

--
-- Indexes for table `bmi`
--
ALTER TABLE `bmi`
    ADD PRIMARY KEY (`id`),
    ADD KEY `idx_bmi_customer_id` (`fk_customer_id`);

--
-- Indexes for table `customers`
--
ALTER TABLE `customers`
    ADD PRIMARY KEY (`id`),
    ADD UNIQUE KEY `email` (`email`),
    ADD UNIQUE KEY `username` (`username`),
    ADD UNIQUE KEY `cin` (`cin`);

--
-- Indexes for table `employees`
--
ALTER TABLE `employees`
    ADD PRIMARY KEY (`id`),
    ADD UNIQUE KEY `cin_number` (`cin_number`),
    ADD UNIQUE KEY `username` (`username`),
    ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `expenses`
--
ALTER TABLE `expenses`
    ADD PRIMARY KEY (`id`),
    ADD KEY `fk_employee_id` (`fk_employee_id`);

--
-- Indexes for table `packages`
--
ALTER TABLE `packages`
    ADD PRIMARY KEY (`id`),
    ADD UNIQUE KEY `PACKAGE_NO` (`PACKAGE_NO`);

--
-- Indexes for table `queries`
--
ALTER TABLE `queries`
    ADD PRIMARY KEY (`id`),
    ADD KEY `idx_queries_username` (`username`);

--
-- Indexes for table `revenues`
--
ALTER TABLE `revenues`
    ADD PRIMARY KEY (`id`),
    ADD UNIQUE KEY `for_month` (`for_month`, `for_year`);

--
-- Indexes for table `transactions`
--
ALTER TABLE `transactions`
    ADD PRIMARY KEY (`id`),
    ADD UNIQUE KEY `transaction_number` (`transaction_number`),
    ADD KEY `fk_customer_id` (`fk_customer_id`),
    ADD KEY `idx_transaction_number` (`transaction_number`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bmi`
--
ALTER TABLE `bmi`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 3;

--
-- AUTO_INCREMENT for table `customers`
--
ALTER TABLE `customers`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 3;

--
-- AUTO_INCREMENT for table `employees`
--
ALTER TABLE `employees`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 3;

--
-- AUTO_INCREMENT for table `expenses`
--
ALTER TABLE `expenses`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 3;

--
-- AUTO_INCREMENT for table `packages`
--
ALTER TABLE `packages`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 4;

--
-- AUTO_INCREMENT for table `queries`
--
ALTER TABLE `queries`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 3;

--
-- AUTO_INCREMENT for table `revenues`
--
ALTER TABLE `revenues`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 2;

--
-- AUTO_INCREMENT for table `transactions`
--
ALTER TABLE `transactions`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `bmi`
--
ALTER TABLE `bmi`
    ADD CONSTRAINT `bmi_ibfk_1` FOREIGN KEY (`fk_customer_id`) REFERENCES `customers` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `expenses`
--
ALTER TABLE `expenses`
    ADD CONSTRAINT `expenses_ibfk_1` FOREIGN KEY (`fk_employee_id`) REFERENCES `employees` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `transactions`
--
ALTER TABLE `transactions`
    ADD CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`fk_customer_id`) REFERENCES `customers` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;
COMMIT;



CREATE PROCEDURE get_ids(IN table_name VARCHAR(255))
BEGIN
    SET @query = CONCAT('SELECT MAX(id) FROM ', table_name);
    PREPARE stmt FROM @query;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END;

CREATE PROCEDURE gym_db.delete_data(
    IN tableName VARCHAR(255),
    IN recordId INT
)
BEGIN
    SET @query = CONCAT('DELETE FROM ', tableName, ' WHERE id = ', recordId);
    PREPARE stmt FROM @query;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END;

/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;