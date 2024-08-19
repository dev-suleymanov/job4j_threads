package ru.job4j.concurrent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class Wget implements Runnable {
    private final String url;

    /*
        Скорость в байтах в секунду
    */
    private final int speed;

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    @Override
    public void run() {
        var startAt = System.currentTimeMillis();
        var file = new File("tmp.xml");
        try (InputStream input = new URL(url).openStream();
             var output = new FileOutputStream(file)) {
            System.out.println("Open connection: " + (System.currentTimeMillis() - startAt) + " ms");
            var dataBuffer = new byte[512];
            int bytesRead;
            long nextTimeToWrite = System.nanoTime();
            while ((bytesRead = input.read(dataBuffer, 0, dataBuffer.length)) != -1) {
                long currentTime = System.nanoTime();
                /*
                    Применение ограничения скорости
                */
                long elapsedTime = currentTime - nextTimeToWrite;
                long requiredTime = TimeUnit.MILLISECONDS.toNanos(bytesRead * 1000L / speed);
                if (elapsedTime < requiredTime) {
                    TimeUnit.NANOSECONDS.sleep(requiredTime - elapsedTime);
                }
                output.write(dataBuffer, 0, bytesRead);
                nextTimeToWrite = System.nanoTime();
                System.out.println("Read " + bytesRead + " bytes : " + (System.nanoTime() - currentTime) + " nano.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length < 2) {
            throw new IllegalArgumentException("Please, enter URL and speed value for download");
        }
        String url = args[0];
        int speed;
        try {
            speed = Integer.parseInt(args[1]);
            if (speed <= 0) {
                throw new IllegalArgumentException("Speed must be a positive integer");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid speed value, must be an integer", e);
        }
        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        wget.join();
    }
}
