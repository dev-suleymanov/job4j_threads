package ru.job4j.threads.other;

import java.io.*;
import java.util.function.Predicate;

public class FileContentReader {
    private final File file;

    public FileContentReader(File file) {
        this.file = file;
    }

    public String getContent() {
        return readerStream(c -> true);
    }

    public String getContentWithoutUnicode() {
        return readerStream(c -> c < 0x80);
    }

    private String readerStream(Predicate<Character> filter) {
        String output = "";
        try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(file))) {
            int data;
            while ((data = input.read()) > 0) {
                char temp = (char) data;
                if (filter.test(temp)) {
                    output += temp;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }
}
