package image;

import org.junit.Test;

import java.io.IOException;

/**
 * @author hanyu.wang
 * @version 1.0
 * @date 2025/6/3
 */
public class ImageUtilTest {
    @Test
    public void testCutWebpImage2() throws Exception {
        ImageUtil.cutImageFromUrl(
                "https://static.zaohaowu.com/jjewelry/web/images/img2webp/webp/e89931274bbb4fcc86291609213e6351.webp",
                "/Users/rogerswang/my/source_code/learn-jdk/src/main/java/image/zaohaowu_cut_webp.png"
        );
        System.out.println("裁剪成功！");
    }

    @Test
    public void testCutWebpImage() throws Exception {
        ImageUtil.cutImageFromUrl(
                "https://static.zaohaowu.com/jjewelry/web/images/img2webp/webp/e89931274bbb4fcc86291609213e6351.webp",
                "/Users/rogerswang/my/source_code/learn-jdk/src/main/java/image/zaohaowu_cut_webp.png",
                85,
                660,
                300,
                610
        );
        System.out.println("裁剪成功！");
    }
    @Test
    public void testCutPNGImage() throws IOException {
        ImageUtil.cut_PNG("/Users/rogerswang/my/source_code/learn-jdk/src/main/java/image/zaohaowu_png.png",
                "/Users/rogerswang/my/source_code/learn-jdk/src/main/java/image/zaohaowu_png_cut.png",
                80,
                620,
                300,
                680);

    }

}
