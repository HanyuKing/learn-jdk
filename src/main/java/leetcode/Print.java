package leetcode;

import java.util.ArrayList;

public class Print {

    public static void printArray(int[] array) {
        if (array == null) {
            throw new IllegalArgumentException("array can not be null");
        }

        System.out.print("[");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println("]");
    }

    public static void printTreeInOrder(TreeNode root) {
        if (root == null) {
            return;
        }
        if (root.left != null) {
            printTreeInOrder(root.left);
        }
        System.out.print(root.val + " ");
        if (root.right != null) {
            printTreeInOrder(root.right);
        }
    }

    public static void main(String[] args) {
        int[][] matrix = {
                {1,2,3,4},
                {5,6,7,8},
                {9,10,11,12}};
        System.out.println(printMatrix(matrix));
    }
    public static ArrayList<Integer> printMatrix(int [][] matrix) {
        ArrayList<Integer> list = new ArrayList();
        int row = matrix.length;
        int col = matrix[0].length;
        int left = 0;
        int top = 0;
        int right = col - 1;
        int down = row - 1;
        while (left <= right || top <= down) {
            for (int i = left; i <= right; i++) {
                list.add(Integer.valueOf(matrix[top][i]));
            }

            top++;
            if (top > down) break;

            for (int i = top; i <= down; i++) {
                list.add(Integer.valueOf(matrix[i][right]));
            }

            right--;
            if (right < left) break;

            for (int i = right; i >= left; i--) {
                list.add(Integer.valueOf(matrix[down][i]));

            }
            down--;
            if (top > down) break;

            for (int i = down; i >= top; i--) {
                list.add(Integer.valueOf(matrix[i][left]));
            }
            left++;
        }
        return list;
    }
}

