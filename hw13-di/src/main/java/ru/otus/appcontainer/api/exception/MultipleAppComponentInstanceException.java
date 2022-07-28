package ru.otus.appcontainer.api.exception;

public class MultipleAppComponentInstanceException extends RuntimeException{
    public MultipleAppComponentInstanceException()  {
        super("Multiple instances of same component detected");
    }
}
