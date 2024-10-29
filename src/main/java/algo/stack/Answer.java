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

        for (int i = 0; i < s.length(); i++) {
            char currCh = s.charAt(i);
            if (Character.isDigit(currCh)) {
                StringBuilder numStr = new StringBuilder(String.valueOf(currCh));
                int j = i + 1;
                while (j < s.length() && Character.isDigit(s.charAt(j))) {
                    numStr.append(s.charAt(j++));
                }
                stack.push(numStr.toString());
                i = j - 1;
            } else if (currCh == '[') {
                stack.push("[");
            } else if (Character.isLetter(currCh)) {
                StringBuilder sbString = new StringBuilder(String.valueOf(currCh));
                int j = i + 1;
                while (j < s.length() && Character.isLetter(s.charAt(j))) {
                    sbString.append(s.charAt(j++));
                }
                stack.push(sbString.toString());
                i = j - 1;
            } else {
                StringBuilder topS = new StringBuilder();
                while (!"[".equals(stack.peek())) {
                    topS.insert(0, stack.pop());
                }
                stack.pop(); // [
                int repeat = Integer.parseInt(stack.pop());
                StringBuilder newTopS = new StringBuilder();
                while (repeat-- > 0) {
                    newTopS.append(topS);
                }
                stack.push(newTopS.toString());
            }
        }

        while (!stack.isEmpty()) {
            str.insert(0, stack.pop());
        }

        return str.toString();
    }

    @Test
    public void testP155() {
        MinStack minStack = new MinStack();
        minStack.push(1);
    }
}
