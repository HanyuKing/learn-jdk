package leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2022/12/13 11:09
 * @Description
 * @Version 1.0
 **/
public class P29 {

    public static int add(int num1, int num2){
        if(num2 == 0) {
            return num1;
        }
        int sum = num1 ^ num2;
        int carry = (num1 & num2) << 1;
        return add(sum, carry);
    }

    public static int substract(int num1, int num2) {
        return -1;
    }

    public static void main(String[] args) {
        int num = 7;

        System.out.println(~num + 1);
    }

    //给定两个整数，被除数 dividend 和除数 divisor。将两数相除，要求不使用乘法、除法和 mod 运算符。
//
// 返回被除数 dividend 除以除数 divisor 得到的商。
//
// 整数除法的结果应当截去（truncate）其小数部分，例如：truncate(8.345) = 8 以及 truncate(-2.7335) = -2
//
//
//
// 示例 1:
//
// 输入: dividend = 10, divisor = 3
//输出: 3
//解释: 10/3 = truncate(3.33333..) = truncate(3) = 3
//
// 示例 2:
//
// 输入: dividend = 7, divisor = -3
//输出: -2
//解释: 7/-3 = truncate(-2.33333..) = -2
//
//
//
// 提示：
//
//
// 被除数和除数均为 32 位有符号整数。
// 除数不为 0。
// 假设我们的环境只能存储 32 位有符号整数，其数值范围是 [−2³¹, 231 − 1]。本题中，如果除法结果溢出，则返回 231 − 1。
//
//
// Related Topics 位运算 数学 👍 1026 👎 0


    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int divide(int dividend, int divisor) {
            return dividend / divisor;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}
