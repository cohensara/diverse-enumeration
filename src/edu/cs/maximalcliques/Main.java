package edu.cs.maximalcliques;

import edu.cs.maximalcliques.diversity.*;
import edu.cs.maximalcliques.traversal.BKNode;
import edu.cs.maximalcliques.traversal.BKTraversal;
import edu.cs.maximalcliques.traversal.BKTraversalFactory;
import edu.cs.maximalcliques.traversal.PriorityRoundRobin;
import edu.xxx.cs.cs.diversity.*;
import edu.xxx.cs.cs.traversal.*;
import edu.xxx.cs.diversity.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    static boolean printClique = true;

    private static void logKCliques(BKTraversal traversal, int k, Logger log,
                                    boolean logDiversity, boolean logTime, int increment) throws IOException {

        int i = 0;
        log.write(traversal.getName());
        if (logTime) {
            log.startTimer();
        }

        HashSet<Integer> clique = traversal.getNext();
        log.write("clique#, BKNodes created#, " + traversal.getStatisticsHeader() + ", time");
        while (clique != null && i < k) {
            i++;
            if (printClique) System.out.println(clique);
            if (logTime && i % increment == 0) {
                log.logElapsedTime("" + i + ", " + BKNode.getHowManyCreated() + ", " + traversal.getStatistics());
                //log.write(MeasurementRegistry.getAllMeasurementsHeaders());
                //log.write(MeasurementRegistry.getAllMeasurements());
            }
            if (logDiversity) {
                log.pauseTimer();
                MeasurementRegistry.addClique(clique);
                log.resumeTimer();
            }
            clique = traversal.getNext();
        }

        log.logElapsedTime("" + i + ", " + BKNode.getHowManyCreated() + ", " + traversal.getStatistics());
        log.write(traversal.getStatisticsHeader());
        log.write(traversal.getStatistics());
        if (logDiversity) {
            log.write(MeasurementRegistry.getAllMeasurementsHeaders());
            log.write(MeasurementRegistry.getAllMeasurements());
        }
        log.flush();
    }


    public static void writeIndependentSetOrder(String input, String output) throws IOException {
        System.out.println("Writing " + input + " to " + output + "...");
        Graph graph = new Graph(input, Graph.Type.EDGE_LIST);
        System.out.println("#nodes = " + graph.getNumNodes() + " #edges = " + graph.getNumEdges());
        graph.writeIndependentSetOrder(output);
        Graph ioGraph = new Graph(output, Graph.Type.EDGE_LIST);
        System.out.println("#nodes = " + ioGraph.getNumNodes() + " #edges = " + ioGraph.getNumEdges());
        //System.out.println(graph + "\n" + ioGraph);
    }

    /*
    public static void writeRandomOrder(String input, String output) throws IOException {
        System.out.println("Writing " + input + " to " + output + "...");
        Graph graph = new Graph(input, Graph.Type.EDGE_LIST);
        System.out.println("#nodes = " + graph.getNumNodes() + " #edges = " + graph.getNumEdges());
        graph.writeRandomOrder(output);
        Graph randomOrderGraph = new Graph(output, Graph.Type.EDGE_LIST);
        System.out.println("#nodes = " + randomOrderGraph.getNumNodes() + " #edges = " + randomOrderGraph.getNumEdges());
        //System.out.println(graph + "\n" + ioGraph);
    }

    public static void writeDegreeOrder(String input, String output) throws IOException {
        System.out.println("Writing " + input + " to " + output + "...");
        Graph graph = new Graph(input, Graph.Type.EDGE_LIST);
        System.out.println("#nodes = " + graph.getNumNodes() + " #edges = " + graph.getNumEdges());
        graph.writeDegreeOrder(output);
        Graph degeneracyGraph = new Graph(output, Graph.Type.EDGE_LIST);
        System.out.println("#nodes = " + degeneracyGraph.getNumNodes() + " #edges = " + degeneracyGraph.getNumEdges());
        //System.out.println(graph + "\n" + ioGraph);
    } */

    public static void writeDegeneracyOrder(String input, String output) throws IOException {
        System.out.println("Writing " + input + " to " + output + "...");
        Graph graph = new Graph(input, Graph.Type.EDGE_LIST);
        //System.out.println(graph);
        System.out.println("#nodes = " + graph.getNumNodes() + " #edges = " + graph.getNumEdges());
        graph.writeDegeneracyOrder(output);
        Graph degeneracyGraph = new Graph(output, Graph.Type.EDGE_LIST);
        //System.out.println(degeneracyGraph);
        System.out.println("#nodes = " + degeneracyGraph.getNumNodes() + " #edges = " + degeneracyGraph.getNumEdges());

    }

    public static void runPrefixDiversityTests() throws IOException {

        String[] filenames = {"youtube"};
        //"Amazon0505_degeneracy", "dblp-2008_degeneracy", "eu_2005_degeneracy"}; //, "Amazon0505_degeneracy","dblp-2008", "dblp-2008_degeneracy"};
//                "facebook_combined", "facebook_combined_degeneracy", "eu_2005", "eu_2005_degeneracy"};

        String logFilename = "results/prefix_tests_avg_10000.csv";

        Logger log = new Logger(logFilename);
        ArrayList<BKTraversalFactory.Type> traversalTypes = new ArrayList<>();

        traversalTypes.add(BKTraversalFactory.Type.CLASSIC);
        traversalTypes.add(BKTraversalFactory.Type.STACK_EXTEND_LOWEST);

        int k = 100000;
        int increment = 100 / 20;
        int[] instance_bound = {2500, 5000, 7500};
        BKNode.PivotType[] pivotTypes = {BKNode.PivotType.ARBITRARY, BKNode.PivotType.LOWEST_ID};

        MeasurementRegistry.registerGlobalMeasure(new UnionPrefixMeasure(new int[]{20000, 40000, 60000, 80000, 100000}));
        //MeasurementRegistry.registerGlobalMeasure(new MaxJaccardPrefixMeasure((new int[]{2000, 4000, 6000, 8000, 10000}), "#.####"));

        //MeasurementRegistry.registerWindowMeasure(new WindowUnion(k));
        //MeasurementRegistry.registerWindowMeasure(new MaxJaccard(k));
        //MeasurementRegistry.registerWindowMeasure(new AvgJaccard(k));

        for (String filename : filenames) {

            Graph graph = new Graph("data/" + filename + ".txt", Graph.Type.EDGE_LIST);
            log.write(filename + ", " + graph.getNumNodes() + ", " + graph.getNumEdges());
            log.write("Algorithm, " + MeasurementRegistry.getAllMeasurementsHeaders());
            BKTraversal traversal = BKTraversalFactory.getTraversal(BKTraversalFactory.Type.CLASSIC, graph, 0);

            HashSet<Integer> clique = traversal.getNext();
            int i = 0;
            //log.write(MeasurementRegistry.getAllMeasurementsHeaders());
            while (clique != null && i < k) {
                i++;
                MeasurementRegistry.addClique(clique);
                clique = traversal.getNext();
            }
            log.write("Classic, " + MeasurementRegistry.getAllMeasurements());
            log.flush();
            MeasurementRegistry.reset();
            for (BKNode.PivotType ptype : pivotTypes) {
                for (int inst : instance_bound) {
                    traversal = BKTraversalFactory.getTraversal(BKTraversalFactory.Type.STACK_EXTEND_LOWEST, graph, inst);
                    BKNode.setPivot(ptype);
                    clique = traversal.getNext();
                    i = 0;
                    while (clique != null && i < k) {
                        i++;
                        MeasurementRegistry.addClique(clique);
                        clique = traversal.getNext();
                    }
                    log.write("ExtendLowest " + ptype + " instanceBound = " + inst + ", " + MeasurementRegistry.getAllMeasurements());
                    log.flush();
                    MeasurementRegistry.reset();
                }
            }
            log.write("\n");
        }
    }

    public static void runDiversityAllFilesTests() throws IOException {
        String[] filenames = {"soc-Epinions1", "Amazon0505", "dblp-2008", "youtube"};
        String[] filenameShorts = {"epinions", "amazon", "dblp", "youtube"};
        long[] numberOfCliques = {1775065, 1357215, 843600, 3265956};
        //tring[] filenames = {"soc-Epinions1", "youtube"};
        //String[] filenameShorts = {"epinions", "youtube"};
        //long[] numberOfCliques = {1775065, 3265956};


        Graph[] graphs = new Graph[filenames.length];
        for (int f = 0; f < filenames.length; f++) {

            String filename = filenames[f];
            graphs[f] = new Graph("data/" + filename + ".txt", Graph.Type.EDGE_LIST);
        }
        int window_size = 10;
        int instance_bound = 1000;
        int[] prefixes = new int[]{100, 1000, 1000};
        WindowMeasure[] measures = new WindowMeasure[]{
                new CoverageWindowMeasure(window_size),
                new AvgAllJWindowMeasure(window_size),
                new AvgMaxJWindowMeasure(window_size)};
        //WindowMeasure[] measures = new WindowMeasure[]{new SizeWindowMeasure(window_size)};


        String logFilename = "results/PREFIX_RESULTS.txt";

        Logger log = new Logger(logFilename);
        ArrayList<BKTraversalFactory.Type> traversalTypes = new ArrayList<>();

        traversalTypes.add(BKTraversalFactory.Type.CLASSIC);
        traversalTypes.add(BKTraversalFactory.Type.DIVERSEC);
        traversalTypes.add(BKTraversalFactory.Type.DIVERSER);

        for (WindowMeasure measure : measures) {
            System.out.println("% " + measure);
            for (int i = 0; i < traversalTypes.size(); i++) {
                System.out.println("% " + traversalTypes.get(i));
                MeasurementRegistry.removeAllMeasures();
                MeasurementRegistry.registerWindowMeasure(measure);
                for (int g = 0; g < graphs.length; g++) {
                    System.out.println("% " + filenameShorts[g]);
                    BKTraversal traversal = BKTraversalFactory.getTraversal(traversalTypes.get(i), graphs[g], instance_bound);
                    HashSet<Integer> clique = traversal.getNext();
                    int numCliques = 0;
                    while (clique != null && numCliques < numberOfCliques[g]) {
                        numCliques++;
                        MeasurementRegistry.addClique(clique);
                        clique = traversal.getNext();
                        if (numCliques == 10 || numCliques == 100 || numCliques == 1000) {
                            log.logPair(numCliques + "", MeasurementRegistry.getAllMeasurements());
                            if (numCliques == 1000) break;
                        }
                    }
                    //log.logPair(filenameShorts[g], MeasurementRegistry.getAllMeasurements());
                    System.out.println();
                    MeasurementRegistry.reset();
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
        int window_size = 10;
        int instance_bound = 1000;

        String logFilename = "results/runtimeTests.csv";

        Logger log = new Logger(logFilename);
        ArrayList<BKTraversalFactory.Type> traversalTypes = new ArrayList<>();

        traversalTypes.add(BKTraversalFactory.Type.CLASSIC);
        traversalTypes.add(BKTraversalFactory.Type.DIVERSEC);
        traversalTypes.add(BKTraversalFactory.Type.DIVERSER);


            //System.out.println("% " + filenameShorts[g]);
        for (int i = 0; i < traversalTypes.size(); i++) {
            System.out.println(traversalTypes.get(i));
            for (int g = 0; g < graphs.length; g++) {

                BKTraversal traversal = BKTraversalFactory.getTraversal(traversalTypes.get(i), graphs[g], instance_bound);
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
        int window_size = 10;
        int instance_bound = 1000;

        String logFilename = "results/runtimeIncrementsTests.txt";

        Logger log = new Logger(logFilename);
        ArrayList<BKTraversalFactory.Type> traversalTypes = new ArrayList<>();

        traversalTypes.add(BKTraversalFactory.Type.CLASSIC);
        traversalTypes.add(BKTraversalFactory.Type.DIVERSEC);
        traversalTypes.add(BKTraversalFactory.Type.DIVERSER);


        //System.out.println("% " + filenameShorts[g]);

        for (int i = 0; i < traversalTypes.size(); i++) {
            System.out.println(traversalTypes.get(i));
            for (int g = 0; g < graphs.length; g++) {
                System.out.println("% " + filenameShorts[g]);
                BKTraversal traversal = BKTraversalFactory.getTraversal(traversalTypes.get(i), graphs[g], instance_bound);
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
                //log.logElapsedTime(filenameShorts[g]);
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

        int window_size = 10;
        int instance_bound = 1000;

        String logFilename = "results/runtimeTests.csv";

        Logger log = new Logger(logFilename);
        ArrayList<BKTraversalFactory.Type> traversalTypes = new ArrayList<>();

        traversalTypes.add(BKTraversalFactory.Type.CLASSIC);
        traversalTypes.add(BKTraversalFactory.Type.DIVERSEC);
        traversalTypes.add(BKTraversalFactory.Type.DIVERSER);

        System.out.println("% " + traversalTypes.get(i));

        int g = f;

        BKTraversal traversal = BKTraversalFactory.getTraversal(traversalTypes.get(i), graphs[g], instance_bound);
        MeasurementRegistry.registerGlobalMeasure(new MemoryUsedMeasure());
        HashSet<Integer> clique = traversal.getNext();
        int numCliques = 0;
        while (clique != null && numCliques < numberOfCliques[g]) {
            numCliques++;
            MeasurementRegistry.addClique(clique);
            clique = traversal.getNext();
        }
        System.out.println(MeasurementRegistry.getAllMeasurements());
        //log.write("\n");
        //log.flush();
    }


    public static void runDiversityAsFunctionOfInstancesTests() throws IOException {

        String[] filenames = {"Amazon0505", "dblp-2008", "youtube", "soc-Epinions1"};
        String[] filenameShorts = {"amazon", "dblp", "youtube", "epinions"};

        long[] numberOfCliques = {1357215, 843600, 3265956, 1775065};


        Graph[] graphs = new Graph[filenames.length];
        for (int f = 0; f < filenames.length; f++) {

            String filename = filenames[f];
            graphs[f] = new Graph("data/" + filename + ".txt", Graph.Type.EDGE_LIST);
        }
        int windowSize = 10;
        int instanceBound = 15; //, 1000};
        //WindowMeasure measure = new CoverageWindowMeasure(windowSize);

        String logFilename = "results/diversityInstanceBoundsCoverageTests.csv";

        Logger log = new Logger(logFilename);
        ArrayList<BKTraversalFactory.Type> traversalTypes = new ArrayList<>();

        //traversalTypes.add(BKTraversalFactory.Type.CLASSIC);
        //traversalTypes.add(BKTraversalFactory.Type.DIVERSEC);
        traversalTypes.add(BKTraversalFactory.Type.DIVERSER);

        log.write("% coverage ");
        MeasurementRegistry.registerWindowMeasure(new AvgAllJWindowMeasure(windowSize));
        for (int g = 0; g < graphs.length; g++) {
            log.write("% filename: " + filenameShorts[g]);
            for (int i = 0; i < traversalTypes.size(); i++) {
                log.write("% traversal type: " + traversalTypes.get(i));
                //for (int j = 0 ; j < instanceBounds.length ; j++) {

                    BKTraversal traversal = BKTraversalFactory.getTraversal(traversalTypes.get(i), graphs[g], instanceBound);
                    HashSet<Integer> clique = traversal.getNext();
                    while (clique != null) {
                        MeasurementRegistry.addClique(clique);
                        clique = traversal.getNext();
                    }
                    log.logPair(instanceBound + "", MeasurementRegistry.getAllMeasurements());

                //}
                MeasurementRegistry.reset();
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

        String logFilename = "results/ThisIsTheFile-SMALL-WINDOWS.txt";

        Logger log = new Logger(logFilename);
        ArrayList<BKTraversalFactory.Type> traversalTypes = new ArrayList<>();

        //traversalTypes.add(BKTraversalFactory.Type.CLASSIC);
        traversalTypes.add(BKTraversalFactory.Type.DIVERSER);

        log.write("% coverage");
        for (int g = 0; g < graphs.length; g++) {
            log.write("% filename: " + filenameShorts[g]);
            for (int i = 0; i < traversalTypes.size(); i++) {
                log.write("% traversal type: " + traversalTypes.get(i));
                MeasurementRegistry.removeAllMeasures();
                for (int w = 0 ; w < measures.length ; w++) {
                    //log.write("% window size: " + window_sizes[w]);
                    MeasurementRegistry.registerWindowMeasure(measures[w]);
                }
                BKTraversal traversal = BKTraversalFactory.getTraversal(traversalTypes.get(i), graphs[g], instance_bound);
                HashSet<Integer> clique = traversal.getNext();
                int numCliques = 0;
                while (clique != null && numCliques < numberOfCliques[g]) {
                    numCliques++;
                    if (numCliques % 50000 == 0) System.out.println(numCliques);
                    MeasurementRegistry.addClique(clique);
                    clique = traversal.getNext();
                }
                log.write(MeasurementRegistry.getAllMeasurements());
                MeasurementRegistry.reset();
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
        ArrayList<BKTraversalFactory.Type> traversalTypes = new ArrayList<>();

        traversalTypes.add(BKTraversalFactory.Type.CLASSIC);
        //traversalTypes.add(BKTraversalFactory.Type.DIVERSER);

        log.write("% coverage");
        for (int g = 0; g < graphs.length; g++) {
            log.write("% filename: " + filenameShorts[g]);
            for (int i = 0; i < traversalTypes.size(); i++) {
                log.write("% traversal type: " + traversalTypes.get(i));
                MeasurementRegistry.removeAllMeasures();
                for (int w = 0 ; w < measures.length ; w++) {
                    //log.write("% window size: " + window_sizes[w]);
                    MeasurementRegistry.registerWindowMeasure(measures[w]);
                }
                BKTraversal traversal = BKTraversalFactory.getTraversal(traversalTypes.get(i), graphs[g], instance_bound);
                HashSet<Integer> clique = traversal.getNext();
                int numCliques = 0;
                while (clique != null && numCliques < numberOfCliques[g]) {
                    numCliques++;
                    if (numCliques % 50000 == 0) System.out.println(numCliques);
                    MeasurementRegistry.addClique(clique);
                    clique = traversal.getNext();
                }
                log.write(MeasurementRegistry.getAllMeasurements());
                MeasurementRegistry.reset();
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
        ArrayList<BKTraversalFactory.Type> traversalTypes = new ArrayList<>();

        traversalTypes.add(BKTraversalFactory.Type.CLASSIC);
        traversalTypes.add(BKTraversalFactory.Type.DIVERSER);

        log.write("% percentage covered");
        for (int g = 0; g < graphs.length; g++) {
            log.write("% filename: " + filenameShorts[g]);
            MeasurementRegistry.removeAllMeasures();
            for (int i = 0 ; i < percentages.length ; i++) {
                measures[i] = new PercentageCoveredMeasure(graphs[g].getNumNodes(), numberOfCliques[g], percentages[i]);
                MeasurementRegistry.registerGlobalMeasure(measures[i]);
            }
            for (int i = 0; i < traversalTypes.size(); i++) {
                log.write("% traversal type: " + traversalTypes.get(i));
                MeasurementRegistry.reset();
                BKTraversal traversal = BKTraversalFactory.getTraversal(traversalTypes.get(i), graphs[g], instance_bound);
                HashSet<Integer> clique = traversal.getNext();
                int numCliques = 0;
                while (clique != null && numCliques < numberOfCliques[g]) {
                    numCliques++;
                    //if (numCliques % 50000 == 0) System.out.println(numCliques);
                    MeasurementRegistry.addClique(clique);
                    clique = traversal.getNext();
                }
                log.write(MeasurementRegistry.getAllMeasurements());
            }
            log.write("\n");
            log.flush();
        }
    }



    //TODO: Is there some way to ensure greater values for union?
    public static void main(String[] args) throws IOException {
        //memoryAllFilesTests();
        //runtimeAllFilesTests();
        //runPercentageCoveredTests();
        //runDiversityAllFilesTests();
        //runDiversityAsFunctionOfWindowSizeTests();
        //runDiversityAsFunctionOfWindowSizeTests2();
        runtimeIncrementsAllFilesTests();
        //runDiversityAsFunctionOfInstancesTests();
        //runPrefixDiversityTests();
        System.exit(0);
        //writeDegeneracyOrder("data/Amazon0505.txt", "data/Amazon0505_degeneracy.txt");
        //System.exit(0);
        //String[] filenames = {  &  & & \\_degeneracy"};//,  "Amazon0505"}; //"facebook_combined",
        //String[] filenames = {"Amazon0505_degeneracy"}; //facebook_combined_degeneracy","facebook_combined"};
        //String[] filenames = {"soc-Epinions1_degeneracy"};
        //String[] filenames = {"youtube"};
        //String[] filenames = {"facebook_combined_degeneracy"};
        String[] filenames = { "soc-Epinions", "Amazon0505", "dblp-2008", "youtube"};
        long[] numberOfCliques = {1775065, 1357215, 843600, 3265956};
        //MeasurementRegistry.registerGlobalMeasure(new UnionMeasure());
        //MeasurementRegistry.registerGlobalMeasure(new CliqueSizeMeasure());
        //MeasurementRegistry.registerGlobalMeasure(new UnionPrefixMeasure(new int[]{200, 400, 600, 800, 1000}));
        boolean logTime = true;
        boolean logDiversity = true;
        printClique = false;

        ArrayList<Integer> windowSizes = new ArrayList<>(Arrays.asList(2, 10, 50));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String logFilename = "results/ALL_combined_" + formatter.format(System.currentTimeMillis()) + ".csv";

        Logger log = new Logger(logFilename);
        ArrayList<BKTraversalFactory.Type> traversalTypes = new ArrayList<>();

        traversalTypes.add(BKTraversalFactory.Type.CLASSIC);
        traversalTypes.add(BKTraversalFactory.Type.STACK_EXTEND_LOWEST);
        //traversalTypes.add(BKTraversalFactory.Type.EPPSTEIN);
        //traversalTypes.add(BKTraversalFactory.Type.STACK_EXTEND_RANDOM);
        //traversalTypes.add(BKTraversalFactory.Type.ITERATIVE);
        BKNode.PivotType[] pivotTypes = {BKNode.PivotType.LOWEST_ID, BKNode.PivotType.ARBITRARY};
        int k = 5000000;
        int instanceBound = 3;
        int increment = k / 10;
        log.write("k, " + k);

        for (String filename: filenames) {

            Graph graph = new Graph("data/" + filename + ".txt", Graph.Type.EDGE_LIST);
            System.out.println("nodes=" + graph.getNumNodes() + " edges=" + graph.getNumEdges());
            log.write(filename);

            for (Integer size: windowSizes) {
                MeasurementRegistry.registerWindowMeasure(new CoverageWindowMeasure(size));
                //MeasurementRegistry.registerWindowMeasure(new WindowInverseCoverage(size, graph.getNumNodes()));
                MeasurementRegistry.registerWindowMeasure(new WindowAlphaCoverage(size, graph.getNumNodes()));
                //MeasurementRegistry.registerWindowMeasure(new WindowInverseProportionalCoverage(size));
                MeasurementRegistry.registerWindowMeasure(new MaxJaccard(size));
                //MeasurementRegistry.registerWindowMeasure(new AvgJaccard(size));
            }

            for (BKNode.PivotType pivot : pivotTypes) {
                BKNode.setPivot(pivot);
                System.out.println(pivot);
                for (BKTraversalFactory.Type type : traversalTypes) {

                    BKTraversal bkt = BKTraversalFactory.getTraversal(type, graph, instanceBound);
                    logKCliques(bkt, k, log, logDiversity, logTime, increment);
                    log.write("\n");
                    PriorityRoundRobin.reset();
                    MeasurementRegistry.reset();
                }
            }
        }

        log.close();
    }

}
