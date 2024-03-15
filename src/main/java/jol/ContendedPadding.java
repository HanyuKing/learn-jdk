package jol;

/**
 * @Author Hanyu.Wang
 * @Date 2024/3/4 16:41
 * @Description
 * @Version 1.0
 **/
public class ContendedPadding {
//    @sun.misc.Contended("a")
//    int a;

    @sun.misc.Contended("b")
    byte b;

//    @sun.misc.Contended("tlr")
//    long threadLocalRandomSeed;
//
//    /** Probe hash value; nonzero if threadLocalRandomSeed initialized */
//    @sun.misc.Contended("tlr")
//    int threadLocalRandomProbe;
//
//    /** Secondary seed isolated from public ThreadLocalRandom sequence */
//    @sun.misc.Contended("tlr")
//    int threadLocalRandomSecondarySeed;
}
