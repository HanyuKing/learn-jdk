package algo.leetcode;

import java.util.Arrays;

/**
 * @Author Hanyu.Wang
 * @Date 2024/5/31 16:48
 * @Description
 * @Version 1.0
 **/
public class P2965 {
    public static void main(String[] args) {
        int[][] grid = {
                {9,1,7},
                {8,9,2},
                {3,4,6}
        };
        int[] ans = new P2965().findMissingAndRepeatedValues(grid);
        Arrays.stream(ans).forEach(System.out::println);
    }

    public int[] findMissingAndRepeatedValues(int[][] grid) {
        int n = grid.length;
        boolean[] flags = new boolean[n * n + 1];
        int[] ans = new int[2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (flags[grid[i][j]]) {
                    ans[0] = grid[i][j];
                }
                flags[grid[i][j]] = true;
            }
        }
        for (int i = 1; i < flags.length; i++) {
            if (!flags[i]) {
                ans[1] = i;
            }
        }
        return ans;
    }
}
