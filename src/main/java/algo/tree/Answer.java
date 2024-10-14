package algo.tree;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Hanyu.Wang
 * @Date 2024/10/10 11:08
 * @Description
 * @Version 1.0
 **/
public class Answer {
    @Test
    public void testP104() {

    }

    public int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int leftMax = maxDepth(root.left);
        int rightMax = maxDepth(root.right);

        return Math.max(leftMax, rightMax) + 1;
    }

    @Test
    public void testP543() {

    }

    @Test
    public void testP144() {

    }

    public List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        doPreorderTraversal(root, result);
        return result;
    }

    public void doPreorderTraversal(TreeNode root, List<Integer> result) {
        if (root == null) {
            return;
        }
        result.add(root.val);
        doPreorderTraversal(root.left, result);
        doPreorderTraversal(root.right, result);
    }

    int maxDiameter = 0;
    public int diameterOfBinaryTree(TreeNode root) {
        doDiameterOfBinaryTree(root);
        return maxDiameter;
    }
    public int doDiameterOfBinaryTree(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int leftMax = doDiameterOfBinaryTree(root.left);
        int rightMax = doDiameterOfBinaryTree(root.right);
        maxDiameter = Math.max(maxDiameter, leftMax + rightMax);
        return Math.max(leftMax, rightMax) + 1;
    }

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
}
