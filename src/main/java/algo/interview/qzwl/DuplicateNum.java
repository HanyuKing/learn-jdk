package algo.interview.qzwl;

import org.junit.Test;

public class DuplicateNum {
    class MaxNum {
        public int num;
        public int count;
        public MaxNum(int num, int count) {
            this.num = num;
            this.count = count;
        }

        @Override
        public String toString() {
            return "maxNum: " + this.num + ", count: " + this.count;
        }
    }

    public MaxNum find(int[] nums) {
        if (nums.length == 1) {
            return new MaxNum(nums[0], 1);
        }
        int maxCount = 1;
        int maxNum = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == maxNum) {
                maxCount++;
            } else if (nums[i] > maxNum) {
                maxNum = nums[i];
                maxCount = 1;
            } else { // <
                break;
            }
        }
        return new MaxNum(maxNum, maxCount);
    }

    @Test
    public void testFind() {
        int[] nums = new int[] {1,1,2,2,2,2,3,4,5,6,7,7,7,7,99,99,99,4,3,2,1};
        System.out.println(find(nums));
    }
}
