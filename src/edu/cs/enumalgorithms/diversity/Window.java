package edu.cs.enumalgorithms.diversity;

import java.util.LinkedList;

public class Window<T> {

    LinkedList<T> window = new LinkedList<>();
    int window_size;

    public Window(int window_size) {

        this.window_size = window_size;
    }

    public void reset() {
        window = new LinkedList<>();
    }

    public T add(T item) {

        T removed = null;

        if (window.size() == window_size) {
            removed = window.removeFirst();
        }
        window.addLast(item);

        return removed;
    }

    public boolean isFull() {
        return window.size() == window_size;
    }

    public int getSize() {
        return window_size;
    }

    public T get(int i) {
        if (i < window.size())
            return window.get(i);
        return null;
    }
}
