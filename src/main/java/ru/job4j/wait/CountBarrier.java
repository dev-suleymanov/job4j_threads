package ru.job4j.wait;

public class CountBarrier {
    private final Object monitor = this;

    private final int total;

    private int count = 0;

    public CountBarrier(final int total) {
        this.total = total;
    }

    public void count() {
        synchronized (monitor) {
            count++;
            if (count >= total) {
                monitor.notifyAll();
            }
        }
    }

    public void await() {
        synchronized (monitor) {
            while (count < total) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        CountBarrier countBarrier = new CountBarrier(10);
        Thread first = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                countBarrier.count();
            }
            System.out.printf("count %s = %d\n", Thread.currentThread().getName(), countBarrier.count);
        }, "First");

        Thread second = new Thread(() -> {
            countBarrier.await();
            for (int i = 0; i < 10; i++) {
                countBarrier.count();
            }
            System.out.printf("count %s = %d\n", Thread.currentThread().getName(), countBarrier.count);
        }, "Second");

        Thread third = new Thread(() -> {
            countBarrier.await();
            for (int i = 0; i < 10; i++) {
                countBarrier.count();
            }
            System.out.printf("count %s = %d\n", Thread.currentThread().getName(), countBarrier.count);
        }, "Third");

        first.start();
        second.start();
        third.start();
        first.join();
        second.join();
        third.join();
        System.out.printf("count %s = %d\n", Thread.currentThread().getName(), countBarrier.count);
    }
}