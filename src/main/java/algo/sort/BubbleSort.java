package algo.sort;

/**
 * @Author Hanyu.Wang
 * @Date 2024/10/12 11:08
 * @Description
 * @Version 1.0
 **/
public class BubbleSort {
    public static void main(String[] args) {
        BubbleSort bubbleSort = new BubbleSort();
        int[] array = new int[] {5,1,1,2,0,0}; // 5,1,1,2,0,0

        for (int n :  bubbleSort.bubbleSort(array)) {
            System.out.print(n + " ");
        }
        System.out.println();
    }

    public int[] bubbleSort(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    swap(array, j, j + 1);
                }
            }
        }
        return array;
    }

    private void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

}
