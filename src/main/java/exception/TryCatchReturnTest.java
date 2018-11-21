package exception;

/**
 * @author Hanyu King
 * @since 2018-10-25 9:59
 */
public class TryCatchReturnTest {
    public static void main(String[] args) {
        System.out.println(test());
    }

    public static int test() {
        try {
            return 1;
        } finally {
            return 2;
        }
    }
}
