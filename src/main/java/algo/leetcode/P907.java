package algo.leetcode;

import java.util.Arrays;

/**
 * @Author Hanyu.Wang
 * @Date 2023/11/27 09:59
 * @Description
 * @Version 1.0
 **/
public class P907 {

    public static void main(String[] args) {
//        int[] arr = new int[]{3, 1, 2, 4}; // 17
        int[] arr = new int[]{11, 81, 94, 43, 3}; // 444

        int result = new P907().sumSubarrayMins2(arr);

        System.out.println(result);
    }

    public int sumSubarrayMins2(int[] arr) {
        int MOD = 1000000007;

        int sum = 0;

        int[][] mem = new int[arr.length][arr.length];
        for (int i = 0; i < arr.length; i++) {
            Arrays.fill(mem[i], Integer.MAX_VALUE);
        }

        for (int i = 0; i < arr.length; i++) {
            int min = arr[i];
            for (int j = i; j < arr.length; j++) {
                min = Math.min(min, arr[j]);
                mem[i][j] = min;
            }
        }

        for (int i = 1; i <= arr.length; i++) {
            for (int j = 0; j < arr.length - i + 1; j++) {
                sum = (sum + mem[j][j + i - 1]) % MOD;
            }
        }

        return sum;
    }

    /**
     * 暴力超时
     *
     * @param arr
     * @return
     */
    public int sumSubarrayMins(int[] arr) {
        int MOD = 1000000007;

        int sum = 0;

        for (int i = 1; i <= arr.length; i++) {
            for (int j = 0; j < arr.length - i + 1; j++) {
                int min = arr[j];
                for (int k = j; k < i + j; k++) {
                    min = Math.min(min, arr[k]);
                }

                sum = (sum + min) % MOD;
            }
        }

        return sum;
    }
}
