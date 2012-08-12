package de.htw.berater.test.junit;

import java.util.Set;

import junit.framework.TestCase;
import de.htw.berater.Berater;
import de.htw.berater.StaticFactory;
import de.htw.berater.db.SQLConstraint;

public class Berater1Test extends TestCase {

	Berater berater;
	
	public Berater1Test() {
		this.berater = StaticFactory.getNewBerater1("inferredSmartphones.rdf", "http://semantische-interoperabilitaet-projekt#");
	}
	
	public void testZweck() {
		// Frage 1
		System.out.println(berater.askFirstQuestionZweck());
		String question = berater.evaluateAndAskNewQuestion("spiele");
		printCurrentSQLConstraints(berater.getCurrentSQLConstraintsList());
		
		// Frage 2a
		printLeerzeilen();
		System.out.println(question);
		question = berater.evaluateAndAskNewQuestion("normales");
		printCurrentSQLConstraints(berater.getCurrentSQLConstraintsList());
		
		// Frage 2b
		/*printLeerzeilen();
		System.out.println(question);
		question = berater.evaluateAndAskNewQuestion("gutes");
		printCurrentSQLConstraints(berater.getCurrentSQLConstraintsList());*/
		
		// Frage 3
		printLeerzeilen();
		System.out.println(question);
		question = berater.evaluateAndAskNewQuestion("???");
		printCurrentSQLConstraints(berater.getCurrentSQLConstraintsList());
		
		// Frage 4 
		printLeerzeilen();
		System.out.println(question);
		question = berater.evaluateAndAskNewQuestion("???");
		printCurrentSQLConstraints(berater.getCurrentSQLConstraintsList());
				
		// Frage 5
		printLeerzeilen();
		System.out.println(question);
		question = berater.evaluateAndAskNewQuestion("outdoor");
		printCurrentSQLConstraints(berater.getCurrentSQLConstraintsList());
		
		// Frage 6
		printLeerzeilen();
		System.out.println(question);
		question = berater.evaluateAndAskNewQuestion("navi");
		printCurrentSQLConstraints(berater.getCurrentSQLConstraintsList());
		
		// Frage 7
		printLeerzeilen();
		System.out.println(question);
		question = berater.evaluateAndAskNewQuestion("???");
		printCurrentSQLConstraints(berater.getCurrentSQLConstraintsList());
		
		// Frage 8
		printLeerzeilen();
		System.out.println(question);
		question = berater.evaluateAndAskNewQuestion("???");
		printCurrentSQLConstraints(berater.getCurrentSQLConstraintsList());
	}
	
	private static void printCurrentSQLConstraints(Set<SQLConstraint> list) {
		System.out.println("============Ergebnisse soweit============");
		for (SQLConstraint constraint : list) {
			System.out.println(constraint);
		}
	}
	
	private static void printLeerzeilen() {
		System.out.println();
		System.out.println();
		System.out.println();
	}
	
	public void testFrage2A() {

	}

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(Berater1Test.class);
	}
}
