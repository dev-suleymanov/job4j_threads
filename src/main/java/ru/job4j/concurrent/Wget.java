package ru.job4j.concurrent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Wget implements Runnable {
    private final String url;
    private final int speed;
    private final String destinationFileName;

    public Wget(String url, int speed, String destinationFileName) {
        this.url = url;
        this.speed = speed;
        this.destinationFileName = destinationFileName;
    }
    @Override
    public void run() {
        var file = new File(destinationFileName);
        try (InputStream input = new URL(url).openStream();
             FileOutputStream output = new FileOutputStream(file)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            int bytesCount = 0;
            long startAt = System.currentTimeMillis();
            System.out.println("Open connection: " + (System.currentTimeMillis() - startAt) + " ms");
            while ((bytesRead = input.read(dataBuffer)) != -1) {
                output.write(dataBuffer, 0, bytesRead);
                bytesCount += bytesRead;
                if (bytesCount >= speed) {
                    regulateSpeed(startAt);
                    bytesCount = 0;
                    startAt = System.currentTimeMillis();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void regulateSpeed(long startTime) throws InterruptedException {
        long downloadTime = System.currentTimeMillis() - startTime;
        long sleepTime = 1000 - downloadTime;
        if (sleepTime > 0) {
            Thread.sleep(sleepTime);
        }
    }
    public static void main(String[] args) throws InterruptedException {
        if (args.length < 3) {
            throw new IllegalArgumentException("Please, enter URL, speed value for download and destination file name");
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
        String destinationFileName = args[2];
        if (destinationFileName == null || destinationFileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty");
        }
        Thread wget = new Thread(new Wget(url, speed, destinationFileName));
        wget.start();
        wget.join();
    }
}
