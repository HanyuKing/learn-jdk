package bytebuddy;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.io.File;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

/**
 * @Author Hanyu.Wang
 * @Date 2023/3/6 17:14
 * @Description
 * @Version 1.0
 **/
public class RedefineHello {
    public static void main(String[] args) throws Exception {
        new RedefineHello().print();
    }

    public void print() throws Exception {
        ByteBuddyAgent.install();
        new ByteBuddy().redefine(Foo.class)
                .method(ElementMatchers.named("println").and(ElementMatchers.takesArgument(0, String.class)))
                .intercept(MethodDelegation.to(Bar.class))
                .make()
                .load(Foo.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());

        Foo foo = new Foo();
        foo.println("Hanyu");


        ByteBuddyAgent.install();
        new ByteBuddy().redefine(LoadingCache.class)
                .method(ElementMatchers.named("get").and(ElementMatchers.takesArgument(0, String.class)))
                .intercept(FixedValue.value("asdasds"))
                .make()
                .load(Thread.currentThread().getContextClassLoader(), ClassReloadingStrategy.fromInstalledAgent());

        LoadingCache<Object, Object> loadingCache = CacheBuilder.newBuilder()
                .expireAfterWrite(1000L, TimeUnit.MILLISECONDS)
                .refreshAfterWrite(1000L, TimeUnit.MILLISECONDS)
                .expireAfterAccess(1000L, TimeUnit.MILLISECONDS)
                .build(new CacheLoader<Object, Object>() {
                    @Override
                    public Object load(Object o) throws Exception {
                        return "";
                    }
                });

        System.out.println(loadingCache.get(""));
    }
}
