package ru.otus.solid.banknote;

public class BanknoteFactory {

    public static Banknote getBanknote(BanknoteEnum nominal) {

        return switch (nominal) {
            case Banknote10 -> new Banknote10();
            case Banknote100 -> new Banknote100();
            case Banknote500 -> new Banknote500();
            case Banknote1000 -> new Banknote1000();
            default -> throw new IllegalArgumentException("unknown banknote: " + nominal.getNominal());
        };

/*        if(nominal == 10) return new Banknote10();
        if(nominal == 100) return new Banknote100();
        if(nominal == 500) return new Banknote500();
        if(nominal == 1000) return new Banknote1000();

        throw new IllegalArgumentException("unknown banknote: " + nominal);*/
    }

}
