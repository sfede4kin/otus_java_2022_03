package ru.otus.webflux.chat.client.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.util.HtmlUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.webflux.chat.client.domain.Message;

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    private static final String TOPIC_TEMPLATE = "/topic/response.";

    private final WebClient datastoreClient;
    private final SimpMessagingTemplate template;

    private final static String MAGIC_ROOM = "1408";

    public MessageController(WebClient datastoreClient, SimpMessagingTemplate template) {
        this.datastoreClient = datastoreClient;
        this.template = template;
    }

    @MessageMapping("/message.{roomId}")
    @SendTo({TOPIC_TEMPLATE + "{roomId}", TOPIC_TEMPLATE + MAGIC_ROOM })
    public Message getMessage(@DestinationVariable String roomId, Message message) {
        logger.info("got message:{}, roomId:{}", message, roomId);

        if(MAGIC_ROOM.equals(roomId)){
            logger.info("skip message from magic room");
            return null;
        }

        saveMessage(roomId, message)
                .subscribe(msgId -> logger.info("message send id:{}", msgId));
        return new Message(HtmlUtils.htmlEscape(message.messageStr()));
    }


    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        var genericMessage = (GenericMessage<byte[]>) event.getMessage();
        var simpDestination = (String) genericMessage.getHeaders().get("simpDestination");
        if (simpDestination == null) {
            logger.error("Cannot get simpDestination header, headers:{}", genericMessage.getHeaders());
            throw new ChatException("Cannot get simpDestination header");
        }
        var roomId = parseRoomId(simpDestination);

        getMessagesByRoomId(roomId)
                .doOnError(ex -> logger.error("getting messages for roomId:{} failed", roomId, ex))
                .subscribe(message -> template.convertAndSend(simpDestination, message));
    }

    private long parseRoomId(String simpDestination) {
        try {
            return Long.parseLong(simpDestination.replace(TOPIC_TEMPLATE, ""));
        } catch (Exception ex) {
            logger.error("Cannot get roomId", ex);
            throw new ChatException("Cannot get roomId");
        }
    }

    private Mono<Long> saveMessage(String roomId, Message message) {

        return datastoreClient.post().uri(String.format("/msg/%s", roomId))
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(message)
                .exchangeToMono(response -> response.bodyToMono(Long.class));
    }

    private Flux<Message> getMessagesByRoomId(long roomId) {
        var uri = MAGIC_ROOM.equals(String.valueOf(roomId)) ? "/msg" : String.format("/msg/%s", roomId);

        return datastoreClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_NDJSON)
                .exchangeToFlux(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToFlux(Message.class);
                    } else {
                        return response.createException().flatMapMany(Mono::error);
                    }
                });
    }
}
