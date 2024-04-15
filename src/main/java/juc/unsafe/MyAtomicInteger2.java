package juc.unsafe;

import sun.misc.Unsafe;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MyAtomicInteger2 {
    private volatile int value;

    public AtomicLong count = new AtomicLong(0);

    public int incrementAndGet() {
        int expect;
        do {
            expect = U.getIntVolatile(this, valueOffset);
            count.incrementAndGet();
        } while (!U.compareAndSwapInt(this, valueOffset, expect, expect + 1));
        return expect + 1;
    }

    public int getValue() {
        return U.getIntVolatile(this, valueOffset);
    }

    private static final Unsafe U = UnsafeUtil.getUnsafe();

    private static final long valueOffset;

    static {
        try {
            valueOffset = U.objectFieldOffset
                    (MyAtomicInteger2.class.getDeclaredField("value"));
        } catch (Exception ex) { throw new Error(ex); }
    }
}
