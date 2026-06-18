# 后端服务语言适配（i18n）方案与落地手册

> 适用范围：本仓库所有 Java/Spring Boot 微服务
> 目标读者：架构评审同事 + 一线开发同事
> 维护者：架构组
> 版本：v1.0（2026-06-18）

---

## 目录

1. [概述](#1-概述)
2. [现状盘点](#2-现状盘点)
3. [整体方案](#3-整体方案)
4. [立即可执行的代码改造（Before / After）](#4-立即可执行的代码改造before--after)
5. [各服务接入步骤（统一 5 步法）](#5-各服务接入步骤统一-5-步法)
6. [跨服务调用链路 Locale 透传](#6-跨服务调用链路-locale-透传)
7. [文案 key 命名规范](#7-文案-key-命名规范)
8. [扩展新语言的标准流程](#8-扩展新语言的标准流程)
9. [验证与回归](#9-验证与回归)
10. [里程碑与分批落地](#10-里程碑与分批落地)
11. [附录](#11-附录)

---

## 1. 概述

### 1.1 Why
社区电商业务正在从国内向**美国市场**扩展，后端 12 个 Java/Spring Boot 微服务需完成"语言适配"，以便在美国集群默认输出英文、在国内集群默认输出中文，且预留扩展第三种语言（如西班牙语、日语）的能力。

### 1.2 Scope（本文档覆盖）
- 后端 API 返回给前端 / App / 小程序的**用户可见文案**：错误码 message、业务提示、推送 / SMS / 邮件模板
- Locale 解析、传递、回退策略
- 文案 key 命名规范、资源文件管理
- 各服务接入步骤、CI 校验

### 1.3 Out of Scope（本文档不覆盖）
- 三方供应商替换（火山 SMS → Twilio / 极光 Push → FCM）
- 时区、货币、日期、数字格式本地化（L10n）
- 数据库长文本字段翻译策略（商品名 / 类目 / 营销文案）
- 美区合规、隐私协议、Cookie 横幅
- 前端 i18n（仅后端 API 返回的 msg）
- AIGC prompt 内容本地化（仅给出 Nacos key 命名建议，详见 §7.3）

### 1.4 核心原则（贯穿全文）
1. **代码中禁止出现面向用户的中文（或任何语种）字面量** —— 一律抽到 `messages_*.properties`
2. **`messages_zh.properties` 的 value 是原封不动的中文原文**（不做改写、不做润色）
3. **`messages_en.properties` 等其它语种文件**的 value 是对应语种的翻译
4. 所有语言文件**必须保持 key 集合严格一致**（CI 门禁）
5. 三级回退：请求 Locale → 默认 Locale (`en-US`) → 英文兜底字面量
6. Locale 通过 `Accept-Language` 进入，通过 `RequestHeaderContext`（ThreadLocal）在进程内传递，通过 Feign Header / MQ Header 跨服务传递

---

## 2. 现状盘点

### 2.1 已有 i18n 基建（部分服务已接入）

| 服务 | i18n 资源文件 | LocaleConfig | HeaderInterceptor | GlobalExceptionHandler |
|---|---|---|---|---|
| `user-center/uc-api-common` | ✅ `messages_{zh,en}.properties` | ✅ | ✅ | ✅ |
| `jjewelry-java/jjewelry-api-common` | ✅ `messages_{zh,en}.properties` | ✅（同结构） | ✅ | ✅ |
| `order-java/trade-order-service` | ✅ `messages_{zh,en}.properties`（仅 push 文案） | ⚠️ 未独立配置 | ⚠️ | ⚠️ |
| `hunter-service` | ❌ | ❌ | ❌ | ❌ |
| `notify-java` | ❌ | ❌ | ❌ | ❌ |
| `sms-service` | ❌ | ❌ | ❌ | ❌ |
| `push-service` | ❌ | ❌ | ❌ | ❌ |
| `reward-task-center` | ❌ | ❌ | ❌ | ❌ |
| `short_link` | ❌ | ❌ | ❌ | ❌ |
| `operation-platform` | ❌ | ❌ | ❌ | ❌ |
| `jjewelry-aigc-agent` | ❌ | ❌ | ❌ | ❌ |
| `jjewelry-generator` | ❌ | ❌ | ❌ | ❌ |

**已串通的核心链路**（`user-center` 为代表）：

```
HTTP 请求 → HeaderInterceptor.preHandle()
       → 解析 Accept-Language → Locale
       → RequestHeaderContext.setLocale(locale)   ← ThreadLocal
                                              │
                          业务抛 ServiceException(code, codeExt, params)
                                              │
                          GlobalExceptionHandler 捕获
                                              │
                          messageSource.getMessage(
                              "errorCode." + code + "." + codeExt,
                              params,
                              RequestHeaderContext.getLocale())
                                              │
                          CommonJsonObject{ code, msg, data } → JSON
```

### 2.2 已识别问题清单（与文件:行号挂钩）

| # | 文件:行号 | 现状 | 问题 |
|---|---|---|---|
| 1 | `user-center/uc-api-common/src/main/java/cn/mathmagic/jjewelry/common/config/HeaderInterceptor.java:40-44` | `startsWith("zh"/"cn") → zh, else → en` 的二分法 | 无法扩展第 3 种语言 |
| 2 | `HeaderInterceptor.java:47` | 解析异常时 `locale = Locale.SIMPLIFIED_CHINESE` | 美国部署时**异常兜底反而是中文** |
| 3 | `HeaderInterceptor.java:17` `@Value("${locale.lang}")` | 仓库 `application*.yml` 里**没有该配置项** | 启动时若注入失败会抛错；缺省值缺失 |
| 4 | `RequestHeaderContext.java:27-43` | `getLanguage()` / `getRegion()` 用 `isZh()` 二元硬编码返回 `zh-Hans/CN` 或 `en/US` | 违反"多语言可扩展" |
| 5 | `LocaleConfig.java:24` | `setBasename("classpath:/i18n/messages")`，单一 basename | 无法多模块聚合资源；缺 `setFallbackToSystemLocale(false)` |
| 6 | `CommonJsonObject.java:73-83` | fallback 到 `errorCodeEnum.getDescription()`（英文硬编码） | 兜底语义不清晰；缺中间一级"默认 Locale" |
| 7 | `GlobalExceptionHandler.java:113` | `"param serialization failed"` 硬编码字符串 | 不支持 i18n |
| 8 | 多数服务无 i18n 基建 | hunter / notify / sms / push / reward-task-center / short_link / operation-platform / aigc-agent / generator 未接入 | 用户在这些服务能拿到的报错全是英文兜底或裸异常 |
| 9 | `order-java/trade-order-service/.../i18n/messages_en.properties` | 使用 `push.{event}.{slot}.{variant}` key | 命名空间未约定；其它服务可能各搞一套 |
| 10 | 业务代码散落硬编码中文 | （需扫描确认）`throw new ServiceException(400, "提示词不能为空")` 类写法 | 违反核心原则 #1 |

### 2.3 服务接入矩阵

参见 §2.1。落地优先级见 §10 里程碑。

---

## 3. 整体方案

### 3.1 设计原则

| 原则 | 含义 |
|---|---|
| **多语言可扩展** | 添加新语言只需新增一个 `messages_xx.properties` 文件 + 在 `SUPPORTED` 列表追加一行 |
| **零硬编码用户文案** | 代码中不允许出现 `"提示词不能为空"`、`"User not found"` 等用户可见字面量；一律走 `messageSource.getMessage(key, ...)` 或 `throw new ServiceException(code, codeExt)` |
| **三级回退** | 请求 Locale → 默认 Locale（`${locale.lang}`，美区 `en-US`、国内 `zh-CN`）→ 英文兜底字面量（`ErrorCodeEnum.description`） |
| **上下文透传** | 进程内 ThreadLocal；跨进程 Header；跨线程显式 wrap |
| **配置即语言** | 集群默认语言通过 Nacos / `application-{profile}.yml` 配置 `locale.lang`，不写死在代码 |

### 3.2 Locale 解析与传递

```
┌─────────────┐  Accept-Language  ┌──────────────────┐
│   Client    │ ────────────────► │ HeaderInterceptor│
└─────────────┘                   └────────┬─────────┘
                                           │ Locale.lookup
                                           ▼
                                  ┌──────────────────┐
                                  │RequestHeaderContext│ (ThreadLocal)
                                  └────────┬─────────┘
                                           │
                ┌──────────────────────────┼───────────────────────────┐
                ▼                          ▼                           ▼
       ┌─────────────────┐      ┌─────────────────┐       ┌─────────────────┐
       │ MessageSource   │      │ Feign Interceptor│      │LocaleContextProp.│
       │ (取本地化文案)  │      │ (透传到下游)     │       │ (跨线程 wrap)    │
       └─────────────────┘      └─────────────────┘       └─────────────────┘
```

### 3.3 文案 key 命名规范

详见 §7。简表：

| 类别 | 格式 | 示例 |
|---|---|---|
| 错误码 | `errorCode.{httpCode}[.{bizCode}]` | `errorCode.500.5000001` |
| 推送 | `push.{event}.{slot}.{variant}` | `push.order_start_production.title.post` |
| SMS | `sms.{scenario}.{slot}` | `sms.verifyCode.body` |
| 邮件 | `email.{scenario}.{slot}` | `email.welcome.subject` |
| 业务文案 | `biz.{module}.{scenario}` | `biz.cart.itemCount` |
| 枚举描述 | `enum.{enumName}.{value}` | `enum.contentStatus.draft` |

### 3.4 资源文件管理

**目录结构**（每个服务都按此组织）：

```
src/main/resources/
├── application.yml
└── i18n/
    ├── messages.properties           # 默认（推荐与 en 一致）
    ├── messages_zh.properties        # 中文原文
    ├── messages_en.properties        # 英文翻译
    ├── messages_es.properties        # （可选）西班牙语
    ├── biz/
    │   ├── messages.properties
    │   ├── messages_zh.properties
    │   └── messages_en.properties
    └── push/
        ├── messages.properties
        ├── messages_zh.properties
        └── messages_en.properties
```

**编码**：所有 `.properties` 文件**必须 UTF-8**。Spring 的 `ReloadableResourceBundleMessageSource` 配合 `setDefaultEncoding("UTF-8")` 已支持非 ASCII 字符直接写入，**不需要 native2ascii**。

### 3.5 三级回退策略

```java
// 调用方
String msg = messageSource.getMessage(
    key,                                    // 1️⃣ key
    args,                                   // 2️⃣ 占位符参数
    enumDefaultEnglishDescription,          // 3️⃣ 兜底字面量（建议英文）
    RequestHeaderContext.getLocale()        // 4️⃣ 当前 Locale
);
```

`MessageSource` 内部查找顺序：
1. `messages_${locale.language}_${locale.country}.properties`（如 `messages_en_US.properties`）
2. `messages_${locale.language}.properties`（如 `messages_en.properties`）
3. `messages_${defaultLocale.language}_${defaultLocale.country}.properties`（默认 Locale）
4. `messages.properties`
5. **若仍找不到**：抛 `NoSuchMessageException`；调用方捕获后用 `defaultMessage`（参数 3️⃣）兜底
6. **若 `defaultMessage` 也是 null**：调用方再 fallback 到 `enumDefaultDescription`（英文）

⚠️ **关键**：`messageSource.setFallbackToSystemLocale(false)`，否则在德文 Linux 容器上会拿到 `messages_de.properties`（不存在）→ 抛 `NoSuchMessageException`，行为不可预期。

---

## 4. 立即可执行的代码改造（Before / After）

> 以下改造**先在 `user-center/uc-api-common` 落地**，再让 `jjewelry-java/jjewelry-api-common` 同步，最后通过 §5 的 5 步法推到其它服务。

### 4.1 HeaderInterceptor：默认回退改为可配置 + 多语言协商

**Before** (`user-center/uc-api-common/src/main/java/cn/mathmagic/jjewelry/common/config/HeaderInterceptor.java`)：

```java
@Value("${locale.lang}")  // ❌ 没默认值，配置缺失会启动失败
private String defaultLang;

@Override
public boolean preHandle(HttpServletRequest request, ...) {
    Locale locale;
    try {
        String lange = request.getHeader("Accept-Language");
        if (lange == null) lange = defaultLang;
        lange = lange.trim().toLowerCase();
        if (lange.startsWith("zh") || lange.startsWith("cn")) {  // ❌ 二分法
            locale = Locale.SIMPLIFIED_CHINESE;
        } else {
            locale = Locale.US;
        }
    } catch (Exception e) {
        log.error("get Accept-Language error", e);
        locale = Locale.SIMPLIFIED_CHINESE;  // ❌ 美国部署的兜底反而是中文
    }
    RequestHeaderContext.setLocale(locale);
    return true;
}
```

**After**：

```java
@Slf4j
@Component
public class HeaderInterceptor implements HandlerInterceptor {

    /** 支持的语言列表。新增语言只改这里。 */
    private static final List<Locale> SUPPORTED = Arrays.asList(
        Locale.US,                  // en-US
        Locale.SIMPLIFIED_CHINESE   // zh-CN
        // new Locale("es", "ES"),  // 未来扩展
        // Locale.JAPAN,
    );

    @Value("${locale.lang:en-US}")  // ✅ 给默认值，集群侧通过 Nacos 覆盖
    private String defaultLangTag;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Locale locale;
        try {
            String header = request.getHeader("Accept-Language");
            if (header == null || header.isEmpty()) {
                header = defaultLangTag;
            }
            // RFC 4647 语言协商：解析 q 权重 → 在 SUPPORTED 中找最佳匹配
            List<Locale.LanguageRange> ranges = Locale.LanguageRange.parse(header);
            locale = Optional.ofNullable(Locale.lookup(ranges, SUPPORTED))
                             .orElse(Locale.forLanguageTag(defaultLangTag));
        } catch (Exception e) {
            log.warn("parse Accept-Language failed, fallback to {}", defaultLangTag, e);
            locale = Locale.forLanguageTag(defaultLangTag);
        }
        RequestHeaderContext.setLocale(locale);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        RequestHeaderContext.clear();
    }
}
```

### 4.2 RequestHeaderContext：去掉 isZh() 二分

**Before** (`RequestHeaderContext.java:27-43`)：

```java
public static String getLanguage() {
    if (isZh()) return "zh-Hans";
    return "en";
}
public static String getRegion() {
    if (isZh()) return "CN";
    return "US";
}
private static boolean isZh() {
    return getLocale().getLanguage().startsWith("zh");
}
```

**After**：

```java
/** BCP-47 语言标签：zh-CN / en-US / es-ES */
public static String getLanguage() {
    return getLocale().toLanguageTag();
}

/** ISO 3166 国家代码：CN / US / ES */
public static String getRegion() {
    return getLocale().getCountry();
}

// isZh() 删除。如果调用方确实需要"是不是中文"，写：
//     RequestHeaderContext.getLocale().getLanguage().equals("zh")
// 或更明确：
//     "zh".equals(RequestHeaderContext.getLocale().getLanguage())
```

⚠️ **影响面**：删除 `isZh()` 前先 `grep -rn "isZh()" --include="*.java"` 确认调用方，逐个改造。

### 4.3 LocaleConfig：多 basename + 关闭系统 Locale 回退

**Before** (`LocaleConfig.java`)：

```java
@Bean
public ReloadableResourceBundleMessageSource messageSource() {
    ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
    ms.setBasename("classpath:/i18n/messages");
    ms.setDefaultEncoding("UTF-8");
    return ms;
}
```

**After**：

```java
@Bean
public LocaleResolver localeResolver(@Value("${locale.lang:en-US}") String defaultLangTag) {
    AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
    resolver.setDefaultLocale(Locale.forLanguageTag(defaultLangTag));
    return resolver;
}

@Bean
public ReloadableResourceBundleMessageSource messageSource() {
    ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
    ms.setBasenames(
        "classpath:/i18n/messages",
        "classpath:/i18n/biz/messages",
        "classpath:/i18n/push/messages"
        // 新增子模块时追加
    );
    ms.setDefaultEncoding("UTF-8");
    ms.setFallbackToSystemLocale(false);  // ✅ 关键：避免容器 Locale 干扰
    ms.setUseCodeAsDefaultMessage(false); // 找不到时抛异常而非返回 key
    ms.setCacheSeconds(60);               // 60s 热更新（生产环境可调大）
    return ms;
}
```

### 4.4 CommonJsonObject：fallback 链路明确化

**Before** (`CommonJsonObject.java:73-83`)：

```java
public CommonJsonObject(ErrorCodeEnum errorCodeEnum) {
    this.code = errorCodeEnum.getCode();
    MessageSource messageSource = SpringBeanUtil.getBean(MessageSource.class);
    String message = null;
    try {
        message = messageSource.getMessage(errorCodePrefix + code, null, RequestHeaderContext.getLocale());
    } catch (Exception e) {
    }
    this.msg = message == null ? errorCodeEnum.getDescription() : message;
}
```

**After**：

```java
public CommonJsonObject(ErrorCodeEnum errorCodeEnum) {
    this.code = errorCodeEnum.getCode();
    this.msg = resolveMessage(errorCodePrefix + code, null, errorCodeEnum.getDescription());
}

/**
 * 三级回退：
 *  1) 当前请求 Locale 对应的 messages_xx.properties
 *  2) 默认 Locale（${locale.lang}）对应的 messages_xx.properties
 *  3) enum 内置英文 description（最终兜底）
 */
private static String resolveMessage(String key, Object[] args, String englishFallback) {
    MessageSource ms;
    try {
        ms = SpringBeanUtil.getBean(MessageSource.class);
    } catch (Exception e) {
        return englishFallback;
    }
    Locale locale = RequestHeaderContext.getLocale();
    try {
        return ms.getMessage(key, args, locale);
    } catch (NoSuchMessageException e1) {
        // 第二级：尝试默认 Locale
        try {
            Locale defaultLocale = SpringBeanUtil.getBean(LocaleResolver.class)
                .resolveLocale(null);  // AcceptHeaderLocaleResolver 会返回 defaultLocale
            if (!Objects.equals(locale, defaultLocale)) {
                return ms.getMessage(key, args, defaultLocale);
            }
        } catch (Exception ignore) {}
        // 第三级：enum 兜底
        return englishFallback;
    }
}
```

### 4.5 ErrorCodeEnum：description 抽离为 i18n key

**Before** (`ErrorCodeEnum.java`)：

```java
public enum ErrorCodeEnum {
    SUCCESS(200, "success"),
    NOT_EXIST(400, "not exist"),
    ERROR_TOKEN(401, "login expired"),
    ...
    private final int code;
    private final String description;
}
```

**After**（保持兼容、不破坏调用方）：

```java
public enum ErrorCodeEnum {
    SUCCESS(200, "success"),
    NOT_EXIST(400, "not exist"),
    ERROR_TOKEN(401, "login expired"),
    ...

    private final int code;
    /** 英文兜底文案。i18n key 由 CommonJsonObject 自动拼为 "errorCode." + code */
    private final String description;

    public int    getCode()        { return code; }
    public String getDescription() { return description; }  // 保持原方法语义：英文兜底
}
```

对应资源文件：

```properties
# messages_zh.properties
errorCode.200=成功
errorCode.400=资源不存在
errorCode.401=登录已过期

# messages_en.properties
errorCode.200=success
errorCode.400=not exist
errorCode.401=login expired
```

### 4.6 新增 LocaleContextPropagator：跨线程 / 异步透传

ThreadLocal 在切线程时会丢，所以**所有 `@Async`、`CompletableFuture.supplyAsync`、自建线程池**都必须显式 wrap。

新建 `user-center/uc-api-common/src/main/java/cn/mathmagic/jjewelry/common/config/LocaleContextPropagator.java`：

```java
package cn.mathmagic.jjewelry.common.config;

import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public final class LocaleContextPropagator {

    private LocaleContextPropagator() {}

    public static Runnable wrap(Runnable task) {
        Locale current = RequestHeaderContext.getLocale();
        return () -> {
            try {
                if (current != null) RequestHeaderContext.setLocale(current);
                task.run();
            } finally {
                RequestHeaderContext.clear();
            }
        };
    }

    public static <T> Callable<T> wrap(Callable<T> task) {
        Locale current = RequestHeaderContext.getLocale();
        return () -> {
            try {
                if (current != null) RequestHeaderContext.setLocale(current);
                return task.call();
            } finally {
                RequestHeaderContext.clear();
            }
        };
    }

    public static <T> Supplier<T> wrap(Supplier<T> task) {
        Locale current = RequestHeaderContext.getLocale();
        return () -> {
            try {
                if (current != null) RequestHeaderContext.setLocale(current);
                return task.get();
            } finally {
                RequestHeaderContext.clear();
            }
        };
    }
}
```

**调用示例**：

```java
// CompletableFuture
CompletableFuture.supplyAsync(LocaleContextPropagator.wrap(() -> userService.find(id)), executor);

// 自建线程池
executor.submit(LocaleContextPropagator.wrap(() -> doWork()));
```

`@Async` 推荐配合 `TaskDecorator`（Spring 提供）一次性接管：

```java
@Configuration
public class AsyncConfig implements AsyncConfigurer {
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setTaskDecorator(runnable -> LocaleContextPropagator.wrap(runnable));
        executor.initialize();
        return executor;
    }
}
```

### 4.7 硬编码中文 → i18n key 抽取规则与示例 ⭐ 核心章节

#### 4.7.1 抽取规则

1. **业务代码中禁止出现面向用户的中文字面量**
   - 扫描手段：`grep -rnP "[\u4e00-\u9fff]" --include="*.java" .`
   - 对每条命中分类处理（见 4.7.2）

2. **抽取目标**（必须改造）：
   - `throw new ServiceException(code, "中文message")` 类异常 message
   - `return CommonJsonObject.error(code, "中文")` 类 controller 直接返回
   - `enum XxxEnum { A(1, "中文描述") ... }` 的描述字段
   - 推送 / SMS / 邮件 / IM 模板字面量
   - `BizException` / `IllegalArgumentException` 等任何会回流到 API msg 的异常

3. **不需要抽取**（保留中文，便于排查）：
   - `log.info(...)` / `log.error(...)` 中给运维 / 开发看的日志
   - Java 注释（`//` 和 `/** */`）
   - 单元测试断言、测试夹具
   - Swagger `@ApiOperation`、`@ApiModelProperty` 描述（前端自有 i18n）

4. **资源文件填写规则**
   - `messages_zh.properties` 的 value = **代码里原本的中文字面量原文**（一字不改）
   - `messages_en.properties` 的 value = 翻译后的英文（请 PM / 翻译同事确认）
   - `messages.properties` 的 value = 与 `messages_en.properties` 一致（兜底）
   - 三个文件的 **key 集合必须严格一致**（CI 校验，详见 §7.6）

#### 4.7.2 抽取示例（Before / After）

**反例 1：直接抛中文异常**

```java
// ❌ Before
if (prompt == null) {
    throw new ServiceException(400, "提示词不能为空");
}
```

```java
// ✅ After
if (prompt == null) {
    // codeExt = "promptEmpty" → GlobalExceptionHandler 拼成 "errorCode.400.promptEmpty"
    throw new ServiceException(400, "promptEmpty");
}
```

```properties
# messages_zh.properties
errorCode.400.promptEmpty=提示词不能为空
# messages_en.properties
errorCode.400.promptEmpty=Prompt cannot be empty
```

**反例 2：enum 中文描述**

```java
// ❌ Before
public enum ContentStatus {
    DRAFT(0, "草稿"),
    PUBLISHED(1, "已发布"),
    DELETED(2, "已删除");

    private final int code;
    private final String description;
    public String getDescription() { return description; }
}
```

```java
// ✅ After
public enum ContentStatus {
    DRAFT    (0, "enum.contentStatus.draft",     "Draft"),
    PUBLISHED(1, "enum.contentStatus.published", "Published"),
    DELETED  (2, "enum.contentStatus.deleted",   "Deleted");

    private final int code;
    private final String i18nKey;
    private final String fallbackEn;

    ContentStatus(int code, String i18nKey, String fallbackEn) {
        this.code = code; this.i18nKey = i18nKey; this.fallbackEn = fallbackEn;
    }

    public int getCode() { return code; }

    /** 返回当前请求 Locale 对应的描述；不可用时返回英文兜底。 */
    public String getDescription() {
        try {
            return SpringBeanUtil.getBean(MessageSource.class)
                .getMessage(i18nKey, null, fallbackEn, RequestHeaderContext.getLocale());
        } catch (Exception e) {
            return fallbackEn;
        }
    }
}
```

```properties
# messages_zh.properties
enum.contentStatus.draft=草稿
enum.contentStatus.published=已发布
enum.contentStatus.deleted=已删除
# messages_en.properties
enum.contentStatus.draft=Draft
enum.contentStatus.published=Published
enum.contentStatus.deleted=Deleted
```

**反例 3：return 直接拼字面量**

```java
// ❌ Before
return CommonJsonObject.error(500, "用户不存在，请检查后重试");
```

```java
// ✅ After（推荐：抛异常，让 GlobalExceptionHandler 统一处理）
throw new ServiceException(500, "userNotFound");
```

或：

```java
// ✅ Alt（已经在 controller 里、不方便抛异常时，直接走 messageSource）
String msg = messageSource.getMessage(
    "errorCode.500.userNotFound",
    null,
    "User not found, please check and try again",   // 英文兜底
    RequestHeaderContext.getLocale()
);
return CommonJsonObject.error(500, msg);
```

```properties
# messages_zh.properties
errorCode.500.userNotFound=用户不存在，请检查后重试
# messages_en.properties
errorCode.500.userNotFound=User not found, please check and try again
```

**反例 4：推送模板字面量**

```java
// ❌ Before
String title = "您的订单已发货";
String content = "您的「" + productName + "」已开始配送，预计 " + days + " 天送达";
pushService.send(userId, title, content);
```

```java
// ✅ After
String title = messageSource.getMessage(
    "push.order_start_delivery.title.post",
    null,
    RequestHeaderContext.getLocale()
);
String content = messageSource.getMessage(
    "push.order_start_delivery.content.post",
    new Object[]{productName, days},   // 占位符 {0} {1}
    RequestHeaderContext.getLocale()
);
pushService.send(userId, title, content);
```

```properties
# messages_zh.properties
push.order_start_delivery.title.post=\uD83D\uDE9A 您的订单已发货
push.order_start_delivery.content.post=您的"{0}"已开始配送，预计 {1} 天送达
# messages_en.properties
push.order_start_delivery.title.post=\uD83D\uDE9A Your item has been shipped!
push.order_start_delivery.content.post=Your "{0}" is on its way, estimated {1} days
```

#### 4.7.3 抽取流程（Step-by-step）

1. **扫描**：每个服务模块下执行
   ```bash
   grep -rnP "[\u4e00-\u9fff]" --include="*.java" \
       --exclude-dir=target --exclude-dir=test src/main/java
   ```
2. **分类**：人工 review，标注 `[USER-VISIBLE]` / `[LOG]` / `[COMMENT]`
3. **设计 key**：按 §7 命名规范
4. **写中文**：把代码里原文一字不差地放进 `messages_zh.properties`
5. **找翻译**：把 key + 中文 + 业务上下文给到产品 / 翻译同事，拿到英文译文写进 `messages_en.properties`
6. **替换代码**：把代码里的中文字面量替换为 `messageSource.getMessage(...)` 或 `throw new ServiceException(code, codeExt)`
7. **CI 校验**：跑 §7.6 脚本，确保 zh/en key 集合一致
8. **冒烟测试**：参见 §9

---

## 5. 各服务接入步骤（统一 5 步法）

未接入 i18n 基建的服务（hunter / notify / sms / push 等），按下面 5 步走：

### Step 1：引入 `uc-api-common` 依赖（或本地等价复制）

```xml
<!-- 服务的 pom.xml -->
<dependency>
    <groupId>cn.mathmagic.jjewelry</groupId>
    <artifactId>uc-api-common</artifactId>
    <version>${uc-auth-sdk.version}</version>
</dependency>
```

⚠️ 若服务不依赖 user-center（如 `notify-java`、`sms-service` 是基础设施服务），二期方案是**抽出独立 `infra-i18n-starter` 模块**。一期可**本地复制** `LocaleConfig` / `HeaderInterceptor` / `RequestHeaderContext` / `LocaleContextPropagator` 4 个文件到该服务的 common 包下。

### Step 2：注册 HandlerInterceptor

```java
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final HeaderInterceptor headerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(headerInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/actuator/**", "/health");
    }
}
```

### Step 3：创建资源文件

```bash
mkdir -p src/main/resources/i18n
touch src/main/resources/i18n/messages.properties
touch src/main/resources/i18n/messages_zh.properties
touch src/main/resources/i18n/messages_en.properties
```

骨架内容：

```properties
# src/main/resources/i18n/messages_zh.properties
errorCode.200=成功
errorCode.400=请求参数错误
errorCode.401=登录已过期
errorCode.403=没有权限
errorCode.404=资源不存在
errorCode.500=系统繁忙

# src/main/resources/i18n/messages_en.properties
errorCode.200=success
errorCode.400=bad request
errorCode.401=login expired
errorCode.403=forbidden
errorCode.404=not found
errorCode.500=internal error
```

### Step 4：`application.yml` 增加 `locale.lang` 默认值

```yaml
# application.yml（基础默认值）
locale:
  lang: en-US
```

```yaml
# application-dev.yml / application-test_bj.yml / application-pre_bj.yml / application-produce_bj.yml（国内集群）
locale:
  lang: zh-CN
```

```yaml
# application-produce_us.yml / application-pre_us.yml（美国集群，新增）
locale:
  lang: en-US
```

⚠️ **生产建议**：把 `locale.lang` 放在 **Nacos 配置中心**，按集群命名空间区分（`prod-cn` 命名空间 = `zh-CN`，`prod-us` 命名空间 = `en-US`），避免误发。

### Step 5：改造业务代码

业务方法抛 `ServiceException` 时**统一用 codeExt**：

```java
// ❌ 不要这样
throw new ServiceException(400, "订单不存在");

// ✅ 这样
throw new ServiceException(400, "orderNotExist");
// 然后在 messages_zh / messages_en 中维护 errorCode.400.orderNotExist
```

带参数：

```java
throw new ServiceException(400, "orderAmountTooLow", new Object[]{minAmount});
```

```properties
# messages_zh.properties
errorCode.400.orderAmountTooLow=订单金额低于最低限额 {0}
# messages_en.properties
errorCode.400.orderAmountTooLow=Order amount is below minimum {0}
```

---

## 6. 跨服务调用链路 Locale 透传

### 6.1 Feign / OpenFeign

```java
@Configuration
public class FeignLocaleConfig {
    @Bean
    public RequestInterceptor localePropagationInterceptor() {
        return template -> {
            Locale l = RequestHeaderContext.getLocale();
            if (l != null) {
                template.header("Accept-Language", l.toLanguageTag());
            }
        };
    }
}
```

下游服务的 `HeaderInterceptor` 会自动从 `Accept-Language` 解析。

### 6.2 内部 RPC（若有自研 RPC / Dubbo）

在 RPC 框架的 `ClientFilter` 注入 `accept-language`，在 `ServerFilter` 解析后 `RequestHeaderContext.setLocale(...)`。

### 6.3 异步 / @Async / CompletableFuture

见 §4.6 `LocaleContextPropagator`。

### 6.4 MQ 消费者（RocketMQ / Kafka）

**生产端**：把 Locale 写进消息头

```java
Message<XxxDTO> msg = MessageBuilder.withPayload(dto)
    .setHeader("acceptLanguage", RequestHeaderContext.getLocale().toLanguageTag())
    .build();
```

**消费端**：从消息头还原

```java
@RocketMQMessageListener(...)
public class XxxListener implements RocketMQListener<MessageExt> {
    @Override
    public void onMessage(MessageExt message) {
        String langTag = message.getUserProperty("acceptLanguage");
        Locale locale = StringUtils.hasText(langTag)
            ? Locale.forLanguageTag(langTag)
            : Locale.forLanguageTag(System.getProperty("locale.lang", "en-US"));
        try {
            RequestHeaderContext.setLocale(locale);
            handle(message);
        } finally {
            RequestHeaderContext.clear();
        }
    }
}
```

### 6.5 定时任务（XxlJob / Spring Scheduled）

任务**没有"用户 Locale"概念**——给谁发文案，就用谁的 Locale：

- 给单个用户发推送 → 从用户表读 `user.preferred_language`，`RequestHeaderContext.setLocale(...)` 包一层
- 全员广播 → 按 user.preferred_language 分组，每组一份本地化文案

```java
@XxlJob("dailyRevenuePushJob")
public void execute() {
    List<User> users = userService.listActive();
    Map<String, List<User>> byLang = users.stream()
        .collect(Collectors.groupingBy(u ->
            Optional.ofNullable(u.getPreferredLanguage()).orElse("en-US")));
    byLang.forEach((langTag, group) -> {
        Locale locale = Locale.forLanguageTag(langTag);
        try {
            RequestHeaderContext.setLocale(locale);
            String title = messageSource.getMessage(
                "push.yesterday_revenue.title.post", null, locale);
            // ... 调用 push-service 发送
        } finally {
            RequestHeaderContext.clear();
        }
    });
}
```

---

## 7. 文案 key 命名规范

### 7.1 错误码（已存在）

```
errorCode.{httpCode}                       # 通用错误
errorCode.{httpCode}.{bizCode}             # 业务细分错误
errorCode.{httpCode}.{semanticName}        # 语义化错误（推荐新增时使用）
```

示例：

```
errorCode.200                              # success
errorCode.500.5000001                      # content not found（兼容旧格式）
errorCode.400.promptEmpty                  # 推荐新增格式
errorCode.500.userNotFound
```

### 7.2 推送 / SMS / 邮件

```
push.{event}.{slot}.{variant}
sms.{scenario}.{slot}
email.{scenario}.{slot}
```

- `event` / `scenario`：业务事件，下划线分隔，如 `order_start_production`、`verify_code`
- `slot`：`title` / `content` / `subject` / `body`
- `variant`（可选）：`post`、`plain`、`short`、`long`，用于多种文案变体

示例：

```
push.order_start_production.title.post     # ✅ 已存在
push.order_start_production.content.post
sms.verify_code.body
email.welcome.subject
email.welcome.body
```

### 7.3 业务文案 / AIGC prompt key

```
biz.{module}.{scenario}                    # 业务文案
prompt.{agent}.{scenario}.{lang}           # AIGC prompt（Nacos key 命名建议）
```

示例：

```
biz.cart.itemCount
biz.product.outOfStock
prompt.replyAgent.default.zh
prompt.replyAgent.default.en
```

⚠️ AIGC prompt 因为是给 LLM 看的、不是给用户看的，**不强制走 messages_*.properties**。建议在 Nacos 配置文件名上区分语言，由 `AgentPromptService` 按 `RequestHeaderContext.getLanguage()` 选择。

### 7.4 枚举描述

```
enum.{enumName}.{value}
```

示例：

```
enum.contentStatus.draft
enum.orderStatus.paid
enum.userRole.admin
```

### 7.5 占位符约定

- **MessageFormat**（默认）：`{0}` `{1}` `{2}` 按位置取参数
- **不要用** `${}`（Spring EL）和 `%s`（printf）
- **复数 / 性别** 推荐 ICU MessageFormat（Spring 6 起内建支持；当前 Spring Boot 2.7 需引入 `org.ocpsoft.rewrite:rewrite-icu` 或 `com.ibm.icu:icu4j`，复杂场景再考虑）

```properties
# 简单占位符
biz.cart.itemCount={0} 件商品                  # zh
biz.cart.itemCount={0} item(s) in cart         # en

# 多参数
errorCode.400.orderAmountTooLow=订单金额低于最低限额 {0}（当前 {1}）
errorCode.400.orderAmountTooLow=Order amount {1} is below minimum {0}
```

调用：

```java
messageSource.getMessage("biz.cart.itemCount", new Object[]{count}, locale);
```

### 7.6 多语言资源文件对齐规则 + CI 校验脚本

**规则**：所有 `messages_*.properties` 文件的 key 集合**必须严格一致**。CI 阶段强制门禁。

新建 `scripts/check_i18n_keys.sh`：

```bash
#!/usr/bin/env bash
# 检查所有 messages_zh.properties 与 messages_en.properties 的 key 集合一致
# 用法：bash scripts/check_i18n_keys.sh
set -e
fail=0
while IFS= read -r zh; do
    dir=$(dirname "$zh")
    en="$dir/messages_en.properties"
    if [ ! -f "$en" ]; then
        echo "❌ MISSING en file: $en"
        fail=1
        continue
    fi
    zh_keys=$(grep -oE '^[^#=[:space:]]+' "$zh" | sort -u)
    en_keys=$(grep -oE '^[^#=[:space:]]+' "$en" | sort -u)
    diff_out=$(diff <(echo "$zh_keys") <(echo "$en_keys") || true)
    if [ -n "$diff_out" ]; then
        echo "❌ KEY MISMATCH between:"
        echo "    $zh"
        echo "    $en"
        echo "$diff_out" | sed 's/^/      /'
        fail=1
    fi
done < <(find . -name 'messages_zh.properties' -not -path '*/target/*')

if [ $fail -eq 0 ]; then
    echo "✅ All messages_zh / messages_en key sets aligned."
fi
exit $fail
```

**接入 GitLab CI**（`.gitlab-ci.yml` 增加 stage）：

```yaml
i18n-check:
  stage: lint
  image: bash:latest
  script:
    - bash scripts/check_i18n_keys.sh
  only:
    - merge_requests
    - main
```

---

## 8. 扩展新语言的标准流程

以新增西班牙语（`es-ES`）为例：

1. **代码**：`HeaderInterceptor.SUPPORTED` 列表追加 `new Locale("es", "ES")`
2. **资源文件**：每个有 i18n 的服务模块下，复制 `messages_zh.properties` → `messages_es.properties`，把 value 翻译成西班牙语
3. **CI 脚本**：`scripts/check_i18n_keys.sh` 增加对 `messages_es.properties` 的 diff 校验（与 `messages_zh.properties` 比 key 集合）
4. **集群配置**（如果新建西班牙语集群）：Nacos 命名空间 `prod-es` 配 `locale.lang: es-ES`
5. **用户偏好**：`user` 表的 `preferred_language` 字段允许写 `es-ES`
6. **Push 服务**：FCM / APNS 的多语言能力对照检查
7. **冒烟**：`curl -H "Accept-Language: es-ES" ...` 全链路测试

---

## 9. 验证与回归

### 9.1 单元测试（MessageSource Mock）

```java
@SpringBootTest
class I18nTest {
    @Autowired MessageSource messageSource;

    @Test
    void shouldReturnChineseWhenLocaleZh() {
        assertEquals("成功",
            messageSource.getMessage("errorCode.200", null, Locale.SIMPLIFIED_CHINESE));
    }

    @Test
    void shouldReturnEnglishWhenLocaleEn() {
        assertEquals("success",
            messageSource.getMessage("errorCode.200", null, Locale.US));
    }

    @Test
    void shouldFallbackToEnglishWhenKeyMissingInZh() {
        // messages_zh 没定义、messages_en 有定义 → 应回退到 en
        // （需配置 setFallbackToSystemLocale(false) + LocaleResolver.defaultLocale = en-US）
        assertNotNull(messageSource.getMessage("errorCode.500.onlyInEn",
            null, Locale.SIMPLIFIED_CHINESE));
    }
}
```

### 9.2 接口级冒烟（curl 不同 Accept-Language）

```bash
# 中文
curl -H "Accept-Language: zh-CN" http://localhost:8080/api/order/notExist
# 期望：{"code":404,"msg":"订单不存在"}

# 英文
curl -H "Accept-Language: en-US" http://localhost:8080/api/order/notExist
# 期望：{"code":404,"msg":"Order not found"}

# 不传 header（用集群默认 locale.lang）
curl http://localhost:8080/api/order/notExist
# 美区集群期望：英文；国内集群期望：中文

# 不支持的语言（应回退到默认 Locale）
curl -H "Accept-Language: fr-FR" http://localhost:8080/api/order/notExist
# 期望：英文（美区）/ 中文（国内）
```

### 9.3 资源文件覆盖率脚本

```bash
bash scripts/check_i18n_keys.sh
```

预期输出：`✅ All messages_zh / messages_en key sets aligned.`

### 9.4 硬编码中文检测脚本（建议加 CI）

```bash
#!/usr/bin/env bash
# scripts/check_no_chinese_literal.sh —— 扫描 .java 中是否还有用户可见的中文字面量
# 白名单：日志、注释、单元测试、Swagger 注解
set -e
fail=0
while IFS= read -r line; do
    file=$(echo "$line" | cut -d: -f1)
    lineno=$(echo "$line" | cut -d: -f2)
    content=$(echo "$line" | cut -d: -f3-)
    # 跳过白名单
    if echo "$content" | grep -qE '(log\.(info|warn|error|debug|trace)|^\s*//|^\s*\*|@Api|@Schema)'; then
        continue
    fi
    # 跳过测试目录
    if echo "$file" | grep -q '/src/test/'; then
        continue
    fi
    echo "⚠️  $file:$lineno: $content"
    fail=1
done < <(grep -rnP '"[^"]*[\u4e00-\u9fff]+[^"]*"' --include="*.java" \
    --exclude-dir=target src/main/java 2>/dev/null || true)

if [ $fail -eq 0 ]; then
    echo "✅ No user-visible Chinese literals found."
fi
exit $fail
```

⚠️ 此脚本会有**误报**（如 SQL 中的中文常量、单元测试夹具）；建议**先以 warning 模式运行**（`exit 0`），积累白名单后再升级为 hard fail。

---

## 10. 里程碑与分批落地

| 阶段 | 时间 | 范围 | 交付 |
|---|---|---|---|
| **P0 基建修复** | 1 周 | `user-center/uc-api-common` + `jjewelry-java/jjewelry-api-common` | §4.1～4.6 全部代码改造合并；CI 校验脚本上线 |
| **P1 高频感知服务** | 2 周 | `order-java`、`push-service`、`notify-java`、`sms-service` 接入 + 文案抽取 | 4 个服务的 messages_{zh,en} 完整；冒烟通过 |
| **P2 长尾服务** | 2 周 | `hunter-service`、`reward-task-center`、`short_link`、`operation-platform`、`jjewelry-aigc-agent`、`jjewelry-generator` | 全部接入 §5 5 步法 |
| **P3 CI 门禁** | 1 周 | `scripts/check_i18n_keys.sh` 升级为硬门禁；`scripts/check_no_chinese_literal.sh` 上线为 warning | GitLab CI 强制 |
| **P4 美国集群上线** | 与 P1～P3 并行 | 新建 `application-*_us.yml`、Nacos `prod-us` 命名空间、`locale.lang: en-US` | 美区灰度发布 |

**总工期**：6 周（含并行）。

---

## 11. 附录

### A. 关键文件清单（涉及改造）

需修改：

- `user-center/uc-api-common/src/main/java/cn/mathmagic/jjewelry/common/config/HeaderInterceptor.java`
- `user-center/uc-api-common/src/main/java/cn/mathmagic/jjewelry/common/config/RequestHeaderContext.java`
- `user-center/uc-api-common/src/main/java/cn/mathmagic/jjewelry/common/config/LocaleConfig.java`
- `user-center/uc-api-common/src/main/java/cn/mathmagic/jjewelry/common/config/GlobalExceptionHandler.java`
- `user-center/uc-api-common/src/main/java/cn/mathmagic/jjewelry/common/dto/CommonJsonObject.java`
- `user-center/uc-api-common/src/main/java/cn/mathmagic/jjewelry/common/constant/ErrorCodeEnum.java`
- `jjewelry-java/jjewelry-api-common/`（同结构同步）

需新增：

- `user-center/uc-api-common/src/main/java/cn/mathmagic/jjewelry/common/config/LocaleContextPropagator.java`
- 各无 i18n 基建服务的 `src/main/resources/i18n/messages_{zh,en}.properties`
- 各服务 `application.yml` 增加 `locale.lang` 默认值
- `scripts/check_i18n_keys.sh`
- `scripts/check_no_chinese_literal.sh`
- `application-*_us.yml`（美区 profile）

参考（已存在）：

- `order-java/trade-order-service/src/main/resources/i18n/messages_en.properties`（push 文案 24 条）
- `user-center/uc-api-common/src/main/resources/i18n/messages_en.properties`（errorCode 224 条）
- `jjewelry-java/jjewelry-api-common/src/main/resources/i18n/messages_en.properties`

### B. 现有 messages_en.properties 节选

```properties
# user-center/uc-api-common/src/main/resources/i18n/messages_en.properties
errorCode.200=success
errorCode.400=not exist
errorCode.401=login expired
errorCode.500=interval error
errorCode.500.5000001=content not found
errorCode.1000.10000002=Sorry, only English prompts are supported right now. ...
errorCode.1000.10000022=Oops! Your account has been suspended for %d seconds. ...

# order-java/trade-order-service/src/main/resources/i18n/messages_en.properties
push.order_start_production.title.post=\uD83D\uDC40 Your item is now in production!
push.order_start_production.content.post=Your "{0}" has started production and will be in your hands soon~
push.yesterday_revenue.title.post=\uD83C\uDF8A {0} people bought your creation yesterday!
```

### C. 常见坑

1. **properties 文件编码**
   - Spring `ReloadableResourceBundleMessageSource.setDefaultEncoding("UTF-8")` 必设
   - **不需要** native2ascii；可直接写中文 / Emoji
   - Emoji 用 `\uD83D\uDE9A`（UTF-16 代理对）或直接粘贴 `🚚`，两者等价

2. **占位符转义**
   - properties 文件中的 `'`（单引号）会被 MessageFormat 当作转义字符吃掉
   - 写 `It''s ok`（双单引号）才能输出 `It's ok`
   - 中文场景一般无影响，但**英文文案**要警惕

3. **Lombok @AllArgsConstructor 与 enum 冲突**
   - 如果用 Lombok 自动生成 enum 构造器，添加新字段（如 `i18nKey`、`fallbackEn`）时要同步更新所有 enum 值
   - 推荐**手写构造器**，避免 IDE 自动同步遗漏

4. **`fallbackToSystemLocale`**
   - 默认 `true`，会用容器机器的 Locale 兜底
   - 美区 K8s 节点可能是 `en_US.UTF-8`、国内可能是 `zh_CN.UTF-8`
   - **必须设 false**，否则文案会因调度到的节点不同而抖动

5. **ThreadLocal 内存泄漏**
   - `HeaderInterceptor.afterCompletion()` 已 `clear()`
   - 自建线程池 / `@Async` 用 §4.6 `LocaleContextPropagator.wrap()` 后**必 finally clear()**
   - Tomcat 线程池有线程复用，泄漏会导致下个请求拿到上个请求的 Locale

6. **MessageSource 在静态上下文中无法注入**
   - `CommonJsonObject` 的构造器无 Spring 容器；只能 `SpringBeanUtil.getBean(MessageSource.class)`
   - 启动早期（如 Listener、Filter）调用可能拿不到 → 必须 try-catch 兜底

7. **ICU MessageFormat 与 Spring 版本**
   - Spring Boot 2.7 内建只支持 `MessageFormat`（Java 自带）
   - 复杂复数 / 性别需引入 `com.ibm.icu:icu4j` + 自定义 `MessageSource`
   - 当前阶段够用，二期再考虑

8. **Feign 透传 Accept-Language**
   - 服务调用方 `RequestHeaderContext.getLocale()` 必须有值，否则 Feign 拦截器拿不到
   - Job / 定时任务调 Feign 前**显式 setLocale()**

---

**文档结束**

如有疑问或建议，请联系架构组（@hanyu.wang）或在 GitLab MR 中评论。
