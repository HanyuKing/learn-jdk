package algo.leetcode;

/**
 * 530. Minimum Absolute Difference in BST (Easy)
 */
class P530 {
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

    private int min = Integer.MAX_VALUE;
    private int preVal = -1;
    public int getMinimumDifference(TreeNode root) {
        if (root == null) {
            return -1;
        }

        getMinimumDifference(root.left);

        if (preVal >= 0) {
            min = Math.min(min, root.val - preVal);
        }

        preVal = root.val;

        getMinimumDifference(root.right);

        return min;
    }
}