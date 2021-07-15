package edu.cs.enumalgorithms.diversity.cliques;

import edu.cs.enumalgorithms.diversity.Measure;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UnionPrefixMeasure implements Measure<HashSet<Integer>> {

    DecimalFormat df = new DecimalFormat("#.###");
    int numCliques = 0;
    int totalSize = 0;
    HashSet<Integer> nodes = new HashSet<>();
    int diversity = 0;
    String diversityString = null;
    int[] prefixSizes;

    public UnionPrefixMeasure(int[] prefixSizes) {
        this.prefixSizes = prefixSizes;
    }

    @Override
    public void addItem(HashSet<Integer> clique) {
        numCliques++;
        totalSize += clique.size();
        for (int i: clique) {
            if (nodes.add(i)) {
                diversity++;
            }
        }
        if (Arrays.stream(prefixSizes).anyMatch(i-> i == numCliques)) {
            if (diversityString == null) {
                diversityString = "";
            }
            else diversityString += ", ";
            diversityString += df.format(diversity);
        }
    }

    @Override
    public String getDiversityValue() {
        return diversityString;
    }

    @Override
    public void reset() {
        diversity = 0;
        diversityString = null;
        numCliques = 0;
        totalSize = 0;
        nodes = new HashSet<>();
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
