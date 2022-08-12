package ru.otus.multithread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CounterThreadsApp {
    private static final Logger log = LoggerFactory.getLogger(CounterThreadsApp.class);

    private static final int COUNTER_LOW_LIMIT = 1;
    private static final int COUNTER_UPPER_LIMIT = 10;
    private String lastThreadName = "t2";
    private int counter = 0;
    private int sign = 1;

    public static void main(String... args) {
        CounterThreadsApp counterApp = new CounterThreadsApp();
        new Thread(counterApp::count, "t1").start();
        new Thread(counterApp::count, "t2").start();
    }

    private synchronized void count() {
        String currentThreadName = Thread.currentThread().getName();
        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (lastThreadName.equals(currentThreadName)) {
                    this.wait();
                }

                if ("t1".equals(currentThreadName)) {
                    calcCounter();
                }

                log.info(String.valueOf(counter));
                lastThreadName = currentThreadName;

                sleep();
                notifyAll();

            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void calcCounter() {
        if (counter == COUNTER_LOW_LIMIT) {
            sign = 1;
        }
        if (counter == COUNTER_UPPER_LIMIT) {
            sign = -1;
        }
        counter += sign;
    }

    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

}
