package edu.cs.satassignments.traversal;

import java.util.HashMap;
import java.util.HashSet;

public interface BKTraversal {

    boolean finishedExecution();

    HashMap<Integer, Boolean> getNext();

    String getName();

    String getStatistics();

    String getStatisticsHeader();
}
