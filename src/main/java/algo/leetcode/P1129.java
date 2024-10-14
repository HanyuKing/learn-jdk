package algo.leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2023/2/17 16:57
 * @Description
 * @Version 1.0
 **/
public class P1129 {
    public static void main(String[] args) {
        int[][] grid = new int[][]{
                {0,1,1,1,1,0},
                {1,1,0,1,1,0},
                {1,1,0,1,0,1},
                {1,1,0,1,1,1},
                {1,1,0,1,1,1},
                {1,1,1,1,1,1},
                {1,0,1,1,1,1},
                {0,0,1,1,1,1},
                {1,1,1,1,1,1}
        };
        System.out.println(new Solution1129().largest1BorderedSquare(grid));
    }

    //给你一个由若干 0 和 1 组成的二维网格 grid，请你找出边界全部由 1 组成的最大 正方形 子网格，并返回该子网格中的元素数量。如果不存在，则返回 0
//。
//
//
//
// 示例 1：
//
// 输入：grid = [
// [1,1,1],
// [1,0,1],
// [1,1,1]
// ]
//输出：9
//
//
// 示例 2：
//
// 输入：grid = [[1,1,0,0]]
//输出：1
//
//
//
//
// 提示：
//
//
// 1 <= grid.length <= 100
// 1 <= grid[0].length <= 100
// grid[i][j] 为 0 或 1
//
//
// Related Topics 数组 动态规划 矩阵 👍 181 👎 0
    static class Solution1129 {
        public int largest1BorderedSquare(int[][] grid) {
            int result = 0;

            Log[][] log = new Log[grid.length][grid[0].length];
            for (int i = 0; i < grid[0].length; i++) {
                if (grid[0][i] == 0) {
                    log[0][i] = new Log(0, 0);
                } else {
                    if (i == 0) {
                        int v = grid[0][0] == 1 ? 1 : 0;
                        log[0][0] = new Log(v, v);
                        result = v;
                    } else {
                        log[0][i] = new Log(log[0][i - 1].left + 1, 1);
                        result = 1;
                    }
                }
            }
            for (int i = 1; i < grid.length; i++) {
                if (grid[i][0] == 0) {
                    log[i][0] = new Log(0, 0);
                } else {
                    result = 1;
                    log[i][0] = new Log(1, log[i - 1][0].up + 1);
                }
            }

            if (grid.length == 1 || grid[0].length == 1) {
                return result;
            }


        /*
            [0,1,1,1,1,0],
            [1,1,0,1,1,0],
            [1,1,0,1,0,1],
            [1,1,0,1,1,1],
            [1,1,0,1,1,1],
            [1,1,1,1,1,1],
            [1,0,1,1,1,1],
            [0,0,1,1,1,1],
            [1,1,1,1,1,1]
         */

            for (int i = 1; i < grid.length; i++) {
                System.out.println();
                for (int j = 1; j < grid[0].length; j++) {
                    if (grid[i][j] == 1) {
                        int maxWidth = Math.min(log[i - 1][j].up, log[i][j - 1].left) + 1;
                        result = Math.max(result, getMax(log, i, j, maxWidth));
                        log[i][j] = new Log(log[i][j - 1].left + 1, log[i - 1][j].up + 1);
                    } else {
                        log[i][j] = new Log(0, 0);
                    }
                }
            }

            return result * result;
        }

        private int getMax(Log[][] log, int row, int col, int maxWidth) {
            if (maxWidth == 1) {
                return 1;
            }
            int maxIndex = maxWidth - 1;
            while (maxIndex > 0) {
                if (log[row][col - maxIndex].up >= maxWidth && log[row - maxIndex][col].left >= maxWidth) {
                    return maxIndex + 1;
                }
                maxIndex--;
                maxWidth--;
            }
            return 1;
        }
        class Log {
            public int left;
            public int up;
            public Log(int left, int up) {
                this.left = left;
                this.up = up;
            }
            public Log() {}
        }
    }
}
