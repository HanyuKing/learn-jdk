package leetcode;

import java.util.Arrays;

/**
 * @Author Hanyu.Wang
 * @Date 2023/10/30 16:51
 * @Description
 * @Version 1.0
 **/
public class P2918 {
    public static void main(String[] args) {
//        int [] num1 = new int[]{3, 2, 0, 1, 0};
//        int [] num2 = new int[]{6, 5, 0};

//        int [] num1 = new int[]{2, 0, 2, 0};
//        int [] num2 = new int[]{1, 4};

        int[] num1 = new int[]{0,16,28,12,10,15,25,24,6,0,0};
        int[] num2 = new int[]{20,15,19,5,6,29,25,8,12};

        System.out.println(new P2918().minSum(num1, num2));
    }

    public long minSum(int[] nums1, int[] nums2) {
        long sumZero1 = sumZero(nums1);
        long sumZero2 = sumZero(nums2);

        long sum1 = sum(nums1);
        long sum2 = sum(nums2);

        long minSum1 = sumZero1 + sum1;
        long minSum2 = sumZero2 + sum2;

        if (sumZero1 > 0 && sumZero2 > 0) {
            return Math.max(minSum1, minSum2);
        } else if (sumZero1 > 0 && sumZero2 == 0) {
            if (minSum1 > sum2) {
                return -1;
            } else {
                return sum2;
            }
        } else if (sumZero1 == 0 && sumZero2 > 0) {
            if (sum1 < minSum2) {
                return -1;
            } else {
                return sum1;
            }
        } else {
            if (sum1 == sum2) {
                return sum1;
            }
        }

        return -1;
    }

    private long sum(int[] nums) {
        long s = 0;
        for (int n : nums) {
            s += n;
        }
        return s;
    }

    private long sumZero(int[] nums) {
        return Arrays.stream(nums).filter(n -> n == 0).count();
    }
}
