package leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2023/11/13 10:54
 * @Description
 * @Version 1.0
 **/
public class P307 {
    public static void main(String[] args) {

    }

    class NumArray {

        private int[] nums;

        public NumArray(int[] nums) {
            this.nums = nums;
        }

        public void update(int index, int val) {
            nums[index] = val;
        }

        public int sumRange(int left, int right) {
            int sum = 0;
            for (int i = left; i <= right; i++) {
                sum += nums[i];
            }
            return sum;
        }
    }
}
