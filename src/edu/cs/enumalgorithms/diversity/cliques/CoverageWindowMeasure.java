package edu.cs.enumalgorithms.diversity.cliques;

import edu.cs.enumalgorithms.diversity.Window;
import edu.cs.enumalgorithms.diversity.WindowMeasure;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Hashtable;

public class CoverageWindowMeasure implements WindowMeasure<HashSet<Integer>> {

    DecimalFormat df = new DecimalFormat("#.###");

    Hashtable<Integer, Integer> nodesCount = new Hashtable<>();
    int num_windows = 0;
    long running_diversity = 0;
    int windowSize;

    @Override
    public void reset() {
        nodesCount = new Hashtable<>();
        num_windows = 0;
        running_diversity = 0;
    }

    @Override
    public String getHeader() { return "nodesSeenInWindow#";  }

    public int getWindowSize() {
        return windowSize;
    }

    public CoverageWindowMeasure(int windowSize) {

        this.windowSize = windowSize;
    }

    @Override
    public void addItem(Window<HashSet<Integer>> window, HashSet<Integer> removed, HashSet<Integer> clique) {

        if (removed != null) {

            for (Integer x : removed) {
                int multiplicity = nodesCount.get(x);
                multiplicity--;
                if (multiplicity == 0) nodesCount.remove(x);
                else nodesCount.put(x, multiplicity);
            }
        }

        for (Integer x : clique) {
            nodesCount.merge(x, 1, Integer::sum);
        }

        if (window.isFull()) {
            num_windows++;
            running_diversity += nodesCount.size();
        }

    }

    @Override
    public String getDiversityValue() {

        if (num_windows >= 1)
            return df.format(running_diversity / ((float)num_windows));
        return "0";
    }



    public String toString() {
        return "WindowUnionMeasure, windowSize, " + getWindowSize() +
                ", diversityValue, " + getDiversityValue();
    }
}
