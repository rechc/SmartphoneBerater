package de.htw.berater.test.junit;

import junit.framework.TestCase;
import de.htw.berater.Berater;
import de.htw.berater.StaticFactory;

public class Berater2Test extends TestCase {

	Berater berater;
	
	public Berater2Test() {
		this.berater = StaticFactory.getNewBerater2("inferredSmartphones.rdf", "http://semantische-interoperabilitaet-projekt#");
	}
	
	public void testZweck() {
		// Frage 1
		System.out.println(berater.askFirstQuestion());
		String question = berater.evaluateAndAskNewQuestion("ProfiSmartphone");
		System.out.println(berater.getSQLString());
		
		// Frage 2
		printLeerzeilen();
		System.out.println(question);
		question = berater.evaluateAndAskNewQuestion("GrosserSpeicherSmartphone");
		System.out.println(berater.getSQLString());
		
		// Frage 3
		printLeerzeilen();
		System.out.println(question);
		// a)
		System.out.println("nein:\n --> kein neuer filter");
		// b)
		System.out.println("ja:");
		question = berater.evaluateAndAskNewQuestion("GroßesSmartphone");
		System.out.println(berater.getSQLString());
		
		// Frage 4
		printLeerzeilen();
		System.out.println(question);
		question = berater.evaluateAndAskNewQuestion("KeineTastaturSmartphone");
		System.out.println(berater.getSQLString());
		
		// Frage 5
		printLeerzeilen();
		System.out.println(question);
		question = berater.evaluateAndAskNewQuestion("MultimediaSmartphone");
		System.out.println(berater.getSQLString());
		
		// Frage 6
		printLeerzeilen();
		System.out.println(question);
		question = berater.evaluateAndAskNewQuestion("GuteKameraSmartphone");
		System.out.println(berater.getSQLString());
		
		// Frage 7
		printLeerzeilen();
		System.out.println(question);
		question = berater.evaluateAndAskNewQuestion("AndroidSmartphone");
		System.out.println(berater.getSQLString());
		
		// Frage 8
		printLeerzeilen();
		System.out.println(question);
		question = berater.evaluateAndAskNewQuestion("NaviSmartphone");
		System.out.println(berater.getSQLString());
		
		// Frage 9
//		printLeerzeilen();
//		System.out.println(question);
//		question = berater.evaluateAndAskNewQuestion("???");
//		System.out.println(berater.getSQLString());
		
		// Frage 10
//		printLeerzeilen();
//		System.out.println(question);
//		question = berater.evaluateAndAskNewQuestion("NaviSmartphone");
//		System.out.println(berater.getSQLString());
	}
		

	private static void printLeerzeilen() {
		System.out.println();
		System.out.println();
		System.out.println();
	}

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(Berater2Test.class);
	}
}
