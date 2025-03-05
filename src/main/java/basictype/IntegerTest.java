package basictype;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Hanyu King
 * @since 2018-05-29 10:59
 */
public class IntegerTest {

    @Test
    public void testEquals() {
        Integer a = 200;
        Integer b = 200;
        System.out.println(a == b);
        System.out.println(a.equals(b));
    }

    public static void main(String[] args) {
        Integer i = 14;
        Integer b = 199;
        Integer c = 200;

        System.out.println(b >= c);
        System.out.println(i >= 5);

        new IntegerTest().func();
    }

    public void func() {
        StackTraceElement[] stackTraceElements = (new Throwable()).getStackTrace();
        for (int i = 0; i < stackTraceElements.length; i++) {
            StackTraceElement stackTraceElement = stackTraceElements[i];
            System.out.println("index=" + i + "----------------------------------");
            System.out.println("className=" + stackTraceElement.getClassName());
            System.out.println("fileName=" + stackTraceElement.getFileName());
            System.out.println("methodName=" + stackTraceElement.getMethodName());
            System.out.println("lineNumber=" + stackTraceElement.getLineNumber());
        }
    }

    @Test
    public void testByteLength() {
        char c = 'c';
        System.out.println(c);
    }

    @Test
    public void testAnd() {
        for(int i = 0; i < 10; i++) {
            System.out.println(i & 1);
        }
    }

    @Test
    public void testObjectSize() {
        Sku sku = new Sku();
        System.out.println(sku);
    }

    @Test
    public void testStrLen() {
        System.out.println("Hanyu King".length());
        System.out.println("王".length());
        char a = '王';
        System.out.println(a);
    }

    @Test
    public void testHashMapHashCode() {
        Map<Sku, Integer> map = new HashMap<>();
        map.put(new Sku("hanyu"), 1);
        System.out.println(map.get(new Sku("hanyu")));
    }
}

class Sku {
    private String name;

    public Sku(String name) {
        this.name = name;
    }

    public Sku() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sku sku = (Sku) o;
        return Objects.equals(name, sku.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
