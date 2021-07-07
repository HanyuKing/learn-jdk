package jianzhioffer;

import scala.Int;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * JZ29 最小的K个数
 */
public class JZ29 {

    public static void main(String[] args) {
        GetLeastNumbers_Solution(new int[]{4,5,1,6,2,7,3,8}, 4);
    }

    public static ArrayList<Integer> GetLeastNumbers_Solution(int [] input, int k) {
        if (input == null || k > input.length || k <= 0) {
            return new ArrayList<>();
        }
        PriorityQueue<Integer> q = new PriorityQueue<Integer>(k, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });
        int i = 0;
        for (; i < k && i < input.length; i++) {
            q.add(Integer.valueOf(input[i]));
        }

        for (; i < input.length; i++) {
            if (input[i] < q.peek()) {
                q.poll();
                q.add(input[i]);
            }
        }
        ArrayList<Integer> results = new ArrayList<Integer>();
        while (q.size() > 0) {
            results.add(q.poll());
        }
        return results;
    }
}
