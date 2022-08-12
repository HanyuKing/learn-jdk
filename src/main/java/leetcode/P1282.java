package leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Hanyu.Wang
 * @Date 2022/8/12 10:32
 * @Description
 * @Version 1.0
 **/
public class P1282 {
    public static void main(String[] args) {
        System.out.println(new SolutionP1282().groupThePeople(new int[]{2,1,3,3,3,2}));
    }
}

//有 n 个人被分成数量未知的组。每个人都被标记为一个从 0 到 n - 1 的唯一ID 。
//
// 给定一个整数数组 groupSizes ，其中
// groupSizes[i] 是第 i 个人所在的组的大小。例如，如果 groupSizes[1] = 3 ，则第 1 个人必须位于大小为 3 的组中。
//
// 返回一个组列表，使每个人 i 都在一个大小为
// groupSizes[i] 的组中。
//
// 每个人应该 恰好只 出现在 一个组 中，并且每个人必须在一个组中。如果有多个答案，返回其中 任何 一个。可以 保证 给定输入 至少有一个 有效的解。
//
//
//
// 示例 1：
//
//
//输入：groupSizes = [3,3,3,3,3,1,3]
//输出：[[5],[0,1,2],[3,4,6]]
//解释：
//第一组是 [5]，大小为 1，groupSizes[5] = 1。
//第二组是 [0,1,2]，大小为 3，groupSizes[0] = groupSizes[1] = groupSizes[2] = 3。
//第三组是 [3,4,6]，大小为 3，groupSizes[3] = groupSizes[4] = groupSizes[6] = 3。
//其他可能的解决方案有 [[2,1,6],[5],[0,4,3]] 和 [[5],[0,6,2],[4,3,1]]。
//
//
// 示例 2：
//
//
//输入：groupSizes = [2,1,3,3,3,2]
//输出：[[1],[0,5],[2,3,4]]
//
//
//
//
// 提示：
//
//
// groupSizes.length == n
// 1 <= n <= 500
// 1 <= groupSizes[i] <= n
//
//
// Related Topics 数组 哈希表 👍 82 👎 0


//leetcode submit region begin(Prohibit modification and deletion)
class SolutionP1282 {
    public List<List<Integer>> groupThePeople(int[] groupSizes) {
        Map<Integer, List<Integer>> groupData = new HashMap<>();
        for (int i = 0; i < groupSizes.length; i++) {
            List<Integer> group = groupData.getOrDefault(Integer.valueOf(groupSizes[i]), new ArrayList<>());
            group.add(i);
            groupData.put(Integer.valueOf(groupSizes[i]), group);
        }
        List<List<Integer>> result = new ArrayList<>();
        for (Map.Entry<Integer, List<Integer>> entry : groupData.entrySet()) {
            Integer size = entry.getKey();
            int count = 0;
            List<Integer> data = entry.getValue();
            List<Integer> gData = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                gData.add(data.get(i));
                if (++count % size == 0) {
                    result.add(gData);
                    gData = new ArrayList<>();
                }
            }
        }
        return result;
    }
}
//leetcode submit region end(Prohibit modification and deletion)

