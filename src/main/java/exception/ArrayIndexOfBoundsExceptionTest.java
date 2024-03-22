package exception;

/**
 * @Author Hanyu.Wang
 * @Date 2024/3/22 09:53
 * @Description
 * @Version 1.0
 **/
public class ArrayIndexOfBoundsExceptionTest {
    public static void main(String[] args) {
        int[] a = new int[10];

        System.out.println(a[1]);

        System.out.println(a[Integer.MAX_VALUE]);
    }
}
