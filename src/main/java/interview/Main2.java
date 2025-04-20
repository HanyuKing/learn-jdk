package interview;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Hanyu.Wang
 * @Date 2025/3/6 19:36
 * @Description
 * @Version 1.0
 **/
public class Main2 {
    public List<List<Integer>> findTarget(int[] data, int k, int m) {
        List<List<Integer>> result = new ArrayList<>();
        doFindTarget(data, k, m, 0, new ArrayList<>(), 0, result);
        return result;
    }

    private void doFindTarget(int[] data,
                              int k,
                              int m,
                              int start,
                              List<Integer> current,
                              int currentSum,
                              List<List<Integer>> result) {

        if (current.size() == k && currentSum == m) {
            result.add(new ArrayList<>(current));
            return;
        }

        if (current.size() > k || currentSum > m) {
            return;
        }

        for (int i = start; i < data.length; i++) {

            current.add(data[i]);

            doFindTarget(data, k, m, i + 1, current, currentSum + data[i], result);

            current.remove(current.size() - 1);
        }
    }

    @Test
    public void test3() {
        int data[] = {1,2,3,4,5,6,8,10};
        int k = 5;
        int m = 1;

        System.out.println(findTarget(data, k, m));
    }

    @Test
    public void test1() {
        int data[] = {1,2,3,4,5,6,8,10};
        int k = 2;
        int m = 7;

        System.out.println(findTarget(data, k, m));
    }

    @Test
    public void test2() {
        int data[] = {1,2,3,4,5,6,8,10};
        int k = 3;
        int m = 7;

        System.out.println(findTarget(data, k, m));
    }

    @Test
    public void test4() {
        int data[] = {1,2,3,4,5,6,8,10};
        int k = 1;
        int m = 1;

        System.out.println(findTarget(data, k, m));
    }
}
