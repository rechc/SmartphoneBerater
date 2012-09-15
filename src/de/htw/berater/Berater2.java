package de.htw.berater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.hp.hpl.jena.ontology.OntClass;
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

	private int found;

	public Berater2(String rdfPath, String ns) {
		super(rdfPath, ns, true);
	}

	@Override
	public void evaluateAnswer(Answer answer) {
		List<String> answerList = answer.getValues();

		switch (context) {
		case 1:
			proSmartphone(answerList.get(0));
			break;
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
			noKeyboardSmartphone(answerList.get(0));
			break;
		default:
			throw new IllegalStateException("Weiter gehts nicht");
		}
	}



	private void proSmartphone(String proPhone) {
		OntClass subClassOfInterest = searchClassContaining(proPhone, "Smartphone");
		if (subClassOfInterest == null)
			throw new RuntimeException(proPhone + " not in ontology");
		setCurrentProperties(subClassOfInterest);

		context = 2;
		HashMap<Integer, List<Choice>> choices = new ChoicesBuilder()
				.add("Es ist zu schwer zu bedienen", "Bedienbarkeit", ChoiceType.CHECK)
				.add("Es hat zu wenig Speicherplatz", "GrosserSpeicherSmartphone", ChoiceType.CHECK)
				.add("Das Display ist zu klein", "lolfail", ChoiceType.CHECK)
				.add("Es ist zu groß", "lolfail", ChoiceType.CHECK)
				.build();
		nextQuestion = new Question(
				"Sie besitzen also schon eines. Was fehlt Ihnen an Ihrem alten Smartphone?",
				choices);
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
		nextQuestion = new Question(
				"Möchten Sie das Smartphone auch über eine Hardware-Tastatur bedienen können?",
				new ChoicesBuilder().add("Eine Tastatur ist mir wichtig", "TastaturSmartphone", ChoiceType.RADIO).
				add("Ich möchte eins mit Touchscreen", "TouchOnlySmartphone", ChoiceType.RADIO).build());
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
		
//		if (os.equals("Ios"))  //neues IPhone bekommt wohl größeres Display
//			askAboutHardwareKeyboard();
//		else
			usabilityOs("Nein");
	}

	private void noKeyboardSmartphone(String keyboard) {
		OntClass subClassOfInterest = searchClassContaining(keyboard, "Smartphone");
		setCurrentProperties(subClassOfInterest);

		context = 5;
		nextQuestion = new Question(
				"Soll das Smartphone Multimedia-Fähigkeiten haben?",
				new ChoicesBuilder()
				.add("Ja, ich werde Multimedia-Fähigkeiten nutzen", "MultimediaSmartphone", ChoiceType.RADIO)
				.add("Nein", "do nothing", ChoiceType.RADIO).build());
	}

	@Override
	public Question firstSpecificQuestion() {
		context = 1;
		HashMap<Integer, List<Choice>> choices = new ChoicesBuilder()
				.add("Hallo! Ich will ein iPhone! Darf aber nicht mehr als 100 € kosten!",
						"Loser", ChoiceType.RADIO)
				.add("Ich möchte ein Dingsbums…handy. Mit Äpps, und so. Hilfe!",
						"DAU", ChoiceType.RADIO)
				.add("Ich benötige ein neues Smartphone, da mir mein altes nicht mehr genügt.",
						"Profi", ChoiceType.RADIO)
				.build();
		return new Question("Guten Tag! Wie kann ich Ihnen helfen?", choices);
	}

}
