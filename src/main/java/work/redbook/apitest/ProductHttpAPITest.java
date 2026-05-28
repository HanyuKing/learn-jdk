package work.redbook.apitest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import work.redbook.util.RedbookSignUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * 小红书商品 API 纯 HTTP 请求测试（不依赖 SDK Client）
 * 签名算法参考 SDK: com.xiaohongshu.fls.opensdk.util.Utils#addSign
 */
public class ProductHttpAPITest {
    private static final String API_URL = "https://ark.xiaohongshu.com/ark/open_api/v3/common_controller";

    private String appId = "ae807376fea64bbe9335";
    private String version = "2.0";
    private String appSecre = "7f86dcecb3237a5502ae51eff5a232bb";
    private String accessToken = "token-f0011ace562b49409adb093c4a219c87-7720a1088a1c4676a753d85126be5770";

    /**
     * 获取 ITEM 详情
     * method: product.getItemInfo
     */
    @Test
    public void testGetItemInfoByHttp() throws IOException {
        JSONObject bizParams = new JSONObject();
        bizParams.put("itemId", "69c5ebe1f1fe0f00153c6e64");
        bizParams.put("pageNo", 1);
        bizParams.put("pageSize", 10);

        String response = post("product.getItemInfo", bizParams);
        System.out.println(response);
    }

    /**
     * 查询 Item 列表
     * method: product.searchItemList
     */
    @Test
    public void testSearchItemListByHttp() throws IOException {
        JSONObject searchParam = new JSONObject();

        JSONObject bizParams = new JSONObject();
        bizParams.put("pageNo", 1);
        bizParams.put("pageSize", 10);
        bizParams.put("searchParam", searchParam);

        String response = post("product.searchItemList", bizParams);
        System.out.println(response);
    }

    private String post(String method, JSONObject bizParams) throws IOException {
        String timestamp = RedbookSignUtil.currentTimestamp();
        String sign = RedbookSignUtil.sign(method, appId, timestamp, version, appSecre);

        JSONObject body = new JSONObject();
        body.put("method", method);
        body.put("appId", appId);
        body.put("timestamp", timestamp);
        body.put("version", version);
        body.put("accessToken", accessToken);
        body.put("sign", sign);
        if (bizParams != null) {
            body.putAll(bizParams);
        }

        byte[] requestBytes = JSON.toJSONString(body).getBytes(StandardCharsets.UTF_8);

        HttpURLConnection conn = (HttpURLConnection) new URL(API_URL).openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(30000);
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        try (OutputStream outputStream = conn.getOutputStream()) {
            outputStream.write(requestBytes);
        }

        int responseCode = conn.getResponseCode();
        InputStream inputStream = responseCode >= 400 ? conn.getErrorStream() : conn.getInputStream();
        if (inputStream == null) {
            throw new IOException("HTTP 请求失败，状态码: " + responseCode);
        }

        byte[] buffer = new byte[4096];
        StringBuilder responseBuilder = new StringBuilder();
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            responseBuilder.append(new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
        }
        inputStream.close();
        conn.disconnect();
        return responseBuilder.toString();
    }
}
