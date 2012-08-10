package de.htw.berater;

import java.util.List;

import de.htw.berater.db.SQLConstraint;

public class Berater1 extends Berater {

	public Berater1(String rdfPath, String ns) {
		super(rdfPath, ns);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String evaluateAndAskNewQuestion(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String evaluateAndAskNewQuestion(boolean yes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<SQLConstraint> getCurrentSQLConstraintsList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean expectsYesNoAnswer() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean expectsKeywordAnswer() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String askFirstQuestionZweck() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String askFirstQuestionGeneral() {
		// TODO Auto-generated method stub
		return null;
	}

}
