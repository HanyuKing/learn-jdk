package algo.tree;

import algo.hot200.Base;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Hanyu.Wang
 * @Date 2024/10/10 11:08
 * @Description
 * @Version 1.0
 **/
public class Answer extends Base {

    @Test
    public void testP230() {

    }

    public int kthSmallest(TreeNode root, int k) {
        doKthSmallest(root, k);
        return val;
    }

    private int count = 0;
    private int val = 0;
    public void doKthSmallest(TreeNode root, int k) {
        if (root == null) {
            return;
        }
        kthSmallest(root.left, k);
        if (++count == k) {
            val = root.val;
        }
        kthSmallest(root.right, k);
    }

    @Test
    public void testP98() {
        // isValidBST
    }

    private Integer preVal = null;
    public boolean isValidBST(TreeNode root) {
        if (root == null) {
            return true;
        }
        boolean result = isValidBST(root.left);
        if (!result) {
            return false;
        }

        if (preVal != null) {
            if (root.val <= preVal) {
                return false;
            }
        }
        preVal = root.val;

        result = isValidBST(root.right);
        return result;
    }

    @Test
    public void test101() {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(2);

        print(isSymmetric(root));
    }

    public boolean isSymmetric(TreeNode root) {
        if (root == null) {
            return true;
        }
        return isSymmetricCheck(root.left, root.right);
    }

    private boolean isSymmetricCheck(TreeNode node1, TreeNode node2) {
        if (node1 == null && node2 == null) {
            return true;
        } else if (node1 == null || node2 == null) {
            return false;
        }
        return node1.val == node2.val
                && isSymmetricCheck(node1.left, node2.right)
                && isSymmetricCheck(node1.right, node2.left);
    }

    @Test
    public void testP226() {
        // invertTree
    }

    public TreeNode invertTree(TreeNode root) {
        if (root == null) {
            return null;
        }
        TreeNode left = root.left;
        root.left = invertTree(root.right);
        root.right = invertTree(left);
        return root;
    }

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
