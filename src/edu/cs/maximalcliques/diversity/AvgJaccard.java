package edu.cs.maximalcliques.diversity;

import java.text.DecimalFormat;
import java.util.HashSet;

public class AvgJaccard implements WindowMeasure{

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

    public AvgJaccard(int windowSize) {
        this.windowSize = windowSize;
    }

    @Override
    public int getWindowSize() {
        return windowSize;
    }

    @Override
    public void addClique(Window window, HashSet<Integer> removed, HashSet<Integer> added) {

        if (!window.isFull()) return;
        numberOfWindows++;
        float jacSum = 0;

        for (int i = 0 ; i < windowSize - 1 ; i++) {
            HashSet<Integer> clique = window.get(i);
            jacSum += computeJaccard(clique, added);
        }

        System.out.println("!!!! Measure is implemented incorrectly !!!!");
        runningSum += jacSum / (windowSize - 1);
    }

    @Override
    public String getDiversityValue() {

        if (numberOfWindows == 0) return "0";
        return df.format(runningSum / numberOfWindows);
    }

    @Override
    public void reset() {
        numberOfWindows = 0;
        runningSum = 0;
    }

    @Override
    public String getHeader() {
        return "avgJaccard";
    }

    public String toString() {
        return "AvgJaccard, windowSize, " + windowSize + ", diversity, " + getDiversityValue();
    }
}
