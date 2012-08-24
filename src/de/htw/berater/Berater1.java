package de.htw.berater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.util.iterator.Map1;

import de.htw.berater.controller.Answer;
import de.htw.berater.controller.Choice;
import de.htw.berater.controller.ChoiceType;
import de.htw.berater.controller.ChoicesBuilder;
import de.htw.berater.controller.Question;
import de.htw.berater.db.DBException;
import de.htw.berater.db.SQLClient;

public class Berater1 extends Berater {

	
	

	public Berater1(String rdfPath, String ns) {
		super(rdfPath, ns);
	}

	@Override
	public void evaluateAnswer(Answer answer) throws DBException {
		String string = answer.getSingleValue();

		if (answer.getValues().contains(Customer.SEHBEHINDERT + "")) {
			customer.addCustomerInfo(Customer.SEHBEHINDERT);
			answer.getValues().remove(Customer.SEHBEHINDERT + "");
		}
		switch (context) {
		case 1:
			smartphoneZweck(string);
			break;
		case 2:
			spieleSmartphone(answer.getValues());
			break;
		case 3:
			displaySmartphone(answer.getValues().get(0));
			break;
		case 4:
			touchBedinung(string);
			break;
		case 5:
			outdoorSmartphone(string);
			break;
		case 6:
			navigationSmartphone(string.equals("Ja") ? true : false);
			break;
		case 7:
			kameraSmartphone(string.equals("Ja") ? true : false);
			break;
		case 8:
			smartphoneMarke(string);
			break;
		default:
			throw new IllegalStateException("Unknown context.");
		}
	}



	protected List<OntClass> getClassesWithProperty(String property) {
		OntClass smartphone = model.getOntClass(ns + "Smartphone");
		ExtendedIterator<OntClass> ri = smartphone.listSubClasses();
		List<OntClass> smartphones = new ArrayList<OntClass>();
		while (ri.hasNext()) {
			OntClass subClass = ri.next();
			List<Restriction> restrictions = getRestrictionsDeep(subClass);
			for (Restriction restriction : restrictions) {
				if (restriction.getOnProperty().getLocalName().contains(property)) {
					if (restriction.isSomeValuesFromRestriction()) {
						if (!smartphones.contains(subClass)) {
							smartphones.add(subClass);
						}
					} else {
						throw new RuntimeException("not defined in ontology");
					}
				}
			}
		}
		return smartphones;
	}
	
	private void smartphoneZweck(String string) {

		OntClass zweckSubClass = searchClassContaining(string, "Zweck");
		List<OntClass> smartphonesWithHatZweck = getClassesWithProperty("hatZweck");
		List<OntClass> smartphonesWithHatZweckOnZweckKeyword = new ArrayList<OntClass>();
		for (OntClass clazz: smartphonesWithHatZweck) {
			List<Restriction> restrictions = getRestrictionsDeep(clazz);
			for (Restriction restriction : restrictions) {
				OntClass range = restriction
						.asSomeValuesFromRestriction()
						.getSomeValuesFrom().as(OntClass.class);
				if (range.equals(zweckSubClass)) {
					smartphonesWithHatZweckOnZweckKeyword.add(clazz);
				}
			}
		}
		List<List<OntClass>> classesCoveringAxiomsResolved = null;
		boolean abstractClassSomewhere = false;
		for (OntClass smphone : smartphonesWithHatZweckOnZweckKeyword) {
			if (isCoveringAxiom(smphone)) {
				abstractClassSomewhere = true;
				classesCoveringAxiomsResolved = getCoveringAxiomClasses(smartphonesWithHatZweckOnZweckKeyword);
				break;
			}
		}

		String question = "";
		if (abstractClassSomewhere) {
			ChoicesBuilder cb = new ChoicesBuilder();
			question = "Möchten sie ";
			int group = 0;
			rememberList.clear();
			if (classesCoveringAxiomsResolved == null) throw new RuntimeException("Ontologie geaendert");
			for (int i = 0; i < classesCoveringAxiomsResolved.size(); i++) {
				List<OntClass> classes = classesCoveringAxiomsResolved.get(i);
				for (OntClass smphone : classes) {
					question += smphone.getLocalName() + ", ";
					rememberList.add(smphone);
					if (i == classesCoveringAxiomsResolved.size() - 1)
						cb.add("Ein " + smphone.getLocalName(), smphone.getLocalName(), ChoiceType.CHECK, group);
					else
						cb.add("Ein " + smphone.getLocalName(), smphone.getLocalName(), ChoiceType.RADIO, group);
					setCurrentProperties(smphone);
				}
				group++;
			}
			question += "?";
			question = question.replace(", ?", "?");
			context = 2;
			nextQuestion = new Question(question, cb.build());
		} else {
			// dieser fall ist nicht im Szenario enthalten, aber egal. Funktioniert auch für BilderMachenZweck
			for (int i = 0; i < smartphonesWithHatZweckOnZweckKeyword.size(); i++) {
				setCurrentProperties(smartphonesWithHatZweckOnZweckKeyword.get(i));
			}
			context = 3;
			nextQuestion = questionDisplaySize();
		}
	}

	private void spieleSmartphone(List<String> list) throws DBException {
		for (int i = 0; i < rememberList.size(); i++) {
			boolean found = false;
			for (String keyword : list) {
				if (rememberList.get(i).getLocalName().toLowerCase()
						.contains(keyword.toLowerCase())) {
					found = true;
					break;
				}
			}
			if (!found) {
				removeSQLConstraints(rememberList.get(i));
			}
		}

		rememberList.clear();
		setCustomerInfo();

		context = 3;
		nextQuestion = questionDisplaySize();
	}

	private void displaySmartphone(String display) throws DBException {
		OntClass displaySmartphone = searchClassContaining(display, "Smartphone");
		if (display != null)
			setCurrentProperties(displaySmartphone);
		if (!customer.isCustomer(Customer.SEHBEHINDERT)) {
			context = 4;
			HashMap<Integer, List<Choice>> choices = new ChoicesBuilder()
					.add("Ein Gerät ohne Hardware-Tastatur", "KeineTastatur", ChoiceType.RADIO)
					.add("Ein Gerät mit integrierter Tastatur", "Tastatur", ChoiceType.RADIO)
					.build();
			nextQuestion = new Question(
					"Möchten Sie ein reines Touchdisplay oder eine zusätzliche Hardwaretastatur?",
					choices);
		} else {
			if (isSmartphoneOkForCustumer(displaySmartphone, Customer.SEHBEHINDERT)) {
				System.out.println("es wird eine Frage übersprungen: Möchten Sie ein reines Touchdisplay oder eine zusätzliche Hardwaretastatur?");
				context = 5;
				nextQuestion = questionUsage();
			} else {
				removeSQLConstraints(displaySmartphone);
				customer.removeCustomerInfo(Customer.SEHBEHINDERT);
				nextQuestion = questionDisplaySize();
				context = 3;
				System.out.println("Sie können kein kleines sm holen, wenn Sie sehbehindert sind. OMG! Daher wiederhole ich die letzte Frage nochmal");
			}
		}
	}

	

	private void touchBedinung(String touch) {
		OntClass subClassOfInterest = searchClassContaining(touch, "Smartphone"); //Hier muessen die unterklassen gefunden werden. IosSmartphone konkret. ansonsten ist das so falsch.
		setCurrentProperties(subClassOfInterest);
		context = 5;
		nextQuestion = questionUsage();
	}

	private void outdoorSmartphone(String outdoor) {
		OntClass smartphone = model.getOntClass(ns + "Smartphone");
		ExtendedIterator<OntClass> ri = smartphone.listSubClasses();
		List<OntClass> outdoorSmartphones = new ArrayList<OntClass>(); //Outdoorsmartphon wird nicht gefunden.
		while (ri.hasNext()) {
			OntClass subClass = ri.next();
			if (subClass.getLocalName().toLowerCase().contains(outdoor.toLowerCase())) {
				outdoorSmartphones.add(subClass);
			}
		}

		for (int i = 0; i < outdoorSmartphones.size(); i++) {
			setCurrentProperties(outdoorSmartphones.get(i));
		}

		context = 6;
		nextQuestion = new Question(
				"Möchten Sie das Smartphone zur Navigation oder zur Aufzeichnung ihrer sportlichen Aktivitäten verwenden?",
				ChoicesBuilder.yesNo("ahjo...", "eher net so"));
	}

	private void navigationSmartphone(boolean yes) {
		if (yes) {
			OntClass smartphone = model.getOntClass(ns + "Smartphone");
			ExtendedIterator<OntClass> ri = smartphone.listSubClasses();
			List<OntClass> naviSmartphones = new ArrayList<OntClass>();
			while (ri.hasNext()) {
				OntClass subClass = ri.next();
				if (subClass.getLocalName().toLowerCase().contains("navi")) {
					naviSmartphones.add(subClass);
				}
			}
	
			for (int i = 0; i < naviSmartphones.size(); i++) {
				setCurrentProperties(naviSmartphones.get(i));
			}
		}

		context = 7;
		nextQuestion = new Question(
				"Nutzen Sie das Smartphone auch als Kamera?",
				ChoicesBuilder.yesNo("jo, was feur eine dumme Frage!?", "Neee"));
	}

	private void kameraSmartphone(boolean yes) {
		if (yes) {
			//TODO Kamerasmartphone oder was??
		}
		context = 8;
		List<Choice> choices = new ArrayList<Choice>();
		choices.add(new Choice("Nein", "nein", ChoiceType.RADIO));
		try {
			for (String brand : SQLClient.getInstance().getBrands())
				choices.add(new Choice("Ja, " + brand, brand, ChoiceType.RADIO));
		} catch (DBException ex) {
			ex.printStackTrace();
			// Pech gehabt.
		}
		HashMap<Integer, List<Choice>> choicesGroupMap = new HashMap<Integer, List<Choice>>();
		choicesGroupMap.put(0, choices);
		nextQuestion = new Question(
				"Bevorzugen Sie einen bestimmten Hersteller?",
				choicesGroupMap);
	}

	private void smartphoneMarke(String marke) {
		context = 9;
	}

	@Override
	public Question firstQuestion() {
		context = 1;
		OntClass zweckClass = model.getOntClass(ns + "Zweck");
		List<Choice> choices = zweckClass.listSubClasses()
				.mapWith(new Map1<OntClass, Choice>() {
					@Override
					public Choice map1(OntClass o) {
						String value = o.getLocalName();
						String text = value.replace("Zweck", "")
								.replaceAll("([^A-Z])([A-Z])", "$1 $2");
						return new Choice(text, value, ChoiceType.RADIO);
					}
				}).toList();
		HashMap<Integer, List<Choice>> choicesMap = new HashMap<Integer, List<Choice>>();
		choicesMap.put(0, choices);
		return new Question("Für welchen Zweck benötigen Sie ein Smartphone?", choicesMap);
	}

	private Question questionDisplaySize() {
		List<OntClass> smartphonesWithHatDisplaygroesse = getClassesWithProperty("hatDisplaygroesse");
		ChoicesBuilder cb = new ChoicesBuilder();
		for (OntClass clazz : smartphonesWithHatDisplaygroesse) {
			cb.add(clazz.getLocalName(), clazz.getLocalName(), ChoiceType.RADIO);
		}
		cb.add("Sie sind sehbehindert.", Customer.SEHBEHINDERT + "", ChoiceType.CHECK);
		return new Question(
				"Wie groß soll das Display des Geräts sein?",
				cb.build());
	}

	private Question questionUsage() {
		HashMap<Integer, List<Choice>> choices = new ChoicesBuilder()
				.add("Ich benutze das Gerät nur privat.", "?", ChoiceType.RADIO)
				.add("Ich benutze das Gerät auch beim Sport.", "Outdoor", ChoiceType.RADIO)
				.add("Ich benutze das Gerät geschäftlich.", "Business", ChoiceType.RADIO)
				.build();
		return new Question(
				"Nutzen Sie das Gerät eher für geschäftliche Zwecke oder in ihrer Freizeit?",
				choices);
	}

	public int getContext() {
		return context;
	}
}
