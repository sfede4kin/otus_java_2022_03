package ru.otus.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import javax.annotation.Nonnull;
import java.util.Set;

@Table("client")
public class Client implements Persistable<Long> {
    @Id
    private final Long id;
    @Nonnull
    private final String name;
    @MappedCollection(idColumn = "client_id")
    private final Address address;
    @MappedCollection(idColumn = "client_id")
    private final Set<Phone> phones;

    @Transient
    private final boolean isNew;

    public Client(Long id, String name, Address address, Set<Phone> phones, boolean isNew) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
        this.isNew = isNew;
    }

    @PersistenceCreator
    public Client(Long id, String name, Address address, Set<Phone> phones) {
        this(id, name, address, phones, true);
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Client(@JsonProperty("name") String name, @JsonProperty("address") Address address, @JsonProperty("phones") Set<Phone> phones) {
        this(null, name, address, phones);
    }

    @Override
    @JsonIgnore
    public boolean isNew() {
        return isNew;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    public Set<Phone> getPhones() {
        return phones;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phones='" + phones + '\'' +
                ", isNew=" + isNew +
                '}';
    }

}
