package leetcode;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * @Author Hanyu.Wang
 * @Date 2023/1/8 18:00
 * @Description
 * @Version 1.0
 **/
public class P6285 {
    public static void main(String[] args) {
        System.out.println(new Solution().maxKelements(new int[]{1,10,3,3,3}, 3));
    }

    static class Solution {

        public long maxKelements(int[] nums, int k) {
            PriorityQueue<Integer> pq = new PriorityQueue<>(nums.length, (o1, o2) -> o2 - o1);
            for (int i = 0; i < nums.length; i++) {
                pq.add(nums[i]);
            }
            long result = 0;

            while (!pq.isEmpty() && k-- > 0) {
                Integer maxValue = pq.poll();
                result += maxValue;
                pq.add((int) Math.ceil((double) maxValue / 3));
            }

            return result;
        }
    }
}
