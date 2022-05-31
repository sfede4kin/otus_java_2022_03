package ru.otus.solid.banknote;

public class Banknote1000 implements Banknote{
    private final static BanknoteEnum VALUE = BanknoteEnum.Banknote1000;

    @Override
    public BanknoteEnum getValue() {
        return VALUE;
    }
}
