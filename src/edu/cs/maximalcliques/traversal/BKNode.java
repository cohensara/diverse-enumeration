package edu.cs.maximalcliques.traversal;

import edu.cs.maximalcliques.Graph;

import java.util.*;
import java.util.stream.Collectors;

public class BKNode {

    public enum PivotType {LOWEST_ID, GREATEST_INTERSECTION, ARBITRARY, UNSEEN}
    static int howManyCreated = 0;
    Graph graph;
    HashSet<Integer> P, X;
    HashSet<Integer> R;
    int level;
    static PivotType pivotType = PivotType.LOWEST_ID;
    static HashSet<Integer> seenNodes;
    static public Random rand = new Random();

    static public void reset() {
        howManyCreated = 0;
        pivotType = PivotType.LOWEST_ID;
        seenNodes = new HashSet<>();
        rand = new Random();
    }

    Integer[] PMinusNPivot = null;
    int childIndex = 0;

    public BKNode(Graph graph) {
        this.R = new HashSet<Integer>(100);
        this.P =  new HashSet<>(graph.getNodes());
        this.X = new HashSet<Integer>(100);
        this.graph = graph;
        level = 0;
        seenNodes = new HashSet<>();
        howManyCreated++;
        if (!isFinal()) setIteration();
    }

    public static int getHowManyCreated() {
        return howManyCreated;
    }

    public BKNode(HashSet<Integer> R, HashSet<Integer> P,
                  HashSet<Integer> X, Graph graph, int level) {
        this.R = R;
        this.P = P;
        this.X = X;
        this.graph = graph;
        this.level = level;
        howManyCreated++;
        if (!isFinal()) setIteration();
    }

    public int getLevel() {
        return level;
    }

    public boolean isFinal() {
        return P.isEmpty();
    }

    public HashSet<Integer> getClique() {
        if (P.isEmpty() && X.isEmpty()) {
            for (int v : R) seenNodes.add(v);
            return R;
        }
        return null;
    }

    public boolean isFinalSuccess() {
        return P.isEmpty() && X.isEmpty();
    }

    public static void setPivot(PivotType type) {
        pivotType = type;
        seenNodes = new HashSet<>();
    }

    private int getPivot() {

        int newPivot = -1;
        //return the node with the lowest id
        if (pivotType == PivotType.LOWEST_ID) {
            newPivot = P.stream().mapToInt(v -> v).min().orElse(-1);
        }
        //return the element with the greatest intersection in the set
        else if (pivotType == PivotType.GREATEST_INTERSECTION)
            newPivot = P.stream().mapToInt(v ->
                graph.getNeighbors(v).stream().filter(P::contains).collect(Collectors.toSet()).size()).max().
                orElse(-1);
        //return an arbitrary element
        else if (pivotType == PivotType.ARBITRARY) {
            int index = rand.nextInt(P.size());
            Iterator<Integer> iterator = P.iterator();
            for (int i = 0 ; i < index ; i++) iterator.next();
            newPivot = iterator.next();
        }
        //return node with lowest id that has not yet been returned by any clique.
        //If all have been returned, then return lowest id
        else if (pivotType == PivotType.UNSEEN) {
            if (seenNodes.size() == graph.getNumNodes()) {
                pivotType = PivotType.LOWEST_ID;
                newPivot = getPivot();
            }
            else {
                HashSet<Integer> choices = myDifference(P, seenNodes);
                if (choices.isEmpty()) return P.stream().mapToInt(v -> v).min().orElse(-1);
                newPivot = choices.stream().mapToInt(v -> v).min().orElse(-1);
            }
        }
        //System.out.println("pivot:" + newPivot);
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
        //return PMinusNPivotIterator.hasNext();
    }

    public int numberOfChildrenLeft() {
        if (isFinal()) return 0;
        return PMinusNPivot.length - childIndex;
    }

    private HashSet<Integer> myIntersection(HashSet<Integer> l1, HashSet<Integer> l2) {

        if (l1.size() < l2.size()) {
            HashSet<Integer> intersection = new HashSet<Integer>(l1.size());;
            for (Integer x: l1) {
                if (l2.contains(x)) intersection.add(x);
            }
            return intersection;
        }
        HashSet<Integer> intersection = new HashSet<Integer>(l2.size());
        for (Integer x: l2) {
            if (l1.contains(x)) intersection.add(x);
        }
        return intersection;
    }

    private HashSet<Integer> myDifference(HashSet<Integer> l1, HashSet<Integer> l2) {
        return l1.stream().filter(x -> !l2.contains(x)).collect(Collectors.toCollection(HashSet::new));
    }

    public BKNode getNextChild() {

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
        return new BKNode(nextR, nextP, nextX, graph, level+1);
    }

    /*public void delete() {
        P = null;
        X = null;
        R = null;
    }*/

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
