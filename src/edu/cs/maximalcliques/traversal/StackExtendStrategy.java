package edu.cs.maximalcliques.traversal;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Stack;

public abstract class StackExtendStrategy {

    abstract public boolean addSingleStack(LinkedList<Stack<BKNode>> allStacks,
                                           ListIterator<Stack<BKNode>> stackIterator);

    abstract public String getName();
}
