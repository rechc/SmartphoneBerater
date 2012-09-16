package de.htw.berater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.hp.hpl.jena.ontology.ComplementClass;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.rdf.model.RDFList;
import com.hp.hpl.jena.rdf.model.RDFNode;
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

	private int found;

	public Berater1(String rdfPath, String ns) {
		super(rdfPath, ns, true);
	}

	@Override
	public void evaluateAnswer(Answer answer) throws Exception {
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
			if (answer.getValues().size() > 0)
				displaySmartphone(answer.getValues().get(0));
			else
				throw new Exception("Keine Displaygroesse ausgewaehlt.");
			break;
		case 4:
			touchBedinung(string.equals("Ja") ? true : false);
			break;
		case 5:
			outdoorSmartphone(string);
			break;
		case 6:
			navigationSmartphone(string.equals("Ja") ? true : false);
			break;
		case 7:
			kameraSmartphone(string);
			break;
		case 8:
			smartphoneMarke(string);
			break;
		default:
			throw new IllegalStateException("Berater ist bereits am Ende.");
		}
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
				setCurrentProperties(smphone);
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
					setCurrentProperties(rememberList.get(i));
					found = true;
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

	private void displaySmartphone(String display) throws Exception {
		OntClass displaySmartphone = searchClassContaining(display, "Smartphone");
		if (display != null)
			setCurrentProperties(displaySmartphone);
		if (customer.isCustomer(Customer.SEHBEHINDERT)) {
			if (isSmartphoneOkForCustumer(displaySmartphone, Customer.SEHBEHINDERT)) {
				OntClass tastaturSmartphone = searchClassContaining("Tastatur", "Smartphone");
				if (!isSmartphoneOkForCustumer(tastaturSmartphone, Customer.SEHBEHINDERT)) {
					OntClass tmpClass = model.createClass("TmpSmartphone");

					List<OntClass> complements = new LinkedList<OntClass>();
					for (OntClass property : getClassProperties(tastaturSmartphone)) {
						ComplementClass cc = model.createComplementClass(null, property);
						complements.add(cc);
					}
					RDFList inList = model.createList(complements.toArray(new RDFNode[0]));
					OntClass intersectionClass = model.createIntersectionClass(null, inList);
					tmpClass.addSuperClass(intersectionClass);
					setCurrentProperties(tmpClass);
					
					System.out.println("es wird eine Frage übersprungen: Möchten Sie ein reines Touchdisplay oder eine zusätzliche Hardwaretastatur?");
					context = 5;
					nextQuestion = questionUsage();
					return;
				}
			} else {
				removeSQLConstraints(displaySmartphone);
				customer.removeCustomerInfo(Customer.SEHBEHINDERT);
				nextQuestion = questionDisplaySize();
				context = 3;
				throw new Exception("Sie können kein kleines Smartphone wählen, wenn Sie sehbehindert sind. Daher wiederhole ich die letzte Frage nochmal");
			}
		}
		context = 4;
		nextQuestion = new Question(
				"Möchten Sie ein reines Touchdisplay oder eine zusätzliche Hardwaretastatur?",
				ChoicesBuilder.yesNo("Ein Gerät mit integrierter Tastatur", "Ein Gerät ohne Hardware-Tastatur"));
	}

	private void touchBedinung(boolean withKeyboard) {
		List<OntClass> properties = new LinkedList<OntClass>();
		OntClass subClassOfInterest = searchClassContaining("Tastatur", "Smartphone");
		properties.addAll(getClassProperties(subClassOfInterest));
		OntClass tmpClass = model.createClass("TmpSmartphone");

		if (!withKeyboard) {
			List<OntClass> complements = new LinkedList<OntClass>();
			for (OntClass property : properties) {
				ComplementClass cc = model.createComplementClass(null, property);
				complements.add(cc);
			}
			RDFList inList = model.createList(complements.toArray(new RDFNode[0]));
			OntClass intersectionClass = model.createIntersectionClass(null, inList);
			tmpClass.addSuperClass(intersectionClass);
		} else {
			RDFList inList = model.createList(properties.toArray(new RDFNode[0]));
			OntClass unionClass = model.createUnionClass(null, inList);
			tmpClass.addSuperClass(unionClass);
		}
		
		setCurrentProperties(tmpClass);

		context = 5;
		nextQuestion = questionUsage();
	}

	private void outdoorSmartphone(String outdoor) throws DBException {		
		List<OntClass> outdoorSmartphones = findClass(outdoor.toLowerCase());
		if (outdoor.equals("Outdoor")) {
			List<OntClass> properties = setCurrentProperties(outdoorSmartphones.get(0));

			for (OntClass property : properties) {
				searchForCustomer("Sportler", property);	
			}

			int tempFound = found;
			OntClass smartphone = model.getOntClass(ns + "Smartphone");
			ExtendedIterator<OntClass> ri = smartphone.listSubClasses();
			List<OntClass> result = new ArrayList<OntClass>();
			while (ri.hasNext()) {
				OntClass subClass = ri.next();
				properties = getProperties(subClass);
				for (OntClass property : properties) {
					searchForCustomer("Sportler", property);	
				}
				if (tempFound != found) {
					result.add(subClass);
					found--;
				}
			}

			result.remove(outdoorSmartphones.get(0));
			for (OntClass x : result) {
				String one = x.getLocalName();
				String two = outdoorSmartphones.get(0).getSubClass().getLocalName();
				if (one.contains(two)) {
					result.remove(x);
				}
			}
			context = 6;
			nextQuestion = new Question(
					"Möchten Sie ein " + result.get(0).getLocalName() + "?",
					ChoicesBuilder.yesNo("Ja", "Nein"));
		} else {
			if (outdoor.equals("Business")) {
				OntClass smartphone = searchClassContaining("Business", "Smartphone");
				if (smartphone != null) {
					setCurrentProperties(smartphone);
				}
			}
			context = 7;
			nextQuestion = questionCamera();
		}

//		if (context != 6) {
//			context = 6;
//			nextQuestion = new Question(
//					"Möchten Sie das Smartphone zur Navigation verwenden?",
//					ChoicesBuilder.yesNo("Ja", "Nein"));
//		}
	}

	private void searchForCustomer(String customer, OntClass property) throws DBException {
		if (property.isRestriction()) {
			ReadableProperty constraint = getReadablePropertyFromRestriction(property
					.asRestriction());
			if (constraint.getKey().equals("fuerKunde")) {
				if (constraint.getValue().toString().contains(customer)) {
					found++;
				} 
			}
		} else {
			if (property.isIntersectionClass()) {
				for (Iterator<? extends OntClass> it = property.asIntersectionClass()
						.listOperands(); it.hasNext();) {
					OntClass op = it.next();
					searchForCustomer(customer, op);
				}
			} else {
				if (property.isUnionClass()) {
					for (Iterator<? extends OntClass> it = property.asUnionClass()
							.listOperands(); it.hasNext();) {
						OntClass op = it.next();
						searchForCustomer(customer, op);
					}
				} else {
					if (property.isComplementClass()) {
						for (Iterator<? extends OntClass> it = property.asComplementClass()
								.listOperands(); it.hasNext();) {
							OntClass op = it.next();
							searchForCustomer(customer, op);	
						}
					}
				}
			}
		}
	}

	private void navigationSmartphone(boolean yes) {
		if (yes) {
			List<OntClass> kamera = findClass("navi");
			setProperties(kamera);
		}
		context = 7;
		nextQuestion = questionCamera();
	}

	private void kameraSmartphone(String answer) {
		if (answer.indexOf("GuteKamera") != -1) {
			List<OntClass> kamera = findClass(answer.toLowerCase());
			setProperties(kamera);
		} else if (answer.indexOf("Kamera") != -1) {
			List<OntClass> kamera = findClassEquals(answer.toLowerCase() + "smartphone");
			setProperties(kamera);
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
		if (marke.equals("nein")) {
			setBrand("");
		} else {
			setBrand(marke);
		}
		context = 9;
	}
	
	private List<OntClass> findClass(String search) {
		OntClass smartphone = model.getOntClass(ns + "Smartphone");
		ExtendedIterator<OntClass> ri = smartphone.listSubClasses();
		List<OntClass> result = new ArrayList<OntClass>();
		while (ri.hasNext()) {
			OntClass subClass = ri.next();
			if (subClass.getLocalName().toLowerCase().contains(search)) {
				result.add(subClass);
			}
		}
		return result;
	}
	
	private List<OntClass> findClassEquals(String search) {
		OntClass smartphone = model.getOntClass(ns + "Smartphone");
		ExtendedIterator<OntClass> ri = smartphone.listSubClasses();
		List<OntClass> result = new ArrayList<OntClass>();
		while (ri.hasNext()) {
			OntClass subClass = ri.next();
			if (subClass.getLocalName().toLowerCase().equals(search)) {
				result.add(subClass);
			}
		}
		return result;
	}
	
	private void setProperties(List<OntClass> result) {
		for (int i = 0; i < result.size(); i++) {
			setCurrentProperties(result.get(i));
		}
	}

	@Override
	public Question firstSpecificQuestion() {
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
		.add("Ich benutze das Gerät nur privat.", "Privat", ChoiceType.RADIO)
		.add("Ich benutze das Gerät auch beim Sport.", "Outdoor", ChoiceType.RADIO)
		.add("Ich benutze das Gerät geschäftlich.", "Business", ChoiceType.RADIO)
		.build();
		return new Question(
				"Nutzen Sie das Gerät eher für geschäftliche Zwecke oder in ihrer Freizeit?",
				choices);
	}
	
	private Question questionCamera() {
		HashMap<Integer, List<Choice>> choices = new ChoicesBuilder()
		.add("Ja, eine Kamera wäre toll.", "Kamera", ChoiceType.RADIO)
		.add("Ja, ich brauche eine gute Kamera.", "GuteKamera", ChoiceType.RADIO)
		.add("Nein, eine Kamera brauch ich nicht.", "Nein", ChoiceType.RADIO)
		.build();
		return new Question(
				"Nutzen Sie das Smartphone auch als Kamera?",
				choices);
	}

	public int getContext() {
		return context;
	}
}
