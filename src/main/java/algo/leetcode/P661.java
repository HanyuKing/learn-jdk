package algo.leetcode;

import algo.hot200.Base;

/**
 * @Author Hanyu.Wang
 * @Date 2024/11/18 10:46
 * @Description
 * @Version 1.0
 **/
public class P661 extends Base {
    public static void main(String[] args) {
        P661 p = new P661();

        p.print(p.imageSmoother(new int[][] {
                {100,200,100},
                {200,50,200},
                {100,200,100}}));
    }

    public int[][] imageSmoother(int[][] img) {
        int m = img.length;
        int n = img[0].length;
        int[][] result = new int[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int sum = 0;
                int cnt = 0;
                if (j - 1 >= 0) {
                    sum += img[i][j - 1];
                    cnt++;
                }
                if (j - 1 >= 0 && i - 1 >= 0) {
                    sum += img[i - 1][j - 1];
                    cnt++;
                }
                if (i - 1 >= 0) {
                    sum += img[i - 1][j];
                    cnt++;
                }
                if (i - 1 >= 0 && j + 1 < n) {
                    sum += img[i - 1][j + 1];
                    cnt++;
                }
                if (j + 1 < n) {
                    sum += img[i][j + 1];
                    cnt++;
                }
                if (i + 1 < m && j + 1 < n) {
                    sum += img[i + 1][j + 1];
                    cnt++;
                }
                if (i + 1 < m) {
                    sum += img[i + 1][j];
                    cnt++;
                }
                if (i + 1 < m && j - 1 >= 0) {
                    sum += img[i + 1][j - 1];
                    cnt++;
                }
                cnt++;
                result[i][j] = (sum + img[i][j]) / cnt;
            }
        }
        return result;
    }
}
