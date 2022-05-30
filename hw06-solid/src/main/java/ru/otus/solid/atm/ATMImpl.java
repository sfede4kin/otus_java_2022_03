package ru.otus.solid.atm;

import ru.otus.solid.atm.holder.BanknoteHolder;
import ru.otus.solid.atm.holder.BanknoteHolderImpl;
import ru.otus.solid.banknote.Banknote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ATMImpl implements ATM{
    private final Map<Integer, BanknoteHolder> holderTreeMap;
    private static final String EX_SUM_NOT_AVAILABLE = "Requested sum is not available in ATM";

    private ATMImpl(Builder builder) {
        this.holderTreeMap = builder.holderTreeMap;
    }

    public static class Builder {
        private final Map<Integer, BanknoteHolder> holderTreeMap = new TreeMap<>(Collections.reverseOrder());

        public Builder withHolder10(List<Banknote> banknotes){
            int nominal = 10;
            this.holderTreeMap.put(nominal, new BanknoteHolderImpl(nominal, banknotes));
            return this;
        }

        public Builder withHolder100(List<Banknote> banknotes){
            int nominal = 100;
            this.holderTreeMap.put(nominal, new BanknoteHolderImpl(nominal, banknotes));
            return this;
        }

        public Builder withHolder500(List<Banknote> banknotes){
            int nominal = 500;
            this.holderTreeMap.put(nominal, new BanknoteHolderImpl(nominal, banknotes));
            return this;
        }

        public Builder withHolder1000(List<Banknote> banknotes){
            int nominal = 1000;
            this.holderTreeMap.put(nominal, new BanknoteHolderImpl(nominal, banknotes));
            return this;
        }

        public ATMImpl build(){
            return new ATMImpl(this);
        }

    }

    @Override
    public String toString(){

        return holderTreeMap.values().stream()
                .map(banknoteHolder -> "nominal=" + banknoteHolder.getNominal()
                                        + ",size=" + banknoteHolder.getSize())
                .collect(Collectors.joining("; ", "{", "}"));

    }

    @Override
    public int getBalance() {
        int balance = 0;
        for(BanknoteHolder bh : holderTreeMap.values()){
            balance += bh.getBalance();
        }
        return balance;
    }

    @Override
    public void addCash(List<Banknote> banknotes) {
        banknotes.forEach(bn -> holderTreeMap.get(bn.getValue()).add(bn));

    }

    @Override
    public List<Banknote> getCash(int sum) {

        Map<Integer, Integer> banknoteCalcMap = calculateBanknotesToGet(sum);
        List<Banknote> banknoteList = new ArrayList<>();

        //key = номинал, value = количество банкнот
        banknoteCalcMap.forEach((k,v)->{
            for(int i = 0; i < v; i++){
                banknoteList.add(holderTreeMap.get(k).get());
            }
        });

        return banknoteList;
    }

    private Map<Integer, Integer> calculateBanknotesToGet(int sum){

        if(sum > getBalance()){
            throw new RuntimeException(EX_SUM_NOT_AVAILABLE);
        }

        int sumRemain = sum;
        Map<Integer, Integer> banknoteCalcMap = new HashMap<>();

        for(var bh : holderTreeMap.values()){
            int banknoteToRequest = sumRemain / bh.getNominal(); //целое число без остатка
            sumRemain = sumRemain - banknoteToRequest * bh.getNominal();
            System.out.println("nominal=" + bh.getNominal() + ",banknoteToRequest="
                    + banknoteToRequest + ",sumRemain=" + sumRemain);

            if(banknoteToRequest > 0){
                if(!bh.checkCashOut(banknoteToRequest)) {
                    throw new RuntimeException(EX_SUM_NOT_AVAILABLE);
                }
                banknoteCalcMap.put(bh.getNominal(), banknoteToRequest);
            }
        }

        if(sumRemain > 0){
            throw new RuntimeException(EX_SUM_NOT_AVAILABLE);
        }

        return banknoteCalcMap;
    }
}
