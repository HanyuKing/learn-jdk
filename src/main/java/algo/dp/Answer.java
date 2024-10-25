package algo.dp;

import algo.hot200.Base;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author Hanyu.Wang
 * @Date 2024/10/24 15:13
 * @Description
 * @Version 1.0
 **/
public class Answer extends Base {

    @Test
    public void testP279() {
        print(numSquares2(12)); // 12 = 4 + 4 + 4

        print(numSquares2(13)); // 13 = 4 + 9
    }

    public int numSquares3(int n) {
        int[] dp = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            int min = Integer.MAX_VALUE;
            for (int j = 1; j * j <= i; j++) {
                min = Math.min(min, dp[i - j * j] + 1);
            }
            dp[i] = min;
        }
        return dp[n];
    }

    public int numSquares2(int n) {
        int[] dp = new int[n + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        int sqrtN = (int) Math.sqrt(n);
        for (int i = 1; i <= sqrtN; i++) {
            dp[i * i] = 1;
        }
        for (int i = 1; i <= n; i++) {
            int min = dp[i];
            for (int j = 1; j * j < i; j++) {
                min = Math.min(min, dp[i - j * j] + 1);
            }
            dp[i] = min;
        }
        return dp[n];
    }

    public int numSquares(int n) {
        int[] dp = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            int min = Integer.MAX_VALUE;
            for (int j = 1; j * j <= i; j++) {
                min = Math.min(min, dp[i - j * j]);
            }
            dp[i] = min + 1;
        }
        return dp[n];
    }

    @Test
    public void testP198() {
        int[] nums = new int[] {1,2,3,1};
//        print(rob(nums)); // 4
//
//        nums = new int[] {2,7,9,3,1};
//        print(rob(nums)); // 12

        nums = new int[] {2,1,1,2};
        print(rob2(nums)); // 4
    }

    public int rob2(int[] nums) {
        int prePre = 0;
        int pre = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (i == 1) {
                prePre = pre;
                pre = Math.max(nums[i], nums[i - 1]);
            } else {
                int curr = nums[i] + prePre;
                curr = Math.max(curr, pre);
                prePre = pre;
                pre = curr;
            }
        }
        return pre;
    }

    public int rob(int[] nums) {
        int[] dp = new int[nums.length];
        dp[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (i == 1) {
                dp[i] = Math.max(nums[i], nums[i - 1]);
            } else {
                dp[i] = nums[i] + dp[i - 2];
                dp[i] = Math.max(dp[i], dp[i - 1]);
            }
        }
        return dp[nums.length - 1];
    }

    @Test
    public void testP118() {
        print(generate(5));
    }

    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> first = new ArrayList<>();
        first.add(1);
        result.add(first);

        for (int i = 1; i < numRows; i++) {
            List<Integer> newRow = new ArrayList<>();
            newRow.add(1);
            for (int j = 1; j < i; j++) {
                List<Integer> preRow = result.get(i - 1);
                newRow.add(preRow.get(j) + preRow.get(j - 1));
            }
            newRow.add(1);
            result.add(newRow);
        }

        return result;
    }

    @Test
    public void testP70() {
        print(climbStairs(4));
    }

    public int climbStairs(int n) {
        if (n == 1) {
            return 1;
        }
        int a = 1;
        int b = 2;
        for (int i = 3; i <= n; i++) {
            int curr = a + b;
            a = b;
            b = curr;
        }
        return b;
    }
}
