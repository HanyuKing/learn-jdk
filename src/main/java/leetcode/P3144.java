package leetcode;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Hanyu.Wang
 * @Date 2024/8/28 09:40
 * @Description
 * @Version 1.0
 **/
public class P3144 {
    @Test
    public void test1() {
        String s = "fabccddg"; // 3
        System.out.println(minimumSubstringsInPartition(s));
    }

    @Test
    public void test2() {
        String s = "abab"; // 2
        System.out.println(minimumSubstringsInPartition(s));
    }


    @Test
    public void test3() {
        String s = "abababaccddb"; // 2
        System.out.println(minimumSubstringsInPartition(s));
    }

    public int minimumSubstringsInPartition(String s) {
        int n = s.length() + 1;
        int[] dp = new int[n];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;

        int[] table = new int[26];

        for (int i = 1; i < n; i++) {
            Arrays.fill(table, 0);
            int maxCnt = 0;
            int charCount = 0;
            for (int j = i; j >= 1; j--) {
                char c = s.charAt(j - 1);
                int index = c - 'a';
                if (table[index] == 0) {
                    charCount++;
                }
                table[index]++;
                maxCnt = Math.max(maxCnt, table[index]);
                if (i - j + 1 == maxCnt * charCount) {
                    dp[i] = Math.min(dp[j - 1] + 1, dp[i]);
                }
            }
        }

        return dp[s.length()];
    }

    public int minimumSubstringsInPartition2(String s) {
        int n = s.length() + 1;
        int[] dp = new int[n];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;

        for (int i = 1; i < n; i++) {
            Map<Character, Integer> map = new HashMap<>();
            int maxCnt = 0;
            for (int j = i; j >= 1; j--) {
                Character c = s.charAt(j - 1);
                map.put(c, map.getOrDefault(c, 0) + 1);
                maxCnt = Math.max(maxCnt, map.get(c));
                if (i - j + 1 == maxCnt * map.size()) {
                    dp[i] = Math.min(dp[j - 1] + 1, dp[i]);
                }
            }
        }

        return dp[s.length()];
    }
}
