package algo.leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2023/10/26 09:41
 * @Description
 * @Version 1.0
 **/
public class P2520 {
    public int countDigits(int num) {
        int count = 0;

        char[] numStr = Integer.valueOf(num).toString().toCharArray();
        for (char c : numStr) {
            if (num % (c - '0') == 0) {
                count++;
            }
        }

        return count;
    }
}
