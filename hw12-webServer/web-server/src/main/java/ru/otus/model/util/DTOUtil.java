package ru.otus.model.util;

import ru.otus.crm.model.Client;
import ru.otus.model.AddressDTO;
import ru.otus.model.ClientDTO;
import ru.otus.model.PhoneDTO;

import java.util.List;

public class DTOUtil {

    public static ClientDTO cloneClient(Client client) {
        List<PhoneDTO> phoneDTO = client.getPhones().stream().map(p -> new PhoneDTO(p.getNumber())).toList();
        return new ClientDTO(client.getId(), client.getName(), new AddressDTO(client.getAddress().getStreet()), phoneDTO);
    }

    public static List<ClientDTO> cloneClientList(List<Client> clientList) {
        return clientList.stream().map(DTOUtil::cloneClient).toList();
    }
}
