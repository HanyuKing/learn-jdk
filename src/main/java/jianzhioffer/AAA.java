package jianzhioffer;

import java.util.HashMap;

public class AAA {
    public static void main(String[] args) {
        System.out.println(new AAA().majorityElement(new int[]{2,2,1,1,1,2,2}));
    }
    public int majorityElement(int[] nums) {
        if (nums == null || nums.length == 0 ) return -1;

        int count = 1;
        int majority = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == majority) {
                count++;
            } else {
                count--;
            }
            if (count == 0) {
                majority = nums[i];
                count = 1;
            }
        }
        return majority;
    }
}
