代码问题
1. trade_coupon_template表的写操作，在trade_operation_log表中记录，void_time,void_by,void_reason是不是可以放到在trade_operation_log表中记录表？
2. cn/mathmagic/jjewelry/business/couponv1/service/template/CouponTemplateAssembler.java:130这一行为什么不用枚举？代码写的一坨死，为什么在数据库对象里写常量？给我个合理理由
3. cn.mathmagic.jjewelry.client.read.CouponV1InternalReadClient 这个接口补充search接口，按照表接口字段和你的理解，直接分页查询数据库，没有查询条件时，查询所有数据，返回数据也是返回表所有字段
4. 券模板name和description的含义是，name只在运营平台用，description 是给C端用户看的，这个需要修改下，有疑问询问我
5. cn.mathmagic.jjewelry.client.read.CouponV1InternalReadClient 这个接口补充search接口，按照表接口字段和你的理解，直接分页查询数据库，没有查询条件时，查询所有数据，返回数据也是返回表所有字段
6. cn.mathmagic.jjewelry.dto.couponv1.CouponIssueTaskItemResponse#failReason 改为failReasonCode，表示现在错误码，加一个failReason字段表示错误原因，中文描述，运营能看得懂
7. cn/mathmagic/jjewelry/business/couponv1/cache/CouponTemplateCache.java:53 计算ttl时间，如果都用时间戳，是不是就不用考虑时区了？更简单？
8. 券模板ttl时间有必要这么准么？是不是可以延长下ttl时间，避免卡在极端时间点？
9. cn/mathmagic/jjewelry/business/couponv1/model/CouponTemplateSnapshot.java:65 魔法值改一下，把本分支所有代码检查下
10. cn/mathmagic/jjewelry/business/couponv1/cache/CouponTemplateCache.java:63 放在一个script里执行啊，原子性懂不懂？不懂可以问我
11. cn.mathmagic.jjewelry.business.controller.couponv1.CouponV1Controller#history 历史优惠券接口，status传null是表示查询所有历史优惠券，而不是所有优惠券，历史优惠券定义，你翻下技术文档，和产品需求文档，大致是：已使用：优惠券已成功核销，已过期：已超过有效期，且未使用，已锁定：订单是待支付状态锁定了优惠券，你和产品需求文档，技术设计文档对一下，不一致可以询问我
12. cn/mathmagic/jjewelry/business/couponv1/service/issue/ArrivalPopupServiceImpl.java:164 分布式锁用工具类里面的啊，看下这个 cn.mathmagic.jjewelry.business.common.service.RedissonDistributedLocker