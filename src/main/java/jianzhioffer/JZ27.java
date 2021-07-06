package jianzhioffer;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * 字符串的排列
 */
public class JZ27 {
    public static void main(String[] args) {
        System.out.println(new JZ27().Permutation("abc"));
    }

    public ArrayList<String> Permutation(String str) {
        if (str.length() == 0) {
            return new ArrayList<>();
        }
        TreeSet<String> set = doPermutation(str, 0, new TreeSet<String>());
        return new ArrayList<>(set);
    }

    public TreeSet<String> doPermutation(String str, int index, TreeSet<String> set) {
        if (index + 1 == str.length()) {
            set.add(str);
            return set;

        }

        for (int i = index; i < str.length(); i++) {
            str = swap(str, index, i);
            doPermutation(str, index + 1, set);
            str = swap(str, i, index);
        }

        return set;
    }

    public String swap(String s, int i, int j) {
        char[] chars = s.toCharArray();
        char temp = chars[i];
        chars[i] = chars[j];
        chars[j] = temp;
        return new String(chars);
    }
}
