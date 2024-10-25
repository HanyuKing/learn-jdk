package algo.greedy;

import algo.hot200.Base;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author Hanyu.Wang
 * @Date 2024/10/24 10:09
 * @Description
 * @Version 1.0
 **/
public class Answer extends Base {

    @Test
    public void testP763() {
        String s = "ababcbacadefegdehijhklij"; // [9,7,8]
        print(partitionLabels(s));

        s = "eccbbbbdec"; // [10]
        print(partitionLabels(s));
    }

    public List<Integer> partitionLabels(String s) {
        int[] last = new int[26];
        for (int i = 0; i < s.length(); i++) {
            last[s.charAt(i) - 'a'] = i;
        }

        int maxIndex = 0;
        int startIndex = 0;
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < s.length(); i++) {
            maxIndex = Math.max(maxIndex, last[s.charAt(i) - 'a']);
            if (maxIndex == i) {
                result.add(i - startIndex + 1);
                startIndex = i + 1;
            }
        }

        return result;
    }

    @Test
    public void testP45() {
        int[] nums = new int[] {2,3,1,1,4}; // 2
        print(jump(nums));

        nums = new int[] {2,3,0,1,4}; // 2
        print(jump2(nums));
    }

    public int jump2(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }

        int step = 0;
        int maxIndex = 0;
        int end = 0;

        for (int i = 0; i < nums.length; i++) {
            maxIndex = Math.max(maxIndex, nums[i] + i);
            if (maxIndex >= nums.length - 1) {
                break;
            }

            if (i == end) {
                end = maxIndex;
                step++;
            }

        }

        return step + 1;
    }

    public int jump(int[] nums) {
        int[] maxJump = new int[nums.length];
        Arrays.fill(maxJump, Integer.MAX_VALUE);
        maxJump[0] = 0;

        for (int i = 0; i < nums.length; i++) {
            int currMaxJumpIndex = nums[i] + i;
            for (int j = i + 1; j < nums.length && j <= currMaxJumpIndex; j++) {
                if (maxJump[j] == Integer.MAX_VALUE) {
                    maxJump[j] = maxJump[i] + 1;
                } else {
                    maxJump[j] = Math.min(maxJump[j], maxJump[i] + 1);
                }
            }
        }

        return maxJump[nums.length - 1];
    }

    @Test
    public void testP55() {
        int[] nums = new int[] {2,3,1,1,4}; // true
        print(canJump(nums));

        nums = new int[] {3,2,1,0,4}; // false
        print(canJump(nums));

        nums = new int[] {1,2,3}; // false
        print(canJump(nums));
    }

    public boolean canJump(int[] nums) {
        int maxIndex = nums[0];
        for (int i = 1; i <= maxIndex && i < nums.length; i++) {
            maxIndex = Math.max(maxIndex, nums[i] + i);
        }
        return maxIndex >= nums.length - 1;
    }

    @Test
    public void testP121() {
        // maxProfit
    }

    public int maxProfit(int[] prices) {
        int ans = 0;
        int rightMax = prices[prices.length - 1];
        for (int i = prices.length - 2; i >= 0; i--) {
            ans = Math.max(ans, rightMax - prices[i]);
            rightMax = Math.max(rightMax, prices[i]);
        }
        return ans;
    }
}
