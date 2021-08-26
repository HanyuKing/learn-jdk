package leetcode;

/**
 * 124. Binary Tree Maximum Path Sum
 */

import org.apache.commons.lang3.AnnotationUtils;

/**
 * A path in a binary tree is a sequence of nodes where each pair of adjacent nodes in the sequence has an edge
 * connecting them. A node can only appear in the sequence at most once. Note that the path does not need to pass
 * through the root.
 *
 * The path sum of a path is the sum of the node's values in the path.
 *
 * Given the root of a binary tree, return the maximum path sum of any path.
 */
class TreeNode {
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

public class P124 {
    public static void main(String[] args) {
        TreeNode root5 = new TreeNode(5);
        TreeNode root4 = new TreeNode(4);
        TreeNode root8 = new TreeNode(8);
        TreeNode root11 = new TreeNode(11);
        TreeNode root13 = new TreeNode(13);
        TreeNode root44 = new TreeNode(4);
        TreeNode root7 = new TreeNode(7);
        TreeNode root2 = new TreeNode(2);
        TreeNode root1 = new TreeNode(1);

        root5.left = root4;
        root5.right = root8;
        root4.left = root11;
        root8.left = root13;
        root8.right = root44;
        root11.left = root7;
        root11.right = root2;
        root44.right = root1;

        System.out.println(new P124().maxPathSum(root5));
    }


    private int max = Integer.MIN_VALUE;
    public int maxPathSum(TreeNode root) {
        doMaxPathSum(root);
        return max;
    }
    // [5,4,8,11,null,13,4,7,2,null,null,null,1]
    /**
     *               5
     *          4       8
     *        11      13  4
     *     7     2           1
     *
     *
     * 27 + 13 + 8 = 48
     *
     * @param root
     * @return
     */
    private int doMaxPathSum(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int leftSum = doMaxPathSum(root.left);
        int rightSum = doMaxPathSum(root.right);
        int currNodeMax = Math.max(root.val, root.val + Math.max(leftSum, rightSum));
        max = Math.max(root.val + leftSum + rightSum, Math.max(currNodeMax, max));
        return currNodeMax;
    }
}
