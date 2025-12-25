package interview.veeva;

import org.junit.Test;

public class SetZero {
    /**
     * 给出一个二维数组，把含有 0 的行、列的元素全部置零，要求原地修改。
     *
     *
     *
     * 例如：
     *
     * 1 0 9 8 7
     *
     * 0 2 3 5 6
     *
     * 5 6 7 1 9
     *
     *
     *
     * Output
     *
     * 0 0 0 0 0
     *
     * 0 0 0 0 0
     *
     * 0 0 7 1 9
     */

    public void setMatrixZero(int[][] matrix) {
        if (matrix == null || matrix.length == 0) {
            return;
        }

        boolean firstRowHasZero = false;
        boolean firstColHasZero = false;
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i][0] == 0) {
                firstColHasZero = true;
                break;
            }
        }
        for (int j = 0; j < matrix[0].length; j++) {
            if (matrix[0][j] == 0) {
                firstRowHasZero = true;
                break;
            }
        }

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][0] = 0;
                    matrix[0][j] = 0;
                }
            }
        }

        for (int i = 1; i < matrix.length; i++) {
            for (int j = 1; j < matrix[0].length; j++) {
                if (matrix[i][0] == 0 || matrix[0][j] == 0) {
                    matrix[i][j] = 0;
                }
            }
        }

        //
        if (firstRowHasZero) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[0][j] = 0;
            }
        }
        if (firstColHasZero) {
            for (int i = 0; i < matrix.length; i++) {
                matrix[i][0] = 0;
            }
        }

    }

    @Test
    public void test1() {
        int[][] matrix = new int[][] {
                {1, 0, 9, 8, 7},
                {0, 2, 3, 5, 6},
                {5, 6, 7, 1, 9}
        };

        setMatrixZero(matrix);

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }


}
