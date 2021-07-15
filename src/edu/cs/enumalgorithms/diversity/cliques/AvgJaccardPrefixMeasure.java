package edu.cs.enumalgorithms.diversity.cliques;

import edu.cs.enumalgorithms.diversity.Measure;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AvgJaccardPrefixMeasure implements Measure<HashSet<Integer>> {

    DecimalFormat df;
    int numCliques = 0;
    Vector<HashSet<Integer>> allCliques = new Vector<>();
    String diversityString = null;
    int[] prefixSizes;

    private float computeJaccard(HashSet<Integer> clique1, HashSet<Integer> clique2) {
        int overlap = 0;
        for (Integer x : clique1) {
            if (clique2.contains(x)) overlap++;
        }

        return ((float)overlap) / (clique1.size() + clique2.size() - overlap);
    }

    public AvgJaccardPrefixMeasure(int[] prefixSizes) {
        this(prefixSizes, "#.###");
    }

    public AvgJaccardPrefixMeasure(int[] prefixSizes, String pattern) {
        this.prefixSizes = prefixSizes;
        df = new DecimalFormat(pattern);
    }


    @Override
    public void addItem(HashSet<Integer> clique) {

        float sum = 0;

        numCliques++;
        allCliques.add(clique);

        if (Arrays.stream(prefixSizes).anyMatch(i-> i == numCliques)) {

            for (HashSet<Integer> clique1: allCliques) {
                for (HashSet<Integer> clique2: allCliques) {
                    sum += computeJaccard(clique1, clique2);
                }
            }
            //remove one, since we compare a clique to itself
            sum--;
            if (diversityString == null) {
                diversityString = "";
            }
            else diversityString += ", ";
            diversityString += df.format(2 * sum/(numCliques * (numCliques-1)));
        }
    }

    @Override
    public String getDiversityValue() {
        return diversityString;
    }

    @Override
    public void reset() {
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
