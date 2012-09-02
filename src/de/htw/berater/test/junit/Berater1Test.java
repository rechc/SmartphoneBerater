package de.htw.berater.test.junit;


import java.util.List;

import junit.framework.TestCase;
import de.htw.berater.Berater1;
import de.htw.berater.Customer;
import de.htw.berater.controller.Answer;
import de.htw.berater.controller.Question;
import de.htw.berater.db.DBException;
import de.htw.berater.db.SQLClient;
import de.htw.berater.db.Smartphone;

public class Berater1Test extends TestCase {

	Berater1 berater;
	
	public Berater1Test() {
		this.berater = new Berater1("inferredSmartphones.rdf", "http://semantische-interoperabilitaet-projekt#");
	}
	
	public void testZweck() throws Exception {
		// Datenbank Verbindung
		SQLClient sqlCl = SQLClient.getInstance();
		sqlCl.initialConnection();
		
		
		
		// Frage 1
		Question question = berater.generateQuestion();
		System.out.println(question);
		System.out.println(question.getChoices());
		berater.evaluateAnswer(new Answer("bilder"));
		System.out.println(berater.getSQLString());
		ausgabeDB(sqlCl, berater.getSQLString());
		
		// Frage 2a
		printLeerzeilen();
		question = berater.generateQuestion();
		System.out.println(question);
		berater.evaluateAnswer(new Answer("normales"));
		System.out.println(berater.getSQLString());
		ausgabeDB(sqlCl, berater.getSQLString());
		
		// Frage 2b
//		printLeerzeilen();
//		question = berater.generateQuestion();
//		System.out.println(question);
//		berater.evaluateAnswer(new Answer("gutes"));
//		System.out.println(berater.getSQLString());
		
//		// Frage 3 Fall 1
//		printLeerzeilen();
//		question = berater.generateQuestion();
//		System.out.println(question);
//		question = berater.evaluateAndAskNewQuestion("kleines");
//		System.out.println(berater.getSQLString());
		
		// Frage 3 Fall 2
		printLeerzeilen();
		question = berater.generateQuestion();
		System.out.println(question);
		berater.addCustomerInfo(Customer.SEHBEHINDERT);
		berater.evaluateAnswer(new Answer("großes"));
		System.out.println(berater.getSQLString());
		ausgabeDB(sqlCl, berater.getSQLString());
		if (berater.getContext() == 4) {
			// Frage 4 
			printLeerzeilen();
			question = berater.generateQuestion();
			System.out.println(question);
			berater.evaluateAnswer(new Answer("keinetastatur"));
			System.out.println(berater.getSQLString());
			ausgabeDB(sqlCl, berater.getSQLString());
		} 
		
		// Frage 5
		printLeerzeilen();
		question = berater.generateQuestion();
		System.out.println(question);
		berater.evaluateAnswer(new Answer("outdoor"));
		System.out.println(berater.getSQLString());
		ausgabeDB(sqlCl, berater.getSQLString());
		
		// Frage 6
		printLeerzeilen();
		question = berater.generateQuestion();
		System.out.println(question);
		berater.evaluateAnswer(new Answer("navi"));
		System.out.println(berater.getSQLString());
		ausgabeDB(sqlCl, berater.getSQLString());
		
		// Frage 7
		printLeerzeilen();
		question = berater.generateQuestion();
		System.out.println(question);
		berater.evaluateAnswer(new Answer("???"));
		System.out.println(berater.getSQLString());
		ausgabeDB(sqlCl, berater.getSQLString());
		
		// Frage 8
		printLeerzeilen();
		question = berater.generateQuestion();
		System.out.println(question);
		berater.evaluateAnswer(new Answer("???"));
		System.out.println(berater.getSQLString());
		ausgabeDB(sqlCl, berater.getSQLString());
		
		
		//Connection close
		sqlCl.closeConnection();
	}
	
	private static void printLeerzeilen() {
		System.out.println("\n");
	}
	
	private void ausgabeDB(SQLClient sqlcl , String sql){
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
		junit.swingui.TestRunner.run(Berater1Test.class);
	}
}
