package ru.otus.appcontainer.api.exception;

public class AppComponentsConfigNotFoundException extends RuntimeException{
    public AppComponentsConfigNotFoundException()  {
        super("Class annotated by AppComponentsConfig not found");
    }
}
