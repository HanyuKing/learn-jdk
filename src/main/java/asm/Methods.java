package asm;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Methods {

    public static List<String> getParameterNames(Method method) {
        Parameter[] parameters = method.getParameters();
        List<String> parameterNames = new ArrayList<>();

        for (Parameter parameter : parameters) {
            if(!parameter.isNamePresent()) {
                throw new IllegalArgumentException("Parameter names are not present!");
            }

            String parameterName = parameter.getName();
            parameterNames.add(parameterName);
        }

        return parameterNames;
    }

    private Methods(){}

    public static void main(String[] args) throws NoSuchMethodException {
        Method[] methods = GenericClass.class.getMethods();
        for(Method method : methods) {
            String[] params = getParameterNames(method).toArray(new String[0]);
            System.out.println(Arrays.toString(params));
        }
    }
}