package algo.leetcode;

/**
 * 109. Convert Sorted List to Binary Search Tree (Medium)
 */
public class P109 {

    public static void main(String[] args) {
        ListNode list = new ListNode(-10);
        ListNode head = list;
        list.next = new ListNode(-3);
        list = list.next;
        list.next = new ListNode(0);
        list = list.next;
        list.next = new ListNode(5);
        list = list.next;
        list.next = new ListNode(9);

        TreeNode n = new P109().sortedListToBST(head);
        System.out.println(n);

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
    public static class ListNode {
         int val;
         ListNode next;
         ListNode() {}
         ListNode(int val) { this.val = val; }
         ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }
    public TreeNode sortedListToBST(ListNode head) {
        if (head == null) {
            return null;
        }
        TreeNode root = new TreeNode();

        ListNode midNodePre = getMidNodePre(head);

        ListNode midNode = null;
        if (midNodePre == null) {
            if (head.next == null) {
                root.val = head.val;
            } else if (head.next.next == null) {
                root.val = head.val;
                root.right = new TreeNode(head.next.val);
            }
            return root;
        }

        midNode = midNodePre.next;
        midNodePre.next = null;

        ListNode midNodeNext = midNode.next;

        root.val = midNode.val;

        root.left = sortedListToBST(head);
        root.right = sortedListToBST(midNodeNext);

        return root;
    }

    private ListNode getMidNodePre(ListNode head) {
        if (head == null) {
            return head;
        }
        ListNode slow = head;
        ListNode fast = head;
        ListNode midPre = null;
        while (fast != null) {
            fast = fast.next;
            if (fast == null) {
                break;
            }
            fast = fast.next;
            if (fast == null) {
                break;
            }
            midPre = slow;
            slow = slow.next;
        }
        return midPre;
    }
}
