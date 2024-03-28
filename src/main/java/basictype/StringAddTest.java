package basictype;

public class StringAddTest {
    public static void main(String[] args) {
        int loopTotal = 90000;

        test1(loopTotal);
        test2(loopTotal);
        test3(loopTotal);

    }

    public static void test3(int loopTotal) {
        long start = System.currentTimeMillis();

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < loopTotal; i++) {
            sb.append(new StringBuilder(i).toString());
        }

        System.out.println(System.currentTimeMillis() - start);
    }

    public static void test2(int loopTotal) {
        long start = System.currentTimeMillis();

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < loopTotal; i++) {
            sb.append(i);
        }

        System.out.println(System.currentTimeMillis() - start);
    }

    public static void test1(int loopTotal) {
        long start = System.currentTimeMillis();

        String s = "";
        for(int i = 0; i < loopTotal; i++) {
            s = s + i;
        }

        System.out.println(System.currentTimeMillis() - start);
    }
}
