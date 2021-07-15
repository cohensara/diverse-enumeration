package edu.cs.enumalgorithms.traversal;

import edu.cs.enumalgorithms.Graph;
import edu.cs.enumalgorithms.Formula;

import java.util.HashMap;
import java.util.HashSet;

public class EnumerationAlgorithmFactory {

    public enum Type {
        CLASSIC,
        DIVERSEC,
        DIVERSER,
        STACK_EXTEND_LOWEST,
        STACK_EXTEND_RAND
    }

    public static EnumerationAlgorithm<HashSet<Integer>> getMaximalCliquesTraversal(Type type, Graph graph, int instanceBound) {

        switch (type) {
            case CLASSIC: {
                EnumerationNode.reset();
                EnumerationNode.setPivot(EnumerationNode.PivotType.LOWEST_ID);
                return new ClassicEnum(new BronKerboschNode(graph));
            }
            case DIVERSEC: {
                EnumerationNode.reset();
                EnumerationNode.setPivot(EnumerationNode.PivotType.LOWEST_ID);
                return new DiverseEnum(new BronKerboschNode(graph), instanceBound);
            }
            case DIVERSER: {
                EnumerationNode.reset();
                EnumerationNode.setPivot(EnumerationNode.PivotType.ARBITRARY);
                return new DiverseEnum(new BronKerboschNode(graph), instanceBound);
            }
            case STACK_EXTEND_LOWEST: return new DiverseEnum(new BronKerboschNode(graph), instanceBound);
        }

        return null;
    }

    public static EnumerationAlgorithm<HashMap<Integer, Boolean>>  getSatisfyingAssignmentsTraversal(Type type, Formula formula, int instanceBound) {

        switch (type) {
            case CLASSIC:
                EnumerationNode.reset();
                EnumerationNode.setPivot(EnumerationNode.PivotType.LOWEST_ID);
                return new ClassicEnum(new SatisfyingAssignmentNode(formula));
            case STACK_EXTEND_LOWEST:
                EnumerationNode.reset();
                EnumerationNode.setPivot(EnumerationNode.PivotType.LOWEST_ID);
                return new DiverseEnum(new SatisfyingAssignmentNode(formula), instanceBound);
            case STACK_EXTEND_RAND:
                EnumerationNode.reset();
                EnumerationNode.setPivot(EnumerationNode.PivotType.ARBITRARY);
                return new DiverseEnum(new SatisfyingAssignmentNode(formula), instanceBound);
        }
        return null;
    }
}
