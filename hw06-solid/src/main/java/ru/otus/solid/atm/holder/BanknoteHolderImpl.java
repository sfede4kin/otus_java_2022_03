package ru.otus.solid.atm.holder;

import ru.otus.solid.banknote.Banknote;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BanknoteHolderImpl implements BanknoteHolder{

    private final int nominal;
    private final static int MAX_SIZE = 10;

    private final Queue<Banknote> banknoteQueue = new LinkedList<>();

    public BanknoteHolderImpl(int nominal, List<Banknote> banknotes){
        this.nominal = nominal;
        this.banknoteQueue.addAll(banknotes);
    }

    @Override
    public int getNominal() {
        return nominal;
    }

    @Override
    public int getSize(){
        return banknoteQueue.size();
    }

    @Override
    public int getBalance(){ return getSize() * getNominal();}

/*    @Override
    public boolean checkCashIn(int count){
        return count <= MAX_SIZE - banknoteQueue.size();
    }*/

    @Override
    public boolean checkCashOut(int count){
        return count <= banknoteQueue.size();
    }

    @Override
    public void add(Banknote banknote) {
        if(banknoteQueue.size() == MAX_SIZE){
            throw new RuntimeException("The " + getNominal() + " banknote holder is full");
        }
        banknoteQueue.offer(banknote);
    }

    @Override
    public Banknote get() {
        if(banknoteQueue.size() == 0){
            throw new RuntimeException("The " + getNominal() + " banknote holder is empty");
        }
        return banknoteQueue.poll();
    }
}
