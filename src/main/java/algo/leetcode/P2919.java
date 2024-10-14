package algo.leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2023/10/30 19:21
 * @Description
 * @Version 1.0
 **/
public class P2919 {
    public static void main(String[] args) {
        int[] nums = new int[]{2, 3, 0, 0, 2};
        int k = 4;
//        int[] nums = new int[]{0, 1, 3, 3};
//        int k = 5;
//        int[] nums = new int[]{7, 7, 2, 7};
//        int k = 9;
//        int[] nums = new int[]{43,31,14,4};
//        int k = 73;
        long result = new P2919().minIncrementOperations(nums, k);
        System.out.println(result);
    }

    public long minIncrementOperations(int[] nums, int k) {
        long[] dp = new long[nums.length];
        for (int i = 0; i < 3; i++) {
            dp[i] = Math.max(0, k - nums[i]);
        }
        for (int i = 3; i < nums.length; i++) {
            dp[i] = Math.min(dp[i-3], Math.min(dp[i - 2], dp[i - 1])) + Math.max(0, k - nums[i]);
        }

        return Math.min(dp[nums.length - 3], Math.min(dp[nums.length - 1], dp[nums.length - 2]));
    }
}
