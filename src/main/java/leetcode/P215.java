package leetcode;

import java.util.*;

public class P215 {
    public static int findKthLargest(int[] nums, int k) {
//        Set<Integer> sets = new HashSet<>(nums.length);
//        for (int i = 0; i < nums.length; i++) {
//            sets.add(Integer.valueOf(nums[i]));
//        }
//        Object[] newNums = sets.toArray();
        new HashMap<Integer, Integer>();
        PriorityQueue<Integer> pq = new PriorityQueue<>(k, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });
        for (int i = 0; i < k; i++) {
            pq.add(Integer.valueOf(nums[i]));
        }
        for (int i = k; i < nums.length; i++) {
            if (nums[i] >= pq.peek()) {
                pq.poll();
                pq.add(Integer.valueOf(nums[i]));
            }
        }
        return pq.peek().intValue();
    }

    public static void main(String []args) {
       System.out.println(findKthLargest(new int[]{3,2,1,5,6,4}, 2));
    }

}
