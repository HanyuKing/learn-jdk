package leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2023/11/23 15:59
 * @Description
 * @Version 1.0
 **/
public class P1410 {
    public static void main(String[] args) {
        String text = "&amp; is an HTML entity but &ambassador; is not.";
        String result = new P1410().entityParser(text);
        System.out.println(result);
    }

    /**
     * 双引号：字符实体为 &quot; ，对应的字符是 " 。
     * 单引号：字符实体为 &apos; ，对应的字符是 ' 。
     * 与符号：字符实体为 &amp; ，对应对的字符是 & 。
     * 大于号：字符实体为 &gt; ，对应的字符是 > 。
     * 小于号：字符实体为 &lt; ，对应的字符是 < 。
     * 斜线号：字符实体为 &frasl; ，对应的字符是 /
     *
     * 示例 1：
     *
     * 输入：text = "&amp; is an HTML entity but &ambassador; is not."
     * 输出："& is an HTML entity but &ambassador; is not."
     * 解释：解析器把字符实体 &amp; 用 & 替换
     * 示例 2：
     *
     * 输入：text = "and I quote: &quot;...&quot;"
     * 输出："and I quote: \"...\""
     * 示例 3：
     *
     * 输入：text = "Stay home! Practice on Leetcode :)"
     * 输出："Stay home! Practice on Leetcode :)"
     * 示例 4：
     *
     * 输入：text = "x &gt; y &amp;&amp; x &lt; y is always false"
     * 输出："x > y && x < y is always false"
     * 示例 5：
     *
     * 输入：text = "leetcode.com&frasl;problemset&frasl;all"
     * 输出："leetcode.com/problemset/all"
     *
     * @param text
     * @return
     */
    public String entityParser(String text) {
        return text.replaceAll("&quot;", "\"")
                .replaceAll("&apos;", "'")
                .replaceAll("&gt;", ">")
                .replaceAll("&lt;", "<")
                .replaceAll("&frasl;", "/")
                .replaceAll("&amp;", "&");
    }

    public String entityParser2(String text) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                if (i + 3 < text.length()) {
                    if (text.charAt(i + 1) == 'g' && text.charAt(i + 2) == 't' && text.charAt(i + 3) == ';') {
                        result.append(">");
                        i = i + 3;
                        continue;
                    }
                    if (text.charAt(i + 1) == 'l' && text.charAt(i + 2) == 't' && text.charAt(i + 3) == ';') {
                        result.append("<");
                        i = i + 3;
                        continue;
                    }
                }
                if (i + 4 < text.length()) {
                    if (text.charAt(i + 1) == 'a' && text.charAt(i + 2) == 'm' && text.charAt(i + 3) == 'p' && text.charAt(i + 4) == ';') {
                        result.append("&");
                        i = i + 4;
                        continue;
                    }
                }
                if (i + 5 < text.length()) {
                    if (text.charAt(i + 1) == 'q' && text.charAt(i + 2) == 'u' && text.charAt(i + 3) == 'o' && text.charAt(i + 4) == 't' && text.charAt(i + 5) == ';') {
                        result.append("\"");
                        i = i + 5;
                        continue;
                    }
                    if (text.charAt(i + 1) == 'a' && text.charAt(i + 2) == 'p' && text.charAt(i + 3) == 'o' && text.charAt(i + 4) == 's' && text.charAt(i + 5) == ';') {
                        result.append("'");
                        i = i + 5;
                        continue;
                    }
                }
                if (i + 6 < text.length()) {
                    if (text.charAt(i + 1) == 'f' && text.charAt(i + 2) == 'r' && text.charAt(i + 3) == 'a' && text.charAt(i + 4) == 's' && text.charAt(i + 5) == 'l' && text.charAt(i + 6) == ';') {
                        result.append("/");
                        i = i + 6;
                        continue;
                    }
                }
            }
            result.append(text.charAt(i));
        }
        return result.toString();
    }
}
