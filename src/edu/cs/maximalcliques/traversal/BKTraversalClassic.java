package edu.cs.maximalcliques.traversal;

import edu.cs.maximalcliques.Graph;

import java.util.HashSet;
import java.util.Stack;

public class BKTraversalClassic implements BKTraversal {

    final Graph graph;
    Stack<BKNode> stack;
    int nonCliquesCreated = 0;

    public BKTraversalClassic(Graph graph) {
        this.graph = graph;
        stack = new Stack<>();
        BKNode bk = new BKNode(graph);
        stack.push(bk);
    }

    @Override
    public boolean finishedExecution() {
        return stack.isEmpty();
    }

    @Override
    public HashSet<Integer> getNext() {

        while (!stack.isEmpty()) {

            BKNode next = stack.pop();
            if (next.isFinal()) {
                HashSet<Integer> clique = next.getClique();
                if (clique != null) {
                    return clique;
                }
                else {
                    nonCliquesCreated++;
                }
            }
            else {
                if (next.hasMoreChildren()) {
                    BKNode child = next.getNextChild();
                    if (next.hasMoreChildren()) stack.push(next);
                    stack.push(child);
                }
            }
        }


        return null;
    }

    public String getStatisticsHeader() {
        return "totalFailed#";
    }

    public String getStatistics() {
        return "" + nonCliquesCreated;
    }

    @Override
    public String getName() {
        return "Classic";
    }
}
