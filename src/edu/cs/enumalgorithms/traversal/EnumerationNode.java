package edu.cs.enumalgorithms.traversal;

import java.util.Random;

public abstract class EnumerationNode<T> {

    public enum PivotType {LOWEST_ID, ARBITRARY};
    static int howManyCreated = 0;
    static PivotType pivotType = PivotType.LOWEST_ID;
    static Random rand = new Random();
    protected int level = 0;


    public static void reset() {

        howManyCreated = 0;
        pivotType = PivotType.LOWEST_ID;
        rand = new Random();
    }

    public int getLevel() {
        return level;
    }

    public static int getHowManyCreated() {
        return howManyCreated;
    }

    public static void setPivot(PivotType type) {
        pivotType = type;
    }

    public abstract boolean isFinal();
    public abstract T getAnswer();
    public abstract boolean isFinalSuccess();
    public abstract boolean hasMoreChildren();
    public abstract int numberOfChildrenLeft();
    public abstract EnumerationNode<T> getNextChild();
}
