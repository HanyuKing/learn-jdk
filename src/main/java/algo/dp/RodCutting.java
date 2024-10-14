package algo.dp;

/**
 * @Author Hanyu.Wang
 * @Date 2023/11/15 15:45
 * @Description https://www.geeksforgeeks.org/problems/rod-cutting0840/1
 * @Version 1.0
 **/
public class RodCutting {
    public static void main(String[] args) {
//        int[] price = new int[] {1, 5, 8, 9, 10, 17, 17, 20};
//        int n = 8; // 5 + 7 = 22

        int[] price = new int[] {3, 5, 8, 9, 10, 17, 17, 20};
        int n = 8; // 8*price[1]= 8*3 = 24

        int result = new RodCutting().cutRod(price, n);

        System.out.println(result);
    }

    public int cutRod(int[] price, int n) {
        int[] dp = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            for (int j = i; j > 0; j--) {
                dp[i] = Math.max(dp[i], price[j - 1] + dp[i - j]);
            }
        }

        return dp[n];
    }
}
