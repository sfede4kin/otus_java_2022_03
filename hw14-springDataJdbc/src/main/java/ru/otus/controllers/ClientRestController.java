package ru.otus.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.otus.domain.Client;
import ru.otus.services.ClientService;

import java.util.List;

@RestController
public class ClientRestController {

    private static final Logger log = LoggerFactory.getLogger(ClientRestController.class);

    private final ClientService clientService;

    public ClientRestController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/api/client/{id}")
    public Client getClientById(@PathVariable(name = "id") long id) {
        return clientService.findById(id).orElse(null);
    }

    @GetMapping("/api/client")
    public List<Client> getClients() {
        return clientService.findAll();
    }

    @PostMapping("/api/client")
    public Client saveClient(@RequestBody Client client) {
        return clientService.save(client);
    }

}
