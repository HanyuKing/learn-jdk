package jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hanyu King
 * @since 2018-05-30 10:51
 */
public class FinalizerTest {

    private static class Lock{}
    static Lock lock = new Lock();

    public static void main(String[] args) {
        FinalizerObject finalizerObject = new FinalizerObject();
        finalizerObject.setName("Hanyu");
        System.gc();
        System.out.println(finalizerObject.getName());

    }
}

class FinalizerObject {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}