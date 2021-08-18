package leetcode;

import java.util.ArrayList;
import java.util.List;

public class P17 {
    public static void main(String[] args) {
        System.out.println(new P17().letterCombinations("23"));
    }
    public List<String> letterCombinations(String digits) {
        if (digits.length() == 0) return new ArrayList<>();
        String[] digitStrs = new String[]{"abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};
        List<String> strs = new ArrayList<>();

        for (char c : digits.toCharArray()) {
            strs.add(digitStrs[c - '0' - 2]);
        }

        List<String> result = new ArrayList<>();
        doLetterCombinations(strs, result, new StringBuilder(), digits.length(), 0);
        return result;
    }

    private void doLetterCombinations(List<String> strs, List<String> result, StringBuilder prefix, int length, int index) {

        if (prefix.length() == length) {
            result.add(prefix.toString());
            return ;
        }

        if (index >= strs.size()) return ;

        char[] str = strs.get(index).toCharArray();
        for (int i = 0; i < str.length; i++) {
            prefix.append(str[i]);
            doLetterCombinations(strs, result, prefix, length,index + 1);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }
}
