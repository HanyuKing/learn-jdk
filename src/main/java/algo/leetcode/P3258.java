package algo.leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2024/11/12 19:26
 * @Description
 * @Version 1.0
 **/
public class P3258 {
    public static void main(String[] args) {
        System.out.println(new P3258().countKConstraintSubstrings("10101", 1)); // 12
    }

    public int countKConstraintSubstrings(String s, int k) {
        int n = s.length();
        int ans = 0;
        for (int i = 0; i < n; i++) {
            int[] counts = new int[2];
            for (int j = i; j < n; j++) {
                counts[s.charAt(j) - '0']++;
                if (counts[0] > k && counts[1] > k) {
                    break;
                }
                ans++;
            }
        }
        return ans;
    }
}
