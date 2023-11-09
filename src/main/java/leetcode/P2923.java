package leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2023/11/8 10:44
 * @Description
 * @Version 1.0
 **/
public class P2923 {
    public static void main(String[] args) {

    }

    public int findChampion(int[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            int j = 0;
            for (; j < grid[0].length; j++) {
                if (i == j) {
                    continue;
                }
                if (grid[i][j] != 1) {
                    break;
                }
            }
            if (j == grid[0].length) {
                return i;
            }
        }
        return 0;
    }
}
