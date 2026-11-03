-- =============================================================================
-- 卡券系统表设计 DDL（MVP 版）
--
-- 设计依据：
--   docs/coupon-architecture-industry-reference.md  业界资料整理
--   docs/电商卡券系统高并发技术设计.md              领取/核销/过期/缓存
--   docs/营销优惠叠加规则-技术设计分析.md           叠加分层与订单分摊模型
--   管理后台建券页面（基础规则 + 优惠设置）          MVP 字段范围来源
--
-- 原则：尽量少建表。能用字段、索引、单分片 count 解决的，不单独建表。
--
-- ID 命名：
--   template_id  券模板ID，建券页面提交一次生成一个
--   coupon_id    券ID，按模板发到用户手里的一张券
--
-- 参考表清单（完整设计 9 张；MVP 实际执行 trade_coupon_template、trade_coupon_scope、
--   trade_user_coupon、trade_user_coupon_log、trade_coupon_issue_task、
--   trade_coupon_issue_task_item、trade_order_discount_detail 共 7 张）：
--
--   trade_coupon_template         券模板（规则 + 发放设置，一次建券 = 一个模板）
--   trade_coupon_stock            券计数（后置；MVP 不建，不支持限量库存）
--   trade_coupon_scope            适用范围（仅商品券需要）
--   trade_user_coupon             用户券逻辑表（按 user_id 路由到 trade_user_coupon_{00..N}）
--   trade_user_coupon_log         用户券状态流水
--   trade_coupon_issue_task       人群灌券任务
--   trade_coupon_issue_task_item  人群灌券明细（一个用户一行）
--   trade_coupon_expire_notify    券到期提醒记录（后置；MVP 不建）
--   trade_order_discount_detail   订单优惠分摊明细
--
-- 有意不建的表（需要时按「七、扩展点」加）：
--   券批次表     建券页面一次提交就是一个模板，发放量、领取时间直接放在模板上，没有同一模板分多期发放的用法
--   券统计表     后置；MVP 不建设发放/核销统计任务
--   限领计数表   用户券表按 user_id 分片，「某人领了几张」是单分片 count
--   本地消息表   状态变更后直接发 MQ，失败由流水表补偿
--
-- 命名约定（对齐 trade-order-service/src/main/resources/sql/trade_cart.sql）：
--   * 主键统一 `id` BIGINT UNSIGNED AUTO_INCREMENT
--   * 业务编号 VARCHAR(64)，内部 ID 用 BIGINT 全局序列
--   * 金额 DECIMAL(12,2)，折扣率 DECIMAL(6,4)
--   * 业务时间统一使用 Unix epoch milliseconds；create_time、modified_time 仍使用 DATETIME 审计
--   * 删除标记 `closed`（0=未删除，1=已删除）
--   * 创建/修改时间 `create_time` / `modified_time`
--   * 多租户表带 `tenant_id`，来源应用带 `app_id`
--   * 引擎 InnoDB，字符集 utf8mb4；JSON 列需 MySQL 5.7+
-- =============================================================================


-- =============================================================================
-- 一、配置
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 1. 券模板：一行 = 建券页面的一次提交（规则 + 发放设置）
--
-- 字段分组对应建券页面的两个区块：
--   基础规则 → issue_channel / name / receive_* / valid_*
--   优惠设置 → discount_* / threshold_amount / max_per_user / crowd_type / register_within_days / scope_type
--   「券发放量」本期固定不限量，不建 trade_coupon_stock
--
-- 本表是低频改动的配置，适合整行缓存。
-- 完整设计可将已发放数、已核销数拆到 trade_coupon_stock；MVP 不建该表，
-- 不做限量扣减和发放/核销统计，避免每次领券、核销都更新配置行。
--
-- 状态机：
--   创建 → 未开始 --领取开始时间已到--> 生效中 --领取结束时间已到--> 已过期（终态）
--          未开始 / 生效中 --作废--> 已作废（终态）
--   已过期不能作废。
--
-- 只存人工操作的结果，时间驱动的流转在查询时推导，不用定时任务改状态：
--   存储 status：1正常 2已作废
--   列表页「状态」筛选按下面顺序推导，命中即停：
--     已作废  status = 2
--     未开始  status = 1 AND UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)) * 1000 <  receive_start_time
--     已过期  status = 1 AND UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)) * 1000 >  receive_end_time
--     生效中  status = 1 AND receive_start_time <= UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)) * 1000 <= receive_end_time
--   作废时以同一毫秒时间戳比较 receive_end_time，即只有未开始、生效中能作废。
--   MVP 不支持限量库存，因此不存在“已领完”状态；后续启用库存表后再由 trade_coupon_stock 拦截。
--
-- 生效人群（crowd_type，优惠设置）：
--   issue_channel=1～5 默认全部用户，页面不展示人群选择；issue_channel=6 本期仅新人；
--   issue_channel=7 人群灌券不配置人群，仅由发券任务按名单发放。
--   0 全部用户：不校验
--   1 新用户  ：领取人注册后 N 天内可领，N = register_within_days，此时必填（应用层校验）
--               例：N=7，用户注册后 7 天内可领
--   注册时间从本地 biz_user.create_time 批量读取（不在券表里存），领券时校验。
--   只在领取时校验，领到后不再因超过 N 天而失效。
--
-- 人群惊喜券（issue_channel=6）：
--   本期仅配置新人，符合 register_within_days 的用户由到账弹窗自动领取；同一用户最多一张新人券。
--   不进入全部用户索引，不创建运营发券任务。
-- 人群灌券（issue_channel=7）：创建时不配置人群，仅由任务名单决定发放对象；不进入 C 端领取候选。
--
-- 作废：不可恢复。作废后不能再领取，用户已领取的券不受影响，仍可在各自有效期内使用。
-- -----------------------------------------------------------------------------
CREATE TABLE `trade_coupon_template` (
    `id`                   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键，仅表内使用',
    `template_id`          BIGINT UNSIGNED NOT NULL COMMENT '券模板ID，全局时间序列号左移分片位，业务唯一，对外暴露，其他表关联用',
    `name`                 VARCHAR(64)     NOT NULL COMMENT '运营模板名称，不用于C端展示',
    `description`          VARCHAR(6)      NOT NULL COMMENT 'C端券名称，必填，最多6个字符',

    -- ===== 基础规则 · 推广方式 =====
    `issue_channel`        TINYINT         NOT NULL COMMENT '推广方式：1全网公开推广 2直播渠道 3客服渠道 4领券中心渠道 5自有渠道（均默认全部用户） 6人群惊喜券（仅新人自动领取） 7人群灌券（仅由发券任务发放）',

    -- ===== 基础规则 · 领取时间 =====
    `receive_start_time`   BIGINT          NOT NULL COMMENT '领取开始时间，Unix epoch milliseconds',
    `receive_end_time`     BIGINT          NOT NULL COMMENT '领取结束时间，Unix epoch milliseconds，与开始时间间隔须 <= 180 天（应用层校验）',

    -- ===== 基础规则 · 使用时间（三选一）=====
    `valid_type`           TINYINT         NOT NULL COMMENT '使用时间：1与领取时间相同 2限制有效天数 3自定义时间',
    `relative_valid_days`  INT             DEFAULT NULL COMMENT '有效天数，valid_type=2 时必填，从领取时刻起算',
    `valid_start_time`     BIGINT          DEFAULT NULL COMMENT '有效期开始，Unix epoch milliseconds，valid_type=3 时必填',
    `valid_end_time`       BIGINT          DEFAULT NULL COMMENT '有效期结束，Unix epoch milliseconds，valid_type=3 时必填',

    -- ===== 优惠设置 · 优惠方式 =====
    `discount_type`        TINYINT         NOT NULL COMMENT '优惠方式：1立减 2满减 3折扣',
    `discount_value`       DECIMAL(12,2)   DEFAULT NULL COMMENT '减免金额：立减、满减必填',
    `threshold_amount`     DECIMAL(12,2)   DEFAULT NULL COMMENT '满减门槛：满 X 减 Y 的 X，满减必填',
    `discount_rate`        DECIMAL(6,4)    DEFAULT NULL COMMENT '折扣率：0.8000 表示 8 折，折扣必填',
    `max_discount_amount`  DECIMAL(12,2)   DEFAULT NULL COMMENT '折扣封顶优惠额，NULL 表示不封顶',

    -- ===== 优惠设置 · 限领与范围 =====
    `max_per_user`         INT             NOT NULL DEFAULT 1 COMMENT '每人限领张数，MVP 固定为 1',
    `crowd_type`           TINYINT         NOT NULL DEFAULT 0 COMMENT '生效人群：0全部用户 1新用户',
    `register_within_days` INT             DEFAULT NULL COMMENT '新用户的注册天数：注册后 N 天内可领，crowd_type=1 时必填。领取时刻 - 注册时间 <= N 天',
    `scope_type`           TINYINT         NOT NULL DEFAULT 2 COMMENT '商品范围：1指定商品(商品券) 2全店商品(店铺券)。=1 时明细见 trade_coupon_scope',

    -- ===== 叠加与成本（页面没有，后台给默认值）=====
    `stack_layer`          TINYINT         NOT NULL DEFAULT 2 COMMENT '叠加层：1商家券 2平台券，决定算价执行顺序',
    `stackable`            TINYINT(1)      NOT NULL DEFAULT 1 COMMENT '是否可与其他层叠加：1可叠加 0互斥',
    `funder`               TINYINT         NOT NULL DEFAULT 1 COMMENT '成本承担方：1平台 2商家，分摊与结算依据',

    -- ===== 状态 =====
    `status`               TINYINT         NOT NULL DEFAULT 1 COMMENT '状态：1正常 2已作废。未开始/生效中/已过期由领取时间推导，不存储',
    `void_time`            BIGINT          DEFAULT NULL COMMENT '作废时间，Unix epoch milliseconds',
    `void_by`              VARCHAR(64)     DEFAULT NULL COMMENT '作废操作人',
    `void_reason`          VARCHAR(255)    DEFAULT NULL COMMENT '作废原因',
    `closed`               TINYINT(1)      NOT NULL DEFAULT 0 COMMENT '删除标记（0=未删除，1=已删除）',
    `tenant_id`            VARCHAR(32)     NOT NULL COMMENT '租户ID',
    `app_id`               VARCHAR(32)     NOT NULL COMMENT '来源应用ID',
    `create_by`            VARCHAR(64)     DEFAULT NULL COMMENT '创建人',
    `update_by`            VARCHAR(64)     DEFAULT NULL COMMENT '最后修改人',
    `create_time`          DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_time`        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_template_id` (`template_id`),
    KEY `idx_tenant_id` (`tenant_id`, `id`),
    -- 列表页按状态筛选：status 定位作废与否，再按领取时间区分未开始/生效中/已过期
    KEY `idx_status_receive` (`tenant_id`, `status`, `closed`, `receive_end_time`, `receive_start_time`),
    -- 按渠道捞生效中的券（领券中心、客服页）
    KEY `idx_channel_status_receive` (`tenant_id`, `issue_channel`, `status`, `closed`, `receive_end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='券模板表';

-- -----------------------------------------------------------------------------
-- 2. 券计数：完整设计参考（后置，MVP 不执行）
--
-- 参考：docs/电商卡券系统高并发技术设计.md 7.3 节的 coupon_stock_segment
--       vivo 全球商城优惠券系统「库存记录表 + 拆分库存」
--
-- 领券扣减，影响行数 = 1 即领取成功：
--   UPDATE trade_coupon_stock
--      SET issued_count = issued_count + 1
--    WHERE template_id = ? AND bucket_no = 0
--      AND (total_stock IS NULL OR issued_count < total_stock);
--
-- 页面「券发放量」：不限 → total_stock 为 NULL；限制数量 → 填张数。
-- 用 issued_count 递增而不是剩余数递减，是为了「不限量」时也能记录已发数量。
--
-- used_count 不在领券链路里，不要每核销一张就更新本行（会和领券抢行锁），
--   由定时任务按 trade_user_coupon_log 的流水批量累加（USE +1，RETURN -1），例如每分钟一次。
--   分桶后各桶的 used_count 统一记在 0 号桶，展示时直接读 0 号桶。
--
-- 建券时与 trade_coupon_template 同一事务写入 bucket_no=0 一行。
-- MVP 只有 0 号桶；热点券需要分桶时，把库存拆成多行、领券随机选桶，表结构不变。
-- -----------------------------------------------------------------------------
CREATE TABLE `trade_coupon_stock` (
    `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `template_id`   BIGINT UNSIGNED NOT NULL COMMENT '券模板ID',
    `bucket_no`     INT             NOT NULL DEFAULT 0 COMMENT '库存分桶号，MVP 固定 0',
    `total_stock`   INT             DEFAULT NULL COMMENT '本桶发放量，NULL 表示不限量',
    `issued_count`  INT             NOT NULL DEFAULT 0 COMMENT '本桶已发放数，领券时同步条件更新',
    `used_count`    INT             NOT NULL DEFAULT 0 COMMENT '已核销数，定时任务按流水批量累加，仅统计用，只记在 0 号桶',
    `create_time`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_template_bucket` (`template_id`, `bucket_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='券计数表';


-- -----------------------------------------------------------------------------
-- 3. 适用范围：商品券指定哪些商品，一行一个商品
--
-- 用行存而不是 JSON 数组，是为了「按商品反查可用券」能命中索引，
-- 商详页、购物车的核心查询就是：
--   SELECT template_id FROM trade_coupon_scope WHERE scope_type = 1 AND ref_id = ?
--
-- 店铺券（trade_coupon_template.scope_type=2）不写本表。
-- -----------------------------------------------------------------------------
CREATE TABLE `trade_coupon_scope` (
    `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `template_id`   BIGINT UNSIGNED NOT NULL COMMENT '券模板ID',
    `scope_type`    TINYINT         NOT NULL DEFAULT 1 COMMENT '范围类型：1商品（预留 2SKU 3类目）',
    `ref_id`        VARCHAR(64)     NOT NULL COMMENT '范围对象ID',
    `closed`        TINYINT(1)      NOT NULL DEFAULT 0 COMMENT '删除标记（0=未删除，1=已删除）',
    `create_time`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_template_scope` (`template_id`, `scope_type`, `ref_id`),
    KEY `idx_scope_ref` (`scope_type`, `ref_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='券适用范围表';


-- =============================================================================
-- 二、用户资产
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 4. 用户券（逻辑表定义，发布时按 user_id 路由到物理分片）
--
-- 状态机：
--   领券 → 未使用 --下单--> 已锁定 --支付--> 已使用
--                           已锁定 --超时/主动取消--> 未使用
--                                               已使用 --整单退款--> 未使用
--                                               已使用 --部分退款--> 已使用（状态不变，券不退）
--          未使用 --到期--> 已过期（终态）
--
-- 只存操作产生的状态，「已过期」按时间推导，不用定时任务改状态：
--   存储 status：1未使用 2已锁定 3已使用
--   用户券列表的 tab 按下面推导：
--     可使用  status = 1 AND now <= valid_end_time（now 为 Unix epoch milliseconds）
--     已过期  status = 1 AND now >  valid_end_time
--     已使用  status = 3
--   已锁定的券不在「可使用」里，可显示为「使用中」；订单取消后回到未使用。
--   整单退款回到未使用时，如果已经过了 valid_end_time，会自动显示为已过期，不用额外处理。
--   部分退款不改状态、不写流水，退款金额记在 trade_order_discount_detail.refunded_amount。
--
-- 下单锁券时必须以当前 epoch milliseconds 校验 valid_start_time <= now <= valid_end_time；
-- 锁券后到期的券允许继续支付，避免用户下单后因券过期付不了款。
--
-- 领取时把金额/有效期规则快照写进本表，之后改券模板不影响已发出的券。
--
-- valid_start_time / valid_end_time 在领取时按 trade_coupon_template.valid_type 算好：
--   1 与领取时间相同 → 领取时刻 ~ receive_end_time
--     例：领取时间 11.1~11.19，用户 11.3 领取，可用期 11.3~11.19
--   2 限制有效天数   → 领取时刻 ~ 领取时刻 + relative_valid_days 天
--   3 自定义时间     → valid_start_time ~ valid_end_time
--
--   display_name / scope_type 在领取时保存 C 端券名称（description）和商品范围类型快照；
--   算价读取模板 Redis；用户券只保存展示、人群判断所需的最小快照；
--   crowd_type 保存新人券历史领取判断快照，不对外展示；门槛和优惠文案由规则字段生成。
--   商品范围明细、stackable、stack_layer、funder 不复制到用户券，统一以模板 Redis 为运行时来源。
--
-- 每人限领不单独建计数表：
--   本表按 user_id 分片，同一用户的券都在一个分片，
--   限领由 user_id + template_id 唯一键保证，并由发券服务在冲突后区分幂等和限领：
--   本期自动领取通过稳定 request_no 和唯一键防重；新人额外按用户加 Redis 短锁并在锁内回查终身领取历史。
-- -----------------------------------------------------------------------------
CREATE TABLE `trade_user_coupon` (
    `id`                  BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `coupon_id`           BIGINT UNSIGNED NOT NULL COMMENT '券ID，发到用户手里的一张券，全局序列ID，低位含分片路由位',
    `user_id`             VARCHAR(64)     NOT NULL COMMENT '用户ID，分片键',
    `template_id`         BIGINT UNSIGNED NOT NULL COMMENT '券模板ID',
    `source_type`         TINYINT         NOT NULL DEFAULT 1 COMMENT '来源：1系统自动领取 2人群灌券任务发放；任务发放时 request_no 为任务幂等号',
    `display_name`        VARCHAR(64)     DEFAULT NULL COMMENT '发券时的C端券名称快照，来源模板description',
    `scope_type`          TINYINT         DEFAULT NULL COMMENT '发券时的商品范围类型快照；1指定商品 2全店商品',
    `crowd_type`          TINYINT         DEFAULT NULL COMMENT '发券时的人群快照：0全部用户 1新人；仅用于新人领取历史判断，不对外展示',

    -- 状态
    `status`              TINYINT         NOT NULL DEFAULT 1 COMMENT '状态：1未使用 2已锁定 3已使用。已过期 = status=1 且超过 valid_end_time，不存储',
    `order_id`            VARCHAR(64)     DEFAULT NULL COMMENT '锁定或核销时关联的订单ID',

    -- 时间
    `receive_time`        BIGINT          NOT NULL COMMENT '领取时间，Unix epoch milliseconds',
    `valid_start_time`    BIGINT          NOT NULL COMMENT '实际生效时间，Unix epoch milliseconds',
    `valid_end_time`      BIGINT          NOT NULL COMMENT '实际失效时间，Unix epoch milliseconds',
    `used_time`           BIGINT          DEFAULT NULL COMMENT '核销时间，Unix epoch milliseconds',

    -- 规则快照（领取时写入，之后不随配置变化）
    `discount_type`       TINYINT         NOT NULL COMMENT '优惠方式快照',
    `discount_value`      DECIMAL(12,2)   DEFAULT NULL COMMENT '减免金额快照',
    `threshold_amount`    DECIMAL(12,2)   DEFAULT NULL COMMENT '门槛快照',
    `discount_rate`       DECIMAL(6,4)    DEFAULT NULL COMMENT '折扣率快照',
    `max_discount_amount` DECIMAL(12,2) DEFAULT NULL COMMENT '折扣封顶快照',

    -- 幂等与并发
    `request_no`          VARCHAR(64)     NOT NULL COMMENT '发放幂等号，业务方不传时由券系统生成',
    `version`             INT             NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',

    `tenant_id`           VARCHAR(32)     NOT NULL COMMENT '租户ID',
    `create_time`         DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_time`       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_coupon_id` (`coupon_id`),
    -- 发放幂等：同一用户的同一请求只发一张
    UNIQUE KEY `uk_user_request_no` (`user_id`, `request_no`),
    -- 用户券列表：可使用/已过期都是 status=1，再按 valid_end_time 与当前时间比较区分
    KEY `idx_user_status_valid` (`user_id`, `status`, `valid_end_time`),
    -- MVP 每人每模板限领一张；与请求幂等键分别承担不同约束
    UNIQUE KEY `uk_user_template` (`user_id`, `template_id`),
    -- 新人终身领取判断：按租户、用户和发券时人群快照查询，不看状态/有效期
    KEY `idx_user_crowd` (`tenant_id`, `user_id`, `crowd_type`),
    -- 到期提醒扫描：按状态 + 失效时间范围捞，覆盖 user_id、coupon_id，不回表
    KEY `idx_status_valid` (`status`, `valid_end_time`, `user_id`, `coupon_id`),
    -- 订单取消、退款时按订单找券
    KEY `idx_order` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户券表';


-- -----------------------------------------------------------------------------
-- 5. 用户券状态流水：每次状态变更写一行（领券、锁定、解锁、核销、退回；过期是推导状态，部分退款不改状态，都不写流水）
--
-- 不能省：
--   1. 幂等：request_no 唯一索引，重复请求在 DB 层冲突
--   2. 排查：券卡在已锁定时，只有流水能看出是哪个订单、什么时候锁的
--   3. 补偿：状态变更后发 MQ 失败，按流水重发（替代本地消息表）
--   4. 对账：财务按流水核对发放与核销
-- -----------------------------------------------------------------------------
CREATE TABLE `trade_user_coupon_log` (
    `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `coupon_id`     BIGINT UNSIGNED NOT NULL COMMENT '券ID',
    `user_id`       VARCHAR(64)     NOT NULL COMMENT '用户ID',
    `template_id`   BIGINT UNSIGNED NOT NULL COMMENT '券模板ID',
    `action`        VARCHAR(16)     NOT NULL COMMENT '动作：ISSUE领券 LOCK下单锁定 UNLOCK超时或取消解锁 USE支付核销 RETURN整单退款退回',
    `before_status` TINYINT         DEFAULT NULL COMMENT '变更前状态',
    `after_status`  TINYINT         NOT NULL COMMENT '变更后状态',
    `order_id`      VARCHAR(64)     DEFAULT NULL COMMENT '关联订单ID',
    `request_no`    VARCHAR(64)     NOT NULL COMMENT '幂等号',
    `mq_sent`       TINYINT(1)      NOT NULL DEFAULT 0 COMMENT 'MQ 是否已发送：0未发送 1已发送，补偿任务按此重发',
    `operator`      VARCHAR(64)     DEFAULT NULL COMMENT '操作人，系统触发填 SYSTEM',
    `remark`        VARCHAR(255)    DEFAULT NULL COMMENT '备注',
    `create_time`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_request_no` (`request_no`),
    KEY `idx_coupon_id` (`coupon_id`),
    KEY `idx_order` (`order_id`),
    KEY `idx_mq_sent` (`mq_sent`, `create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户券状态流水表';


-- =============================================================================
-- 三、人群灌券
-- =============================================================================
--
-- 运营上传用户名单，系统替这批用户领券。MVP 仅允许 issue_channel=7，复用领券的全部规则，不另起一套：
--   库存        本期不限量，不扣库存，不建设 trade_coupon_stock
--   每人限领    按 user_id 批量查出这组人各自已领张数
--   生效人群    渠道 7 不校验新人/全部用户资格，任务名单即最终发放对象
--   幂等        用户券 request_no = 任务ID_用户ID，任务重跑不会重复发
--   流水        每发一张写一条 ISSUE，与系统自动领取相同
--
-- 执行流程：
--   1. 创建任务：仅允许 issue_channel=7 且未作废，名单去重后最多 100 人，写任务和明细（明细 status=0）。
--   2. 当前请求内按 user_id 顺序 for 循环执行；先批量读取模板和用户已有券，不使用线程池或异步执行器。
--   3. 每个用户独立事务：校验领取窗口、有效期、每人限领和请求幂等，写用户券和 ISSUE 流水，回写当前明细。
--      重复键标记幂等成功；当前用户真实失败只标记失败并继续下一个用户；本期不限量，不扣库存。
--   4. 循环结束后汇总 success_count/fail_count，并在创建接口返回最终状态。

-- -----------------------------------------------------------------------------
-- 6. 定向发券任务：一次上传名单 = 一个任务
--
-- 任务状态由操作推进，不是按时间推导的，所以直接存储。
-- 成功数、失败数由执行过程累加，任务完成后可用明细表 count 校对。
-- -----------------------------------------------------------------------------
CREATE TABLE `trade_coupon_issue_task` (
    `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `task_id`       BIGINT UNSIGNED NOT NULL COMMENT '任务ID，全局时间序列号左移分片位',
    `template_id`   BIGINT UNSIGNED NOT NULL COMMENT '券模板ID，只能是 issue_channel=7 人群灌券模板',
    `name`          VARCHAR(64)     NOT NULL COMMENT '任务名称，运营用',
    `source_file`   VARCHAR(255)    DEFAULT NULL COMMENT '上传的名单文件名，排查用',
    `total_count`   INT             NOT NULL DEFAULT 0 COMMENT '名单人数（去重后）',
    `success_count` INT             NOT NULL DEFAULT 0 COMMENT '发放成功人数',
    `fail_count`    INT             NOT NULL DEFAULT 0 COMMENT '发放失败人数',
    `status`        TINYINT         NOT NULL DEFAULT 0 COMMENT '状态：0待执行 1执行中 2已完成 3已取消',
    `start_time`    BIGINT          DEFAULT NULL COMMENT '开始执行时间，Unix epoch milliseconds',
    `finish_time`   BIGINT          DEFAULT NULL COMMENT '完成时间，Unix epoch milliseconds',
    `tenant_id`     VARCHAR(32)     NOT NULL COMMENT '租户ID',
    `create_by`     VARCHAR(64)     NOT NULL COMMENT '创建人，同时作为发券流水操作人',
    `create_time`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_task_id` (`task_id`),
    KEY `idx_template` (`template_id`),
    KEY `idx_tenant_id` (`tenant_id`, `id`),
    KEY `idx_tenant_status_create` (`tenant_id`, `status`, `create_time`, `task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='定向发券任务表';


-- -----------------------------------------------------------------------------
-- 7. 人群灌券明细：名单里一个用户一行
--
-- 用途：
--   1. 当前请求内顺序执行：先批量读取待处理用户，再逐项回写结果
--   2. 后置能力：失败名单导出、复杂重试不属于 MVP
--
-- (task_id, user_id) 唯一：同一任务里同一用户只发一次，上传名单时重复的用户在入库时去掉。
-- 一个用户发多张：MVP 一行发一张；需要一次发多张时加 issue_qty 列，request_no 后缀加序号。
-- -----------------------------------------------------------------------------
CREATE TABLE `trade_coupon_issue_task_item` (
    `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `task_id`       BIGINT UNSIGNED NOT NULL COMMENT '任务ID',
    `user_id`       VARCHAR(64)     NOT NULL COMMENT '用户ID',
    `status`        TINYINT         NOT NULL DEFAULT 0 COMMENT '状态：0待发放 1已发放 2发放失败',
    `fail_reason`   VARCHAR(32)     DEFAULT NULL COMMENT '失败原因码：STOCK_EMPTY库存不足 OVER_LIMIT超过每人限领 CROWD_MISMATCH不在生效人群 USER_INVALID用户不存在 TEMPLATE_INVALID模板已作废或不在发放时间 SYSTEM_ERROR系统异常',
    `coupon_id`     BIGINT UNSIGNED DEFAULT NULL COMMENT '发放成功后的券ID',
    `retry_count`   INT             NOT NULL DEFAULT 0 COMMENT '重试次数',
    `create_time`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_task_user` (`task_id`, `user_id`),
    -- 分批拉待发明细、导出失败名单
    KEY `idx_task_status` (`task_id`, `status`, `id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='定向发券明细表';


-- =============================================================================
-- 四、券到期提醒
-- =============================================================================
--
-- 方案：每天定时扫用户券表，到期前 N 天的可用券，按用户合并成一条提醒发出。
--   不在领券时预写通知记录：锁定、核销、退回都要同步删改通知，状态一多就容易对不上。
--   扫的是用户券表「当时的真实状态」，已锁定、已使用的券天然不会被提醒。
--
-- 这与「不用定时任务改状态」不冲突：定时任务只读用户券表、只写本表，不改任何券的状态。
--
-- 执行流程（XXL-Job 分片广播，每个执行器负责一部分用户券分表）：
--   1. 计算提醒窗口：today + N 天这一整天，N 来自 Nacos 配置（如 3 天），MVP 全局统一
--   2. 分片内按索引 idx_status_valid 扫：
--        SELECT user_id, coupon_id FROM trade_user_coupon_{shard}
--         WHERE status = 1 AND valid_end_time >= ? AND valid_end_time < ?
--         ORDER BY ... LIMIT 500   -- 按 (valid_end_time, user_id, coupon_id) 游标分页
--   3. 按 user_id 合并：一个用户当天只发一条，「您有 3 张券将于 11.19 到期」
--   4. 批量 INSERT IGNORE 本表，唯一键 (user_id, notify_date) 去重，任务重跑不会重复写
--   5. 只对本次新插入、status=0 的记录调用 PushService 发送，结果回写 status
--      推送开关先批量查（PushService.getPushSwitchWithSys 目前是单查，需补批量方法），
--      用户关闭推送的记为 3 已跳过，不调用发送
--
-- 为什么不选另外两种：
--   领券时写待通知记录  需要和锁定、解锁、核销、退回多处同步，漏一处就会提醒已用掉的券
--   延时消息            有效期可长达 180 天，大量长延时消息积压；券提前用掉还要撤销消息
--
-- 窗口用「今天 + N 天这一天」而不是「N 天以内」：每张券只会落进一天的窗口，
--   任务每天跑一次即可，不会因为窗口重叠在连续几天里重复提醒同一张券。
--   任务某天没跑：当天的券会漏提醒，补跑时传指定日期即可（参考 RecruitShowcaseNotifyJob 的 mockToday 参数）。

-- -----------------------------------------------------------------------------
-- 8. 券到期提醒记录：一个用户一天一行（后置，MVP 不执行）
--
-- 不按券一行：同一用户同一天多张券到期只发一条，按用户存，行数少，去重也简单。
-- 涉及哪些券记在 coupon_ids，客诉排查时能看出提醒了哪几张。
--
-- 记录保留 30 天，超期由清理任务物理删除，表不会无限增长。
-- -----------------------------------------------------------------------------
CREATE TABLE `trade_coupon_expire_notify` (
    `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `user_id`       VARCHAR(64)     NOT NULL COMMENT '用户ID',
    `notify_date`   DATE            NOT NULL COMMENT '提醒日期，同一用户同一天只提醒一次',
    `expire_date`   DATE            NOT NULL COMMENT '这批券的到期日期，即 notify_date + 提前天数',
    `coupon_count`  INT             NOT NULL COMMENT '即将到期的券张数',
    `coupon_ids`    JSON            NOT NULL COMMENT '即将到期的券ID列表，排查用，如 [1001,1002]',
    `channel`       TINYINT         NOT NULL DEFAULT 1 COMMENT '通知方式：1App推送（预留 2短信 3站内信）',
    `status`        TINYINT         NOT NULL DEFAULT 0 COMMENT '状态：0待发送 1已发送 2发送失败 3已跳过（用户关闭推送）',
    `fail_reason`   VARCHAR(255)    DEFAULT NULL COMMENT '发送失败原因',
    `retry_count`   INT             NOT NULL DEFAULT 0 COMMENT '重试次数',
    `send_time`     DATETIME        DEFAULT NULL COMMENT '实际发送时间',
    `tenant_id`     VARCHAR(32)     NOT NULL COMMENT '租户ID',
    `create_time`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
    PRIMARY KEY (`id`),
    -- 去重：任务重跑时 INSERT IGNORE 不会重复写
    UNIQUE KEY `uk_user_date` (`user_id`, `notify_date`),
    -- 重试失败的、清理过期记录
    KEY `idx_date_status` (`notify_date`, `status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='券到期提醒记录表';


-- =============================================================================
-- 五、订单侧
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 9. 订单优惠分摊明细：订单行 × 优惠来源，一行一条
--
-- 券抵扣的是整单金额，但退款、结算、开票都按商品行处理，
-- 所以下单时要把每笔优惠拆到订单行、再拆到件，存下来。
-- 退款时直接读这张表，不重新计算，避免券模板改动、商品改价后对不上账。
--
-- 分摊算法见 demo：trade-order-api/src/test/java/cn/mathmagic/jjewelry/coupon
-- -----------------------------------------------------------------------------
CREATE TABLE `trade_order_discount_detail` (
    `id`               BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `main_order_id`    VARCHAR(64)     NOT NULL COMMENT '主订单ID',
    `order_id`         VARCHAR(64)     NOT NULL COMMENT '子订单ID',
    `line_id`          VARCHAR(64)     DEFAULT NULL COMMENT '订单行ID',
    `sku_id`           VARCHAR(64)     NOT NULL COMMENT 'SKU ID',

    -- 优惠来源
    `layer_code`       VARCHAR(32)     NOT NULL COMMENT '优惠层：PROMOTION单品促销 SHOP_COUPON商家券 PLATFORM_COUPON平台券',
    `layer_order`      INT             NOT NULL DEFAULT 0 COMMENT '执行顺序，按此可还原计算过程',
    `template_id`      BIGINT UNSIGNED DEFAULT NULL COMMENT '券模板ID，券类优惠有值',
    `coupon_id`        BIGINT UNSIGNED DEFAULT NULL COMMENT '券ID，券类优惠有值',
    `promotion_id`     VARCHAR(64)     DEFAULT NULL COMMENT '促销活动ID，非券类优惠有值',
    `funder`           TINYINT         NOT NULL COMMENT '成本承担方：1平台 2商家',

    -- 金额
    `original_amount`  DECIMAL(12,2)   NOT NULL COMMENT '本行原价金额',
    `amount`           DECIMAL(12,2)   NOT NULL COMMENT '分摊到本行的减免额',
    `unit_amount_json` JSON            DEFAULT NULL COMMENT '按件分摊明细，如 [4.28,4.28,4.30]，退款按件读',
    `refunded_amount`  DECIMAL(12,2)   NOT NULL DEFAULT 0 COMMENT '已退分摊额，累计值',

    `create_time`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_time`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
    PRIMARY KEY (`id`),
    KEY `idx_order` (`order_id`),
    KEY `idx_main_order` (`main_order_id`),
    KEY `idx_coupon` (`coupon_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单优惠分摊明细表';


-- =============================================================================
-- 六、分库分表
-- =============================================================================
--
-- 只有 trade_user_coupon 分表，其余表数据量可控。
--
--   table_index = hash(user_id) % table_count
--   table_name  = trade_user_coupon_{table_index}
--
-- 按 user_id 分：用户券列表、下单用券、每人限领校验都按 user_id 查，都在单分片内。
--
-- 后台按券ID coupon_id 反查分片：不建路由表，把分片号编进全局序列 ID 低位。
--   8 张表取低 3 位：coupon_id = (globalSeq << 3) | (hash(user_id) % 8)
--   反查：shard = coupon_id & 0b111
--   前提是分片数稳定，扩分片需要兼容旧 ID 的路由。
--
-- 建表脚本按分片数批量生成，示例（8 张表）：
--   for i in $(seq -w 0 7); do
--     sed 's/`trade_user_coupon`/`trade_user_coupon_'${i}'`/g' \
--       trade_user_coupon.sql | mysql -h$HOST -u$USER -p$DB
--   done


-- =============================================================================
-- 七、扩展点（都是加列、加枚举值或加表，不改已有字段）
-- =============================================================================
--
-- 1. 新优惠方式（一口价、包邮）
--    trade_coupon_template.discount_type 加枚举值，算价引擎加一个对应的计算实现。
--
-- 2. 新推广渠道
--    trade_coupon_template.issue_channel 加枚举值。
--
-- 2a. 新生效人群（老用户、学生/老师、指定标签等）
--    trade_coupon_template.crowd_type 加枚举值；需要参数的人群（如标签）再加一列，
--    校验逻辑按 crowd_type 分策略实现，可复用 usertag 下现有的 UserTagStrategy。
--
-- 3. 新范围维度（SKU、类目）
--    trade_coupon_scope.scope_type 加枚举值，直接写数据。
--
-- 3a. 去掉 trade_coupon_scope，适用商品改为模板上的数组列（暂不做）
--    参考微信支付代金券：适用商品是模板上的 available_items 数组，最多 50 个，不建关联表。
--    做法：trade_coupon_template 加 scope_item_ids JSON 列，应用层限制商品数量上限，
--          按商品反查可用券时从缓存的在发模板里内存过滤。
--    MySQL 8.0.17+ 可在该列上建多值索引，用 MEMBER OF 查询。
--
-- 4. 热点券库存分桶
--    trade_coupon_stock 按 bucket_no 写多行，领券随机选桶，表结构不变。
--
-- 5. 每日限领
--    trade_coupon_template 加 max_per_user_daily 列，
--    扩展多张时移除 uk_user_template，改用用户-模板配额/计数控制，不复用本唯一键。
--
-- 6. 同一模板分多期发放
--    新建批次表 trade_coupon_batch(batch_id, template_id, receive_*, ...)，
--    把模板上的领取时间挪过去，trade_coupon_stock 和 trade_user_coupon 加 batch_id 列。
--    模板ID、规则字段、范围表都不用改。
--
-- 7. 暂停发放
--    trade_coupon_template.status 加 3已暂停，可与 1正常 互相切换，推导规则把 3 当作「已暂停」最先判断。
--
-- 8. 互斥组、预算、人群标签、退款策略
--    trade_coupon_template 加 mutex_group / budget_amount / user_tag_name / refund_policy 列。


-- =============================================================================
-- 八、已确认的决策
-- =============================================================================
--
-- 1. 使用时间「与领取时间相同」：领取时刻生效，receive_end_time 失效。
-- 2. 统计：MVP 不建 trade_coupon_stock，也不做已发放/已核销统计任务；后续可按流水建设。
-- 3. 每人限领：不建计数表，查用户券表单分片 count。
-- 4. 券模板状态：只存 1正常 2已作废，未开始/生效中/已过期按领取时间推导，不用定时任务改状态。
--    只有未开始、生效中能作废，已过期不能作废。
--    券状态：只存 1未使用 2已锁定 3已使用，已过期按 valid_end_time 推导。券没有作废态。
-- 5. 券模板作废：不可恢复，不能再领，用户已领的券不受影响。
-- 6. 新用户：不是推广方式，是优惠设置里的生效人群 crowd_type=1，只支持「注册后 N 天内」，只在领取时校验，注册时间从本地 biz_user.create_time 批量读取。
-- 7. 适用范围：保留 trade_coupon_scope 关联表，暂不改成模板上的数组列（备选方案见扩展点 3a）。
-- 8. 发券渠道：1～5 默认全部用户，6 人群惊喜券（本期仅新人自动领取），7 人群灌券；仅渠道 7 使用任务表 + 明细表记录名单和每人结果。
-- 9. 到期提醒：后置，不属于 MVP；trade_coupon_expire_notify 仅保留在上方完整设计参考中。
--
--
-- =============================================================================
-- 九、待确认
-- =============================================================================
--
-- 1. 领取时间 <= 180 天：当前在应用层校验，不加 DB 约束。
-- 2. 部分退款后不满足门槛是否追回优惠：MVP 建议固定「不追回」。
-- 3. 注册时间：MVP 使用订单服务本地 biz_user.create_time，后续再评估跨服务来源。
-- 4. 人群灌券：MVP 直接接收去重后的 userIds（最多 100 人）；CSV、失败导出和复杂重试后置。
