package ru.otus.processor.homework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.handler.ComplexProcessor;
import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProcessorEvenNumberSecondErrTest {

    @Test
    @DisplayName("Исключение при обработке сообщения в четную секунду")
    void processorEvenNumberExceptionTest(){

        var message = new Message.Builder(1L).field7("field7").build();

        var processor1 = mock(Processor.class);
        when(processor1.process(message)).thenReturn(message);

        var processor2 = new ProcessorEvenNumberSecondErr(
                                ()->LocalDateTime.of(2022, 6, 2, 0, 0, 2)
                            );

        var processors = List.of(processor1,processor2);

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {
            throw new EvenNumberException(ex.getMessage());
        });

        assertThatExceptionOfType(EvenNumberException.class).isThrownBy(() -> complexProcessor.handle(message));

    }

    private static class EvenNumberException extends RuntimeException {
        public EvenNumberException(String msg) {
            super(msg);
        }
    }
}
