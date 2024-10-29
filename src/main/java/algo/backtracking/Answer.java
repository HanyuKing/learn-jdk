package algo.backtracking;

import algo.hot200.Base;
import org.junit.Test;

import java.util.*;

/**
 * @Author Hanyu.Wang
 * @Date 2024/10/21 15:03
 * @Description
 * @Version 1.0
 **/
public class Answer extends Base {

    @Test
    public void testP47() {
        print(permuteUnique(new int[] {1, 1, 2}));
    }

    public List<List<Integer>> permuteUnique(int[] nums) {
        Arrays.sort(nums);
        return permuteUnique3(nums);
    }
    public List<List<Integer>> permuteUnique3(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        boolean[] visited = new boolean[nums.length];
        getAllPermuteUnique3(nums, result, visited, new ArrayList<>());

        return result;
    }

    public void getAllPermuteUnique3(int[] nums, List<List<Integer>> result, boolean[] visited, List<Integer> curr) {
        if (curr.size() == nums.length) {
            result.add(new ArrayList<>(curr));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (visited[i] || (i > 0 && nums[i] == nums[i - 1] && !visited[i - 1])) {
                continue;
            }

            curr.add(nums[i]);

            visited[i] = true;

            getAllPermuteUnique3(nums, result, visited, curr);

            visited[i] = false;

            curr.remove(curr.size() - 1);
        }
    }

    @Test
    public void testP46() {
//        print(permute(new int[]{1, 2, 3}));
        print(permute3(new int[]{1, 2, 3}));
    }

    public List<List<Integer>> permute3(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        boolean[] visited = new boolean[nums.length];
        getAllPermute3(nums, result, visited, new ArrayList<>());
        return result;
    }

    public void getAllPermute3(int[] nums, List<List<Integer>> result, boolean[] visited, List<Integer> curr) {
        if (curr.size() == nums.length) {
            result.add(new ArrayList<>(curr));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (visited[i]) {
                continue;
            }

            curr.add(nums[i]);

            visited[i] = true;

            getAllPermute3(nums, result, visited, curr);

            visited[i] = false;

            curr.remove(curr.size() - 1);
        }
    }

    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        getAllPermute(nums, result, new ArrayList<>(), 0);
        return result;
    }

    public void getAllPermute(int[] nums, List<List<Integer>> result, List<Integer> curr, int start) {
        if (curr.size() == nums.length) {
            result.add(new ArrayList<>(curr));
            return;
        }
        for (int i = start; i < nums.length; i++) {
            curr.add(nums[i]);

            swap(nums, start, i);

            getAllPermute(nums, result, curr, start + 1);

            swap(nums, start, i);

            curr.remove(curr.size() - 1);
        }
    }

    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    public List<List<Integer>> permute2(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        doPermute2(nums, 0, nums.length, result);
        return result;
    }

    public void doPermute2(int[] nums, int start, int end, List<List<Integer>> result) {
        if (end - start == 1) {
            List<Integer> ans = new ArrayList<>();
            for (int num : nums) {
                ans.add(num);
            }
            result.add(ans);
            return;
        }

        for (int i = start; i < end; i++) {

            int temp = nums[start];
            nums[start] = nums[i];
            nums[i] = temp;

            doPermute2(nums, start + 1, nums.length, result);

            temp = nums[i];
            nums[i] = nums[start];
            nums[start] = temp;
        }

    }

    @Test
    public void testP131() {
        String s = "aab"; // [["a","a","b"],["aa","b"]]
        // print(partition(s));

        s = "abbab"; // abbab
        print(partition(s));
    }

    boolean[][] flag;
    List<List<String>> ret = new ArrayList<List<String>>();
    List<String> ans = new ArrayList<String>();
    int n;
    public List<List<String>> partition(String s) {
        n = s.length();
        flag = new boolean[n][n];

        for (int i = 0; i < n; i++) {
            Arrays.fill(flag[i], true);
        }

        for (int i = n - 1; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {
                flag[i][j] = flag[i + 1][j - 1] && s.charAt(i) == s.charAt(j);
            }
        }
        doPartition(s, 0);
        return ret;
    }

    private void doPartition2(String s, int index) {
        for (int i = index; i < n; i++) {
            for (int j = i; j < n; j++) {
                if (flag[i][j]) {
                    ans.add(s.substring(i, j + 1));
                }
            }
            ret.add(ans);
            ans = new ArrayList<>();
        }
    }

    private void doPartition(String s, int index) {
        if (index == n) {
            ret.add(new ArrayList<>(ans));
            return;
        }
        for (int j = index; j < n; j++) {
            if (flag[index][j]) {
                ans.add(s.substring(index, j + 1));
                doPartition(s, j + 1);
                ans.remove(ans.size() - 1);
            }
        }
    }

    @Test
    public void testP79() {
        char[][] board = new char[][] {
                {'A','B','C','E'},
                {'S','F','C','S'},
                {'A','D','E','E'}};
        String word = "ABCCEE";
        print(exist(board, word)); // true

        board = new char[][] {
                {'A','B','C','E'},
                {'S','F','C','S'},
                {'A','D','E','E'}};
        word = "SEE"; // true
        print(exist(board, word));
    }

    public boolean exist(char[][] board, String word) {
        int row = board.length;
        int col = board[0].length;
        boolean[][] visited = new boolean[row][col];
        for (int i = 0 ; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (board[i][j] == word.charAt(0)) {
                    if (found2(board, visited, i, j, word, 0)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 优化版本，不使用visited数组标记是否访问过，直接用原数组
     *
     * @param board
     * @param visited
     * @param i
     * @param j
     * @param word
     * @param searchIndex
     * @return
     */
    private boolean found2(char[][] board, boolean[][] visited, int i, int j, String word, int searchIndex) {
        if (searchIndex == word.length()) {
            return true;
        }

        int row = board.length;
        int col = board[0].length;

        if (i < 0 || i >= row || j < 0 || j >= col || board[i][j] == '0') {
            return false;
        }

        if (word.charAt(searchIndex) == board[i][j]) {
            char temp = board[i][j];
            board[i][j] = '0';
            if (found2(board, visited, i, j - 1, word, searchIndex + 1)) {
                return true;
            }
            if (found2(board, visited, i - 1, j, word, searchIndex + 1)) {
                return true;
            }
            if (found2(board, visited, i, j + 1, word, searchIndex + 1)) {
                return true;
            }
            if (found2(board, visited, i + 1, j, word, searchIndex + 1)) {
                return true;
            }
            board[i][j] = temp;
        }

        return false;
    }

    private boolean found(char[][] board, boolean[][] visited, int i, int j, String word, int searchIndex) {
        if (searchIndex == word.length()) {
            return true;
        }

        int row = board.length;
        int col = board[0].length;

        if (i < 0 || i >= row || j < 0 || j >= col || visited[i][j]) {
            return false;
        }

        if (word.charAt(searchIndex) == board[i][j]) {
            visited[i][j] = true;
            if (found(board, visited, i, j - 1, word, searchIndex + 1)) {
                return true;
            }
            if (found(board, visited, i - 1, j, word, searchIndex + 1)) {
                return true;
            }
            if (found(board, visited, i, j + 1, word, searchIndex + 1)) {
                return true;
            }
            if (found(board, visited, i + 1, j, word, searchIndex + 1)) {
                return true;
            }
            visited[i][j] = false;
        }

        return false;
    }
}
