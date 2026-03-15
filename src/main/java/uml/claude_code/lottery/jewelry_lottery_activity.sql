DROP TABLE IF EXISTS `jewelry_lottery_award`;
CREATE TABLE `jewelry_lottery_award` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `activity_id` varchar(128) NOT NULL COMMENT '活动ID',
  `award_id` varchar(128) NOT NULL COMMENT '奖励ID',
  `award_name` varchar(255) NOT NULL COMMENT '奖励名称',
  `award_img_url` varchar(255) NOT NULL COMMENT '奖励图片URL',
  `award_type` varchar(32) NOT NULL COMMENT '奖品类型: WANT(兜底)/PRODUCT(实物)',
  `award_level` varchar(16) NOT NULL DEFAULT 'FALLBACK' COMMENT '奖品等级: A(大奖)/B(中等奖)/C(小奖)/FALLBACK(兜底)，字母可扩展',
  `total_stock` int NOT NULL DEFAULT '0' COMMENT '总库存（活动开始后不修改）',
  `stock` int NOT NULL DEFAULT '0' COMMENT '当前可用库存（随发放扣减，全局上限兜底用）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `closed` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_activity_id` (`activity_id`),
  KEY `idx_award_id` (`award_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动奖励';

DROP TABLE IF EXISTS `jewelry_lottery_rule`;
CREATE TABLE `jewelry_lottery_rule` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `activity_id` varchar(128) NOT NULL COMMENT '活动ID',
  `rule_type` varchar(32) NOT NULL COMMENT 'TIME/WEIGHT/PITY/UID/LIMIT',
  `rule_version` int NOT NULL DEFAULT '1' COMMENT '规则版本',
  `rule_json` json NOT NULL COMMENT '规则内容(简化配置)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `closed` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `udx_activity_rule` (`activity_id`,`rule_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='抽奖规则';

DROP TABLE IF EXISTS `jewelry_lottery_activity_balance`;
CREATE TABLE `jewelry_lottery_activity_balance` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `activity_id` varchar(128) NOT NULL COMMENT '活动ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `lottery_remaining` int NOT NULL DEFAULT '0' COMMENT '剩余抽奖次数',
  `miss_streak` int NOT NULL DEFAULT '0' COMMENT '连续未中次数',
  `total_lottery` int NOT NULL DEFAULT '0' COMMENT '总抽奖次数',
  `total_wins` int NOT NULL DEFAULT '0' COMMENT '总中奖次数',
  `last_lottery_time` datetime DEFAULT NULL COMMENT '最后抽奖时间',
  `last_win_time` datetime DEFAULT NULL COMMENT '最后中奖时间',
  `last_non_fallback_win_time` datetime DEFAULT NULL COMMENT '最后非兜底中奖时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `closed` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `udx_activity_user` (`activity_id`,`user_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='抽奖余额';

DROP TABLE IF EXISTS `jewelry_lottery_activity_balance_log`;
CREATE TABLE `jewelry_lottery_activity_balance_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `activity_id` varchar(128) NOT NULL COMMENT '活动ID',
  `biz_id` varchar(128) NOT NULL COMMENT '业务ID',
  `biz_type` varchar(16) NOT NULL COMMENT 'TASK/DARW',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `use` int NOT NULL COMMENT '变更(+/-)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `closed` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_biz_id` (`biz_id`),
  KEY `idx_activity_user` (`activity_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='抽奖余额流水';

DROP TABLE IF EXISTS `jewelry_lottery_activity_draw_record`;
CREATE TABLE `jewelry_lottery_activity_draw_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `draw_id` varchar(128) NOT NULL COMMENT '抽奖ID',
  `activity_id` varchar(128) NOT NULL COMMENT '活动ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `award_id` varchar(128) NOT NULL COMMENT '奖品ID',
  `stage_no` int NOT NULL COMMENT '阶段序号',
  `weight_snapshot` int NOT NULL DEFAULT '0' COMMENT '抽奖时权重',
  `status` varchar(16) NOT NULL COMMENT 'INIT/SUCCESS/FAIL',
  `fail_reason` varchar(64) DEFAULT NULL COMMENT '失败原因',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `closed` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `udx_draw_id` (`draw_id`),
  KEY `idx_activity_id` (`activity_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='抽奖记录';
