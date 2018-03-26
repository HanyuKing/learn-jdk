package exception;

/**
 * @author wanghanyu
 * @create 2018-03-03 20:40
 */
public class TryNoCatchTest {
    public static void main(String[] args) {
        try {
            if(true) {
                throw new RuntimeException();
            }
        } finally {
            System.out.println("finally");
        }
        System.out.println("end");
    }
}
