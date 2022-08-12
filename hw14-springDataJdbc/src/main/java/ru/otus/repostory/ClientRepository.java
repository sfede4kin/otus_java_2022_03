package ru.otus.repostory;

import org.springframework.data.repository.CrudRepository;
import ru.otus.domain.Client;

import java.util.List;
import java.util.Optional;


public interface ClientRepository extends CrudRepository<Client, Long> {

    List<Client> findAll();

    Optional<Client> findById(Long id);

    Client save(Client client);

}
