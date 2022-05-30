package ru.otus.solid.banknote;

public class Banknote100 implements Banknote{
    private final static int VALUE = 100;

    @Override
    public int getValue() {
        return VALUE;
    }
}
