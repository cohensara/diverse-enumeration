package edu.cs.enumalgorithms.diversity.cliques;

import edu.cs.enumalgorithms.diversity.Measure;

import java.text.DecimalFormat;
import java.util.HashSet;

public class CliqueSizeMeasure implements Measure<HashSet<Integer>> {

    DecimalFormat df = new DecimalFormat("#.###");
    int totalSize = 0;
    int numberOfCliques = 0;

    @Override
    public void addItem(HashSet<Integer> clique) {

        totalSize += clique.size();
        numberOfCliques++;
    }

    @Override
    public String getDiversityValue() {
        if (numberOfCliques == 0) return "0";
        return df.format(((float)totalSize) / numberOfCliques);
    }

    @Override
    public void reset() {
        totalSize = 0;
        numberOfCliques = 0;
    }

    @Override
    public String getHeader() {
        return "avgCliqueSize";
    }

    @Override
    public String toString() {
        return "averageSize, " + getDiversityValue();
    }
}
