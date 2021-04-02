CREATE TABLE `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cardNo` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `money` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `test`.`account`(`id`, `cardNo`, `name`, `money`) VALUES (1, '6029621011001', '韩梅梅', 10000);
INSERT INTO `test`.`account`(`id`, `cardNo`, `name`, `money`) VALUES (2, '6029621011000', '李大雷', 10000);
