package ru.otus.grpc.service;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.grpc.protobuf.generated.NumberGeneratorServiceGrpc;
import ru.otus.grpc.protobuf.generated.NumberReq;
import ru.otus.grpc.protobuf.generated.NumberResp;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class NumberGeneratorServiceImpl extends NumberGeneratorServiceGrpc.NumberGeneratorServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(NumberGeneratorServiceImpl.class);
    private static final int EXECUTOR_POOL_SIZE = 1;
    private static final int EXECUTOR_DELAY = 0;
    private static final int EXECUTOR_PERIOD = 2;

    @Override
    public void getNumber(NumberReq request, StreamObserver<NumberResp> responseObserver) {
        log.info("start new numbers sequence: first {}, last {}", request.getFirst(), request.getLast());

        var current = new AtomicLong(request.getFirst());
        var executor = Executors.newScheduledThreadPool(EXECUTOR_POOL_SIZE);
        Runnable task = () -> {
            var value = current.incrementAndGet();
            var response = NumberResp.newBuilder().setValue(value).build();
            responseObserver.onNext(response);
            if(value == request.getLast()){
                executor.shutdown();
                responseObserver.onCompleted();
                log.info("sequence is finished");
            }
        };

        executor.scheduleAtFixedRate(task, EXECUTOR_DELAY, EXECUTOR_PERIOD, TimeUnit.SECONDS);
    }
}
