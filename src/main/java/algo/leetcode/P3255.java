package algo.leetcode;

import java.util.Arrays;

/**
 * @Author Hanyu.Wang
 * @Date 2024/11/7 11:07
 * @Description
 * @Version 1.0
 **/
public class P3255 {

    public static void main(String[] args) {
        System.out.println(Arrays.toString(new P3255().resultsArray(new int[]{1, 2, 3, 4, 3, 2, 5}, 3)));

        System.out.println(Arrays.toString(new P3255().resultsArray(new int[]{2,2,2,2,2}, 4)));

        System.out.println(Arrays.toString(new P3255().resultsArray(new int[]{3,2,3,2,3,2}, 2)));

        System.out.println(Arrays.toString(new P3255().resultsArray(new int[]{1, 7, 8}, 2)));
    }
    public int[] resultsArray(int[] nums, int k) {
        int[] result = new int[nums.length - k + 1];
        Arrays.fill(result, -1);
        int count = 0;
        for (int i = 0; i < nums.length; i++) {
            count = i == 0 || nums[i] == nums[i - 1] + 1 ? count + 1 : 1;
            if (count >= k) {
                result[i - k + 1] = nums[i];
            }
        }
        return result;
    }
    public int[] resultsArray2(int[] nums, int k) {
        if (k == 1) {
            return nums;
        }
        int[] result = new int[nums.length - k + 1];
        Arrays.fill(result, -1);
        for (int i = 0; i <= nums.length - k; i++) {
            int max = nums[i + k - 1];
            for (int j = i + 1; j < i + k; j++) {
                if (nums[j] != nums[j - 1] + 1) {
                    max = -1;
                    break;
                }
            }
            result[i] = max;
        }
        return result;
    }
}
