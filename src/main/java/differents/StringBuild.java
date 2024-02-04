package differents;

import java.util.Arrays;

/**
 * Класс StringBuild представляет собой простую реализацию динамической строки,
 * которая может быть изменена и расширена в многопоточной среде.
 */
public class StringBuild {
    private int capacity;
    private int size = 0;
    private char[] array;

    public StringBuild() {
        this(16);
    }

    public StringBuild(int capacity) {
        this.capacity = capacity;
        array = new char[capacity];
    }

    public synchronized void set(String str) {
        for (char c : str.toCharArray()) {
            if (size == capacity) {
                expand();
            }
            array[size++] = c;
        }
    }
    private synchronized void expand() {
        capacity *= 2;
        array = Arrays.copyOf(array, capacity);
    }
    public String toBuildString() {
        return new String(Arrays.copyOf(array, size));
    }
}
