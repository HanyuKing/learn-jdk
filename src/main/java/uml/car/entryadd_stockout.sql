/********************************** 入库单 **********************************/
DROP
  TABLE IF EXISTS `ctms_entry_order`;
CREATE TABLE `ctms_entry_order` (
  `id` BIGINT (20) NOT NULL AUTO_INCREMENT,
  `entry_order_id` VARCHAR (20) NOT NULL COMMENT '入库单号',
  `related_id` VARCHAR (20) DEFAULT NULL COMMENT '关联单号',
  `wh_id` VARCHAR (10) NOT NULL COMMENT '目标仓库id',
  `type` TINYINT (3) NOT NULL COMMENT '入库类型id(补货需求-1, 采购入库-2, 匀仓入库-3)',
  `expect_time` datetime DEFAULT NULL COMMENT '预期到货时间',
  `status` VARCHAR (20) NOT NULL COMMENT '单据状态(CREATE-创建, PUSHED-已推送,  PARTLY_COMPLETED-部分完成, COMPLETED-收到仓库确认, CANCELED-已取消)',
  `wms_process_status` VARCHAR (20) DEFAULT NULL COMMENT '仓库状态流水(NEW-未开始处理, ACCEPT-仓库接单, PARTFULFILLED-部分收货完成, FULFILLED-收货完成, EXCEPTION-异常, CANCELED-取消, CLOSED-关闭, REJECT-拒单, CANCELEDFAIL-取消失败)',
  `push_to_wms_time` datetime DEFAULT NULL COMMENT '推送WMS时间',
  `receive_from_wms_time` datetime DEFAULT NULL COMMENT '收到WMS确认时间',
  `cancel_time` datetime DEFAULT NULL COMMENT '取消时间',
  `is_fulfilled` TINYINT (2) DEFAULT NULL COMMENT '入库确认商品数量是否有差异(0-没有,1-有)',
  `is_cc` TINYINT (2) DEFAULT NULL COMMENT '入库确认商品中是否有残次(0-没有,1-有)',
  `remark` VARCHAR (200) DEFAULT NULL COMMENT '描述',
  `operator` VARCHAR (50) DEFAULT NULL COMMENT '创建人名字',
  `delete_flag` TINYINT ( 2 ) NOT NULL DEFAULT '0' COMMENT '是否逻辑删除 0-有效 1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_entry_order_id` (`entry_order_id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '入库单';
DROP TABLE
    IF
        EXISTS `ctms_entry_order_item_flow`;
CREATE TABLE `ctms_entry_order_item_flow` (
  `id` BIGINT ( 20 ) NOT NULL AUTO_INCREMENT,
  `entry_order_id` VARCHAR ( 20 ) NOT NULL COMMENT '入库单号',
  `delivery_order_id` VARCHAR ( 20 ) NOT NULL COMMENT '运单号',
  `ssu_id` BIGINT ( 20 ) NOT NULL DEFAULT '0' COMMENT '商品id',
  `item_id` VARCHAR ( 128 ) NOT NULL COMMENT '商品编码',
  `item_name` VARCHAR ( 128 ) DEFAULT NULL COMMENT '商品名称',
  `receivable` INT ( 11 ) NOT NULL DEFAULT '0' COMMENT '应收数量',
  `received_zp` INT ( 11 ) NOT NULL DEFAULT '0' COMMENT '实收正品数量',
  `received_cc` INT ( 11 ) NOT NULL DEFAULT '0' COMMENT '实收残次数量',
  `is_delete` TINYINT ( 2 ) NOT NULL DEFAULT '1' COMMENT '是否逻辑删除 1-有效 2-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY ( `id` ),
  UNIQUE KEY `udx_entry_delivery_order_ssu_id` ( `entry_order_id`, `delivery_order_id` )
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '入库单商品明细流水(幂等)';
DROP
  TABLE IF EXISTS `ctms_entry_order_item`;
CREATE TABLE `ctms_entry_order_item` (
  `id` BIGINT (20) NOT NULL AUTO_INCREMENT,
  `entry_order_id` VARCHAR (20) NOT NULL COMMENT '入库单号',
  `ssu_id` BIGINT (20) NOT NULL DEFAULT '0' COMMENT '商品id',
  `item_id` VARCHAR (128) NOT NULL COMMENT '商品编码',
  `item_name` VARCHAR (128) DEFAULT NULL COMMENT '商品名称',
  `receivable` INT (11) NOT NULL DEFAULT '0' COMMENT '应收数量',
  `received_zp` INT (11) NOT NULL DEFAULT '0' COMMENT '实收正品数量',
  `received_cc` INT (11) NOT NULL DEFAULT '0' COMMENT '实收残次数量',
  `delete_flag` TINYINT ( 2 ) NOT NULL DEFAULT '0' COMMENT '是否逻辑删除 0-有效 1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_entry_order_id` (`entry_order_id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '入库单商品明细';
/********************************** 入库单 end **********************************/

/********************************** 出库单 **********************************/
DROP
  TABLE IF EXISTS `ctms_stock_out_order`;
CREATE TABLE `ctms_stock_out_order` (
  `id` BIGINT (20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `stock_out_id` VARCHAR (20) NOT NULL COMMENT '出库单号',
  `related_id` VARCHAR (20) DEFAULT NULL COMMENT '关联单号',
  `type` TINYINT (3) NOT NULL COMMENT '出库类型id(补货需求-1, 残品出库-2, 匀仓出库-3)',
  `inventory_type` VARCHAR (11) NOT NULL COMMENT 'ZP=正品, CC=残次品, BXJJ=保修旧件，WXJJ=维修旧件',
  `expect_out_time` datetime NOT NULL COMMENT '预计出库时间',
  `delivery_promise_time` INT (11) NOT NULL DEFAULT 0 COMMENT '发货时效',
  `transport_promise_time` INT (11) NOT NULL DEFAULT 0 COMMENT '运输时效',
  `priority` VARCHAR (10) NOT NULL COMMENT '优先级 (VOR, 紧急, 库存)',
  `remark` VARCHAR (500) DEFAULT NULL COMMENT '备注',
  `status` VARCHAR (20) NOT NULL DEFAULT 'CREATE' COMMENT '出库单状态(CREATE=已创建, PUSHING=未推送, COMPLETED=已完成, CANCELED=已取消, REJECT=驳回, RECEIVED=收到仓库确认, CANCEL_PUSHING=取消WMS推送)',
  `wms_process_status` VARCHAR (20) DEFAULT NULL COMMENT '仓库状态流水（NEW-未开始处理，ACCEPT-仓库接单，FULLFILLED-出库完成，EXECEPTION-异常，CANCELED-取消，CLOSED-关闭，REJECT-拒单，CANCELEDFAIL-取消失败）',
  `push_to_wms_time` datetime DEFAULT NULL COMMENT '推送wms时间',
  `receive_from_wms_time` datetime DEFAULT NULL COMMENT '收到wms确认时间|出库时间',
  `out_biz_code` VARCHAR (45) DEFAULT NULL COMMENT '入库单确认接口中唯一编码',
  `is_fulfilled` TINYINT (2) DEFAULT NULL COMMENT '出库确认商品数量是否有差异(0-没有,1-有)',
  `wh_id` VARCHAR (10) NOT NULL COMMENT '目标仓库id',
  `address_id` BIGINT (20) DEFAULT NULL COMMENT '收件人id',
  `receiver_name` VARCHAR (200) DEFAULT NULL COMMENT '收件人',
  `receiver_phone` VARCHAR (200) NOT NULL DEFAULT '' COMMENT '收件人电话',
  `receiver_phone_suffix` CHAR (4) NOT NULL DEFAULT '0000' COMMENT '收件人手机号后缀',
  `province` VARCHAR (50) DEFAULT NULL COMMENT '收件省份',
  `city` VARCHAR (50) DEFAULT NULL COMMENT '收件城市',
  `district` VARCHAR (50) DEFAULT NULL COMMENT '收件城区',
  `area` VARCHAR (50) DEFAULT NULL COMMENT '收件街道',
  `detail_address` VARCHAR (2048) NOT NULL DEFAULT '' COMMENT '详细收货地址',
  `create_from` VARCHAR (30) NOT NULL COMMENT '创建来源 ERP-供应链平台, M_CENTER-商家工作台',
  `operator` VARCHAR (50) DEFAULT NULL COMMENT '创建人名字',
  `editor` VARCHAR (20) NOT NULL DEFAULT '' COMMENT '修改人',
  `delete_flag` TINYINT ( 2 ) NOT NULL DEFAULT '0' COMMENT '是否逻辑删除 0-有效 1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_stock_out_id` (`stock_out_id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT '出库单';
DROP
  TABLE IF EXISTS `ctms_stock_out_order_item`;
CREATE TABLE `ctms_stock_out_order_item` (
  `id` BIGINT (20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `stock_out_id` VARCHAR (20) NOT NULL COMMENT '出库单号',
  `ssu_id` BIGINT (20) NOT NULL COMMENT '商品id',
  `item_id` VARCHAR (200) DEFAULT NULL COMMENT '商品编码',
  `item_name` VARCHAR (200) DEFAULT NULL COMMENT '商品名称',
  `expect_number` INT (10) DEFAULT NULL COMMENT '预计出库数量',
  `real_number` INT (10) DEFAULT NULL COMMENT '实际出库数量',
  `delete_flag` TINYINT ( 2 ) NOT NULL DEFAULT '0' COMMENT '是否逻辑删除 0-有效 1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_stock_out_id` (`stock_out_id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT '出库单商品明细';
/********************************** 出库单 end **********************************/

/********************************** 运单 ****************************************/
DROP
  TABLE IF EXISTS `ctms_delivery_order`;
CREATE TABLE `ctms_delivery_order` (
  `id` BIGINT (20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `delivery_order_id` VARCHAR (20) NOT NULL COMMENT '运单号',
  `related_id` VARCHAR (20) NOT NULL COMMENT '关联单号',
  `logistics_code` VARCHAR (50) NOT NULL DEFAULT '' COMMENT '快递公司代码',
  `logistics_name` VARCHAR (50) NOT NULL DEFAULT '' COMMENT '快递公司名称',
  `express_code` VARCHAR (50) NOT NULL DEFAULT '' COMMENT '快递单号',
  `status` VARCHAR (20) NOT NULL DEFAULT 'CREATE' COMMENT '运单状态(RECEIVED=已妥投, 在途=ONWAY)',
  `delete_flag` TINYINT ( 2 ) NOT NULL DEFAULT '0' COMMENT '是否逻辑删除 0-有效 1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_delivery_order_id` (`delivery_order_id`),
  UNIQUE KEY `udx_related_express_code` (`delivery_order_id`, `express_code`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT '运单';
DROP
  TABLE IF EXISTS `ctms_delivery_order_item`;
CREATE TABLE `ctms_delivery_order_item` (
  `id` BIGINT (20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `delivery_order_id` VARCHAR (20) NOT NULL COMMENT '运单号',
  `ssu_id` BIGINT (20) NOT NULL COMMENT '商品id',
  `item_code` VARCHAR (200) DEFAULT NULL COMMENT '商品编码',
  `item_name` VARCHAR (200) DEFAULT NULL COMMENT '商品名称',
  `number` INT (10) DEFAULT NULL COMMENT '数量',
  `delete_flag` TINYINT ( 2 ) NOT NULL DEFAULT '0' COMMENT '是否逻辑删除 0-有效 1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_delivery_order_id` (`delivery_order_id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT '运单明细';
/********************************** 运单 end ****************************************/

/********************************** 运单关系表  ****************************************/
DROP
    TABLE IF EXISTS `ctms_delivery_order_relation`;
CREATE TABLE `ctms_delivery_order_relation`
(
    `id` BIGINT (20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `related_id` VARCHAR (20) NOT NULL COMMENT '关联单号',
    `delivery_order_id` VARCHAR(20) NOT NULL COMMENT '运单号',
    `type`              TINYINT(2)  NOT NULL COMMENT '1-出库单 2-入库单',
    `create_time`       datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_delivery_order_id` (`delivery_order_id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT '运单关系表';
/********************************** 运单关系表 end ****************************************/

/********************************** log ****************************************/
DROP
  TABLE IF EXISTS `ctms_entry_order_log`;
CREATE TABLE `ctms_entry_order_log` (
  `id` BIGINT (20) NOT NULL AUTO_INCREMENT,
  `entry_id` VARCHAR (20) NOT NULL COMMENT '入库单号',
  `status` VARCHAR (20) NOT NULL COMMENT '入库单状态',
  `operator` BIGINT (20) DEFAULT NULL COMMENT '操作人id',
  `content` text COMMENT '入库单变化内容',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_entry_order_id` (`entry_id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '入库单操作日志';
DROP
  TABLE IF EXISTS `ctms_stock_out_log`;
CREATE TABLE `ctms_stock_out_log` (
  `id` BIGINT (20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `stock_out_id` VARCHAR (20) DEFAULT NULL COMMENT '出库单号',
  `content` text COMMENT '出库单变化内容',
  `operator` VARCHAR (20) DEFAULT NULL COMMENT '操作人id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_stock_out_id` (`stock_out_id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = '出库单操作日志';
