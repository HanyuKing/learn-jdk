package algo.leetcode;

import algo.hot200.Base;
import org.junit.Test;

import java.util.Arrays;

/**
 * @Author Hanyu.Wang
 * @Date 2025/2/12 14:48
 * @Description
 * @Version 1.0
 **/
public class P1760 extends Base {
    @Test
    public void test1() {
        print(minimumSize(new int[] {9}, 2));
    }

    @Test
    public void test2() {
        print(minimumSize(new int[] {2,4,8,2}, 4));
    }

    @Test
    public void test3() {
        print(minimumSize(new int[] {7,17}, 2));
    }

    public int minimumSize(int[] nums, int maxOperations) {
        int left = 1;
        int right = Arrays.stream(nums).max().getAsInt();
        int ans = left;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (minimumSizeCheck(nums, mid, maxOperations)) {
                ans = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return ans;
    }

    private boolean minimumSizeCheck(int[] nums, int target, int maxOperations) {
        int ops = 0;
        for (int num : nums) {
            if (num > target) {
                ops += (num - 1) / target;
                if (ops > maxOperations) {
                    return false;
                }
            }
        }
        return true;
    }
}
