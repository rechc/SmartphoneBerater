package de.htw.berater.controller;

import java.util.List;

import de.htw.berater.Berater;
import de.htw.berater.db.DBException;
import de.htw.berater.db.Smartphone;
import de.htw.berater.db.SQLClient;
import de.htw.berater.ui.BeraterUI;

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
		String newQuestion = berater.askFirstQuestion();
		informUI(newQuestion);
	}
	
	public void answer(String keyWord) throws FalseAnswerException {
		if (berater.expectsYesNoAnswer()) {
			throw new FalseAnswerException(Answer.YESNO);
		}
		String newQuestion = berater.evaluateAndAskNewQuestion(keyWord);
		informUI(newQuestion);
	}
	
	public void answer(boolean yes) throws FalseAnswerException {
		if (berater.expectsKeywordAnswer()) {
			throw new FalseAnswerException(Answer.KEYWORD);
		}
		String newQuestion = berater.evaluateAndAskNewQuestion(yes);
		informUI(newQuestion);
	}
	
	private void informUI(String newQuestion) {
		String sql = berater.getSQLString();
		List<Smartphone> resultData;
		try {
			resultData = SQLClient.getInstance().getSmartphones(sql);
			beraterUI.onNewData(resultData);
			beraterUI.onNewQuestion(newQuestion);
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
