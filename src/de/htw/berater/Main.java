package de.htw.berater;

import de.htw.berater.controller.Controller;
import de.htw.berater.ui.BeraterUI;

public class Main {
	public static void main(String[] args) {
		String rdfPath = "inferredSmartphones.rdf";
		String namespace = "http://semantische-interoperabilitaet-projekt#";
		Berater berater = StaticFactory.getNewBerater(rdfPath, namespace);
		BeraterUI beraterUI = StaticFactory.getNewBeraterUI();
		beraterUI.show();
		Controller controller = new Controller(berater, beraterUI);
		beraterUI.setController(controller);
	}
}
