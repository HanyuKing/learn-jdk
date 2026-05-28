package work.redbook.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 小红书开放平台签名工具
 * 算法参考 SDK: com.xiaohongshu.fls.opensdk.util.Utils#addSign
 */
@Slf4j
public final class RedbookSignUtil {

    private RedbookSignUtil() {
    }

    /**
     * 生成当前秒级时间戳
     */
    public static String currentTimestamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    /**
     * sign = MD5(method + "?" + "appId=xxx&timestamp=xxx&version=xxx" + appSecret)
     */
    public static String sign(String method, String appId, String timestamp, String version, String appSecret) {
        List<String> params = new ArrayList<>();
        params.add("appId=" + appId);
        params.add("timestamp=" + timestamp);
        params.add("version=" + version);
        Collections.sort(params, String.CASE_INSENSITIVE_ORDER);

        String signSource = method + "?" + String.join("&", params) + appSecret;
        return md5(signSource);
    }

    private static String md5(String source) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(source.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            for (byte b : bytes) {
                builder.append(String.format("%02x", b & 0xff));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 算法不可用", e);
        }
    }

    public static void main(String[] args) {
        String appId = "ae807376fea64bbe9335";
        String version = "2.0";
        String appSecret = "7f86dcecb3237a5502ae51eff5a232bb";
        String accessToken = "token-f0011ace562b49409adb093c4a219c87-7720a1088a1c4676a753d85126be5770";
        String method = "product.getItemInfo";
        String timestamp = currentTimestamp();
        String sign = sign(method, appId, timestamp, version, appSecret);

        JSONObject body = new JSONObject();
        body.put("method", method);
        body.put("appId", appId);
        body.put("timestamp", timestamp);
        body.put("version", version);
        body.put("accessToken", accessToken);
        body.put("sign", sign);
        body.put("itemId", "69c5ebe1f1fe0f00153c6e64");
        body.put("pageNo", 1);
        body.put("pageSize", 10);

        log.info("post body: {}", JSON.toJSONString(body));
    }
}
