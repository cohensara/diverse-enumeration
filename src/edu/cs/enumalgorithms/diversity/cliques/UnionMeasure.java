package edu.cs.enumalgorithms.diversity.cliques;

import edu.cs.enumalgorithms.diversity.Measure;

import java.util.HashSet;

public class UnionMeasure implements Measure<HashSet<Integer>> {

    HashSet<Integer> nodes = new HashSet<>();

    @Override
    public void addItem(HashSet<Integer> clique) {
        nodes.addAll(clique);
    }

    @Override
    public String getDiversityValue() {
        return Integer.toString(nodes.size());
    }

    @Override
    public void reset() {
        nodes = new HashSet<>();
    }

    @Override
    public String getHeader() {
        return "nodesSeen#";
    }

    public String toString() {
        return "UnionMeasure, " + getDiversityValue();
    }
}
