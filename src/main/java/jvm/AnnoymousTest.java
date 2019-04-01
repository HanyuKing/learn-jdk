package jvm;

import java.lang.invoke.MethodHandles;

public class AnnoymousTest {
    public void test() {
        MethodHandles.lookup();
    }

    class Horse {
        public void race() {
            System.out.println("Horse.race()");
        }
    }

    class Deer {
        public void race() {
            System.out.println("Deer.race()");
        }
    }

    class Cobra {
        public void race() {
            System.out.println("How do you turn this on?");
        }
    }
}
