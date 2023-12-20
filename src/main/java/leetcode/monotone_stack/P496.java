package leetcode.monotone_stack;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @Author Hanyu.Wang
 * @Date 2023/11/28 15:29
 * @Description
 * @Version 1.0
 **/
public class P496 {

    public static void main(String[] args) {
        int[] nums1 = new int[] {4, 1, 2};
        int[] nums2 = new int[] {1, 3, 4, 2};

        int[] result = new P496().nextGreaterElement(nums1, nums2);

        for (int i = 0; i < result.length; i++) {
            System.out.println(result[i] + " ");
        }
        System.out.println();
    }

    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        Map<Integer, Integer> map = new HashMap<>();
        Stack<Integer> monotoneStack = new Stack<>();

        for (int i = 0; i < nums2.length; i++) {
            while (!monotoneStack.isEmpty() && nums2[monotoneStack.peek()] < nums2[i]) {
                int index = monotoneStack.pop();
                map.put(nums2[index], nums2[i]);
            }
            monotoneStack.push(i);
        }

        int[] result = new int[nums1.length];

        for (int i = 0; i < nums1.length; i++) {
            result[i] = map.getOrDefault(nums1[i], -1);
        }

        return result;
    }
}
