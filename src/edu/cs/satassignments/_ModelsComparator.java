package edu.cs.satassignments;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.cs.satassignments.traversal.BKTraversal;
import edu.cs.satassignments.traversal.BKTraversalFactory;
import edu.cs.satassignments.traversal.VariableNode;
import org.logicng.datastructures.Assignment;
//import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.io.parsers.ParserException;
import org.logicng.io.parsers.PropositionalParser;
import org.logicng.solvers.MiniSat;
import org.logicng.solvers.SATSolver;

public class _ModelsComparator {

	private static final int NUM_VARIABLE = 50;

	public static void main(String[] args) throws ParserException, IOException {
//		String fileName = "C:\\Users\\nilib\\eclipse-workspace\\GrayCode\\dnf-formulas-100000.txt";
//		Path out = Paths.get(fileName);
//		List<String> formulas = Files.readAllLines(out, Charset.defaultCharset());
		
		List<String> formulas = new ArrayList<String>();

		for (int i = 0; i < 100; i++) {
			String formula = _DnfGenerator.getFormula(NUM_VARIABLE, 20, 10);
			formulas.add(formula);
		}

		for (String formulaStr : formulas) {
			final FormulaFactory f = new FormulaFactory();
			final PropositionalParser p = new PropositionalParser(f);
			final org.logicng.formulas.Formula formula = p.parse(formulaStr);
			final SATSolver miniSat = MiniSat.miniSat(f);
			miniSat.add(formula);
			List<Assignment> a = miniSat.enumerateAllModels();
//		printAssignments(a);
			int a1 = a.size();
			if (a.size() == 1 && a.get(0).positiveVariables().size() == 0 && a.get(0).negativeVariables().size() == 0) {
				a1 = (int) Math.pow(2, NUM_VARIABLE);
			}

//			Formula formula1 = parseFormula(formulaStr);
//			for (int i = 1; i < 10; i++) {
//				ModelsEnumerator.MAX_STACKS = i;
//				ModelsEnumerator e = new ModelsEnumerator();
//				e.init(formula1);
//				e.modelsEnumeration();
//				int a2 = e.allAssign.size();
//				if (a1 != a2) {
//					System.out.println(formulaStr);
//					System.out.println(a1 + " " + a2);
//					System.out.println("num of stacks: " + i);
//				}
//			}
			Formula ff = new DnfFormula(formulaStr, NUM_VARIABLE);
			 VariableNode.setPivot(VariableNode.PivotType.ARBITRARY);
			BKTraversal bkt = BKTraversalFactory.getTraversal(BKTraversalFactory.Type.STACK_EXTEND_LOWEST, ff, 3);
			int a2 = _Main2.logKCliques(bkt);
			if (a1 != a2) {
				System.out.println(formulaStr);
				System.out.println(a1 + " " + a2);
			}
		}
		
	}
	

}
