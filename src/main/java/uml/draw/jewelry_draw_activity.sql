CREATE TABLE `jewelry_topic` (
  `unique_topic_id` varchar(255) NOT NULL COMMENT '唯一话题ID',
  `start_time` datetime NOT NULL COMMENT '活动开始时间',
  `end_time` datetime NOT NULL COMMENT '活动结束时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `closed` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`unique_topic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='话题表';

------开始--
DROP TABLE IF EXISTS `jewelry_collaboration_activity`;
CREATE TABLE `jewelry_collaboration_activity` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `unique_topic_id` varchar(255) NOT NULL COMMENT '唯一话题ID',
  `activity_id` varchar(128) NOT NULL COMMENT '活动ID',
  `start_time` datetime NOT NULL COMMENT '活动开始时间',
  `end_time` datetime NOT NULL COMMENT '活动结束时间',
  `daily_shard_limit` int NOT NULL DEFAULT '0' COMMENT '每日分享次数上限',
  `daily_create_content_limit` int NOT NULL DEFAULT '0' COMMENT '每日创作次数上限',
  `daily_buy_limit` int NOT NULL DEFAULT '0' COMMENT '每日购买次数上限',
  `product_list` text COMMENT '活动联名商品列表',
  `user_total` int NOT NULL DEFAULT '0' COMMENT '活动参与总人数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `closed` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `udx_activity_id` (`activity_id`),
  KEY `idx_unique_topic_id` (`unique_topic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='联名活动';

DROP TABLE IF EXISTS `jewelry_collaboration_activity_task`;
CREATE TABLE `jewelry_collaboration_activity_task` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `activity_id` varchar(128) NOT NULL COMMENT '活动ID',
  `task_id` varchar(128) NOT NULL COMMENT '任务ID',
  `biz_id` varchar(128) NOT NULL COMMENT '业务ID',
  `biz_type` varchar(16) NOT NULL COMMENT 'CREATE_CONTENT/SHARE/BUY/CANCEL_BUY',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `biz_date` date NOT NULL COMMENT '业务日期(yyyy-mm-dd)',
  `popup` int NOT NULL DEFAULT '0' COMMENT '是否弹窗(0/1)',
  `status` varchar(16) NOT NULL COMMENT 'INIT/SUCCESS/FAIL',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `closed` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `udx_task_id` (`task_id`),
  KEY `idx_lottery_activity_user` (`activity_id`,`user_id`, `biz_date`),
  KEY `idx_user_biz_type` (`user_id`,`biz_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='联名活动任务';

DROP TABLE IF EXISTS `jewelry_lottery_award`;
CREATE TABLE `jewelry_lottery_award` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `activity_id` varchar(128) NOT NULL COMMENT '活动ID',
  `award_id` varchar(128) NOT NULL COMMENT '奖励ID',
  `award_name` varchar(255) NOT NULL COMMENT '奖励名称',
  `award_img_url` varchar(255) NOT NULL COMMENT '奖励图片URL',
  `award_type` varchar(32) NOT NULL COMMENT 'WANT/PRODUCT',
  `total_stock` int NOT NULL DEFAULT '0' COMMENT '总库存',
  `stock` int NOT NULL DEFAULT '0' COMMENT '当前可用库存',
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
