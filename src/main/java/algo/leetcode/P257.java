package algo.leetcode;

import java.util.ArrayList;
import java.util.List;

public class P257 {
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

    private List<String> result = new ArrayList();
    public List<String> binaryTreePaths(TreeNode root) {
        doBinaryTreePaths(root, new StringBuilder());
        return result;
    }

    private void doBinaryTreePaths(TreeNode root, StringBuilder s) {
        if (root == null) {
            return;
        }
        s.append(root.val).append("->");
        if (root.left == null && root.right == null) {
            result.add(s.delete(s.length() - 2, s.length()).toString());
            return;
        }
        doBinaryTreePaths(root.left, new StringBuilder(s));
        doBinaryTreePaths(root.right, new StringBuilder(s));
    }
}
