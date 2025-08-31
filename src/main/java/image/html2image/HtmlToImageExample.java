package image.html2image;

public class HtmlToImageExample {

    public static void main(String[] args) {
        // 1. 创建转换器实例
        // 如果ChromeDriver不在系统PATH中，需要指定完整路径
        // HtmlToImageConverter converter = new HtmlToImageConverter("/path/to/chromedriver");
        HtmlToImageConverter converter = new HtmlToImageConverter();

        try {
            // 2. 准备HTML内容
            String htmlContent = "<h1>这是由Java生成的HTML内容截图</h1>";

            // 3. 转换为图片字节流
            byte[] imageBytes = converter.convertHtmlToImage(htmlContent, 800, 600);
            System.out.println("转换成功，图片大小: " + imageBytes.length + " 字节");

            // 4. 将字节流保存为文件（可选，用于测试）
            java.nio.file.Files.write(
                java.nio.file.Paths.get("output-screenshot.png"),
                imageBytes
            );
            System.out.println("图片已保存到: output-screenshot.png");

        } catch (Exception e) {
            System.err.println("转换失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 5. 释放资源
            converter.close();
        }
    }
}