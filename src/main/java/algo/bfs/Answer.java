package algo.bfs;

import algo.hot200.Base;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @Author Hanyu.Wang
 * @Date 2024/10/16 17:09
 * @Description
 * @Version 1.0
 **/
public class Answer extends Base {
    @Test
    public void test994() {
        int[][] grid = new int[][] {
                {2,1,1},
                {1,1,0},
                {0,1,1}
        };
        print(orangesRotting(grid));

        grid = new int[][] {
                {2,1,1},
                {0,1,1},
                {1,0,1}
        };
        print(orangesRotting(grid));
    }

    public int orangesRotting(int[][] grid) {
        int count = 0;

        Queue<int[]> queue = new LinkedList<>();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 2) {
                    queue.add(new int[]{i, j});
                }
            }
        }

        while (!queue.isEmpty()) {
            count++;

            int size = queue.size();
            while (--size >= 0) {
                int[] indexes = queue.poll();
                addNewIndexes(queue, grid, indexes[0], indexes[1]);
            }
        }

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1) {
                    return -1;
                }
            }
        }

        return count - 1 < 0 ? 0 : count - 1;
    }

    public int orangesRotting2(int[][] grid) {
        int times = 0;
        int refreshTotal = 0;

        Queue<int[]> queue = new LinkedList<>();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 2) {
                    queue.add(new int[]{i, j});
                }
                if (grid[i][j] == 1) {
                    refreshTotal++;
                }
            }
        }

        while (!queue.isEmpty()) {
            times++;

            int size = queue.size();
            while (--size >= 0) {
                int[] indexes = queue.poll();
                int refresh = addNewIndexes(queue, grid, indexes[0], indexes[1]);
                refreshTotal -= refresh;
            }
        }

        if (refreshTotal != 0) {
            return -1;
        }

        return times - 1 < 0 ? 0 : times - 1;
    }

    private int addNewIndexes(Queue<int[]> queue, int[][] grid, int row, int col) {
        int left = col - 1;
        int up = row - 1;
        int right = col + 1;
        int down = row + 1;

        int refreshTotal = 0;

        if (left >= 0 && grid[row][left] == 1) {
            grid[row][left] = 2;
            queue.add(new int[] {row, left});
            refreshTotal++;
        }
        if (right < grid[0].length && grid[row][right] == 1) {
            grid[row][right] = 2;
            queue.add(new int[] {row, right});
            refreshTotal++;
        }
        if (up >= 0 && grid[up][col] == 1) {
            grid[up][col] = 2;
            queue.add(new int[] {up, col});
            refreshTotal++;
        }
        if (down < grid.length && grid[down][col] == 1) {
            grid[down][col] = 2;
            queue.add(new int[] {down, col});
            refreshTotal++;
        }
        return refreshTotal;
    }

}
