package algo.dp;

import java.util.Arrays;

/**
 * @Author Hanyu.Wang
 * @Date 2023/11/17 10:37
 * @Description
 * @Version 1.0
 **/
public class P322_Coin_Change {

    public static void main(String[] args) {
        int[] coins = new int[] {1, 2, 5};
        int amount = 11;

//        int[] coins = new int[] { 2};
//        int amount = 3;
//
//        int[] coins = new int[] {1};
//        int amount = 0;

        int result = new P322_Coin_Change().coinChange3(coins, amount);

        System.out.println(result);
    }

    public int coinChange3(int[] coins, int amount) {
        if (amount == 0) {
            return 0;
        }

        int[] dp = new int[amount + 1];

        Arrays.fill(dp, amount + 1);

        dp[0] = 0;

        for (int i = 0; i < coins.length; i++) {
            int value = coins[i];
            for (int j = 0; j <= amount; j++) {
                if (j - value >= 0) {
                    dp[j] = Math.min(dp[j], dp[j - value] + 1);
                }
            }
        }

        /*

            f[i][j] = Math.min(f[i][j], f[i - 1][j - 0 * value] + 0)
                      Math.min(f[i][j], f[i - 1][j - 1 * value] + 1)
                      Math.min(f[i][j], f[i - 1][j - 2 * value] + 2)
                      Math.min(f[i][j], f[i - 1][j - 3 * value] + 3)
                      Math.min(f[i][j], f[i - 1][j - k * value] + k)

    f[i][j - value] = Math.min(f[i][j - value], f[i - 1][j - 1 * value] + 0)
                      Math.min(f[i][j - value], f[i - 1][j - 2 * value] + 1)
                      Math.min(f[i][j - value], f[i - 1][j - 3 * value] + 2)
                      Math.min(f[i][j - value], f[i - 1][j - 4 * value] + 3)
                      Math.min(f[i][j - value], f[i - 1][j - k * value] + k)

            *f[i][j] = Math.min(f[i - 1][j], f[i][j - value] + 1)

            *f[j] = Math.min(f[j], f[j - value] + 1)

         */

        return dp[amount] == amount + 1 ? -1 : dp[amount];
    }

    public int coinChange2(int[] coins, int amount) {
        if (amount == 0) {
            return 0;
        }

        int[][] dp = new int[coins.length + 1][amount + 1];

        for (int i = 0; i < coins.length + 1; i++) {
            Arrays.fill(dp[i], amount + 1);
        }

        dp[0][0] = 0;

        for (int i = 1; i <= coins.length; i++) {
            int value = coins[i - 1];
            for (int j = 0; j <= amount; j++) {
                dp[i][j] = dp[i - 1][j];
                if (j - value >= 0) {
                    dp[i][j] = Math.min(dp[i][j], dp[i][j - value] + 1);
                }
            }
        }

        /*

            f[i][j] = Math.min(f[i][j], f[i - 1][j - 0 * value] + 0)
                      Math.min(f[i][j], f[i - 1][j - 1 * value] + 1)
                      Math.min(f[i][j], f[i - 1][j - 2 * value] + 2)
                      Math.min(f[i][j], f[i - 1][j - 3 * value] + 3)
                      Math.min(f[i][j], f[i - 1][j - k * value] + k)

    f[i][j - value] = Math.min(f[i][j - value], f[i - 1][j - 1 * value] + 0)
                      Math.min(f[i][j - value], f[i - 1][j - 2 * value] + 1)
                      Math.min(f[i][j - value], f[i - 1][j - 3 * value] + 2)
                      Math.min(f[i][j - value], f[i - 1][j - 4 * value] + 3)
                      Math.min(f[i][j - value], f[i - 1][j - k * value] + k)

            *f[i][j] = Math.min(f[i - 1][j], f[i][j - value] + 1)

            *f[j] = Math.min(f[j], f[j - value] + 1)

         */

        return dp[coins.length][amount] == amount + 1 ? -1 : dp[coins.length][amount];
    }

    public int coinChange(int[] coins, int amount) {
        if (amount == 0) {
            return 0;
        }

        int[][] dp = new int[coins.length + 1][amount + 1];

        for (int i = 0; i < coins.length + 1; i++) {
            for (int j = 0; j < amount + 1; j++) {
                dp[i][j] = -1;
            }
        }

        dp[0][0] = 0;

        for (int i = 1; i <= coins.length; i++) {
            int value = coins[i - 1];
            for (int j = 0; j <= amount; j++) {

                for (int k = 0; k * value <= j; k++) {
                    if (dp[i - 1][j - k * value] >= 0) {
                        if (dp[i][j] == -1) {
                            dp[i][j] = dp[i - 1][j - k * value] + k;
                        } else {
                            dp[i][j] = Math.min(dp[i][j], dp[i - 1][j - k * value] + k);
                        }
                    }
                }
            }
        }

        /*

            f[i][j] = Math.min(f[i][j], f[i - 1][j - 0 * value] + 0)
                      Math.min(f[i][j], f[i - 1][j - 1 * value] + 1)
                      Math.min(f[i][j], f[i - 1][j - 2 * value] + 2)
                      Math.min(f[i][j], f[i - 1][j - 3 * value] + 3)
                      Math.min(f[i][j], f[i - 1][j - k * value] + k)

    f[i][j - value] = Math.min(f[i][j - value], f[i - 1][j - 1 * value] + 0)
                      Math.min(f[i][j - value], f[i - 1][j - 2 * value] + 1)
                      Math.min(f[i][j - value], f[i - 1][j - 3 * value] + 2)
                      Math.min(f[i][j - value], f[i - 1][j - 4 * value] + 3)
                      Math.min(f[i][j - value], f[i - 1][j - k * value] + k)

            *f[i][j] = Math.min(f[i - 1][j], f[i][j - value] + 1)

            *f[j] = Math.min(f[j], f[j - value] + 1)

         */

        return dp[coins.length][amount] == 0 ? -1 : dp[coins.length][amount];
    }
}
