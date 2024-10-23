package algo.heap;

import java.util.PriorityQueue;

class MedianFinder {

    private PriorityQueue<Integer> minHeap = new PriorityQueue<>(); // 右边大
    private PriorityQueue<Integer> maxHeap = new PriorityQueue<>((o1, o2) -> o2 - o1); // 左边小

    public MedianFinder() {

    }
    
    public void addNum(int num) {
        int leftSize = maxHeap.size();
        int rightSize = minHeap.size();

        if (leftSize <= rightSize) {
            if (minHeap.isEmpty() || num <= minHeap.peek()) {
                maxHeap.offer(num);
            } else {
                minHeap.offer(num);
                maxHeap.offer(minHeap.poll());
            }
        } else {
            if (num >= maxHeap.peek()) {
                minHeap.offer(num);
            } else {
                maxHeap.offer(num);
                minHeap.offer(maxHeap.poll());
            }
        }
    }
    
    public double findMedian() {
        int leftSize = maxHeap.size();
        int rightSize = minHeap.size();

        if (((leftSize + rightSize) & 1) == 1) {
            return maxHeap.peek();
        } else {
            return ((double) minHeap.peek() + (double) maxHeap.peek()) / 2;
        }
    }
}