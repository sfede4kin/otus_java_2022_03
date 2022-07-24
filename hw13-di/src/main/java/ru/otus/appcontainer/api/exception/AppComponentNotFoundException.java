package ru.otus.appcontainer.api.exception;

public class AppComponentNotFoundException extends RuntimeException{
    public AppComponentNotFoundException()  {
        super("AppComponent not found");
    }
}
