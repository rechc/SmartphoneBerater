package de.htw.berater;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.util.iterator.Map1;

import de.htw.berater.controller.Answer;
import de.htw.berater.controller.Choice;
import de.htw.berater.controller.ChoicesBuilder;
import de.htw.berater.controller.Question;
import de.htw.berater.controller.QuestionType;
import de.htw.berater.db.DBException;
import de.htw.berater.db.SQLClient;

public class Berater1 extends Berater {

	private List<OntClass> rememberList = new LinkedList<OntClass>();
	private Question nextQuestion;

	public Berater1(String rdfPath, String ns) {
		super(rdfPath, ns);
	}

	@Override
	public void evaluateAnswer(Answer answer) {
		String string = answer.getSingleValue();

		switch (context) {
		case 1:
			smartphoneZweck(string);
			break;
		case 2:
			spieleSmartphone(string);
			break;
		case 3:
			displaySmartphone(string);
			break;
		case 4:
			touchBedinung(string);
			break;
		case 5:
			outdoorSmartphone(string);
			break;
		case 6:
			navigationSmartphone(string);
			break;
		case 7:
			kameraSmartphone(string);
			break;
		case 8:
			smartphoneMarke(string);
			break;
		default:
			throw new IllegalStateException("Unknown context.");
		}
	}

	@Override
	public Question generateQuestion() {
		switch (context) {
		case 0:
			return firstQuestion();
		default:
			return nextQuestion;
		}
	}

	private void smartphoneZweck(String zweck) {
		OntClass zweckClass = model.getOntClass(ns + "Zweck");
		OntClass zweckSubClass = null;
		for (Iterator<OntClass> i = zweckClass.listSubClasses(); i.hasNext();) {
			OntClass subClass = i.next();
			if (subClass.getLocalName().toLowerCase().contains(zweck)) {
				zweckSubClass = subClass;
			}
		}

		OntClass smartphone = model.getOntClass(ns + "Smartphone");
		ExtendedIterator<OntClass> ri = smartphone.listSubClasses();
		List<OntClass> smartphones = new ArrayList<OntClass>();
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
							smartphones.add(subClass);
						}
					}
				}
			}
		}
		List<OntClass> classesCoveringAxiomsResolved = getCoveringAxiomClasses(smartphones);

		String question = "";
		// gab es ein covering axiom (abstrakte klasse)?
		if (!classesCoveringAxiomsResolved.equals(smartphones)) {
			question = "Möchten sie ";
			for (OntClass smphone : classesCoveringAxiomsResolved) {
				question += smphone.getLocalName() + ", ";
			}
			question += "?";
			question = question.replace(", ?", "?");
			context = 2;
			rememberList.clear();
			for (OntClass clazz : classesCoveringAxiomsResolved) {
				if (!smartphones.contains(clazz)) {
					rememberList.add(clazz);
				}
			}
			for (int i = 0; i < classesCoveringAxiomsResolved.size(); i++) {
				setCurrentProperties(classesCoveringAxiomsResolved.get(i));
			}
			nextQuestion = new Question(QuestionType.CHOICE, "Was ist das? " + question, ChoicesBuilder.yesNo("lol", "olol"));
		} else {
			// dieser fall ist nicht in der ontologie enthalten, aber egal. Funktioniert auch für BilderMachenZweck
			for (int i = 0; i < classesCoveringAxiomsResolved.size(); i++) {
				setCurrentProperties(smartphones.get(i));
			}
			context = 3;
			nextQuestion = questionDisplaySize();
		}
	}

	private void spieleSmartphone(String spiele) {
		for (int i = 0; i < rememberList.size(); i++) {
			if (!rememberList.get(i).getLocalName().toLowerCase()
					.contains(spiele.toLowerCase())) {
				removeSQLConstraints(rememberList.get(i));
			}
		}

		rememberList.clear();
		setCustomerInfo();

		context = 3;
		nextQuestion = questionDisplaySize();
	}

	private void displaySmartphone(String display) {
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
			List<Choice> choices = new ChoicesBuilder()
					.add("Ein Gerät ohne Hardware-Tastatur", "KeineTastatur")
					.add("Ein Gerät mit integrierter Tastatur", "Tastatur")
					.build();
			nextQuestion = new Question(
					QuestionType.CHOICE,
					"Möchten Sie ein reines Touchdisplay oder eine zusätzliche Hardwaretastatur?",
					choices);
		} else {
			System.out.println("es wird eine Frage übersprungen: Möchten Sie ein reines Touchdisplay oder eine zusätzliche Hardwaretastatur?");
			context = 5;
			nextQuestion = questionUsage();
		}
	}

	private void touchBedinung(String touch) {
		OntClass subClassOfInterest = searchPhoneClassContaining(touch);
		setCurrentProperties(subClassOfInterest);
		context = 5;
		nextQuestion = questionUsage();
	}

	private void outdoorSmartphone(String outdoor) {
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
		nextQuestion = new Question(
				QuestionType.CHOICE,
				"Möchten Sie das Smartphone zur Navigation oder zur Aufzeichnung ihrer sportlichen Aktivitäten verwenden?",
				ChoicesBuilder.yesNo("navi", "whatever ..."));
	}

	private void navigationSmartphone(String navigation) {
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
		nextQuestion = new Question(
				QuestionType.CHOICE,
				"Nutzen Sie das Smartphone auch als Kamera?",
				ChoicesBuilder.yesNo("kamera", "nix ..."));
	}

	private void kameraSmartphone(String kamera) {
		context = 8;
		List<Choice> choices = new ArrayList<Choice>();
		choices.add(new Choice("Nein", "nein"));
		try {
			for (String brand : SQLClient.getInstance().getBrands())
				choices.add(new Choice("Ja, " + brand, brand));
		} catch (DBException ex) {
			// Pech gehabt.
		}
		nextQuestion = new Question(
				QuestionType.CHOICE,
				"Bevorzugen Sie einen bestimmten Hersteller?",
				choices);
	}

	private void smartphoneMarke(String marke) {
		context = 9;
//		QuestionType.FINISHED;
	}

	private Question firstQuestion() {
		context = 1;
		OntClass zweckClass = model.getOntClass(ns + "Zweck");
		List<Choice> choices = zweckClass.listSubClasses()
				.mapWith(new Map1<OntClass, Choice>() {
					@Override
					public Choice map1(OntClass o) {
						String value = o.getLocalName();
						String text = value.replace("Zweck", "")
								.replaceAll("([^A-Z])([A-Z])", "$1 $2");
						return new Choice(text, value);
					}
				}).toList();
		return new Question(QuestionType.MULTI,
				"Für welchen Zweck benötigen Sie ein Smartphone?", choices);
	}

	private Question questionDisplaySize() {
		List<Choice> choices = new ChoicesBuilder()
				.add("Ich will etwas darauf erkennen können", "Großes")
				.add("Es soll bequem in jede Hosentasche passen", "Kleines")
				.build();
		return new Question(
				QuestionType.CHOICE,
				"Wie groß soll das Display des Geräts sein?",
				choices);
	}

	private Question questionUsage() {
		List<Choice> choices = new ChoicesBuilder()
				.add("Ich benutze das Gerät nur privat.", "?")
				.add("Ich benutze das Gerät auch beim Sport.", "Outdoor")
				.add("Ich benutze das Gerät geschäftlich.", "Business")
				.build();
		return new Question(
				QuestionType.CHOICE,
				"Nutzen Sie das Gerät eher für geschäftliche Zwecke oder in ihrer Freizeit?",
				choices);
	}

	public int getContext() {
		return context;
	}

}
