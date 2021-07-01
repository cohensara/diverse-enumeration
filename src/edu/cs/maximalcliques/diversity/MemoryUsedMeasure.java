package edu.cs.maximalcliques.diversity;

import java.util.HashSet;

public class MemoryUsedMeasure implements Measure{

    Runtime rt = Runtime.getRuntime();
    long memoryUsed = 0;
    long totalMemory = rt.totalMemory();

    @Override
    public void addClique(HashSet<Integer> clique) {
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
