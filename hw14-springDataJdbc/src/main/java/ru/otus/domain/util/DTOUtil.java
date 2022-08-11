package ru.otus.domain.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.domain.Client;
import ru.otus.domain.Address;
import ru.otus.domain.Phone;
import ru.otus.domain.ClientDTO;
import ru.otus.domain.AddressDTO;
import ru.otus.domain.PhoneDTO;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DTOUtil {

    private final static Logger log = LoggerFactory.getLogger(DTOUtil.class);

    public static ClientDTO cloneClientToDTO(Client client) {

        List<PhoneDTO> phoneDTO = client.getPhones().stream().map(p -> new PhoneDTO(p.getNumber())).toList();
        return new ClientDTO(client.getId(), client.getName(), new AddressDTO(client.getAddress().getStreet()), phoneDTO);
    }

    public static List<ClientDTO> cloneClientListToDTO(List<Client> clientList) {
        return clientList.stream().map(DTOUtil::cloneClientToDTO).toList();
    }

    public static Client cloneClientFromDTO(ClientDTO client) {
        Set<Phone> phones = client.getPhones().stream().map(p -> new Phone(p.getNumber())).collect(Collectors.toSet());
        return new Client(client.getName(), new Address(client.getAddress().getStreet()), phones);
    }

}
