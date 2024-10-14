package algo.leetcode;

import java.util.Arrays;

/**
 * @Author Hanyu.Wang
 * @Date 2023/11/15 14:38
 * @Description
 * @Version 1.0
 **/
public class P2656 {

    public int maximizeSum(int[] nums, int k) {
        int max = Arrays.stream(nums).max().getAsInt();
        return (k * max) + (k - 1) * k / 2;
    }
}
