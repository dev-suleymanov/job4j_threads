package ru.job4j.wait;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

import static java.util.stream.IntStream.*;
import static org.assertj.core.api.Assertions.*;

class SimpleBlockingQueueTest {
    SimpleBlockingQueue<Integer> sB = new SimpleBlockingQueue(10);

    @Test
    void addElementAndCompareSize() throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    sB.offer(i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Thread thread2 = new Thread(() -> {
            try {
                sB.poll();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        assertThat(sB.size()).isEqualTo(9);
    }

    @Test
    public void whenFetchAllThenGetIt() throws InterruptedException {
        final CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
        final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(10);
        Thread producer = new Thread(() -> {
            IntStream.range(0, 7).forEach(i -> {
                try {
                    queue.offer(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });
        Thread consumer = new Thread(() -> {
            while (!queue.isEmpty() || !Thread.currentThread().isInterrupted()) {
                try {
                    buffer.add(queue.poll());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        producer.start();
        consumer.start();
        producer.join();
        consumer.interrupt();
        consumer.join();
        assertThat(buffer).containsExactly(0, 1, 2, 3, 4, 5, 6);
    }

    @Test
    public void whenFetchAllThenGetIt2() throws InterruptedException {
        final CopyOnWriteArrayList<Character> buffer = new CopyOnWriteArrayList<>();
        final SimpleBlockingQueue<Character> queue = new SimpleBlockingQueue<>(10);
        List<Character> list = List.of('a', 'b', 'c', 'd', 'e', 'f', 'g');
        Thread producer = new Thread(() -> {
            list.stream()
                    .forEach(el -> {
                        try {
                            queue.offer(el);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
        });
        Thread consumer = new Thread(() -> {
            while (!queue.isEmpty() || !Thread.currentThread().isInterrupted()) {
                try {
                    buffer.add(queue.poll());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        producer.start();
        consumer.start();
        producer.join();
        consumer.interrupt();
        consumer.join();
        assertThat(buffer).isEqualTo(list);
    }
}