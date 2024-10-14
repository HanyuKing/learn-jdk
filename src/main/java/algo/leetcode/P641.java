package algo.leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2022/8/15 15:56
 * @Description
 * @Version 1.0
 **/
public class P641 {
    public static void main(String[] args) {
        MyCircularDeque obj = new MyCircularDeque(3);
        boolean param_1 = obj.insertFront(2);
        boolean param_2 = obj.insertLast(1);
        System.out.println(obj.getFront());
    }
}

//设计实现双端队列。
//
// 实现 MyCircularDeque 类:
//
//
// MyCircularDeque(int k) ：构造函数,双端队列最大为 k 。
// boolean insertFront()：将一个元素添加到双端队列头部。 如果操作成功返回 true ，否则返回 false 。
// boolean insertLast() ：将一个元素添加到双端队列尾部。如果操作成功返回 true ，否则返回 false 。
// boolean deleteFront() ：从双端队列头部删除一个元素。 如果操作成功返回 true ，否则返回 false 。
// boolean deleteLast() ：从双端队列尾部删除一个元素。如果操作成功返回 true ，否则返回 false 。
// int getFront() )：从双端队列头部获得一个元素。如果双端队列为空，返回 -1 。
// int getRear() ：获得双端队列的最后一个元素。 如果双端队列为空，返回 -1 。
// boolean isEmpty() ：若双端队列为空，则返回 true ，否则返回 false 。
// boolean isFull() ：若双端队列满了，则返回 true ，否则返回 false 。
//
//
//
//
// 示例 1：
//
//
//输入
//["MyCircularDeque", "insertLast", "insertLast", "insertFront", "insertFront",
//"getRear", "isFull", "deleteLast", "insertFront", "getFront"]
//[[3], [1], [2], [3], [4], [], [], [], [4], []]
//输出
//[null, true, true, true, false, 2, true, true, true, 4]
//
//解释 4431
//MyCircularDeque circularDeque = new MycircularDeque(3); // 设置容量大小为3
//circularDeque.insertLast(1);			        // 返回 true
//circularDeque.insertLast(2);			        // 返回 true
//circularDeque.insertFront(3);			        // 返回 true
//circularDeque.insertFront(4);			        // 已经满了，返回 false
//circularDeque.getRear();  				// 返回 2
//circularDeque.isFull();				        // 返回 true
//circularDeque.deleteLast();			        // 返回 true
//circularDeque.insertFront(4);			        // 返回 true
//circularDeque.getFront();				// 返回 4
// 
//
//
//
// 提示：
//
//
// 1 <= k <= 1000
// 0 <= value <= 1000
// insertFront, insertLast, deleteFront, deleteLast, getFront, getRear, isEmpty,
// isFull 调用次数不大于 2000 次
//
//
// Related Topics 设计 队列 数组 链表 👍 167 👎 0


//leetcode submit region begin(Prohibit modification and deletion)
class MyCircularDeque {
    private int capacity;
    private Node head;
    private Node tail;
    private int size;

    /**
     * 构造函数,双端队列最大为 k
     * @param k
     */
    public MyCircularDeque(int k) {
        this.capacity = k;
        size = 0;
    }

    /**
     * 将一个元素添加到双端队列头部。 如果操作成功返回 true ，否则返回 false
     * @param value
     * @return
     */
    public boolean insertFront(int value) {
        if (isFull()) {
            return false;
        }
        Node newNode = new Node(value, head, null);
        if (head == null) {
            head = tail = newNode;
        } else {
            head.pre = newNode;
            head = newNode;
        }
        size++;
        return true;
    }

    /**
     * 将一个元素添加到双端队列尾部。如果操作成功返回 true ，否则返回 false
     * @param value
     * @return
     */
    public boolean insertLast(int value) {
        if (isFull()) {
            return false;
        }
        Node newNode = new Node(value, null, tail);
        if (tail == null) {
            tail = head = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
        return true;
    }

    /**
     * 从双端队列头部删除一个元素。 如果操作成功返回 true ，否则返回 false
     * @return
     */
    public boolean deleteFront() {
        if (isEmpty()) {
            return false;
        }
        if (head == tail) {
            head = tail = null;
        } else {
            Node preHead = head;
            head = head.next;
            preHead.next = null;
            head.pre = null;
        }
        size--;
        return true;
    }

    /**
     * 从双端队列尾部删除一个元素。如果操作成功返回 true ，否则返回 false
     * @return
     */
    public boolean deleteLast() {
        if (isEmpty()) {
            return false;
        }
        if (head == tail) {
            head = tail = null;
        } else {
            Node preTail = tail.pre;
            tail = preTail;
            tail.next = null;
        }
        size--;
        return true;
    }

    /**
     * 从双端队列头部获得一个元素。如果双端队列为空，返回 -1
     * @return
     */
    public int getFront() {
        if (isEmpty()) {
            return -1;
        }
        return head.value;
    }

    /**
     * 获得双端队列的最后一个元素。 如果双端队列为空，返回 -1
     * @return
     */
    public int getRear() {
        if (isEmpty()) {
            return -1;
        }
        return tail.value;
    }

    /**
     * 若双端队列为空，则返回 true ，否则返回 false
     * @return
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 若双端队列满了，则返回 true ，否则返回 false
     * @return
     */
    public boolean isFull() {
        return size == capacity;
    }

    private class Node {
        int value;
        Node next;
        Node pre;
        public Node(int value, Node next, Node pre) {
            this.value = value;
            this.next = next;
            this.pre = pre;
        }
    }
}

/**
 * Your MyCircularDeque object will be instantiated and called as such:
 * MyCircularDeque obj = new MyCircularDeque(k);
 * boolean param_1 = obj.insertFront(value);
 * boolean param_2 = obj.insertLast(value);
 * boolean param_3 = obj.deleteFront();
 * boolean param_4 = obj.deleteLast();
 * int param_5 = obj.getFront();
 * int param_6 = obj.getRear();
 * boolean param_7 = obj.isEmpty();
 * boolean param_8 = obj.isFull();
 */
//leetcode submit region end(Prohibit modification and deletion)
