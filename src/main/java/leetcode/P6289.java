package leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2023/1/7 22:58
 * @Description
 * @Version 1.0
 **/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 输入：nums = [1,4]
 * 输出：5
 * 解释：
 * 三元组和它们对应的有效值如下：
 * - (0,0,0) 有效值为 ((1 | 1) & 1) = 1
 * - (0,0,1) 有效值为 ((1 | 1) & 4) = 0
 * - (0,1,0) 有效值为 ((1 | 4) & 1) = 1
 * - (0,1,1) 有效值为 ((1 | 4) & 4) = 4
 * - (1,0,0) 有效值为 ((4 | 1) & 1) = 1
 * - (1,0,1) 有效值为 ((4 | 1) & 4) = 4
 * - (1,1,0) 有效值为 ((4 | 4) & 1) = 0
 * - (1,1,1) 有效值为 ((4 | 4) & 4) = 4
 * 数组的 xor 美丽值为所有有效值的按位异或 1 ^ 0 ^ 1 ^ 4 ^ 1 ^ 4 ^ 0 ^ 4 = 5 。
 */
public class P6289 {
    public static void main(String[] args) {
        System.out.println(new Solution6289().xorBeauty3(new int[]{15,45,20,2,34,35,5,44,32,30}));
    }

    static class Solution6289 {

        public int xorBeauty3(int[] nums) {
            int len = nums.length;
            List<Integer> or = new ArrayList<>(nums.length * 2);
            for (int i = 0; i < len; i++) {
                for (int j = 0; j < len; j++) {
                    if (i == j) {
                        or.add(nums[i]);
                    } else {
                        or.add(nums[i] | nums[j]);
                    }
                }
            }

            int result = nums[0];
            for (int ij = 1; ij < or.size(); ij++) {
                for (int k = 1; k < nums.length; k++) {
                    result ^= or.get(ij) & nums[k];
                }
            }
            return result;
        }
        public int xorBeauty2(int[] nums) {
            int len = nums.length;
            int result = -1;
            int[][] a = new int[len][len];
            for (int i = 0; i < len; i++) {
                for (int j = 0; j < len; j++) {
                    if (i == j) {
                        a[i][j] = nums[i];
                    } else {
                        a[i][j] = nums[i] | nums[j];
                    }
                }
            }
            for (int i = 0; i < len; i++) {
                for (int j = 0; j < len; j++) {
                    for (int k = 0; k < len; k++) {
                        if (i == j && i == k) {
                            if (result == -1) {
                                result = nums[0];
                                continue;
                            }
                        }
                        result = result ^ (a[i][j] & nums[k]);
                    }
                }
            }
            return result;
        }
    }
}
