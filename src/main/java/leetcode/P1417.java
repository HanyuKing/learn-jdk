package leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2022/8/11 14:10
 * @Description
 * @Version 1.0
 **/
public class P1417 {
    public static void main(String[] args) {
        System.out.println(new SolutionP1417().reformat("covid2019"));
    }
}

//给你一个混合了数字和字母的字符串 s，其中的字母均为小写英文字母。
//
// 请你将该字符串重新格式化，使得任意两个相邻字符的类型都不同。也就是说，字母后面应该跟着数字，而数字后面应该跟着字母。
//
// 请你返回 重新格式化后 的字符串；如果无法按要求重新格式化，则返回一个 空字符串 。
//
//
//
// 示例 1：
//
// 输入：s = "a0b1c2"
//输出："0a1b2c"
//解释："0a1b2c" 中任意两个相邻字符的类型都不同。 "a0b1c2", "0a1b2c", "0c2a1b" 也是满足题目要求的答案。
//
//
// 示例 2：
//
// 输入：s = "leetcode"
//输出：""
//解释："leetcode" 中只有字母，所以无法满足重新格式化的条件。
//
//
// 示例 3：
//
// 输入：s = "1229857369"
//输出：""
//解释："1229857369" 中只有数字，所以无法满足重新格式化的条件。
//
//
// 示例 4：
//
// 输入：s = "covid2019"
//输出："c2o0v1i9d"
//
//
// 示例 5：
//
// 输入：s = "ab123"
//输出："1a2b3"
//
//
//
//
// 提示：
//
//
// 1 <= s.length <= 500
// s 仅由小写英文字母和/或数字组成。
//
//
// Related Topics 字符串 👍 57 👎 0

class SolutionP1417 {
    public String reformat(String s) {
        char[] numberArray = new char[s.length()];
        char[] charArray = new char[s.length()];
        int numIndex = 0;
        int charIndex = 0;
        for (char c : s.toCharArray()) {
            if (isNumber(c)) {
                numberArray[numIndex++] = c;
            } else {
                charArray[charIndex++] = c;
            }
        }
        if (Math.abs(charIndex - numIndex) > 1)  {
            return "";
        }

        boolean numberFirst = numIndex - charIndex > 0;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length() / 2 + 1; i++) {
            if (numberFirst) {
                if (i < numIndex) {
                    sb.append(numberArray[i]);
                }
                if (i < charIndex) {
                    sb.append(charArray[i]);
                }
            } else {
                if (i < charIndex) {
                    sb.append(charArray[i]);
                }
                if (i < numIndex) {
                    sb.append(numberArray[i]);
                }
            }

        }
        return sb.toString();
    }

    private boolean isNumber(char c) {
        return c >= '0' && c <= '9';
    }
}

