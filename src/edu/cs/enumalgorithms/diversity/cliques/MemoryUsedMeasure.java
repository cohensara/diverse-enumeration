package edu.cs.enumalgorithms.diversity.cliques;

import edu.cs.enumalgorithms.diversity.Measure;

import java.util.HashSet;

public class MemoryUsedMeasure implements Measure<HashSet<Integer>> {

    Runtime rt = Runtime.getRuntime();
    long memoryUsed = 0;
    long totalMemory = rt.totalMemory();

    @Override
    public void addItem(HashSet<Integer> clique) {
        long memory = totalMemory - rt.freeMemory();

        if (memory > memoryUsed) memoryUsed = memory;
    }

    @Override
    public String getDiversityValue() {
        return memoryUsed + "";
    }

    @Override
    public void reset() {
        memoryUsed = 0;
    }

    @Override
    public String getHeader() {
        return "Memory Used";
    }
}
