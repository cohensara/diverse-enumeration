package edu.cs.enumalgorithms.diversity;

import java.util.*;

public class MeasurementRegistry<T> {

    TreeMap<Integer, Window<T>> allWindows = new TreeMap<>();
    ArrayList<Measure<T>> globalMeasures = new ArrayList<>();
    HashMap<Integer, ArrayList<WindowMeasure<T>>> windowMeasures = new HashMap<>();

    public void registerGlobalMeasure(Measure<T> m) {

        globalMeasures.add(m);
    }

    public void removeAllMeasures() {
        allWindows = new TreeMap<>();
        globalMeasures = new ArrayList<>();
        windowMeasures = new HashMap<>();
    }

    public void reset() {

        allWindows.replaceAll((s, v) -> new Window<T>(s));

        for (Measure m: globalMeasures)
            m.reset();

        for (ArrayList<WindowMeasure<T>> arrWm: windowMeasures.values())
            for (WindowMeasure<T> wm: arrWm)
                wm.reset();
    }

    public void registerWindowMeasure(WindowMeasure<T> w) {

        int size = w.getWindowSize();

        if (allWindows.get(size) == null) {
            allWindows.put(size, new Window<T>(size));
            ArrayList<WindowMeasure<T>> wm = new ArrayList<>();
            wm.add(w);
            windowMeasures.put(size, wm);
        }
        else {
            ArrayList<WindowMeasure<T>> wm = windowMeasures.get(size);
            wm.add(w);
        }
    }

    public void addItem(T item) {

        for (Measure<T> m : globalMeasures) {
            m.addItem(item);
        }

        for (Integer size: allWindows.keySet()) {
            Window<T> w = allWindows.get(size);
            T removed = w.add(item);
            for (WindowMeasure<T> wm: windowMeasures.get(size)) {
                wm.addItem(w, removed, item);
            }
        }
    }

    public String getAllMeasurements() {

        ArrayList<String> measurements = new ArrayList<>();
        for (Measure<T> m: globalMeasures) measurements.add("" + m.getDiversityValue());
        for (Integer size: allWindows.keySet()) {
            for (WindowMeasure<T> wm : windowMeasures.get(size)) {
                measurements.add(wm.getDiversityValue());
            }
        }

        return String.join(" ", measurements);
    }

    public String getAllMeasurementsHeaders() {

        ArrayList<String> measurements = new ArrayList<>();

        for (Measure<T> m: globalMeasures) measurements.add("" + m.getHeader());
        for (Integer size: allWindows.keySet()) {
            for (WindowMeasure<T> wm : windowMeasures.get(size)) {
                measurements.add(wm.getHeader() + "(" + size + ")");
            }
        }

        return String.join(", ", measurements);
    }
}
