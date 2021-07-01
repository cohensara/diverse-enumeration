package edu.cs.satassignments;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import edu.cs.satassignments.traversal.BKTraversal;
import edu.cs.satassignments.traversal.BKTraversalFactory;
import edu.cs.satassignments.traversal.BKTraversalFactory.Type;

public class _Main2 {

	private static final int NUM_OF_VARIABLES = 20;

	public static int logKCliques(BKTraversal traversal) throws IOException {

		int num = 0;
		HashMap<Integer, Boolean> model = traversal.getNext();
		while (model != null) {
//			System.out.println(model);
			num++;
			if (num% 100000 == 0) {
				System.out.println(num);
			}
			model = traversal.getNext();
		}
		return num;
	}

	// TODO: Is there some way to ensure greater values for union?
	public static void main(String[] args) throws IOException {
//		String formulaStr = "(~1 & ~3) | (0 & 1) | (~0 & 2 & 3 & 5 & ~9) | (6 & 7 & ~9) | (0 & ~4 & ~6 & ~7) | (3 & 5 & ~6 & ~7 & ~8 & ~9) | (0 & ~2 & 4 & 5 & ~6 & ~7 & 9) | (7 & 8) | (1 & ~2 & ~3 & ~4 & 5 & 6 & ~7)";
//		_DnfGenerator gen = new _DnfGenerator();
//		String formulaStr = gen.getFormula(NUM_OF_VARIABLES, 100, 5);
//		System.out.println(formulaStr);
//
//		Path out = Paths.get("dnf-formulas-100-1000-10.txt");
//		try {
//			List<String> s = new ArrayList<String>();
//			s.add(formulaStr);
//			Files.write(out, s , Charset.defaultCharset());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		String fileName = "5dnf-20vars-100clauses.txt";
		Path out = Paths.get(fileName);
		List<String> formulas = Files.readAllLines(out, Charset.defaultCharset());
		String formulaStr = formulas.get(0);
		
		
		Formula f = new DnfFormula(formulaStr, NUM_OF_VARIABLES);
		BKTraversal bkt = BKTraversalFactory.getTraversal(Type.CLASSIC, f, 100);
		System.out.println(logKCliques(bkt));
	}


}
