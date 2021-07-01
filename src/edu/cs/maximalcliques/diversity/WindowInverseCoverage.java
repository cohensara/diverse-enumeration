package edu.cs.maximalcliques.diversity;

import java.util.HashSet;
import java.util.Hashtable;

public class WindowInverseCoverage implements WindowMeasure {

    Hashtable<Integer, Integer> nodesCount = new Hashtable<>();
    int num_windows = 0;
    long running_diversity = 0;
    int windowSize;
    int numberOfNodes;

    @Override
    public void reset() {
        nodesCount = new Hashtable<>();
        num_windows = 0;
        running_diversity = 0;
    }

    @Override
    public String getHeader() { return "inverseTotalCoverage#";  }

    public int getWindowSize() {
        return windowSize;
    }

    public WindowInverseCoverage(int windowSize, int numberOfNodes) {

        this.windowSize = windowSize;
        this.numberOfNodes = numberOfNodes;
    }

    @Override
    public void addClique(Window window, HashSet<Integer> removed, HashSet<Integer> clique) {

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
        return Float.toString(1 - running_diversity / ((float)num_windows *numberOfNodes)); //num_windows - running_diversity / ((float)num_windows*numberOfNodes);
    }



    public String toString() {
        return "WindowUnionMeasure, windowSize, " + getWindowSize() +
                ", diversityValue, " + getDiversityValue();
    }
}
