package interview.xhzn.q1;

public class Singleton {
    private Singleton() {}
    private static class SingletonHolder {
        private static final Singleton SINGLETON = new Singleton();
    }

    public static Singleton getInstance() {
        return SingletonHolder.SINGLETON;
    }
}