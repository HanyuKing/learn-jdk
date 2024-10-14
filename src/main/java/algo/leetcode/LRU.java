package algo.leetcode;

import java.util.HashMap;

public class LRU {

}

class LRUCache {

    private HashMap<Integer, CacheNode> cache;
    private CacheNode head, tail;
    private int capacity;
    private int size;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        head = tail = new CacheNode(-1);
        this.size = 0;
    }

    public int get(int key) {
        CacheNode node = this.cache.get(key);
        if (node == null) return -1;

        CacheNode currNode = node;
        if (node.next == null) {
            node = null;
        } else {
            node.val = node.next.val;
            node.next = node.next.next;
        }

        currNode.next = head.next;
        head.next = currNode;

        return currNode.val;
    }

    public void put(int key, int value) {
        if (this.size == capacity) {

        }

        CacheNode newNode = new CacheNode(value, null);
        this.cache.put(key, newNode);
        newNode.next = head.next;
        head.next = newNode;

        size++;
    }
}

class CacheNode {
    public CacheNode next;
    public CacheNode pre;
    public int val;
    public CacheNode() {}
    public CacheNode(int val) {
        this.val = val;
    }
    public CacheNode(int val, CacheNode next) {
        this.val = val;
        this.next = next;
    }
    public CacheNode(CacheNode next) {
        this.next = next;
    }
}
