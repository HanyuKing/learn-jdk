package algo.matrix;

import algo.hot200.Base;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Hanyu.Wang
 * @Date 2024/10/14 16:29
 * @Description
 * @Version 1.0
 **/
public class Answer extends Base {

    @Test
    public void testP48() {
        int[][] matrix = new int[][]{
                {5, 1, 9, 11},
                {2, 4, 8, 10},
                {13, 3, 6, 7},
                {15, 14, 12, 16}
        };

        // [[15,13,2,5],
        // [14,3,4,1],
        // [12,6,8,9],
        // [16,7,10,11]]

        rotate(matrix);

        print(matrix);
    }

    public void rotate(int[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }
        for (int i = 0; i < n; i++) {
            reverse(matrix[i], 0, n - 1);
        }
    }

    private void reverse(int[] nums, int start, int end) {
        while (start < end) {
            int temp = nums[start];
            nums[start] = nums[end];
            nums[end] = temp;
            start++;
            end--;
        }
    }

    @Test
    public void testP54() {
        int[][] matrix = new int[][] {
                {1,2,3},
                {4,5,6},
                {7,8,9}}; // [1,2,3,6,9,8,7,4,5]

        print(spiralOrder(matrix));

        matrix = new int[][] {
                {1,2,3,4},
                {5,6,7,8},
                {9,10,11,12} // 1,2,3,4,8,12,11,10,9,5,6,7
        };
        print(spiralOrder(matrix));
    }

    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> result = new ArrayList<>();
        int l = 0;
        int r = matrix[0].length - 1;
        int u = 0;
        int d = matrix.length - 1;

        while (l <= r && u <= d) {
            int i = l;
            while (i <= r && u <= d) {
                result.add(matrix[u][i++]);
            }
            i = ++u;
            while (i <= d && l <= r) {
                result.add(matrix[i++][r]);
            }
            i = --r;
            while (i >= l && u <= d) {
                result.add(matrix[d][i--]);
            }
            i = --d;
            while (i >= u && l <= r) {
                result.add(matrix[i--][l]);
            }
            l++;
        }

        return result;
    }

    @Test
    public void testP73() {
        int[][] matrix = new int[][] {
                {0,1,2,0},
                {3,4,5,2},
                {1,3,1,5}
        };
        setZeroes(matrix);
        print(matrix);
    }

    public void setZeroes(int[][] matrix) {
        boolean firstRowHasZero = false;
        boolean firstColHasZero = false;
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i][0] == 0) {
                firstColHasZero = true;
                break;
            }
        }
        for (int i = 0; i < matrix[0].length; i++) {
            if (matrix[0][i] == 0) {
                firstRowHasZero = true;
                break;
            }
        }
        for (int i = 1; i < matrix.length; i++) {
            for (int j = 1; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][0] = 0;
                    matrix[0][j] = 0;
                }
            }
        }
        for (int i = 1; i < matrix.length; i++) {
            if (matrix[i][0] == 0) {
                for (int j = 1; j < matrix[0].length; j++) {
                    matrix[i][j] = 0;
                }
            }
        }
        for (int i = 1; i < matrix[0].length; i++) {
            if (matrix[0][i] == 0) {
                for (int j = 1; j < matrix.length; j++) {
                    matrix[j][i] = 0;
                }
            }
        }
        if (firstRowHasZero) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[0][j] = 0;
            }
        }
        if (firstColHasZero) {
            for (int j = 0; j < matrix.length; j++) {
                matrix[j][0] = 0;
            }
        }
    }
}
