package algo.linklist;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

class LFUCache_LogN {
    private PriorityQueue<Pair> minHeap;
    private Map<Integer, Pair> keyPairMap;
    private int capacity;
    private int time;

    public LFUCache_LogN(int capacity) {
        this.minHeap = new PriorityQueue<>(capacity, (o1, o2) -> o1.count.compareTo(o2.count) == 0 ? o1.time >= o2.time ? 1 : -1 : o1.count - o2.count);
        this.keyPairMap = new HashMap<>(capacity);
        this.capacity = capacity;
    }

    // logN
    public int get(int key) {
        if (!keyPairMap.containsKey(key)) {
            return -1;
        }
        Pair pair = keyPairMap.get(key);
        pair.count++;
        pair.time = time++;
        minHeap.remove(pair);
        minHeap.offer(pair);
        return pair.value;
    }

    // logN
    public void put(int key, int value) {
        if (capacity == keyPairMap.size() && !keyPairMap.containsKey(key)) {
            Pair minFreqPair = minHeap.poll();
            keyPairMap.remove(minFreqPair.key);
            minHeap.remove(minFreqPair);
        }
        Pair pair = keyPairMap.getOrDefault(key, new Pair(key, value, 0, 0));
        keyPairMap.put(key, pair);
        pair.value = value;
        pair.count++;
        pair.time = time++;
        minHeap.remove(pair);
        minHeap.offer(pair);
    }

    private class Pair {
        Integer key;
        Integer value;
        Integer count;
        Integer time;
        public Pair(Integer key, Integer value, Integer count, Integer time) {
            this.key = key;
            this.value = value;
            this.count = count;
            this.time = time;
        }
    }
}

/**
 * Your LFUCache object will be instantiated and called as such:
 * LFUCache obj = new LFUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */