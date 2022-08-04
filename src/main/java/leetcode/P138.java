package leetcode;

import java.util.HashMap;
import java.util.Map;

public class P138 {
    public static void main(String[] args) {
        Node head = new Node(7);
        Node node1 = new Node(13);
        Node node2 = new Node(11);
        Node node3 = new Node(10);
        Node node4 = new Node(1);
        head.next = node1;
        head.random = null;
        node1.next = node2;
        node1.random = head;
        node2.next = node3;
        node2.random = node4;
        node3.next = node4;
        node3.random = node2;
        node4.next = null;
        node4.random = head;

        Node headTemp = head;
        while (headTemp != null) {
            System.out.println(headTemp);
            headTemp = headTemp.next;
        }

        headTemp = new P138Solution().copyRandomList(head);

        while (headTemp != null) {
            System.out.println(headTemp.val);
            headTemp = headTemp.next;
        }

    }
}



// Definition for a Node.
class Node {
    int val;
    Node next;
    Node random;

    public Node(int val) {
        this.val = val;
        this.next = null;
        this.random = null;
    }
}


class P138Solution {
    public Node copyRandomList(Node pHead) {
        Node newHead = new Node(-1);
        Node newHeadTemp = newHead;
        Map<Node, Node> map = new HashMap<>();
        Node pHeadTemp = pHead;

        while (pHeadTemp != null) {
            Node newNode = map.get(pHeadTemp);
            if (newNode == null) {
                newNode = new Node(pHeadTemp.val);
                map.put(pHeadTemp, newNode);
            }

            newHeadTemp.next = newNode;
            newHeadTemp = newHeadTemp.next;

            pHeadTemp = pHeadTemp.next;
        }

        pHeadTemp = pHead;
        newHeadTemp = newHead.next;

        while (pHeadTemp != null) {

            if (pHeadTemp.random != null) {
                Node randNode = map.get(pHeadTemp.random);
                newHeadTemp.random = randNode;
            }

            newHeadTemp = newHeadTemp.next;
            pHeadTemp = pHeadTemp.next;
        }

        return newHead.next;
    }
}
