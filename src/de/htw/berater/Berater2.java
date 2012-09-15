package de.htw.berater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.hp.hpl.jena.ontology.ComplementClass;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.rdf.model.RDFList;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.util.iterator.Filter;
import com.hp.hpl.jena.util.iterator.Map1;

import de.htw.berater.controller.Answer;
import de.htw.berater.controller.Choice;
import de.htw.berater.controller.ChoiceType;
import de.htw.berater.controller.ChoicesBuilder;
import de.htw.berater.controller.Question;
import de.htw.berater.db.DBException;
import de.htw.berater.db.SQLClient;

public class Berater2 extends Berater {

	public Berater2(String rdfPath, String ns) {
		super(rdfPath, ns, true);
	}

	@Override
	public void evaluateAnswer(Answer answer) {
		List<String> answerList = answer.getValues();

		switch (context) {
		case 2:
			largeMemorySmartphone(answerList);
			break;
		case 3:
			usabilityOs(answerList.get(0));
			break;
		case 31:
			smallDisplay(answerList.get(0));
			break;
		case 32:
			osChoice(answerList.get(0));
			break;
		case 4:
//			noKeyboardSmartphone(answerList.get(0));
			noKeyboardSmartphone(answerList.get(0).equals("Ja") ? true : false);
			break;
		default:
			throw new IllegalStateException("Weiter gehts nicht");
		}
	}

	private void largeMemorySmartphone(List<String> answerList) {
		for(String s : answerList) {
			if (!s.equals("GrosserSpeicherSmartphone") && !s.equals("Bedienbarkeit"))
				throw new IllegalStateException("Im Szenario nicht vorgesehen!");
			if (!s.equals("Bedienbarkeit")) {
				OntClass subClassOfInterest = searchClassContaining(s, "Smartphone");
				setCurrentProperties(subClassOfInterest);
			}
		}

		context = 3;
		OntClass osClass = model.getOntClass(ns + "BetriebsystemEigenschaft");
		List<Choice> choices = osClass.listSubClasses()
				.mapWith(new Map1<OntClass, Choice>() {
					@Override
					public Choice map1(OntClass o) {
						String value = o.getLocalName();
						String displayName = value
								.replace("BetriebssystemEigenschaft", "")
								.replace("Betriebssystemeigenschaft", "")
								.replaceAll("([^A-Z])([A-Z])", "$1 $2");
						String text = "Ja. Ich finde " + displayName + " nicht so toll ...";
						return new Choice(text, value, ChoiceType.RADIO);
					}
				}).toList();
		choices.add(new Choice("Nein.", "Nein", ChoiceType.RADIO));
		HashMap<Integer, List<Choice>> choicesMap = new HashMap<Integer, List<Choice>>();
		choicesMap.put(0, choices);
		nextQuestion = new Question(
				"Liegt die schlechte Bedienbarkeit am Betriebssystem?",
				choicesMap);
	}

	private void usabilityOs(final String os) {
		if(os.contains("Nein")) {
			context = 31;
			nextQuestion = new Question("Ist Ihnen das Display zu klein?",
					new ChoicesBuilder()
						.add("Ich möchte ein großes Display", "GroßesSmartphone", ChoiceType.RADIO)
						.add("Ich möchte ein kleines Display", "KleinesSmartphone", ChoiceType.RADIO)
						.add("Mir doch egal!", "lolfail", ChoiceType.RADIO)
						.build());
		} else {
			context = 32;
			OntClass osClass = model.getOntClass(ns + "BetriebsystemEigenschaft");
			List<Choice> choices = osClass.listSubClasses()
					.filterDrop(new Filter<OntClass>() {
						@Override
						public boolean accept(OntClass o) {
							return o.getLocalName().equals(os);
						}
					})
					.mapWith(new Map1<OntClass, Choice>() {
						@Override
						public Choice map1(OntClass o) {
							String value = o.getLocalName();
							String displayName = value
									.replace("BetriebssystemEigenschaft", "")
									.replace("Betriebssystemeigenschaft", "")
									.replaceAll("([^A-Z])([A-Z])", "$1 $2");
							String text = "Ich möchte gerne " + displayName;
							return new Choice(text, value, ChoiceType.RADIO);
						}
					}).toList();
			
			//Filter not wanted OS
			for(Choice c : choices){
//				String className = c.getValue().replace("BetriebssystemEigenschaft", "")  + "Smartphone";
//				System.out.println("search class: " + className);
				OntClass subClassOfInterest = searchClassContaining(c.getValue(), "Eigenschaften");
				setCurrentProperties(subClassOfInterest);
			}
			
			choices.add(new Choice("Egal.", "lolfail", ChoiceType.RADIO));
			HashMap<Integer, List<Choice>> choicesMap = new HashMap<Integer, List<Choice>>();
			choicesMap.put(0, choices);
			nextQuestion = new Question(
					"Welches Betriebssystem hätten Sie denn gerne?",
					choicesMap);
		}
	}

	private void askAboutHardwareKeyboard() {
		context = 4;
//		nextQuestion = new Question(
//				"Möchten Sie das Smartphone auch über eine Hardware-Tastatur bedienen können?",
//				new ChoicesBuilder().add("Eine Tastatur ist mir wichtig", "TastaturSmartphone", ChoiceType.RADIO).
//				add("Ich möchte eins mit Touchscreen", "TouchOnlySmartphone", ChoiceType.RADIO).build());
		
		nextQuestion = new Question(
				"Möchten Sie das Smartphone auch über eine Hardware-Tastatur bedienen können?",
				ChoicesBuilder.yesNo("Eine Tastatur ist mir wichtig", "Ich möchte eins mit Touchscreen"));
	}
	
//	private void noKeyboardSmartphone(String keyboard) {
//		OntClass subClassOfInterest = searchClassContaining(keyboard, "Smartphone");
//		setCurrentProperties(subClassOfInterest);
//	}
	
	private void noKeyboardSmartphone(boolean withKeyboard) {
		List<OntClass> properties = new LinkedList<OntClass>();
		List<OntClass> propertiesNOT = new LinkedList<OntClass>();
		OntClass subClassOfInterest = searchClassContaining("TouchOnly", "Smartphone");
		ExtendedIterator<OntClass> ri = subClassOfInterest.listSubClasses();
		while (ri.hasNext()) {
			OntClass subClass = ri.next();
			if (withKeyboard) {
//				List<OntClass> disjointClasses = getDisjointSmartphones(subClass);
//				for (OntClass disjointClass : disjointClasses) {
//					properties.addAll(getClassProperties(disjointClass));
//				}
				propertiesNOT.addAll(getClassProperties(subClass));
			} else {
				properties.addAll(getClassProperties(subClass));
			}
		} 
		OntClass tmpClass = model.createClass("TmpSmartphone");

		if (withKeyboard) {
			List<OntClass> complements = new LinkedList<OntClass>();
			for (OntClass property : propertiesNOT) {
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
		
//		new ChoicesBuilder().add("Eine Tastatur ist mir wichtig", "TastaturSmartphone", ChoiceType.RADIO).
//		add("Ich möchte eins nur mit Touchscreen", "TouchOnlySmartphone", ChoiceType.RADIO).build());
	}

	private void smallDisplay(String answer) {
		if (answer.equals("lolfail"))
			throw new IllegalStateException("Im Szenario nicht vorgesehen");
		OntClass subClassOfInterest = searchClassContaining(answer, "Smartphone");
		setCurrentProperties(subClassOfInterest);
		askAboutHardwareKeyboard();
	}

	private void osChoice(String os) {
		if (os.equals("lolfail"))
			throw new IllegalStateException("Im Szenario nicht vorgesehen");
		OntClass subClassOfInterest = searchClassContaining(os, "Eigenschaften");
		setCurrentProperties(subClassOfInterest);
		
		
//		askAboutHardwareKeyboard();
			usabilityOs("Nein"); //nach auswahl der OS auch noch fragen ob die unzufriedenheit an der Hardware liegt.
	}


	@Override
	public Question firstSpecificQuestion() {
		context = 2;
		OntClass subClassOfInterest = searchClassContaining("ProfiSmartphone", "Smartphone");
		if (subClassOfInterest == null)
			throw new RuntimeException("ProfiSmartphone not in ontology");
		setCurrentProperties(subClassOfInterest);

		context = 2;
		HashMap<Integer, List<Choice>> choices = new ChoicesBuilder()
				.add("Es ist zu schwer zu bedienen", "Bedienbarkeit", ChoiceType.CHECK)
				.add("Es hat zu wenig Speicherplatz", "GrosserSpeicherSmartphone", ChoiceType.CHECK)
				.add("Das Display ist zu klein", "lolfail", ChoiceType.CHECK)
				.add("Es ist zu groß", "lolfail", ChoiceType.CHECK)
				.build();
		return new Question(
				"Sie besitzen also schon eines. Was fehlt Ihnen an Ihrem alten Smartphone?",
				choices);
	}

}
