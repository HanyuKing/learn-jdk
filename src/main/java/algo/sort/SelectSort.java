package algo.sort;

/**
 * @Author Hanyu.Wang
 * @Date 2024/10/12 11:09
 * @Description
 * @Version 1.0
 **/
public class SelectSort {
    public static void main(String[] args) {
        SelectSort selectSort = new SelectSort();
        int[] array = new int[] {5,1,1,2,0,0}; // 5,1,1,2,0,0
        for (int n : selectSort.selectSort(array)) {
            System.out.print(n + " ");
        }
        System.out.println();
    }

    public int[] selectSort(int[] array) {
        for (int i = 0; i < array.length; i++) {
            int minIndex = i;
            for (int j = i + 1; j < array.length; j++) {
                if (array[j] < array[minIndex]) {
                    minIndex = j;
                }
            }
            swap(array, i, minIndex);
        }
        return array;
    }

    private void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
