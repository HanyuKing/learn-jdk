package leetcode;

import java.util.Stack;

/**
 * @Author Hanyu.Wang
 * @Date 2023/11/22 14:14
 * @Description
 * @Version 1.0
 **/
public class P2216 {
    public static void main(String[] args) {
//        int[] nums = new int[] {1, 1, 2, 3, 5}; // 1
        int[] nums = new int[] {1, 1, 2, 2, 3, 3}; // 2

        int result = new P2216().minDeletion2(nums);
        System.out.println(result);
    }

    public int minDeletion2(int[] nums) {
        int preNum = -1;
        int newArraySize = 0;
        int cnt = 0;

        for (int num : nums) {

            if (newArraySize != 0) {
                if ((newArraySize & 1) == 0) {
                    preNum = num;
                    newArraySize++;
                    continue;
                }
                if (num == preNum) {
                    cnt++;
                    continue;
                }
            }

            preNum = num;
            newArraySize++;
        }

        if ((newArraySize & 1) == 1) {
            cnt++;
        }

        return cnt;
    }

    public int minDeletion(int[] nums) {
        Stack<Integer> stack = new Stack<>();
        int cnt = 0;

        for (int num : nums) {

            if (stack.size() != 0) {
                if ((stack.size() & 1) == 0) {
                    stack.push(num);
                    continue;
                }
                if (num == stack.peek()) {
                    cnt++;
                    continue;
                }
            }

            stack.push(num);
        }

        if ((stack.size() & 1) == 1) {
            cnt++;
        }

        return cnt;
    }
}
