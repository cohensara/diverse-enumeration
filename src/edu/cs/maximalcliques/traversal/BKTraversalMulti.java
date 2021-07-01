package edu.cs.maximalcliques.traversal;

import edu.cs.maximalcliques.Graph;

import java.util.*;

//TODO: Check that expanding stack is done correctly
public class BKTraversalMulti implements BKTraversal {

    PriorityRoundRobin<MyStack> prrAllStacks;
    Graph graph;

    int maxStacks;
    int failureBound = 50;

    int stacksAdded_stat = 0;
    int stacksRemoved_stat = 0;
    int passedFailureBound_stat = 0;
    int totalFailed_stat = 0;

    public BKTraversalMulti(Graph graph, int maxStacks) {
        //does not use an external extend strategy

        this.graph = graph;
        this.maxStacks = maxStacks;
        MyStack stack = new MyStack();
        BKNode node = new BKNode(graph);
        stack.push(node);
        PriorityRoundRobin.reset();
        prrAllStacks = new PriorityRoundRobin<>();
        prrAllStacks.add(stack.getScore(), stack);
    }

    @Override
    public HashSet<Integer> getNext() {

        while (!prrAllStacks.isEmptyAllItems()) {
            int stackId = prrAllStacks.nextId();
            MyStack stack = prrAllStacks.idValue(stackId);
            HashSet<Integer> clique = getNextFromStack(stack);
            if (clique == null) {
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
                MyStack toSplit = prrAllStacks.idValue(bestId);
                MyStack newStack = toSplit.split();
                prrAllStacks.updateScore(bestId, toSplit.getScore());
                prrAllStacks.add(newStack.getScore(), newStack);
            }

            if (clique != null && clique.size() > 0) {
                return clique;
            }
        }

        return null;
    }

    //get the next clique from the given stack
    //if number_failed > failed_bound, quit early and return the empty array list
    private HashSet<Integer> getNextFromStack(MyStack stack) {

        int numFailed = 0;
        while (!stack.isEmpty()) {
            BKNode node = stack.pop();
            if (node.isFinal()) {
                if (node.isFinalSuccess()) {
                    return node.getClique();
                }
                else {
                    totalFailed_stat++;
                    numFailed++;
                    if (numFailed > failureBound) {
                        passedFailureBound_stat++;
                        return new HashSet<>();
                    }
                }
            } else {
                if (node.hasMoreChildren()) {
                    BKNode child = node.getNextChild();
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


class MyStack {

    Stack<BKNode> stack = new Stack<>();
    int splitIndex = -1;

    void push(BKNode node) {
        stack.push(node);
        if (splitIndex == -1 && node.numberOfChildrenLeft() > 1) {
            splitIndex = stack.size() - 1;
        }
    }

    boolean isFinal() {
        return stack.elementAt(0).isFinal();
    }

    BKNode pop() {
        if (splitIndex == stack.size() - 1)
            splitIndex = -1;
        return stack.pop();
    }

    boolean canBeSplit() {
        return splitIndex != -1;
    }

    MyStack split() {
        if (!canBeSplit()) {
            System.out.println("Trying to split unsplittable stack!!");
        }

        BKNode splitNode = stack.elementAt(splitIndex);
        BKNode child = splitNode.getNextChild();
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

