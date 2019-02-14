package asm;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

public class GenericClassTest {
    public static void main(String[] args) throws NoSuchMethodException {
        Method method = GenericClass.class.getMethod("func1", List.class);
        Type[] types = method.getGenericParameterTypes();
        for(Type type : types) {

        }
    }
}
