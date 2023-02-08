package leetcode;

import java.util.Arrays;

/**
 * @Author Hanyu.Wang
 * @Date 2023/2/5 21:35
 * @Description
 * @Version 1.0
 **/
public class P1798 {
    public static void main(String[] args) {
        System.out.println(new Solution1798().getMaximumConsecutive(new int[]{1,2,2,6}));
    }

    static class Solution1798 {
        public int getMaximumConsecutive(int[] coins) {
            Arrays.sort(coins);

            int r = 0;

            for (int i = 0; i < coins.length; i++) {
                if (coins[i] > r + 1) {
                    break;
                }
                r += coins[i];
            }

            return r + 1;
        }
    }
}
