package interview;

import org.junit.Test;

import java.util.*;

public class Problem {
    /**
     * 判断数组包含下面哪个类型，并返回最先达到类型的下标
     * 类型有ABCDEF，AAABBB
     * ABCDEF：所有数字不同，不用考虑顺序
     * AAABBB：前三个一样，后三个一样
     *
     * 比如[1,5,3,4,7,6] 返回 ABCDEF ， 0，5
     * 比如[1,1,5,3,4,7,6] 返回 ABCDEF ， 1，6
     * 比如[1,5,1,3,1,4,1,7,1,6] 返回  ， -1,-1
     * 比如[1,1,1,3,4,7,6] 返回  ， -1，-1
     * 比如[1,1,1,2,2,2] 返回 AAABBB ， 0，5
     * 比如[1,1,1,1,2,2,2] 返回 AAABBB ， 1，6
     * 比如[1,1,1,1,1,2,2] 返回  ， -1,-1
     * 比如[1,1,1,2,1,2,1,2] 返回  ，-1,-1
     * 比如[1,1,1,1,1,1,1] 返回  ， -1,-1
     */

    @Test
    public void test1() {
        System.out.println(findType(new int[] {1,5,3,4,7,6}));
    }
    @Test
    public void test2() {
        System.out.println(findType(new int[] {1,1,5,3,4,7,6}));
    }
    @Test
    public void test3() {
        System.out.println(findType(new int[] {1,5,1,3,1,4,1,7,1,6}));
    }
    @Test
    public void test4() {
        System.out.println(findType(new int[] {1,1,1,3,4,7,6}));
    }
    @Test
    public void test5() {
        System.out.println(findType(new int[] {1,1,1,2,2,2}));
    }
    @Test
    public void test6() {
        System.out.println(findType(new int[] {1,1,1,1,2,2,2}));
    }
    @Test
    public void test7() {
        System.out.println(findType(new int[] {1,1,1,1,1,2,2}));
    }
    @Test
    public void test8() {
        System.out.println(findType(new int[] {1,1,1,2,1,2,1,2}));
    }
    @Test
    public void test9() {
        System.out.println(findType(new int[] {1,1,1,1,1,1,1}));
    }

    static class Value {
        public String type;
        public int start;
        public int end;
        public Value(String type, int start, int end) {
            this.type = type;
            this.start = start;
            this.end = end;
        }

        @Override
        public String toString() {
            return this.type + ", " + this.start + ", " + this.end;
        }
    }

    /**
     * O(6*N)
     *
     * @param nums
     * @return
     */
    public Value findType(int[] nums) {
        if (nums == null || nums.length < 6) {
            return null;
        }
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < 6; i++) {
            queue.offer(nums[i]);
        }
        String type = judge(queue);
        if (!type.equals("")) {
            return new Value(type, 0, 5);
        }

        for (int i = 6; i < nums.length; i++) {
            queue.poll();
            queue.offer(nums[i]);
            type = judge(queue);
            if (!type.equals("")) {
                return new Value(type, i - 5, i);
            }
        }
        return new Value("", -1, -1);
    }

    private String judge(Queue<Integer> queue) {
        int queenSize = queue.size();
        List<Integer> list = new ArrayList<>();
        while (!queue.isEmpty()) {
            list.add(queue.poll());
        }
        queue.addAll(list);

        Set<Integer> set = new HashSet<>(list);
        if (set.size() == queenSize) {
            return "ABCDEF";
        }
        int firstValue = list.get(0);
        for (int i = 1; i < 3; i++) {
            if (list.get(i) != firstValue) {
                return "";
            }
        }
        int secondValue = list.get(3);
        if (firstValue == secondValue) {
            return "";
        }
        for (int i = 4; i < 6; i++) {
            if (list.get(i) != secondValue) {
                return "";
            }
        }

        return "AAABBB";
    }

}
