package edu.cs.enumalgorithms.diversity;

public interface Measure<T> {


    void addItem(T item);

    String getDiversityValue();

    void reset();

    String getHeader();
}
