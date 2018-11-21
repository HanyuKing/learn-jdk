package exception;

import org.junit.Test;

import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Solution {
    public static void main(String[] args) {
        System.out.println(threeSum(new int[] {-1, 0, 1, 2, -1, -4}));
    }
    public static List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);

        List<List<Integer>> result = new ArrayList<List<Integer>>();

        for(int i = 0; i < nums.length; i++) {
            int a = nums[i];
            for(int j = i + 1, k = nums.length - 1; j < k ;) {
                int b = nums[j];
                int c = nums[k];
                int value = a + b + c;

                if(value == 0) {
                    result.add(Arrays.asList(a, b, c));
                    while (j < k && b == nums[++j]);
                    while (j < k && c == nums[--k]);
                } else if (value > 0) {
                    k--;
                } else if (value < 0) {
                    j++;
                }
            }
        }

        return result;
    }
}