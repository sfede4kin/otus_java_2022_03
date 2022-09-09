package ru.otus.webflux.chat.datastore.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.webflux.chat.datastore.domain.Message;

public interface DataStore {

    Mono<Message> saveMessage(Message message);

    Flux<Message> loadMessages(String roomId);

    Flux<Message> loadMessages();

}
