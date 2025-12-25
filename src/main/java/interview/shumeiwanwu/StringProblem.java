package interview.shumeiwanwu;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class StringProblem {
    /**
     * abcde -> abcde
     * abade -> bade
     * aaaaa -> a
     */
    public int maxUnDuplicateSubString(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        int maxLength = 1;
        int left = 0;
        int right = 0;
        Set<Character> existsCharSet = new HashSet<>();

        while (right < s.length()) {
            char rightChar = s.charAt(right);
            if (!existsCharSet.contains(rightChar)) {
                existsCharSet.add(rightChar);
                maxLength = Math.max(right - left + 1, maxLength);
                right++;
            } else {
                while (existsCharSet.contains(rightChar)) {
                    char leftChar = s.charAt(left++);
                    existsCharSet.remove(leftChar);
                }
            }
        }

        return maxLength;
    }

    @Test
    public void test1() {
        System.out.println(maxUnDuplicateSubString("abade"));
    }

    @Test
    public void test2() {
        System.out.println(maxUnDuplicateSubString("abcde"));
    }

    @Test
    public void test3() {
        System.out.println(maxUnDuplicateSubString("aaaaa"));
    }

    @Test
    public void test4() {
        System.out.println(maxUnDuplicateSubString("aaaaa"));
    }
}
