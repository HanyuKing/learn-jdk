package algo.linklist;

import algo.hot200.Base;
import org.junit.Test;

/**
 * @Author Hanyu.Wang
 * @Date 2024/10/15 10:10
 * @Description
 * @Version 1.0
 **/
public class Answer extends Base {

    @Test
    public void testP460_2() {
        LFUCache lfu = new LFUCache(2);
        lfu.put(3, 1);
        lfu.put(2, 1);
        lfu.put(2, 2);
        lfu.put(4, 4);
        System.out.println(lfu.get(2));      // 返回 2
    }

    @Test
    public void testP460() {
        // cnt(x) = 键 x 的使用计数
        // cache=[] 将显示最后一次使用的顺序（最左边的元素是最近的）
        LFUCache lfu = new LFUCache(2);
        lfu.put(1, 1);   // cache=[1,_], cnt(1)=1
        lfu.put(2, 2);   // cache=[2,1], cnt(2)=1, cnt(1)=1
        System.out.println(lfu.get(1));      // 返回 1
        // cache=[1,2], cnt(2)=1, cnt(1)=2
        lfu.put(3, 3);   // 去除键 2 ，因为 cnt(2)=1 ，使用计数最小
        // cache=[3,1], cnt(3)=1, cnt(1)=2
        System.out.println(lfu.get(2));      // 返回 -1（未找到）
        System.out.println(lfu.get(3));      // 返回 3
        // cache=[3,1], cnt(3)=2, cnt(1)=2
        lfu.put(4, 4);   // 去除键 1 ，1 和 3 的 cnt 相同，但 1 最久未使用
        // cache=[4,3], cnt(4)=1, cnt(3)=2
        System.out.println(lfu.get(1));      // 返回 -1（未找到）
        System.out.println(lfu.get(3));      // 返回 3
        // cache=[3,4], cnt(4)=1, cnt(3)=3
        System.out.println(lfu.get(4));      // 返回 4
        // cache=[3,4], cnt(4)=2, cnt(3)=3
    }
    @Test
    public void testP146() {
        // TODO LRU
    }

    @Test
    public void testP148() {
        ListNode head = new ListNode(3);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(5);
        ListNode node4 = new ListNode(4);
        ListNode node5 = new ListNode(5);
        head.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;

        print(sortList(head));
    }

    public ListNode sortList(ListNode head) {
        ListNode newHead = new ListNode();
        ListNode newHeadTemp = newHead;

        while (head != null) {
            if (newHead.next == null) {
                newHeadTemp.next = head;
                newHeadTemp = head;
                head = head.next;
                newHeadTemp.next = null;
            } else {
                ListNode pre = newHead;
                newHeadTemp = newHead.next;
                while (newHeadTemp != null && newHeadTemp.val < head.val) {
                    pre = newHeadTemp;
                    newHeadTemp = newHeadTemp.next;
                }
                if (newHeadTemp == null) {
                    pre.next = head;
                    pre = head;
                    head = head.next;
                    pre.next = null;
                } else {
                    ListNode headNext = head.next;
                    pre.next = head;
                    head.next = newHeadTemp;
                    head = headNext;
                }
            }
        }

        return newHead.next;
    }

    @Test
    public void testP25() {
        ListNode head = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        ListNode node5 = new ListNode(5);
        head.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;

        print(reverseKGroup(head, 1)); // 1,2,3,4,5 => 2,1,4,3,5
    }

    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode pHead = head;
        ListNode pTail = head;
        ListNode newTail = null;
        int kTemp = k;

        while (pTail != null) {
            while (--k > 0 && pTail != null) {
                pTail = pTail.next;
            }
            if (pTail == null) {
                newTail.next = pHead;
                break;
            }

            ListNode nextHead = pTail.next;

            pTail.next = null;

            ListNode newHead = reverse(pHead);
            if (newTail == null) { // first
                head = newHead;
                newTail = pHead;
            } else {
                newTail.next = pTail;
                newTail = pHead;
            }

            pHead = nextHead;
            pTail = nextHead;
            k = kTemp;
        }

        return head;
    }

    private ListNode reverse(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode p = head;
        ListNode q = p.next;
        head.next = null;

        while (q != null) {
            ListNode r = q.next;
            q.next = p;
            p = q;
            q = r;
        }

        return p;
    }

    @Test
    public void testP24() {
        // [1,2,3,4] => 2,1,4,3]
        ListNode head = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        ListNode node5 = new ListNode(5);
        head.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;

        print(swapPairs(head));
    }

    public ListNode swapPairs(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode tail = null;
        ListNode p1 = head;
        ListNode p2 = p1.next;
        while (p2 != null) {
            ListNode p3 = p2.next;
            ListNode p4 = null;
            if (p3 != null) {
                p4 = p3.next;
            }
            p1.next = null;
            p2.next = p1;
            if (tail == null) {
                tail = p1;
                head = p2;
            } else {
                tail.next = p2;
                tail = p1;
            }

            p1 = p3;
            p2 = p4;
        }
        if (p1 != null) {
            tail.next = p1;
        }
        return head;
    }

    @Test
    public void testP19() {
        // head = [1,2,3,4,5], n = 2     =>     [1,2,3,5]
        ListNode head = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        ListNode node5 = new ListNode(5);
        head.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;

        print(removeNthFromEnd(head, 4));
    }

    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode fast = head;
        while (--n > 0) {
            fast = fast.next;
        }

        ListNode slow = head;
        ListNode pre = null;

        while (fast.next != null) {
            fast = fast.next;
            pre = slow;
            slow = slow.next;
        }

        if (pre == null) {
            return head.next;
        }

        pre.next = slow.next;
        slow.next = null;
        return head;
    }

    @Test
    public void testP2() {
        // 2-4-3
        // 5-6-4
        // 7-0-8
        ListNode l1 = new ListNode();
        l1.val = 2;
        ListNode node1 = new ListNode();
        node1.val = 4;
        ListNode node2 = new ListNode();
        node2.val = 3;
        ListNode l2 = new ListNode();
        l2.val = 5;
        ListNode node4 = new ListNode();
        node4.val = 6;
        ListNode node5 = new ListNode();
        node5.val = 4;

        l1.next = node1;
        node1.next = node2;
        l2.next = node4;
        node4.next = node5;

        print(addTwoNumbers(l1, l2));
    }

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode newHead = new ListNode();
        ListNode tail = newHead;
        int carry = 0;
        while (l1 != null || l2 != null) {
            int val = getNodeVal(l1) + getNodeVal(l2) + carry;
            carry = val / 10;
            int currVal = val % 10;

            tail.next = new ListNode();
            tail = tail.next;
            tail.val = currVal;

            if (l1 != null) {
                l1 = l1.next;
            }
            if (l2 != null) {
                l2 = l2.next;
            }
        }
        if (carry > 0) {
            tail.next = new ListNode();
            tail = tail.next;
            tail.val = carry;
        }

        return newHead.next;
    }

    public ListNode addTwoNumbers3(ListNode l1, ListNode l2) {
        ListNode newHead = l1;
        ListNode tail = newHead;
        int carry = 0;
        while (l1 != null || l2 != null) {
            int val = getNodeVal(l1) + getNodeVal(l2) + carry;
            carry = val / 10;
            int currVal = val % 10;

            if (l1 != null) {
                l1.val = currVal;
                tail = l1;

                l1 = l1.next;

                if (l2 != null) {
                    l2 = l2.next;
                }
            } else {
                l2.val = currVal;
                tail.next = l2;
                tail = l2;

                l2 = l2.next;
            }
        }
        if (carry > 0) {
            tail.next = new ListNode();
            tail = tail.next;
            tail.val = carry;
        }

        return newHead;
    }

    public ListNode addTwoNumbers2(ListNode l1, ListNode l2) {
        ListNode newHead = l1;
        ListNode l2Pre = null;
        ListNode l1Pre = null;
        int carry = 0;
        while (l1 != null || l2 != null) {
            int val = l1.val + l2.val + carry;
            carry = val / 10;
            int currVal = val % 10;

            l1.val = currVal;

            l2Pre = l2;
            l1Pre = l1;
            l1 = l1.next;
            l2 = l2.next;
        }

        while (l1 != null) {
            int val = l1.val + carry;
            carry = val / 10;
            int currVal = val % 10;
            l1.val = currVal;

            l1Pre = l1;
            l1 = l1.next;
        }

        if (l2Pre != null) { // unlink
            l2Pre.next = null;
        }

        l1 = l1Pre;
        while (l2 != null) {
            int val = l2.val + carry;
            carry = val / 10;
            int currVal = val % 10;
            l2.val = currVal;

            l1.next = l2;
            l1 = l2;
            l2 = l2.next;
        }

        if (carry > 0) {
            l1.next = new ListNode();
            l1 = l1.next;
            l1.val = carry;
        }

        return newHead;
    }

    private int getNodeVal(ListNode node) {
        return node == null ? 0 : node.val;
    }
}
