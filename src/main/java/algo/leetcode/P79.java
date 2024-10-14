package algo.leetcode;

public class P79 {
    public static void main(String[] args) {
        char[][] board = new char[][]{
                {'a','a','a','a'},
                {'a','a','a','a'},
                {'a','a','a','a'},
            };

        System.out.println(new P79().exist(board, "aaaaaaaaaaa"));
    }

    public boolean exist(char[][] board, String word) {
        if (board == null || board.length == 0) return false;

        char[] wordArr = word.toCharArray();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                boolean[][] visited = new boolean[board.length][board[0].length];
                boolean exists = find(board, visited, i, j, 0, wordArr);
                if (exists) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean find(char[][] board, boolean[][] visited, int i, int j, int wordIndex, char[] wordArr) {

        if (i < 0 || j < 0 || i >= board.length || j >= board[0].length || visited[i][j]) return false;

        if (wordIndex < wordArr.length && board[i][j] != wordArr[wordIndex]) return false;

        if (wordIndex == wordArr.length - 1) return true;

        visited[i][j] = true;

        boolean exists = false;

        // left
        exists = find(board, visited, i, j - 1, wordIndex + 1, wordArr);
        if (exists) {
            return true;
        }

        // up
        exists = find(board, visited, i - 1, j, wordIndex + 1, wordArr);
        if (exists) {
            return true;
        }

        // right
        exists = find(board, visited, i, j + 1, wordIndex + 1, wordArr);
        if (exists) {
            return true;
        }

        // down
        exists = find(board, visited, i + 1, j, wordIndex + 1, wordArr);
        if (exists) {
            return true;
        }

        visited[i][j] = false;

        return false;
    }
}
