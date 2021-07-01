package edu.cs.satassignments.traversal;

import java.util.HashMap;
import java.util.Stack;

import edu.cs.satassignments.Formula;

public class BKTraversalClassic implements BKTraversal {

    final Formula formula;
    Stack<VariableNode> stack;
    int nonCliquesCreated = 0;

    public BKTraversalClassic(Formula formula) {
        this.formula = formula;
        stack = new Stack<>();
        VariableNode bk = new VariableNode(formula);
        stack.push(bk);
    }

    @Override
    public boolean finishedExecution() {
        return stack.isEmpty();
    }

    @Override
	public HashMap<Integer, Boolean> getNext() {
		HashMap<Integer, Boolean> assignment = null;

		while (!stack.isEmpty()) {

			VariableNode next = stack.pop();
			if (next.isFinal()) {
				assignment = next.getAssignment();
				while (!next.hasBrother() && !stack.isEmpty()) {
					next = stack.pop();
				}
				if (next.hasBrother()) {
					VariableNode brother = next.getBrother();
					stack.push(brother);
				}
			} else {
				VariableNode child = next.getNextChild();
				stack.push(next);
				stack.push(child);
			}

			if (assignment != null) {
				return assignment;
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
