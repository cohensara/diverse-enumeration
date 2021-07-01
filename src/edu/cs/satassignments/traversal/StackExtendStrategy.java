package edu.cs.satassignments.traversal;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Stack;

public abstract class StackExtendStrategy {

    abstract public boolean addSingleStack(LinkedList<Stack<VariableNode>> allStacks,
                                           ListIterator<Stack<VariableNode>> stackIterator);

    abstract public String getName();
}
