package de.htw.berater;

import java.util.LinkedList;
import java.util.List;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.Restriction;

import de.htw.berater.controller.Answer;
import de.htw.berater.db.SQLConstraint;

public abstract class Berater {
	
	String rdfPath;
	String ns;
	List<OntClass> smartphonesDone = new LinkedList<OntClass>();
	int context; //irgendwie den kontext beachten um sinnvoll die naechste frage zu stellen
	Answer nextAnswer;
	
	public Berater(String rdfPath, String ns) {
		this.rdfPath = rdfPath;
		this.ns = ns;
	}
	
	public abstract String evaluateAndAskNewQuestion(String string);

	public abstract String evaluateAndAskNewQuestion(boolean yes);

	public final void reset() {
	
	}

	public final List<SQLConstraint> getCurrentSQLConstraintsList() {
		return null;
	}

	public final boolean expectsYesNoAnswer() {
		return nextAnswer == Answer.YESNO;
	}

	public final boolean expectsKeywordAnswer() {
		return nextAnswer == Answer.KEYWORD;
	}

	/*real super classes, no properties*/
	public final List<OntClass> getSuperclasses() {
		return null;
	}
	
	/*hatEigenschaft some AppstoreEigenscahft*/
	public final List<Restriction> getRestrictions(OntClass ontClass) {
		return null;
	}
	
	/* noch mehr allgemeine Methoden ??*/
	public abstract String askFirstQuestionZweck(); //Szenario1
	
	public abstract String askFirstQuestionGeneral(); //Szenario2
}
