package algo.leetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * P145 后续遍历树
 */
public class P145 {
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

//    /**
//     * 递归
//     *
//     * @param root
//     * @return
//     */
//    public List<Integer> postorderTraversal(TreeNode root) {
//        if (root == null) return new ArrayList<>();
//
//        List<Integer> result = new ArrayList<>();
//        doPostorderTraversal(root, result);
//        return result;
//    }
//
//    private void doPostorderTraversal(TreeNode root, List<Integer> result) {
//        if (root == null) return ;
//
//        doPostorderTraversal(root.left, result);
//        doPostorderTraversal(root.right, result);
//
//        result.add(root.val);
//    }

    /**
     * 非递归
     *
     * @param root
     * @return
     */
    public List<Integer> postorderTraversal(TreeNode root) {
        if (root == null) return new ArrayList<>();

        List<Integer> result = new ArrayList<>();

        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode currRoot = stack.pop();
            result.add(currRoot.val);

            if (currRoot.left != null) {
                stack.add(currRoot.left);
            }

            if (currRoot.right != null) {
                stack.add(currRoot.right);
            }
        }

        return reverse(result);
    }

    private List<Integer> reverse(List<Integer> list) {
        List<Integer> newList = new ArrayList<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            newList.add(list.get(i));
        }
        return newList;
    }
}
