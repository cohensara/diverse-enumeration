package edu.cs.satassignments.traversal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import edu.cs.satassignments.Formula;
import org.apache.commons.collections4.ListUtils;

public class VariableNode {

	public enum PivotType {
		LOWEST_ID, ARBITRARY
	}

	static PivotType pivotType = PivotType.LOWEST_ID;

	static int howManyCreated = 0;
	Formula formula;
	int variableId;
	Boolean value;
	Boolean shouldHandleOppositeValue;
	HashMap<Integer, Boolean> varValues = new HashMap<>();
	int level;
	static Random rand = new Random();

	Integer[] PMinusNPivot = null;

	List<Integer> satisfiedClausesByFather = new ArrayList<Integer>();
	List<Integer> clausesToRemoveFromSatCheck = new ArrayList<Integer>();
	List<Integer> clausesToRemoveFromSatCheckForBrother = new ArrayList<Integer>();

	int childIndex = 0;

	public VariableNode(Formula formula) {
		this.formula = formula;
		this.varValues = new HashMap<Integer, Boolean>(100);
		satisfiedClausesByFather = IntStream.rangeClosed(0, formula.getNumOfclauses() - 1).boxed()
				.collect(Collectors.toList());

		this.variableId = 0;// TODO change to random varId
		this.value = true;
		this.shouldHandleOppositeValue = false;

		varValues.put(variableId, !value);
		boolean isOppositeValueSat = formula.isAssignmentSatisfyingFormula(varValues, satisfiedClausesByFather,
				clausesToRemoveFromSatCheckForBrother);
		varValues.put(variableId, value);
		boolean isValueSat = formula.isAssignmentSatisfyingFormula(varValues, satisfiedClausesByFather,
				clausesToRemoveFromSatCheck);

		if (isValueSat) {
			shouldHandleOppositeValue = isOppositeValueSat;
		} else {
			shouldHandleOppositeValue = false;
			value = !value;
			varValues.put(variableId, value);
			clausesToRemoveFromSatCheck = clausesToRemoveFromSatCheckForBrother;
			clausesToRemoveFromSatCheckForBrother = null;
		}

		level = 0;
		howManyCreated++;
		if (!isFinal())
			setIteration();
	}

	public static int getHowManyCreated() {
		return howManyCreated;
	}

	public VariableNode(int variableId, Boolean value, Boolean shouldHandleOppositeValue, Formula formula, int level,
			HashMap<Integer, Boolean> varValues, Integer[] PMinusNPivot, List<Integer> satisfiedClausesByFather,
			List<Integer> clausesToRemove, List<Integer> clausesToRemoveForBrother) {
		this.variableId = variableId;
		this.value = value;
		this.shouldHandleOppositeValue = shouldHandleOppositeValue;

		this.formula = formula;
		this.level = level;
		this.varValues = varValues;
		this.satisfiedClausesByFather = satisfiedClausesByFather;
		this.clausesToRemoveFromSatCheck = clausesToRemove;
		this.clausesToRemoveFromSatCheckForBrother = clausesToRemoveForBrother;
		howManyCreated++;
		if (!isFinal())
			setIteration(PMinusNPivot);
	}

	public int getLevel() {
		return level;
	}

	public boolean isFinal() {
		return formula.getNumOfVariables() == varValues.size();
	}

	public static void setPivot(PivotType type) {
		pivotType = type;
	}

	private void setIteration() {
		PMinusNPivot = new Integer[formula.getNumOfVariables() - 1];
		for (int i = 1; i < formula.getNumOfVariables(); i++) {
			PMinusNPivot[i - 1] = i;
		}
		childIndex = 0;
	}

	private void setIteration(Integer[] PMinusNPivot) {
		this.PMinusNPivot = PMinusNPivot;
		childIndex = 0;
	}

	public boolean hasBrother() {
		return this.shouldHandleOppositeValue;
	}

	public VariableNode getBrother() {
		if (this.shouldHandleOppositeValue == false) {
			return null;
		}

		boolean brotherValue = !this.value;
		int nextChildLevel = level;

		HashMap<Integer, Boolean> nextVarValues = new HashMap<Integer, Boolean>(varValues);
		nextVarValues.put(this.variableId, brotherValue);

		Integer[] nextPivot = null;
		if (!isFinal()) {
			nextPivot = Arrays.copyOf(PMinusNPivot, PMinusNPivot.length);
		}
		return new VariableNode(this.variableId, brotherValue, false, formula, nextChildLevel, nextVarValues, nextPivot,
				satisfiedClausesByFather, clausesToRemoveFromSatCheckForBrother, null);
	}

	public VariableNode getNextChild() {

		boolean value = true;
		if (pivotType == PivotType.ARBITRARY) {
			value = rand.nextBoolean();
		}
		boolean shouldHandleOppositeValue = false;
		int v = -1;
		int nextChildLevel = level + 1;
		List<Integer> clausesToCheck = ListUtils.removeAll(satisfiedClausesByFather, clausesToRemoveFromSatCheck);
		List<Integer> clausesToRemove1 = new ArrayList<Integer>();
		List<Integer> clausesToRemove2 = new ArrayList<Integer>();
		while (v == -1 && childIndex < PMinusNPivot.length) {
			if (pivotType == PivotType.ARBITRARY) {
				int i = rand.nextInt(PMinusNPivot.length - childIndex);
				int j = PMinusNPivot[childIndex];
				PMinusNPivot[childIndex] = PMinusNPivot[childIndex + i];
				PMinusNPivot[childIndex + i] = j;
			}

			varValues.put(PMinusNPivot[childIndex], value);
			boolean isValueSat = formula.isAssignmentSatisfyingFormula(varValues, clausesToCheck, clausesToRemove1);
			varValues.put(PMinusNPivot[childIndex], !value);
			boolean isOppositeValueSat = formula.isAssignmentSatisfyingFormula(varValues, clausesToCheck,
					clausesToRemove2);
			if (isValueSat) {
				shouldHandleOppositeValue = isOppositeValueSat;
				v = PMinusNPivot[childIndex];
				if (!isOppositeValueSat) {
					clausesToRemove2 = null;
				}
			} else if (isOppositeValueSat) {
				shouldHandleOppositeValue = false;
				value = !value;
				v = PMinusNPivot[childIndex];
				clausesToRemove1 = clausesToRemove2;
				clausesToRemove2 = null;
			}
			varValues.remove(PMinusNPivot[childIndex]);

			childIndex++;
		}

		HashMap<Integer, Boolean> nextVarValues = new HashMap<Integer, Boolean>(varValues);
		nextVarValues.put(v, value);

		Integer[] nextPivot = Arrays.copyOfRange(PMinusNPivot, childIndex, PMinusNPivot.length);
		return new VariableNode(v, value, shouldHandleOppositeValue, formula, nextChildLevel, nextVarValues, nextPivot,
				clausesToCheck, clausesToRemove1, clausesToRemove2);
	}

	public HashMap<Integer, Boolean> getAssignment() {
		if (varValues.size() == formula.getNumOfVariables()) {
			return varValues;
		}
		return null;
	}

	@Override
	public String toString() {
		return "" + this.varValues;
	}
}
