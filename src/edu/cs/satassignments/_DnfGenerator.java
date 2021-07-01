package edu.cs.satassignments;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class _DnfGenerator {
	public static int maxNumOfVars = 20;
	public static int maxNumOfClause = 100;
	public static int numOfVarsInClause = 10;
	public static int numOfFormulas = 1;

	public static void main(String[] args) {
		List<String> formulas = new ArrayList<String>();

		for (int i = 0; i < numOfFormulas; i++) {
			String formula = getFormula(maxNumOfVars, maxNumOfClause, numOfVarsInClause);
			formulas.add(formula);
		}

		Path out = Paths.get("10dnf-20vars-100clauses.txt");
		try {
			Files.write(out, formulas, Charset.defaultCharset());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String getFormula(int maxNumOfVars, int maxNumOfClause, int numOfVarsInClause) {
		StringBuilder formula = new StringBuilder();
		Random rand = new Random();
		int i = 0;
		while (i < maxNumOfClause) {

			int[] vars = new Random().ints(0, maxNumOfVars).distinct().limit(numOfVarsInClause).sorted().toArray();

			formula.append("(");
			for (Integer integer : vars) {
				String neg = rand.nextBoolean() == true ? "~" : "";
				formula.append(neg + integer + " & ");
			}
			formula.delete(formula.length() - 3, formula.length());
			formula.append(") | ");
			i++;
		}
		formula.delete(formula.length() - 3, formula.length());
		return formula.toString();
	}

}
