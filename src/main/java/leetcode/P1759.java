package leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Hanyu.Wang
 * @Date 2022/12/27 01:00
 * @Description
 * @Version 1.0
 **/
public class P1759 {
    public static void main(String[] args) {
        System.out.println(new Solution1759().countHomogenous("abbcccaa")); // abbcccaa
    }
    static class Solution1759 {
        public int countHomogenous(String s) {
            List<String>[] lists = new List[26];

            // part 1
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                List<String> list = lists[c - 'a'];
                if (list == null) {
                    list = new ArrayList<>();
                    lists[c - 'a'] = list;
                }
                int j = i + 1;
                for (; j < s.length(); j++) {
                    if (s.charAt(j) != c) {
                        break;
                    }
                }
                list.add(s.substring(i, j));
                i = j - 1;
            }

            // part 2
            long totalCnt = 0;

            for (List<String> list : lists) {
                if (list == null) {
                    continue;
                }

                long cnt = 0;
                for (String str : list) {
                    cnt += (str.length() * (long) (str.length() + 1)) / 2;
                }

                totalCnt += cnt;
            }

            return (int) (totalCnt % 1000000007);
        }
    }
}
