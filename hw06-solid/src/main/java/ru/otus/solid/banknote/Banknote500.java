package ru.otus.solid.banknote;

public class Banknote500 implements Banknote{
    private final static int VALUE = 500;

    @Override
    public int getValue() {
        return VALUE;
    }
}
