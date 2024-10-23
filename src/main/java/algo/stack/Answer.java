package algo.stack;

import algo.hot200.Base;
import org.junit.Test;

import java.util.Stack;

/**
 * @Author Hanyu.Wang
 * @Date 2024/10/23 10:43
 * @Description
 * @Version 1.0
 **/
public class Answer extends Base {

    @Test
    public void testP394() {
        // todo
        String s = "3[a]2[bc]"; // "aaabcbc"
        print(decodeString(s));
//
        s = "3[a2[c]]"; // "accaccacc"
        print(decodeString(s));
//
        s = "abc3[cd]xyz"; // "abccdcdcdxyz"
        print(decodeString(s));
    }

    public String decodeString(String s) {
        Stack<String> stack = new Stack<>();
        StringBuilder str = new StringBuilder();

        return str.toString();
    }

    @Test
    public void testP155() {
        MinStack minStack = new MinStack();
        minStack.push(1);
    }
}
