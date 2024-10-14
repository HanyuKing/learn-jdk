package algo.leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2023/1/1 23:23
 * @Description
 * @Version 1.0
 **/
public class P6196 {
    public static void main(String[] args) {
        System.out.println(new P6196().minimumPartition("1829727651", 10));
    }

    public int minimumPartition(String s, int k) {
        int digit = 1;
        int tempK = k;
        while ((tempK / 10) != 0) {
            digit++;
            tempK = tempK / 10;
        }

        // init
        int[] dp = new int[s.length()];
        for (int i = 0; i < digit - 1; i++) {
            dp[i] = 1;
        }

        dp[digit - 1] = Integer.parseInt(s.substring(0, digit)) <= k ? 1 : digit - 2 >= 0 ? dp[digit - 2] + 1 : -1;

        for (int i = digit; i < s.length(); i++) {
            if (Integer.parseInt(s.substring(i, i + 1)) > k) {
                return -1;
            }
            dp[i] = dp[i-1] + 1;
            for (int j = i - digit + 1; j < i; j++) {
                if (Integer.parseInt(s.substring(j, i + 1)) <= k) {
                    dp[i] = Math.min(dp[i], dp[j - 1] + 1);
                }
            }
        }

        return dp[s.length() - 1];
    }
}
