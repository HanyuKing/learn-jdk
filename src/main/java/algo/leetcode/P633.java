package algo.leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2024/11/4 19:34
 * @Description
 * @Version 1.0
 **/
public class P633 {
    public static void main(String[] args) {
        System.out.println(new P633().judgeSquareSum(5)); // true

        System.out.println(new P633().judgeSquareSum(3)); // false

        System.out.println(new P633().judgeSquareSum(4)); // true

        System.out.println(new P633().judgeSquareSum(2147483600)); // true

    }

    public boolean judgeSquareSum(int c) {
        // 2 * 2 + 1 * 1
        int sqrtC = (int) Math.sqrt(c);
        long start = 0;
        long end = sqrtC;
        while (start <= end) {
            long value = start * start + end * end;
            if (value == c) {
                return true;
            } else if (value < c) {
                start++;
            } else {
                end--;
            }
        }
        return false;
    }
}
