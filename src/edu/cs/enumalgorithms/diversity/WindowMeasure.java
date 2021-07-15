package edu.cs.enumalgorithms.diversity;

public interface WindowMeasure<T> {

    int getWindowSize();

    void addItem(Window<T> window, T removed, T added);

    String getDiversityValue();

    void reset();

    String getHeader();
}
