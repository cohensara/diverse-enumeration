package edu.cs.satassignments;

import java.io.*;
import java.util.*;

//TODO: Implement adjacency list reader
public class Graph {

    public enum Type {ADJACENCY_LIST, EDGE_LIST}
    private HashSet<Integer> nodes;
    private HashMap<Integer, HashSet<Integer>> edges;
    int numEdges = 0;

    //assumes that the nodes are numbered 0 to size-1
    public void writeEdgeList(String filename) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        boolean started = false;
        for (int i = 0 ; i < nodes.size() ; i++) {
            HashSet<Integer> edgesList = edges.get(i);
            for (Integer x : edgesList) {
                if (i < x) {
                    if (started) {
                        writer.newLine();
                    }
                    else {
                        started = true;
                    }
                    writer.write(i + " " + x);
                }
            }
        }
        writer.close();
    }

    public Graph(HashSet<Integer> nodes, HashMap<Integer, HashSet<Integer>>  edges) {

        this.nodes = nodes;
        this.edges = edges;
        numEdges = 0;
        for (Integer x : nodes) {
            numEdges += edges.get(x).size();
        }
        numEdges = numEdges / 2;
    }

    public Graph(String filename, Type filetype) throws IOException {

        if (filetype == Type.ADJACENCY_LIST) {
            readAdjacencyList(filename);
        }
        else readEdgeList(filename);
    }

    private void readEdgeList(String filename) throws IOException {

        nodes = new HashSet<>();
        edges = new HashMap<>();
        String[] edge;

        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line = reader.readLine();
        while (line != null) {
            edge = line.split("\\s+");
            int x = Integer.parseInt(edge[0]);
            int y = Integer.parseInt(edge[1]);

            //assigning edge to null ensures garbage collection, and has been proven important in practice
            edge = null;

            if (x != y) {

                HashSet<Integer> xneighbors = edges.get(x);
                HashSet<Integer> yneighbors = edges.get(y);
                if (xneighbors == null) {
                    nodes.add(x);
                    xneighbors = new HashSet<>();
                    edges.put(x, xneighbors);
                }
                if (yneighbors == null) {
                    nodes.add(y);
                    yneighbors = new HashSet<>();
                    edges.put(y, yneighbors);
                }
                xneighbors.add(y);
                yneighbors.add(x);
            }
            line = reader.readLine();
        }

        for (Integer x: nodes) {
            numEdges += edges.get(x).size();
        }
        numEdges = numEdges / 2;
    }

    private void readAdjacencyList(String filename) {

        System.out.println("Cannot load " + filename + ". Reading adjacency list not supported!");
        System.exit(1);
    }

    public int getNumEdges() {
        return numEdges;
    }

    public int getNumNodes() {
        return nodes.size();
    }

    public static Graph getCIKMGraph() {

        HashSet<Integer> nodes = new HashSet<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));
        HashMap<Integer, HashSet<Integer>> edges = new HashMap<>();
        edges.put(0, new HashSet<>(Arrays.asList(1, 2, 3, 4)));
        edges.put(1, new HashSet<>(Arrays.asList(0,2,4)));
        edges.put(2, new HashSet<>(Arrays.asList(0,1)));
        edges.put(3, new HashSet<>(Arrays.asList(0,4)));
        edges.put(4, new HashSet<>(Arrays.asList(0,5)));
        edges.put(5, new HashSet<>(Arrays.asList(4,6,7)));
        edges.put(6, new HashSet<>(Arrays.asList(5,7,8)));
        edges.put(7, new HashSet<>(Arrays.asList(5,6,8)));
        edges.put(8, new HashSet<>(Arrays.asList(6,7)));
        return new Graph(nodes, edges);
    }

    public HashSet<Integer> getNeighbors(int node) {

        return edges.get(node);
    }

    public HashSet<Integer> getNodes() {

        return new HashSet<>(nodes);
    }

    @Override
    public String toString() {
        return "Graph{" +
                "nodes=" + nodes +
                ", edges=" + edges +
                '}';
    }

    public void writeIndependentSetOrder(String filename) throws IOException {

        Hashtable<Integer, Integer> nodePermutation = new Hashtable<>();


        ArrayList<Integer> allNodes = new ArrayList<>(nodes);
        ArrayList<Integer> canChoose = new ArrayList<>(nodes);

        int i = 0;
        while (allNodes.size() > 0) {
            int x = canChoose.get(0);
            //System.out.println(x + "->" + i);
            nodePermutation.put(x, i);
            canChoose.remove(0);
            allNodes.remove(Integer.valueOf(x));
            i++;
            canChoose.removeAll(edges.get(x));
            if (canChoose.isEmpty()) canChoose.addAll(allNodes);
        }

        writeOutToFile(filename, nodePermutation);
    }

   public void writeDegeneracyOrder(String filename) throws IOException {

        Hashtable<Integer, Integer> nodePermutation = new Hashtable<>();

        TreeMap<Integer, HashSet<Integer>> nodesWithGivenDegree = new TreeMap<>();
        Hashtable<Integer, Integer> nodeDegree = new Hashtable<>();

        for (Integer x : nodes) {

            int degree = edges.get(x).size();
            HashSet<Integer> nodesOfLength = nodesWithGivenDegree.get(degree);
            if (nodesWithGivenDegree.get(degree) == null) {
                nodesOfLength = new HashSet<>();
            }
            nodesOfLength.add(x);
            nodesWithGivenDegree.put(degree, nodesOfLength);
            nodeDegree.put(x, degree);
        }

        for (int i = 0 ; i < nodes.size() ; i++) {

            int smallestDegree = nodesWithGivenDegree.firstKey();
            HashSet<Integer> nodesList = nodesWithGivenDegree.get(smallestDegree);
            int x = nodesList.stream().findAny().orElse(-1);
            nodePermutation.put(x, i);
            nodesList.remove(x);
            if (nodesList.size() == 0) nodesWithGivenDegree.remove(smallestDegree);
            HashSet<Integer> neighbors = edges.get(x);
            nodeDegree.remove(x);
            for (Integer y: neighbors) {
                if (nodeDegree.containsKey(y)) {
                    int yDegree = nodeDegree.get(y);
                    nodesWithGivenDegree.get(yDegree).remove(y);
                    if (nodesWithGivenDegree.get(yDegree).size() ==0)
                        nodesWithGivenDegree.remove(yDegree);
                    yDegree--;
                    nodeDegree.put(y, yDegree);
                    HashSet<Integer> newListForY = nodesWithGivenDegree.get(yDegree);
                    if (newListForY == null) newListForY = new HashSet<>();
                    newListForY.add(y);
                    nodesWithGivenDegree.put(yDegree, newListForY);
                }
            }
        }
       writeOutToFile(filename, nodePermutation);
    }

    private void writeOutToFile(String filename, Hashtable<Integer, Integer> nodePermutation) throws IOException {


        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        for (Integer x: nodes) {

            HashSet<Integer> xneighbors = edges.get(x);
            for (Integer y: xneighbors) {

                if (nodePermutation.get(x) < nodePermutation.get(y)) {
                    writer.write(nodePermutation.get(x) + " " + nodePermutation.get(y));
                    writer.newLine();
                }
            }
        }
        writer.close();
    }
/*
    public void writeRandomOrder(String filename) throws IOException {

        Hashtable<Integer, Integer> nodePermutation = new Hashtable<>();

        ArrayList<Integer> allNodes = new ArrayList<>();
        allNodes.addAll(nodes);

        Random random = new Random();

        int i = 0;
        while (allNodes.size() > 0) {
            int j = random.nextInt(allNodes.size());
            int x = allNodes.get(j);
            nodePermutation.put(x, i);
            allNodes.remove(j);
            i++;
        }

        writeOutToFile(filename, nodePermutation);
    }

    public void writeDegreeOrder(String filename) throws IOException {

        Hashtable<Integer, Integer> nodePermutation = new Hashtable<>();


        ArrayList<Integer> allNodes = new ArrayList<>();
        allNodes.addAll(nodes);
        allNodes.sort(new SortByDegree(edges));

        for (int i = 0 ; i < allNodes.size() ; i++) {
            nodePermutation.put(allNodes.get(i), i);
            //System.out.println(allNodes.get(i) + "->" + i);
        }

        writeOutToFile(filename, nodePermutation);
    } */


}

/*
class SortByDegree implements Comparator<Integer> {

    HashMap<Integer,HashSet<Integer>> edges;

    public SortByDegree(HashMap<Integer,HashSet<Integer>> edges) {
        this.edges = edges;
    }

    public int compare(Integer x, Integer y) {
        int xdegree = edges.get(x).size();
        int ydegree = edges.get(y).size();

        return ydegree - xdegree;
    }
}*/
