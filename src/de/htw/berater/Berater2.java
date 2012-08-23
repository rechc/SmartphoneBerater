package de.htw.berater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.util.iterator.Map1;

import de.htw.berater.controller.Answer;
import de.htw.berater.controller.Choice;
import de.htw.berater.controller.ChoiceType;
import de.htw.berater.controller.ChoicesBuilder;
import de.htw.berater.controller.Question;
import de.htw.berater.db.DBException;
import de.htw.berater.db.SQLClient;

public class Berater2 extends Berater {

	private Question nextQuestion;

	public Berater2(String rdfPath, String ns) {
		super(rdfPath, ns);
	}

	@Override
	public void evaluateAnswer(Answer answer) {
		String string = answer.getSingleValue();

		switch (context) {
		case 1:
			proSmartphone(string);
			break;
		case 2:
			largeMemorySmartphone(string);
			break;
		case 3:
			usabilityOs(string);
			break;
		case 4:
			noKeyboardSmartphone(string);
			break;
		case 5:
			multimediaSmartphone(string);
			break;
		case 6:
			cameraSmartphone(string);
			break;
		case 7:
			sameOsSmartphone(string);
			break;
		case 8:
			navigationSmartphone(string);
			break;
		case 9:
			smartphoneBrand(string);
			break;
		case 10:
			restrictPrice(string);
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

	private void proSmartphone(String proPhone) {
		OntClass subClassOfInterest = searchClassContaining(proPhone, "Smartphone");
		if (subClassOfInterest == null) throw new RuntimeException(proPhone + " not in ontology");
		setCurrentProperties(subClassOfInterest);

		context = 2;
		HashMap<Integer, List<Choice>> choices = new ChoicesBuilder()
				.add("Es ist zu schwer zu bedienen", "Bedienbarkeit", ChoiceType.CHECK)
				.add("Es hat zu wenig Speicherplatz", "Speicher", ChoiceType.CHECK)
				.add("Das Display ist zu klein", "KleinesDisplay", ChoiceType.CHECK)
				.add("Es ist zu groß", "", ChoiceType.CHECK)
				.build();
		nextQuestion = new Question(
				"Sie besitzen also schon eines. Was stört sie an Ihrem alten Smartphone insbesondere?",
				choices);
	}

	private void largeMemorySmartphone(String memory) {
		OntClass subClassOfInterest = searchClassContaining(memory, "Smartphone");
		setCurrentProperties(subClassOfInterest);

		context = 3;
		nextQuestion = new Question(
				"Liegt die schlechte Bedienbarkeit am Betriebssystem?",
				ChoicesBuilder.yesNo());
	}

	private void usabilityOs(String os) {
		if(!os.contains("nein")){
			OntClass subClassOfInterest = searchClassContaining(os, "Smartphone");
			setCurrentProperties(subClassOfInterest);
		}
		context = 4;
		nextQuestion = new Question(
				"Möchten Sie das Smartphone auch über eine Hardware-Tastatur bedienen können?",
				ChoicesBuilder.yesNo("Tastatur", "KeineTastatur"));
	}

	private void noKeyboardSmartphone(String keyboard) {
		OntClass subClassOfInterest = searchClassContaining(keyboard, "Smartphone");
		setCurrentProperties(subClassOfInterest);

		context = 5;
		nextQuestion = new Question(
				"Soll das Smartphone Multimedia-Fähigkeiten haben?",
				ChoicesBuilder.yesNo("Multimedia", "LolFail"));
	}

	private void multimediaSmartphone(String media) {
		OntClass subClassOfInterest = searchClassContaining(media, "Smartphone");
		setCurrentProperties(subClassOfInterest);
		context = 6;
		nextQuestion = new Question(
				"Soll das Smartphone eine gute Kamera haben, damit Sie Bilder und Videos in hoher Qualität aufnehmen können?",
				ChoicesBuilder.yesNo("Kamera", "Blindfisch"));
	}

	private void cameraSmartphone(String camera) {
		OntClass subClassOfInterest = searchClassContaining(camera, "Smartphone");
		setCurrentProperties(subClassOfInterest);

		context = 7;
		OntClass zweckClass = model.getOntClass(ns + "BetriebsystemEigenschaft");
		List<Choice> choices = zweckClass.listSubClasses()
				.mapWith(new Map1<OntClass, Choice>() {
					@Override
					public Choice map1(OntClass o) {
						String value = o.getLocalName();
						String displayName = value
								.replace("BetriebssystemEigenschaft", "")
								.replace("Betriebssystemeigenschaft", "")
								.replaceAll("([^A-Z])([A-Z])", "$1 $2");
						String text = "Ja. Ich habe ein Gerät mit " + displayName;
						return new Choice(text, value, ChoiceType.RADIO);
					}
				}).toList();
		choices.add(new Choice("Nein, ich habe genug Geld.", "Nein", ChoiceType.RADIO));
		choices.add(new Choice("Apps? Betriebssystem?", "Nein", ChoiceType.RADIO));
		HashMap<Integer, List<Choice>> choicesMap = new HashMap<Integer, List<Choice>>();
		choicesMap.put(0, choices);
		nextQuestion = new Question(
				"Wenn Sie schon ein Smartphone besitzen, haben sie wahrscheinlich schon Apps gekauft? Soll das neue Gerät das gleiche Betriebssystem haben, damit Sie ihre Anwendungen weiterverwenden können?",
				choicesMap);
	}

	private void sameOsSmartphone(String os) {
		OntClass subClassOfInterest = searchClassContaining(os, "Smartphone");
		setCurrentProperties(subClassOfInterest);

		context = 8;
		nextQuestion = new Question(
				"Möchten Sie das Smartphone auch als Navigationsgerät nutzen",
				ChoicesBuilder.yesNo("Navi", "OhnePeilung"));
	}

	private void navigationSmartphone(String navigation) {
		OntClass subClassOfInterest = searchClassContaining(navigation, "Smartphone");
		setCurrentProperties(subClassOfInterest);

		context = 9;
		List<Choice> choices = new ArrayList<Choice>();
		choices.add(new Choice("Nein", "nein", ChoiceType.RADIO));
		try {
			for (String brand : SQLClient.getInstance().getBrands())
				choices.add(new Choice("Ja, " + brand, brand, ChoiceType.RADIO));
		} catch (DBException ex) {
			// Pech gehabt.
		}
		HashMap<Integer, List<Choice>> choicesMap = new HashMap<Integer, List<Choice>>();
		choicesMap.put(0, choices);
		nextQuestion = new Question(
				"Bevorzugen Sie einen bestimmten Hersteller?",
				choicesMap);
	}

	private void smartphoneBrand(String brand) {
		// TODO Einschränkung bzgl. Marke.
//		OntClass subClassOfInterest = searchPhoneClassContaining(brand);
//		setCurrentProperties(subClassOfInterest);

		context = 10;
		nextQuestion = new Question(
				"Welchen Preisrahmen haben Sie sich vorgestellt? (von-bis)",
				null);
	}

	private void restrictPrice(String restriction) {
		// TODO Einschränkung bzgl. Preis.
		restriction = restriction.replaceAll("[^0-9,.-]", "").replace(',', '.');
		String[] split = restriction.split("-");
		@SuppressWarnings("unused")
		double min = 0, max;
		if (split.length == 1 || split.length == 2) {
			try {
				min = Double.valueOf(split[0]);
			} catch (NumberFormatException ex) {
				min = 0;
			}
			try {
				if (split.length == 2)
					max = Double.valueOf(split[1]);
			} catch (NumberFormatException ex) {
				max = 999999;
			}
		} else {
			// Fail.
		}
		context = 11;
	}

	private Question firstQuestion() {
		context = 1;
		HashMap<Integer, List<Choice>> choices = new ChoicesBuilder()
				.add("Hallo! Ich will ein iPhone! Darf aber nicht mehr als 100 € kosten!",
						"Loser", ChoiceType.CHECK)
				.add("Ich möchte ein Dingsbums…handy. Mit Äpps, und so. Hilfe!",
						"DAU", ChoiceType.CHECK)
				.add("Ich benötige ein neues Smartphone, da mir mein altes nicht mehr genügt.",
						"Profi", ChoiceType.CHECK)
				.build();
		return new Question("Guten Tag! Wie kann ich Ihnen helfen?", choices);
	}

}
