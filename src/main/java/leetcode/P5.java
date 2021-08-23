package leetcode;

import java.util.HashMap;
import java.util.Map;

public class P5 {
    public static void main(String[] args) {
        System.out.println(new P5().longestPalindrome("aaaa"));
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
