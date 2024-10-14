package algo.leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2022/8/17 11:21
 * @Description
 * @Version 1.0
 **/
public class P1302 {
    public static void main(String[] args) {

    }
}

//给你一棵二叉树的根节点 root ，请你返回 层数最深的叶子节点的和 。
//
//
//
// 示例 1：
//
//
//
//
//输入：root = [1,2,3,4,5,null,6,7,null,null,null,null,8]
//输出：15
//
//
// 示例 2：
//
//
//输入：root = [6,7,8,2,7,1,3,9,null,1,4,null,null,null,5]
//输出：19
//
//
//
//
// 提示：
//
//
// 树中节点数目在范围 [1, 10⁴] 之间。
// 1 <= Node.val <= 100
//
//
// Related Topics 树 深度优先搜索 广度优先搜索 二叉树 👍 112 👎 0


//leetcode submit region begin(Prohibit modification and deletion)

class Solution1302 {
    public int deepestLeavesSum(TreeNode root) {
        deepestLeavesSum(root, 0);
        return sum;
    }

    private int maxDeep = 0;
    private int sum = 0;

    private void deepestLeavesSum(TreeNode root, int deep) {
        if (root == null) {
            return ;
        }
        if (root.left == null && root.right == null) {
            // leaf
            if (deep > maxDeep) {
                maxDeep = deep;
                sum = root.val;
            } else if (deep == maxDeep) {
                sum = sum + root.val;
            } else {
                return ;
            }
        }
        deepestLeavesSum(root.left, deep + 1);
        deepestLeavesSum(root.right, deep + 1);
        return ;
    }

    private class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
}
//leetcode submit region end(Prohibit modification and deletion)
