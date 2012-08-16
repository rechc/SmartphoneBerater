package de.htw.berater;

import java.util.Iterator;

import com.hp.hpl.jena.ontology.OntClass;

import de.htw.berater.controller.Answer;

public class Berater2 extends Berater {

	public Berater2(String rdfPath, String ns) {
		super(rdfPath, ns);
	}

	@Override
	public String evaluateAndAskNewQuestion(String string) {
		switch (context) {
			case 1:
				return proSmartphone(string);
			case 2:
				return largeMemorySmartphone(string);
			case 3:
				return usabilityOs(string);
			case 4:
				return noKeyboardSmartphone(string);
			case 5:
				return multimediaSmartphone(string);
			case 6:
				return cameraSmartphone(string);
			case 7:
				return sameOsSmartphone(string);
			case 8:
				return navigationSmartphone(string);
			case 9:
				return smarphoneBrand(string);
		}
		throw new RuntimeException("wrong context");
	}

	@Override
	public String evaluateAndAskNewQuestion(boolean yes) {
		switch (context) {
		}
		throw new RuntimeException("wrong context");
	}

	@Override
	public String askFirstQuestionZweck() {
		context = 1;
		nextAnswer = Answer.KEYWORD;
		return "Guten Tag! Wie kann ich Ihnen helfen??";
	}

	@Override
	public String askFirstQuestionGeneral() {
		throw new UnsupportedOperationException();
	}
	
	public String proSmartphone(String proPhone){
		OntClass smartphone = model.getOntClass(ns + "Smartphone");
		OntClass subClassOfInterest = null;
		for (Iterator<OntClass> i = smartphone.listSubClasses(); i.hasNext();) {
			OntClass subClass = (OntClass) i.next();
			if (subClass.getLocalName().toLowerCase().contains(proPhone.toLowerCase())) {
				subClassOfInterest = subClass;
			}
		}
		setCurrentProperties(subClassOfInterest);
		context = 2;
		nextAnswer = Answer.KEYWORD;
		return "Sie besitzen also schon eines. Was st�rt sie an ihrem alten Smartphone insbesondere?";
	}
	
	public String largeMemorySmartphone(String memory){
		OntClass smartphone = model.getOntClass(ns + "Smartphone");
		OntClass subClassOfInterest = null;
		for (Iterator<OntClass> i = smartphone.listSubClasses(); i.hasNext();) {
			OntClass subClass = (OntClass) i.next();
			if (subClass.getLocalName().toLowerCase().contains(memory.toLowerCase())) {
				subClassOfInterest = subClass;
			}
		}
		setCurrentProperties(subClassOfInterest);
		context = 3;
		nextAnswer = Answer.YESNO;
		return "Liegt die schlechte Bedienbarkeit am Betriebssystem?";
	}
	
	public String usabilityOs(String os){
		if(!os.contains("nein")){
			OntClass smartphone = model.getOntClass(ns + "Smartphone");
			OntClass subClassOfInterest = null;
			for (Iterator<OntClass> i = smartphone.listSubClasses(); i.hasNext();) {
				OntClass subClass = (OntClass) i.next();
				if (subClass.getLocalName().toLowerCase().contains(os.toLowerCase())) {
					subClassOfInterest = subClass;
				}
			}
			setCurrentProperties(subClassOfInterest);
		}
		context = 4;
		nextAnswer = Answer.KEYWORD;
		//TODO evaluate expected yes/no answer
		return "M�chten Sie das Smartphone auch �ber eine Hardware-Tastatur bedienen k�nnen?";
	}
	
	public String noKeyboardSmartphone(String keyboard){
		OntClass smartphone = model.getOntClass(ns + "Smartphone");
		OntClass subClassOfInterest = null;
		for (Iterator<OntClass> i = smartphone.listSubClasses(); i.hasNext();) {
			OntClass subClass = (OntClass) i.next();
			if (subClass.getLocalName().toLowerCase().contains(keyboard.toLowerCase())) {
				subClassOfInterest = subClass;
			}
		}
		setCurrentProperties(subClassOfInterest);
		context = 5;
		nextAnswer = Answer.YESNO;
		return "Soll das Smartphone Multimedia-F�higkeiten haben?";
	}
	
	public String multimediaSmartphone(String media){
		context = 6;
		nextAnswer = Answer.YESNO;
		return "Soll das Smartphone eine gute Kamera haben, damit Sie Bilder und Videos in hoher Qualit�t aufnehmen k�nnen?";
	}
	
	public String cameraSmartphone(String camera){
		context = 7;
		nextAnswer = Answer.KEYWORD;
		return "Wenn Sie schon ein Smartphone besitzen, haben sie wahrscheinlich schon Apps gekauft? Soll das neue Ger�t das gleiche Betriebssystem haben, damit Sie ihre Anwendungen weiterverwenden k�nnen?";
	}
	
	public String sameOsSmartphone(String os){
		context = 8;
		nextAnswer = Answer.YESNO;
		return "M�chten Sie das Smartphone auch als Navigationsger�t nutzen";
	}
	
	public String navigationSmartphone(String navigation){
		context = 9;
		nextAnswer = Answer.KEYWORD;
		return "Bevorzugen Sie einen bestimmten Hersteller?";
	}
	
	public String smarphoneBrand(String brand){
		context = 10;
		nextAnswer = Answer.FINISHED;
		return "Welchen Preisrahmen haben Sie sich vorgestellt?";
	}

}
