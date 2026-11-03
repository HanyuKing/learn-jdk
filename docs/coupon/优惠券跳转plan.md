# 魔法师：优化优惠券单商品跳转解析

## Summary

将跳转解析改为“缓存摘要 + 商品路由策略注册表”：

- 运行时不再查询 `trade_coupon_scope`，也不加载全部商品范围。
- Redis 模板、商品范围、范围摘要、人群索引由同一个 Lua 脚本原子写入。
- 商品详情页和兜底列表页由商品类型策略决定。
- 商品不可用时跳转对应商品类型列表页；只有无法确定路由时才使用通用列表页。

## Redis 原子缓存设计

在现有范围 key 外新增摘要 key：

```text
coupon:v1:template-v2:{tenantId}:scope-summary:{templateId}
```

摘要只保存：

```json
{
  "cardinality": 0,
  "productId": "P1001",
  "routeType": 3
}
```

约定：

- `cardinality=0`：无范围或全店券。
- `cardinality=1`：只有一个商品，保存 `productId`。
- `cardinality=2`：两个及以上商品，不保存全部 ID。
- `routeType`：范围商品类型一致时保存对应 `TradeProductTypeEnum`；类型混合或未知时为空。

修改 `CouponTemplateCache.PUT_SCRIPT`：

- `KEYS[1]`：模板规则。
- `KEYS[2]`：人群索引或 noop key。
- `KEYS[3]`：完整商品范围。
- `KEYS[4]`：商品范围摘要。
- 模板、人群索引、完整范围、摘要及 TTL 在同一次 Lua 执行中完成。
- 没有商品范围时，由脚本同时删除完整范围 key 和摘要 key。
- 所有 key 继续使用 `{tenantId}`，保证 Redis Cluster 下位于同一 hash slot。
- 禁止在脚本外额外 `SET` 摘要或单独设置 TTL。
- 脚本成功后再清理当前实例的完整范围 Caffeine 缓存和摘要 Caffeine 缓存。

新增：

```java
Map<Long, CouponScopeJumpSummary> multiGetScopeSummaries(
        String tenantId, Collection<Long> templateIds);
```

摘要本地缓存继续使用 Caffeine，TTL 和容量与现有范围缓存保持一致。摘要缺失、非法或 Redis 异常时返回未知状态，不回源 `trade_coupon_scope`。

模板创建校验时已经批量读取商品基础信息，将商品类型元数据写入运行时快照，用于生成摘要；不新增运行时数据库查询。算价链路继续使用现有完整范围缓存。

## 商品跳转策略

新增策略接口和注册表：

```java
public interface ProductJumpRouteStrategy {

    TradeProductTypeEnum productType();

    String detailUrl(String productId);

    String listUrl();
}
```

由 `ProductJumpRouteRegistry` 按 `TradeProductTypeEnum` 查找策略，`CouponJumpResolver` 不再直接拼接 URL，也不写商品类型分支。

默认配置：

| 商品类型 | 详情页 | 列表页 |
|---|---|---|
| WANT(1) | `/product/query?productId={productId}` | `/product/list?productType=1` |
| TEMPLATE(2) | `/product/query?productId={productId}` | `/product/list?productType=2` |
| SHOP(3) | `/product/query?productId={productId}` | `/product/list?productType=3` |
| CROWDFUNDING(4) | `/product/query?productId={productId}` | `/product/crowdfunding/v2/feed` |
| DEFAULT/未知 | 无详情路由 | `/product/list` |

路由模板放入优惠券配置中，支持 Nacos 动态覆盖；代码内保留上述默认值。配置加载时校验详情模板必须包含 `{productId}`，非法配置回退默认策略。

## CouponJumpResolver 行为

修改接口：

```java
forUserCoupons(String tenantId, List<UserCouponEntity> coupons, Long now)
```

同步修改列表、历史和到账弹窗调用方。`forCandidates` 从候选模板的 `tenantId` 获取缓存上下文。

处理规则：

1. 先过滤有效期内的 `UNUSED/LOCKED` 指定商品券。
2. 按租户和模板 ID 批量读取范围摘要。
3. `cardinality=0`：
    - 视为全店券或范围为空。
    - 使用通用商品列表页。
    - 不查询商品。
4. `cardinality=2`：
    - 若摘要中有统一 `routeType`，跳该类型列表页。
    - 类型混合或未知时跳 `/product/list`。
    - 不查询商品。
5. `cardinality=1`：
    - 只查询该商品的轻量元数据：`product_id、product_type、status、closed`。
    - 商品已发布且未删除：使用对应策略的详情页。
    - 商品下架、删除或不存在：使用对应策略的列表页。
    - 商品类型未知时使用通用列表页。
6. 商品查询按商品 ID 去重；同一商品被多个模板引用时只查询一次。
7. Redis、商品查询或路由配置异常时，记录日志并使用通用商品列表页。
8. 只要存在可用的列表兜底 URL，`cannotJumpText` 置空，避免现有前端协议因非空文案阻断跳转；只有连通用列表路由也无法生成时才返回 `jumpUrl=null` 和不可跳转文案。

在 `TradeProductRepository` 增加只返回商品跳转元数据的批量查询，避免加载完整 `Product` 实体。跳转解析器完全移除 `CouponScopeRepository` 依赖。

## Test Plan

- Lua 脚本测试：
    - 一次执行同时写入模板、范围、摘要和人群索引。
    - 所有 key 使用相同租户 hash tag。
    - 全店券同时删除范围和摘要。
    - 所有 TTL 使用同一个过期时间。
    - Redis 脚本失败时不清理本地缓存。
- 摘要缓存测试：
    - 空范围、单商品、多商品、同类型多商品、混合类型多商品。
    - 本地缓存命中不访问 Redis。
    - 摘要缺失或非法时不访问 `CouponScopeRepository`。
- 跳转策略测试：
    - WANT、TEMPLATE、SHOP、CROWDFUNDING 的详情和列表路由。
    - 配置覆盖和非法配置回退。
    - 未发布、已删除、不存在商品均跳对应列表页。
    - 未知类型跳通用商品列表。
- Resolver 测试：
    - 全店和多商品不查询商品。
    - 单商品只查询一次轻量元数据。
    - 多券共享商品时查询去重。
    - 依赖异常仍返回列表兜底。
    - 已使用、已过期和无效券不触发范围或商品查询。
    - 更新后的 `tenantId` 参数在列表、历史、到账弹窗和标签链路中正确传递。
- 运行 `CouponJumpResolverTest`、`CouponTemplateCacheTest` 及 coupon 模块相关测试。

## Assumptions

- 商品类型以 `TradeProductTypeEnum` 为准。
- 多商品范围只有在类型明确一致时才跳对应类型列表；类型混合时使用通用商品列表。
- 商品详情和列表 URL先采用上述默认值，前端最终路由通过配置覆盖。
- Redis Lua 原子性只保证 Redis 内部多 key 写入一致；本地 Caffeine 失效在脚本成功后执行，失败时保留旧缓存。
