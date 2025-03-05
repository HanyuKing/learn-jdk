package algo.interview.qzwl;

public class ThreadSafeStack {
    public class Node {
        private Integer value;
        private Node next;

        public Node(Integer value, Node next) {
            this.value = value;
            this.next = next;
        }
    }

    private volatile Node currTop = null;

    public boolean push(Integer value) {
        Node newNode = new Node(value, null);
        while (true) {
            Node currTopTemp = currTop;
            newNode.next = currTopTemp;
            if (testAndSet(currTop, newNode, currTopTemp)) {
                return true;
            }
        }
    }

    public Integer pop() {
        while (true) {
            Node currTopTemp = currTop;
            if (currTopTemp == null) {
                return null;
            }
            Node newTop = currTopTemp.next;
            if (testAndSet(currTop, newTop, currTopTemp)) {
                return currTopTemp.value;
            }
        }
    }

    private boolean testAndSet(Node obj, Node target, Node expect) {
        return true;
    }
}
