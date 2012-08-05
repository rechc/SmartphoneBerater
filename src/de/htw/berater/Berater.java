package de.htw.berater;

import java.util.List;

import de.htw.berater.db.SQLConstraint;

public interface Berater {

	void init();

	String askFirstQuestion(String constraint);

	String evaluateAndAskNewQuestion(String string);

	void reset();

	List<SQLConstraint> getCurrentSQLConstraintsList();
}
