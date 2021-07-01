package edu.cs.satassignments;
import java.util.HashMap;
import java.util.List;

public interface Formula {
	
	public int getNumOfVariables();
	
	public int getNumOfclauses();

	public boolean isAssignmentSatisfyingFormula(HashMap<Integer, Boolean> varValues, List<Integer> satisfiedClauses,
			List<Integer> clausesToRemove);

}
