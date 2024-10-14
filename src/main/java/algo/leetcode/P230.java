package algo.leetcode;

public class P230 {
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

    private int val = 0;
    private int count = 0;
    public int kthSmallest(TreeNode root, int k) {
        doKthSmallest(root, k);
        return val;
    }

    private void doKthSmallest(TreeNode root, int k) {
        if (root == null) {
            return ;
        }

        doKthSmallest(root.left, k);

        count++;

        if (val == 0 && count == k) {
            val = root.val;
            return;
        }

        doKthSmallest(root.right, k);
    }
}
