package bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.io.File;
import java.io.PrintStream;

/**
 * @Author Hanyu.Wang
 * @Date 2023/3/6 17:14
 * @Description
 * @Version 1.0
 **/
public class Hello {
    public static void main(String[] args) throws Exception {
        new Hello().print();
    }

    public void print() throws Exception {
        ByteBuddyAgent.install();
        new ByteBuddy().redefine(PrintStream.class)
                .method(ElementMatchers.named("println")
                        .and(ElementMatchers.returns(Void.class)
                                .and(ElementMatchers.takesArgument(1, String.class))))
                .intercept(MethodDelegation.to(MyPrint.class))
                .make()
                .load(Thread.currentThread().getContextClassLoader(), ClassReloadingStrategy.fromInstalledAgent());

        PrintStream printStream = new PrintStream(new File("aaa.txt"));
        //new Hello().print();
        printStream.println("Hello2");
    }
}
