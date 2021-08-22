package leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * 409. Longest Palindrome
 */
public class P409 {
    public int longestPalindrome(String s) {
        Map<Character, Integer> map = new HashMap<>();

        for(Character c : s.toCharArray()) {
            map.put(c, map.getOrDefault(c, 0) + 1);
        }

        int sum = 0;
        for (Map.Entry<Character, Integer> entry : map.entrySet()) {
            Integer val = entry.getValue();
            if(val % 2 == 0) {
                sum += val;
            } else {
                sum += val - 1;
            }
        }


        return sum < s.length() ? sum + 1 : sum;
    }
}
