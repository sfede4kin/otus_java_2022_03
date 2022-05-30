package ru.otus.solid.atm.holder;

import ru.otus.solid.banknote.Banknote;

import java.util.List;

public interface BanknoteHolder {
    public int getNominal();

    public int getSize();

    public int getBalance();
/*    public boolean checkCashIn(int count);*/
    public boolean checkCashOut(int count);
    public void add(Banknote banknote);
    public Banknote get();
}
