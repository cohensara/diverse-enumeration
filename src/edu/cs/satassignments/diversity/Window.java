package edu.cs.satassignments.diversity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class Window {

    LinkedList<HashMap<Integer, Boolean>> window = new LinkedList<>();
    int window_size;

    public Window(int window_size) {

        this.window_size = window_size;
    }

    public void reset() {
        window = new LinkedList<>();
    }

    public HashMap<Integer, Boolean> add(HashMap<Integer, Boolean> model) {

    	HashMap<Integer, Boolean> removed = null;

        if (window.size() == window_size) {
            removed = window.removeFirst();
        }
        window.addLast(model);

        return removed;
    }

    public boolean isFull() {
        return window.size() == window_size;
    }

    public int getSize() {
        return window_size;
    }

    public HashMap<Integer, Boolean> get(int i) {
        if (i < window.size())
            return window.get(i);
        return null;
    }
}
