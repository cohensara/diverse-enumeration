package edu.cs.enumalgorithms.traversal;

import java.util.*;

//TODO: Check that expanding stack is done correctly
public class DiverseEnum<T> implements EnumerationAlgorithm<T> {

    PriorityRoundRobin<MyStack<T>> prrAllStacks;

    int maxStacks;
    int failureBound = 50;

    int stacksAdded_stat = 0;
    int stacksRemoved_stat = 0;
    int passedFailureBound_stat = 0;
    int totalFailed_stat = 0;
    boolean failedFlag = false;

    public DiverseEnum(EnumerationNode<T> node, int maxStacks) {
        //does not use an external extend strategy

        this.maxStacks = maxStacks;
        MyStack<T> stack = new MyStack<>();
        stack.push(node);
        PriorityRoundRobin.reset();
        prrAllStacks = new PriorityRoundRobin<>();
        prrAllStacks.add(stack.getScore(), stack);
    }

    @Override
    public T getNext() {

        while (!prrAllStacks.isEmptyAllItems()) {
            int stackId = prrAllStacks.nextId();
            MyStack<T> stack = prrAllStacks.idValue(stackId);
            T answer = getNextFromStack(stack);
            if (answer == null && !failedFlag) {
                //stack has been emptied
                prrAllStacks.removeId(stackId);
                stacksRemoved_stat++;
            }
            else {
                //stack has not been emptied; update score
                prrAllStacks.updateScore(stackId, stack.getScore());
            }

            if (prrAllStacks.numAllItems() < maxStacks && prrAllStacks.hasScoredItems()) {
                int bestId = prrAllStacks.nextBestId();
                MyStack<T> toSplit = prrAllStacks.idValue(bestId);
                MyStack<T> newStack = toSplit.split();
                prrAllStacks.updateScore(bestId, toSplit.getScore());
                prrAllStacks.add(newStack.getScore(), newStack);
            }

            if (answer != null && !failedFlag) {
                return answer;
            }
            else failedFlag = false;
        }

        return null;
    }

    //get the next clique from the given stack
    //if number_failed > failed_bound, quit early and return the empty array list
    private T getNextFromStack(MyStack stack) {

        int numFailed = 0;
        while (!stack.isEmpty()) {
            EnumerationNode<T> node = stack.pop();
            if (node.isFinal()) {
                if (node.isFinalSuccess()) {
                    return node.getAnswer();
                }
                else {
                    totalFailed_stat++;
                    numFailed++;
                    if (numFailed > failureBound) {
                        passedFailureBound_stat++;
                        failedFlag = true;
                        return null;
                    }
                }
            } else {
                if (node.hasMoreChildren()) {
                    EnumerationNode child = node.getNextChild();
                    if (node.hasMoreChildren()) stack.push(node);
                    stack.push(child);
                }
            }
        }
        return null;
    }

    @Override
    public boolean finishedExecution() {
        return prrAllStacks.isEmptyAllItems();
    }

    public String getStatisticsHeader() {
        return "stacksAdded#, stacksRemoved#, passedFailureBound#, totalFailed#";
    }


    public String getStatistics() {
        return stacksAdded_stat + ", " + stacksRemoved_stat + ", " + passedFailureBound_stat + ", " + totalFailed_stat;
    }

    @Override
    public String getName() {
        return "Multi ExpandStackStrategy=" + "internal" + " instanceBound=" + maxStacks;
    }
}


class MyStack<T> {

    Stack<EnumerationNode<T>> stack = new Stack<>();
    int splitIndex = -1;

    void push(EnumerationNode<T> node) {
        stack.push(node);
        if (splitIndex == -1 && node.numberOfChildrenLeft() > 1) {
            splitIndex = stack.size() - 1;
        }
    }

    boolean isFinal() {
        return stack.elementAt(0).isFinal();
    }

    EnumerationNode<T>  pop() {
        if (splitIndex == stack.size() - 1)
            splitIndex = -1;
        return stack.pop();
    }

    boolean canBeSplit() {
        return splitIndex != -1;
    }

    MyStack<T> split() {
        if (!canBeSplit()) {
            System.out.println("Trying to split unsplittable stack!!");
        }

        EnumerationNode<T>  splitNode = stack.elementAt(splitIndex);
        EnumerationNode<T>  child = splitNode.getNextChild();
        MyStack newStack = new MyStack();
        newStack.push(child);

        int oldSplitIndex = splitIndex;
        splitIndex = -1;
        for (int i = oldSplitIndex ; i < stack.size() - 1; i++) {
            if (stack.elementAt(i).hasMoreChildren()) {
                splitIndex = i;
                break;
            }
        }
        if (splitIndex == -1 && stack.elementAt(stack.size() -1).numberOfChildrenLeft() > 1)
            splitIndex = stack.size() - 1;

        return newStack;
    }

    boolean isEmpty() {
        return stack.isEmpty();
    }

    int getScore() {
        if (splitIndex == -1) return -1;
        return stack.elementAt(splitIndex).getLevel();
    }
}

