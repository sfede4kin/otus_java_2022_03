package ru.otus.solid.banknote;

public class Banknote100 implements Banknote{
    private final static BanknoteEnum VALUE = BanknoteEnum.Banknote100;

    @Override
    public BanknoteEnum getValue() {
        return VALUE;
    }
}
