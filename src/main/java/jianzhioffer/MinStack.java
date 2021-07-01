package jianzhioffer;

import java.util.Stack;

/**
 * 辅助栈
 */
public class MinStack {
    private Stack<Integer> stack = new Stack<>();
    private Stack<Integer> stack2 = new Stack<>();

    public static void main(String[] args) {
        MinStack ms = new MinStack();
        ms.push(-1);
        ms.push(2);
        System.out.println(ms.min());
        System.out.println(ms.top());
        ms.pop();
        ms.push(1);
        System.out.println(ms.top());
        System.out.println(ms.min());
    }

    public void push(int node) {
        stack.push(node);
        if (stack2.empty()) {
            stack2.push(node);
        } else if (node <= stack2.peek()) {
            stack2.push(node);
        }
    }

    public void pop() {

        if (stack.peek() == stack2.peek()) {
            stack2.pop();
        }
        stack.pop();
    }

    //
    public int top() {
        return stack.peek();
    }

    public int min() {
        return stack2.peek();
    }
}


