package algo.dp;

import algo.hot200.Base;
import jol.A;
import org.junit.Test;

import java.util.*;

/**
 * @Author Hanyu.Wang
 * @Date 2024/10/24 15:13
 * @Description
 * @Version 1.0
 **/
public class Answer extends Base {

    @Test
    public void testP279() {
        print(numSquares2(12)); // 12 = 4 + 4 + 4

        print(numSquares2(13)); // 13 = 4 + 9
    }

    public int numSquares3(int n) {
        int[] dp = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            int min = Integer.MAX_VALUE;
            for (int j = 1; j * j <= i; j++) {
                min = Math.min(min, dp[i - j * j] + 1);
            }
            dp[i] = min;
        }
        return dp[n];
    }

    public int numSquares2(int n) {
        int[] dp = new int[n + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        int sqrtN = (int) Math.sqrt(n);
        for (int i = 1; i <= sqrtN; i++) {
            dp[i * i] = 1;
        }
        for (int i = 1; i <= n; i++) {
            int min = dp[i];
            for (int j = 1; j * j < i; j++) {
                min = Math.min(min, dp[i - j * j] + 1);
            }
            dp[i] = min;
        }
        return dp[n];
    }

    public int numSquares(int n) {
        int[] dp = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            int min = Integer.MAX_VALUE;
            for (int j = 1; j * j <= i; j++) {
                min = Math.min(min, dp[i - j * j]);
            }
            dp[i] = min + 1;
        }
        return dp[n];
    }

    @Test
    public void testP198() {
        int[] nums = new int[] {1,2,3,1};
//        print(rob(nums)); // 4
//
//        nums = new int[] {2,7,9,3,1};
//        print(rob(nums)); // 12

        nums = new int[] {2,1,1,2};
        print(rob2(nums)); // 4
    public void testP300() {
        int[] nums = new int[] {1,3,6,7,9,4,10,5,6};
        print(lengthOfLIS(nums));
    }

    public int lengthOfLIS(int[] nums) {
        int[] dp = new int[nums.length];
        Arrays.fill(dp, 1);
        int max = 1;
        for (int i = 1; i < nums.length; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            max = Math.max(max, dp[i]);
        }

        return max;
    }
    @Test
    }


            }
            newRow.add(1);
            result.add(newRow);
        }

        return dp[s.length()];
    }

    @Test
    public void testP70() {
        print(climbStairs(4));
    public void testP322() {
        int[] coins = new int[] {2};
        int amount = 3;
        print(coinChange(coins, amount));
    }

    public int climbStairs(int n) {
        if (n == 1) {
            return 1;
        }
        int a = 1;
        int b = 2;
        for (int i = 3; i <= n; i++) {
            int curr = a + b;
            a = b;
            b = curr;
    public int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;

        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (i - coin >= 0 && dp[i - coin] != Integer.MAX_VALUE) {
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }
    }
}
