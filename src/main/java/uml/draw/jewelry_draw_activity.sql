CREATE TABLE `jewelry_topic` (
  `unique_topic_id` varchar(255) NOT NULL COMMENT '唯一话题ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `closed` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`unique_topic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='话题';

CREATE TABLE `jewelry_draw_activity` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `unique_topic_id` varchar(255) NOT NULL COMMENT '唯一话题ID',
  `activity_id` varchar(128) NOT NULL COMMENT '活动ID',
  `daily_shard_limit` int NOT NULL DEFAULT '0' COMMENT '每日分享次数上限',
  `daily_create_content_limit` int NOT NULL DEFAULT '0' COMMENT '每日创作次数上限',
  `daily_buy_limit` int NOT NULL DEFAULT '0' COMMENT '每日购买次数上限',
  `user_total` int NOT NULL DEFAULT '0' COMMENT '活动参与总人数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `closed` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `udx_activity_id` (`activity_id`),
  KEY `idx_unique_topic_id` (`unique_topic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='抽奖活动';

CREATE TABLE `jewelry_draw_activity_award` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `activity_id` varchar(128) NOT NULL COMMENT '活动ID',
  `award_id` varchar(128) NOT NULL COMMENT '奖励ID',
  `award_type` varchar(32) NOT NULL COMMENT 'WANT/PRODUCT',
  `stock` int NOT NULL DEFAULT '0' COMMENT '库存',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `closed` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_activity_id` (`activity_id`),
  KEY `idx_award_id` (`award_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动奖励';

CREATE TABLE `jewelry_draw_activity_task` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
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
  KEY `idx_biz_date` (`biz_date`),
  KEY `idx_user_biz_type` (`user_id`,`biz_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动任务';

CREATE TABLE `jewelry_draw_activity_balance` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `draw_remaining` int NOT NULL DEFAULT '0' COMMENT '剩余抽奖次数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `closed` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='抽奖余额';

CREATE TABLE `jewelry_draw_activity_balance_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `biz_id` varchar(128) NOT NULL COMMENT '业务ID',
  `biz_type` varchar(16) NOT NULL COMMENT 'TASK/DARW',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `use` int NOT NULL COMMENT '变更(+/-)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `closed` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_biz_id` (`biz_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='抽奖余额流水';

CREATE TABLE `jewelry_draw_activity_draw_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `draw_id` varchar(128) NOT NULL COMMENT '抽奖ID',
  `activity_id` varchar(128) NOT NULL COMMENT '活动ID',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `award_id` varchar(32) NOT NULL COMMENT '奖励ID',
  `status` varchar(16) NOT NULL COMMENT 'INIT/SUCCESS/FAIL',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `closed` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_draw_id` (`draw_id`),
  KEY `idx_activity_id` (`activity_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='抽奖记录';
