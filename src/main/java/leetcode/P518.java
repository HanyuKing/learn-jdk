package leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2023/11/15 19:50
 * @Description
 * @Version 1.0
 **/
public class P518 {
    public static void main(String[] args) {
        int amount = 5;
        int[] coins = new int[]{1, 2, 5};
//        int amount = 3;
//        int[] coins = new int[]{2};
        int result = new P518().change4(amount, coins);
        System.out.println(result);
    }

    public int change4(int amount, int[] coins) {
        int[] dp = new int[amount + 1];

        dp[0] = 1;

        for (int i = 1; i <= coins.length; i++) {
            int value = coins[i - 1];
            for (int j = 1; j <= amount; j++) {
                if (j - value >= 0) {
                    dp[j] += dp[j - value];
                }
            }
        }
        return dp[amount];
    }

    public int change3(int amount, int[] coins) {
        int[][] dp = new int[coins.length + 1][amount + 1];

        dp[0][0] = 1;

        for (int i = 1; i <= coins.length; i++) {
            int value = coins[i - 1];
            for (int j = 0; j <= amount; j++) {
                dp[i][j] = dp[i - 1][j];
                if (j - value >= 0) {
                    dp[i][j] += dp[i][j - value];
                }
            }
        }
        return dp[coins.length][amount];
    }

    public int change2(int amount, int[] coins) {
        int[][] dp = new int[coins.length + 1][amount + 1];

        dp[0][0] = 1;

        for (int i = 1; i <= coins.length; i++) {
            int value = coins[i - 1];
            for (int j = 0; j <= amount; j++) {
                for (int k = 0; k*value <= j; k++) {
                    dp[i][j] += dp[i - 1][j - k*value];
//                    if (k - value >= 0) {
//                        dp[i][j] += dp[i - 1][j - k*value];
//                    } else {
//                        dp[i][j] = dp[i - 1][j];
//                    }
                }
            }
        }
        return dp[coins.length][amount];
    }

    public int change(int amount, int[] coins) {
        int[][] dp = new int[coins.length + 1][amount + 1];

        dp[0][0] = 1;

        for (int i = 1; i <= coins.length; i++) {
            int value = coins[i - 1];
            for (int j = 0; j <= amount; j++) {
                if (j - value >= 0) {
                    dp[i][j] = dp[i][j - value] + dp[i - 1][j];
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }
        return dp[coins.length][amount];
    }
}
