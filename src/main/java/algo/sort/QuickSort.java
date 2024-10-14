package algo.sort;

/**
 * @Author Hanyu.Wang
 * @Date 2024/10/11 10:05
 * @Description
 * @Version 1.0
 **/
public class QuickSort {
    public static void main(String[] args) {
        QuickSort quickSort = new QuickSort();
        int[] array = new int[] {5,1,1,2,0,0}; // 5,1,1,2,0,0

        for (int n : quickSort.sortArray(array)) {
            System.out.print(n + " ");
        }
        System.out.println();
    }

    public int[] sortArray(int[] array) {
        quicksort(array, 0, array.length - 1);
        return array;
    }

    private void quicksort(int[] array, int left, int right) {
        if (left >= right) {
            return;
        }
        int p = partition2(array, left, right);
        quicksort(array, left, p - 1);
        quicksort(array, p + 1, right);
    }

    // Random r = new Random();
    private int partition(int[] array, int left, int right) {
        int base = left + (right - left) / 2;

        int temp = array[left];
        array[left] = array[base];
        array[base] = temp;

        int data = array[left];

        while (left < right) {
            while (left < right && data <= array[right]) {
                right--;
            }
            array[left] = array[right];
            while (left < right && data >= array[left]) {
                left++;
            }
            array[right] = array[left];
        }
        array[left] = data;
        return left;
    }

    private int partition2(int[] array, int left, int right) {
        int base = left + (right - left) / 2;
        swap(array, base, right);

        int pivot = array[right];
        int index = left;
        for (int i = left; i < right; i++) {
            if (array[i] < pivot) {
                swap(array, index, i);
                index++;
            }
        }
        swap(array, index, right);
        return index;
    }

    private static int partition3(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        return i + 1;
    }

    private void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
