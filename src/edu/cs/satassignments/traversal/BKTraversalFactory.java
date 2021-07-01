package edu.cs.satassignments.traversal;

import edu.cs.satassignments.Formula;
import edu.cs.satassignments.traversal.VariableNode.PivotType;

public class BKTraversalFactory {

    public enum Type {
        CLASSIC,
        STACK_EXTEND_LOWEST,
        STACK_EXTEND_RAND
    }

    public static BKTraversal getTraversal(Type type, Formula formula, int instanceBound) {

		switch (type) {
		case CLASSIC:
			VariableNode.setPivot(PivotType.LOWEST_ID);
			return new BKTraversalClassic(formula);
		case STACK_EXTEND_LOWEST:
			VariableNode.setPivot(PivotType.LOWEST_ID);
			return new BKTraversalMulti(formula, instanceBound);
		case STACK_EXTEND_RAND:
			VariableNode.setPivot(PivotType.ARBITRARY);
			return new BKTraversalMulti(formula, instanceBound);
		}

        return null;
    }
}
