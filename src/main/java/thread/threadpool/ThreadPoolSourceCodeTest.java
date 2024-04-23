package thread.threadpool;

import org.junit.Test;

/**
 * @Author Hanyu.Wang
 * @Date 2024/4/22 16:22
 * @Description
 * @Version 1.0
 **/
public class ThreadPoolSourceCodeTest {
    @Test
    public void testRunState() {
        final int COUNT_BITS = Integer.SIZE - 3;
        final int RUNNING    = -1 << COUNT_BITS;
        final int SHUTDOWN   =  0 << COUNT_BITS;
        final int STOP       =  1 << COUNT_BITS;
        final int TIDYING    =  2 << COUNT_BITS;
        final int TERMINATED =  3 << COUNT_BITS;

        System.out.println("RUNNING->" + Integer.toBinaryString(RUNNING));
        System.out.println("SHUTDOWN->" + Integer.toBinaryString(SHUTDOWN));
        System.out.println("STOP->" + Integer.toBinaryString(STOP));
        System.out.println("TIDYING->" + Integer.toBinaryString(TIDYING));
        System.out.println("TERMINATED->" + Integer.toBinaryString(TERMINATED));

    }
}
