package ru.otus.solid.banknote;

public class BanknoteFactory {

    public static Banknote getBanknote(BanknoteEnum nominal) {

        return switch (nominal) {
            case Banknote10 -> new Banknote10();
            case Banknote100 -> new Banknote100();
            case Banknote500 -> new Banknote500();
            case Banknote1000 -> new Banknote1000();
        };
    }
}
