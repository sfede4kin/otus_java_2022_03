package ru.otus.aop;

import ru.otus.aop.annotations.Log;

public class TestLoggingImpl implements TestLogging{

    public TestLoggingImpl(){}
    @Log
    public void calc(int param1){}

    @Log
    public void calc(int param1, int param2){}

    public void calc(int param1, int param2, int param3){}
    @Log
    public void calc(int param1, int param2, int param3, int param4){}

    public void act(int param1, int param2, int param3, int param4){}
}
