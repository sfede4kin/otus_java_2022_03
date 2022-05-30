package ru.otus.solid.banknote;

public class Banknote10 implements Banknote{
    private final static int VALUE = 10;

    @Override
    public int getValue() {
        return VALUE;
    }
}
