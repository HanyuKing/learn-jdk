package leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2022/12/27 00:51
 * @Description
 * @Version 1.0
 **/
public class P2027 {
    public static void main(String[] args) {
        System.out.println(new Solution2027().minimumMoves("OXOX"));
    }

    static class Solution2027 {
        public int minimumMoves(String s) {
            int cnt = 0;
            for (int i = 0; i < s.length(); ) {
                if (s.charAt(i) == 'X') {
                    cnt++;
                    i += 3;
                } else {
                    i++;
                }
            }
            return cnt;
        }
    }
}
