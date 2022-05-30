package ru.otus.solid.banknote;

public class BanknoteFactory {

    public static Banknote getBanknote(int nominal) {
        if(nominal == 10) return new Banknote10();
        if(nominal == 100) return new Banknote100();
        if(nominal == 500) return new Banknote500();
        if(nominal == 1000) return new Banknote1000();

        throw new IllegalArgumentException("unknown banknote: " + nominal);
    }

}
