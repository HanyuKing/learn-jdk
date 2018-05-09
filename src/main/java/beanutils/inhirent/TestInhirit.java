package beanutils.inhirent;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Hanyu King
 * @since 2018-04-16 14:53
 */
public class TestInhirit {
    public static void main(String[] args) {
        Father father = new Father();
        father.setName("Hanyu");
        father.setAge(20);

        Son dest = new Son();
        try {
            BeanUtils.copyProperties(dest, father);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        System.out.println(dest.getAge());
    }
}
