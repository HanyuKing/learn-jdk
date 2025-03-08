package algo.qzwl;

import java.util.ArrayList;
import java.util.List;

public class NodeProblem {
     class Node {
        private int index;
        private Node next;

        public Node(int index) {
            this.index = index;
        }

        @Override
        public String toString() {
            return "Node" + (index + 1);
        }
    }

    public List<Node> find(Node[] root) {
        boolean[] ref = new boolean[root.length];
        for (int i = 0; i < root.length; i++) {
            if (root[i].next != null) {
                ref[root[i].next.index] = true;
            }
        }
        List<Node> list = new ArrayList<>();
        for (int i = 0; i < root.length; i++) {
            if (!ref[i]) {
                list.add(root[i]);
            }
        }
        return list;
    }

    public static void main(String[] args) {
//        Node[] ROOT = new Node[5];
//        // 创建节点
//        ROOT[0] = new Node(0);
//        ROOT[1] = new Node(1);
//        ROOT[2] = new Node(2);
//        ROOT[3] = new Node(3);
//        ROOT[4] = new Node(4);
//
//        // 设置链表关系
//        ROOT[0].next = ROOT[1]; // Node1 -> Node2
//        ROOT[3].next = ROOT[1]; // Node4 -> Node2
//        ROOT[4].next = ROOT[3];    // Node5 -> Node4
//
//        System.out.println(new NodeProblem().find(ROOT));
    }
}
