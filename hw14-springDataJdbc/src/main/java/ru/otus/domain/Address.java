package ru.otus.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

import javax.annotation.Nonnull;

@Table("address")
public class Address {

    @Id
    private final Long clientId;

    @Nonnull
    private final String street;

    @PersistenceCreator
    public Address(Long clientId, String street) {
        this.clientId = clientId;
        this.street = street;
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Address(@JsonProperty("street") String street) {
        this(null, street);
    }

    public Long getClientId() {
        return clientId;
    }

    public String getStreet() {
        return street;
    }

    @Override
    public String toString() {
        return "Address{" +
                "clientId=" + clientId +
                ", street='" + street + '\'' +
                '}';
    }
}
