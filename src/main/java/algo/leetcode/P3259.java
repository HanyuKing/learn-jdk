package algo.leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2024/11/1 16:17
 * @Description
 * @Version 1.0
 **/
public class P3259 {
    public static void main(String[] args) {
        System.out.println(new P3259().maxEnergyBoost(new int[] {1,3,1}, new int[]{3,1,1})); // 5

        System.out.println(new P3259().maxEnergyBoost(new int[] {4,1,1}, new int[]{1,1,3})); // 7

    }
    public long maxEnergyBoost(int[] energyDrinkA, int[] energyDrinkB) {
        int n = energyDrinkA.length;
        long[][] dp = new long[n][2];
        dp[0][0] = energyDrinkA[0]; // switch
        dp[0][1] = energyDrinkB[0]; // not switch

        for (int i = 1; i < n; i++) {
            dp[i][0] = dp[i - 1][0] + energyDrinkA[i];
            dp[i][1] = dp[i - 1][1] + energyDrinkB[i];
            if (i >= 2) {
                dp[i][0] = Math.max(dp[i][0], dp[i - 2][1] + energyDrinkA[i]);
                dp[i][1] = Math.max(dp[i][1], dp[i - 2][0] + energyDrinkB[i]);
            }
        }
        return Math.max(dp[n - 1][0], dp[n - 1][1]);
    }
}
