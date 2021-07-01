package edu.cs.maximalcliques.traversal;

import edu.cs.maximalcliques.Graph;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Stack;
import java.util.TreeSet;

public class BKTraversalMultiStableButWrongPQ implements BKTraversal {

    MyList<Stack<BKNode>> allStacks;
    TreeSet<Stack<BKNode>> allStacksPQ;
    Graph graph;

    int maxStacks;
    int failureBound = 50;

    int stacksAdded_stat = 0;
    int stacksRemoved_stat = 0;
    int passedFailureBound_stat = 0;
    int totalFailed_stat = 0;

    public BKTraversalMultiStableButWrongPQ(Graph graph, int maxStacks) {
        //does not use an external extend strategy

        this.graph = graph;
        this.maxStacks = maxStacks;
        Stack<BKNode> stack = new Stack<>();
        BKNode par = new BKNode(graph);
        BKNode child = par.getNextChild();
        pushIntoStack(par, child, stack);
        allStacks = new MyList<>(stack);
        allStacksPQ = new TreeSet<>(new StackComparator());
        addToPQ(stack);
    }

    private boolean addToPQ(Stack<BKNode> stack) {
        if (stack.size() > 1 && stack.elementAt(0).hasMoreChildren()) {
            allStacksPQ.add(stack);
            return true;
        }
        return false;
    }

    //get the next clique from the given stack
    //if number_failed > failed_bound, quit early and return the empty array list
    private HashSet<Integer> getNext(Stack<BKNode> stack, int failed_bound) {

        int numFailed = 0;
        allStacksPQ.remove(stack);
        while (!stack.isEmpty()) {

            BKNode next = stack.pop();
            if (next.isFinal()) {
                HashSet<Integer> clique = next.getClique();
                if (clique != null) {
                    addToPQ(stack);
                    return clique;
                }
                else {
                    totalFailed_stat++;
                    numFailed++;
                    if (numFailed > failed_bound) {
                        passedFailureBound_stat++;
                        addToPQ(stack);
                        return new HashSet<>();
                    }
                }
            } else {
                if (next.hasMoreChildren()) {
                    BKNode child = next.getNextChild();
                    pushIntoStack(next, child, stack);
                }
            }
        }
        addToPQ(stack);
        return null;
    }

    public static void pushIntoStack(BKNode par, BKNode child, Stack<BKNode> stack) {

        while (!par.hasMoreChildren() && child.hasMoreChildren()) {
            par = child;
            child = child.getNextChild();
        }

        if (par.hasMoreChildren()) {
            stack.push(par);
        }
        stack.push(child);
    }

    @Override
    public boolean finishedExecution() {
        return allStacks.size() == 0;
    }

    @Override
    public HashSet<Integer> getNext() {

        //if (allStacksPQ.size() != allStacks.size()) {
        //    System.out.println("pq=" + allStacksPQ.size() + " ll=" + allStacks.size());
        //}
        while (!finishedExecution()) {
            Stack<BKNode> currentStack = allStacks.next();

            HashSet<Integer> clique = getNext(currentStack, failureBound);

            //new clique found
            if (clique != null && clique.size() > 0) {
                //Should I do this always or only when a new clique is found?
                if (allStacks.size() < maxStacks && !allStacksPQ.isEmpty()) {
                    boolean success = addSingleStack();
                    if (success) stacksAdded_stat++;
                }
                return clique;
            }

            //stack was emptied
            if (clique == null) {
                allStacks.removeCurrent();
                stacksRemoved_stat++;
            }
        }

        return null;
    }

    public boolean addSingleStack() {
        Stack<BKNode> stackToExpand = allStacksPQ.pollFirst();
        if (stackToExpand.size() <= 1) return false;

        BKNode child = stackToExpand.elementAt(0).getNextChild();
        Stack<BKNode> stack = new Stack<>();
        if (child.hasMoreChildren()) {
            BKNode par = child;
            child = par.getNextChild();
            pushIntoStack(par, child, stack);
        }
        else {
            stack.push(child);
        }

        //fix stack
        while (stackToExpand.size() > 1 && !stackToExpand.elementAt(0).hasMoreChildren()) {
            stackToExpand.remove(0);
        }

        addToPQ(stackToExpand);
        allStacks.add(stack);
        addToPQ(stack);
        return true;
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


class StackComparator implements Comparator<Stack<BKNode>> {

    @Override
    public int compare(Stack<BKNode> o1, Stack<BKNode> o2) {
        if (o1.size() > 1 && o2.size() > 1) {
            return o1.elementAt(0).getLevel() - o2.elementAt(0).getLevel();
        }
        if (o1.size() > 1) return -1;
        if (o2.size() > 1) return 1;
        return 0;
    }
}

