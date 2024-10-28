package algo.dp;

import algo.hot200.Base;
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
    public void testP712() {
        String s1 = "sea", s2 = "eat";
        print(minimumDeleteSum(s1, s2)); // 231

        s1 = "delete"; s2 = "leet";
        print(minimumDeleteSum(s1, s2)); // 403
    }

    public int minimumDeleteSum(String s1, String s2) {
        // todo
    /*
                  e a t
                0 1 2 3
              s 1 2 3 4
              e 2 1 2 3
              a 3 2 1 2
         */
        int l1 = s1.length();
        int l2 = s2.length();
        int[][] dp = new int[l1 + 1][l2 + 1];
        for (int i = 1; i < l1 + 1; i++) {
            dp[i][0] = s1.charAt(i - 1);
        }
        for (int j = 1; j < l2 + 1; j++) {
            dp[0][j] = s2.charAt(j - 1);
        }
        for (int i = 1; i < dp.length; i++) {
            for (int j = 1; j < dp[0].length; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    int left = s1.charAt(i - 1);
                    int up = s2.charAt(j - 1);
                    dp[i][j] = Math.min(dp[i - 1][j - 1] + left + up, Math.min(dp[i - 1][j] + left, dp[i][j - 1] + up));
                }
            }
        }
        return dp[l1][l2];
    }

    @Test
    public void testP583() {
        String word1 = "sea", word2 = "eat";
        print(minDistance583(word1, word2)); // 2

        word1 = "leetcode"; word2 = "etco";
        print(minDistance583(word1, word2)); // 4
    }

    public int minDistance583(String word1, String word2) {
        /*
                  e a t
                0 1 2 3
              s 1 2 3 4
              e 2 1 2 3
              a 3 2 1 2
         */
        int l1 = word1.length();
        int l2 = word2.length();
        int[][] dp = new int[l1 + 1][l2 + 1];
        for (int i = 0; i < l1 + 1; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j < l2 + 1; j++) {
            dp[0][j] = j;
        }
        for (int i = 1; i < dp.length; i++) {
            for (int j = 1; j < dp[0].length; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j - 1] + 2, Math.min(dp[i - 1][j], dp[i][j - 1]) + 1);
                }
            }
        }
        return dp[l1][l2];
    }

    @Test
    public void testP72() {
        String word1 = "hh";
        String word2 = "hhh";
        print(minDistance(word1, word2)); // 1

        word1 = "horse";
        word2 = "ros";
        print(minDistance(word1, word2)); // 3

        word1 = "intention";
        word2 = "execution";
        print(minDistance(word1, word2)); // 5
    }

    public int minDistance(String word1, String word2) {
        /*
              r o s
            0 1 2 3
          h 1 1 2 3
          o 2 2 1 2
          r 3 2 2 3
          s 4 3 3 2
          e 5 4 4 3
         */
        int l2 = word2.length();
        char[] array1 = word1.toCharArray();
        char[] array2 = word2.toCharArray();
        int[] dp = new int[l2 + 1];

        for (int j = 0; j < l2 + 1; j++) {
            dp[j] = j;
        }

        int[] pre = dp.clone();

        for (int i = 1; i <= word1.length(); i++) {
            dp[0] = i;
            for (int j = 1; j <= word2.length(); j++) {
                if (array1[i - 1] == array2[j - 1]) {
                    dp[j] = pre[j - 1];
                } else {
                    dp[j] = Math.min(pre[j - 1], Math.min(pre[j], dp[j - 1])) + 1;
                }
            }
            pre = dp.clone();
        }
        return pre[l2];
    }

    public int minDistance2(String word1, String word2) {
        /*
              r o s
            0 1 2 3
          h 1 1 2 3
          o 2 2 1 2
          r 3 2 2 3
          s 4 3 3 2
          e 5 4 4 3
         */
        int l1 = word1.length();
        int l2 = word2.length();
        int[][] dp = new int[l1 + 1][l2 + 1];
        for (int i = 0; i < l1 + 1; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j < l2 + 1; j++) {
            dp[0][j] = j;
        }
        for (int i = 1; i < dp.length; i++) {
            for (int j = 1; j < dp[0].length; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1])) + 1;
                }
            }
        }
        return dp[l1][l2];
    }

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
        int[] nums = new int[]{1, 2, 3, 1};
//        print(rob(nums)); // 4
//
//        nums = new int[] {2,7,9,3,1};
//        print(rob(nums)); // 12

        nums = new int[]{2, 1, 1, 2};
//        print(rob2(nums)); // 4
    }

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
    public void testP70() {
        print(climbStairs(4));
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
        }
        return b;
    }

    public void testP322() {
        int[] coins = new int[] {2};
        int amount = 3;
        print(coinChange(coins, amount));
    }

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

        return dp[amount] == Integer.MAX_VALUE ? -1 : dp[amount];
    }
}
