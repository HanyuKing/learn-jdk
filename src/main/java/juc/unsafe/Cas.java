package juc.unsafe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sun.misc.Unsafe;

/**
 * @Author Hanyu.Wang
 * @Date 2024/4/3 15:25
 * @Description
 * @Version 1.0
 **/
public class Cas {
    public static void main(String[] args) throws Exception {
        Unsafe unsafe = UnsafeUtil.getUnsafe();

        int age = 0;
        Person p = new Person(age);

        long ageFieldOffset = unsafe.objectFieldOffset(Person.class.getDeclaredField("age"));

        unsafe.compareAndSwapInt(p, ageFieldOffset, age, age + 1);

        System.out.println(unsafe.getIntVolatile(p, ageFieldOffset));
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Person {
    private int age;
}
