package image.html2image;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * HTML转图片工具类
 * 注意：运行此代码的机器必须已安装Chrome/Chromium浏览器和对应版本的ChromeDriver
 */
public class HtmlToImageConverter {

    // WebDriver实例，建议作为单例，避免重复创建销毁消耗资源
    private WebDriver driver;

    /**
     * 初始化转换器
     * @param chromeDriverPath ChromeDriver可执行文件的完整路径（可选，如果已在PATH中则无需设置）
     */
    public HtmlToImageConverter(String chromeDriverPath) {
        if (chromeDriverPath != null && !chromeDriverPath.trim().isEmpty()) {
            System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        }
        this.initializeDriver();
    }

    public HtmlToImageConverter() {
        this(null); // 使用系统PATH中的ChromeDriver
    }

    private void initializeDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // 无头模式
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1200,800"); // 设置默认窗口大小
        // 可选：禁用图片加载以加快速度（如果不需要截图中的图片）
        // options.addArguments("--blink-settings=imagesEnabled=false");

        this.driver = new ChromeDriver(options);
    }

    /**
     * 核心方法：将HTML字符串转换为图片字节流
     * @param htmlContent 完整的HTML字符串
     * @return 图片的字节数组 (PNG格式)
     * @throws Exception 如果转换过程发生错误
     */
    public byte[] convertHtmlToImage(String htmlContent) throws Exception {
        return convertHtmlToImage(htmlContent, 1200, 800);
    }

    /**
     * 核心方法：将HTML字符串转换为图片字节流（可指定大小）
     * @param htmlContent 完整的HTML字符串
     * @param width 浏览器窗口宽度
     * @param height 浏览器窗口高度
     * @return 图片的字节数组 (PNG格式)
     * @throws Exception 如果转换过程发生错误
     */
    public byte[] convertHtmlToImage(String htmlContent, int width, int height) throws Exception {
        try {
            // 1. 设置浏览器窗口大小
            driver.manage().window().setSize(new org.openqa.selenium.Dimension(width, height));

            // 2. 将HTML内容转换为Data URL并导航
            // 对HTML内容进行URL编码，防止特殊字符导致问题
            String encodedHtml = URLEncoder.encode(htmlContent, StandardCharsets.UTF_8.toString());
            String dataUrl = "data:text/html;charset=utf-8," + encodedHtml;
            driver.get(dataUrl);

            // 3. 等待页面渲染完成（简单版本）
            // 生产环境应使用更智能的等待策略，如等待特定元素出现
            Thread.sleep(1500); // 等待1.5秒

            // 4. 执行截图并获取字节数组
            TakesScreenshot screenshotTaker = (TakesScreenshot) driver;
            return screenshotTaker.getScreenshotAs(OutputType.BYTES);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new Exception("截图过程被中断", e);
        } catch (Exception e) {
            throw new Exception("HTML转图片失败", e);
        }
    }

    /**
     * 关闭WebDriver，释放资源
     */
    public void close() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }
}