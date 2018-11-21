package concurrent;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Hanyu King
 * @since 2018-09-14 11:09
 */
public class AtomicUtilTest {

    @Test
    public void testAtomicLongGetAndAdd() {
        AtomicLong atomicLong = new AtomicLong(0);
        System.out.println(atomicLong.getAndAdd(99));
        System.out.println(atomicLong.getAndAdd(1));
        System.out.println(atomicLong.get());
    }
}
