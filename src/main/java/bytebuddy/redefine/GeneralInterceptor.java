package bytebuddy.redefine;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class GeneralInterceptor {
    @RuntimeType
    public static Object intercept(@SuperCall Callable<?> zuper) throws Exception {
        System.out.println("method start");
        try {
            return zuper.call();
        } finally {
            System.out.println("method end");
        }
    }
}