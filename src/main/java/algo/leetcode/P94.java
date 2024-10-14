package algo.leetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class P94 {

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

    class Solution {

        /**
         * 非递归
         *
         * @param root
         * @return
         */
        public List<Integer> inorderTraversal(TreeNode root) {
            List<Integer> result = new ArrayList<>();
            if (root == null) {
                return result;
            }

            Stack<TreeNode> stack = new Stack<>();
            TreeNode curr = root;

            while (curr != null || !stack.isEmpty()) {
                while (curr != null) {
                    stack.add(curr);
                    curr = curr.left;
                }
                TreeNode n = stack.pop();
                result.add(n.val);

                curr = n.right;
            }

            return result;
        }


//        /**
//         * 递归
//         *
//         * @param root
//         * @return
//         */
//        public List<Integer> inorderTraversal(TreeNode root) {
//
//            List<Integer> result = new ArrayList<>();
//            doInorderTraversal(root, result);
//            return result;
//        }
//
//        private void doInorderTraversal(TreeNode root, List<Integer> result) {
//            if (root == null) return;
//
//            doInorderTraversal(root.left, result);
//
//            result.add(root.val);
//
//            doInorderTraversal(root.right, result);
//
//        }
    }
}
