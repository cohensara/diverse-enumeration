package edu.cs.maximalcliques.diversity;

import java.util.HashSet;

public interface Measure {


    void addClique(HashSet<Integer> clique);

    String getDiversityValue();

    void reset();

    String getHeader();
}
