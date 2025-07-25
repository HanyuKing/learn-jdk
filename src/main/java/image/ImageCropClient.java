package image;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Map;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ImageCropClient {

    // 将图片转为 Base64
    public static String imageToBase64(String imagePath) throws IOException {
        byte[] imageBytes = Files.readAllBytes(new File(imagePath).toPath());
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    public static void main(String[] args) {
        String imagePath = "/Users/rogerswang/my/source_code/learn-jdk/src/main/java/image/zaohaowu_png.png";
        String urlStr = "http://172.31.0.43:42123/crop-image/";
        Gson gson = new Gson();

        try {
            // 构建请求参数
            Map<String, Object> cropParams = new HashMap<>();
            cropParams.put("image_base64", imageToBase64(imagePath));
            cropParams.put("left", 0.05);
            cropParams.put("top", 0.5);
            cropParams.put("right", 0.4);
            cropParams.put("bottom", 0.9);

            String jsonPayload = gson.toJson(cropParams);

            // 发送 POST 请求
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonPayload.getBytes("UTF-8"));
            }

            // 读取响应
            int status = conn.getResponseCode();
            InputStream is = (status >= 200 && status < 300) ? conn.getInputStream() : conn.getErrorStream();

            StringBuilder responseBuilder = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    responseBuilder.append(line);
                }
            }

            String responseJson = responseBuilder.toString();

            if (status == 200) {
                // 解析成功响应
                Map<String, Object> responseMap = gson.fromJson(responseJson, new TypeToken<Map<String, Object>>(){}.getType());
                String croppedImageBase64 = (String) responseMap.get("cropped_image_base64");

                byte[] croppedImageBytes = Base64.getDecoder().decode(croppedImageBase64);
                try (FileOutputStream fos = new FileOutputStream("cropped.png")) {
                    fos.write(croppedImageBytes);
                }

                System.out.println("Cropped image saved. Dimensions: " + responseMap.get("dimensions"));
            } else {
                System.err.println("Error response: " + responseJson);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
