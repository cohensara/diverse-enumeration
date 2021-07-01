package edu.cs.maximalcliques.diversity;

import java.util.HashSet;

public interface WindowMeasure {

    int getWindowSize();

    void addClique(Window window, HashSet<Integer> removed, HashSet<Integer> added);

    String getDiversityValue();

    void reset();

    String getHeader();
}
