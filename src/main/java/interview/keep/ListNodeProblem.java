package interview.keep;

import org.junit.Test;

public class ListNodeProblem {
    public boolean hasCircle(ListNode head) {
        if (head == null || head.next == null) {
            return false;
        }
        ListNode slow = head;
        ListNode fast = head.next;
        while (fast != null && fast.next != null) {
            if (slow == fast) {
                return true;
            }
            slow = slow.next;
            fast = fast.next.next;
        }
        return false;
    }

    @Test
    public void testHasCircle() {
        ListNode head = new ListNode();
        ListNode node1 = new ListNode();
        ListNode node2 = new ListNode();
        ListNode node3 = new ListNode();
        head.next = node1;
        node1.next = node2;
        node2.next = node3;
        node3.next = node1;

        System.out.println(hasCircle(head));
    }

    @Test
    public void testHasNoCircle() {
        ListNode head = new ListNode();
        ListNode node1 = new ListNode();
        ListNode node2 = new ListNode();
        ListNode node3 = new ListNode();
        head.next = node1;
        node1.next = node2;
        node2.next = node3;

        System.out.println(hasCircle(head));
    }

    @Test
    public void test3() {
        ListNode head = new ListNode();

        System.out.println(hasCircle(head));
    }

    @Test
    public void test4() {
        ListNode head = new ListNode();

        System.out.println(hasCircle(null));
    }
}
