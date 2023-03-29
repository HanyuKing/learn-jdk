package leetcode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author Hanyu.Wang
 * @Date 2023/3/9 10:51
 * @Description
 * @Version 1.0
 **/
public class P22 {
    public static void main(String[] args) {
        System.out.println(new P22().generateParenthesis(4));
    }

    //数字 n 代表生成括号的对数，请你设计一个函数，用于能够生成所有可能的并且 有效的 括号组合。
//
//
//
// 示例 1：
//
//
//输入：n = 3
//输出：["((()))","(()())","(())()","()(())","()()()"]
//
//
// 示例 2：
//
//
//输入：n = 1
//输出：["()"]
//
//
//  n = 2
//  ["(())", "()()"]
//
// 提示：
//
//
// 1 <= n <= 8
//
//
// Related Topics 字符串 动态规划 回溯 👍 3116 👎 0

    public List<String> generateParenthesis(int n) {
        String left = "(";
        String right = ")";
        String first = left +right;

        List<String> result = new ArrayList<>();
        result.add(first);

        while (--n > 0) {
            Set<String> set = new HashSet<>();
            for (int i = 0; i < result.size(); i++) {
                set.add(result.get(i) + left + right);
                set.add(left + result.get(i) + right);
                set.add(left + right + result.get(i));
            }
            result = new ArrayList<>(set);
        }

        return result;
    }
}