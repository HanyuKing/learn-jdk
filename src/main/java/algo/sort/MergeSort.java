package algo.sort;

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
        int[] temp = new int[array.length];
        doMergeSort(array, temp, 0, array.length - 1);
    }
    private void doMergeSort(int[] array, int[] temp, int l, int r) {
        if(l < r) {
            int mid = l + (r - l) / 2;
            doMergeSort(array, temp, l, mid);
            doMergeSort(array, temp, mid + 1, r);
            merge(array, temp, l, mid, r);
        }
    }

    private void merge(int[] array, int[] temp, int l, int mid, int r) {
        int i = l;
        int j = mid + 1;
        int tempIndex = 0;
        while (i <= mid && j <= r) {
            if (array[i] > array[j]) {
                temp[tempIndex++] = array[j++];
            } else {
                temp[tempIndex++] = array[i++];
            }
        }
        while (i <= mid) {
            temp[tempIndex++] = array[i++];
        }
        while (j <= r) {
            temp[tempIndex++] = array[j++];
        }
        System.arraycopy(temp, 0, array, l, r - l + 1);
//        for (int k = l; k <= r; k++) {
//            array[k] = temp[k - l];
//        }
    }
}
