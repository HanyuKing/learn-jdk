package string;

/**
 * @author wanghanyu
 * @create 2017-12-21 12:41
 */
public class ReplaceTest {
    public static void main(String[] args) {
        String str1 = "//img10.360buyimg.com/n5/jfs/t3244/322/5408870901/503547/703bd9e8/5870e5a5N6cdcdefe.jpg";

        System.out.println(str1.replace("360buyimg.com/n5", "360buyimg.com/n2"));
    }
}
