package edu.cs.maximalcliques.diversity;

import java.util.HashSet;
import java.util.LinkedList;

public class Window {

    LinkedList<HashSet<Integer>> window = new LinkedList<>();
    int window_size;

    public Window(int window_size) {

        this.window_size = window_size;
    }

    public void reset() {
        window = new LinkedList<>();
    }

    public HashSet<Integer> add(HashSet<Integer> clique) {

        HashSet<Integer> removed = null;

        if (window.size() == window_size) {
            removed = window.removeFirst();
        }
        window.addLast(clique);

        return removed;
    }

    public boolean isFull() {
        return window.size() == window_size;
    }

    public int getSize() {
        return window_size;
    }

    public HashSet<Integer> get(int i) {
        if (i < window.size())
            return window.get(i);
        return null;
    }
}
