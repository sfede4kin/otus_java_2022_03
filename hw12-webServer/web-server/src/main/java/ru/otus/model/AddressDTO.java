package ru.otus.model;

public class AddressDTO {
    private final String street;

    public AddressDTO(String street) {
        this.street = street;
    }

    public String getStreet() {
        return street;
    }
}
