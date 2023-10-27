package leetcode;

import java.util.HashMap;
import java.util.Map;

public class P5 {
    public static void main(String[] args) {
        System.out.println(new P5().longestPalindromeForDP("aacabdkacaa"));
    }

    public String longestPalindromeForDP(String s) {
        String res = String.valueOf(s.charAt(0));

        int length = s.length();

        int[][] dp = new int[length][length];

        for (int i = 0; i < length; i++) {
            dp[i][i] = 1;
        }

        int longest = 1;

        for (int i = length - 1 - 1; i >= 0; i--) {
            for (int j = length - 1; j > i; j--) {
                if (s.charAt(i) == s.charAt(j) && dp[i + 1][j - 1] >= 0) {
                    dp[i][j] = dp[i + 1][j - 1] + 2;
                    if (dp[i][j] > longest) {
                        longest = dp[i][j];
                        res = s.substring(i, j + 1);
                    }
                } else {
                    dp[i][j] = -1;
                }
            }
        }

        return res;
    }

    public String longestPalindrome(String s) {
        boolean[][] flag = new boolean[s.length()][s.length()];

        String res = String.valueOf(s.charAt(0));

        Map<String, Boolean> map = new HashMap<>();

        for(int i = s.length() - 1; i >= 0; i--) {
            for(int j = i; j < s.length(); j++) {
                if(s.charAt(i) == s.charAt(j)) {
                    if(j - i < 3) {
                        flag[i][j] = true;
                        map.put(i + "_" + j, true);
                    } else if(map.getOrDefault((i + 1) + "_" + (j - 1), false)) {
                        flag[i][j] = true;
                        map.put(i + "_" + j, true);
                    }
                }
                if (flag[i][j] && (j - i + 1) > res.length()) {
                    res = s.substring(i, j + 1);
                }
            }
        }
        return res;
    }
}
