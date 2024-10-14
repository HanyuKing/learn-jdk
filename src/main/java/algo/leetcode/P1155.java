package algo.leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2023/10/26 10:18
 * @Description
 * @Version 1.0
 **/
public class P1155 {
    public static void main(String[] args) {
        int n = 30;
        int k = 30;
        int target = 500;

        System.out.println(new P1155().numRollsToTarget(n, k, target));
    }

    public int numRollsToTarget(int n, int k, int target) {
        if (n * k < target || n > target) {
            return 0;
        }

        int[][] dp = new int[n + 1][target + 1];

        for (int i = 1; i <= k && i <= target; i++) {
            dp[1][i] = 1;
        }

        int mod = (1000000000 + 7);

        for (int i = 2; i <= n; i++) {
            dp[i][i] = 1;
            for (int j = i + 1; j <= target; j++) {
                for (int kk = 1; kk <= j - i + 1 && kk <= k; kk++) {
                    dp[i][j] = (dp[i][j] + dp[i - 1][j - kk]) % mod;
                    /*
                        if (j - kk > 0) {
                            dp[i][j] = (dp[i][j] + dp[i - 1][j - kk]) % mod;
                        }
                    */
                }

            }
        }

        return dp[n][target];
    }
}
