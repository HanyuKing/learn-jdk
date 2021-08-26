package leetcode;

import org.apache.commons.lang3.AnnotationUtils;

/**
 * 129. Sum Root to Leaf Numbers
 */
public class P129 {
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

    private int totalSum = 0;
    public int sumNumbers(TreeNode root) {
        doSumNumbers(root, 0);
        return totalSum;
    }

    private void doSumNumbers(TreeNode root, int sum) {
        if (root == null) {
            return;
        }
        sum += root.val;
        if (root.left == null && root.right == null) {
            totalSum += sum;
            return;
        }
        doSumNumbers(root.left, sum * 10);
        doSumNumbers(root.right, sum * 10);
    }
}
