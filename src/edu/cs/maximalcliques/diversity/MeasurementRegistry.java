package edu.cs.maximalcliques.diversity;

import java.util.*;

public class MeasurementRegistry {

    static TreeMap<Integer, Window> allWindows = new TreeMap<>();
    static ArrayList<Measure> globalMeasures = new ArrayList<>();
    static HashMap<Integer, ArrayList<WindowMeasure>> windowMeasures = new HashMap<>();

    public static void registerGlobalMeasure(Measure m) {

        globalMeasures.add(m);
    }

    public static void removeAllMeasures() {
        allWindows = new TreeMap<>();
        globalMeasures = new ArrayList<>();
        windowMeasures = new HashMap<>();
    }

    public static void reset() {

        allWindows.replaceAll((s, v) -> new Window(s));

        for (Measure m: globalMeasures)
            m.reset();

        for (ArrayList<WindowMeasure> arrWm: windowMeasures.values())
            for (WindowMeasure wm: arrWm)
                wm.reset();
    }

    public static void registerWindowMeasure(WindowMeasure w) {

        int size = w.getWindowSize();

        if (allWindows.get(size) == null) {
            allWindows.put(size, new Window(size));
            ArrayList<WindowMeasure> wm = new ArrayList<>();
            wm.add(w);
            windowMeasures.put(size, wm);
        }
        else {
            ArrayList<WindowMeasure> wm = windowMeasures.get(size);
            wm.add(w);
        }
    }

    public static void addClique(HashSet<Integer> clique) {

        for (Measure m : globalMeasures) {
            m.addClique(clique);
        }

        for (Integer size: allWindows.keySet()) {
            Window w = allWindows.get(size);
            HashSet<Integer> removed = w.add(clique);
            for (WindowMeasure wm: windowMeasures.get(size)) {
                wm.addClique(w, removed, clique);
            }
        }
    }

    public static String getAllMeasurements() {

        ArrayList<String> measurements = new ArrayList<>();
        for (Measure m: globalMeasures) measurements.add("" + m.getDiversityValue());
        for (Integer size: allWindows.keySet()) {
            for (WindowMeasure wm : windowMeasures.get(size)) {
                measurements.add(wm.getDiversityValue());
            }
        }

        return String.join(" ", measurements);
    }

    public static String getAllMeasurementsHeaders() {

        ArrayList<String> measurements = new ArrayList<>();

        for (Measure m: globalMeasures) measurements.add("" + m.getHeader());
        for (Integer size: allWindows.keySet()) {
            for (WindowMeasure wm : windowMeasures.get(size)) {
                measurements.add(wm.getHeader() + "(" + size + ")");
            }
        }

        return String.join(", ", measurements);
    }
}
