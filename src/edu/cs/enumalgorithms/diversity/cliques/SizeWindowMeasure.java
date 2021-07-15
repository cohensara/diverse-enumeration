package edu.cs.enumalgorithms.diversity.cliques;

import edu.cs.enumalgorithms.diversity.Window;
import edu.cs.enumalgorithms.diversity.WindowMeasure;

import java.text.DecimalFormat;
import java.util.HashSet;

public class SizeWindowMeasure implements WindowMeasure<HashSet<Integer>> {

    DecimalFormat df = new DecimalFormat("#.###");

    int sizeOfCliquesInCurrentWindow = 0;
    int num_windows = 0;
    long running_diversity = 0;
    int windowSize;

    @Override
    public void reset() {
        sizeOfCliquesInCurrentWindow = 0;
        num_windows = 0;
        running_diversity = 0;
    }

    @Override
    public String getHeader() { return "sizeOfCliquesInWindow#";  }

    public int getWindowSize() {
        return windowSize;
    }

    public SizeWindowMeasure(int windowSize) {

        this.windowSize = windowSize;
    }

    @Override
    public void addItem(Window<HashSet<Integer>> window, HashSet<Integer> removed, HashSet<Integer> clique) {

        if (removed != null) sizeOfCliquesInCurrentWindow -= removed.size();
        sizeOfCliquesInCurrentWindow += clique.size();

        if (window.isFull()) {
            num_windows++;
            running_diversity += sizeOfCliquesInCurrentWindow;
        }

    }

    @Override
    public String getDiversityValue() {

        if (num_windows >= 1)
            return df.format(running_diversity / ((float)num_windows));
        return "0";
    }



    public String toString() {
        return "SizeWindowMeasure, windowSize, " + getWindowSize() +
                ", diversityValue, " + getDiversityValue();
    }
}
