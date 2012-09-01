package de.htw.berater.controller;

import java.util.List;

import de.htw.berater.Berater;
import de.htw.berater.db.DBException;
import de.htw.berater.db.Smartphone;
import de.htw.berater.db.SQLClient;
import de.htw.berater.ui.BeraterUI;

/**
 * 
 *	Vermittler zwischen GUI und Berater
 */
public class Controller {
	private Berater berater;
	private Berater berater1;
	private Berater berater2;
	
	private BeraterUI beraterUI;
	
	public Controller(BeraterUI beraterUI, Berater berater1, Berater berater2) {
		this.beraterUI = beraterUI;
		this.berater1 = berater1;
		this.berater2 = berater2;
	}
	
	
	public void finish() {
		berater.reset();
	}
	
	public void getFirstQuestion(boolean szenario1) {
		this.berater = szenario1 ? berater1 : berater2;
		Question question = berater.generateQuestion();
		informUI(question);
	}
	
	public void answer(Answer answer) {
		try {
			berater.evaluateAnswer(answer);
			Question question = berater.generateQuestion();
			informUI(question);
		} catch (DBException e) {
			e.printStackTrace();
		}
	}
	
	private void informUI(Question question) {
		Thread thread = new Thread() {
			@Override
			public void run(){
				try{
					String sql = berater.getSQLString();
					List<Smartphone> resultData;
					System.out.println(sql);
					SQLClient sqlc = SQLClient.getInstance();
					sqlc.initialConnection();
					resultData = sqlc.getSmartphones(sql);
					sqlc.closeConnection();
					beraterUI.onNewData(resultData);
				} catch (DBException e){
					e.printStackTrace();
				}
			}
		};
		
		beraterUI.onNewQuestion(question);
		thread.start();
		
	}
}
