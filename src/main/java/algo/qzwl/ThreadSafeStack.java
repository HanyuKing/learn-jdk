package algo.qzwl;

import sun.misc.Unsafe;

public class ThreadSafeStack {
    // 栈节点类
    static class Node {
        int value;
        volatile Node next;  // 使用volatile保证内存可见性

        Node(int value) {
            this.value = value;
            this.next = null;
        }
    }

    private volatile Node top;  // 栈顶指针，使用volatile保证内存可见性
    private static final int MAX_SIZE = 100;  // 栈的最大长度

    // 栈构造函数
    public ThreadSafeStack() {
        top = null;  // 初始化栈为空
    }

    // push操作
    public boolean push(int value) {
        Node newNode = new Node(value);
        while (true) {
            Node currentTop = top;  // 当前栈顶
            newNode.next = currentTop;  // 新节点指向栈顶
            // 使用testAndSet进行原子操作：如果top仍然是currentTop，则更新top
            if (testAndSet(top, currentTop, newNode)) {
                return true;  // push成功
            }
            // 如果top已经发生变化，表示其他线程修改了栈顶，我们重新尝试
        }
    }

    // pop操作
    public Integer pop() {
        while (true) {
            Node currentTop = top;  // 当前栈顶
            if (currentTop == null) {
                return null;  // 栈为空
            }
            Node newTop = currentTop.next;  // 新栈顶
            // 使用testAndSet进行原子操作：如果top仍然是currentTop，则更新top
            if (testAndSet(top, currentTop, newTop)) {
                return currentTop.value;  // pop成功，返回栈顶值
            }
            // 如果top已经发生变化，表示其他线程修改了栈顶，我们重新尝试
        }
    }

    // 使用testAndSet原子操作来进行栈顶指针的更新
    private boolean testAndSet(Node top, Node expected, Node target) {
        if (top == expected) {
            // 模拟testAndSet: 如果当前top是expected，则更新为target
            top = target;
            return true;  // 返回true表示成功更新
        }
        // Unsafe.getUnsafe().compareAndSwapObject(top, offset, expected, target); todo
        return false;  // 否则返回false
    }

    // 检查栈是否为空
    public boolean isEmpty() {
        return top == null;
    }

    // 打印栈的所有元素（仅用于调试）
    public void printStack() {
        Node current = top;
        while (current != null) {
            System.out.print(current.value + " ");
            current = current.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        ThreadSafeStack stack = new ThreadSafeStack();

        // 测试push和pop操作
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.printStack();  // 应该输出: 3 2 1

        System.out.println("Popped: " + stack.pop());  // 应该输出: 3
        stack.printStack();  // 应该输出: 2 1

        stack.push(4);
        stack.push(5);
        stack.printStack();  // 应该输出: 5 4 2 1
    }
}
