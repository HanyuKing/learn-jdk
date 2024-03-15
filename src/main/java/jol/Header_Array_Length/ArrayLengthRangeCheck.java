package jol.Header_Array_Length;


/**
 * @Author Hanyu.Wang
 * @Date 2024/3/1 19:54
 * @Description
 * @Version 1.0
 **/
public class ArrayLengthRangeCheck {

    private int[] a = new int[12345];

//    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
//    @Benchmark
    public int testHanyu() {
        return a[42];
    }

    public static void main(String[] args) {
        new ArrayLengthRangeCheck().testHanyu();
    }
}