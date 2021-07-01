package edu.cs.satassignments;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class DnfFormula implements Formula {
	
	private List<HashMap<Integer, Boolean>> clauses = new ArrayList<HashMap<Integer, Boolean>>();
	private int numOfVariables = 0;

	public DnfFormula(String formulaStr, int numOfVariables) {
		this.numOfVariables = numOfVariables;
		String[] clausesStr = formulaStr.split(" \\| ");
		for (String clauseStr : clausesStr) {
			HashMap<Integer, Boolean> clause = new HashMap<>();
			clauseStr = clauseStr.substring(1, clauseStr.length() - 1);
			String[] literals = clauseStr.split(" & ");
			for (String literal : literals) {
				boolean isNeg = false;
				if (literal.startsWith("~")) {
					literal = literal.substring(1);
					isNeg = true;
					
				} else {
					isNeg = false;
				}
				clause.put(Integer.parseInt(literal), !isNeg);
			}
			clauses.add(clause);
		}
	}
	
	public static DnfFormula getFormulaFromFile(String fileName, int numOfVariables) {
		Path out = Paths.get(fileName);
		List<String> formulas;
		try {
			formulas = Files.readAllLines(out, Charset.defaultCharset());
			String formulaStr = formulas.get(0);
			return new DnfFormula(formulaStr, numOfVariables);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	
	public void addClause(HashMap<Integer, Boolean> clause) {
		clauses.add(clause);
	}
	
	@Override
	public int getNumOfVariables() {
		return numOfVariables;
	}
	
	@Override
	public int getNumOfclauses() {
		return clauses.size();
	}
	
//	@Override
//	public boolean isAssignmentSatisfyingFormula(HashMap<Integer, Boolean> assignment, List<Integer> satisfiedClauses,
//			List<Integer> clausesToRemove) {
//		for (HashMap<Integer, Boolean> clauseVars : clauses) {
//			boolean isClauseSat = true;
//			for (Entry<Integer, Boolean> var : clauseVars.entrySet()) {
//				int varId = var.getKey();
//				if (assignment.containsKey(varId) && !assignment.get(varId).equals(var.getValue())) {
//					isClauseSat = false;
//					break;
//				}
//			}
//			if (isClauseSat) {
//				return true;
//			}
//		}
//		return false;
//	}
	
	@Override
	public boolean isAssignmentSatisfyingFormula(HashMap<Integer, Boolean> assignment, List<Integer> satisfiedClauses,
			List<Integer> clausesToRemove) {
		for (int clauseId : satisfiedClauses) {
			HashMap<Integer, Boolean> clauseVars = clauses.get(clauseId);
			boolean isClauseSat = true;
			for (Entry<Integer, Boolean> var : clauseVars.entrySet()) {
				int varId = var.getKey();
				if (assignment.containsKey(varId) && !assignment.get(varId).equals(var.getValue())) {
					isClauseSat = false;
					break;
				}
			}
			if (isClauseSat) {
				return true;
			} else {
				clausesToRemove.add(clauseId);
			}
		}
		return false;
	}
}
