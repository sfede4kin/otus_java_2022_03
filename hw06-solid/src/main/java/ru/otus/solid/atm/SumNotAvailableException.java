package ru.otus.solid.atm;

public class SumNotAvailableException extends RuntimeException{
    public SumNotAvailableException() {
        super("Requested sum is not available in ATM");
    }
}
