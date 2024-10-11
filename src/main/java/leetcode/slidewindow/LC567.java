package leetcode.slidewindow;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Hanyu.Wang
 * @Date 2024/10/10 09:41
 * @Description
 * @Version 1.0
 **/
public class LC567 {
    public static void main(String[] args) {
        String s1 = "adc";
        String s2 = "dcda";
        System.out.println(new LC567().checkInclusion(s1, s2));
    }
    public boolean checkInclusion(String s1, String s2) {
        Map<Character, Integer> needMap = new HashMap<>();
        Map<Character, Integer> windowMap = new HashMap<>();

        for (Character c : s1.toCharArray()) {
            needMap.put(c, needMap.getOrDefault(c, 0) + 1);
        }
        int l = 0;
        int r = 0;

        int validTotal = 0;

        while (r < s2.length()) {
            Character rValue = s2.charAt(r);
            r++;
            if (needMap.containsKey(rValue)) {
                Integer rValueTotal = windowMap.getOrDefault(rValue, 0) + 1;
                windowMap.put(rValue, rValueTotal);
                if (rValueTotal.equals(needMap.get(rValue))) {
                    validTotal++;
                }
            }

            while (validTotal == needMap.size()) {
                if (r - l == s1.length()) {
                    return true;
                }
                Character lValue = s2.charAt(l);
                Integer lValueTotal = windowMap.get(lValue);
                if (lValueTotal == null) {
                    l++;
                    continue;
                }

                if (lValueTotal == 1) {
                    windowMap.remove(lValue);
                } else {
                    windowMap.put(lValue, lValueTotal - 1);
                }

                if (lValueTotal <= needMap.get(lValue)) {
                    validTotal--;
                }

                l++;
            }
        }
        return false;
    }
}
