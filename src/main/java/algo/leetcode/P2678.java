package algo.leetcode;

import java.util.Arrays;

/**
 * @Author Hanyu.Wang
 * @Date 2023/10/23 11:20
 * @Description
 * @Version 1.0
 **/
public class P2678 {
    public static void main(String[] args) {

    }
    class Solution {
        public int countSeniors(String[] details) {
            return (int) Arrays.stream(details).filter(s -> Integer.parseInt(s.substring(11, 13)) > 60).count();
        }
    }
}
