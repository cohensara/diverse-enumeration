package edu.cs.enumalgorithms.traversal;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import edu.cs.enumalgorithms.Formula;

public class SatisfyingAssignmentNode extends EnumerationNode<HashMap<Integer, Boolean>> {

	Formula formula;
	int variableIndex = -1;
	int[] variableIds;

	Boolean value;
	HashMap<Integer, Boolean> varValues;
	List<Integer> satisfiedClausesByFather;
	List<Integer> clausesToRemoveFromSatCheck = new ArrayList<>();

	boolean hasTrueChild = false;
	boolean hasFalseChild = false;
	List<Integer> clausesToRemoveFromSatCheckForTrueChild = new ArrayList<>();
	List<Integer> clausesToRemoveFromSatCheckForFalseChild = new ArrayList<>();

	public SatisfyingAssignmentNode(Formula formula) {
		this.formula = formula;
		this.varValues = new HashMap<>(100);
		satisfiedClausesByFather = IntStream.rangeClosed(0, formula.getNumOfclauses() - 1).boxed()
				.collect(Collectors.toList());

		variableIds = new int[formula.getNumOfVariables()];
		for (int i = 0 ; i < variableIds.length ; i++)
			variableIds[i] = i;

		if (variableIndex < variableIds.length - 1) {
			if (pivotType == PivotType.ARBITRARY) shuffleChild();
			updateChildren();
		}

		howManyCreated++;
	}

	private void updateChildren() {
		varValues.put(variableIds[variableIndex+1], false);
		hasFalseChild = formula.isAssignmentSatisfyingFormula(varValues, satisfiedClausesByFather,
				clausesToRemoveFromSatCheckForFalseChild);
		varValues.put(variableIds[variableIndex+1], true);
		hasTrueChild = formula.isAssignmentSatisfyingFormula(varValues, satisfiedClausesByFather,
				clausesToRemoveFromSatCheckForTrueChild);

		varValues.remove(variableIds[variableIndex+1]);
	}

	private void shuffleChild() {

		int chosen = rand.nextInt(variableIds.length - (variableIndex+1)) + variableIndex + 1;
		int temp = variableIds[chosen];
		variableIds[chosen] = variableIds[variableIndex + 1];
		variableIds[variableIndex + 1] = temp;
	}



	private SatisfyingAssignmentNode(Boolean value, Formula formula, int level,
									 HashMap<Integer, Boolean> varValues, int[] variableIds, int variableIndex,
									 List<Integer> satisfiedClausesByFather,
									 List<Integer> clausesToRemove) {
		this.value = value;
		this.formula = formula;
		this.level = level;
		this.varValues = varValues;
		this.variableIds = variableIds;
		this.variableIndex = variableIndex;
		this.satisfiedClausesByFather = satisfiedClausesByFather;
		this.clausesToRemoveFromSatCheck = clausesToRemove;
		howManyCreated++;
		if (variableIndex < variableIds.length - 1) updateChildren();
	}

	public boolean isFinal() {
		return formula.getNumOfVariables() == varValues.size();
	}

	@Override
	public boolean isFinalSuccess() {
		return true;
	}

	@Override
	public boolean hasMoreChildren() {
		return hasTrueChild || hasFalseChild;
	}

	@Override
	public int numberOfChildrenLeft() {
		int children = 0;
		if (hasTrueChild) children++;
		if (hasFalseChild) children++;
		return children;
	}

	private SatisfyingAssignmentNode getChild(boolean value, List<Integer> clausesToRemoveFromSatCheckForChild) {

		HashMap<Integer, Boolean> childVarValues = new HashMap<>(varValues);
		childVarValues.put(variableIds[variableIndex + 1], value);
		int[] variableIdsCopy = new int[variableIds.length];
		System.arraycopy(variableIds, 0, variableIdsCopy, 0, variableIds.length);
		return new SatisfyingAssignmentNode(value, formula, level + 1, childVarValues,
				variableIdsCopy, variableIndex + 1,
				satisfiedClausesByFather, clausesToRemoveFromSatCheckForChild);
	}

	public SatisfyingAssignmentNode getNextChild() {


		if (!hasMoreChildren()) return null;
		if (hasTrueChild) {
			SatisfyingAssignmentNode child = getChild(true, clausesToRemoveFromSatCheckForTrueChild);
			hasTrueChild = false;
			return child;
		}
		hasFalseChild = false;
		return getChild(false, clausesToRemoveFromSatCheckForFalseChild);
	}

	@Override
	public HashMap<Integer, Boolean> getAnswer() {
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
