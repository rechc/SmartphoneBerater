package de.htw.berater;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import de.htw.berater.controller.Answer;
import de.htw.berater.db.SQLConstraint;

public class Berater1 extends Berater {

	private List<OntClass> rememberList = new LinkedList<OntClass>();
	
	public Berater1(String rdfPath, String ns) {
		super(rdfPath, ns);
	}

	@Override
	public String evaluateAndAskNewQuestion(String string) {
		switch (context) {
		case 1:
			return zweck(string);
		case 2:
			return coveringAxiom(string);
		case 3: return "ka";
		}
		throw new RuntimeException("wrong context");
	}

	private String coveringAxiom(String string) {
		for (int i = 0; i < rememberList.size(); i++) {
			if (!rememberList.get(i).getLocalName().toLowerCase().contains(string.toLowerCase())) {
				rememberList.remove(i);
				i--;
			}
		}
		List<SQLConstraint> sqlConstraintsAll = new LinkedList<SQLConstraint>();
		for (int i = 0; i < rememberList.size(); i++) {
			List<SQLConstraint> sqlConstraints = getSQLConstraints(rememberList.get(i));
			sqlConstraintsAll.addAll(sqlConstraints);
		}
		currentSQLConstraints.clear();
		currentSQLConstraints.addAll(sqlConstraintsAll);
		rememberList.clear();
		String question = "Wie groﬂ soll das Display des Ger‰ts sein?";
		nextAnswer = Answer.KEYWORD;
		context = 3;
		return question;
	}

	@Override
	public String evaluateAndAskNewQuestion(boolean yes) {
		switch (context) {
		}
		throw new RuntimeException("wrong context");
	}

	private String zweck(String zweck) {
		OntClass zweckClass = model.getOntClass(ns + "Zweck");
		OntClass zweckSubClass = null;
		for (Iterator<OntClass> i = zweckClass.listSubClasses(); i.hasNext();){
		        OntClass subClass = (OntClass) i.next();
		        if (subClass.getLocalName().toLowerCase().contains(zweck)) {
		        	zweckSubClass = subClass;
		        }
		}
		
		OntClass smartphone = model.getOntClass(ns + "Smartphone");
		ExtendedIterator<OntClass> ri = smartphone.listSubClasses();
		List<OntClass> spielesmartphones = new ArrayList<OntClass>();
		while (ri.hasNext()) {
			OntClass subClass = ri.next();
			List<Restriction> restrictions = getRestrictions(subClass);
			for (Restriction restriction : restrictions) {
				if (restriction.getOnProperty().getLocalName().contains("hatZweck")) {
                	//some
                	if (restriction.isSomeValuesFromRestriction()) {
                		OntClass range = restriction.asSomeValuesFromRestriction().getSomeValuesFrom().as(OntClass.class);
                		if (range.equals(zweckSubClass)) {
                			spielesmartphones.add(subClass);
                		}
                	}
                }
			}
		}
		List<OntClass> classesCoveringAxiomsResolved = getCoveringAxiomClasses(spielesmartphones);
		
		String question = "";
		//gab es ein covering axiom (abstrakte klasse)?
		if (!classesCoveringAxiomsResolved.equals(spielesmartphones)) {
			question = "Moechten sie ";
			for (OntClass smphone : classesCoveringAxiomsResolved) {
				question += smphone.getLocalName() + ", "; 
			}
			question+= "?";
			question = question.replace(", ?", "?");
			context = 2;
			nextAnswer = Answer.KEYWORD;
			rememberList.clear();
			for (OntClass clazz : classesCoveringAxiomsResolved) {
				if (!spielesmartphones.contains(clazz)) {
					rememberList.add(clazz);
				}
			}
		} else {
			//dieser fall ist nicht in der ontologie enthalten
		}
	
		List<SQLConstraint> sqlConstraintsAll = new LinkedList<SQLConstraint>();
		for (int i = 0; i < classesCoveringAxiomsResolved.size(); i++) {
			List<SQLConstraint> sqlConstraints = getSQLConstraints(classesCoveringAxiomsResolved.get(i));
			sqlConstraintsAll.addAll(sqlConstraints);
		}
		currentSQLConstraints.addAll(sqlConstraintsAll);
		
		return question;
	}

	



	@Override
	public String askFirstQuestionZweck() {
		context = 1;
		nextAnswer = Answer.KEYWORD;
		return "Fuer welchen Zweck benoetigen Sie ein Smartphone?";
	}

	@Override
	public String askFirstQuestionGeneral() {
		throw new UnsupportedOperationException();
	}

}
