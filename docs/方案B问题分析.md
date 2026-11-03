# 方案B问题分析

## 方案B的语义

- **lq** = 总预留量（包含已支付和未支付的部分）
- **oq** = 已支付的数量
- **lq 包含 oq**（有重叠）
- **可用库存** = `sq - lq`

---

## 问题1：UNLOCK 操作的语义混乱

### 场景
- 初始：sq=100, lq=20, oq=10（已锁定20，其中10个已支付）
- 执行 UNLOCK(10)

### 当前SQL（方案B）
```sql
UPDATE trade_pd_stock SET lq = lq - 10
WHERE lq >= 10
```

### 结果
- sq=100, lq=10, oq=10

### 问题
1. **语义不清**：解锁的10个是"未支付的10个"还是"总预留的10个"？
2. **如果解锁的是未支付部分**：
   - 应该：lq=10（总预留20，解锁未支付10，剩余未支付10）
   - 但结果：lq=10, oq=10 → `lq - oq = 0`（没有未支付的了）
   - **矛盾**：如果还有10个已支付，那lq应该至少是10
3. **如果解锁的是总预留**：
   - 可能解锁了已支付的库存，导致 oq > lq 的异常状态

### 正确的逻辑应该是？
- 方案B下，UNLOCK应该只解锁"未支付"的部分
- 但当前SQL无法区分，只能减少lq
- **需要修改**：UNLOCK条件改为 `(lq - oq) >= qty`，确保只解锁未支付部分

---

## 问题2：RELEASE 操作后 lq 不变导致数据不一致

### 场景
- 初始：sq=100, lq=20, oq=20（全部已支付）
- 执行 RELEASE(10)（退款10个）

### 当前SQL（方案B）
```sql
UPDATE trade_pd_stock SET oq = oq - 10
WHERE oq >= 10
```

### 结果
- sq=100, lq=20, oq=10

### 问题
1. **数据不一致**：
   - lq=20 表示总预留20个
   - oq=10 表示已支付10个
   - `lq - oq = 10` 表示还有10个未支付
   - **但实际**：这10个已经退款了，不应该再算在lq中
2. **可用库存错误**：
   - `available = sq - lq = 100 - 20 = 80`
   - **但实际可用**：应该更多（因为退款了10个）

### 正确的逻辑应该是？
- 方案B下，RELEASE后lq应该也要减少
- **需要修改**：RELEASE SQL改为 `lq = lq - qty, oq = oq - qty`

---

## 问题3：LOCK 条件与可用库存计算不一致

### 当前SQL
```sql
UPDATE trade_pd_stock SET lq = lq + 10
WHERE (sq - lq - oq) >= 10
```

### 问题
- 如果lq包含oq，那么 `sq - lq - oq` 会**重复扣除oq**
- 应该改为：`(sq - lq) >= 10`

### 示例
- 初始：sq=100, lq=20, oq=10
- 当前条件：`(100 - 20 - 10) = 70 >= 10` ✅
- 方案B条件：`(100 - 20) = 80 >= 10` ✅
- **差异**：当前条件更严格（多扣了10）

---

## 问题4：ADJUST 操作的公式错误

### 当前SQL
```sql
ON DUPLICATE KEY UPDATE sq = lq + oq + VALUES(sq)
```

### 问题
- 如果lq包含oq，那么 `lq + oq` 会**重复计算oq**
- 应该改为：`sq = lq + VALUES(sq)`

### 示例
- 当前：lq=20, oq=10，设置可售=50
- 当前公式：`sq = 20 + 10 + 50 = 80`
- 方案B公式：`sq = 20 + 50 = 70`
- **差异**：当前公式多加了10

---

## 问题5：DECREASE 操作的条件错误

### 当前SQL
```sql
UPDATE trade_pd_stock SET sq = sq - 10
WHERE (sq - lq - oq) >= 10
```

### 问题
- 如果lq包含oq，条件应该改为：`(sq - lq) >= 10`

---

## 方案B需要修改的地方总结

| 操作 | 当前SQL/条件 | 方案B应该改为 | 问题 |
|------|-------------|--------------|------|
| **LOCK** | `(sq - lq - oq) >= qty` | `(sq - lq) >= qty` | 重复扣除oq |
| **UNLOCK** | `lq = lq - qty`<br>`lq >= qty` | `lq = lq - qty`<br>`(lq - oq) >= qty` | 可能解锁已支付部分 |
| **RELEASE** | `oq = oq - qty`<br>`lq不变` | `lq = lq - qty, oq = oq - qty` | lq包含已释放部分 |
| **DECREASE** | `(sq - lq - oq) >= qty` | `(sq - lq) >= qty` | 重复扣除oq |
| **ADJUST** | `sq = lq + oq + newAvailable` | `sq = lq + newAvailable` | 重复计算oq |

---

## 方案B的核心问题

### 1. UNLOCK 和 RELEASE 的语义冲突

**UNLOCK（取消订单）**：
- 应该只释放"未支付"的锁定
- 但lq包含已支付部分，无法直接区分

**RELEASE（退款）**：
- 应该释放"已支付"的库存
- 但lq包含已支付部分，释放后lq不变会导致数据不一致

### 2. 数据一致性问题

**场景**：LOCK(20) → DEDUCT(20) → RELEASE(10)
- 步骤1：sq=100, lq=20, oq=0
- 步骤2：sq=100, lq=20, oq=20（lq不变）
- 步骤3：sq=100, lq=20, oq=10（lq不变）

**问题**：
- lq=20 表示总预留20
- 但实际只有10个已支付，10个已退款
- lq应该减少到10，否则数据不一致

---

## 方案B的修复方案

如果要采用方案B，需要：

1. **LOCK**：条件改为 `(sq - lq) >= qty`
2. **UNLOCK**：条件改为 `(lq - oq) >= qty`（只解锁未支付部分）
3. **RELEASE**：SQL改为 `lq = lq - qty, oq = oq - qty`（同时减少）
4. **DECREASE**：条件改为 `(sq - lq) >= qty`
5. **ADJUST**：SQL改为 `sq = lq + newAvailable`

**但这样修改后，RELEASE和UNLOCK的逻辑会变得复杂，且容易出错。**

---

## 结论

**方案B存在以下问题**：
1. ✅ UNLOCK语义不清（无法区分解锁的是已支付还是未支付）
2. ✅ RELEASE后lq不变导致数据不一致
3. ✅ 多个SQL条件需要修改（LOCK、DECREASE、ADJUST）
4. ✅ 业务逻辑复杂，容易出错

**建议**：采用方案A（lq和oq不重叠），语义清晰，逻辑简单。
