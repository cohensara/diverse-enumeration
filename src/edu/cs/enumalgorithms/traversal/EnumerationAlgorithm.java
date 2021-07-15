package edu.cs.enumalgorithms.traversal;

public interface EnumerationAlgorithm<T> {

    boolean finishedExecution();

    T getNext();

    String getName();

    String getStatistics();

    String getStatisticsHeader();
}
