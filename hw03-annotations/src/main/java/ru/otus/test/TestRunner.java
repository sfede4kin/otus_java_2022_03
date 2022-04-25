package ru.otus.test;

import ru.otus.test.statistics.TestResult;
import ru.otus.test.util.ReflectionUtil;

import java.lang.reflect.Method;
import java.util.TreeMap;

public class TestRunner {

    public static void main(String[] args){
        TestRunner.run("ru.otus.test.AnnotationTestNotExists");
        TestRunner.run("ru.otus.test.AnnotationTest1");
        TestRunner.run("ru.otus.test.AnnotationTest2");
    }

    public static void run(String className){
        TestResult result = new TestResult(className);
        try{
            Class<?> clazz = ReflectionUtil.getClass(className);
            Object obj = ReflectionUtil.instantiate(clazz);
            Method[] methods = obj.getClass().getMethods();

            TreeMap<Integer, Method> methodsOrdered = ReflectionUtil.orderMethodsByAnnotations(methods);
            methodsOrdered.forEach((k,v) -> {
                    try {
                        var methodName = v.getName();
                        System.out.println(methodName);
                        ReflectionUtil.callMethod(obj, methodName);
                        result.addSuccess();
                    }catch(Exception e){
                        result.addFailed();
                        printRed(e.toString());
                    }
                });

        }catch(Exception e){
            result.addFailed();
            printRed(e.toString());
        }

        printGreen(result.toString());

    }

    private static void printRed(String str){
        System.out.println("\u001B[31m" + str + "\u001B[0m");
    }

    private static void printGreen(String str){
        System.out.println("\u001B[32m" + str + "\u001B[0m");
    }
}
