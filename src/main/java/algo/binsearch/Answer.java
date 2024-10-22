package algo.binsearch;

import algo.hot200.Base;
import org.junit.Test;

public class Answer extends Base {

    @Test
    public void testP153() {
        int[] nums = new int[] {3,4,5,1,2};
        print(findMin(nums)); // 1
    }

    public int findMin(int[] nums) {
        int low = 0;
        int high = nums.length - 1;
        int min = nums[0];

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int midValue = nums[mid];
            if (midValue >= nums[0]) {
                low = mid + 1;
                min = Math.min(min, midValue);
            } else {
                high = mid - 1;
                min = Math.min(min, midValue);
            }
        }

        return min;
    }

    @Test
    public void testP33() {
        int[] nums = new int[] {4,5,6,7,0,1,2};
        int target = 0;
        print(search(nums, target)); // 4

        nums = new int[] {3, 1};
        target = 1;
        print(search(nums, target)); // 1

        nums = new int[] {3, 5, 1};
        target = 3;
        print(search(nums, target)); // 1
    }

    public int search(int[] nums, int target) {
        int low = 0;
        int high = nums.length - 1;
        int firstValue = nums[0];
        int lastValue = nums[nums.length - 1];

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int midValue = nums[mid];
            if (midValue == target) {
                return mid;
            } else if (target > midValue) {
                boolean isHighPart = midValue >= firstValue && midValue >= lastValue;
                if (isHighPart) {
                    low = mid + 1;
                } else {
                    if (target > lastValue) {
                        high = mid - 1;
                    } else {
                        low = mid + 1;
                    }
                }
            } else {
                boolean isHighPart = midValue >= firstValue && midValue >= lastValue;
                if (isHighPart) {
                    if (target >= firstValue) {
                        high = mid - 1;
                    } else {
                        low = mid + 1;
                    }
                } else {
                    high = mid - 1;
                }
            }
        }

        return -1;
    }

    @Test
    public void testP34() {
        int[] nums = new int[] {5,7,7,8,8,10};
        int target = 1;
        print(searchRange(nums, target));

    }

    public int[] searchRange(int[] nums, int target) {
        return new int[] {
                searchRange(nums, target, false),
                searchRange(nums, target, true)
        };
    }

    public int searchRange(int[] nums, int target, boolean isRight) {
        int low = 0;
        int high = nums.length - 1;
        int ret = -1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            int midValue = nums[mid];
            if (target == midValue) {
                ret = mid;
                if (isRight) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            } else if (target < midValue) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return ret;
    }

    @Test
    public void testP74() {
        // searchMatrix
    }

    public boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length;
        int n = matrix[0].length;
        int low = 0;
        int high = m * n  - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            int midValue = matrix[mid / n][mid % n];
            if (target == midValue) {
                return true;
            } else if (target < midValue) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return false;
    }

    @Test
    public void testP35() {
        int[] nums = new int[] {1,3,5,6};
        int target = 4;
        print(searchInsert(nums, target));
    }

    public int searchInsert(int[] nums, int target) {
        int low = 0;
        int high = nums.length - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (target == nums[mid]) {
                return mid;
            } else if (target > nums[mid]) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return low;
    }
}
