package leetcode;

public class P538 {
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

    public TreeNode convertBST(TreeNode root) {
        doConvertBST(root);
        return root;
    }
    private int sum = 0;
    private void doConvertBST(TreeNode root) {
        if (root == null) {
            return;
        }
        doConvertBST(root.right);
        sum += root.val;
        root.val = sum;
        doConvertBST(root.left);
    }

//    public TreeNode convertBST(TreeNode root) {
//        sum = sumBST(root);
//        doConvertBST(root);
//        return root;
//    }
//    private int travelSum = 0;
//    private int sum = 0;
//    private void doConvertBST(TreeNode root) {
//        if (root == null) {
//            return ;
//        }
//
//        doConvertBST(root.left);
//
//        int rootVal = root.val;
//        root.val = sum - travelSum;
//        travelSum = travelSum + rootVal;
//
//        System.out.println(travelSum);
//
//        doConvertBST(root.right);
//
//        return ;
//    }
//
//    private int sumBST(TreeNode root) {
//        if (root == null) {
//            return 0;
//        }
//        int leftSum = sumBST(root.left);
//        int rightSum =  sumBST(root.right);
//        return root.val + leftSum + rightSum;
//    }
}
