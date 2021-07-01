package edu.cs.satassignments;

import edu.cs.satassignments.diversity.AvgDistance;
import edu.cs.satassignments.diversity.MeasurementRegistry;
import edu.cs.satassignments.diversity.MinDistance;
import edu.cs.satassignments.traversal.BKTraversal;
import edu.cs.satassignments.traversal.BKTraversalFactory;
import edu.cs.satassignments.traversal.PriorityRoundRobin;
import edu.cs.satassignments.traversal.VariableNode;
import edu.cs.satassignments.diversity.*;
import edu.cs.satassignments.traversal.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    static boolean printModel = true;

    private static void logKModels(BKTraversal traversal, int k, Logger log,
                                   boolean logDiversity, boolean logTime, int increment) throws IOException {

        int i = 0;
        log.write(traversal.getName());
        if (logTime) {
            log.startTimer();
        }

        HashMap<Integer, Boolean> model = traversal.getNext();
        log.write("model#, BKNodes created#, " + traversal.getStatisticsHeader() + ", time");
        while (model != null && i < k) {
            i++;
            if (printModel) System.out.println(model);
            if (logTime && i % increment == 0) {
                log.logElapsedTime("" + i + ", " + VariableNode.getHowManyCreated() + ", " + traversal.getStatistics());
                //log.write(MeasurementRegistry.getAllMeasurementsHeaders());
                //log.write(MeasurementRegistry.getAllMeasurements());
            }
            if (logDiversity) {
                log.pauseTimer();
                MeasurementRegistry.addModel(model);
                log.resumeTimer();
            }
            model = traversal.getNext();
        }

        log.logElapsedTime("" + i + ", " + VariableNode.getHowManyCreated() + ", " + traversal.getStatistics());
        log.write(traversal.getStatisticsHeader());
        log.write(traversal.getStatistics());
        if (logDiversity) {
            log.write(MeasurementRegistry.getAllMeasurementsHeaders());
            log.write(MeasurementRegistry.getAllMeasurements());
        }
        log.flush();
    }

//    public static void runPrefixDiversityTests() throws IOException {
//
//        String[] filenames = {"Amazon0505_degeneracy", "dblp-2008_degeneracy", "eu_2005_degeneracy"}; //, "Amazon0505_degeneracy","dblp-2008", "dblp-2008_degeneracy"};
////                "facebook_combined", "facebook_combined_degeneracy", "eu_2005", "eu_2005_degeneracy"};
//
//        String logFilename = "results/prefix_tests_avg_10000.csv";
//
//        Logger log = new Logger(logFilename);
//        ArrayList<BKTraversalFactory.Type> traversalTypes = new ArrayList<>();
//
//        traversalTypes.add(BKTraversalFactory.Type.CLASSIC);
//        traversalTypes.add(BKTraversalFactory.Type.STACK_EXTEND_LOWEST);
//
//        int k = 10000;
//        int increment = 100 / 20;
//        int[] instance_bound = {5000, 7500};
//        VariableNode.PivotType[] pivotTypes = {VariableNode.PivotType.ARBITRARY, VariableNode.PivotType.LOWEST_ID};
//
//        MeasurementRegistry.registerGlobalMeasure(new UnionPrefixMeasure(new int[]{2000, 4000, 6000, 8000, 10000}));
//        //MeasurementRegistry.registerGlobalMeasure(new MaxJaccardPrefixMeasure((new int[]{2000, 4000, 6000, 8000, 10000}), "#.####"));
//
//        //MeasurementRegistry.registerWindowMeasure(new WindowUnion(k));
//        //MeasurementRegistry.registerWindowMeasure(new MaxJaccard(k));
//        //MeasurementRegistry.registerWindowMeasure(new AvgJaccard(k));
//
//        for (String filename: filenames) {
//
//            Graph graph = new Graph("data/" + filename + ".txt", Graph.Type.EDGE_LIST);
//            log.write(filename + ", " + graph.getNumNodes() + ", " + graph.getNumEdges());
//            log.write("Algorithm, " + MeasurementRegistry.getAllMeasurementsHeaders());
//            BKTraversal traversal = BKTraversalFactory.getTraversal(BKTraversalFactory.Type.CLASSIC, graph, 0);
//
//            HashSet<Integer> clique = traversal.getNext();
//            int i = 0;
//            //log.write(MeasurementRegistry.getAllMeasurementsHeaders());
//            while (clique != null && i < k) {
//                i++;
//                MeasurementRegistry.addModel(clique);
//                clique = traversal.getNext();
//            }
//            log.write("Classic, " + MeasurementRegistry.getAllMeasurements());
//            log.flush();
//            MeasurementRegistry.reset();
//            for (VariableNode.PivotType ptype : pivotTypes) {
//                for (int inst : instance_bound) {
//                    traversal = BKTraversalFactory.getTraversal(BKTraversalFactory.Type.STACK_EXTEND_LOWEST, graph, inst);
//                    VariableNode.setPivot(ptype);
//                    clique = traversal.getNext();
//                    i = 0;
//                    while (clique != null && i < k) {
//                        i++;
//                        MeasurementRegistry.addModel(clique);
//                        clique = traversal.getNext();
//                    }
//                    log.write("ExtendLowest " + ptype + " instanceBound = " + inst + ", " + MeasurementRegistry.getAllMeasurements());
//                    log.flush();
//                    MeasurementRegistry.reset();
//                }
//            }
//            log.write("\n");
//        }
//    }


    //TODO: Is there some way to ensure greater values for union?
    public static void main(String[] args) throws IOException {
    	
    	String fileName = "C:\\Users\\nilib\\eclipse-workspace\\GrayCode\\dnf-formulas-100000.txt";
		Path path = Paths.get(fileName);
		List<String> formulas = Files.readAllLines(path, Charset.defaultCharset());

    	
    	
        boolean logTime = true;
        boolean logDiversity = true;
        printModel = false;

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
        VariableNode.PivotType[] pivotTypes = {VariableNode.PivotType.LOWEST_ID, VariableNode.PivotType.ARBITRARY};
        int k = 1000000;
        int instanceBound = 100;
        int increment = k / 5;
        log.write("k, " + k);

        for (String formulaStr: formulas) {

        	Formula formula = new DnfFormula(formulaStr, 10);
        	
//            Graph graph = new Graph("data/" + filename + ".txt", Graph.Type.EDGE_LIST);
//            System.out.println("nodes=" + graph.getNumNodes() + " edges=" + graph.getNumEdges());
//            log.write(filename);

            for (Integer size: windowSizes) {
//                MeasurementRegistry.registerWindowMeasure(new WindowUnion(size));
                //MeasurementRegistry.registerWindowMeasure(new WindowInverseCoverage(size, graph.getNumNodes()));
//                MeasurementRegistry.registerWindowMeasure(new WindowAlphaCoverage(size, graph.getNumNodes()));
                //MeasurementRegistry.registerWindowMeasure(new WindowInverseProportionalCoverage(size));
//                MeasurementRegistry.registerWindowMeasure(new MaxJaccard(size));
                //MeasurementRegistry.registerWindowMeasure(new AvgJaccard(size));
                
                MeasurementRegistry.registerWindowMeasure(new AvgDistance(size));
                MeasurementRegistry.registerWindowMeasure(new MinDistance(size));
            }

            for (VariableNode.PivotType pivot : pivotTypes) {
                VariableNode.setPivot(pivot);
                System.out.println(pivot);
                for (BKTraversalFactory.Type type : traversalTypes) {

                    BKTraversal bkt = BKTraversalFactory.getTraversal(type, formula, instanceBound);
                    logKModels(bkt, k, log, logDiversity, logTime, increment);
                    log.write("\n");
                    PriorityRoundRobin.reset();
                    MeasurementRegistry.reset();
                }
            }
        }

        log.close();
    }

}
