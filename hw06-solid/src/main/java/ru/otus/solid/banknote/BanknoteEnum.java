package ru.otus.solid.banknote;

public enum BanknoteEnum {
    Banknote10(10),
    Banknote100(100),
    Banknote500(500),
    Banknote1000(1000);

    private final int nominal;

    BanknoteEnum(int nominal){
        this.nominal = nominal;
    }

    public int getNominal(){
        return nominal;
    }

    @Override
    public String toString() {
        return "BanknoteEnum{" +
                "nominal='" + nominal + '\'' +
                '}';
    }
}
