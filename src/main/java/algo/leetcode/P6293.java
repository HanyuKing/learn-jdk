package algo.leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Hanyu.Wang
 * @Date 2023/1/15 11:50
 * @Description
 * @Version 1.0
 **/
public class P6293 {
    public static void main(String[] args) {
        System.out.println(countGood(new int[]{1,1,1,1,1}, 10));
    }

    public static long countGood(int[] nums, int k) {
        long cnt = 0;
        Map<String, Boolean> map = new HashMap<>();
        for (int gap = 1; gap < nums.length; gap++) {
            int goodsArrayNum = 0;
            for (int i = 0; i < nums.length; i++) {
                int j = Math.min(i + gap, nums.length - 1);

                if (Boolean.TRUE.equals(map.getOrDefault(i + "_" + j, Boolean.FALSE))) {
                    continue;
                }

                if (isGood(i, j, nums)) {
                    System.out.println(i + "_" + j);
                    map.put(i + "_" + j, Boolean.TRUE);
                    goodsArrayNum++;
                }
            }
            if (goodsArrayNum >= k) {
                cnt++;
            }
        }
        return cnt;
    }

    public static boolean isGood(int i, int j, int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        for (; i <= j; i++) {
            int value = map.getOrDefault(nums[i], 0);
            map.put(nums[i], ++value);
        }
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() >= 2) {
                return true;
            }
        }
        return false;
    }
}
