package interview;/**
 * @Author Hanyu.Wang
 * @Date 2025/3/19 19:15
 * @Description 
 * @Version 1.0
 **/
public class ListNode {
     /*
     两两翻转链表， 给一个头节点head，例如1->2->3->4,每两个节点互换位置，结果为2->1->4->3，返回新的头节点
      */

    public void reverseTwo(Node head) {
        if (head == null || head.next == null) {
            return;
        }
        Node pHead = head;
        Node tail = head;
        Node newTail = null;
        int total = 1;
        while (tail.next != null) {
            if (total % 2 == 0) {
                Node nextHead = tail.next;
                tail.next = pHead;
                pHead.next = null;
                newTail = pHead;

            }

            total++;
            tail = tail.next;
        }
    }

    class Node {
        Node next;
        int value;
    }
}
