package ru.otus.solid.banknote;

public class Banknote500 implements Banknote{
    private final static BanknoteEnum VALUE = BanknoteEnum.Banknote500;

    @Override
    public BanknoteEnum getValue() {
        return VALUE;
    }
}
