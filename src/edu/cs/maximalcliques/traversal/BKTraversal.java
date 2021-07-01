package edu.cs.maximalcliques.traversal;

import java.util.HashSet;

public interface BKTraversal {

    boolean finishedExecution();

    HashSet<Integer> getNext();

    String getName();

    String getStatistics();

    String getStatisticsHeader();
}
