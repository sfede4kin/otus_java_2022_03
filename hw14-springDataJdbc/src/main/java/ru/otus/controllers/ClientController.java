package ru.otus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.domain.Address;
import ru.otus.domain.Client;
import ru.otus.domain.ClientDTO;
import ru.otus.domain.Phone;
import ru.otus.services.ClientService;

import java.util.List;
import java.util.Set;

@Controller
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping({"/", "/client/list"})
    public String clientsListView(Model model) {
        List<Client> clients = clientService.findAll();
        model.addAttribute("clients", clients);
        return "clientsList";
    }

    @GetMapping("/client/create")
    public String clientCreateView(Model model) {
        model.addAttribute("client", new ClientDTO());
        return "clientCreate";
    }

    @PostMapping("/client/save")
    public RedirectView clientSave(@ModelAttribute ClientDTO client) {
        clientService.save(new Client(client.getName(), new Address(client.getAddress()), Set.of(new Phone(client.getPhone()))));
        return new RedirectView("/", true);
    }

}
