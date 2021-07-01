package edu.cs.maximalcliques.diversity;

import java.text.DecimalFormat;
import java.util.HashSet;

public class AvgAllJWindowMeasure implements WindowMeasure{

    DecimalFormat df = new DecimalFormat("#.###");
    private final int windowSize;
    int numberOfWindows = 0;
    float runningSum = 0;
    float previousSum = 0;

    private float computeJaccard(HashSet<Integer> clique1, HashSet<Integer> clique2) {
        int overlap = 0;
        if (clique1.size() < clique2.size())
            for (Integer x : clique1) {
                if (clique2.contains(x)) overlap++;
            }
        else {
            for (Integer x : clique2) {
                if (clique1.contains(x)) overlap++;
            }
        }

        return ((float)overlap) / (clique1.size() + clique2.size() - overlap);
    }

    public AvgAllJWindowMeasure(int windowSize) {
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
        float sum = 0;

        if (previousSum == 0) {
            for (int i = 0; i < windowSize; i++) {
                HashSet<Integer> clique1 = window.get(i);
                for (int j = 0; j < windowSize; j++) {
                    if (i == j) continue;
                    HashSet<Integer> clique2 = window.get(j);
                    sum += computeJaccard(clique1, clique2);
                }
            }
            previousSum = sum;
            sum = sum / (windowSize * (windowSize - 1));
            runningSum += sum;
        }
        else{
            float change = 0;
            for (int i = 0; i < windowSize - 1; i++) {
                HashSet<Integer> clique = window.get(i);
                change -= computeJaccard(removed, clique);
            }
            for (int i = 0; i < windowSize - 1; i++) {
                HashSet<Integer> clique = window.get(i);
                change += computeJaccard(added, clique);
            }
            change = change * 2;
            previousSum = previousSum + change;
            runningSum += previousSum / (windowSize * (windowSize - 1));
        }
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
        previousSum = 0;
    }

    @Override
    public String getHeader() {
        return "AvgAllJ";
    }

    public String toString() {
        return "AvgAllJ, windowSize, " + windowSize + ", diversity, " + getDiversityValue();
    }
}
