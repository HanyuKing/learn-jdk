package jianzhioffer;

public class JZ65 {
    public static void main(String[] args) {
        char[][] matrix = new char[][]{
                {'a','b','c','e'},
                {'s','f','c','s'},
                {'a','d','e','e'}
        };

        System.out.println(new JZ65().hasPath(matrix, "see"));
    }

    public boolean hasPath (char[][] matrix, String word) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                int[][] traveled = new int[matrix.length][matrix[0].length];
                if (judgeHashPath(traveled, matrix, i, j, word.toCharArray(), 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean judgeHashPath(int[][] traveled, char[][] matrix, int row, int col, char[] word, int wordIndex) {
        if (row < 0 || col < 0 || row >= matrix.length || col >= matrix[0].length) return false;
        if (wordIndex == word.length) return true;

        boolean succ = false;

        if (traveled[row][col] == 0 && word[wordIndex] == matrix[row][col]) {
            traveled[row][col] = 1;
            System.out.println("[" + row + "," + col + "]");
            succ = judgeHashPath(traveled, matrix, row - 1, col, word, wordIndex + 1);
            if (succ) {
                return true;
            }
            succ = judgeHashPath(traveled, matrix, row + 1, col, word, wordIndex + 1);
            if (succ) {
                return true;
            }
            succ = judgeHashPath(traveled, matrix, row, col - 1, word, wordIndex + 1);
            if (succ) {
                return true;
            }
            succ = judgeHashPath(traveled, matrix, row, col + 1, word, wordIndex + 1);
            if (succ) {
                return true;
            }
            traveled[row][col] = 0;
        }
        return false;
    }
}
