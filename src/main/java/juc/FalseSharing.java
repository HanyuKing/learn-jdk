package juc;

import sun.misc.Contended;

public final class FalseSharing
    implements Runnable
{
    public final static int NUM_THREADS = 4; // change
    public final static long ITERATIONS = 500L * 1000L * 1000L;
    private final int arrayIndex;

    private static ContendedValue[] longs = new ContendedValue[NUM_THREADS];
    static
    {
        for (int i = 0; i < longs.length; i++)
        {
            longs[i] = new ContendedValue();
        }
    }

    public FalseSharing(final int arrayIndex)
    {
        this.arrayIndex = arrayIndex;
    }

    public static void main(final String[] args) throws Exception
    {
        final long start = System.nanoTime();
        runTest();
        System.out.println("duration = " + (System.nanoTime() - start));

        // 3438145000  PaddingValue (padding)
        // 30853908000 PaddingValue (no padding)

        // 33559656000 ContendedValue
        // 3470833000  ContendedValue add vm option (-XX:-RestrictContended)
    }

    private static void runTest() throws InterruptedException
    {
        Thread[] threads = new Thread[NUM_THREADS];

        for (int i = 0; i < threads.length; i++)
        {
            threads[i] = new Thread(new FalseSharing(i));
        }

        for (Thread t : threads)
        {
            t.start();
        }

        for (Thread t : threads)
        {
            t.join();
        }
    }

    public void run()
    {
        long i = ITERATIONS + 1;
        while (0 != --i)
        {
            longs[arrayIndex].value = i;
        }
    }

    @Contended
    static class ContendedValue
    {
        protected volatile long value;
    }

    static class PaddingLongValue extends RhsPadding
    {
        public long getValue() {
            return this.value;
        }
    }

    static class LhsPadding
    {
        // comment out
        protected byte
                p10, p11, p12, p13, p14, p15, p16, p17,
                p20, p21, p22, p23, p24, p25, p26, p27,
                p30, p31, p32, p33, p34, p35, p36, p37,
                p40, p41, p42, p43, p44, p45, p46, p47,
                p50, p51, p52, p53, p54, p55, p56, p57,
                p60, p61, p62, p63, p64, p65, p66, p67,
                p70, p71, p72, p73, p74, p75, p76, p77;
    }

    static class Value extends LhsPadding
    {
        protected volatile long value;
    }

    static class RhsPadding extends Value
    {
        // comment out
        protected byte
                p90, p91, p92, p93, p94, p95, p96, p97,
                p100, p101, p102, p103, p104, p105, p106, p107,
                p110, p111, p112, p113, p114, p115, p116, p117,
                p120, p121, p122, p123, p124, p125, p126, p127,
                p130, p131, p132, p133, p134, p135, p136, p137,
                p140, p141, p142, p143, p144, p145, p146, p147,
                p150, p151, p152, p153, p154, p155, p156, p157;
    }

}