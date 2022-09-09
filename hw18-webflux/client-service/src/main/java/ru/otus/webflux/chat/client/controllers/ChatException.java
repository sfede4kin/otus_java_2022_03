package ru.otus.webflux.chat.client.controllers;

public class ChatException extends RuntimeException {
    public ChatException(String message) {
        super(message);
    }
}
