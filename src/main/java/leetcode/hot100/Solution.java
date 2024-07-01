package leetcode.hot100;

class Solution {

    public static void main(String[] args) {
        System.out.println(new Solution().minDistance("pneumonoultramicroscopicsilicovolcanoconiosis", "ultramicroscopically"));
    }

    // 提供个更简单的思路
    // 如果遍历相同 直接取左下角的值 
    // 如果不相同 左 下 左下取最小值 +1 
    public int minDistance(String word1, String word2) {
        int n = word1.length();
        int m = word2.length();

        // DP 数组
        int[][] D = new int[n + 1][m + 1];

        // 边界状态初始化
        for (int i = 0; i < n + 1; i++) {
            D[i][0] = i;
        }
        for (int j = 0; j < m + 1; j++) {
            D[0][j] = j;
        }

        // 计算所有 DP 值
        for (int i = 1; i < n + 1; i++) {
            for (int j = 1; j < m + 1; j++) {
                int up = D[i - 1][j];
                int left = D[i][j - 1];
                int left_up = D[i - 1][j - 1];
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    D[i][j] = left_up;
                } else {
                    D[i][j] = Math.min(left_up, Math.min(up, left)) + 1;
                }
            }
        }
        return D[n][m];
    }
}