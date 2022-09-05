package ru.otus.webflux.chat.service;

import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.webflux.chat.domain.Message;
import ru.otus.webflux.chat.repository.MessageRepository;

import static java.time.temporal.ChronoUnit.SECONDS;

@Service
public class DataStoreR2dbc implements DataStore {
    private static final Logger log = LoggerFactory.getLogger(DataStoreR2dbc.class);
    private final MessageRepository messageRepository;
    private final Scheduler workerPool;

    public DataStoreR2dbc(Scheduler workerPool, MessageRepository messageRepository) {
        this.workerPool = workerPool;
        this.messageRepository = messageRepository;
    }

    @Override
    public Mono<Message> saveMessage(Message message) {
        log.info("saveMessage:{}", message);
        return messageRepository.save(message);
    }

    @Override
    public Flux<Message> loadMessages(String roomId) {
        log.info("loadMessages roomId:{}", roomId);

        return messageRepository.findByRoomId(roomId)
                .delayElements(Duration.of(3, SECONDS), workerPool);
    }

    @Override
    public Flux<Message> loadMessages() {
        log.info("loadMessages all");
        return messageRepository.findAll()
                .delayElements(Duration.of(1, SECONDS), workerPool);
    }
}
