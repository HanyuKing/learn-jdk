package leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2023/11/7 15:47
 * @Description
 * @Version 1.0
 **/
public class P2586 {
    public int vowelStrings(String[] words, int left, int right) {
        int cnt = 0;
        String s = "aeiou";
        for (int i = left;  i < words.length && i <= right; i++) {
            if (s.contains(words[i].charAt(0) + "") && s.contains(words[i].charAt(words[i].length() - 1) + "")) {
                cnt++;
            }
        }
        return cnt;
    }
}
