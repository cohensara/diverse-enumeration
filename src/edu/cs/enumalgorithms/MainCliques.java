package edu.cs.enumalgorithms;

import edu.cs.enumalgorithms.diversity.*;
import edu.cs.enumalgorithms.diversity.cliques.*;
import edu.cs.enumalgorithms.traversal.BronKerboschNode;
import edu.cs.enumalgorithms.traversal.EnumerationAlgorithm;
import edu.cs.enumalgorithms.traversal.EnumerationAlgorithmFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class MainCliques {

    static MeasurementRegistry<HashSet<Integer>> measurementRegistry = new MeasurementRegistry<>();


    public static void runDiversityAllFilesTests() throws IOException {
        String[] filenames = {"soc-Epinions1", "Amazon0505", "dblp-2008", "youtube"};
        String[] filenameShorts = {"epinions", "amazon", "dblp", "youtube"};
        long[] numberOfCliques = {1775065, 1357215, 843600, 3265956};


        Graph[] graphs = new Graph[filenames.length];
        for (int f = 0; f < filenames.length; f++) {

            String filename = filenames[f];
            graphs[f] = new Graph("data/" + filename + ".txt", Graph.Type.EDGE_LIST);
        }
        int window_size = 10;
        int instance_bound = 1000;
        WindowMeasure[] measures = new WindowMeasure[]{
                new CoverageWindowMeasure(window_size),
                new AvgAllJWindowMeasure(window_size),
                new AvgMaxJWindowMeasure(window_size)};

        String logFilename = "results/DIVERSITY_ALL_FILES_RESULTS.txt";

        Logger log = new Logger(logFilename);
        ArrayList<EnumerationAlgorithmFactory.Type> traversalTypes = new ArrayList<>();

        traversalTypes.add(EnumerationAlgorithmFactory.Type.CLASSIC);
        traversalTypes.add(EnumerationAlgorithmFactory.Type.DIVERSEC);
        traversalTypes.add(EnumerationAlgorithmFactory.Type.DIVERSER);

        for (WindowMeasure measure : measures) {
            System.out.println("% " + measure);
            for (int i = 0; i < traversalTypes.size(); i++) {
                System.out.println("% " + traversalTypes.get(i));
                measurementRegistry.removeAllMeasures();
                measurementRegistry.registerWindowMeasure(measure);
                for (int g = 0; g < graphs.length; g++) {
                    System.out.println("% " + filenameShorts[g]);
                    EnumerationAlgorithm<HashSet<Integer>> traversal = EnumerationAlgorithmFactory.getMaximalCliquesTraversal(traversalTypes.get(i), graphs[g], instance_bound);
                    HashSet<Integer> clique = traversal.getNext();
                    int numCliques = 0;
                    while (clique != null && numCliques < numberOfCliques[g]) {
                        numCliques++;
                        measurementRegistry.addItem(clique);
                        clique = traversal.getNext();
                        if (numCliques == 10 || numCliques == 100 || numCliques == 1000) {
                            log.logPair(numCliques + "", measurementRegistry.getAllMeasurements());
                            if (numCliques == 1000) break;
                        }
                    }
                    //log.logPair(filenameShorts[g], measurementRegistry.getAllMeasurements());
                    System.out.println();
                    measurementRegistry.reset();
                }
                log.write("\n");
                log.flush();
            }
        }
    }


    public static void runtimeAllFilesTests() throws IOException {
        String[] filenames = {"soc-Epinions1", "Amazon0505", "dblp-2008", "youtube"};
        String[] filenameShorts = {"epinions", "amazon", "dblp", "youtube"};
        long[] numberOfCliques = {1775065, 1357215, 843600, 3265956};

        Graph[] graphs = new Graph[filenames.length];
        for (int f = 0; f < filenames.length; f++) {

            String filename = filenames[f];
            graphs[f] = new Graph("data/" + filename + ".txt", Graph.Type.EDGE_LIST);
        }
        int instance_bound = 1000;

        String logFilename = "results/runtimeTests.csv";

        Logger log = new Logger(logFilename);
        ArrayList<EnumerationAlgorithmFactory.Type> traversalTypes = new ArrayList<>();

        traversalTypes.add(EnumerationAlgorithmFactory.Type.CLASSIC);
        traversalTypes.add(EnumerationAlgorithmFactory.Type.DIVERSEC);
        traversalTypes.add(EnumerationAlgorithmFactory.Type.DIVERSER);

        for (int i = 0; i < traversalTypes.size(); i++) {
            System.out.println(traversalTypes.get(i));
            for (int g = 0; g < graphs.length; g++) {

                EnumerationAlgorithm<HashSet<Integer>> traversal = EnumerationAlgorithmFactory.getMaximalCliquesTraversal(traversalTypes.get(i), graphs[g], instance_bound);
                log.startTimer();
                HashSet<Integer> clique = traversal.getNext();
                int numCliques = 0;
                while (clique != null && numCliques < numberOfCliques[g]) {
                    numCliques++;
                    clique = traversal.getNext();
                }
                log.logElapsedTime(filenameShorts[g]);
            }
            log.write("\n");
            log.flush();
        }
    }

    public static void runtimeIncrementsAllFilesTests() throws IOException {
        String[] filenames = {"soc-Epinions1", "Amazon0505", "dblp-2008", "youtube"};
        String[] filenameShorts = {"epinions", "amazon", "dblp", "youtube"};
        long[] numberOfCliques = {1775065, 1357215, 843600, 3265956};

        Graph[] graphs = new Graph[filenames.length];
        for (int f = 0; f < filenames.length; f++) {

            String filename = filenames[f];
            graphs[f] = new Graph("data/" + filename + ".txt", Graph.Type.EDGE_LIST);
        }
       int instance_bound = 1000;

        String logFilename = "results/runtimeIncrementsTests.txt";

        Logger log = new Logger(logFilename);
        ArrayList<EnumerationAlgorithmFactory.Type> traversalTypes = new ArrayList<>();

        traversalTypes.add(EnumerationAlgorithmFactory.Type.CLASSIC);
        traversalTypes.add(EnumerationAlgorithmFactory.Type.DIVERSEC);
        traversalTypes.add(EnumerationAlgorithmFactory.Type.DIVERSER);


        for (int i = 0; i < traversalTypes.size(); i++) {
            System.out.println(traversalTypes.get(i));
            for (int g = 0; g < graphs.length; g++) {
                System.out.println("% " + filenameShorts[g]);
                EnumerationAlgorithm<HashSet<Integer>> traversal = EnumerationAlgorithmFactory.getMaximalCliquesTraversal(traversalTypes.get(i), graphs[g], instance_bound);
                long increment = numberOfCliques[g]/10;
                log.startTimer();
                HashSet<Integer> clique = traversal.getNext();
                int numCliques = 0;
                while (clique != null && numCliques < numberOfCliques[g]) {
                    numCliques++;
                    if (numCliques % increment == 0) {

                        log.logElapsedTime((numCliques / increment) / 10F  + "");
                        log.startTimer();
                    }
                    clique = traversal.getNext();
                }
                System.out.println("\n");
            }
            System.out.println("\n");
            log.write("\n");
            log.flush();
        }
    }

    public static void memoryAllFilesTests() throws IOException {
        String[] filenames = {"soc-Epinions1", "Amazon0505", "dblp-2008", "youtube"};
        String[] filenameShorts = {"epinions", "amazon", "dblp", "youtube"};
        long[] numberOfCliques = {1775065, 1357215, 843600, 3265956};

        Graph[] graphs = new Graph[filenames.length];
        int i = 0;
        int f = 2;


        String filename = filenames[f];
        graphs[f] = new Graph("data/" + filename + ".txt", Graph.Type.EDGE_LIST);

        System.out.println("% " + filenameShorts[f]);

        int instance_bound = 1000;

        String logFilename = "results/runtimeTests.csv";

        Logger log = new Logger(logFilename);
        ArrayList<EnumerationAlgorithmFactory.Type> traversalTypes = new ArrayList<>();

        traversalTypes.add(EnumerationAlgorithmFactory.Type.CLASSIC);
        traversalTypes.add(EnumerationAlgorithmFactory.Type.DIVERSEC);
        traversalTypes.add(EnumerationAlgorithmFactory.Type.DIVERSER);

        System.out.println("% " + traversalTypes.get(i));

        int g = f;

        EnumerationAlgorithm<HashSet<Integer>> traversal = EnumerationAlgorithmFactory.getMaximalCliquesTraversal(traversalTypes.get(i), graphs[g], instance_bound);
        measurementRegistry.registerGlobalMeasure(new MemoryUsedMeasure());
        HashSet<Integer> clique = traversal.getNext();
        int numCliques = 0;
        while (clique != null && numCliques < numberOfCliques[g]) {
            numCliques++;
            measurementRegistry.addItem(clique);
            clique = traversal.getNext();
        }
        System.out.println(measurementRegistry.getAllMeasurements());
        //log.write("\n");
        //log.flush();
    }


    public static void runDiversityAsFunctionOfInstancesTests() throws IOException {

        String[] filenames = {"Amazon0505", "dblp-2008", "youtube", "soc-Epinions1"};
        String[] filenameShorts = {"amazon", "dblp", "youtube", "epinions"};

        Graph[] graphs = new Graph[filenames.length];
        for (int f = 0; f < filenames.length; f++) {

            String filename = filenames[f];
            graphs[f] = new Graph("data/" + filename + ".txt", Graph.Type.EDGE_LIST);
        }
        int windowSize = 10;
        int instanceBound = 15; //, 1000};

        String logFilename = "results/diversityInstanceBoundsCoverageTests.csv";

        Logger log = new Logger(logFilename);
        ArrayList<EnumerationAlgorithmFactory.Type> traversalTypes = new ArrayList<>();

        //traversalTypes.add(BKTraversalFactory.Type.CLASSIC);
        //traversalTypes.add(BKTraversalFactory.Type.DIVERSEC);
        traversalTypes.add(EnumerationAlgorithmFactory.Type.DIVERSER);

        log.write("% coverage ");
        measurementRegistry.registerWindowMeasure(new AvgAllJWindowMeasure(windowSize));
        for (int g = 0; g < graphs.length; g++) {
            log.write("% filename: " + filenameShorts[g]);
            for (int i = 0; i < traversalTypes.size(); i++) {
                log.write("% traversal type: " + traversalTypes.get(i));
                //for (int j = 0 ; j < instanceBounds.length ; j++) {

                    EnumerationAlgorithm<HashSet<Integer>> traversal = EnumerationAlgorithmFactory.getMaximalCliquesTraversal(traversalTypes.get(i), graphs[g], instanceBound);
                    HashSet<Integer> clique = traversal.getNext();
                    while (clique != null) {
                        measurementRegistry.addItem(clique);
                        clique = traversal.getNext();
                    }
                    log.logPair(instanceBound + "", measurementRegistry.getAllMeasurements());

                //}
                measurementRegistry.reset();
                System.out.println();
            }
            log.write("\n");
            log.flush();
        }
    }



    public static void runDiversityAsFunctionOfWindowSizeTests() throws IOException {

        String[] filenames = { "dblp-2008", "Amazon0505", "soc-Epinions1", "youtube"};
        String[] filenameShorts = { "dblp", "amazon", "epinions", "youtube"};

        long[] numberOfCliques = {  843600, 1357215, 1775065, 3265956};

        Graph[] graphs = new Graph[filenames.length];
        for (int f = 0; f < filenames.length; f++) {

            String filename = filenames[f];
            graphs[f] = new Graph("data/" + filename + ".txt", Graph.Type.EDGE_LIST);
        }
        int[] window_sizes = new int[]{50, 100, 150}; //, 1000};
        int instance_bound = 1000;
        WindowMeasure[] measures = new WindowMeasure[window_sizes.length];
        for (int i = 0 ; i < window_sizes.length ; i++) {
            measures[i] = new AvgMaxJWindowMeasure(window_sizes[i]);
            //measures[i] = new CoverageWindowMeasure(window_sizes[i]);
        }

        String logFilename = "results/ThisIsTheFile-SMALL-WINDOWS.txt";

        Logger log = new Logger(logFilename);
        ArrayList<EnumerationAlgorithmFactory.Type> traversalTypes = new ArrayList<>();

        //traversalTypes.add(BKTraversalFactory.Type.CLASSIC);
        traversalTypes.add(EnumerationAlgorithmFactory.Type.DIVERSER);

        log.write("% coverage");
        for (int g = 0; g < graphs.length; g++) {
            log.write("% filename: " + filenameShorts[g]);
            for (int i = 0; i < traversalTypes.size(); i++) {
                log.write("% traversal type: " + traversalTypes.get(i));
                measurementRegistry.removeAllMeasures();
                for (int w = 0 ; w < measures.length ; w++) {
                    //log.write("% window size: " + window_sizes[w]);
                    measurementRegistry.registerWindowMeasure(measures[w]);
                }
                EnumerationAlgorithm<HashSet<Integer>> traversal = EnumerationAlgorithmFactory.getMaximalCliquesTraversal(traversalTypes.get(i), graphs[g], instance_bound);
                HashSet<Integer> clique = traversal.getNext();
                int numCliques = 0;
                while (clique != null && numCliques < numberOfCliques[g]) {
                    numCliques++;
                    if (numCliques % 50000 == 0) System.out.println(numCliques);
                    measurementRegistry.addItem(clique);
                    clique = traversal.getNext();
                }
                log.write(measurementRegistry.getAllMeasurements());
                measurementRegistry.reset();
            }
            log.write("\n");
            log.flush();
        }
    }

    public static void runDiversityAsFunctionOfWindowSizeTests2() throws IOException {

        String[] filenames = { "youtube"};
        String[] filenameShorts = {"youtube"};

        long[] numberOfCliques = { 3265956};

        //String[] filenames = {"dblp-2008"};
        //String[] filenameShorts = {"dblp"};

        //long[] numberOfCliques = {843600};

        Graph[] graphs = new Graph[filenames.length];
        for (int f = 0; f < filenames.length; f++) {

            String filename = filenames[f];
            graphs[f] = new Graph("data/" + filename + ".txt", Graph.Type.EDGE_LIST);
        }
        int[] window_sizes = new int[]{50, 100, 150}; //, 1000};
        int instance_bound = 1000;
        WindowMeasure[] measures = new WindowMeasure[window_sizes.length];
        for (int i = 0 ; i < window_sizes.length ; i++) {
            measures[i] = new AvgMaxJWindowMeasure(window_sizes[i]);
            //measures[i] = new CoverageWindowMeasure(window_sizes[i]);
        }

        String logFilename = "results/ThisIsTheFile2-SMALL-WINDOWS.txt";

        Logger log = new Logger(logFilename);
        ArrayList<EnumerationAlgorithmFactory.Type> traversalTypes = new ArrayList<>();

        traversalTypes.add(EnumerationAlgorithmFactory.Type.CLASSIC);
        //traversalTypes.add(BKTraversalFactory.Type.DIVERSER);

        log.write("% coverage");
        for (int g = 0; g < graphs.length; g++) {
            log.write("% filename: " + filenameShorts[g]);
            for (int i = 0; i < traversalTypes.size(); i++) {
                log.write("% traversal type: " + traversalTypes.get(i));
                measurementRegistry.removeAllMeasures();
                for (int w = 0 ; w < measures.length ; w++) {
                    //log.write("% window size: " + window_sizes[w]);
                    measurementRegistry.registerWindowMeasure(measures[w]);
                }
                EnumerationAlgorithm<HashSet<Integer>> traversal = EnumerationAlgorithmFactory.getMaximalCliquesTraversal(traversalTypes.get(i), graphs[g], instance_bound);
                HashSet<Integer> clique = traversal.getNext();
                int numCliques = 0;
                while (clique != null && numCliques < numberOfCliques[g]) {
                    numCliques++;
                    if (numCliques % 50000 == 0) System.out.println(numCliques);
                    measurementRegistry.addItem(clique);
                    clique = traversal.getNext();
                }
                log.write(measurementRegistry.getAllMeasurements());
                measurementRegistry.reset();
            }
            log.write("\n");
            log.flush();
        }
    }

    public static void runPercentageCoveredTests() throws IOException {

        String[] filenames = {"soc-Epinions1", "Amazon0505", "dblp-2008", "youtube"};
        String[] filenameShorts = {"epinions", "amazon", "dblp", "youtube"};

        long[] numberOfCliques = {1775065, 1357215, 843600, 3265956};

        Graph[] graphs = new Graph[filenames.length];
        for (int f = 0; f < filenames.length; f++) {

            String filename = filenames[f];
            graphs[f] = new Graph("data/" + filename + ".txt", Graph.Type.EDGE_LIST);
        }
        float[] percentages = new float[]{0.25F, 0.5F, 0.75F, 1};
        int instance_bound = 1000;
        Measure[] measures = new Measure[percentages.length];

        String logFilename = "results/diversityPercentageCovered.txt";

        Logger log = new Logger(logFilename);
        ArrayList<EnumerationAlgorithmFactory.Type> traversalTypes = new ArrayList<>();

        traversalTypes.add(EnumerationAlgorithmFactory.Type.CLASSIC);
        traversalTypes.add(EnumerationAlgorithmFactory.Type.DIVERSER);

        log.write("% percentage covered");
        for (int g = 0; g < graphs.length; g++) {
            log.write("% filename: " + filenameShorts[g]);
            measurementRegistry.removeAllMeasures();
            for (int i = 0 ; i < percentages.length ; i++) {
                measures[i] = new PercentageCoveredMeasure(graphs[g].getNumNodes(), numberOfCliques[g], percentages[i]);
                measurementRegistry.registerGlobalMeasure(measures[i]);
            }
            for (int i = 0; i < traversalTypes.size(); i++) {
                log.write("% traversal type: " + traversalTypes.get(i));
                measurementRegistry.reset();
                EnumerationAlgorithm<HashSet<Integer>> traversal = EnumerationAlgorithmFactory.getMaximalCliquesTraversal(traversalTypes.get(i), graphs[g], instance_bound);
                HashSet<Integer> clique = traversal.getNext();
                int numCliques = 0;
                while (clique != null && numCliques < numberOfCliques[g]) {
                    numCliques++;
                    //if (numCliques % 50000 == 0) System.out.println(numCliques);
                    measurementRegistry.addItem(clique);
                    clique = traversal.getNext();
                }
                log.write(measurementRegistry.getAllMeasurements());
            }
            log.write("\n");
            log.flush();
        }
    }

    private static void sanityCheckForCliques() throws IOException {

        String[] filenames = {"small_example_graph", "Amazon0505", "dblp-2008", "youtube", "soc-Epinions1"};
        String[] filenameShorts = {"small", "amazon", "dblp", "youtube", "epinions"};

        long[] numberOfCliques = {6, 1357215, 843600, 3265956, 1775065};


        Graph[] graphs = new Graph[filenames.length];
        for (int f = 0; f < filenames.length; f++) {

            String filename = filenames[f];
            graphs[f] = new Graph("data/" + filename + ".txt", Graph.Type.EDGE_LIST);
        }
        int instanceBound = 15; //, 1000};
        //WindowMeasure measure = new CoverageWindowMeasure(windowSize);
        ArrayList<EnumerationAlgorithmFactory.Type> traversalTypes = new ArrayList<>();

        traversalTypes.add(EnumerationAlgorithmFactory.Type.CLASSIC);
        traversalTypes.add(EnumerationAlgorithmFactory.Type.DIVERSEC);
        traversalTypes.add(EnumerationAlgorithmFactory.Type.DIVERSER);


        for (int g = 2; g < graphs.length; g++) {
            System.out.println("% filename: " + filenameShorts[g]);
            for (int i = 0; i < traversalTypes.size(); i++) {
                System.out.println("% traversal type: " + traversalTypes.get(i));

                EnumerationAlgorithm<HashSet<Integer>> traversal = EnumerationAlgorithmFactory.getMaximalCliquesTraversal(traversalTypes.get(i), graphs[g], instanceBound);
                HashSet<Integer> clique = traversal.getNext();
                int n = 0;
                while (clique != null) {
                    n++;
                    clique = traversal.getNext();
                }
                if (n == numberOfCliques[g]) {
                    System.out.println("Correct!");
                }
                else System.out.println("Incorrect! Expected " + numberOfCliques[g] + " Got " + n);
                System.out.println();
            }
        }
    }

    private static void sanityCheckForAssignments() throws IOException {

        String[] filenames = {"10dnf-20vars-100clauses", "5dnf-20vars-100clauses", "10dnf-30vars-10clauses"};
        String[] filenameShorts = {"100K", "1M", "10M"};
        int[] numberOfVariables = {20, 20, 30};
        long[] numberOfSatisfyingAssignments = {98278, 1007400, 10426496};


        Formula[] formulas = new Formula[filenames.length];
        for (int f = 0; f < filenames.length; f++) {

            String filename = filenames[f];
            BufferedReader reader = new BufferedReader(new FileReader("data/" + filename + ".txt"));
            formulas[f] = new DnfFormula(reader.readLine(), numberOfVariables[f]);
        }

        int instanceBound = 15; //, 1000};
        //WindowMeasure measure = new CoverageWindowMeasure(windowSize);
        ArrayList<EnumerationAlgorithmFactory.Type> traversalTypes = new ArrayList<>();

        traversalTypes.add(EnumerationAlgorithmFactory.Type.CLASSIC);
        traversalTypes.add(EnumerationAlgorithmFactory.Type.STACK_EXTEND_LOWEST);
        traversalTypes.add(EnumerationAlgorithmFactory.Type.STACK_EXTEND_RAND);


        for (int g = 0; g < formulas.length; g++) {
            System.out.println("% filename: " + filenameShorts[g]);
            for (int i = 0; i < traversalTypes.size(); i++) {
                System.out.println("% traversal type: " + traversalTypes.get(i));

                EnumerationAlgorithm<HashMap<Integer, Boolean>> traversal =
                        EnumerationAlgorithmFactory.getSatisfyingAssignmentsTraversal(traversalTypes.get(i), formulas[g], instanceBound);
                HashMap<Integer, Boolean> assignment = traversal.getNext();
                int n = 0;
                while (assignment != null) {
                    n++;
                    assignment = traversal.getNext();
                }
                if (n == numberOfSatisfyingAssignments[g]) {
                    System.out.println("Correct!");
                }
                else System.out.println("Incorrect! Expected " + numberOfSatisfyingAssignments[g] + " Got " + n);
                System.out.println();
            }
        }
    }

    public static void main(String[] args) throws IOException {

        //memoryAllFilesTests();
        //runtimeAllFilesTests();
        //runPercentageCoveredTests();
        //runDiversityAllFilesTests();
        //runDiversityAsFunctionOfWindowSizeTests();
        //runDiversityAsFunctionOfWindowSizeTests2();
        //runtimeIncrementsAllFilesTests();
        //runDiversityAsFunctionOfInstancesTests();
    }

}
