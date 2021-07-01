package edu.cs.maximalcliques.diversity;

import java.util.HashSet;
import java.util.Hashtable;

public class WindowInverseProportionalCoverage implements WindowMeasure {

    Hashtable<Integer, Integer> nodesCount = new Hashtable<>();
    int num_windows = 0;
    float running_diversity = 0;
    int windowSize;
    int cliquesInWindowSize = 0;

    @Override
    public void reset() {
        nodesCount = new Hashtable<>();
        num_windows = 0;
        running_diversity = 0;
        cliquesInWindowSize = 0;
    }

    @Override
    public String getHeader() { return "inverseSizeCoverage#";  }

    public int getWindowSize() {
        return windowSize;
    }

    public WindowInverseProportionalCoverage(int windowSize) {

        this.windowSize = windowSize;
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
            cliquesInWindowSize -= removed.size();
        }


        for (Integer x : clique) {
            nodesCount.merge(x, 1, Integer::sum);
        }

        cliquesInWindowSize += clique.size();

        if (window.isFull()) {
            num_windows++;
            //System.out.println(cliquesInWindowSize + ", " + nodesCount.size());
            running_diversity += (cliquesInWindowSize - nodesCount.size()) / ((float)cliquesInWindowSize);
        }

    }

    @Override
    public String getDiversityValue() {
        return  Float.toString(running_diversity / ((float)num_windows));
    }



    public String toString() {
        return "WindowUnionMeasure, windowSize, " + getWindowSize() +
                ", diversityValue, " + getDiversityValue();
    }
}
