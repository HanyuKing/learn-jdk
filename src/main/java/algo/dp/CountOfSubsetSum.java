package algo.dp;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Hanyu.Wang
 * @Date 2023/11/7 16:13
 * @Description
 *  https://practice.geeksforgeeks.org/problems/perfect-sum-problem5633/1
 *
 * Given an array arr of non-negative integers and an integer sum, the task is to count all subsets of the given array with a sum equal to a given sum.
 *
 * Note: Answer can be very large, so, output answer modulo 109+7.
 *
 * Example 1:
 *
 * Input:
 * N = 6
 * arr = [5, 2, 3, 10, 6, 8]
 * sum = 10
 * Output:
 * 3
 * Explanation:
 * {5, 2, 3}, {2, 8}, {10} are possible subsets.
 *
 * Example 2:
 * Input:
 * N = 5
 * arr = [2, 5, 1, 4, 3]
 * sum = 10
 * Output:
 * 3
 * Explanation:
 * {2, 1, 4, 3}, {5, 1, 4}, {2, 5, 3} are possible subsets.
 * Your Task:
 * You don't need to read input or print anything. Complete the function perfectSum() which takes N, array arr and sum as input parameters and returns an integer value.
 *
 * Expected Time Complexity: O(N*sum)
 * Expected Auxiliary Space: O(N*sum)
 *
 * Constraints:
 * 1 ≤ N*sum ≤ 106
 * 0 ≤ arr[i] ≤ 106
 *
 * @Version 1.0
 **/
public class CountOfSubsetSum {
    public static void main(String[] args) {
//        int[] array = new int[]{5, 2, 3, 10, 6, 8};
//        int N = 6;
//        int sum = 10;

//        int[] array = new int[]{2, 5, 1, 4, 3};
//        int N = 5;
//        int sum = 10;

        int[] array = new int[]{9, 7, 0, 3, 9, 8, 6, 5, 7, 6};
        int N = 10;
        int sum = 31;

        System.out.println(new CountOfSubsetSum().perfectSum2(array, N, sum));
    }

    public int perfectSum2(int[] nums, int n, int sum) {
        Map<Integer, Integer> sumCountMap = new HashMap<>();

        int MOD = (int) (Math.pow(10, 9) + 7);

        for (int num : nums) {
            if (num > sum) {
                continue;
            }

            Map<Integer, Integer> newSumCountMap = new HashMap<>();
            newSumCountMap.put(num, 1);

            for (Map.Entry<Integer, Integer> sumCount : sumCountMap.entrySet()) {
                int newSum = sumCount.getKey() + num;

                if (newSum > sum) {
                    continue;
                }

                newSumCountMap.put(newSum, (sumCountMap.getOrDefault(sumCount.getKey(), 1)
                        + newSumCountMap.getOrDefault(newSum, 0)) % MOD);
            }

            for (Map.Entry<Integer, Integer> sumCount : newSumCountMap.entrySet()) {
                sumCountMap.put(sumCount.getKey(), (sumCountMap.getOrDefault(sumCount.getKey(), 0) + sumCount.getValue()) % MOD);
            }

        }
        return sumCountMap.get(sum) == null ? 0 : sumCountMap.get(sum);
    }

    public int perfectSum(int[] nums, int n, int sum) {

        int[][] dp = new int[n][sum + 1];
        if (nums[0] <= sum) {
            dp[0][nums[0]] = 1;
        }

        int MOD = (int) Math.pow(10, 9) + 7;

        for (int i = 1; i < n; i++) {
            if (nums[i] <= sum) {
                dp[i][nums[i]] = 1;
            }
            for (int j = 0; j <= sum; j++) {
                dp[i][j] = (dp[i][j] + dp[i - 1][j]) % MOD;
                if (j >= nums[i]) {
                    dp[i][j] = (dp[i][j] + dp[i - 1][j - nums[i]]) % MOD;
                }
            }
        }

        return dp[n - 1][sum];
    }
}
