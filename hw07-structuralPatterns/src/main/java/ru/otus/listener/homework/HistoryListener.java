package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, Message> historyMsgMap = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        try {
            var msgCloned = (Message)msg.clone();
            historyMsgMap.put(msgCloned.getId(), msgCloned);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(historyMsgMap.get(id));
    }
}
