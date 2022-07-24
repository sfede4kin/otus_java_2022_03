package ru.otus.appcontainer.api;

public class AppComponentsConfigNotFoundException extends RuntimeException{
    public AppComponentsConfigNotFoundException()  {
        super("Class annotated by AppComponentsConfig not found");
    }
}
