package edu.cs.enumalgorithms.diversity.cliques;

import edu.cs.enumalgorithms.diversity.Window;
import edu.cs.enumalgorithms.diversity.WindowMeasure;

import java.text.DecimalFormat;
import java.util.HashSet;

public class MaxJaccard implements WindowMeasure<HashSet<Integer>> {

    DecimalFormat df = new DecimalFormat("#.###");
    private final int windowSize;
    int numberOfWindows = 0;
    float runningSum = 0;

    private float computeJaccard(HashSet<Integer> clique1, HashSet<Integer> clique2) {
        int overlap = 0;
        for (Integer x : clique1) {
            if (clique2.contains(x)) overlap++;
        }

        return ((float)overlap) / (clique1.size() + clique2.size() - overlap);
    }

    public MaxJaccard(int windowSize) {
        this.windowSize = windowSize;
    }

    @Override
    public int getWindowSize() {
        return windowSize;
    }

    @Override
    public void addItem(Window<HashSet<Integer>> window, HashSet<Integer> removed, HashSet<Integer> added) {

        if (!window.isFull()) return;
        numberOfWindows++;
        float jacMax = 0;

        for (int i = 0 ; i < windowSize - 1 ; i++) {
            HashSet<Integer> clique = window.get(i);
            float j = computeJaccard(clique, added);
            if (j > jacMax) jacMax = j;
        }

        runningSum += jacMax;
    }

    @Override
    public String getDiversityValue() {

        if (numberOfWindows == 0) {
            return "0";
        }
        return df.format(runningSum / numberOfWindows);
    }

    @Override
    public void reset() {
        numberOfWindows = 0;
        runningSum = 0;
    }

    @Override
    public String getHeader() {
        return "maxJaccard";
    }

    public String toString() {
        return "MaxJaccard, windowSize, " + windowSize + ", diversity, " + getDiversityValue();
    }
}
