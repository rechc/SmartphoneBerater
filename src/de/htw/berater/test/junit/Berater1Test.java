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
		System.out.println(berater.askFirstQuestionZweck());
		System.out.println(berater.evaluateAndAskNewQuestion("spiele"));
		Set<SQLConstraint> list = berater.getCurrentSQLConstraintsList();
		for (SQLConstraint constraint : list) {
			System.out.println(constraint);
		}
	}
	
	public static void main(String[] args) {
		junit.swingui.TestRunner.run(Berater1Test.class);
	}
}
