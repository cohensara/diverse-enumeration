package edu.cs.maximalcliques.diversity;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Hashtable;

public class WindowAlphaCoverage implements WindowMeasure {

    DecimalFormat df = new DecimalFormat("#.###");
    Hashtable<Integer, Integer> nodesCount = new Hashtable<>();
    int num_windows = 0;
    float running_diversity = 0;
    int windowSize;
    int cliquesInWindowSize = 0;
    int numberOfNodes;

    @Override
    public void reset() {
        nodesCount = new Hashtable<>();
        num_windows = 0;
        running_diversity = 0;
        cliquesInWindowSize = 0;
    }

    @Override
    public String getHeader() { return "alphaCoverage#";  }

    public int getWindowSize() {
        return windowSize;
    }

    public WindowAlphaCoverage(int windowSize, int numberOfNodes) {

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
            cliquesInWindowSize -= removed.size();
        }


        for (Integer x : clique) {
            nodesCount.merge(x, 1, Integer::sum);
        }

        cliquesInWindowSize += clique.size();

        if (window.isFull()) {
            num_windows++;
            int alpha = Math.min(cliquesInWindowSize, numberOfNodes);
            //System.out.println(cliquesInWindowSize + ", " + nodesCount.size());
            running_diversity += (alpha - nodesCount.size()) / ((float)alpha);
        }

    }

    @Override
    public String getDiversityValue() {
        return  df.format(running_diversity / ((float)num_windows));
    }



    public String toString() {
        return "WindowUnionMeasure, windowSize, " + getWindowSize() +
                ", diversityValue, " + getDiversityValue();
    }
}
