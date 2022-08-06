package ru.otus.model;

import ru.otus.crm.model.Address;
import ru.otus.crm.model.Phone;

import java.util.List;

public class ClientDTO {
    private final Long id;
    private final String name;
    private final AddressDTO address;
    private final List<PhoneDTO> phones;

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

    public List<PhoneDTO> getPhones() {
        return phones;
    }
}
