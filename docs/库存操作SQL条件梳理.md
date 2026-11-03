# 库存操作 SQL 条件与变更关系梳理

## 库存模型说明

- **sq**: 总库存 (Sale Quantity)
- **lq**: 锁定库存 (Lock Quantity) - 已下单但未支付的数量
- **oq**: 已用库存 (Occupied Quantity) - 已支付的数量
- **可用库存**: 当前可售的数量

## 当前实现 vs 测试注释的语义冲突

### 测试注释的语义（testDeductStock_Success:150-151）
> "Locked" 的语义通常意味着 "总预留量 (包含已售出的)"
> 扣减时：lq 保持不变，只增加 oq

**含义**：`lq` 包含已转为 `oq` 的部分（有重叠）

### 当前 SQL 条件的语义
- LOCK: `(sq - lq - oq) >= qty` → 假设 lq 和 oq **不重叠**
- DECREASE: `(sq - lq - oq) >= qty` → 假设 lq 和 oq **不重叠**

**含义**：`lq` 和 `oq` 是独立的（无重叠）

---

## 所有 OrderAction 的 SQL 条件与变更关系

### 1. LOCK（锁库存）- 下单时

**SQL 变更**：
```sql
UPDATE trade_pd_stock 
SET lq = lq + #{qty}
WHERE (sq - lq - oq) >= #{qty}
```

**字段变更**：
- `lq += qty`
- `sq` 不变
- `oq` 不变

**SQL 条件**：`(sq - lq - oq) >= qty`
- **当前语义**：可用库存 = sq - lq - oq（假设 lq 和 oq 不重叠）
- **测试注释语义**：如果 lq 包含已售出，条件应为 `(sq - lq) >= qty`

**策略校验**：`stockInfoDto.getAvailable() >= qty`（依赖 getAvailable 的实现）

---

### 2. UNLOCK（解锁库存）- 取消/超时

**SQL 变更**：
```sql
UPDATE trade_pd_stock 
SET lq = lq - #{qty}
WHERE lq >= #{qty}
```

**字段变更**：
- `lq -= qty`
- `sq` 不变
- `oq` 不变

**SQL 条件**：`lq >= qty`
- **语义一致**：确保有足够的锁定库存可以解锁

**策略校验**：`stockInfoDto.getLq() >= qty`

---

### 3. DEDUCT（扣减库存）- 支付成功

**SQL 变更**：
```sql
UPDATE trade_pd_stock 
SET oq = oq + #{qty}
WHERE (lq - oq) >= #{qty}
```

**字段变更**：
- `oq += qty`
- `lq` **不变**（关键！）
- `sq` 不变

**SQL 条件**：`(lq - oq) >= qty`
- **语义**：确保有足够的"锁定但未支付"库存可以扣减
- **含义**：`lq - oq` = 锁定但未支付的数量

**策略校验**：`(lq - oq) >= qty`

**测试注释说明**：
> lq 保持 20，因为扣减逻辑通常是: 检查 (lq-oq)>=qty，然后 oq+=qty。
> 它不会减少 LQ。 "Locked" 的语义通常意味着 "总预留量 (包含已售出的)"

**矛盾点**：
- 如果 lq 包含已售出部分，那么 `lq - oq` 表示"锁定但未支付"
- 但 LOCK 条件 `(sq - lq - oq) >= qty` 假设 lq 和 oq 不重叠

---

### 4. RELEASE（释放库存）- 退款

**SQL 变更**：
```sql
UPDATE trade_pd_stock 
SET oq = oq - #{qty}
WHERE oq >= #{qty}
```

**字段变更**：
- `oq -= qty`
- `sq` 不变
- `lq` 不变

**SQL 条件**：`oq >= qty`
- **语义一致**：确保有足够的已用库存可以释放

**策略校验**：`stockInfoDto.getOq() >= qty`

---

### 5. INCREASE（增加总库存）- 运营后台

**SQL 变更**：
```sql
UPDATE trade_pd_stock 
SET sq = sq + #{qty}
WHERE 1=1 (无条件)
```

**字段变更**：
- `sq += qty`
- `lq` 不变
- `oq` 不变

**SQL 条件**：无（直接增加）

---

### 6. DECREASE（减少总库存）- 运营后台

**SQL 变更**：
```sql
UPDATE trade_pd_stock 
SET sq = sq - #{qty}
WHERE (sq - lq - oq) >= #{qty}
```

**字段变更**：
- `sq -= qty`
- `lq` 不变
- `oq` 不变

**SQL 条件**：`(sq - lq - oq) >= qty`
- **语义**：确保可售库存足够才允许减少总库存
- **假设**：lq 和 oq 不重叠

---

### 7. ADJUST（设置可售库存）- 运营后台（initStock）

**SQL 变更**：
```sql
-- INSERT: sq = qty, lq = 0, oq = 0
-- UPDATE: sq = lq + oq + #{qty}
ON DUPLICATE KEY UPDATE sq = lq + oq + VALUES(sq)
```

**字段变更**：
- `sq = lq + oq + newAvailable`
- `lq` 不变
- `oq` 不变

**SQL 条件**：无（直接设置）

**语义**：设置新的可售库存 = `sq - lq - oq = newAvailable`
- **假设**：lq 和 oq 不重叠

---

## 可用库存计算公式

### 当前实现（StockInfoDto.getAvailable）
```java
// 方案A：假设 lq 和 oq 不重叠
available = sq - lq - oq
```

### 测试注释语义（如果 lq 包含已售出）
```java
// 方案B：假设 lq 包含已转为 oq 的部分
available = sq - lq
```

---

## 矛盾点总结

| 操作 | SQL条件 | 当前语义 | 测试注释语义 | 矛盾 |
|------|---------|----------|--------------|------|
| **LOCK** | `(sq - lq - oq) >= qty` | lq 和 oq 不重叠 | lq 包含已售出 | ✅ 矛盾 |
| **DEDUCT** | `(lq - oq) >= qty` | lq - oq = 锁定未支付 | lq 包含已售出 | ✅ 一致 |
| **DECREASE** | `(sq - lq - oq) >= qty` | lq 和 oq 不重叠 | lq 包含已售出 | ✅ 矛盾 |
| **ADJUST** | `sq = lq + oq + newAvailable` | lq 和 oq 不重叠 | lq 包含已售出 | ✅ 矛盾 |

---

## 两种语义方案对比

### 方案A：lq 和 oq 不重叠（当前 SQL 实现）

**语义**：
- `lq` = 锁定但未支付的数量
- `oq` = 已支付的数量
- `lq` 和 `oq` 互斥

**可用库存**：`available = sq - lq - oq`

**DEDUCT 后**：
- 初始：sq=100, lq=20, oq=0 → 可用=80
- 扣减10：sq=100, lq=10, oq=10 → 可用=80
- **问题**：DEDUCT 时 lq 应该减少，但当前实现 lq 不变！

**需要修改**：
- DEDUCT SQL: `lq = lq - qty, oq = oq + qty`（同时变更）
- 条件：`lq >= qty`（检查是否有足够的锁定库存）

---

### 方案B：lq 包含已售出（测试注释语义）

**语义**：
- `lq` = 总预留量（包含已支付和未支付）
- `oq` = 已支付的数量
- `lq` 包含 `oq`（有重叠）

**可用库存**：`available = sq - lq`

**DEDUCT 后**：
- 初始：sq=100, lq=20, oq=0 → 可用=80
- 扣减10：sq=100, lq=20, oq=10 → 可用=80 ✅
- **符合测试注释**：lq 保持不变

**需要修改**：
- LOCK SQL 条件：`(sq - lq) >= qty`
- DECREASE SQL 条件：`(sq - lq) >= qty`
- ADJUST SQL：`sq = lq + newAvailable`（不需要 + oq）

---

## 建议

根据测试注释和 DEDUCT 的实现（lq 不变），**建议采用方案B**：

1. **lq 语义**：总预留量（包含已支付和未支付）
2. **可用库存**：`available = sq - lq`
3. **需要修改的 SQL**：
   - LOCK: `(sq - lq) >= qty`
   - DECREASE: `(sq - lq) >= qty`
   - ADJUST: `sq = lq + newAvailable`

请确认采用哪种方案，我将统一修改所有相关代码。
