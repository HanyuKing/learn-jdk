package leetcode;

import java.util.*;

/**
 * @Author Hanyu.Wang
 * @Date 2022/12/12 10:18
 * @Description
 * @Version 1.0
 **/
public class P1781 {
    public static void main(String[] args) {
        System.out.println(new Solution1781().beautySum("aabcbaa"));
    }

//一个字符串的 美丽值 定义为：出现频率最高字符与出现频率最低字符的出现次数之差。
//
//
// 比方说，"abaacc" 的美丽值为 3 - 1 = 2 。
//
//
// 给你一个字符串 s ，请你返回它所有子字符串的 美丽值 之和。
//
//
//
// 示例 1：
//
//
//输入：s = "aabcb"
//输出：5
//解释：美丽值不为零的字符串包括 ["aab","aabc","aabcb","abcb","bcb"] ，每一个字符串的美丽值都为 1 。
//
// 示例 2：
//
//
//输入：s = "aabcbaa"
//输出：17
//
//
//
// 提示：
//
//
// 1 <= s.length <= 500
// s 只包含小写英文字母。
//
//
// Related Topics 哈希表 字符串 计数 👍 33 👎 0

    static class Solution1781 {
        public int beautySum(String s) {
            List<String> allSubstrings = this.getAllSubstring(s);
            Map<String, Integer> cache = new HashMap<>();
            int sum = 0;

            for (String substring : allSubstrings) {
                Integer number = cache.getOrDefault(substring, getSingleBeautyNum(substring));
                sum += number;
                cache.put(substring, number);
            }

            return sum;
        }

        private int getSingleBeautyNum(String s) {
            char[] array = s.toCharArray();
            int[] charSum = new int[26];
            for (int i = 0; i < array.length; i++) {
                charSum[array[i] - 'a']++;
            }
            int max = Integer.MIN_VALUE, min = Integer.MAX_VALUE;
            for (int i = 0; i < charSum.length; i++) {
                if (charSum[i] > 0 && charSum[i] > max) {
                    max = charSum[i];
                }
                if (charSum[i] > 0 && charSum[i] < min) {
                    min = charSum[i];
                }
            }
            return max - min;
        }

        private List<String> getAllSubstring(String s) {
            List<String> allSubstrings = new ArrayList<>();
            int length = s.length();
            for (int i = 0; i < length; i++) {
                for (int j = i + 3; j <= length; j++) {
                    allSubstrings.add(s.substring(i, j));
                }
            }
            return allSubstrings;
        }
    }

}
