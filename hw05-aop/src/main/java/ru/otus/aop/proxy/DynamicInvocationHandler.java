package ru.otus.aop.proxy;

import ru.otus.aop.TestLogging;
import ru.otus.aop.annotations.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DynamicInvocationHandler implements InvocationHandler {

    private final TestLogging testLogging;
    private final List<Method> logMethods;

    public DynamicInvocationHandler(TestLogging testLogging) {
        this.testLogging = testLogging;

        this.logMethods = Arrays.stream(testLogging.getClass().getMethods())
                .filter(m -> Arrays.stream(m.getDeclaredAnnotations()).anyMatch(a -> a instanceof Log))
                .collect(Collectors.toList());

        //logMethods.forEach(lm -> System.out.println(Arrays.toString(lm.getParameters())));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if(logMethods.stream().anyMatch(
                lm ->   lm.getName().equals(method.getName()) &&
                        Arrays.toString(lm.getParameters()).equals(Arrays.toString(method.getParameters())))) {
            System.out.println("executed method: " + method.getName() + ": " + Arrays.toString(args));
        }
        return method.invoke(testLogging, args);
    }

}
