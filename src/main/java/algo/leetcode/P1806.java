package algo.leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2023/1/8 22:01
 * @Description
 * @Version 1.0
 **/
public class P1806 {
    public static void main(String[] args) {
        System.out.println(new Solution().reinitializePermutation(4));
    }

    static class Solution {
        public int reinitializePermutation(int n) {
            int[] arr = new int[n];
            int[] arrMap = new int[n];
            for (int i = 0; i < n; i++) {
                if ((i & 1) == 0) {
                    arr[i] = i / 2;
                } else {
                    arr[i] = n / 2 + (i - 1) / 2;
                }
            }
            for (int i = 0; i < n; i++) {
                arrMap[arr[i]] = i;
            }

            int cnt = 1;

            int first = n - 2;
            for (int i = first;;) {
                int rawIndex = arrMap[i];

                if (rawIndex == first) {
                    break;
                }
                int temp = arr[i];
                arr[i] = arr[rawIndex];
                arr[rawIndex] = temp;
                cnt++;
                i = rawIndex;
            }

            return cnt;
        }
    }
}
