package algo.leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2024/11/15 16:36
 * @Description
 * @Version 1.0
 **/
public class P3239 {
    public static void main(String[] args) {
        System.out.println(new P3239().minFlips(new int[][] {
                {1,0,0},
                {0,0,0},
                {0,0,1}
        })); // 2

        System.out.println(new P3239().minFlips(new int[][] {
                {0,1},
                {0,1},
                {0,0}
        })); // 1

        System.out.println(new P3239().minFlips(new int[][] {
                {1},
                {0}
        })); // 0
    }

    public int minFlips(int[][] grid) {
        int columnMin = 0;
        int rowMin = 0;

        int m = grid.length;
        int n = grid[0].length;

        for (int i = 0; i < m; i++) {
            int start = 0, end = n - 1;
            int currMin = 0;
            while (start < end) {
                if (grid[i][start++] != grid[i][end--]) {
                    currMin++;
                }
            }
            rowMin += currMin;
        }

        for (int i = 0; i < n; i++) {
            int start = 0, end = m - 1;
            int currMin = 0;
            while (start < end) {
                if (grid[start++][i] != grid[end--][i]) {
                    currMin++;
                }
            }
            columnMin += currMin;
        }

        return Math.min(rowMin, columnMin);
    }
}
