package de.htw.berater.test.junit;

import java.util.Set;

import junit.framework.TestCase;
import de.htw.berater.Berater;
import de.htw.berater.Customer;
import de.htw.berater.SQLConstraint;
import de.htw.berater.StaticFactory;

public class Berater1Test extends TestCase {

	Berater berater;
	
	public Berater1Test() {
		this.berater = StaticFactory.getNewBerater1("inferredSmartphones.rdf", "http://semantische-interoperabilitaet-projekt#");
	}
	
	public void testZweck() {
		// Frage 1
		System.out.println(berater.askFirstQuestionZweck());
		String question = berater.evaluateAndAskNewQuestion("spiele");
		System.out.println(berater.getSQLString());
		
		// Frage 2a
		printLeerzeilen();
		System.out.println(question);
		question = berater.evaluateAndAskNewQuestion("normales");
		System.out.println(berater.getSQLString());
		
		// Frage 2b
		/*printLeerzeilen();
		System.out.println(question);
		question = berater.evaluateAndAskNewQuestion("gutes");
		printCurrentSQLConstraints(berater.getCurrentSQLConstraintsList());*/
		
//		// Frage 3 Fall 1
//		printLeerzeilen();
//		System.out.println(question);
//		question = berater.evaluateAndAskNewQuestion("kleines");
//		System.out.println(berater.getSQLString());
		
		// Frage 3 Fall 2
		printLeerzeilen();
		System.out.println(question);
		berater.addCustomerInfo(Customer.SEHBEHINDERT);
		question = berater.evaluateAndAskNewQuestion("groﬂes");
		System.out.println(berater.getSQLString());
		
		// Frage 4 
		printLeerzeilen();
		System.out.println(question);
		question = berater.evaluateAndAskNewQuestion("???");
		System.out.println(berater.getSQLString());
				
		// Frage 5
		printLeerzeilen();
		System.out.println(question);
		question = berater.evaluateAndAskNewQuestion("outdoor");
		System.out.println(berater.getSQLString());
		
		// Frage 6
		printLeerzeilen();
		System.out.println(question);
		question = berater.evaluateAndAskNewQuestion("navi");
		System.out.println(berater.getSQLString());
		
		// Frage 7
		printLeerzeilen();
		System.out.println(question);
		question = berater.evaluateAndAskNewQuestion("???");
		System.out.println(berater.getSQLString());
		
		// Frage 8
		printLeerzeilen();
		System.out.println(question);
		if (!berater.expectsKeywordAnswer() && !berater.expectsYesNoAnswer()) {
			System.out.println("Berater ist schon fertig, weil wohl eine Frage weggefallen ist");
		} else {
			question = berater.evaluateAndAskNewQuestion("???");
			System.out.println(berater.getSQLString());
		}
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
