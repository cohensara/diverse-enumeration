package edu.cs.maximalcliques.diversity;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MaxJaccardPrefixMeasure implements Measure{

    DecimalFormat df;
    int numCliques = 0;
    Vector<HashSet<Integer>> allCliques = new Vector<>();
    float sumMax = 0;
    String diversityString = null;
    int[] prefixSizes;

    private float computeJaccard(HashSet<Integer> clique1, HashSet<Integer> clique2) {
        int overlap = 0;
        for (Integer x : clique1) {
            if (clique2.contains(x)) overlap++;
        }

        return ((float)overlap) / (clique1.size() + clique2.size() - overlap);
    }

    public MaxJaccardPrefixMeasure(int[] prefixSizes) {
        this(prefixSizes, "#.###");
    }

    public MaxJaccardPrefixMeasure(int[] prefixSizes, String pattern) {
        this.prefixSizes = prefixSizes;
        df = new DecimalFormat(pattern);
    }

    @Override
    public void addClique(HashSet<Integer> clique) {

        float sum = 0;
        numCliques++;
        allCliques.add(clique);

        if (Arrays.stream(prefixSizes).anyMatch(i-> i == numCliques)) {

            for (HashSet<Integer> clique1: allCliques) {
                float max = 0;
                for (HashSet<Integer> clique2: allCliques) {
                    float m = computeJaccard(clique1, clique2);
                    if (m > max && m != 1) max = m;
                }
                sum += max;
            }
            if (diversityString == null) {
                diversityString = "";
            }
            else diversityString += ", ";
            diversityString += df.format(sum/numCliques);
        }
    }

    @Override
    public String getDiversityValue() {
        return diversityString;
    }

    @Override
    public void reset() {
        sumMax = 0;
        diversityString = null;
        numCliques = 0;
        allCliques = new Vector<>();
    }

    @Override
    public String getHeader() {
        /*String header = "UnionPrefix (" + prefixSizes[0] + ")";
        for (int i = 1 ; i < prefixSizes.length ; i++) {
            header += ", UnionPrefix (" + prefixSizes[i] + ")";
        }*/
        return IntStream.of(prefixSizes).mapToObj(Integer::toString).collect(Collectors.joining(", "));
    }
}
