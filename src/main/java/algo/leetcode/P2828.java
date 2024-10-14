package algo.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Hanyu.Wang
 * @Date 2023/12/20 14:56
 * @Description
 * @Version 1.0
 **/
public class P2828 {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("aaaa");
        list.add("bbbb");
        String s = "ab";
        boolean result = new P2828().isAcronym(list, s);
        assert result;
    }
    public boolean isAcronym(List<String> words, String s) {
        StringBuilder wordFirst = new StringBuilder();
        for (String word : words) {
            wordFirst.append(word.charAt(0));
        }
        return s.equals(wordFirst.toString());
    }
}
