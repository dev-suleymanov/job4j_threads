package ru.job4j.wait;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

@ThreadSafe
public class SimpleBlockingQueue<T> {
    @GuardedBy("this")
    private Queue<T> queue = new LinkedList<>();
    private final int size;

    public SimpleBlockingQueue(int size) {
        this.size = size;
    }

    public synchronized void offer(T value) throws InterruptedException {
        while (queue.size() == size) {
            this.wait();
        }
        queue.add(value);
        this.notifyAll();
    }

    public synchronized T poll() throws InterruptedException {
        while (queue.isEmpty()) {
            this.wait();
        }
        T result = queue.poll();
        this.notifyAll();
        return result;
    }

    public int size() {
        return queue.size();
    }

    public synchronized boolean isEmpty() {
        return size() == 0;
    }

    public static void main(String[] args) throws InterruptedException {
        SimpleBlockingQueue<Integer> simpleBlockingQueue = new SimpleBlockingQueue<>(10);
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    simpleBlockingQueue.offer(i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.printf("%s = %d size\n",
                    Thread.currentThread().getName(), simpleBlockingQueue.size());
        });
        Thread thread2 = new Thread(() -> {
            try {
                System.out.printf("%s = %d poll\n",
                        Thread.currentThread().getName(), simpleBlockingQueue.poll());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.printf("%s = %d size\n", Thread.currentThread().getName(), simpleBlockingQueue.size());
    }
}