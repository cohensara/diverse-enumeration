package edu.cs.satassignments.diversity;

import java.util.HashMap;
import java.util.HashSet;

public interface Measure {


//    void addClique(HashSet<Integer> clique);
    
    void addModel(HashMap<Integer, Boolean> model);

    String getDiversityValue();

    void reset();

    String getHeader();
}
