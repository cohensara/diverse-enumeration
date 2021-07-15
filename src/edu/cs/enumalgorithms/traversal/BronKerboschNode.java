package edu.cs.enumalgorithms.traversal;

import edu.cs.enumalgorithms.Graph;

import java.util.*;
import java.util.stream.Collectors;

public class BronKerboschNode extends EnumerationNode<HashSet<Integer>> {

    static PivotType pivotType = PivotType.LOWEST_ID;
    static public Random rand = new Random();

    Graph graph;
    HashSet<Integer> P, X;
    HashSet<Integer> R;
    Integer[] PMinusNPivot = null;
    int childIndex = 0;
    
    static public void reset() {
        howManyCreated = 0;
        pivotType = PivotType.LOWEST_ID;
        rand = new Random();
    }
    

    public BronKerboschNode(Graph graph) {
        this.R = new HashSet<>(100);
        this.P =  new HashSet<>(graph.getNodes());
        this.X = new HashSet<>(100);
        this.graph = graph;
        level = 0;
        howManyCreated++;
        if (!isFinal()) setIteration();
    }

    public static int getHowManyCreated() {
        return howManyCreated;
    }

    public BronKerboschNode(HashSet<Integer> R, HashSet<Integer> P,
                            HashSet<Integer> X, Graph graph, int level) {
        this.R = R;
        this.P = P;
        this.X = X;
        this.graph = graph;
        this.level = level;
        howManyCreated++;
        if (!isFinal()) setIteration();
    }

    public boolean isFinal() {
        return P.isEmpty();
    }

    public HashSet<Integer> getAnswer() {
        if (P.isEmpty() && X.isEmpty()) {
            return R;
        }
        return null;
    }

    public boolean isFinalSuccess() {
        return P.isEmpty() && X.isEmpty();
    }

    public static void setPivot(PivotType type) {
        pivotType = type;
    }

    private int getPivot() {

        int newPivot;
        //return the node with the lowest id
        if (pivotType == PivotType.LOWEST_ID) {
            newPivot = P.stream().mapToInt(v -> v).min().orElse(-1);
        }
        //return an arbitrary element
        else  {
            int index = rand.nextInt(P.size());
            Iterator<Integer> iterator = P.iterator();
            for (int i = 0 ; i < index ; i++) iterator.next();
            newPivot = iterator.next();
        }
        return newPivot;
    }

    private void setIteration() {

        int pivot = getPivot();
        //PMinusNPivotIterator = myDifference(P, graph.getNeighbors(pivot)).iterator();
        HashSet<Integer> diff = myDifference(P, graph.getNeighbors(pivot));
        PMinusNPivot = diff.toArray(new Integer[diff.size()]);
        childIndex = 0;
    }

    public boolean hasMoreChildren() {
        if (isFinal()) return false;
        return childIndex < PMinusNPivot.length;
    }

    public int numberOfChildrenLeft() {
        if (isFinal()) return 0;
        return PMinusNPivot.length - childIndex;
    }

    private HashSet<Integer> myIntersection(HashSet<Integer> l1, HashSet<Integer> l2) {

        if (l1.size() < l2.size()) {
            HashSet<Integer> intersection = new HashSet<>(l1.size());
            for (Integer x: l1) {
                if (l2.contains(x)) intersection.add(x);
            }
            return intersection;
        }
        HashSet<Integer> intersection = new HashSet<>(l2.size());
        for (Integer x: l2) {
            if (l1.contains(x)) intersection.add(x);
        }
        return intersection;
    }

    private HashSet<Integer> myDifference(HashSet<Integer> l1, HashSet<Integer> l2) {
        return l1.stream().filter(x -> !l2.contains(x)).collect(Collectors.toCollection(HashSet::new));
    }

    public BronKerboschNode getNextChild() {

        //int v = PMinusNPivotIterator.next();
        if (pivotType == PivotType.ARBITRARY) {
            int i = rand.nextInt(PMinusNPivot.length - childIndex);
            int j = PMinusNPivot[childIndex];
            PMinusNPivot[childIndex] = PMinusNPivot[childIndex + i];
            PMinusNPivot[childIndex + i] = j;
        }
        int v = PMinusNPivot[childIndex];

        childIndex++;

        HashSet<Integer> nextR = new HashSet<>(R);
        nextR.add(v);
        HashSet<Integer> neighbors = graph.getNeighbors(v);

        HashSet<Integer> nextP = myIntersection(P, neighbors);
        HashSet<Integer> nextX = myIntersection(X, neighbors);

        P.remove(v);
        X.add(v);
        return new BronKerboschNode(nextR, nextP, nextX, graph, level+1);
    }


    @Override
    public String toString() {
        if (isFinal()) return "FINAL BKNode{" +
            "R=" + R + ", P=" + P + ", X=" + X + ", level=" + level + '}';
        return "BKNode{" +
                "R=" + R +
                ", P=" + P +
                ", X=" + X +
                //", PMinusNPivotIterator=" + PMinusNPivotIterator.hasNext() +
                ", level=" + level +
                '}';
    }
}
