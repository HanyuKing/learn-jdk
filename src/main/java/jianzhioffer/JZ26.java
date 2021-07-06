package jianzhioffer;

import java.util.List;

/**
 * JZ26 二叉搜索树与双向链表
 */

public class JZ26 {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(10);
        TreeNode n1, n2, n3, n4, n5, n6;
        n1 = new TreeNode(6);
        n2 = new TreeNode(14);
        n3 = new TreeNode(4);
        n4 = new TreeNode(8);
        n5 = new TreeNode(12);
        n6 = new TreeNode(16);
        root.left = n1;
        root.right = n2;
        n1.left = n3;
        n1.right = n4;
        n2.left = n5;
        n2.right = n6;

        TreeNode head = new JZ26().Convert(root);
        while (head != null) {
            System.out.print(head.val + " ");
            head = head.left;
        }
        System.out.println();
    }

    private TreeNode pre;
    private TreeNode curr;

    public TreeNode Convert(TreeNode pRootOfTree) {
        if (pRootOfTree == null) {
            return null;
        }

        Convert(pRootOfTree.left);

        pre = curr;
        curr = pRootOfTree;

        if (pre != null) {
            pre.right = curr;
            curr.left = pre;
        }

        Convert(pRootOfTree.right);

        return curr;
    }

    static class TreeNode {
        int val = 0;
        TreeNode left = null;
        TreeNode right = null;

        public TreeNode(int val) {
            this.val = val;

        }

    }
}
