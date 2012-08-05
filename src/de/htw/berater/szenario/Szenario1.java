package de.htw.berater.szenario;


import java.util.List;

import de.htw.berater.Berater;
import de.htw.berater.StaticFactory;
import de.htw.berater.db.ResultData;
import de.htw.berater.db.SQLClient;
import de.htw.berater.db.SQLConstraint;


public class Szenario1 extends Szenario {
	public Szenario1(Berater berater) {
		super(berater);
	}

	@Override
	public void start() {
		if (beraterUI != null)
			beraterUI.onStart(berater.askFirstQuestion("Zweck"));
	}


	@Override
	public String toString() {
		return "Szenario eins";
	}

	@Override
	public void cases(String answer) {
		switch(progress) {
		case 0:
			if (answer.contains("Spiele")) {
				String newQuestion = berater.evaluateAndAskNewQuestion("spiele");
				List<SQLConstraint> sqlConstraints = berater.getCurrentSQLConstraintsList();
				
				if (beraterUI != null) {
					List<ResultData> resultData = SQLClient.getInstance().getResultData(sqlConstraints);
					beraterUI.show(resultData);
					beraterUI.onNewQuestion(newQuestion);
				}
				progress++;
			} else {
				falseQuestion();
			}
			break;
		case 1:
			if (answer.contains("")) {
				progress++;
			} else {
				falseQuestion();
			}
			break;
		case 2:
			if (answer.contains("")) {
				progress++;
			} else {
				falseQuestion();
			}
			break;
		case 3:
			if (answer.contains("")) {
				progress++;
			} else {
				falseQuestion();
			}
			break;
		case 4:
			if (answer.contains("")) {
				progress++;
			} else {
				falseQuestion();
			}
			break;
		case 5:
			if (answer.contains("")) {
				progress++;
			} else {
				falseQuestion();
			}
			break;
		case 6:
			if (answer.contains("")) {
				progress++;
			} else {
				falseQuestion();
			}
			break;
		case 7:
			if (!answer.contains("Nein")) {
				falseQuestion();
			} else {
				progress++;
			}
		}
	}

	@Override
	public int maxProgress() {
		return 8;
	}
}
