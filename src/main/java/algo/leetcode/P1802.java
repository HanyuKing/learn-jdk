package algo.leetcode;

import java.math.BigDecimal;

/**
 * @Author Hanyu.Wang
 * @Date 2023/1/4 10:47
 * @Description
 * @Version 1.0
 **/
public class P1802 {
    public static void main(String[] args) {
        System.out.println(new Solution1802().maxValue(6, 2, 931384943));
    }
    //给你三个正整数 n、index 和 maxSum 。你需要构造一个同时满足下述所有条件的数组 nums（下标 从 0 开始 计数）：
//
//
// nums.length == n
// nums[i] 是 正整数 ，其中 0 <= i < n
// abs(nums[i] - nums[i+1]) <= 1 ，其中 0 <= i < n-1
// nums 中所有元素之和不超过 maxSum
// nums[index] 的值被 最大化
//
//
// 返回你所构造的数组中的 nums[index] 。
//
// 注意：abs(x) 等于 x 的前提是 x >= 0 ；否则，abs(x) 等于 -x 。
//
//
//
// 示例 1：
//
// 输入：n = 4, index = 2,  maxSum = 6
//输出：2
//解释：数组 [1,1,2,1] 和 [1,2,2,1] 满足所有条件。不存在其他在指定下标处具有更大值的有效数组。
//
//
// 示例 2：
//
// 输入：n = 6, index = 1,  maxSum = 10
    /*
        [1, 1, 1, 1, 1, 1]
        [2, 2, 2, 1, 1, 1]
        [2, 3, 2, 1, 1, 1]
     */
//输出：3
//
//
//
//
// 提示：
//
//
// 1 <= n <= maxSum <= 10⁹
// 0 <= index < n
//
//
// Related Topics 贪心 二分查找 👍 78 👎 0


    //leetcode submit region begin(Prohibit modification and deletion)
    static class Solution1802 {
        public int maxValue(int n, int index, int maxSum) {
            int low = 1;
            int height = maxSum;
            int result = 1;
            while (low <= height) {
                int mid = low + (height - low) / 2;
                if (isValid(n, index, mid, maxSum)) {
                    result = mid;
                    low = mid + 1;
                } else {
                    height = mid - 1;
                }
            }
            return result;
        }

        private boolean isValid(int n, int index, int maxValue, int maxSum) {
            long sum = maxValue;

            // [1, 1, 2, 3, 4, 3, 2]
            // [1, 2, 3, 4, 3, 2]
            // [2, 3, 4, 3]
            // [2, 3, 4]
            long leftSum = 0;
            if (index > 0) {
                if (maxValue - 1 <= index) {
                    long a1 = maxValue - 1;
                    long an = 1;
                    leftSum = (maxValue - 1) * (a1 + an) / 2;
                    leftSum += index - maxValue + 1;
                } else {
                    long a1 = maxValue - 1;
                    long an = maxValue - index;
                    leftSum = index * (a1 + an) / 2;
                }
            }

            // [3, 4, 3, 2, 1, 1, 1]
            // [3, 4, 3, 2]
            // [3, 4, 3]

            // [1, 1, 1, 1]
            // [2, 1, 1, 0] x
            long rightSum = 0;
            if (index < n - 1) {
                if (maxValue >= n - index) {
                    long a1 = maxValue - 1;
                    long an = maxValue - (n - 1 - index);
                    rightSum = (n - 1 - index) * (a1 + an) / 2;
                } else {
                    long a1 = maxValue - 1;
                    long an = 1;
                    rightSum = (maxValue - 1) * (a1 + an) / 2;
                    rightSum += n - index - maxValue;
                }
            }

            sum += leftSum + rightSum;

            return sum <= maxSum;
        }

        private boolean isValid2(int n, int index, int maxValue, int maxSum) {
            BigDecimal sum = new BigDecimal(maxValue);

            // [1, 1, 2, 3, 4, 3, 2]
            // [1, 2, 3, 4, 3, 2]
            // [2, 3, 4, 3]
            // [2, 3, 4]
            BigDecimal leftSum = new BigDecimal(0);
            if (index > 0) {
                if (maxValue - 1 <= index) {
                    int a1 = maxValue - 1;
                    int an = 1;
                    leftSum = new BigDecimal(maxValue - 1).multiply(new BigDecimal(a1 + an)).divide(new BigDecimal(2));
                    leftSum = leftSum.add(new BigDecimal(index - maxValue + 1));
                } else {
                    int a1 = maxValue - 1;
                    int an = maxValue - index;
                    leftSum = new BigDecimal(index).multiply(new BigDecimal(a1 + an)).divide(new BigDecimal(2));
                }
            }

            // [3, 4, 3, 2, 1, 1, 1]
            // [3, 4, 3, 2]
            // [3, 4, 3]

            // [1, 1, 1, 1]
            // [2, 1, 1, 0] x
            BigDecimal rightSum = new BigDecimal(0);
            if (index < n - 1) {
                if (maxValue >= n - index) {
                    int a1 = maxValue - 1;
                    int an = maxValue - (n - 1 - index);
                    rightSum = new BigDecimal(n - 1 - index).multiply(new BigDecimal(a1 + an).divide(new BigDecimal(2)));
                } else {
                    int a1 = maxValue - 1;
                    int an = 1;
                    rightSum = new BigDecimal(maxValue - 1).multiply(new BigDecimal(a1 + an)).divide(new BigDecimal(2));
                    rightSum = rightSum.add(new BigDecimal(n - index - maxValue));
                }
            }

            return sum.add(leftSum).add(rightSum).compareTo(new BigDecimal(maxSum)) <= 0;
        }

        private boolean guess(long max,int index,int maxsum,int n){
            long one=0;
            long two=0;
            if(index<max){
                one=(2*max-index)*(index+1)/2;//左边不会减到0的情况
            }else{
                one=(max+1)*max/2+(index+1-max);
            }
            if((n-1-index)<max){
                two=(2*max+index+1-n)*(n-index)/2;//右边不会减到0的情况
            }else{
                two=(max+1)*max/2+(n-index-max);
            }
            long sum=one+two-max;
            return sum<=maxsum;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}
