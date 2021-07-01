package edu.cs.satassignments;

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

    public void startTimer() {
        startTime = System.nanoTime();
    }

    public void logElapsedTime(String s) throws IOException {
        long endTime = System.nanoTime();
        long elapsedTime = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        log.write(s + ", " + elapsedTime + "\n");
        System.out.println(s + ", " + elapsedTime);
    }

    public long getElapsedTime() throws IOException {
        long endTime = System.nanoTime();
        return TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
    }
    
    public void logPair(String first, String second) throws IOException {
    	log.write("(" + first + "," + second + ")\n");
        System.out.println("(" + first + "," + second + ")");
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
