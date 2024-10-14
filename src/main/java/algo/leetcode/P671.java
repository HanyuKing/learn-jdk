package algo.leetcode;

public class P671 {
    public class TreeNode {
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

    private int min = -1;
    public int findSecondMinimumValue(TreeNode root) {
        if (root == null) return -1;
        int rootVal = root.val;
        doFindMinimumValue(root.left, rootVal);
        doFindMinimumValue(root.right, rootVal);
        return min;
    }

    public void doFindMinimumValue(TreeNode root, int base) {
        if (root == null) return;

        doFindMinimumValue(root.left, base);
        doFindMinimumValue(root.right, base);

        if (root.val > base) {
            if (min == -1) {
                min = root.val;
            } else {
                min = Math.min(min, root.val);
            }
        }

        return ;
    }
}
