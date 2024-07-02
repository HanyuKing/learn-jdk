package leetcode.sort;

/**
 * @Author Hanyu.Wang
 * @Date 2024/6/28 16:28
 * @Description
 * @Version 1.0
 **/
public class HeapSort {
    public static void main(String[] args) {

    }
    public void heapSort(int[] nums, boolean isMinHeap) {
        for (int i = nums.length - 1; i >= 0; i--) {
            adjustHeap(nums, i, nums.length, isMinHeap);
        }
        for (int i = nums.length - 1; i > 0; i--) {
            swap(nums, 0, i);
            adjustHeap(nums, 0, i, isMinHeap);
        }
    }
    /*
    3              5
  2   1         6     3
 5 6 4        4   2  1
     */

    private void adjustHeap(int[] nums, int i, int len, boolean isMinHeap) {
        int temp = nums[i];
        for (int j = 2 * i + 1; j < len; j = 2 * j + 1) {
            if (isMinHeap) {
                if (j + 1 < len && nums[j] > nums[j + 1]) {
                    j++; // 选择小的
                }
                if (nums[j] < temp) {
                    // swap(nums, i, j);
                    nums[i] = nums[j];
                    i = j;
                } else {
                    break;
                }
            } else {
                if (j + 1 < len && nums[j] < nums[j + 1]) {
                    j++; // 选择大的
                }
                if (nums[j] > temp) {
                    // swap(nums, i, j);
                    nums[i] = nums[j];
                    i = j;
                } else {
                    break;
                }
            }
        }
        nums[i] = temp;
    }

    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}
