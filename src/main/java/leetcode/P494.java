package leetcode;

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
        int result = new P494().findTargetSumWays2(nums, target);
        System.out.println(result);
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
