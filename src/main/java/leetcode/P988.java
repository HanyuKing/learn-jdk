package leetcode;

public class P988 {
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

    public static void main(String[] args) {

    }

    private StringBuilder result = new StringBuilder();
    public String smallestFromLeaf(TreeNode root) {
        doSmallestFromLeaf(root, new StringBuilder());
        return result.reverse().toString();
    }

    private void doSmallestFromLeaf(TreeNode root, StringBuilder s) {
        if (root == null) {
            return;
        }

        s.append((char) (root.val + 97));

        if (root.left == null && root.right == null) {
            if (result.length() == 0) {
                result = s;
                return;
            }

            String s1 = new StringBuilder(result.toString()).reverse().toString();
            String s2 = new StringBuilder(s.toString()).reverse().toString();
            if (s2.compareTo(s1) < 0) {
                result = s;
                return;
            }
        }

        doSmallestFromLeaf(root.left, new StringBuilder(s));
        doSmallestFromLeaf(root.right, new StringBuilder(s));
    }
}
