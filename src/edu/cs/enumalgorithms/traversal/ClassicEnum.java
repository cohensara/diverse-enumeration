package edu.cs.enumalgorithms.traversal;

import java.util.Stack;

public class ClassicEnum<T> implements EnumerationAlgorithm<T> {

    Stack<EnumerationNode<T>> stack;
    int nonAnswersCreated = 0;

    public ClassicEnum(EnumerationNode<T> firstNode) {
        stack = new Stack<>();
        stack.push(firstNode);
    }

    @Override
    public boolean finishedExecution() {
        return stack.isEmpty();
    }

    @Override
    public T getNext() {

        while (!stack.isEmpty()) {

            EnumerationNode<T> next = stack.pop();
            if (next.isFinal()) {
                T answer = next.getAnswer();
                if (answer != null) {
                    return answer;
                }
                else {
                    nonAnswersCreated++;
                }
            }
            else {
                if (next.hasMoreChildren()) {
                    EnumerationNode<T> child = next.getNextChild();
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
        return "" + nonAnswersCreated;
    }

    @Override
    public String getName() {
        return "Classic";
    }
}
