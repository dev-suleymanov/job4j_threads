package ru.job4j.cas;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
public class CASCount {
    private final AtomicInteger count = new AtomicInteger(0);

    public void increment() {
        int temp;
        do {
            temp = count.get();
        } while (!count.compareAndSet(temp, temp + 1));
    }

    public int get() {
        return count.get();
    }

    public static void main(String[] args) throws InterruptedException {
        CASCount count = new CASCount();
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10000000; i++) {
                count.increment();
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 10000000; i++) {
                count.increment();
            }
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println(count.get());
    }
}