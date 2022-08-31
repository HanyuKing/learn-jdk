package leetcode;

public class P998 {
    public static void main(String[] args) {
        TreeNode node1 = new TreeNode(1);
        TreeNode node2 = new TreeNode(2);
        TreeNode node3 = new TreeNode(3);
        TreeNode node4 = new TreeNode(4);
        TreeNode node6 = new TreeNode(6);
        TreeNode node7 = new TreeNode(7);
        TreeNode node8 = new TreeNode(8);
        node8.left = node7;
        node8.right = node4;
        node7.left = node1;
        node7.right = node6;
        node4.right = node3;
        node6.left = node2;

        Print.printTreeInOrder(node8);
        System.out.println();

        TreeNode newRoot = new Solution998().insertIntoMaxTree(node8, 5);

        Print.printTreeInOrder(newRoot);
        System.out.println();
    }
}

//最大树 定义：一棵树，并满足：其中每个节点的值都大于其子树中的任何其他值。
//
// 给你最大树的根节点 root 和一个整数 val 。
//
// 就像 之前的问题 那样，给定的树是利用 Construct(a) 例程从列表 a（root = Construct(a)）递归地构建的：
//
//
// 如果 a 为空，返回 null 。
// 否则，令 a[i] 作为 a 的最大元素。创建一个值为 a[i] 的根节点 root 。
// root 的左子树将被构建为 Construct([a[0], a[1], ..., a[i - 1]]) 。
// root 的右子树将被构建为 Construct([a[i + 1], a[i + 2], ..., a[a.length - 1]]) 。
// 返回 root 。
//
//
// 请注意，题目没有直接给出 a ，只是给出一个根节点 root = Construct(a) 。
//
// 假设 b 是 a 的副本，并在末尾附加值 val。题目数据保证 b 中的值互不相同。
//
// 返回 Construct(b) 。
//
//
//
// 示例 1：
//
//
//
//
//输入：root = [4,1,3,null,null,2], val = 5
//输出：[5,4,null,1,3,null,null,2]
//解释：a = [1,4,2,3], b = [1,4,2,3,5]
//
// 示例 2：
//
//
//输入：root = [5,2,4,null,1], val = 3
//输出：[5,2,4,null,1,null,3]
//解释：a = [2,1,5,4], b = [2,1,5,4,3]
//
// 示例 3：
//
//
//输入：root = [5,2,3,null,1], val = 4
//输出：[5,2,4,null,1,3]
//解释：a = [2,1,5,3], b = [2,1,5,3,4]
//
//
//
//
// 提示：
//
//
// 树中节点数目在范围 [1, 100] 内
// 1 <= Node.val <= 100
// 树中的所有值 互不相同
// 1 <= val <= 100
//
//
//
//
// Related Topics 树 二叉树 👍 137 👎 0


//leetcode submit region begin(Prohibit modification and deletion)
/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution998 {
    public TreeNode insertIntoMaxTree(TreeNode root, int val) {
        TreeNode parent = null;
        TreeNode cur = root;
        while (cur != null) {
            if (val > cur.val) {
                if (parent == null) {
                    return new TreeNode(val, root, null);
                }
                TreeNode node = new TreeNode(val, cur, null);
                parent.right = node;
                return root;
            } else {
                parent = cur;
                cur = cur.right;
            }
        }
        parent.right = new TreeNode(val);
        return root;
    }
}
//leetcode submit region end(Prohibit modification and deletion)

/**
 *                        8
 *               7                  4
 *          1        6                  3
 *                 2
 *
 *   1726843
 */
/**
 * [8,7,5,1,6,4,null,null,null,2,null,null,3]
 *                        8
 *               7                   5
 *          1        6           4
 *                 2                3
 *
 *   17268345
 */

/**
 * [8,7,4,1,6,null,5,null,null,2,null,3]
 *                        8
 *               7                   4
 *          1        6                  5
 *                 2                3
 */

