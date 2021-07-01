package edu.cs.maximalcliques.traversal;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;
import java.util.Stack;

public class ExtendRandom extends StackExtendStrategy {

    Random random = new Random(System.currentTimeMillis());

    @Override
    public boolean addSingleStack(LinkedList<Stack<BKNode>> allStacks,
                                  ListIterator<Stack<BKNode>> stackIterator) {
        BKNode toExpand;

        do {
            int stackSize = 0;
            Stack<BKNode> stack = null;
            while (stackSize == 0) {
                int chooseStack = random.nextInt(allStacks.size());
                stack = allStacks.get(chooseStack);
                stackSize = stack.size();
            }
            int chooseNode = random.nextInt(stackSize);
            toExpand = stack.get(chooseNode);
        } while (!toExpand.hasMoreChildren());

        BKNode child = toExpand.getNextChild();
        Stack<BKNode> stack = new Stack<>();
        stack.push(child);
        stackIterator.add(stack);
        stackIterator.previous();
        return true;
    }

    @Override
    public String getName() {
        return "random";
    }
}
