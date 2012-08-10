package de.htw.berater;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import de.htw.berater.controller.Answer;
import de.htw.berater.db.SQLConstraint;

public abstract class Berater {
	
	protected String rdfPath;
	protected String ns;
	protected List<SQLConstraint> currentSQLConstraints = new LinkedList<SQLConstraint>();
	protected int context; //irgendwie den kontext beachten um sinnvoll die naechste frage zu stellen
	protected Answer nextAnswer;
	protected Customer customer;
	protected OntModel  model;
	
	public Berater(String rdfPath, String ns) {
		this.rdfPath = rdfPath;
		this.ns = ns;
	
		if ( System.getProperty("log4j.configuration") == null )	{
			System.setProperty("log4j.configuration", "jena-log4j.properties") ;    		
		}
            	   
		model = ModelFactory.createOntologyModel();
		
		model.read("file:" + rdfPath);
	}
	
	public abstract String evaluateAndAskNewQuestion(String string);

	public abstract String evaluateAndAskNewQuestion(boolean yes);

	public final void reset() {
	
	}

	public final List<SQLConstraint> getCurrentSQLConstraintsList() {
		return currentSQLConstraints;
	}

	public final boolean expectsYesNoAnswer() {
		return nextAnswer == Answer.YESNO;
	}
	
	protected boolean isCoveringAxiom(OntClass ontClass) {
		boolean isCoveringAxiom = false;
		for (Iterator<OntClass> supers = ontClass.listSuperClasses(true); supers.hasNext(); ) {
			OntClass superClass = supers.next();
			if (superClass.isUnionClass()) {
				isCoveringAxiom = true;
				for (Iterator<?> it = superClass.asUnionClass().listOperands(); it.hasNext(); ) {
					OntClass op = (OntClass) it.next();

					if (!op.isRestriction()) {
						boolean found = false;
						ExtendedIterator<OntClass> it2 = ontClass.listSubClasses();
						while (it2.hasNext()) {
							if (it2.next().equals(op)) {
								found = true;
							}
						}
						if (!found) {
							isCoveringAxiom = false;
						}
					}
				}
				if (isCoveringAxiom) {
					return true;
				}
			}
		}
		return false;
	}

	public final boolean expectsKeywordAnswer() {
		return nextAnswer == Answer.KEYWORD;
	}

	/*real super classes, no properties*/
	protected final List<OntClass> getSuperclasses() {
		return null;
	}
	
	/*hatEigenschaft some AppstoreEigenscahft*/
	protected final List<Restriction> getRestrictions(OntClass ontClass) {
		return null;
	}
	
	
	/* noch mehr allgemeine Methoden ??*/
	public abstract String askFirstQuestionZweck(); //Szenario1
	
	public abstract String askFirstQuestionGeneral(); //Szenario2
}
