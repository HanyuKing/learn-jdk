package algo.dp;

import algo.hot200.Base;
import jol.A;
import org.junit.Test;

import java.util.*;

public class Answer extends Base {

    @Test
    public void testP300() {
        int[] nums = new int[] {1,3,6,7,9,4,10,5,6};
        print(lengthOfLIS(nums));
    }

    public int lengthOfLIS(int[] nums) {
        int[] dp = new int[nums.length];
        Arrays.fill(dp, 1);
        int max = 1;
        for (int i = 1; i < nums.length; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            max = Math.max(max, dp[i]);
        }

        return max;
    }
    @Test
    public void testP139() {
        String s = "leetcode";
        List<String> wordDict = new ArrayList<>(Arrays.asList("leet", "code"));
        print(wordBreak(s, wordDict)); // true

        s = "applepenapple";
        wordDict = new ArrayList<>(Arrays.asList("apple", "pen"));
        print(wordBreak(s, wordDict)); // true

        s = "catsandog";
        wordDict = new ArrayList<>(Arrays.asList("cats", "dog", "sand", "and", "cat"));
        print(wordBreak(s, wordDict)); // false
    }

    public boolean wordBreak(String s, List<String> wordDict) {
        Set<String> wordSet = new HashSet<>(wordDict);
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;

        for (int i = 1; i <= s.length(); i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && wordSet.contains(s.substring(j, i))) {
                    dp[i] = true;
                }
            }
        }

        return dp[s.length()];
    }

    @Test
    public void testP322() {
        int[] coins = new int[] {2};
        int amount = 3;
        print(coinChange(coins, amount));
    }

    public int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;

        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (i - coin >= 0 && dp[i - coin] != Integer.MAX_VALUE) {
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }

        return dp[amount] == Integer.MAX_VALUE ? -1 : dp[amount];
    }
}
