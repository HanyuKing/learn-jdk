package algo.heap;

import algo.hot200.Base;
import org.junit.Test;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Answer extends Base {

    @Test
    public void testP295() {
        MedianFinder medianFinder = new MedianFinder();
        medianFinder.addNum(1);
        medianFinder.addNum(2);
        System.out.println(medianFinder.findMedian());
    }


    @Test
    public void testP347() {
        int[] nums = new int[] {1,1,1,2,2,3};
        int k = 2; // [1,2]
        print(topKFrequent(nums, k));


    }
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }
        PriorityQueue<int[]> priorityQueue = new PriorityQueue<>(nums.length, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[1] - o2[1];
            }
        });
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
           if (priorityQueue.size() < k) {
               priorityQueue.offer(new int[] {entry.getKey(), entry.getValue()});
           } else {
               if (entry.getValue() > priorityQueue.peek()[1]) {
                   priorityQueue.offer(new int[] {entry.getKey(), entry.getValue()});
                   priorityQueue.poll();
               }
           }
        }
        int[] result = new int[k];
        int i = 0;
        while (!priorityQueue.isEmpty()) {
            result[i++] = priorityQueue.poll()[0];
        }
        return result;
    }
}
