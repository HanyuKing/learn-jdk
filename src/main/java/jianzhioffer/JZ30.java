package jianzhioffer;

/**
 * JZ30
 *
 * 描述
 * 输入一个整型数组，数组里有正数也有负数。数组中的一个或连续多个整数组成一个子数组。求所有子数组的和的最大值。要求时间复杂度为 O(n).
 * 示例1
 * 输入：
 * [1,-2,3,10,-4,7,2,-5]
 *
 * 返回值：
 * 18
 * 说明：
 * 输入的数组为{1,-2,3,10,—4,7,2,一5}，和最大的子数组为{3,10,一4,7,2}，因此输出为该子数组的和 18。
 */
public class JZ30 {
    public static void main(String[] args) {
        System.out.println(new JZ30().FindGreatestSumOfSubArray(new int[]{2,8,1,5,9}));
    }

    /**
     * O(n)
     *
     * dp[i] 表示包含当前值的最大值
     */
    public int FindGreatestSumOfSubArray(int[] array) {
        if (array.length == 0) return Integer.MIN_VALUE;
        int max = Integer.MIN_VALUE;
        int[] dp = new int[array.length];
        dp[0] = Math.max(max, array[0]);

        for (int i = 1; i < array.length; i++) {
            dp[i] = Math.max(array[i], dp[i - 1] + array[i]);
            if (dp[i] > max) {
                max = dp[i];
            }
        }

        return max;
    }

    /**
     * O(n^2)
     *
     * @param array
     * @return
     */
    public int FindGreatestSumOfSubArray2(int[] array) {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < array.length; i++) {
            int sum = array[i];
            if (sum > max) {
                max = sum;
            }
            for (int j = i + 1; j < array.length; j++) {
                sum += array[j];
                if (sum > max) {
                    max = sum;
                }
            }
        }
        return max;
    }
}
