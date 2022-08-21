package ru.otus.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.lib.SensorDataBufferedWriter;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

// Этот класс нужно реализовать
public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;

    private final ConcurrentNavigableMap<LocalDateTime, SensorData> dataBuffer = new ConcurrentSkipListMap<>(
                                            Comparator.comparingLong(k -> k.toInstant(ZoneOffset.UTC).toEpochMilli()));
    private final ReentrantLock lock = new ReentrantLock();

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
    }

    @Override
    public void process(SensorData data) {
        dataBuffer.put(data.getMeasurementTime(), data);
        if (dataBuffer.size() >= bufferSize) {
            flush();
        }
    }

    public void flush() {

        try {
            boolean isLockAcquired = lock.tryLock(1, TimeUnit.SECONDS);

            if(isLockAcquired) {
                var lastEntry = dataBuffer.lastEntry();
                if (lastEntry == null) {
                    log.info("lastEntry is null");
                    return;
                }
                log.info("lastKey = {}", lastEntry.getKey());

                var dataBufferHeadMap = dataBuffer.headMap(lastEntry.getKey(), true);
                log.info("dataBufferHeadMap size = {}", dataBufferHeadMap.size());

                if (dataBufferHeadMap.size() > 0) {
                    writer.writeBufferedData(dataBufferHeadMap.values().stream().toList());
                    dataBufferHeadMap.clear();
                }
            }
        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
        }finally {
            lock.unlock();
        }

    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}
