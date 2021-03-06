create DATABASE IF NOT EXISTS `Ip-Info-Storage` CHARACTER SET utf8mb4;
use `Ip-Info-Storage`;

drop table IF EXISTS `ip_address`;

create TABLE IF NOT EXISTS `ip_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(15) NOT NULL,
  `processing_state` varchar(15) DEFAULT 0,
  `country_code` varchar(2) DEFAULT NULL,
  `country_code3` varchar(3) DEFAULT NULL,
  `country_name` varchar(100) DEFAULT NULL,
  `country_emoji` varchar(20) DEFAULT NULL,
  `processed_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_address` (`address`),
  INDEX (`processing_state`)
) ENGINE=InnoDB AUTO_INCREMENT=1;

