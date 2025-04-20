package interview;

import org.junit.Test;

/**
 * @Author Hanyu.Wang
 * @Date 2025/3/11 11:10
 * @Description
 * @Version 1.0
 **/
public class AI {
    /*
    给定一个三角形 triangle ，找出自顶向下的最小路径和。
每一步只能移动到下一行中相邻的结点上。相邻的结点 在这里指的是 下标 与 上一层结点下标 相同或者等于 上一层结点下标 + 1 的两个结点。也就是说，如果正位于当前行的下标 i ，那么下一步可以移动到下一行的下标 i 或 i + 1 。
 
示例 1：
输入：triangle = [[2],[3,4],[6,5,7],[4,1,8,3]]
输出：11
解释：如下面简图所示：
2
3 4
6 5 7
4 1 8 3
自顶向下的最小路径和为 11（即，2 + 3 + 5 + 1 = 11）。
示例 2：
输入：triangle = [[-10]]
输出：-10
     */
    /*
        f(0)(0) = 2;
        f(1)(0) = min(f(0)(-1), f(0)(0))

        f(n)(0) = f(0)(0) + f(n)(0)
     */
    public int findShortestPath(int[][] triangle) {
        if (triangle.length == 1) {
            return triangle[0][0];
        }
        for (int i = 1; i < triangle.length; i++) {
            triangle[i][0] += triangle[i - 1][0];
        }
        for (int i = 1; i < triangle.length; i++) {
            for (int j = 1; j < i; j++) {
                triangle[i][j] = triangle[i][j] + Math.min(triangle[i - 1][j - 1], triangle[i - 1][j]);
            }
            triangle[i][i] = triangle[i][i] + triangle[i - 1][i - 1];
        }
        int shortestPath = Integer.MAX_VALUE;
        for (int i = 0; i < triangle.length; i++) {
            shortestPath = Math.min(triangle[triangle.length - 1][i], shortestPath);
        }
        return shortestPath;
    }

    @Test
    public void test1() {
        int[][] triangle = new int[][]{{2},{3,4},{6,5,7},{4,1,8,3}};
        System.out.println(findShortestPath(triangle));
    }

    @Test
    public void test2() {
        int[][] triangle = new int[][]{{-10}};
        System.out.println(findShortestPath(triangle));
    }
}
