package algo.leetcode;

public class P2 {
    public class ListNode {
     int val;
     ListNode next;
     ListNode() {}
     ListNode(int val) { this.val = val; }
     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode l3 = new ListNode();
        ListNode head = l3;

        int carry = 0;
        while (l1 != null || l2 != null || carry > 0) {
            int x, y, curr ;
            if (l1 == null) {
                x = 0;
            } else {
                x = l1.val;
                l1 = l1.next;
            }
            if (l2 == null) {
                y = 0;
            } else {
                y = l2.val;
                l2 = l2.next;
            }
            curr = (x + y + carry) % 10;
            if ((x + y + carry) >= 10) {
                carry = 1;
            } else {
                carry = 0;
            }

            l3.next = new ListNode(curr);
            l3 = l3.next;
        }
        return head.next;
    }

}
