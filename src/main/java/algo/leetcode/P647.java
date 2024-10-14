package algo.leetcode;

public class P647 {
    public static void main(String[] args) {
        System.out.println(new P647().countSubstrings("abc"));
        // c bc b ac abc ab a
    }

    private int count;
    public int countSubstrings(String s) {
        for(int i = 0; i < s.length(); i++) {
            extendHuiWen(s, i, i);
            extendHuiWen(s, i, i + 1);
        }
        return count;
    }

    private void extendHuiWen(String s, int start, int end) {
        while (start >= 0 && end < s.length() && s.charAt(start) == s.charAt(end)) {
            start--;
            end++;

            count++;
        }
        return;
    }
}
