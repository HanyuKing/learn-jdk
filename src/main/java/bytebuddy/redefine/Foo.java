package bytebuddy.redefine;

public class Foo {
    public String getHello() {
        return "not redefined hello!";
    }
    public String getBye() {
        return "not redefined Bye!";
    }
    public void nothing() {}
}