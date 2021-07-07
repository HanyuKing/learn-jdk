package jianzhioffer;

/**
 * 1 -> 1 = 1
 * 2 -> 1*1 = 1
 * 3 -> 1*1*1, 1*2 = 2
 * 4 -> 1*1*1*1, 1*1*2, 2*2, 1*3 = 4
 * 5 -> = 6
 * 6 -> = 9
 * 7 -> = 12
 * 8 -> = 18
 */

/**
 * f(1) = 1
 * f(2) = 1
 * f(3) = 2
 *
 * i=1->n
 * int currMax = n
 * f(n) = max(dp[n-i]*i, currMax)
 */
public class JZ67 {
    public static void main(String[] args) {
        System.out.println(new JZ67().cutRope(8));
    }

    public int cutRope(int target) {
        if (target <= 0) return target;
        if (target <= 2) return 1;
        if (target == 3) return 2;

        int[] dp = new int[target + 1];
        dp[1] = 1;
        dp[2] = 1;
        dp[3] = 2;

        for (int i = 4; i <= target; i++) {
            dp[i] = i;
            for (int j = 1; j < i; j++) {
                int max = Math.max(dp[i - j] * dp[j], dp[i]);
                max = Math.max(dp[i - j] * j, max);
                max = Math.max((i - j) * j, max);
                max = Math.max((i - j) * dp[j], max);
                dp[i] = max;
            }
        }

        return dp[target];
    }
}
