package ru.otus.grpc.client;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.grpc.protobuf.generated.NumberResp;

public class ClientStreamObserver implements StreamObserver<NumberResp> {
    private static final Logger log = LoggerFactory.getLogger(ClientStreamObserver.class);
    private long last = 0;

    @Override
    public void onNext(NumberResp value) {
        setLast(value.getValue());
        log.info("next value from server = {}", value.getValue());
    }

    @Override
    public void onError(Throwable t) {
        log.error("exception", t);
    }

    @Override
    public void onCompleted() {
        log.info("sequence completed");
    }

    private synchronized void setLast(long value){
        this.last = value;
    }

    public synchronized long getLast(){
        var currentLast = this.last;
        this.last = 0;
        return currentLast;
    }

}
