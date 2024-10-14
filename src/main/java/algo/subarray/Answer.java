package algo.subarray;

import algo.hot200.Base;
import org.junit.Test;

import java.util.*;

/**
 * @Author Hanyu.Wang
 * @Date 2024/10/14 10:46
 * @Description
 * @Version 1.0
 **/
public class Answer extends Base {

    @Test
    public void testP238() {
        int[] nums = new int[] {1,2,3,4}; // [24,12,8,6]
        print(productExceptSelf(nums));

        nums = new int[] {-1,1,0,-3,3}; // [0,0,9,0,0]
        print(productExceptSelf(nums));

    }

    public int[] productExceptSelf(int[] nums) {
        int[] rightMulti = new int[nums.length];
        int rightMultiVal = 1;
        rightMulti[nums.length - 1] = 1;
        for (int i = nums.length - 2; i >= 0; i--) {
            rightMulti[i] = nums[i + 1] * rightMultiVal;
            rightMultiVal = rightMulti[i];
        }

        int leftMulti = 1;
        for (int i = 0; i < nums.length; i++) {
            rightMulti[i] = leftMulti * rightMulti[i];
            leftMulti *= nums[i];
        }
        return rightMulti;
    }

    @Test
    public void testP189() {
        int[] nums = new int[] {1,2,3,4,5,6,7}; // 5,6,7,1,2,3,4
        int k = 3;
        rotate(nums, k);
        print(nums);
    }

    public void rotate(int[] nums, int k) {
        /*
        5,6,7, 1,2,3,4
        7,6,5, 4,3,2,1
        5,6,7, 1,2,3,4

         */
        k = k % nums.length;
        reverse(nums, 0, nums.length - 1);
        reverse(nums, 0, k - 1);
        reverse(nums, k, nums.length - 1);
    }

    private void reverse(int[] nums, int start, int end) {
        while (start < end) {
            int temp = nums[start];
            nums[start] = nums[end];
            nums[end] = temp;
            start++;
            end--;
        }
    }

    @Test
    public void testP56() {
        int[][] intervals = new int[][] {{1,3},{2,6},{8,10},{15,18}}; // [[1,6],[8,10],[15,18]]
        print(merge(intervals));

        intervals = new int[][] {{1,4},{2,3}};
        print(merge(intervals));
    }

    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[0] - o2[0];
            }
        });

        int index = 0;
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] <= intervals[index][1]) {
                intervals[index][1] = Math.max(intervals[i][1], intervals[index][1]);
            } else {
                index++;
                intervals[index][0] = intervals[i][0];
                intervals[index][1] = intervals[i][1];
            }
        }
        int[][] result = new int[index + 1][2];
        System.arraycopy(intervals, 0, result, 0, index + 1);
        return result;
    }


    @Test
    public void testP53() {
        int[] nums = new int[] {-2,1,-3,4,-1,2,1,-5,4};
        print(maxSubArray(nums)); // 6

        nums = new int[] {1};
        print(maxSubArray(nums)); // 1

        nums = new int[] {5,4,-1,7,8};
        print(maxSubArray(nums)); // 23
    }

    public int maxSubArray(int[] nums) {
        int preSum = nums[0];
        int max = preSum;

        for (int i = 1; i < nums.length; i++) {
            int currSum = Math.max(nums[i], nums[i] + preSum);
            max = Math.max(max, currSum);
            preSum = currSum;
        }

        return max;
    }

    public int maxSubArray2(int[] nums) {
        int[] dp = new int[nums.length];
        dp[0] = nums[0];
        int max = dp[0];

        for (int i = 1; i < nums.length; i++) {
            dp[i] = Math.max(nums[i], nums[i] + dp[i - 1]);
            max = Math.max(max, dp[i]);
        }

        return max;
    }

    @Test
    public void testP560() {
        int[] nums = new int[] {1,1,1};
        int k = 2;
        //print(subarraySum(nums, k)); // 2

        nums = new int[] {1,2,3};
        k = 5;
        print(subarraySum(nums, k)); // 2

//        nums = new int[] {100,1,2,3,4};
//        k = 6;
//        print(subarraySum(nums, k));
    }

    public int subarraySum(int[] nums, int k) {

        int ans = 0;
        for (int i = 0; i < nums.length; i++) {
            int sum = 0;
            for (int j = i; j >= 0; j--) {
                sum += nums[j];
                if (sum == k) {
                    ans++;
                }
            }
        }
        return ans;
    }
    /*
    使用前缀和的方法可以解决这个问题，因为我们需要找到和为k的连续子数组的个数。通过计算前缀和，我们可以将问题转化为求解两个前缀和之差等于k的情况。

假设数组的前缀和数组为prefixSum，其中prefixSum[i]表示从数组起始位置到第i个位置的元素之和。那么对于任意的两个下标i和j（i < j），如果prefixSum[j] - prefixSum[i] = k，即从第i个位置到第j个位置的元素之和等于k，那么说明从第i+1个位置到第j个位置的连续子数组的和为k。

通过遍历数组，计算每个位置的前缀和，并使用一个哈希表来存储每个前缀和出现的次数。在遍历的过程中，我们检查是否存在prefixSum[j] - k的前缀和，如果存在，说明从某个位置到当前位置的连续子数组的和为k，我们将对应的次数累加到结果中。

这样，通过遍历一次数组，我们可以统计出和为k的连续子数组的个数，并且时间复杂度为O(n)，其中n为数组的长度。
     */
    public int subarraySum2(int[] nums, int k) {
        Map<Integer, Integer> preSum = new HashMap<>();

        int ans = 0;
        // pre[i] = pre[i - 1] + nums[i]
        // pre[i] - pre[j - 1] = k
        int pre = 0;
        preSum.put(0, 1);
        for (int i = 0; i < nums.length; i++) {
            pre += nums[i];
            if (preSum.containsKey(pre - k)) {
                ans += preSum.get(pre - k);
            }
            preSum.put(pre, preSum.getOrDefault(pre, 0) + 1);
        }
        return ans;
    }
}
