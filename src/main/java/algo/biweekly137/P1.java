package algo.biweekly137;

import java.util.Deque;
import java.util.LinkedList;

public class P1 {
    public static void main(String[] args) {
        int[] nums = new int[] {1,2,3,4,3,2,5};
        int k = 3;

        System.out.println(new P1().resultsArray(nums, 3));
        //System.out.println(new P1().resultsArray(new int[] {2,2,2,2,2}, 4));
        //System.out.println(new P1().resultsArray(new int[] {3,2,3,2,3,2}, 2));
        //System.out.println(new P1().resultsArray(new int[] {1,3,4}, 2));
        //System.out.println(new P1().resultsArray(new int[] {1,2,3,2}, 3));
    }

    public int[] resultsArray(int[] nums, int k) {
        int n = nums.length;
        int r = 0;

        int[] result = new int[n - k + 1];
        int index = 0;

        Deque<Integer> queue = new LinkedList<>();

        while (r < n) {
            int numR = nums[r];

            if (queue.isEmpty() || numR == queue.getLast() + 1) {
                queue.addLast(numR);
                if (queue.size() == k) {
                    queue.pollFirst();
                    result[index++] = numR;
                }
                r++;
            } else {
                while (!queue.isEmpty()) {
                    queue.pollFirst();
                    if (index == result.length) {
                        break;
                    }
                    result[index++] = -1;
                }
                queue.addLast(numR);
                r++;
            }
        }

        return result;
    }
}
