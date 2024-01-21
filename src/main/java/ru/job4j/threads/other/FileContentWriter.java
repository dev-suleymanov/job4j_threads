package ru.job4j.threads.other;

import java.io.*;

public class FileContentWriter {
    private final File file;

    public FileContentWriter(File file) {
        this.file = file;
    }

    public void saveContent(String content) {
        try (BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file))) {
            for (int i = 0; i < content.length(); i++) {
                output.write(content.charAt(i));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
