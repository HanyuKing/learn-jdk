package algo.interview.qzwl;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class NodeProblem {

    public static class Node {
        public int index;
        public Node next;
        public Node(int index) {
            this.index = index;
        }

        @Override
        public String toString() {
            return "Node" + (index + 1);
        }
    }

    public List<Node> findUnlinkedNode(Node[] nodes) {
        boolean[] linked = new boolean[nodes.length];
        for (Node node : nodes) {
            if (node.next != null) {
                linked[node.next.index] = true;
            }
        }
        List<Node> result = new ArrayList<>();
        for (int i = 0; i < nodes.length; i++) {
            if (!linked[i]) {
                result.add(nodes[i]);
            }
        }
        return result;
    }

    @Test
    public void test1() {
        Node[] rootN = new Node[5];
        Node node1 = new Node(0);
        Node node2 = new Node(1);
        Node node3 = new Node(2);
        Node node4 = new Node(3);
        Node node5 = new Node(4);
        node1.next = node2;
        node4.next = node2;
        node5.next = node4;

        rootN[0] = node1;
        rootN[1] = node2;
        rootN[2] = node3;
        rootN[3] = node4;
        rootN[4] = node5;

        System.out.println(findUnlinkedNode(rootN));
    }

}
