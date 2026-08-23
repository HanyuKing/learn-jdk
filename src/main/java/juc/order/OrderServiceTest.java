package juc.order;

import okhttp3.*;
import org.junit.Test;
import java.io.IOException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hanyu.wang
 * @version 1.0
 * @date 2026/2/7
 */
public class OrderServiceTest {

    private static final int THREAD_COUNT = 20;
    private static final String TARGET_URL = "https://ecommerce.zaohaowu.com/aigc/trade/order/create";
    private static final String PRE_ORDER_URL = "https://ecommerce.zaohaowu.com/aigc/trade/product/detail?productId=W202602040084&skuId=W2026020400840002&quantity=1";
    
    // TODO: 请在此处填写您的 Cookie，注意必须是有效的登录态 Cookie
    private static final String COOKIE = "JSESSIONID=7FD1F099230BE0A9599FAF2E72301B03; MM_AIGC=GwPVgiPda1mWiwKS*dEPlk_fXnYFHOW0uSEvwuBjMRlg58x_O5NNqzUf8pJLDyKzcEWHvMb9cmQWvHz8MT2EHG-AUnObW0C_FHYJJnzKqAS4YSx4UbRjrF9s30XDQ7tfxUhw3VaVjk5NQM3Tn05GDZ4ukgIX7m-FvmVlCy2WvEL_rWFMU7L-zVJ1_LVVT8fMeJJ-PHLGUNkB8spkWdl3lwA-iSpW7ScRdEpuIYx7XJBMnC6LYPp3CP8_S3FZVqtxsLYfOSolAjLpaylRBporye_Agj79Ry9R_SGgTxTgYrbvIKHzzlVPwB9dTCK6POrSLl9sUX4j2ZrMv1htfO3tfgpiNmySn17j5vOfiviIcdOg-pyrOD2sP8YxZ-PqUf7DorXAaNQ==*R0NitRpM5-DVXYUkWUtbIjIJRw7N_oti3lcN765G1i0=; JSESSIONID=D89BAA1E33C7C0703C9BD9D0F4D17381";

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();
    
    // 简单的正则提取 preOrderId，避免引入额外 JSON 库依赖
    private static final Pattern PRE_ORDER_ID_PATTERN = Pattern.compile("\"preOrderId\"\\s*:\\s*\"([^\"]+)\"");

    @Test
    public void testConcurrentOrder() throws InterruptedException {
        // 创建固定大小的线程池，模拟 100 个并发用户
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        // CyclicBarrier 用于让所有线程在同一点等待，然后同时执行，模拟瞬时高并发
        CyclicBarrier cyclicBarrier = new CyclicBarrier(THREAD_COUNT);

        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.execute(() -> {
                try {
                    // 1. 发送 PreRequest 获取 preOrderId (作为 Token)
                    // 这一步在 Barrier 之前，不占用并发测试的启动时间
                    String preOrderId = fetchPreOrderId();
                    if (preOrderId == null) {
                        System.err.println("Thread-" + Thread.currentThread().getName() + " Failed to get preOrderId");
                        // 如果获取失败，为了不阻塞 Barrier，最好还是参与 Barrier 或者计数，
                        // 但这里简单处理，继续往下，Token 为空可能会报错
                        preOrderId = "";
                    }

                    // 2. 准备下单请求数据
                    String jsonBody = String.format("{" +
                            "\"addressId\":\"e1cab5b2294c4871b8e8b0fefa601198\"," +
                            "\"quantity\":1," +
                            "\"discountPrice\":0.01," +
                            "\"productId\":\"W202604130163\"," + // 替换为大奖 D202512240010
                            "\"token\":\"%s\"," +
                            "\"remark\":\"\"," +
                            "\"skuId\":\"W2026041301630002\"}", preOrderId); // 替换为大奖 D2025122400100002

                    Request request = new Request.Builder()
                            .url(TARGET_URL)
                            .post(RequestBody.create(jsonBody, MediaType.parse("application/json; charset=utf-8")))
                            .addHeader("User-Agent", "Java-Test-Client")
                            .addHeader("Cookie", COOKIE)
                            .build();

                    // 预创建 Call 对象
                    Call call = client.newCall(request);

                    // 3. 在所有配置完成后，等待所有线程到达此处
                    // 确保 execute() 动作瞬间爆发
                    cyclicBarrier.await();

                    // 4. 执行请求
                    try (Response response = call.execute()) {
                        if (response.code() == 200) {
                            System.out.println("Thread-" + Thread.currentThread().getName() +
                                    " | Token(PreOrderId): " + preOrderId +
                                    " | Response: " + response.body().string());
                        } else {
                            System.out.println("Thread-" + Thread.currentThread().getName() +
                                    " | Token(PreOrderId): " + preOrderId +
                                    " | Response: " + response.code());
                        }
                    }

                } catch (Exception e) {
                    System.err.println("Thread-" + Thread.currentThread().getName() + " Error: " + e.getMessage());
                }
            });
        }

        executorService.shutdown();
        // 等待测试结束，超时时间设置为 5 分钟
        boolean finished = executorService.awaitTermination(5, TimeUnit.MINUTES);
        if (!finished) {
            System.err.println("Test execution time exceeded limit.");
        }
    }

    private String fetchPreOrderId() {
        Request request = new Request.Builder()
                .url(PRE_ORDER_URL)
                .post(RequestBody.create("", MediaType.parse("application/json; charset=utf-8"))) // 空 Body POST
                .addHeader("User-Agent", "Java-Test-Client")
                .addHeader("Cookie", COOKIE)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String body = response.body().string();
                Matcher matcher = PRE_ORDER_ID_PATTERN.matcher(body);
                if (matcher.find()) {
                    return matcher.group(1);
                }
            } else {
                 System.err.println("Fetch PreOrder Failed: " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
