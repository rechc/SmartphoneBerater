package de.htw.berater;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.util.iterator.Map1;

import de.htw.berater.controller.Answer;
import de.htw.berater.controller.Choice;
import de.htw.berater.controller.ChoicesBuilder;
import de.htw.berater.controller.Question;
import de.htw.berater.controller.QuestionType;
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
		OntClass subClassOfInterest = searchPhoneClassContaining(proPhone);
		setCurrentProperties(subClassOfInterest);

		context = 2;
		List<Choice> choices = new ChoicesBuilder()
				.add("Es ist zu schwer zu bedienen", "Bedienbarkeit")
				.add("Es hat zu wenig Speicherplatz", "Speicher")
				.add("Das Display ist zu klein", "KleinesDisplay")
				.add("Es ist zu groß", "")
				.build();
		nextQuestion = new Question(
				QuestionType.MULTI,
				"Sie besitzen also schon eines. Was stört sie an Ihrem alten Smartphone insbesondere?",
				choices);
	}

	private void largeMemorySmartphone(String memory) {
		OntClass subClassOfInterest = searchPhoneClassContaining(memory);
		setCurrentProperties(subClassOfInterest);

		context = 3;
		nextQuestion = new Question(
				QuestionType.CHOICE,
				"Liegt die schlechte Bedienbarkeit am Betriebssystem?",
				ChoicesBuilder.yesNo());
	}

	private void usabilityOs(String os) {
		if(!os.contains("nein")){
			OntClass subClassOfInterest = searchPhoneClassContaining(os);
			setCurrentProperties(subClassOfInterest);
		}
		context = 4;
		nextQuestion = new Question(
				QuestionType.CHOICE,
				"Möchten Sie das Smartphone auch über eine Hardware-Tastatur bedienen können?",
				ChoicesBuilder.yesNo("Tastatur", "KeineTastatur"));
	}

	private void noKeyboardSmartphone(String keyboard) {
		OntClass subClassOfInterest = searchPhoneClassContaining(keyboard);
		setCurrentProperties(subClassOfInterest);

		context = 5;
		nextQuestion = new Question(
				QuestionType.CHOICE,
				"Soll das Smartphone Multimedia-Fähigkeiten haben?",
				ChoicesBuilder.yesNo("Multimedia", "LolFail"));
	}

	private void multimediaSmartphone(String media) {
		OntClass subClassOfInterest = searchPhoneClassContaining(media);
		setCurrentProperties(subClassOfInterest);
		context = 6;
		nextQuestion = new Question(
				QuestionType.CHOICE,
				"Soll das Smartphone eine gute Kamera haben, damit Sie Bilder und Videos in hoher Qualität aufnehmen können?",
				ChoicesBuilder.yesNo("Kamera", "Blindfisch"));
	}

	private void cameraSmartphone(String camera) {
		OntClass subClassOfInterest = searchPhoneClassContaining(camera);
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
						return new Choice(text, value);
					}
				}).toList();
		choices.add(new Choice("Nein, ich habe genug Geld.", "Nein"));
		choices.add(new Choice("Apps? Betriebssystem?", "Nein"));
		nextQuestion = new Question(
				QuestionType.CHOICE,
				"Wenn Sie schon ein Smartphone besitzen, haben sie wahrscheinlich schon Apps gekauft? Soll das neue Gerät das gleiche Betriebssystem haben, damit Sie ihre Anwendungen weiterverwenden können?",
				choices);
	}

	private void sameOsSmartphone(String os) {
		OntClass subClassOfInterest = searchPhoneClassContaining(os);
		setCurrentProperties(subClassOfInterest);

		context = 8;
		nextQuestion = new Question(
				QuestionType.CHOICE,
				"Möchten Sie das Smartphone auch als Navigationsgerät nutzen",
				ChoicesBuilder.yesNo("Navi", "OhnePeilung"));
	}

	private void navigationSmartphone(String navigation) {
		OntClass subClassOfInterest = searchPhoneClassContaining(navigation);
		setCurrentProperties(subClassOfInterest);

		context = 9;
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

	private void smartphoneBrand(String brand) {
		// TODO Einschränkung bzgl. Marke.
//		OntClass subClassOfInterest = searchPhoneClassContaining(brand);
//		setCurrentProperties(subClassOfInterest);

		context = 10;
		nextQuestion = new Question(
				QuestionType.INPUT,
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
		List<Choice> choices = new ChoicesBuilder()
				.add("Hallo! Ich will ein iPhone! Darf aber nicht mehr als 100 € kosten!",
						"Loser")
				.add("Ich möchte ein Dingsbums…handy. Mit Äpps, und so. Hilfe!",
						"DAU")
				.add("Ich benötige ein neues Smartphone, da mir mein altes nicht mehr genügt.",
						"Profi")
				.build();
		return new Question(QuestionType.MULTI,
				"Guten Tag! Wie kann ich Ihnen helfen?", choices);
	}

}
