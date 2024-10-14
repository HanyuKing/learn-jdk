package algo.leetcode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Hanyu.Wang
 * @Date 2023/10/31 09:54
 * @Description
 * @Version 1.0
 **/
public class P494 {
    public static void main(String[] args) {
        int[] nums = new int[] {1, 1, 1, 1, 1};
        int target = 3;
//        int[] nums = new int[] {0,0,0,0,0,0,0,0,1};
//        int target = 1;
//        int[] nums = new int[] {100};
//        int target = -200;
        int result = new P494().findTargetSumWays4(nums, target);
        System.out.println(result);
    }

    public int findTargetSumWays4(int[] nums, int target) {
        int sum = Arrays.stream(nums).sum();

        if (Math.abs(target) > sum) {
            return 0;
        }

        int len = 2 * sum + 1;
        int[][] dp = new int[2][len];

        dp[0][nums[0] + sum] = 1;
        dp[0][-nums[0] + sum] += 1;

        for (int i = 1; i < nums.length; i++) {
            for (int j = 0; j < len; j++) {
                if (j - nums[i] >= 0) {
                    dp[1][j] += dp[0][j - nums[i]];
                }
                if (j + nums[i] < len) {
                    dp[1][j] += dp[0][j + nums[i]];
                }
            }
            for (int j = 0; j < len; j++) {
                dp[0][j] = dp[1][j];
                dp[1][j] = 0;
            }
        }

        return dp[0][target + sum];
    }

    public int findTargetSumWays3(int[] nums, int target) {
        int sum = Arrays.stream(nums).sum();

        if (Math.abs(target) > sum) {
            return 0;
        }

        int len = 2 * sum + 1;
        int[][] dp = new int[nums.length][len];

        dp[0][nums[0] + sum] = 1;
        dp[0][-nums[0] + sum] += 1;

        for (int i = 1; i < nums.length; i++) {
            for (int j = 0; j < len; j++) {
                if (j - nums[i] >= 0) {
                    dp[i][j] += dp[i - 1][j - nums[i]];
                }
                if (j + nums[i] < len) {
                    dp[i][j] += dp[i - 1][j + nums[i]];
                }
            }
        }

        return dp[nums.length - 1][target + sum];
    }

    public int findTargetSumWays2(int[] nums, int target) {
        int sum = Arrays.stream(nums).sum();
        if (sum < Math.abs(target)) {
            return 0;
        }
        if (nums.length == 1) {
            if (nums[0] == Math.abs(target)) {
                return 1;
            } else {
                return 0;
            }
        }

        Map<Integer, Integer> sumCountMap = new HashMap<>();

        if (nums[0] == 0) {
            sumCountMap.put(0, 2);
        } else {
            sumCountMap.put(nums[0], 1);
            sumCountMap.put(-nums[0], 1);
        }

        for (int i = 1; i < nums.length - 1; i++) {
            Map<Integer, Integer> newSumCountMap = new HashMap<>();
            for (Map.Entry<Integer, Integer> entry : sumCountMap.entrySet()) {
                Integer s = nums[i] + entry.getKey();
                Integer count = newSumCountMap.getOrDefault(s, entry.getValue());
                newSumCountMap.put(s, count);
            }
            for (Map.Entry<Integer, Integer> entry : sumCountMap.entrySet()) {
                Integer s = -nums[i] + entry.getKey();
                Integer count = newSumCountMap.getOrDefault(s, null);
                if (count == null) {
                    newSumCountMap.put(s, entry.getValue());
                } else {
                    newSumCountMap.put(s, count + entry.getValue());
                }
            }
            sumCountMap = newSumCountMap;
        }

        return sumCountMap.getOrDefault(target - nums[nums.length - 1], 0) + sumCountMap.getOrDefault(target + nums[nums.length - 1], 0);
    }

    private int count = 0;

    public int findTargetSumWays(int[] nums, int target) {
        if (Arrays.stream(nums).sum() < Math.abs(target)) {
            return 0;
        }
        doFindTargetSumWays(nums, 0, target);
        return count;
    }

    private void doFindTargetSumWays(int[] nums, int pos, int target) {
        if (pos == nums.length) {
            if (target == 0) {
                count++;
            }
            return;
        }
        doFindTargetSumWays(nums, pos + 1, target + nums[pos]);
        doFindTargetSumWays(nums, pos + 1, target - nums[pos]);
    }
}
