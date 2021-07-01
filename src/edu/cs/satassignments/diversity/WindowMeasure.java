package edu.cs.satassignments.diversity;

import java.util.HashMap;

public interface WindowMeasure {

    int getWindowSize();

//    void addClique(Window window, HashSet<Integer> removed, HashSet<Integer> added);
    
    void addModel(Window window, HashMap<Integer, Boolean> removed, HashMap<Integer, Boolean> added);

    String getDiversityValue();

    void reset();

    String getHeader();
}
