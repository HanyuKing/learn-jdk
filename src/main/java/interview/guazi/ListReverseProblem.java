package interview.guazi;

import org.junit.Test;

public class ListReverseProblem {

    /**
     * 1->2->3    =>   3->2->1
     * p  q  r
     *
     * 1<-2 3
     *    p q r
     */

    @Test
    public void test1() {
        Node head = new Node(1);
        Node node1 = new Node(2);
        Node node2 = new Node(3);
        head.next = node1;
        node1.next = node2;

        print(head);

        head = reverse(head);

        print(head);
    }

    @Test
    public void test2() {
        Node head = new Node(1);

        print(head);

        head = reverse(head);

        print(head);
    }

    @Test
    public void test3() {
        Node head = new Node(1);
        Node node1 = new Node(2);
        Node node2 = new Node(3);
        Node node3 = new Node(4);
        head.next = node1;
        node1.next = node2;
        node2.next = node3;

        print(head);

        head = reverse(head);

        print(head);
    }

    public void print(Node head) {
        while (head != null) {
            System.out.print(head.value + "->");
            head = head.next;
        }
        System.out.println("null");
    }

    class Node {
        public int value;
        public Node next;
        public Node(int value) {
            this.value = value;
        }

    }

    public Node reverse(Node head) {
        if (head == null || head.next == null) {
            return head;
        }
        Node p = head;
        Node q = p.next;
        p.next = null;

        while (q.next != null) {
            Node r = q.next;
            q.next = p;
            p = q;
            q = r;
        }
        q.next = p;
        return q;
    }


    public static void main(String[] args) {

    }
}
