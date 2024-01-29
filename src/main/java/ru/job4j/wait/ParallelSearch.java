package ru.job4j.wait;

import java.util.concurrent.atomic.AtomicInteger;

public class ParallelSearch {

    public static void main(String[] args) throws InterruptedException {
        int maxFiles = 10;
        AtomicInteger atomicInteger = new AtomicInteger(0);
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(maxFiles);
        final Thread consumer = new Thread(
                () -> {
                    while (atomicInteger.get() < maxFiles) {
                        try {
                            System.out.println("read file number = " + queue.poll());
                            atomicInteger.incrementAndGet();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        final Thread producer = new Thread(
                () -> {
                    for (int index = 1; index <= maxFiles; index++) {
                        try {
                            queue.offer(index);
                            System.out.println("find file number = " + index);
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

        );
        consumer.start();
        producer.start();
        consumer.join();
        producer.join();
        System.out.println("finish");
    }
}