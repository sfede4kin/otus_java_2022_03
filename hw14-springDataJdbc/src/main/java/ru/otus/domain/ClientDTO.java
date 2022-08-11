package ru.otus.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ClientDTO {
    private Long id;
    private String name;
    private AddressDTO address;
    private List<PhoneDTO> phones;

    public ClientDTO(){
        this.address = new AddressDTO();
        this.phones = new ArrayList<>();
        this.phones.add(new PhoneDTO());
    };

    public ClientDTO(Long id, String name, AddressDTO address, List<PhoneDTO> phones){
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setPhones(List<PhoneDTO> phones) {
        this.phones = phones;
    }

    public List<PhoneDTO> getPhones() {
        return phones;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

}
