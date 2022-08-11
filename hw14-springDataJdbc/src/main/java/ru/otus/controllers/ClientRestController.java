package ru.otus.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.otus.domain.Client;
import ru.otus.domain.ClientDTO;
import ru.otus.domain.util.DTOUtil;
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
    public ClientDTO getClientById(@PathVariable(name = "id") long id) {
        var client = clientService.findById(id);
        return client.map(DTOUtil::cloneClientToDTO).orElse(null);
    }

    @GetMapping("/api/client")
    public List<ClientDTO> getClients() {
        return DTOUtil.cloneClientListToDTO(clientService.findAll());
    }

    @PostMapping("/api/client")
    public Client saveClient(@RequestBody ClientDTO client) {
        return clientService.save(DTOUtil.cloneClientFromDTO(client));
    }

}
