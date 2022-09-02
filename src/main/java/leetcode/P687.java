package leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Hanyu.Wang
 * @Date 2022/9/2 11:20
 * @Description
 * @Version 1.0
 **/
public class P687 {
    public static void main(String[] args) {

    }
}

//给定一个二叉树的
// root ，返回 最长的路径的长度 ，这个路径中的 每个节点具有相同值 。 这条路径可以经过也可以不经过根节点。
//
// 两个节点之间的路径长度 由它们之间的边数表示。
//
//
//
// 示例 1:
//
//
//
//
//输入：root = [5,4,5,1,1,5]
//输出：2
//
//
// 示例 2:
//
//
//
//
//输入：root = [1,4,5,4,4,5]
//输出：2
//
//
//
//
// 提示:
//
//
// 树的节点数的范围是
// [0, 10⁴]
// -1000 <= Node.val <= 1000
// 树的深度将不超过 1000
//
//
// Related Topics 树 深度优先搜索 二叉树 👍 641 👎 0


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

/**
 * [1,null,1,1,1,1,1,1]
 *           1
 *                 1
 *             1         1
 *          1     1    1
 */
class Solution687 {
    private int maxPath = 0;

    public int longestUnivaluePath(TreeNode root) {
        findMaxPath(root);
        return maxPath;
    }

    private int findMaxPath(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int leftMaxPath = 0;
        if (root.left != null) {
            leftMaxPath = findMaxPath(root.left);
            if (root.val == root.left.val) {
                leftMaxPath++;
            }
        }
        int rightMaxPath = 0;
        if (root.right != null) {
            rightMaxPath = findMaxPath(root.right);
            if (root.val == root.right.val) {
                rightMaxPath++;
            }
        }

        if (root.left != null && root.right != null
                && root.left.val == root.val && root.right.val == root.val) {
            maxPath = Math.max(leftMaxPath + rightMaxPath, maxPath);
        } else {
            maxPath = Math.max(maxPath, Math.max(leftMaxPath, rightMaxPath));
        }

        int containRootMaxPath = 0;
        if (root.left != null && root.left.val == root.val) {
            containRootMaxPath = leftMaxPath;
        }
        if (root.right != null && root.right.val == root.val) {
            containRootMaxPath = Math.max(containRootMaxPath, rightMaxPath);
        }

        return containRootMaxPath;
    }
}
//leetcode submit region end(Prohibit modification and deletion)

