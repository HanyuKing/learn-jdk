package interview.xhzn.q3;

import org.junit.Test;

public class Q3 {
    /*
    给你两个 非空 的链表，表示两个非负的整数。它们每位数字都是按照 逆序 的方式存储的，并且每个节点只能存储 一位 数字。

    请你将两个数相减，以相同形式返回一个表示差值的列表；如果差值为负数则需将链表第一位设置为-1。

    你可以假设除了数字 0 之外，这两个数都不会以 0 开头。

    输入：l1 = [2,4,3], l2 = [5,6,4]
    输出：[-1,3,2,1]
    解释：342 - 465 = -123.
    示例 2：

    输入：l1 = [0], l2 = [0]
    输出：[0]
    示例 3：

    输入：l1 = [9,9,9,9,9,9,9], l2 = [9,9,9,9]
    输出：[0,0,0,0,9,9,9]
     */

    @Test
    public void test2() {
        Q3 q3 = new Q3();
        Node head1 = new Node(0);

        Node head2 = new Node(0);

        Node newNode = q3.sub(head1, head2);
        while (newNode != null) {
            System.out.print(newNode.value + " " );
            newNode = newNode.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Q3 q3 = new Q3();
        Node head1 = new Node(2);
        Node node2 = new Node(4);
        Node node3 = new Node(3);
        head1.next = node2;
        node2.next = node3;

        Node head2 = new Node(5);
        Node node4 = new Node(6);
        Node node5 = new Node(4);
        head2.next = node4;
        node4.next = node5;

        Node newNode = q3.sub(head1, head2);
        while (newNode != null) {
            System.out.print(newNode.value + " " );
            newNode = newNode.next;
        }
        System.out.println();
    }

    public Node sub(Node head1, Node head2) {
        int num1 = toNumber(head1);
        int num2 = toNumber(head2);

        int diff = num1 - num2;
        Node head = null;
        if (diff < 0) {
            head = new Node(-1);
        }
        Node tail = head;

        diff = -diff;
        while (diff != 0) {
            int n = diff - (diff / 10) * 10;
            Node node = new Node(n);
            tail.next = node;
            tail = node;
            diff /= 10;
        }

        return head;
    }

    private int toNumber(Node head) {
        int num = 0;
        int multi = 1;
        while (head != null) {
            num += head.value * multi;
            multi = multi * 10;
            head = head.next;
        }
        return num;
    }

    public static class Node {
        public int value;
        public Node next;
        public Node (int value) {
            this.value = value;
        }
    }
}
