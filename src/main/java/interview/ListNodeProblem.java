package interview;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Hanyu.Wang
 * @Date 2025/3/5 10:07
 * @Description
 * @Version 1.0
 **/
public class ListNodeProblem {

    // 给定一个排序链表（默认正整数），删除所有含有重复数字的节点，只保留原始链表中没有重复出现的数字。
    public class ListNode {
        int val;
        ListNode next;
        ListNode(int x) {
            val = x;
        }
    }

    /*
        示例
        输入: 1->1->2->3->3->4
        输出: 2->4
     */

    @Test
    public void testDelDupNode() {
        ListNode head = new ListNode(1);
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(3);
        ListNode node5 = new ListNode(4);
        head.next = node1;
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;

        ListNode listNode = solution(head);

        while (listNode != null) {
            System.out.print(listNode.val + "->");
            listNode = listNode.next;
        }
    }

    public ListNode solution(ListNode head){
        if (head == null || head.next == null) {
            return head;
        }
        Map<Integer, Integer> map = new HashMap<>();
        ListNode pHead = head;
        while (pHead != null) {
            Integer count = map.getOrDefault(pHead.val, 0);
            map.put(pHead.val, count + 1);
            pHead = pHead.next;
        }
        ListNode newHead = new ListNode(-1);
        ListNode newTail = newHead;

        pHead = head;
        while (pHead != null) {
            if (map.get(pHead.val) == 1) {
                newTail.next = pHead;
                newTail = pHead;

                pHead = pHead.next;

                newTail.next = null;
            } else {
                pHead = pHead.next;
            }
        }
        return newHead.next;
    }

}
