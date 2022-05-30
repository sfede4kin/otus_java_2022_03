package ru.otus.solid.banknote;

public class Banknote1000 implements Banknote{
    private final static int VALUE = 1000;

    @Override
    public int getValue() {
        return VALUE;
    }
}
