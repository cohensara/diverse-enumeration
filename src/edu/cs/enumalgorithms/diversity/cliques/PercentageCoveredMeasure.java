package edu.cs.enumalgorithms.diversity.cliques;

import edu.cs.enumalgorithms.diversity.Measure;

import java.util.HashSet;

public class PercentageCoveredMeasure implements Measure<HashSet<Integer>> {

    int numNodes;
    float percentage;
    long numCliques;
    float passedPercentage;
    long totalCliques;
    HashSet<Integer> nodesSeen;

    public PercentageCoveredMeasure(int numNodes, long totalCliques, float percentage) {
        this.numNodes = numNodes;
        this.percentage = percentage;
        passedPercentage = -1;
        this.totalCliques = totalCliques;
        nodesSeen = new HashSet<>();
    }

    @Override
    public void addItem(HashSet<Integer> clique) {
        if (passedPercentage != -1) return;
        numCliques++;

        for (int i : clique) {
            nodesSeen.add(i);
        }
        if (nodesSeen.size() >= percentage * numNodes) passedPercentage = ((float)numCliques)/totalCliques;
    }

    @Override
    public String getDiversityValue() {
        return "(" + percentage + "," + passedPercentage + ")";
    }

    @Override
    public void reset() {
        numCliques = 0;
        nodesSeen = new HashSet<>();
        passedPercentage = -1;
    }

    @Override
    public String getHeader() {
        return "PercentageOfNodes (" + percentage + ")";
    }
}
