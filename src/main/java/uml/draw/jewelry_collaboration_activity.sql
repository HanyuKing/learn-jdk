
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
  UNIQUE KEY `udx_bizid_type` (`biz_id`,`biz_type`),
  KEY `idx_activity_user_date` (`activity_id`,`user_id`, `biz_date`),
  KEY `idx_createtime_status` (`create_time`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='联名活动任务';