package other;

/**
 * @author wanghanyu
 * @create 2017-09-28 19:26
 */
public class StaticTest {
    public static void main(String[] args) {
        Static s1 = new Static();
        Static s2 = new Static();
        Static s3 = new Static();

        System.out.println(s1.obj);
        System.out.println(s2.obj);
        System.out.println(s3.obj);
    }
}

class Static {
    public Object obj = StaticMethod.newObj();
}

class StaticMethod {
    public static Object newObj() {
        return new Object();
    }
}
