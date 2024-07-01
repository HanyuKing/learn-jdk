package leetcode.hot100;

import java.util.Arrays;
import java.util.Random;

public class QuickSort {
    public static void main(String[] args) {
        int[] nums = new int[] {5,1,1,2,0,0};
        QuickSort q = new QuickSort();
        q.quicksort3(nums, 0, nums.length - 1);

        for (int i = 0; i < nums.length; i++) {
            System.out.print(nums[i] + " ");
        }
        System.out.println();
    }

    public void quicksort3(int[] nums, int l, int r) {
        if (l < r) {
            int mid = partition3(nums, l, r);
            quicksort3(nums, l, mid - 1);
            quicksort3(nums, mid + 1, r);
        }
    }

    public int partition3(int[] nums, int low, int high) {
        int base = nums[low];
        int i = low;
        int j = high;
        while (i < j) {
            while (i < j && nums[j] >= base) {
                j--;
            }
            while (i < j && nums[i] <= base) {
                i++;
            }
            swap(nums, i, j);
        }
        swap(nums, low, i);
        return i;
    }

    Random random = new Random();
    public int quicksort2(int[] nums, int l, int r) {
        int i = l;
        int j = r;
        int rIndex = l + random.nextInt(Math.max(r - l, 1));
        swap(nums, rIndex, l);

        int base = nums[l];

        while (i < j) {
            while (i < j && nums[j] >= base) {
                j--;
            }
            if (i < j) {
                nums[i] = nums[j];
                i++;
            }
            while (i < j && nums[i] <= base) {
                i++;
            }
            if (i < j) {
                nums[j] = nums[i];
                j--;
            }
        }
        nums[i] = base;
        if (l < i) quicksort2(nums, l, i - 1);
        if (i < r) quicksort2(nums, j + 1, r);
        return i;
    }

    public void quicksort(int[] nums, int l, int r) {
        if (l < r) {
            int mid = partition(nums, l, r);
            quicksort(nums, l, mid - 1);
            quicksort(nums, mid + 1, r);
        }
    }
    public int partition(int[] nums, int low, int high) {
        int rIndex = low + random.nextInt(high - low + 1);
        swap(nums, low, rIndex);
        int pivot = nums[low];//选第一个元素作为枢纽元

        while(low < high)
        {
            while(low < high && nums[high] >= pivot)high--;
            nums[low] = nums[high];//从后面开始找到第一个小于pivot的元素，放到low位置
            while(low < high && nums[low] <= pivot)low++;
            nums[high] = nums[low];//从前面开始找到第一个大于pivot的元素，放到high位置
        }
        nums[low] = pivot;//最后枢纽元放到low的位置
        return low;
    }

    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}
