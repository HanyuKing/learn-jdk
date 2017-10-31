package other;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wanghanyu
 * @create 2017-08-02 9:51
 */
public class CollectionTest {

    @Test
    public void digitTest() {
        int capacity = 2;
        capacity |= capacity >>> 1;
        System.out.println(capacity>>>1);
    }

    @Test
    public void retainAllTest() {
        List<Integer> l1 = new ArrayList<Integer>();
        List<Integer> l2 = new ArrayList<Integer>();
        l1.add(1);
        l1.add(2);
        l1.add(3);
        l2.add(1);
        //l2.add(2);
        l1.retainAll(l2);

        System.out.println(l1);
        System.out.println(l2);
    }
}
