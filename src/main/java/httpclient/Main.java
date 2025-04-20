package httpclient;

import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

/**
 * @Author Hanyu.Wang
 * @Date 2022/11/5 01:17
 * @Description
 * @Version 1.0
 **/
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
}
