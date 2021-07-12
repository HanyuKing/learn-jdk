package jianzhioffer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * JZ31 整数中1出现的次数（从1到n整数中1出现的次数）
 */
public class JZ31 {
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }
    public static void main(String[] args) {
        ListNode l1 = new ListNode(1);
        ListNode l2 = new ListNode(2);
        ListNode l3 = new ListNode(3);
        ListNode l4 = new ListNode(4);
        ListNode l5 = new ListNode(5);
        ListNode l6 = new ListNode(6);
        ListNode l7 = new ListNode(7);
        l1.next = l2;
        l2.next = l3;
        l3.next = l6;

        l4.next = l5;
        l5.next = l6;
        l6.next = l7;



        System.out.println(new JZ31().FindFirstCommonNode(l1, l4).val);
    }
    public ListNode FindFirstCommonNode(ListNode pHead1, ListNode pHead2) {
        ListNode headTemp = pHead1;
        ListNode headTemp2 = pHead2;
        List<ListNode> l1 = new ArrayList<>();
        List<ListNode> l2 = new ArrayList<>();
        while(pHead1 != null) {
            l1.add(pHead1);
            pHead1 = pHead1.next;
        }
        while(pHead2 != null) {
            l2.add(pHead2);
            pHead2 = pHead2.next;
        }
        int len = Math.min(l1.size(), l2.size());
        int i = 0;
        for (; i < len; i++) {
            if (l1.get(l1.size() - 1 - i).val != l2.get(l2.size() - 1 - i).val) {
                if (i != 0) {
                    return l1.get(l1.size()- i);
                }
                return null;
            }
        }
        if (i == len) {
            if (len == l1.size()) return headTemp;
            else return headTemp2;
        }
        return null;
    }
}
