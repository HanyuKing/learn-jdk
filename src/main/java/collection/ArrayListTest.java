package collection;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.ArrayList;
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
