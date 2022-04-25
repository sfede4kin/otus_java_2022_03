package ru.otus.test.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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

    public static List<Map.Entry<Integer, Method>> orderMethodsByAnnotations(Method[] methods){
        List<Map.Entry<Integer, Method>> entries = new ArrayList<>();

        for (Method method : methods) {
            for (Annotation annotation : method.getDeclaredAnnotations()) {
                if (annotation instanceof ru.otus.test.annotations.Before){
                    entries.add(new AbstractMap.SimpleEntry<>(1, method));
                }else if(annotation instanceof ru.otus.test.annotations.Test) {
                    entries.add(new AbstractMap.SimpleEntry<>(2, method));
                }else if(annotation instanceof ru.otus.test.annotations.After){
                    entries.add(new AbstractMap.SimpleEntry<>(3, method));
                }
            }
        }

        entries.sort(new Comparator<>() {
            @Override
            public int compare(
                    Map.Entry<Integer, Method> o1, Map.Entry<Integer, Method> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        return entries;
    }
}
