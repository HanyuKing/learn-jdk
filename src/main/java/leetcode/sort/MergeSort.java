package leetcode.sort;

/**
 * @Author Hanyu.Wang
 * @Date 2024/10/11 19:27
 * @Description
 * @Version 1.0
 **/
public class MergeSort {
    public static void main(String[] args) {
        MergeSort mergeSort = new MergeSort();
        int[] array = new int[] {5,1,1,2,0,0}; // 5,1,1,2,0,0

        mergeSort.mergeSort(array);

        for (int n : array) {
            System.out.print(n + " ");
        }
        System.out.println();
    }

    public void mergeSort(int[] array) {
        if (array == null || array.length == 1) {
            return;
        }
        doMergeSort(array, 0, array.length);
    }
    private void doMergeSort(int[] array, int l, int r) {
        if(l >= r) {
            return;
        } else if (r - l == 1) {
            if (array[l] > array[r]) {
                swap(array, l, r);
            }
        } else {
            int mid = l + (r - l) / 2;
            doMergeSort(array, l, mid);
            doMergeSort(array, mid + 1, r);
            merge(array, l, mid, r);
        }
    }

    private void merge(int[] array, int l, int mid, int r) {
        // todo
    }

    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}
