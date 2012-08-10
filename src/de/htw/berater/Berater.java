package de.htw.berater;

import java.util.LinkedList;
import java.util.List;

import com.hp.hpl.jena.ontology.OntClass;

import de.htw.berater.db.SQLConstraint;

public abstract class Berater {
	
	String rdfPath;
	String ns;
	List<OntClass> smartphonesDone = new LinkedList<OntClass>();
	
	public Berater(String rdfPath, String ns) {
		this.rdfPath = rdfPath;
		this.ns = ns;
	}
	
	public abstract String evaluateAndAskNewQuestion(String string);

	public abstract String evaluateAndAskNewQuestion(boolean yes);

	public abstract void reset();

	public abstract List<SQLConstraint> getCurrentSQLConstraintsList();

	public abstract boolean expectsYesNoAnswer();

	public abstract boolean expectsKeywordAnswer();

	public abstract String askFirstQuestionZweck(); //Szenario1
	
	public abstract String askFirstQuestionGeneral(); //Szenario2
}
