package ru.otus.crm.model;

import javax.persistence.*;

@Entity
@Table(name = "phone")
public class Phone implements Cloneable{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "number")
    private String number;

    //Для избежания лишних update в таблице phones при вставке новых записей
    //требуется двунаправленная связь с явным указанием FK на стороне связи "многие"
    @ManyToOne
    @JoinColumn(name = "fk_client_id", nullable = false)
    private Client client;


    public Phone(){}

    public Phone(Long id, String number) {
        this.id = id;
        this.number = number;
    }

    public Phone(String number) {
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public Phone clone() {
        return new Phone(this.id, this.number);
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", number='" + number + '\'' +
                '}';
    }
}
