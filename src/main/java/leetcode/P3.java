package leetcode;

import java.util.HashSet;
import java.util.Set;

public class P3 {

    public static void main(String[] args) {
        int maxLen = lengthOfLongestSubstring("abcabcbb");
        System.out.println(maxLen);
    }

    public static int lengthOfLongestSubstring(String s) {
        if (s.length() == 0) {
            return 0;
        }
        int maxLen = 1;
        for (int i = 0; i < s.length(); i++) {
            Set<Character> set = new HashSet<>();
            set.add(s.charAt(i));
            int l = 1;
            for (int j = i + 1; j < s.length(); j++) {
                if (set.contains(s.charAt(j))) {
                    set.remove(s.charAt(i));
                    break;
                } else {
                    set.add(s.charAt(j));
                    l++;
                    maxLen = Math.max(l, maxLen);
                }
            }
        }

        return maxLen;
    }
}
