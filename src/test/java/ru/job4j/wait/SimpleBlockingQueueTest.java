package ru.job4j.wait;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
}