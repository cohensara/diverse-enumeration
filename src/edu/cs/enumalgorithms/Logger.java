package edu.cs.enumalgorithms;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Logger {

    BufferedWriter log;
    long startTime;
    long pauseStartTime;


    public Logger(String filename) throws IOException {
        log = new BufferedWriter(new FileWriter(filename));
    }

    public long startTimer() {
        startTime = System.nanoTime();
        return startTime;
    }

    public void logElapsedTime(String s) throws IOException {
        long endTime = System.nanoTime();
        long elapsedTime = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        log.write("(" + s + ", " + elapsedTime + ")");
        System.out.print("(" + s + ", " + elapsedTime + ")");
    }

    public void logPair(String s1, String s2) throws IOException {
        log.write("(" + s1 + "," + s2 + ")");
        System.out.print("(" + s1 + "," + s2 + ")");
    }

    public void write(String s) throws IOException {
        log.write(s + "\n");
        System.out.println(s);
    }

    public  void close() throws IOException {
        log.close();
    }

    public void flush() throws IOException {
        log.flush();
    }

    public void pauseTimer() {

        pauseStartTime = System.nanoTime();
    }

    public void resumeTimer() {
        long pauseEndTime = System.nanoTime();
        startTime += pauseEndTime - pauseStartTime;
    }
}
