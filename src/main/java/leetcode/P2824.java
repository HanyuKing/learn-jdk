package leetcode;

import java.util.Collections;
import java.util.List;

/**
 * @Author Hanyu.Wang
 * @Date 2023/11/24 09:48
 * @Description
 * @Version 1.0
 **/
public class P2824 {
    public static void main(String[] args) {

    }

    public int countPairs(List<Integer> nums, int target) {

        Collections.sort(nums);

        int cnt = 0;
        int start = 0;
        while (start < nums.size()) {
            int end = start + 1;
            while (end < nums.size() && nums.get(start) + nums.get(end) < target) {
                cnt++;
                end++;
            }
            start++;
        }

        return cnt;
    }
}
