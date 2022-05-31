package ru.otus.solid;

import ru.otus.solid.atm.ATM;
import ru.otus.solid.atm.ATMImpl;
import ru.otus.solid.banknote.BanknoteEnum;
import ru.otus.solid.banknote.BanknoteFactory;

import java.util.List;

public class ATMRunner {
    public static void main(String[] args){
        ATM atm = new ATMImpl.Builder()
                    .withHolder10(List.of(BanknoteFactory.getBanknote(BanknoteEnum.Banknote10),
                                          BanknoteFactory.getBanknote(BanknoteEnum.Banknote10),
                                          BanknoteFactory.getBanknote(BanknoteEnum.Banknote10)))
                    .withHolder100(List.of(BanknoteFactory.getBanknote(BanknoteEnum.Banknote100),
                                           BanknoteFactory.getBanknote(BanknoteEnum.Banknote100)))
                    .withHolder500(List.of(BanknoteFactory.getBanknote(BanknoteEnum.Banknote500),
                                           BanknoteFactory.getBanknote(BanknoteEnum.Banknote500),
                                           BanknoteFactory.getBanknote(BanknoteEnum.Banknote500)))
                    .withHolder1000(List.of(BanknoteFactory.getBanknote(BanknoteEnum.Banknote1000),
                                            BanknoteFactory.getBanknote(BanknoteEnum.Banknote1000)))
                    .build();


        System.out.println(atm);
        System.out.println(atm.getBalance());

        atm.addCash(List.of(BanknoteFactory.getBanknote(BanknoteEnum.Banknote10),
                            BanknoteFactory.getBanknote(BanknoteEnum.Banknote100),
                            BanknoteFactory.getBanknote(BanknoteEnum.Banknote500),
                            BanknoteFactory.getBanknote(BanknoteEnum.Banknote1000)
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
