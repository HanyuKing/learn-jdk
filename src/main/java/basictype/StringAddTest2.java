package basictype;

/**
 * @Author Hanyu.Wang
 * @Date 2024/3/24 19:12
 * @Description
 * @Version 1.0
 **/
public class StringAddTest2 {
    /**
     *  public static java.lang.String add(java.lang.String, java.lang.String, java.lang.String);
     *     Code:
     *        0: new           #2                  // class java/lang/StringBuilder
     *        3: dup
     *        4: invokespecial #3                  // Method java/lang/StringBuilder."<init>":()V
     *        7: aload_0
     *        8: invokevirtual #4                  // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
     *       11: aload_1
     *       12: invokevirtual #4                  // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
     *       15: aload_2
     *       16: invokevirtual #4                  // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
     *       19: invokevirtual #5                  // Method java/lang/StringBuilder.toString:()Ljava/lang/String;
     *       22: areturn
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static String add(String a, String b, String c) {
        return a + b + c;
    }
    public static void main(String[] args) {
        System.out.println(add("a", "b", "c"));
    }
}
