package algo.leetcode;

import java.util.Arrays;

/**
 * @Author Hanyu.Wang
 * @Date 2023/11/6 17:00
 * @Description
 * @Version 1.0
 **/
public class P416 {
    public static void main(String[] args) {
//        int[] nums = new int[] {1, 5, 11, 5};

//        int[] nums = new int[] {1, 2, 3, 5};

//        int[] nums = new int[] {2, 2, 3, 5};
        int[] nums = new int[] {14, 9, 8, 4, 3, 2};

//        int[] nums = new int[] {1, 2, 5};
        System.out.println(new P416().canPartition(nums));
    }

    public boolean canPartition(int[] nums) {
        int sum = Arrays.stream(nums).sum();
        if ((sum & 1) == 1) {
            return false;
        }

        int target = sum / 2;

        boolean[][] dp = new boolean[nums.length][target + 1];
        if (nums[0] <= target) {
            dp[0][nums[0]] = true;
        }

        for (int i = 1; i < nums.length; i++) {
            for (int j = 1; j <= target; j++) {
                dp[i][j] = dp[i - 1][j];
                if (j - nums[i] >= 0) {
                    dp[i][j] = dp[i - 1][j] || dp[i - 1][j - nums[i]];
                }
                if (dp[i][target]) {
                    return true;
                }
            }
        }

        return dp[nums.length - 1][target];
    }

}
