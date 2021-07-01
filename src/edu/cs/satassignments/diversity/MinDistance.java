package edu.cs.satassignments.diversity;

import java.text.DecimalFormat;
import java.util.HashMap;

public class MinDistance implements WindowMeasure {

	DecimalFormat df = new DecimalFormat("#.###");
	private final int windowSize;
	int numberOfWindows = 0;
	float runningSum = 0;

	public MinDistance(int windowSize) {
		this.windowSize = windowSize;
	}
	
	private float computeDistance(HashMap<Integer, Boolean> model1, HashMap<Integer, Boolean> model2) {
		int diff = 0;
		for (Integer varId : model1.keySet()) {
			diff += model2.get(varId).equals(model1.get(varId)) ? 0 : 1;
		}
		
		return (float)diff ;
	}

	@Override
	public int getWindowSize() {
		return windowSize;
	}

	@Override
	public void addModel(Window window, HashMap<Integer, Boolean> removed, HashMap<Integer, Boolean> added) {

		if (!window.isFull())
			return;
		numberOfWindows++;
		float minDist = Float.MAX_VALUE;

		for (int i = 0; i < windowSize - 1; i++) {
			HashMap<Integer, Boolean> model = window.get(i);
			float j = computeDistance(model, added);
			if (j < minDist)
				minDist = j;
		}

		runningSum += minDist;
	}

	@Override
    public String getDiversityValue() {
        if (numberOfWindows == 0) {
            return "0";
        }
        return df.format(runningSum / numberOfWindows);
    }

    @Override
    public void reset() {
        numberOfWindows = 0;
        runningSum = 0;
    }

	@Override
	public String getHeader() {
		return "minDistance";
	}

	public String toString() {
		return "minDistance, windowSize, " + windowSize + ", diversity, " + getDiversityValue();
	}
}
