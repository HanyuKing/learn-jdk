package algo.permutation;

import algo.hot200.Base;
import org.junit.Test;

import java.util.*;

/**
 * @Author Hanyu.Wang
 * @Date 2024/10/17 09:54
 * @Description
 * @Version 1.0
 **/
public class Answer extends Base {


    @Test
    public void testP22() {
        print(generateParenthesis(3)); // ["((()))","(()())","(())()","()(())","()()()"]

        print(generateParenthesis(1)); // ["()"]
        /*
            "()"
            "()()","(())"
            "()()()","(()())","()(())","(())()","((()))"
         */
        print(generateParenthesis(4)); // "(((())))","((()()))","((())())","((()))()","(()(()))","(()()())","(()())()","(())(())","(())()()","()((()))","()(()())","()(())()","()()(())","()()()()"
    }

    public List<String> generateParenthesis(int n) {
        List<String>[] result = new ArrayList[n + 1];
        result[0] = new ArrayList<>();
        result[0].add("");
        result[1] = new ArrayList<>();
        result[1].add("()");

        for (int i = 2; i <= n; i++) {
            List<String> newResult = new ArrayList<>();
            for (int j = 0; j < i; j++) {
                for (String s : result[j]) {
                    for (String s2 : result[i - 1 - j]) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("(").append(s).append(")").append(s2);
                        newResult.add(sb.toString());
                    }
                }
            }
            result[i] = newResult;
        }
        return result[n];
    }

    @Test
    public void testP39() {
        int[] candidates = new int[] {2,3,6,7}; // [[2,2,3],[7]]
        int target = 7;
        print(combinationSum(candidates, target));

        candidates = new int[] {2,3,5}; // [[2,2,2,2],[2,3,3],[3,5]]
        // 2
        // 3
        // 2 2
        // 5 2,3
        // 2,2,2 3,3
        // 5,2 2,2,3
        // 2,2,2,2 3,3,2 5,3 2,3,3
//        target = 8;
//        print(combinationSum(candidates, target));

    }

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        doCombinationSum(result, candidates, new ArrayList<>(), 0, target);
        return result;
    }

    private void doCombinationSum(List<List<Integer>> result,
                                  int[] candidates,
                                  List<Integer> values,
                                  int begin,
                                  int target) {
        if (begin == candidates.length || target < 0) {
            return;
        }
        if (target == 0) {
            result.add(new ArrayList<>(values));
            return;
        }
        for (int i = begin; i < candidates.length; i++) {
            int value = candidates[i];
            values.add(value);
            doCombinationSum(result, candidates, values, i, target - value);
            values.remove(values.size() - 1);
        }
    }

    @Test
    public void testP17() {
        print(letterCombinations("23"));
        // print(letterCombinations2("237"));
    }

    public List<String> letterCombinations(String digits) {
        if (Objects.equals(digits, "")) {
            return new ArrayList<>();
        }
        Map<Character, String> map = new HashMap<>();
        map.put('2', "abc");
        map.put('3', "def");
        map.put('4', "ghi");
        map.put('5', "jkl");
        map.put('6', "mno");
        map.put('7', "pqrs");
        map.put('8', "tuv");
        map.put('9', "wxyz");

        String[] s = new String[digits.length()];
        char[] characters = digits.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            s[i] = map.get(digits.charAt(i));
        }
        List<String> result = new ArrayList<>();
        doLetterCombinations(result, s, new StringBuilder(), 0);
        return result;
    }

    private void doLetterCombinations(List<String> result, String[] s, StringBuilder values, int index) {
        if (index == s.length) {
            result.add(values.toString());
            return;
        }
        for (int i = 0; i < s[index].length(); i++) {
            values.append(s[index].charAt(i));
            doLetterCombinations(result, s, values, index + 1);
            values.deleteCharAt(values.length() - 1);
        }
    }

    public List<String> letterCombinations2(String digits) {
        if (Objects.equals(digits, "")) {
            return new ArrayList<>();
        }
        Map<Character, String> map = new HashMap<>();
        map.put('2', "abc");
        map.put('3', "def");
        map.put('4', "ghi");
        map.put('5', "jkl");
        map.put('6', "mno");
        map.put('7', "pqrs");
        map.put('8', "tuv");
        map.put('9', "wxyz");

        List<String> result = new ArrayList<>();
        result.add("");

        for (Character digit : digits.toCharArray()) {
            String s = map.get(digit);
            List<String> newResult = new ArrayList<>();
            for (Character c : s.toCharArray()) {
                for (String preS : result) {
                    newResult.add(preS + c);
                }
            }
            result = newResult;
        }

        return result;
    }

    @Test
    public void testP78() {
        // subsets
        int[] nums = new int[] {1, 2, 3}; // [[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]
        print(subsets(nums));

        nums = new int[] {0};
        print(subsets(nums)); // [[],[0]]
    }

    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            List<List<Integer>> newResult = new ArrayList<>();
            int currNum = nums[i];

            for (List<Integer> preSet : result) {
                List<Integer> currSet = new ArrayList<>(preSet);
                currSet.add(currNum);
                newResult.add(currSet);
            }

            List<Integer> currNumSet = new ArrayList<>();
            currNumSet.add(currNum);
            newResult.add(currNumSet);

            result.addAll(newResult);
        }
        result.add(new ArrayList<>());
        return result;
    }
}
