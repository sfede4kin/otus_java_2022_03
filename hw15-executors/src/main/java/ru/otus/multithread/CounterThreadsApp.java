package ru.otus.multithread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CounterThreadsApp {
    private static final Logger log = LoggerFactory.getLogger(CounterThreadsApp.class);

    private static final int LOW_LIMIT = 1;
    private static final int UPPER_LIMIT = 10;
    private String lastThreadName = "t2";
    private int i = 0;
    private int j = 1;

    public static void main(String... args) {
        CounterThreadsApp counter = new CounterThreadsApp();
        new Thread(counter::count, "t1").start();
        new Thread(counter::count, "t2").start();
    }

    private synchronized void count() {
        String currentThreadName = Thread.currentThread().getName();
        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (lastThreadName.equals(currentThreadName)) {
                    this.wait();
                }

                if ("t1".equals(currentThreadName)) {
                    calcCount();
                }

                log.info(String.valueOf(i));
                lastThreadName = currentThreadName;

                sleep();
                notifyAll();

            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void calcCount() {
        if (i == LOW_LIMIT) {
            j = 1;
        }
        if (i == UPPER_LIMIT) {
            j = -1;
        }
        i += j;
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
