package de.htw.berater;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

import de.htw.berater.controller.Answer;

public class Berater1 extends Berater {

	private List<OntClass> rememberList = new LinkedList<OntClass>();

	public Berater1(String rdfPath, String ns) {
		super(rdfPath, ns);
	}

	@Override
	public String evaluateAndAskNewQuestion(String string) {
		switch (context) {
		case 1:
			return smartphoneZweck(string);
		case 2:
			return spieleSmartphone(string);
		case 3:
			return displaySmartphone(string);
		case 4:
			return touchBedinung(string);
		case 5:
			return outdoorSmartphone(string);
		case 6:
			return navigationSmartphone(string);
		case 7:
			return kameraSmartphone(string);
		case 8:
			return smartphoneMarke(string);
		}
		throw new RuntimeException("wrong context");
	}

	@Override
	public String evaluateAndAskNewQuestion(boolean yes) {
		switch (context) {
		}
		throw new RuntimeException("wrong context");
	}

	private String smartphoneZweck(String zweck) {
		OntClass zweckClass = model.getOntClass(ns + "Zweck");
		OntClass zweckSubClass = null;
		for (Iterator<OntClass> i = zweckClass.listSubClasses(); i.hasNext();) {
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
				if (restriction.getOnProperty().getLocalName()
						.contains("hatZweck")) {
					// some
					if (restriction.isSomeValuesFromRestriction()) {
						OntClass range = restriction
								.asSomeValuesFromRestriction()
								.getSomeValuesFrom().as(OntClass.class);
						if (range.equals(zweckSubClass)) {
							spielesmartphones.add(subClass);
						}
					}
				}
			}
		}
		List<OntClass> classesCoveringAxiomsResolved = getCoveringAxiomClasses(spielesmartphones);

		String question = "";
		// gab es ein covering axiom (abstrakte klasse)?
		if (!classesCoveringAxiomsResolved.equals(spielesmartphones)) {
			question = "Moechten sie ";
			for (OntClass smphone : classesCoveringAxiomsResolved) {
				question += smphone.getLocalName() + ", ";
			}
			question += "?";
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
			// dieser fall ist nicht in der ontologie enthalten
		}

		for (int i = 0; i < classesCoveringAxiomsResolved.size(); i++) {
			setCurrentProperties(classesCoveringAxiomsResolved.get(i));
		}
		return question;
	}

	private String spieleSmartphone(String spiele) {
		for (int i = 0; i < rememberList.size(); i++) {
			if (!rememberList.get(i).getLocalName().toLowerCase()
					.contains(spiele.toLowerCase())) {
				removeSQLConstraints(rememberList.get(i));
			}
		}

		rememberList.clear();
		setCustomerInfo();
		String question = "Wie groﬂ soll das Display des Ger‰ts sein?";
		nextAnswer = Answer.KEYWORD;
		context = 3;
		return question;
	}

	private String displaySmartphone(String display) {
		OntClass smartphone = model.getOntClass(ns + "Smartphone");
		ExtendedIterator<OntClass> ri = smartphone.listSubClasses();
		List<OntClass> displaySmartphones = new ArrayList<OntClass>();
		while (ri.hasNext()) {
			OntClass subClass = ri.next();
			if (subClass.getLocalName().toLowerCase().contains(display)) {
				displaySmartphones.add(subClass);
			}
		}
		for (int i = 0; i < displaySmartphones.size(); i++) {
			setCurrentProperties(displaySmartphones.get(i));
		}
		if (!customer.isCustomer(Customer.SEHBEHINDERT)) {
			context = 4;
			nextAnswer = Answer.KEYWORD;
			return "Möchten Sie ein reines Touchdisplay oder eine zusätzliche Hardwaretastatur?";
		} else {
			System.out.println("es wird eine Frage ¸bersprungen: Möchten Sie ein reines Touchdisplay oder eine zusätzliche Hardwaretastatur?");
			context = 5;
			nextAnswer = Answer.KEYWORD;
			return "Nutzen Sie das Ger‰t eher f¸r gesch‰ftliche Zwecke oder in ihrer Freizeit f¸r Outdooraktivit‰ten.";
		}
	}

	private String touchBedinung(String touch) {
		OntClass smartphone = model.getOntClass(ns + "Smartphone");
		OntClass subClassOfInterest = null;
		for (Iterator<OntClass> i = smartphone.listSubClasses(); i.hasNext();) {
			OntClass subClass = (OntClass) i.next();
			if (subClass.getLocalName().toLowerCase().contains(touch.toLowerCase())) {
				subClassOfInterest = subClass;
			}
		}
		setCurrentProperties(subClassOfInterest);
		context = 5;
		nextAnswer = Answer.KEYWORD;
		return "Nutzen Sie das Gerät eher für geschäftliche Zwecke oder in ihrer Freizeit?";
	}

	private String outdoorSmartphone(String outdoor) {
		OntClass smartphone = model.getOntClass(ns + "Smartphone");
		ExtendedIterator<OntClass> ri = smartphone.listSubClasses();
		List<OntClass> outdoorSmartphones = new ArrayList<OntClass>();
		while (ri.hasNext()) {
			OntClass subClass = ri.next();
			if (subClass.getLocalName().toLowerCase().contains(outdoor)) {
				outdoorSmartphones.add(subClass);
			}
		}

		for (int i = 0; i < outdoorSmartphones.size(); i++) {
			setCurrentProperties(outdoorSmartphones.get(i));
		}

		context = 6;
		nextAnswer = Answer.KEYWORD;
		return "Möchten Sie das Smartphone zur Navigation oder zur Aufzeichnung ihrer sportlichen Aktivitäten verwenden?";
	}

	private String navigationSmartphone(String navigation) {
		OntClass smartphone = model.getOntClass(ns + "Smartphone");
		ExtendedIterator<OntClass> ri = smartphone.listSubClasses();
		List<OntClass> naviSmartphones = new ArrayList<OntClass>();
		while (ri.hasNext()) {
			OntClass subClass = ri.next();
			if (subClass.getLocalName().toLowerCase().contains(navigation)) {
				naviSmartphones.add(subClass);
			}
		}

		for (int i = 0; i < naviSmartphones.size(); i++) {
			setCurrentProperties(naviSmartphones.get(i));
		}

		context = 7;
		nextAnswer = Answer.KEYWORD;
		return "Nutzen Sie das Smartphone auch als Kamera?";
	}

	private String kameraSmartphone(String kamera) {
		context = 8;
		nextAnswer = Answer.KEYWORD;
		return "Bevorzugen Sie eine bestimmte Marke?";
	}

	private String smartphoneMarke(String marke) {
		context = 9;
		nextAnswer = Answer.FINISHED;
		return "Tsch¸ss";
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
