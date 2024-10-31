package algo.dfs;

import algo.hot200.Base;
import org.junit.Test;

import java.util.*;

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
//         print(canFinish(numCourses, prerequisites));
//
//        prerequisites = new int[][]{{1, 0}, {0, 1}}; // false
//        print(canFinish(numCourses, prerequisites));
        prerequisites = new int[][] {{1,4},{2,4},{3,1},{3,2}};
        print(canFinish(5, prerequisites)); // true
    }

    public boolean canFinish(int numCourses, int[][] prerequisites) {
        if (prerequisites.length == 0 || prerequisites.length == 1) {
            return true;
        }
        int finishedCourseTotal = 0;

        Map<Integer, List<Integer>> map = new HashMap<>();

        Queue<Integer> finishedQueue = new LinkedList<>();
        for (int i = 0; i < prerequisites.length; i++) {
            if (prerequisites[i][1] == 0) {
                finishedQueue.offer(prerequisites[i][0]);
            }
            List<Integer> list = map.getOrDefault(prerequisites[i][1], new ArrayList<>());
            list.add(prerequisites[i][0]);
            map.put(prerequisites[i][1], list);
        }
        if (!finishedQueue.isEmpty()) {
            finishedCourseTotal++;
        }
        while (!finishedQueue.isEmpty()) {
            int currFinishedCourseSize = finishedQueue.size();
            finishedCourseTotal += currFinishedCourseSize;

            if (finishedCourseTotal > numCourses) {
                return false;
            }

            while (currFinishedCourseSize-- > 0) {
                Integer currFinishedCourse = finishedQueue.poll();
                List<Integer> nextFinishedCourse = map.get(currFinishedCourse);
                if (nextFinishedCourse != null && !nextFinishedCourse.isEmpty()) {
                    finishedQueue.addAll(nextFinishedCourse);
                }
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
