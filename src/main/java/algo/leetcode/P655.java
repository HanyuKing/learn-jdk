package algo.leetcode;

import java.util.*;

/**
 * @Author Hanyu.Wang
 * @Date 2022/8/22 15:40
 * @Description
 * @Version 1.0
 **/
public class P655 {
    public static void main(String[] args) {
        // [1,2]
        // [1,2,3,null,4]
//        TreeNode root = new TreeNode(1);
//        root.left = new TreeNode(2);
        TreeNode root1 = new TreeNode(1);
        TreeNode root2 = new TreeNode(2);
        TreeNode root3 = new TreeNode(3);
        TreeNode root4 = new TreeNode(4);
        root1.left = root2;
        root1.right = root3;
        root2.right = root4;
        System.out.println(new Solution655().printTree(root1));
    }
}
//给你一棵二叉树的根节点 root ，请你构造一个下标从 0 开始、大小为 m x n 的字符串矩阵 res ，用以表示树的 格式化布局 。构造此格式化布局矩
//阵需要遵循以下规则：
//
//
// 树的 高度 为 height ，矩阵的行数 m 应该等于 height + 1 。
// 矩阵的列数 n 应该等于 2ʰᵉⁱᵍʰᵗ⁺¹ - 1 。
// 根节点 需要放置在 顶行 的 正中间 ，对应位置为 res[0][(n-1)/2] 。
// 对于放置在矩阵中的每个节点，设对应位置为 res[r][c] ，将其左子节点放置在 res[r+1][c-2ʰᵉⁱᵍʰᵗ⁻ʳ⁻¹] ，右子节点放置在
//res[r+1][c+2ʰᵉⁱᵍʰᵗ⁻ʳ⁻¹] 。
// 继续这一过程，直到树中的所有节点都妥善放置。
// 任意空单元格都应该包含空字符串 "" 。
//
//
// 返回构造得到的矩阵 res 。
//
//
//
//
//
// 示例 1：
//
//
//输入：root = [1,2]
//输出：
//[["","1",""],
// ["2","",""]]
//
//
// 示例 2：
//
//
//输入：root = [1,2,3,null,4]
//输出：
//[["","","","1","","",""],
// ["","2","","","","3",""],
// ["","","4","","","",""]]
//
//
//
//
// 提示：
//
//
// 树中节点数在范围 [1, 2¹⁰] 内
// -99 <= Node.val <= 99
// 树的深度在范围 [1, 10] 内
//
//
// Related Topics 树 深度优先搜索 广度优先搜索 二叉树 👍 172 👎 0


//leetcode submit region begin(Prohibit modification and deletion)

class Solution655 {
    public List<List<String>> printTree(TreeNode root) {
        int height = this.getHeight(root);
        height = height - 1;

        int n = (int) Math.pow(2, (height + 1)) - 1;
        String[][] res = new String[height + 1][n];
        Queue<TreeNode> queue = new LinkedList<>();
        Queue<Integer> columnQueue = new LinkedList<>();
        queue.add(root);
        columnQueue.add((n - 1) / 2);
        res[0][(n - 1) / 2] = String.valueOf(root.val);
        int layer = 0;
        int layerCount = 1;
        while (!queue.isEmpty()) {
            int nextLayerCount = 0;
            while (--layerCount >= 0) {
                TreeNode node = queue.poll();
                Integer column = columnQueue.poll();
                int row = layer;
                if (node.left != null) {
                    int c = column - (int) Math.pow(2,(height - row - 1));
                    queue.add(node.left);
                    columnQueue.add(c);
                    res[row + 1][c] = String.valueOf(node.left.val);
                    nextLayerCount++;
                }
                if (node.right != null) {
                    int c = column + (int) Math.pow(2, (height - row - 1));
                    queue.add(node.right);
                    columnQueue.add(c);
                    res[row + 1][c] = String.valueOf(node.right.val);
                    nextLayerCount++;
                }
            }
            layerCount = nextLayerCount;
            layer++;
        }
        List<List<String>> result = new ArrayList<>();
        for (int i = 0; i < res.length; i++) {
            List<String> subList = new ArrayList<>();
            for (int j = 0; j < res[i].length; j++) {
                subList.add(res[i][j] == null ? "" : res[i][j]);
            }
            result.add(subList);
        }
        return result;
    }

    private int getHeight(TreeNode root) { if (root == null) { return 0; } return Math.max(getHeight(root.left), getHeight(root.right)) + 1; }

}
//leetcode submit region end(Prohibit modification and deletion)

