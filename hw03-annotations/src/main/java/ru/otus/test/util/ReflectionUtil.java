package ru.otus.test.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeMap;

public class ReflectionUtil {

    public ReflectionUtil() {}

    public static Class<?> getClass(String name){
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object callMethod(Object object, String name, Object... args) {
        try {
            var method = object.getClass().getDeclaredMethod(name, toClasses(args));
            method.setAccessible(true);
            return method.invoke(object, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T instantiate(Class<T> type, Object... args) {
        try {
            if (args.length == 0) {
                return type.getDeclaredConstructor().newInstance();
            } else {
                Class<?>[] classes = toClasses(args);
                return type.getDeclaredConstructor(classes).newInstance(args);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Class<?>[] toClasses(Object[] args) {
        return Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new);
    }

    public static TreeMap<Integer, Method> orderMethodsByAnnotations(Method[] methods){
        var i = 0;
        TreeMap<Integer, Method> treeMap = new TreeMap<>(Comparator.comparingInt(e -> e));

        for (Method method : methods) {
            for (Annotation annotation : method.getDeclaredAnnotations()) {
                if (annotation instanceof ru.otus.test.annotations.Before){
                    treeMap.put(1000 + i, method);
                }else if(annotation instanceof ru.otus.test.annotations.Test) {
                    treeMap.put(2 * 1000 + i, method);
                }else if(annotation instanceof ru.otus.test.annotations.After){
                    treeMap.put(3 * 1000 + i, method);
                }
                i++;
            }
        }
        return treeMap;
    }
}
