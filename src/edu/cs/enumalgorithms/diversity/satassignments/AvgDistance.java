package edu.cs.enumalgorithms.diversity.satassignments;

import edu.cs.enumalgorithms.diversity.Window;
import edu.cs.enumalgorithms.diversity.WindowMeasure;

import java.text.DecimalFormat;
import java.util.HashMap;

public class AvgDistance implements WindowMeasure<HashMap<Integer, Boolean>> {

	DecimalFormat df = new DecimalFormat("#.###");
	private final int windowSize;
	int numberOfWindows = 0;
	float runningSum = 0;

	public AvgDistance(int windowSize) {
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
	public void addItem(Window<HashMap<Integer, Boolean>> window, HashMap<Integer, Boolean> removed, HashMap<Integer, Boolean> added) {

		if (!window.isFull())
			return;
		numberOfWindows++;
		float sum = 0;

		for (int i = 0; i < windowSize - 1; i++) {
			HashMap<Integer, Boolean> model = window.get(i);
            sum += computeDistance(model, added);
		}

		runningSum += sum / (windowSize - 1);
	}

	@Override
	public String getDiversityValue() {

		if (numberOfWindows == 0)
			return "0";
		return df.format(runningSum / numberOfWindows);
	}

	@Override
	public void reset() {
		numberOfWindows = 0;
		runningSum = 0;
	}

	@Override
	public String getHeader() {
		return "avgDistance";
	}

	public String toString() {
		return "avgDistance, windowSize, " + windowSize + ", diversity, " + getDiversityValue();
	}
}
