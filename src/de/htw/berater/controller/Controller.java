package de.htw.berater.controller;

import java.util.List;
import java.util.Set;

import de.htw.berater.Berater;
import de.htw.berater.db.ResultData;
import de.htw.berater.db.SQLClient;
import de.htw.berater.db.SQLConstraint;
import de.htw.berater.ui.BeraterUI;

public class Controller {
	private Berater berater;
	private BeraterUI beraterUI;
	
	public Controller(BeraterUI beraterUI) {
		this.beraterUI = beraterUI;
	}
	
	public void setBerater(Berater berater) {
		this.berater = berater;
	}
	
	public void finish() {
		berater.reset();
	}
	
	public void getFirstQuestion(boolean szenario1) {
		String newQuestion = null;
		if (szenario1) {
			newQuestion = berater.askFirstQuestionZweck(); //init für Szenario1
		} else {
			newQuestion = berater.askFirstQuestionGeneral(); //init für Szenario2
		}
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
		Set<SQLConstraint> sqlConstraints = berater.getCurrentSQLConstraintsList();
		List<ResultData> resultData = SQLClient.getInstance().getResultData(sqlConstraints);
		beraterUI.onNewData(resultData);
		beraterUI.onNewQuestion(newQuestion);
	}
}
