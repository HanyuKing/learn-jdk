package httpclient;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

/**
 * @Author Hanyu.Wang
 * @Date 2022/11/5 01:17
 * @Description
 * @Version 1.0
 **/
@Slf4j
public class Main {
    public static void main(String[] args) throws Exception {
        String path = "http://127.0.0.1:8080/myserver?123=456";
        String resp = HttpUtils.get(path,  null, 2000);

//        new Thread(() -> {
//            try {
//                PoolingHttpClientConnectionManager poolManager = HttpUtils.getPoolCollectionManager();
//
//                while (true) {
//
//                    for (HttpRoute httpRoute : poolManager.getRoutes()) {
//                        System.out.println(poolManager.getStats(httpRoute));
//                    }
//
//                    System.out.println(poolManager.getTotalStats());
//
//                    Thread.sleep(1000);
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();

        System.out.println(resp);

        Semaphore semaphore = new Semaphore(0);
        semaphore.acquire();
    }

    @Test
    public void testOverlay() {
        long start = System.currentTimeMillis();
        String bodyJson = "{\"images\":[{\"m\":false,\"p\":\"https://static.zaohaowu.com/jjewelry/web/images/20251023/d899f069-236b-438a-87b5-b99b2f11b7d4.png\",\"r\":-359.1497373872529,\"s\":0.78234,\"type\":\"customMade\",\"x\":222.5,\"y\":52.5,\"zIndex\":2}],\"total\":{\"dpi\":1000,\"height\":99.0,\"production_height\":0,\"production_width\":0,\"ratio\":3.97,\"width\":393.0}}";

        ImageEditOverlayRequest request = ImageEditOverlayRequest.builder().jsonConfigStr(bodyJson).build();

        try (HttpResponse response = HttpRequest.post( "http://localhost:8000/image/edit/overlay")
                .header("Content-Type", "application/json")
                .body(JSON.toJSONString(request))
                .setConnectionTimeout(5000)  // 连接超时25秒
                .setReadTimeout(60000)       // 读取超时60秒
                .execute()) {

            log.info("overlayWhiteInk cost: {}", System.currentTimeMillis() - start);

            if (response.isOk()) {
                String responseText = response.body();
                log.error("overlayWhiteInk responseText: {}", response);
            } else {
                log.error("overlayWhiteInk resp: {}", response);
            }
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageEditOverlayRequest {

        @JSONField(name = "json_config_str")
        private String jsonConfigStr;
    }

}
