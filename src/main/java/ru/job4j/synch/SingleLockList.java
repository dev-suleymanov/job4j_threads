package ru.job4j.synch;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@ThreadSafe
public class SingleLockList<T> implements Iterable<T> {
    @GuardedBy("this")
    private final List<T> list;

    public SingleLockList(List<T> list) {
        this.list = copy(list);
    }

    public synchronized void add(T value) {
        list.add(value);
    }

    public synchronized T get(int index) {
        return list.get(index);
    }

    private List<T> copy(List<T> origin) {
        return new ArrayList<>(origin);
    }

    @Override
    public synchronized Iterator<T> iterator() {
        return new Iterator<T>() {
            private int position = 0;

            @Override
            public boolean hasNext() {
                return position < list.size();
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                return get(position++);
            }
        };
    }

    @Override
    public String toString() {
        return "SingleLockList{"
                + "list="
                + list + '}';
    }

    public static void main(String[] args) {
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6);
        SingleLockList<Integer> singleLockList = new SingleLockList<>(list);
        System.out.println(singleLockList);
        Iterator<Integer> iterator = singleLockList.iterator();
        System.out.println(iterator.next());
        System.out.println(iterator.next());
        System.out.println(iterator.next());
        System.out.println(iterator.next());
        System.out.println(iterator.next());
        System.out.println(iterator.next());
        System.out.println(iterator.next());
        System.out.println(iterator.next());
        System.out.println(iterator.next());
    }
}
