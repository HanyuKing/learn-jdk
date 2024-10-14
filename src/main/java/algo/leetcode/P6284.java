package algo.leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2023/1/8 18:15
 * @Description
 * @Version 1.0
 **/
public class P6284 {
    public static void main(String[] args) {
        System.out.println(new Solution().isItPossible("eeeee", "eeeee"));
    }
    static class Solution {
        public boolean isItPossible(String word1, String word2) {
            int[] arr1 = new int[26];
            int[] arr2 = new int[26];
            for (int i = 0; i < word1.length(); i++) {
                arr1[word1.charAt(i) - 'a']++;
            }
            for (int i = 0; i < word2.length(); i++) {
                arr2[word2.charAt(i) - 'a']++;
            }

            for (int i = 0; i < arr1.length; i++) {
                if (arr1[i] == 0) {
                    continue;
                }
                for (int j = 0; j < arr2.length; j++) {
                    if (arr2[j] == 0) {
                        continue;
                    }
                    arr1[i]--;
                    arr1[j]++;
                    arr2[j]--;
                    arr2[i]++;
                    if (isValid(arr1, arr2)) {
                        return true;
                    }
                    arr1[i]++;
                    arr1[j]--;
                    arr2[j]++;
                    arr2[i]--;
                }
            }

            return false;
        }

        private boolean isValid(int[] word1, int[] word2) {
            int cnt1 = 0;
            int cnt2 = 0;
            for (int i = 0; i < word1.length; i++) {
                if (word1[i] > 0) {
                    cnt1++;
                }
            }
            for (int i = 0; i < word2.length; i++) {
                if (word2[i] > 0) {
                    cnt2++;
                }
            }
            return cnt1 == cnt2;
        }
    }
}
