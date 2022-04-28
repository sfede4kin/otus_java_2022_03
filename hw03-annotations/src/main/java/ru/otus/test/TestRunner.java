package ru.otus.test;

import ru.otus.test.stat.TestResult;
import ru.otus.test.util.ReflectionUtil;

import java.lang.reflect.Method;
import java.util.List;

public class TestRunner {

    public static void main(String[] args){
        TestRunner.run(AnnotationTest1.class);
        //TestRunner.run(AnnotationTest2.class);
    }

    public static void run(Class<?> clazz){

        List<List<Method>> methodTripleList = ReflectionUtil.getMethodTriple(clazz.getMethods());

        for(List<Method> methodTriple : methodTripleList){
            System.out.println(methodTriple.toString());
            TestResult result = new TestResult(clazz.getName());
            Object obj = ReflectionUtil.instantiate(clazz);

            for(Method method : methodTriple){
                try {
                    ReflectionUtil.callMethod(obj, method.getName());
                    result.addSuccess();
                }catch(Exception e){
                    result.addFailed();
                    printRed(e.toString());
                }
            }
            printGreen(result.toString());
        }

    }

    private static void printRed(String str){
        System.out.println("\u001B[31m" + str + "\u001B[0m");
    }

    private static void printGreen(String str){
        System.out.println("\u001B[32m" + str + "\u001B[0m");
    }
}
