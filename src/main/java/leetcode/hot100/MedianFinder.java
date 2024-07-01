package leetcode.hot100;

import java.util.PriorityQueue;
import java.util.SortedSet;
import java.util.TreeSet;

class MedianFinder {

    private PriorityQueue<Integer> minHeap = new PriorityQueue<>(); // 右边大
    private PriorityQueue<Integer> maxHeap = new PriorityQueue<>((o1, o2) -> o2 - o1); // 左边小

    public MedianFinder() {

    }
    
    public void addNum(int num) {
        maxHeap.offer(num);
        minHeap.offer(maxHeap.poll());
        if (minHeap.size() > maxHeap.size()) {
            maxHeap.offer(minHeap.poll());
        }
    }
    
    public double findMedian() {
        if (maxHeap.size() > minHeap.size()) {
            return (double) maxHeap.peek();
        } else {
            return (maxHeap.peek() + minHeap.peek()) / 2.0d;
        }
    }
}

/**
 * Your MedianFinder object will be instantiated and called as such:
 * MedianFinder obj = new MedianFinder();
 * obj.addNum(num);
 * double param_2 = obj.findMedian();
 */