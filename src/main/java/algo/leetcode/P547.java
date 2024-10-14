package algo.leetcode;

public class P547 {
    public static void main(String[] args) {
        int[][] isConnedted = new int[][]{
                {1,1,0},
                {1,1,0},
                {0,0,1}
        };
        System.out.println(new P547().findCircleNum(isConnedted));
    }

    public int findCircleNum(int[][] isConnected) {
        if (isConnected == null) {
            return 0;
        }

        int row = isConnected.length;
        int col = isConnected[0].length;

        int count = 0;

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (isConnected[i][j] == 1) {
                    dfs(isConnected, i, j);
                    count++;
                }
            }
        }

        return count;
    }

    private void dfs(int[][] isConnected, int i, int j) {
        if (i < 0 || j < 0 || i >= isConnected.length || j >= isConnected[0].length || isConnected[i][j] != 1) {
            return;
        }
        isConnected[i][j] = 2;

        for (int k = 0; k < isConnected.length; k++) {
            if (isConnected[j][k] != 1) {
                continue;
            }
            dfs(isConnected, j, k);
        }
    }
}
