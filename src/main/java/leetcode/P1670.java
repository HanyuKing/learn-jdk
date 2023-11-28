package leetcode;

import java.util.LinkedList;

/**
 * @Author Hanyu.Wang
 * @Date 2023/11/28 10:40
 * @Description
 * @Version 1.0
 **/
public class P1670 {
    public static void main(String[] args) {

    }

    class FrontMiddleBackQueue {
        private LinkedList<Integer> list = null;

        public FrontMiddleBackQueue() {
            this.list = new LinkedList<>();
        }

        public void pushFront(int val) {
            list.addFirst(val);
        }

        public void pushMiddle(int val) {
            list.add(list.size() / 2, val);
        }

        public void pushBack(int val) {
            list.addLast(val);
        }

        public int popFront() {
            if (list.size() == 0) {
                return -1;
            }
            return list.pop();
        }

        public int popMiddle() {
            if (list.size() == 0) {
                return -1;
            }
            if ((list.size() & 1) == 0) {
                return list.remove(list.size() / 2 - 1);
            }
            return list.remove(list.size() / 2);
        }

        public int popBack() {
            if (list.size() == 0) {
                return -1;
            }
            return list.removeLast();
        }
    }
}
