package interview.guazi;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MultiProblem {
    /**
     * 给定两个以字符串形式表示的非负整数 num1 和 num2，返回 num1 和 num2 的乘积，它们的乘积也表示为字符串形式。
     * 注意：不能使用任何内置的 BigInteger 库或直接将输入转换为整数。
     *
     * 11111
     *    23
     *------------
     *  33333
     * 222220
     *------------
     * 255553
     *
     */

    @Test
    public void testMulti1() {
        System.out.println(multi("11111", "23"));
    }

    @Test
    public void testMulti2() {
        System.out.println(multi("0", "23"));
    }

    @Test
    public void testMulti3() {
        System.out.println(multi("10", "23"));
    }

    public String multi(String num1, String num2) {
        List<String> nums = new ArrayList<>();
        String suffix = "";
        for (int i = num2.length() - 1; i >= 0; i--) {
            String smRes = singleMulti(num1, (num2.charAt(i) - '0'));
            nums.add(smRes + suffix);
            suffix = suffix + "0";
        }
        String result = "0";
        for (String num : nums) {
            result = add(result, num);
        }

        return result;
    }

    private String add(String num1, String num2) {
        StringBuilder result = new StringBuilder();
        int carry = 0;

        int i = 0;
        int n1Length = num1.length();
        int n2Length = num2.length();
        while (i < n1Length || i < n2Length) {
            int currNum = 0;
            if (i < n1Length && i < n2Length) {
                currNum = (num1.charAt(n1Length - 1 - i) - '0')
                        + (num2.charAt(n2Length - 1 - i) - '0')
                        + carry;

            } else if (i < n1Length) {
                currNum = (num1.charAt(n1Length - 1 - i) - '0')
                        + carry;
            } else if (i < n2Length) {
                currNum = (num2.charAt(n2Length - 1 - i) - '0')
                        + carry;
            }
            carry = currNum / 10;
            result.append(currNum % 10);
            i++;
        }

        if (carry > 0) {
            result.append(carry);
        }
        return result.reverse().toString();
    }

    private String singleMulti(String num, int multiNum) {
        StringBuilder result = new StringBuilder();
        int carry = 0;
        for (int i = num.length() - 1; i >= 0; i--) {
            int currNum = (num.charAt(i) - '0') * multiNum + carry;
            carry = currNum / 10;
            result.append(currNum % 10);
        }
        if (carry > 0) {
            result.append(carry);
        }
        return result.reverse().toString();
    }

    public static void main(String[] args) {
        System.out.println(11111 * 23);
    }
}
