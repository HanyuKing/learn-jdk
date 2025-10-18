package wx;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WeChatGetMaterialNative {

    private static final String API_URL =
            "https://api.weixin.qq.com/cgi-bin/material/get_material?access_token=";

    public static void main(String[] args) {
        String accessToken = "YOUR_ACCESS_TOKEN"; // 替换成你的 access_token
        String mediaId = "YOUR_MEDIA_ID";         // 替换成你的 media_id

        try {
            String result = getMaterial(accessToken, mediaId);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取永久素材（支持图文、图片、视频、音频）
     */
    public static String getMaterial(String accessToken, String mediaId) throws IOException {
        String urlStr = API_URL + accessToken;
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // 设置请求属性
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

        // 写入请求体
        String jsonBody = "{\"media_id\":\"" + mediaId + "\"}";
        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
            os.flush();
        }

        // 读取响应
        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("HTTP Error: " + responseCode);
        }

        String contentType = conn.getContentType();
        InputStream inputStream = conn.getInputStream();

        if (contentType != null && contentType.contains("application/json")) {
            // 图文素材 / 错误信息
            String jsonResponse = readStreamAsString(inputStream);
            conn.disconnect();
            return jsonResponse;
        } else {
            // 图片 / 音频 / 视频素材
            byte[] bytes = readStreamAsBytes(inputStream);
            Files.write(Paths.get("material_download.bin"), bytes);
            conn.disconnect();
            return "已保存二进制素材到 material_download.bin";
        }
    }

    /** 将输入流读取为字符串 */
    private static String readStreamAsString(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        }
    }

    /** 将输入流读取为字节数组 */
    private static byte[] readStreamAsBytes(InputStream inputStream) throws IOException {
        try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            byte[] data = new byte[4096];
            int n;
            while ((n = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, n);
            }
            return buffer.toByteArray();
        }
    }
}
