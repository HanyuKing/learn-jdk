package algo.sort;

/**
 * @Author Hanyu.Wang
 * @Date 2024/6/28 16:28
 * @Description
 * @Version 1.0
 **/
public class HeapSort {
    public static void main(String[] args) {
        HeapSort heapSort = new HeapSort();
        int[] array = new int[] {5,1,1,2,0,0}; // 5,1,1,2,0,0

        heapSort.heapSort(array, false);

        for (int n : array) {
            System.out.print(n + " ");
        }
        System.out.println();
    }
    public void heapSort(int[] nums, boolean isMinHeap) {
        for (int i = nums.length - 1; i >= 0; i--) {
            // adjustHeap(nums, i, nums.length, isMinHeap);
            // adjust(nums, i, nums.length, isMinHeap);
            adjust2(nums, i, nums.length, isMinHeap);
        }
        for (int i = nums.length - 1; i > 0; i--) {
            swap(nums, 0, i);
            // adjustHeap(nums, 0, i, isMinHeap);
            // adjust(nums, 0, i, isMinHeap);
            adjust2(nums, 0, i, isMinHeap);
        }
    }

    private void adjust2(int[] nums, int i, int len, boolean isMinHeap) {
        int parentIndex = i;
        int temp = nums[parentIndex];
        for (int j = 2 * i + 1; j < len; j = 2 * j + 1) {
            int rightChildIndex = j + 1;

            if (isMinHeap) {
                int minIndex = j;
                if (rightChildIndex < len && nums[rightChildIndex] < nums[minIndex]) {
                    minIndex = rightChildIndex;
                }
                if (nums[minIndex] < temp) {
                    // swap(nums, minIndex, parentIndex);
                    nums[parentIndex] = nums[minIndex];
                    parentIndex = minIndex;
                } else {
                    break;
                }
            } else {
                int maxIndex = j;
                if (rightChildIndex < len && nums[rightChildIndex] > nums[maxIndex]) {
                    maxIndex = rightChildIndex;
                    j = maxIndex;
                }
                if (nums[maxIndex] > temp) {
                    // swap(nums, maxIndex, parentIndex);
                    nums[parentIndex] = nums[maxIndex];
                    parentIndex = maxIndex;
                } else {
                    break;
                }
            }

        }
        nums[parentIndex] = temp;
    }

    private void adjust(int[] nums, int i, int len, boolean isMinHeap) {
        int parentIndex = i;
        //int temp = nums[parentIndex];
        for (int j = 2 * i + 1; j < len; j = 2 * j + 1) {
            int rightChildIndex = j + 1;

            if (isMinHeap) {
                int minIndex = j;
                if (rightChildIndex < len && nums[rightChildIndex] < nums[minIndex]) {
                    minIndex = rightChildIndex;
                }
                if (nums[minIndex] < nums[parentIndex]) {
                    swap(nums, minIndex, parentIndex);
                    parentIndex = minIndex;
                } else {
                    break;
                }
            } else {
                int maxIndex = j;
                if (rightChildIndex < len && nums[rightChildIndex] > nums[maxIndex]) {
                    maxIndex = rightChildIndex;
                    j = maxIndex;
                }
                if (nums[maxIndex] > nums[parentIndex]) {
                    swap(nums, maxIndex, parentIndex);
                    parentIndex = maxIndex;
                } else {
                    break;
                }
            }

        }
        //nums[parentIndex] = temp;
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
