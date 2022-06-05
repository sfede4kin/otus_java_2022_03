package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ProcessorEvenNumberSecondErr implements Processor {

    private final DateTimeProvider dateTimeProvider;

    public ProcessorEvenNumberSecondErr(DateTimeProvider dateTimeProvider){
        this.dateTimeProvider = dateTimeProvider;
    }
    @Override
    public Message process(Message message) {
        System.out.println(dateTimeProvider.getDate());
        if(dateTimeProvider.getDate().getSecond() % 2 == 0){
            throw new RuntimeException("Process time second is even-numbered");
        }
        return message;
    }
}
