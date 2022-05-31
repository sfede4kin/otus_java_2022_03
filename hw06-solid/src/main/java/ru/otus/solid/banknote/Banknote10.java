package ru.otus.solid.banknote;

public class Banknote10 implements Banknote{
    private final static BanknoteEnum VALUE = BanknoteEnum.Banknote10;

    @Override
    public BanknoteEnum getValue() {
        return VALUE;
    }
}
