package ru.otus.test.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import ru.otus.test.annotations.Test;
import ru.otus.test.annotations.After;
import ru.otus.test.annotations.Before;

public class ReflectionUtil {

    public ReflectionUtil() {
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

    public static List<List<Method>> getMethodTriple(Method[] methods){
        List<Method> testMethods = getTestMethods(methods);
        List<List<Method>> methodTriple = new ArrayList<>();

        for(Method test : testMethods){
            TreeMap<Integer, Method> treeMap = new TreeMap<>(Comparator.comparingInt(e -> e));

            for(Method method : methods){
                for (Annotation annotation : method.getDeclaredAnnotations()) {
                    if (annotation instanceof Before) {
                        treeMap.put(1, method);
                    } else if (annotation instanceof Test &&
                               method.getName().equals(test.getName())) {
                        treeMap.put(2, method);
                    } else if (annotation instanceof After) {
                        treeMap.put(3, method);
                    }
                }
            }
            methodTriple.add(treeMap.values().stream().toList());
        }
        return methodTriple;
    }

    public static List<Method> getTestMethods(Method[] methods) {
        List<Method> list = new ArrayList<>();
        for (Method method : methods) {
            for (Annotation annotation : method.getDeclaredAnnotations()) {
                if (annotation instanceof Test) {
                    list.add(method);
                }
            }
        }
        return list;
    }

}
