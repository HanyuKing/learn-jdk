package algo.stack;

import java.util.LinkedList;
import java.util.Stack;

/*
【不使用辅助栈的Java解法】
栈中每个元素代表的是要压入元素与当前栈中最小值的差值
有个很重要问题：
在弹出时如何维护min？
因为每次压入新的元素时，压入的都是与当前栈中最小值的差值（还未压入当前元素），故在弹出元素时，若弹出了当前最小值，因为栈中记录了当前元素与【之前】最小值的差值，故根据这个记录可以更新弹出元素后的最小值。
接下来看代码吧
 */
class MinStack2 {
    // 记录每个元素与【未压入】该元素时栈中最小元素的差值
    LinkedList<Long> stack;
    // 当前【已压入】栈中元素的最小值
    private long min;
    public MinStack2() {
        stack = new LinkedList();
    }

    public void push(int val) {
        // 压入第一个元素
        if(stack.isEmpty()){
            min = val;
            stack.addFirst(0L);
            return;
        }
        // 栈不为空时，每次压入计算与min的差值后压入结果
        stack.push((long)val-min);
        // 更新min
        min = Math.min((long)val,min);
        // 上面两个语句是不能颠倒的！一定是先压入，在更新，因为min一定是当前栈中的最小值
    }

    public void pop() {
        long pop = stack.removeFirst();
        // 当弹出元素小于0时，说明弹出元素是当前栈中的最小值，要更新最小值
        if(pop<0){
            // 因为对于当前弹出的元素而言，计算压入栈中的值时，计算的是该元素与【未压入】该元素时
            // 栈中元素的最小值的差值，故弹出该元素后栈中的最小值就是未压入该元素时的最小值
            // 即当前元素的值（min）减去两者的差值
            long lastMin = min;
            min = lastMin - pop;
        }
        // 若大于等于0，不会对min有影响
    }

    public int top() {
        long peek = stack.peek();
        // 若当前栈顶小于等于0，说明最小值就是栈顶元素
        if(peek<=0) return (int)min;
        // 否则就是min+peek
        return (int)(min+peek);
    }

    public int getMin() {
        return (int)min;
    }
}
public class MinStack {
    private Stack<Integer> stack = new Stack<>();
    private Stack<Integer> minStack = new Stack<>();
    public MinStack() {
        minStack.push(Integer.MAX_VALUE);
    }
    
    public void push(int val) {
        stack.push(val);
        minStack.push(Math.min(minStack.peek(), val));
    }
    
    public void pop() {
        stack.pop();
        minStack.pop();
    }
    
    public int top() {
        return stack.peek();
    }
    
    public int getMin() {
        return minStack.peek();
    }
}