package ru.otus.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

import javax.annotation.Nonnull;

@Table("phone")
public class Phone {

    @Id
    private final Long id;

    @Nonnull
    private final String number;

    @Nonnull
    private final Long clientId;

    @PersistenceCreator
    public Phone(Long id, String number, Long clientId) {
        this.id = id;
        this.number = number;
        this.clientId = clientId;
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Phone(@JsonProperty("number")  String number) {
        this(null, number, null);
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public Long getClientId() {
        return clientId;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", clientId='" + clientId + '\'' +
                '}';
    }
}
