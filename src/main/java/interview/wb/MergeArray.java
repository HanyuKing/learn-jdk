package interview.wb;

import org.junit.Test;

public class MergeArray {
    /**
     * [1,2,3]
     * [2,3,4,5]
     * -------
     * [1,2,2,3,3,4,5]
     *
     */

    @Test
    public void test1() {
        int[] array1 = new int[] {1,2,3};
        int[] array2 = new int[] {2,3,4,5};

        print(mergeSortedArray(array1, array2));
    }

    public void print(int[] array) {
        for (int n : array) {
            System.out.print(n + " ");
        }
        System.out.println();
    }

    public int[] mergeSortedArray(int[] array1, int[] array2) {
        int length1 = array1.length;
        int length2 = array2.length;
        int[] result = new int[length1 + length2];
        int index = 0;

        int index1 = 0;
        int index2 = 0;
        while (index1 < length1 || index2 < length2) {
            if (index1 < length1 && index2 < length2) {
                if (array1[index1] <= array2[index2]) {
                    result[index++] = array1[index1];
                    index1++;
                } else {
                    result[index++] = array2[index2];
                    index2++;
                }
            } else if (index1 < length1) {
                result[index++] = array1[index1++];
            } else if (index2 < length2) {
                result[index++] = array2[index2++];
            }
        }

        return result;
    }
}
