package algo.dfs;

import algo.hot200.Base;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * @Author Hanyu.Wang
 * @Date 2024/10/16 16:29
 * @Description
 * @Version 1.0
 **/
public class Answer extends Base {

    @Test
    public void testP207() {
        // TODO
        int numCourses = 2;
        int[][] prerequisites = new int[][]{{1, 0}}; // true
        print(canFinish(numCourses, prerequisites));

        prerequisites = new int[][]{{1, 0}, {0, 1}}; // false
        print(canFinish(numCourses, prerequisites));
    }

    public boolean canFinish(int numCourses, int[][] prerequisites) {
        int finishedCourseTotal = 0;

        Map<Integer, Integer> map = new HashMap<>();

        Queue<Integer> finishedQueue = new LinkedList<>();
        for (int i = 0; i < prerequisites.length; i++) {
            if (prerequisites[i][1] == 0) {
                finishedQueue.offer(prerequisites[i][0]);
            }
            map.put(prerequisites[i][1], prerequisites[i][0]);
        }
        if (!finishedQueue.isEmpty()) {
            finishedCourseTotal++;
        }
        while (!finishedQueue.isEmpty()) {
            int currFinishedCourseSize = finishedQueue.size();
            finishedCourseTotal += currFinishedCourseSize;

            while (currFinishedCourseSize-- > 0) {
                Integer currFinishedCourse = finishedQueue.poll();

            }
        }

        return finishedCourseTotal == numCourses;
    }

    @Test
    public void testP200() {
        char[][] grid = {
                {'1','1','1','1','0'},
                {'1','1','0','1','0'},
                {'1','1','0','0','0'},
                {'0','0','0','0','0'}
        };
        print(numIslands(grid));
    }

    public int numIslands(char[][] grid) {
        int count = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '1') {
                    dfs(grid, i, j);
                    count++;
                }
            }
        }
        return count;
    }

    private void dfs(char[][] grid, int i, int j) {
        if (i < 0 || i == grid.length || j < 0 || j == grid[0].length) {
            return;
        }
        if (grid[i][j] == '0') {
            return;
        }

        grid[i][j] = '0';

        dfs(grid, i - 1, j);
        dfs(grid, i + 1, j);
        dfs(grid, i, j - 1);
        dfs(grid, i, j + 1);
    }
}
