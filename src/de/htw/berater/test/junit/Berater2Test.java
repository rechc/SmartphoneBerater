package de.htw.berater.test.junit;

import java.util.List;

import junit.framework.TestCase;
import de.htw.berater.Berater;
import de.htw.berater.Berater2;
import de.htw.berater.controller.Answer;
import de.htw.berater.controller.Question;
import de.htw.berater.db.DBException;
import de.htw.berater.db.SQLClient;
import de.htw.berater.db.Smartphone;

public class Berater2Test extends TestCase {

	private Berater berater;

	public Berater2Test() {
		this.berater = new Berater2("inferredSmartphones.rdf",
				"http://semantische-interoperabilitaet-projekt#");
	}

	public void testZweck() throws DBException {
		// Datenbank Verbindung
		SQLClient sqlCl = SQLClient.getInstance();
		sqlCl.initialConnection();

		// Frage 1
		Question question = berater.generateQuestion();
		System.out.println(question);
		berater.evaluateAnswer(new Answer("ProfiSmartphone"));
		System.out.println(berater.getSQLString());
		ausgabeDB(sqlCl, berater.getSQLString());

		// Frage 2
		printLeerzeilen();
		question = berater.generateQuestion();
		System.out.println(question);
		berater.evaluateAnswer(new Answer("GrosserSpeicherSmartphone"));
		System.out.println(berater.getSQLString());
		ausgabeDB(sqlCl, berater.getSQLString());

		// Frage 3
		printLeerzeilen();
		question = berater.generateQuestion();
		System.out.println(question);
		// a)
		System.out.println("nein:\n --> kein neuer filter");
		// b)
		System.out.println("ja:");
		berater.evaluateAnswer(new Answer("GroßesSmartphone"));
		System.out.println(berater.getSQLString());
		ausgabeDB(sqlCl, berater.getSQLString());

		// Frage 4
		printLeerzeilen();
		question = berater.generateQuestion();
		System.out.println(question);
		berater.evaluateAnswer(new Answer("KeineTastaturSmartphone"));
		System.out.println(berater.getSQLString());
		ausgabeDB(sqlCl, berater.getSQLString());

		// Frage 5
		printLeerzeilen();
		question = berater.generateQuestion();
		System.out.println(question);
		berater.evaluateAnswer(new Answer("MultimediaSmartphone"));
		System.out.println(berater.getSQLString());
		ausgabeDB(sqlCl, berater.getSQLString());

		// Frage 6
		printLeerzeilen();
		question = berater.generateQuestion();
		System.out.println(question);
		berater.evaluateAnswer(new Answer("GuteKameraSmartphone"));
		System.out.println(berater.getSQLString());
		ausgabeDB(sqlCl, berater.getSQLString());

		// Frage 7
		printLeerzeilen();
		question = berater.generateQuestion();
		System.out.println(question);
		berater.evaluateAnswer(new Answer("AndroidSmartphone"));
		System.out.println(berater.getSQLString());
		ausgabeDB(sqlCl, berater.getSQLString());

		// Frage 8
		printLeerzeilen();
		question = berater.generateQuestion();
		System.out.println(question);
		berater.evaluateAnswer(new Answer("NaviSmartphone"));
		System.out.println(berater.getSQLString());
		ausgabeDB(sqlCl, berater.getSQLString());

		// Frage 9
		printLeerzeilen();
		question = berater.generateQuestion();
		System.out.println(question);
		berater.evaluateAnswer(new Answer("Samsung"));
		System.out.println(berater.getSQLString());
		ausgabeDB(sqlCl, berater.getSQLString());

		// Frage 10
		printLeerzeilen();
		question = berater.generateQuestion();
		System.out.println(question);
		berater.evaluateAnswer(new Answer("200-400"));
		System.out.println(berater.getSQLString());
		ausgabeDB(sqlCl, berater.getSQLString());

		// Connection close
		sqlCl.closeConnection();
	}

	private static void printLeerzeilen() {
		System.out.println("\n");
	}

	private void ausgabeDB(SQLClient sqlcl, String sql) {
		List<Smartphone> list;
		try {
			list = sqlcl.getSmartphones(sql);
			System.out.println(list);
			System.out.println(list.size());
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(Berater2Test.class);
	}
}
