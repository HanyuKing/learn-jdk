package image.html2image;

import org.junit.Test;

public class HtmlToImageExample {

    @Test
    public void testTakeHighQualityScreenshot() {
        // 1. 创建转换器实例
        // 如果ChromeDriver不在系统PATH中，需要指定完整路径
        // HtmlToImageConverter converter = new HtmlToImageConverter("/path/to/chromedriver");
        HtmlToImageConverter converter = new HtmlToImageConverter();

        long start = System.currentTimeMillis();
        try {
            // 2. 准备HTML内容
            String htmlContent = "<h1>这是由Java生成的HTML内容截图</h1><p><font color=\"red\">hello世😄界</font></p>" +
                    "\uD83D\uDD0D 核心参数\n" +
                    "\n" +
                    "・材质：实心黄铜基底 + 软磁背贴\n" +
                    "\n" +
                    "・尺寸：6cm（±1mm公差）\n" +
                    "\n" +
                    "・工艺：UV浮雕打印 + 光油密封\n" +
                    "\n" +
                    "・工期：5-7天发货\n" +
                    "\n" +
                    "⚠\uFE0F 养护建议\n" +
                    "\n" +
                    "▎基础维护\n" +
                    "\n" +
                    "① 清洁：用干燥眼镜布单向擦拭（每周1次）\n" +
                    "\n" +
                    "② 除污：棉签蘸少量洗洁精轻点污渍（避开浮雕凸起）\n" +
                    "\n" +
                    "▎禁忌事项\n" +
                    "\n" +
                    "× 水洗/蒸汽清洁（黄铜基底易氧化）\n" +
                    "\n" +
                    "× 用硬物刮擦UV浮雕面（油墨层不可修复）\n" +
                    "\n" +
                    "× 接触灶台/消毒柜（耐温上限80℃）\n" +
                    "\n" +
                    "▎磁力维护\n" +
                    "\n" +
                    "・吸附力减弱时：\n" +
                    "\n" +
                    "→ 用酒精清洁磁片与吸附面（去除杂质）\n" +
                    "\n" +
                    "→ 放置在平整铁质表面12小时恢复磁力\n" +
                    "\n" +
                    "❗ 寿命提示\n" +
                    "\n" +
                    "正常使用下：\n" +
                    "\n" +
                    "・浮雕色彩保持：约2年（会随时间氧化）\n" +
                    "\n" +
                    "・磁力持久性：约2年（与使用频率相关）\n" +
                    "\n" +
                    "⚙\uFE0F重要条款\n" +
                    "\n" +
                    "・图片说明\n" +
                    "\n" +
                    "  ▸ 预售期为AI渲染图/3D效果图\n" +
                    "\n" +
                    "  ▸ 实物颜色/细节可能存在合理差异\n" +
                    "\n" +
                    "・退换政策\n" +
                    "\n" +
                    "    ▸ 订单有问题请添加客服详细沟通\n" +
                    "\n" +
                    "    ▸ 运费规则：非质量问题买家承担\n" +
                    "\n" +
                    "・售后凭证\n" +
                    "\n" +
                    "  ▸ 签收需拍摄完整开箱视频（含快递单+产品全景）\n" +
                    "\n" +
                    "・价格声明\n" +
                    "\n" +
                    " ▸单价为单个商品价格，展示图中多件仅为效果";

            // 3. 转换为图片字节流
            byte[] imageBytes = converter.takeHighQualityScreenshot(htmlContent);
            System.out.println("转换成功，图片大小: " + imageBytes.length + " 字节, " + "cost: " + (System.currentTimeMillis() - start) + " ms");

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

    public static void main(String[] args) {
        // 1. 创建转换器实例
        // 如果ChromeDriver不在系统PATH中，需要指定完整路径
        // HtmlToImageConverter converter = new HtmlToImageConverter("/path/to/chromedriver");
        HtmlToImageConverter converter = new HtmlToImageConverter();

        long start = System.currentTimeMillis();
        try {
            // 2. 准备HTML内容
            String htmlContent = "<h1>这是由Java生成的HTML内容截图</h1>";

            // 3. 转换为图片字节流
            byte[] imageBytes = converter.convertHtmlToImage(htmlContent);
            System.out.println("转换成功，图片大小: " + imageBytes.length + " 字节, " + "cost: " + (System.currentTimeMillis() - start) + " ms");

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