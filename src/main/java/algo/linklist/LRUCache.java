package algo.linklist;

import java.util.HashMap;
import java.util.Map;

class LRUCache {
    private int capacity;
    private DoubleLinkedList linkedList = new DoubleLinkedList();
    private Map<Integer, DoubleLinkedList.Node> map = new HashMap<>();

    public LRUCache(int capacity) {
        this.capacity = capacity;
    }
    
    public int get(int key) {
        DoubleLinkedList.Node node = map.get(key);
        if (node == null) {
            return -1;
        }
        updateAccess(node);
        return node.value;
    }
    
    public void put(int key, int value) {
        DoubleLinkedList.Node node = map.get(key);
        if (node == null) {
            if (map.size() == capacity) {
                evictNode();
            }
            node = new DoubleLinkedList.Node(key, value);
            map.put(key, node);

            linkedList.addFirst(node);
        } else {
            node.value = value;
            updateAccess(node);
        }
    }

    private void updateAccess(DoubleLinkedList.Node node) {
        this.linkedList.remove(node);
        this.linkedList.addFirst(node);
    }

    private void evictNode() {
        DoubleLinkedList.Node tail = this.linkedList.removeLast();
        map.remove(tail.key);
    }

    private static class DoubleLinkedList {
        Node head = null;
        Node tail = null;

        public DoubleLinkedList() {
            this.head = this.tail = new Node(-1, -1);
        }

        private static class Node {
            Node prev;
            Node next;
            int key;
            int value;
            public Node(int key, int value) {
                this.key = key;
                this.value = value;
            }
        }
        public void addFirst(Node node) {
            if (head.next == null) {
                head.next = node;
                node.prev = head;
                tail = node;
            } else {
                node.next = head.next;
                node.prev = head;
                head.next.prev = node;
                head.next = node;
            }
        }

        public void remove(Node node) {
            if (node == tail) {
                Node tailTemp = tail;
                tail = tail.prev;
                tail.next = null;
                tailTemp.prev = null;
            } else {
                node.prev.next = node.next;
                node.next.prev = node.prev;
                node.next = null;
                node.prev = null;
            }
        }

        public Node removeLast() {
            Node tailTemp = tail;
            this.remove(tail);
            return tailTemp;
        }

    }
}
