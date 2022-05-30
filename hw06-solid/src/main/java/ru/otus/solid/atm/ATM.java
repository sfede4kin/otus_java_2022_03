package ru.otus.solid.atm;

import ru.otus.solid.banknote.Banknote;
import java.util.List;

public interface ATM {
    public void addCash(List<Banknote> banknotes);
    public List<Banknote> getCash(int sum);

    public int getBalance();
}
