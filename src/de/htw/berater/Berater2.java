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
				return smartphoneBrand(string);
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
	public String askFirstQuestion() {
		context = 1;
		nextAnswer = Answer.KEYWORD;
		return "Guten Tag! Wie kann ich Ihnen helfen?";
	}

	public String proSmartphone(String proPhone) {
		OntClass smartphone = model.getOntClass(ns + "Smartphone");
		OntClass subClassOfInterest = null;
		for (Iterator<OntClass> i = smartphone.listSubClasses(); i.hasNext();) {
			OntClass subClass = i.next();
			if (subClass.getLocalName().toLowerCase().contains(proPhone.toLowerCase())) {
				subClassOfInterest = subClass;
			}
		}
		setCurrentProperties(subClassOfInterest);
		context = 2;
		nextAnswer = Answer.KEYWORD;
		return "Sie besitzen also schon eines. Was stört sie an Ihrem alten Smartphone insbesondere?";
	}

	public String largeMemorySmartphone(String memory) {
		OntClass smartphone = model.getOntClass(ns + "Smartphone");
		OntClass subClassOfInterest = null;
		for (Iterator<OntClass> i = smartphone.listSubClasses(); i.hasNext();) {
			OntClass subClass = i.next();
			if (subClass.getLocalName().toLowerCase().contains(memory.toLowerCase())) {
				subClassOfInterest = subClass;
			}
		}
		setCurrentProperties(subClassOfInterest);
		context = 3;
		nextAnswer = Answer.YESNO;
		return "Liegt die schlechte Bedienbarkeit am Betriebssystem?";
	}

	public String usabilityOs(String os) {
		if(!os.contains("nein")){
			OntClass smartphone = model.getOntClass(ns + "Smartphone");
			OntClass subClassOfInterest = null;
			for (Iterator<OntClass> i = smartphone.listSubClasses(); i.hasNext();) {
				OntClass subClass = i.next();
				if (subClass.getLocalName().toLowerCase().contains(os.toLowerCase())) {
					subClassOfInterest = subClass;
				}
			}
			setCurrentProperties(subClassOfInterest);
		}
		context = 4;
		nextAnswer = Answer.KEYWORD;
		return "Möchten Sie das Smartphone auch über eine Hardware-Tastatur bedienen können?";
	}

	public String noKeyboardSmartphone(String keyboard) {
		OntClass smartphone = model.getOntClass(ns + "Smartphone");
		OntClass subClassOfInterest = null;
		for (Iterator<OntClass> i = smartphone.listSubClasses(); i.hasNext();) {
			OntClass subClass = i.next();
			if (subClass.getLocalName().toLowerCase().contains(keyboard.toLowerCase())) {
				subClassOfInterest = subClass;
			}
		}
		setCurrentProperties(subClassOfInterest);
		context = 5;
		nextAnswer = Answer.YESNO;
		return "Soll das Smartphone Multimedia-Fähigkeiten haben?";
	}

	public String multimediaSmartphone(String media) {
		OntClass smartphone = model.getOntClass(ns + "Smartphone");
		OntClass subClassOfInterest = null;
		for (Iterator<OntClass> i = smartphone.listSubClasses(); i.hasNext();) {
			OntClass subClass = i.next();
			if (subClass.getLocalName().toLowerCase().contains(media.toLowerCase())) {
				subClassOfInterest = subClass;
			}
		}
		setCurrentProperties(subClassOfInterest);
		context = 6;
		nextAnswer = Answer.YESNO;
		return "Soll das Smartphone eine gute Kamera haben, damit Sie Bilder und Videos in hoher Qualität aufnehmen können?";
	}

	public String cameraSmartphone(String camera) {
		OntClass smartphone = model.getOntClass(ns + "Smartphone");
		OntClass subClassOfInterest = null;
		for (Iterator<OntClass> i = smartphone.listSubClasses(); i.hasNext();) {
			OntClass subClass = i.next();
			if (subClass.getLocalName().toLowerCase().contains(camera.toLowerCase())) {
				subClassOfInterest = subClass;
			}
		}
		setCurrentProperties(subClassOfInterest);
		context = 7;
		nextAnswer = Answer.KEYWORD;
		return "Wenn Sie schon ein Smartphone besitzen, haben sie wahrscheinlich schon Apps gekauft? Soll das neue Gerät das gleiche Betriebssystem haben, damit Sie ihre Anwendungen weiterverwenden können?";
	}

	public String sameOsSmartphone(String os) {
		OntClass smartphone = model.getOntClass(ns + "Smartphone");
		OntClass subClassOfInterest = null;
		for (Iterator<OntClass> i = smartphone.listSubClasses(); i.hasNext();) {
			OntClass subClass = i.next();
			if (subClass.getLocalName().toLowerCase().contains(os.toLowerCase())) {
				subClassOfInterest = subClass;
			}
		}
		setCurrentProperties(subClassOfInterest);
		context = 8;
		nextAnswer = Answer.YESNO;
		return "Möchten Sie das Smartphone auch als Navigationsgerät nutzen";
	}

	public String navigationSmartphone(String navigation) {
		OntClass smartphone = model.getOntClass(ns + "Smartphone");
		OntClass subClassOfInterest = null;
		for (Iterator<OntClass> i = smartphone.listSubClasses(); i.hasNext();) {
			OntClass subClass = i.next();
			if (subClass.getLocalName().toLowerCase().contains(navigation.toLowerCase())) {
				subClassOfInterest = subClass;
			}
		}
		setCurrentProperties(subClassOfInterest);
		context = 9;
		nextAnswer = Answer.KEYWORD;
		return "Bevorzugen Sie einen bestimmten Hersteller?";
	}

	public String smartphoneBrand(String brand) {
		OntClass smartphone = model.getOntClass(ns + "Smartphone");
		OntClass subClassOfInterest = null;
		for (Iterator<OntClass> i = smartphone.listSubClasses(); i.hasNext();) {
			OntClass subClass = i.next();
			if (subClass.getLocalName().toLowerCase().contains(brand.toLowerCase())) {
				subClassOfInterest = subClass;
			}
		}
		setCurrentProperties(subClassOfInterest);
		context = 10;
		nextAnswer = Answer.FINISHED;
		return "Welchen Preisrahmen haben Sie sich vorgestellt?";
	}

}
