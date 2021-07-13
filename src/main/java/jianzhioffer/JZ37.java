package jianzhioffer;

public class JZ37 {
    public static void main(String[] args) {
        System.out.println(new JZ37().GetNumberOfK(new int[]{1,3,3,3,3,4,5}, 2));
    }
    public int GetNumberOfK(int [] array , int k) {
        if (array == null || array.length == 0) return 0;

        int lower = 0, upper = array.length - 1;
        int low;

        // find lower
        while (lower < upper) {
            int mid = (lower + upper) / 2;
            if (array[mid] == k) {
                upper = mid;
            } else if (array[mid] > k) {
                upper = mid - 1;
            } else {
                lower = mid + 1;
            }
        }
        low = lower;
        // find upper
        lower = 0;
        upper = array.length - 1;
        while (lower < upper) {
            int mid = (lower + upper) / 2;
            if (array[mid] == k) {
                if (lower + 1 == upper) {
                    lower = mid + 1;
                } else {
                    lower = mid;
                }
            } else if (array[mid] > k) {
                upper = mid - 1;
            } else {
                lower = mid + 1;
            }
        }

        if (upper == low) {
            if (array[upper] == k) {
                return 1;
            } else {
                return 0;
            }
        }
        return upper - low + 1;
    }
}
