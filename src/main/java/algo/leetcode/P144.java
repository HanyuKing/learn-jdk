package algo.leetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class P144 {

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

//    /**
//     * 递归
//     * @param root
//     * @return
//     */
//    public List<Integer> preorderTraversal(TreeNode root) {
//        List<Integer> result = new ArrayList<>();
//        doPreorderTraversal(root, result);
//        return result;
//    }
//
//    public void doPreorderTraversal(TreeNode root, List<Integer> result) {
//        if (root == null) return;
//
//        result.add(root.val);
//
//        doPreorderTraversal(root.left, result);
//        doPreorderTraversal(root.right, result);
//    }

    /**
     * 非递归
     *
     * @param root
     * @return
     */
    public List<Integer> preorderTraversal(TreeNode root) {
        if (root == null) {
            return new ArrayList<>();
        }

        List<Integer> result = new ArrayList<>();

        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode currRoot = stack.pop();
            result.add(currRoot.val);

            if (currRoot.right != null) {
                stack.push(currRoot.right);
            }
            if (currRoot.left != null) {
                stack.push(currRoot.left);
            }
        }

        return result;
    }
}
