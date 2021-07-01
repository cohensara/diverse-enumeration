package edu.cs.maximalcliques.traversal;

import edu.cs.maximalcliques.Graph;

public class BKTraversalFactory {

    public enum Type {
        CLASSIC,
        DIVERSEC,
        DIVERSER,
        STACK_EXTEND_LOWEST,
    }

    public static BKTraversal getTraversal(Type type, Graph graph, int instanceBound) {

        switch (type) {
            case CLASSIC: {
                BKNode.reset();
                BKNode.setPivot(BKNode.PivotType.LOWEST_ID);
                return new BKTraversalClassic(graph);
            }
            case DIVERSEC: {
                BKNode.reset();
                BKNode.setPivot(BKNode.PivotType.LOWEST_ID);
                return new BKTraversalMulti(graph, instanceBound);
            }
            case DIVERSER: {
                BKNode.reset();
                BKNode.setPivot(BKNode.PivotType.ARBITRARY);
                return new BKTraversalMulti(graph, instanceBound);
            }
            case STACK_EXTEND_LOWEST: return new BKTraversalMulti(graph, instanceBound);
        }

        return null;
    }
}
