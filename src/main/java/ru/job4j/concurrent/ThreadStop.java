package ru.job4j.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadStop {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(
                () -> {
                    AtomicInteger count = new AtomicInteger(0);
                    while (!Thread.currentThread().isInterrupted()) {
                        System.out.println(count.incrementAndGet());
                    }
                }
        );
        thread.start();
        Thread.sleep(1000);
        thread.interrupt();
    }
}