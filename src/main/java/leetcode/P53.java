package leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2023/10/31 15:38
 * @Description
 * @Version 1.0
 **/
public class P53 {
    public static void main(String[] args) {
        int[] nums = new int[] {-2, 1, -3, 4, -1, 2, 1, -5, 4}; // 6
        int result = new P53().maxSubArray(nums);
        System.out.println(result);
    }

    public int maxSubArray(int[] nums) {
        int max = nums[0];

        int[] dp = new int[nums.length];
        dp[0] = nums[0];

        for (int i = 1; i < nums.length; i++) {
            dp[i] = Math.max(nums[i], nums[i] + dp[i - 1]);
            max = Math.max(max, dp[i]);
        }

        return max;
    }

}
