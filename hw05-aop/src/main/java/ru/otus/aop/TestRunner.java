package ru.otus.aop;

import ru.otus.aop.proxy.DynamicInvocationHandler;

import java.lang.reflect.Proxy;

public class TestRunner {

    public static void main(String[] args){

        TestLogging proxyTest = (TestLogging) Proxy.newProxyInstance(
                                                        TestRunner.class.getClassLoader(),
                                                        new Class[] {TestLogging.class},
                                                        new DynamicInvocationHandler(new TestLoggingImpl())
        );

        proxyTest.calc(1);
        proxyTest.calc(1, 2);
        proxyTest.calc(1, 2, 3);
        proxyTest.calc(1, 2, 3, 4);
        proxyTest.act(1, 2, 3, 4);
    }

}
