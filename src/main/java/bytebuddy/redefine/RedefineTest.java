package bytebuddy.redefine;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;
import org.junit.Before;
import org.junit.Test;

import static net.bytebuddy.matcher.ElementMatchers.named;

public class RedefineTest {
    ClassReloadingStrategy classReloadingStrategy;
    @Before
    public void init() {
        ByteBuddyAgent.install();
        classReloadingStrategy = ClassReloadingStrategy.fromInstalledAgent();
    }

    @Test
    public void redefineTest() {
        // okay
        new ByteBuddy()
                .redefine(Foo.class)
                .method(named("getHello"))
                .intercept(FixedValue.value("ByteBuddy Hello!"))
                .make()
                .load(Foo.class.getClassLoader(), classReloadingStrategy);
        Foo foo = new Foo();
        System.out.println(foo.getHello());

        // error
        new ByteBuddy()
                .redefine(Foo.class)
                .method(named("getBye"))
                .intercept(MethodDelegation.to(GeneralInterceptor.class))
                .make()
                .load(Foo.class.getClassLoader(), classReloadingStrategy);

        foo = new Foo();
        System.out.println(foo.getBye());
        //foo.nothing();
    }
}