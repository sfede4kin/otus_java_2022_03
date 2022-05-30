package ru.otus.solid;

import ru.otus.solid.atm.ATM;
import ru.otus.solid.atm.ATMImpl;
import ru.otus.solid.banknote.BanknoteFactory;

import java.util.List;

public class ATMRunner {
    public static void main(String[] args){
        ATM atm = new ATMImpl.Builder()
                    .withHolder10(List.of(BanknoteFactory.getBanknote(10),
                                          BanknoteFactory.getBanknote(10),
                                          BanknoteFactory.getBanknote(10)))
                    .withHolder100(List.of(BanknoteFactory.getBanknote(100),
                                           BanknoteFactory.getBanknote(100)))
                    .withHolder500(List.of(BanknoteFactory.getBanknote(500),
                                           BanknoteFactory.getBanknote(500),
                                           BanknoteFactory.getBanknote(500)))
                    .withHolder1000(List.of(BanknoteFactory.getBanknote(1000),
                                            BanknoteFactory.getBanknote(1000)))
                    .build();


        System.out.println(atm);
        System.out.println(atm.getBalance());

        atm.addCash(List.of(BanknoteFactory.getBanknote(10),
                            BanknoteFactory.getBanknote(100),
                            BanknoteFactory.getBanknote(500),
                            BanknoteFactory.getBanknote(1000)
                    ));
        System.out.println(atm);
        System.out.println(atm.getBalance());

        atm.getCash(1610);
        System.out.println(atm);
        System.out.println(atm.getBalance());

        atm.getCash(2620);
        System.out.println(atm);
        System.out.println(atm.getBalance());

    }
}
