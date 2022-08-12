package ru.otus.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public class AddressDTO {
    private String street;

    public AddressDTO(){};
    public AddressDTO(String street) {
        this.street = street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet() {
        return street;
    }
}
