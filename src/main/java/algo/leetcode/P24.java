package algo.leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2022/8/12 14:22
 * @Description
 * @Version 1.0
 **/
public class P24 {
    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        head.next = node2;
        node2.next = node3;
        node3.next = node4;

        ListNode newHead = new Solution24().swapPairs(head);
        while (newHead != null) {
            System.out.println(newHead.val);
            newHead = newHead.next;
        }
    }
}

//给你一个链表，两两交换其中相邻的节点，并返回交换后链表的头节点。你必须在不修改节点内部的值的情况下完成本题（即，只能进行节点交换）。
//
//
//
// 示例 1：
//
//
//输入：head = [1,2,3,4]
//输出：[2,1,4,3]
//
//
// 示例 2：
//
//
//输入：head = []
//输出：[]
//
//
// 示例 3：
//
//
//输入：head = [1]
//输出：[1]
//
//
//
//
// 提示：
//
//
// 链表中节点的数目在范围 [0, 100] 内
// 0 <= Node.val <= 100
//
//
// Related Topics 递归 链表 👍 1511 👎 0


//leetcode submit region begin(Prohibit modification and deletion)

class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}
// 1->2->3->4
class Solution24 {
    public ListNode swapPairs(ListNode head) {
        if (head == null) {
            return null;
        }
        ListNode newHead = new ListNode();
        ListNode newHeadTemp = newHead;
        newHead.next = head;

        ListNode curr = head;
        ListNode next = curr.next;
        while (next != null) {
            ListNode nNext = next.next;
            curr.next = next.next;
            next.next = curr;

            newHead.next = next;
            newHead = curr;

            curr = nNext;
            if (curr == null) {
                break;
            }
            next = curr.next;
        }
        return newHeadTemp.next;
    }
}