package algo.twopointer;

import algo.hot200.Base;
import org.junit.Test;

/**
 * @Author Hanyu.Wang
 * @Date 2024/10/14 09:53
 * @Description
 * @Version 1.0
 **/
public class Answer extends Base {
    @Test
    public void testP42() {
        int[] height = new int[] {0,1,0,2,1,0,1,3,2,1,2,1}; // 6
        print(trap(height));

        height = new int[] {4,2,0,3,2,5}; // 9
        print(trap(height));
    }

    public int trap(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int leftMax = 0;
        int rightMax = 0;
        int ans = 0;

        while (left < right) {
            leftMax = Math.max(leftMax, height[left]);
            rightMax = Math.max(rightMax, height[right]);
            if (height[left] < height[right]) {
                ans += leftMax - height[left];
                left++;
            } else {
                ans += rightMax - height[right];
                right--;
            }
        }

        return ans;
    }

    public int trap2(int[] height) {
        int len = height.length;
        int[] leftMax = new int[len];
        int[] rightMax = new int[len];

        leftMax[0] = 0;
        rightMax[len - 1] = 0;
        int max = height[0];
        for (int i = 1; i < len; i++) {
            leftMax[i] = max;
            max = Math.max(max, height[i]);
        }

        rightMax[len - 1] = 0;
        max = height[len - 1];
        for (int i = len - 2; i >= 0; i--) {
            rightMax[i] = max;
            max = Math.max(max, height[i]);
        }

        int ans = 0;
        for (int i = 1; i < len; i++) {
            ans += Math.max(0, Math.min(leftMax[i], rightMax[i]) - height[i]);
        }

        return ans;
    }
}
