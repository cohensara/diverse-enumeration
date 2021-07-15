package edu.cs.enumalgorithms;
import java.util.HashMap;
import java.util.List;

public interface Formula {
	
	int getNumOfVariables();
	
	int getNumOfclauses();

	boolean isAssignmentSatisfyingFormula(HashMap<Integer, Boolean> varValues, List<Integer> satisfiedClauses,
			List<Integer> clausesToRemove);

}
