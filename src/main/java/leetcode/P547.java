package leetcode;

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

        int count = 0;

        boolean[] visited = new boolean[row];

        for (int i = 0; i < row; i++) {
            if (!visited[i]) {
                dfs(isConnected, i, visited);
                count++;
            }
        }

        return count;
    }

    private void dfs(int[][] isConnected, int i, boolean[] visited) {
        visited[i] = true;
        for (int j = 0; j < isConnected[0].length; j++) {
            if(isConnected[i][j] == 1 && !visited[j]) {
                dfs(isConnected, j, visited);
            }
        }
    }
}
