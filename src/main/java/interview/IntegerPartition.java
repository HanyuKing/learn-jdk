package interview;

public class IntegerPartition {

    public static int countPartitions(int n) {
        // 只需要一个一维数组 dp，用于存储拆分数
        int[][] dp = new int[n + 1][n + 1];
        for (int i = 0; i < n + 1; i++) {
            dp[0][i] = 1;
        }

        for (int i = 1; i <= n; i++) {
            // 对 dp 数组进行更新
            for (int j = 1; j <= i; j++) {
                if (i < j) {
                    dp[i][j] = dp[i][i];
                } else {
                    dp[i][j] = dp[i][j - 1] + dp[i - j][j];
                }
            }
        }

        return dp[n][n];  // dp[n] 就是最终的答案
    }

    public static void main(String[] args) {
        int n = 6;
        long result = countPartitions(n);
        System.out.println("将 " + n + " 拆分为若干正整数之和的方案数为: " + result);
    }
}
