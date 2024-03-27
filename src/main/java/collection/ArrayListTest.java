package collection;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Hanyu King
 * @since 2018-11-21 15:13
 */
public class ArrayListTest {

    @Test
    public void testAddAll() {
        List<AAA> list = new ArrayList<AAA>();
        list.add(new AAA(1));
        list.add(new AAA(2));

        List<AAA> list2 = new ArrayList<AAA>();
        list2.addAll(list);
        list2.get(0).setAge(100);
        list2.add(new AAA(3));

        System.out.println(list);
        System.out.println(list2);

    }

    /**
     * https://www.geeksforgeeks.org/remove-element-arraylist-java/
     *
     * 1. Using remove() method by indexes(default)
     * 2. Using remove() method by values
     * 3. Using Iterator.remove() method
     *
     */

    @Test
    public void testRemove() {
        List<String> list = Lists.newArrayList("1", "2");
        for (String l : list) {
            if ("2".equals(l)) { // todo 把2换成1
                list.remove(l);
            }
        }

       // System.out.println(list);
    }

    @Test
    public void testRemove2() {
        List<String> list = Lists.newArrayList("1", "2");
        int size = list.size();

        for (int i = 0; i < size; i++) {
            String val = list.get(i);
            if ("2".equals(val)) {
                list.remove(val);
            }
        }

        System.out.println(list);
    }

    @Test
    public void testTravelAndRemove() {
        List<String> list = Lists.newArrayList("1", "2", "3");
        Iterator<String> it = list.iterator();

        while (it.hasNext()) {
            String value = it.next();
            if ("2".equals(value)) {
                it.remove();
            }
        }

        it = list.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

    private static class AAA {
        private int age;

        public AAA(int age) {
            this.age = age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getAge() {
            return age;
        }
    }
}
