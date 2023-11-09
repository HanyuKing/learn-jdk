package leetcode;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author Hanyu.Wang
 * @Date 2023/11/8 10:55
 * @Description
 * @Version 1.0
 **/
public class P2924 {
    public static void main(String[] args) {
        int n = 3;
        int[][] edges = new int[][]{
                {0, 2},
                {0, 1},
        };
        int result = new P2924().findChampion(n, edges);
        System.out.println(result);
    }

    public int findChampion(int n, int[][] edges) {
        int[] inorder = new int[n];
        for (int i = 0; i < edges.length; i++) {
            inorder[edges[i][1]]++;
        }
        int ans = -1;
        int cnt = 0;
        for (int i = 0; i < n; i++) {
            if (inorder[i] == 0) {
                ans = i;
                cnt++;
            }
        }
        return cnt == 1 ? ans : -1;
    }
}
