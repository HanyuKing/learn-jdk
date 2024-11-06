package algo.linklist;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Hanyu.Wang
 * @Date 2024/11/6 15:31
 * @Description
 * @Version 1.0
 **/
public class LFUCache {
    private int capacity;
    private int minFrequently;
    private Map<Integer, DoubleLinkedList.Node<Integer>> keyNodeMap;
    private Map<Integer, DoubleLinkedList<Integer>> freqNodeMap;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.keyNodeMap = new HashMap<>();
        this.freqNodeMap = new HashMap<>();
    }

    public int get(int key) {
        DoubleLinkedList.Node<Integer> keyNode = keyNodeMap.get(key);
        if (keyNode == null) {
            return -1;
        }
        increaseFrequently(keyNode);
        return keyNode.value;
    }

    public void put(int key, int value) {
        DoubleLinkedList.Node<Integer> keyNode = keyNodeMap.get(key);
        if (keyNode == null) {
            if (keyNodeMap.size() == capacity) {
                evictNode();
            }

            DoubleLinkedList<Integer> freqNode = freqNodeMap.getOrDefault(1, new DoubleLinkedList<>(1));
            DoubleLinkedList.Node<Integer> newNode = new DoubleLinkedList.Node<>(freqNode, key, value);
            freqNodeMap.put(1, freqNode);
            keyNodeMap.put(key, newNode);

            freqNode.addFirst(newNode);
            minFrequently = 1;
        } else {
            keyNode.value = value;
            increaseFrequently(keyNode);
        }
    }

    private void increaseFrequently(DoubleLinkedList.Node<Integer> keyNode) {
        int newFreq = keyNode.parent.value + 1;
        int freq = keyNode.parent.value;
        DoubleLinkedList<Integer> newFreqNodeList = freqNodeMap.getOrDefault(newFreq, new DoubleLinkedList<>(newFreq));
        freqNodeMap.put(newFreq, newFreqNodeList);

        DoubleLinkedList<Integer> freqNodeList = freqNodeMap.getOrDefault(freq, new DoubleLinkedList<>(freq));
        freqNodeList.delete(keyNode);

        if (freqNodeList.count == 0) {
            freqNodeMap.remove(freq);
            if (minFrequently == freq) {
                minFrequently++;
            }
        }

        keyNode.parent = newFreqNodeList;
        newFreqNodeList.addFirst(keyNode);
    }

    private void evictNode() {
        DoubleLinkedList.Node<Integer> deletedNode = freqNodeMap.get(minFrequently).deleteLast();
        keyNodeMap.remove(deletedNode.key);
    }

    private static class DoubleLinkedList<E> {
        E value;
        Node<E> head;
        Node<E> tail;
        int count;
        public DoubleLinkedList() {}
        public DoubleLinkedList(E value) {
            this.value = value;
        }

        public void addFirst(Node<E> node) {
            if (head == null) {
                head = tail = node;
            } else if (head == tail) {
                node.next = tail;
                tail.prev = node;
                head = node;
            } else {
                node.next = head;
                head.prev = node;
                head = node;
            }
            count++;
        }

        public void delete(Node<E> node) {
            if (node == head) {
                deleteFirst();
            } else if (node == tail) {
                deleteLast();
            } else if (head == tail) {
                head = tail = null;
                count--;
            } else {
//                if (node.prev == null) {
//                    while (head != null) {
//                        System.out.print(head.value + " ");
//                    }
//                    System.out.println();
//                }
                node.prev.next = node.next;
                node.next.prev = node.prev;
                node.next = null;
                node.prev = null;
                count--;
            }
        }

        public void deleteFirst() {
            if (head == tail) {
                head = tail = null;
            } else {
                head = head.next;
            }
            count--;
        }

        public Node<E> deleteLast() {
            Node<E> tailTemp = tail;
            if (tail == head) {
                head = tail = null;
            } else {
                tail = tail.prev;
                tail.next = null;
                tailTemp.prev = null;
            }
            count--;
            return tailTemp;
        }

        private static class Node<E> {
            Node<E> prev;
            Node<E> next;
            DoubleLinkedList<E> parent;
            E key;
            E value;
            public Node(DoubleLinkedList<E> parent, E key, E value) {
                this.parent = parent;
                this.key = key;
                this.value = value;
            }

        }
    }
}
