package edu.cs.enumalgorithms.diversity.cliques;

import edu.cs.enumalgorithms.diversity.Window;
import edu.cs.enumalgorithms.diversity.WindowMeasure;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Hashtable;

public class AvgMaxJWindowMeasure implements WindowMeasure<HashSet<Integer>> {

    DecimalFormat df = new DecimalFormat("#.###");
    private final int windowSize;
    int numberOfWindows = 0;
    float runningSum = 0;
    Hashtable<HashSet<Integer>, Float> maxValues = null;

    private float computeJaccard(HashSet<Integer> clique1, HashSet<Integer> clique2) {
        int overlap = 0;
        if (clique1.size() < clique2.size()) {
            for (Integer x : clique1) {
                if (clique2.contains(x)) overlap++;
            }
        }
        else{
            for (Integer x : clique2) {
                if (clique1.contains(x)) overlap++;
            }
        }

        return ((float)overlap) / (clique1.size() + clique2.size() - overlap);
    }

    public AvgMaxJWindowMeasure(int windowSize) {
        this.windowSize = windowSize;

    }

    @Override
    public int getWindowSize() {
        return windowSize;
    }

   // @Override
    public void addCliqueOld(Window<HashSet<Integer>> window, HashSet<Integer> removed, HashSet<Integer> added) {

        if (!window.isFull()) return;
        numberOfWindows++;
        float sum = 0;

        for (int i = 0; i < windowSize; i++) {
            float max = 0;
            for (int j = 0; j < windowSize; j++) {
                if (i == j) continue;
                float f = computeJaccard(window.get(i), window.get(j));
                if (f > max) max = f;
            }
            sum += max;
        }
        runningSum += sum / windowSize;
    }


    @Override
    public void addItem(Window<HashSet<Integer>> window, HashSet<Integer> removed, HashSet<Integer> added) {

        if (!window.isFull()) return;
        numberOfWindows++;
        float sum = 0;

        if (maxValues == null) {
            //first time computing the max values
            maxValues = new Hashtable<>();
            for (int i = 0 ; i < windowSize ; i++) {
                HashSet<Integer> clique1 = window.get(i);
                float max = 0;
                for (int j = 0 ; j < windowSize ; j++) {
                    if (i == j) continue;
                    HashSet<Integer> clique2 = window.get(j);
                    float val = computeJaccard(clique1, clique2);
                    if (val > max) {
                        max = val;
                    }
                }
                maxValues.put(clique1, max);
                sum += max;
            }
            sum = sum / windowSize;
            runningSum += sum;
        }
        else {
            maxValues.remove(removed);
            float maxForAdded = 0;
            for (int i = 0; i < windowSize - 1; i++) {
                HashSet<Integer> clique1 = window.get(i);
                float oldVal = maxValues.get(clique1);
                float newVal = computeJaccard(clique1, added);
                if (maxForAdded < newVal) maxForAdded = newVal;
                if (newVal >= oldVal) {
                    maxValues.put(clique1, newVal);
                }
                else {
                    float valWithRemoved = computeJaccard(clique1, removed);
                    if (valWithRemoved == oldVal) {
                        newVal = 0;
                        for (int j = 0 ; j < windowSize ; j++) {
                            if (j == i) continue;
                            float f = computeJaccard(clique1, window.get(j));
                            if (f > newVal) newVal = f;
                        }
                        maxValues.put(clique1, newVal);
                    }
                    else newVal = oldVal;
                }
                sum += newVal;
            }
            maxValues.put(added, maxForAdded);
            sum += maxForAdded;
            sum = sum / (windowSize);
            runningSum += sum;
        }
    }

    @Override
    public String getDiversityValue() {

        if (numberOfWindows == 0) {
            return "0";
        }
       // return df.format(runningSum / numberOfWindows);
        return "(" + windowSize + "," + df.format(runningSum / numberOfWindows) + ")";
    }

    @Override
    public void reset() {
        numberOfWindows = 0;
        runningSum = 0;
        maxValues = null;
    }

    @Override
    public String getHeader() {
        return "AvgMaxJ";
    }

    public String toString() {
        return "AvgMaxJ, windowSize, " + windowSize + ", diversity, " + getDiversityValue();
    }
}
