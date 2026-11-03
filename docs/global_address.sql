CREATE TABLE `global_address` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `receiver_name` varchar(100) NOT NULL COMMENT '收件人姓名',
    `phone` varchar(50) NOT NULL COMMENT '联系电话（含国家码）',
    `province` varchar(50) NOT NULL COMMENT '省/州',
    `city` varchar(50) NOT NULL COMMENT '城市',
    `district` varchar(50) DEFAULT '' COMMENT '区/县',
    `address` varchar(255) NOT NULL COMMENT '详细地址',
    `zip_code` varchar(20) NOT NULL COMMENT '邮政编码',
    `country` varchar(50) NOT NULL COMMENT '国家名称',
    `country_code` varchar(10) NOT NULL COMMENT '国家二字码 CN/US/JP',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='全球统一地址表';