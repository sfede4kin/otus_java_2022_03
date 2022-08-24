package ru.otus.grpc.client;


import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.grpc.protobuf.generated.NumberGeneratorServiceGrpc;
import ru.otus.grpc.protobuf.generated.NumberReq;
import ru.otus.grpc.protobuf.generated.NumberResp;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NumberGeneratorClient {
    private static final Logger log = LoggerFactory.getLogger(NumberGeneratorClient.class);
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;
    private static final int NUMBER_FIRST = 0;
    private static final int NUMBER_LAST = 30;
    private static final int LOOP_LIMIT = 50;
    private static final int EXECUTOR_POOL_SIZE = 1;
    private static final int EXECUTOR_DELAY = 0;
    private static final int EXECUTOR_PERIOD = 1;

    private long current = 0;

    public static void main(String... args) {
        try {
            var client = new NumberGeneratorClient();
            client.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void start() throws InterruptedException {
        log.info("client is starting");

        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT).usePlaintext().build();
        var asyncStub = NumberGeneratorServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(LOOP_LIMIT);
        var executor = Executors.newScheduledThreadPool(EXECUTOR_POOL_SIZE);

        var request = NumberReq.newBuilder().setFirst(NUMBER_FIRST).setLast(NUMBER_LAST).build();
        var streamObserver = new ClientStreamObserver();
        asyncStub.getNumber(request, streamObserver);

        Runnable task = () -> {
            current = current + streamObserver.getLast() + 1;
            log.info("current value = {}", current);
            latch.countDown();
        };

        executor.scheduleAtFixedRate(task, EXECUTOR_DELAY, EXECUTOR_PERIOD, TimeUnit.SECONDS);
        latch.await();

        log.info("client is shutting down");
        channel.shutdown();
        executor.shutdown();
    }
}
