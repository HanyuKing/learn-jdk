package algo.leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 * 205. Isomorphic Strings
 */
public class P205 {
    public static void main(String[] args) {
        System.out.println(new P205().isIsomorphic("aaeaa","uuxyy"));
    }

    public boolean isIsomorphic(String s, String t) {
        Map<Character,Character> map1 = new HashMap<>();
        Map<Character,Character> map2 = new HashMap<>();
        char[] s1 = s.toCharArray();
        char[] s2 = t.toCharArray();

        for(int i = 0; i < s.length(); i++) {
            Character existsC1 = map1.get(s1[i]);
            Character existsC2 = map2.get(s2[i]);

            if (existsC1 == null && existsC2 == null) {
                map1.put(s1[i], s2[i]);
                map2.put(s2[i], s1[i]);
            } else if (existsC1 == null && existsC2 != null) {
                if (s1[i] != existsC2) {
                    return false;
                }
            } else if (existsC1 != null && existsC2 == null) {
                if(s2[i] != existsC1) {
                    return false;
                }
            } else {
                if(!(s2[i] == existsC1 && s1[i] == existsC2)) {
                    return false;
                }
            }
        }

        return true;
    }
}
