package ru.otus.grpc.server;

import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.grpc.service.NumberGeneratorServiceImpl;

import java.io.IOException;

public class NumberGeneratorServer {
    private static final Logger log = LoggerFactory.getLogger(NumberGeneratorServer.class);
    private static final int SERVER_PORT = 8190;

    public static void main(String... args) throws IOException, InterruptedException {

        var service = new NumberGeneratorServiceImpl();
        var server = ServerBuilder.forPort(SERVER_PORT).addService(service).build();
        server.start();
        log.info("server is started");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("received shutdown request");
            server.shutdown();
            log.info("server is stopped");
        }));

        server.awaitTermination();
    }
}
