package leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Hanyu.Wang
 * @Date 2023/2/14 10:08
 * @Description
 * @Version 1.0
 **/
public class P1124 {
    public static void main(String[] args) {
        System.out.println(new Solution1124().longestWPI(new int[]{9,9,6,0,6,6,9}));
    }

    //给你一份工作时间表 hours，上面记录着某一位员工每天的工作小时数。
//
// 我们认为当员工一天中的工作小时数大于 8 小时的时候，那么这一天就是「劳累的一天」。
//
// 所谓「表现良好的时间段」，意味在这段时间内，「劳累的天数」是严格 大于「不劳累的天数」。
//
// 请你返回「表现良好时间段」的最大长度。
//
//
//
// 示例 1：
//
//
//输入：hours = [9,9,6,0,6,6,9]
//输入：hours = [1,1,-1,-1,-1,-1,1]
//输出：3
//解释：最长的表现良好时间段是 [9,9,6]。
//
// 示例 2：
//
//
//输入：hours = [6,6,6]
//输出：0
//
//
//
//
// 提示：
//
//
// 1 <= hours.length <= 10⁴
// 0 <= hours[i] <= 16
//
//
// Related Topics 栈 数组 哈希表 前缀和 单调栈 👍 296 👎 0


    //leetcode submit region begin(Prohibit modification and deletion)
    static class Solution1124 {
        public int longestWPI(int[] hours) {
            int ans = 0, s = 0;
            Map<Integer, Integer> firstSumPos = new HashMap<>();
            for (int i = 0; i < hours.length; i++) {
                s += hours[i] > 8 ? 1 : -1;
                if (s > 0) {
                    ans = i + 1;
                } else if (firstSumPos.containsKey(s - 1)) {
                    ans = Math.max(i - firstSumPos.get(s - 1), ans);
                }
                firstSumPos.putIfAbsent(s, i);
            }
            return ans;
        }
    }
}
