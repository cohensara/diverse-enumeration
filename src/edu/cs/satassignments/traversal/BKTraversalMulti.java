package edu.cs.satassignments.traversal;

import java.util.HashMap;
import java.util.Stack;

import edu.cs.satassignments.Formula;

//TODO: Check that expanding stack is done correctly
public class BKTraversalMulti implements BKTraversal {

    PriorityRoundRobin<MyStack> prrAllStacks = new PriorityRoundRobin<>();
    Formula formula;

    int maxStacks;
    int failureBound = 50;

    int stacksAdded_stat = 0;
    int stacksRemoved_stat = 0;
    int passedFailureBound_stat = 0;
    int totalFailed_stat = 0;

    public BKTraversalMulti(Formula formula, int maxStacks) {
        //does not use an external extend strategy

        this.formula = formula;
        this.maxStacks = maxStacks;
        MyStack stack = new MyStack();
        VariableNode node = new VariableNode(formula);
        stack.push(node);
        prrAllStacks.add(stack.getScore(), stack);
    }

    @Override
    public HashMap<Integer, Boolean> getNext() {

        while (!prrAllStacks.isEmptyAllItems()) {
            int stackId = prrAllStacks.nextId();
            MyStack stack = prrAllStacks.idValue(stackId);
            HashMap<Integer, Boolean> assignment = getNextFromStack(stack);
            if (assignment == null) {
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

            if (assignment != null && assignment.size() > 0) {
                return assignment;
            }
        }

        return null;
    }

    //get the next clique from the given stack
    //if number_failed > failed_bound, quit early and return the empty array list
    private HashMap<Integer, Boolean> getNextFromStack(MyStack stack) {
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

    Stack<VariableNode> stack = new Stack<>();
    int splitIndex = -1;

    void push(VariableNode node) {
        stack.push(node);
        if (splitIndex == -1 && node.hasBrother()) {
            splitIndex = stack.size() - 1;
        }
    }

    boolean isFinal() {
        return stack.elementAt(0).isFinal();
    }

    VariableNode pop() {
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

        VariableNode splitNode = stack.elementAt(splitIndex);
        VariableNode brother = splitNode.getBrother();
        splitNode.shouldHandleOppositeValue = false;
        MyStack newStack = new MyStack();
        newStack.push(brother);

        int oldSplitIndex = splitIndex;
        splitIndex = -1;
        for (int i = oldSplitIndex ; i < stack.size() - 1; i++) {
            if (stack.elementAt(i).hasBrother()) {
                splitIndex = i;
                break;
            }
        }
        if (splitIndex == -1 && stack.elementAt(stack.size() -1).hasBrother())
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

