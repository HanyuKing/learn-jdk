package algo.leetcode;

import java.util.*;

/**
 * @Author Hanyu.Wang
 * @Date 2022/8/25 10:56
 * @Description
 * @Version 1.0
 **/
public class P658 {
    public static void main(String[] args) {
        System.out.println(new Solution658().findClosestElements(
                new int[]{1,10,15,25,35,45,50,59}, 1, 30) // [3,3,4]
        );
    }
}

//给定一个 排序好 的数组 arr ，两个整数 k 和 x ，从数组中找到最靠近 x（两数之差最小）的 k 个数。返回的结果必须要是按升序排好的。
//
// 整数 a 比整数 b 更接近 x 需要满足：
//
//
// |a - x| < |b - x| 或者
// |a - x| == |b - x| 且 a < b
//
//
//
//
// 示例 1：
//
//
//输入：arr = [1,2,3,4,5], k = 4, x = 3
//输出：[1,2,3,4]
//
//
// 示例 2：
//
//
//输入：arr = [1,2,3,4,5], k = 4, x = -1
//输出：[1,2,3,4]
//
//
//
//
// 提示：
//
//
// 1 <= k <= arr.length
// 1 <= arr.length <= 10⁴
//
// arr 按 升序 排列
// -10⁴ <= arr[i], x <= 10⁴
//
//
// Related Topics 数组 双指针 二分查找 排序 堆（优先队列） 👍 383 👎 0


//leetcode submit region begin(Prohibit modification and deletion)
class Solution658 {
    public List<Integer> findClosestElements(int[] arr, int k, int x) {
        List<Integer> result = new ArrayList<>();
        int low = 0;
        int height = arr.length - 1;
        int closeXIndex = -1;
        while (low <= height) {
            int mid = low + (height - low) / 2;
            if (mid == low) {
                low = mid - 1 < 0 ? 0 : mid - 1;
                height = mid + 1 == arr.length ? arr.length - 1 : mid + 1;
                int a = Math.abs(arr[low] - x);
                int b = Math.abs(arr[mid] - x);
                int c = Math.abs(arr[height] - x);
                int min = Math.min(a, Math.min(b, c));
                if (min == a) {
                    closeXIndex = low;
                } else if (min == b) {
                    closeXIndex = mid;
                } else {
                    closeXIndex = height;
                }
                break;
            }
            if (arr[mid] == x) {
                closeXIndex = mid;
                break;
            } else if (arr[mid] > x) {
                height = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        int leftIndex = closeXIndex - 1;
        int rightIndex = closeXIndex + 1;
        int count = 0;

        Set<Integer> sortedIndex = new TreeSet<>();
        sortedIndex.add(closeXIndex);

        while (count < k - 1) {
            int leftAbsX = Integer.MAX_VALUE;
            if (leftIndex >= 0) {
                leftAbsX = Math.abs(arr[leftIndex] - x);
            }
            int rightAbsX = Integer.MAX_VALUE;
            if (rightIndex < arr.length) {
                rightAbsX = Math.abs(arr[rightIndex] - x);
            }

            if (leftAbsX <= rightAbsX) {
                sortedIndex.add(leftIndex);
                leftIndex--;
            } else {
                sortedIndex.add(rightIndex);
                rightIndex++;
            }
            count++;
        }

        for (Iterator<Integer> it = sortedIndex.iterator(); it.hasNext(); ) {
            Integer index = it.next();
            result.add(arr[index]);
        }
        return result;
    }
}
//leetcode submit region end(Prohibit modification and deletion)

