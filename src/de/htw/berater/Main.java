package de.htw.berater;

import de.htw.berater.controller.Controller;
import de.htw.berater.ui.BeraterUI;

public class Main {
	public static void main(String[] args) {
		String rdfPath = "inferredSmartphones.rdf";
		String namespace = "http://semantische-interoperabilitaet-projekt#";
		Berater berater1 = StaticFactory.getNewBerater1(rdfPath, namespace); 
		Berater berater2 = StaticFactory.getNewBerater2(rdfPath, namespace);
		BeraterUI beraterUI = StaticFactory.getNewBeraterUI();
		beraterUI.show();
		Controller controller = new Controller(beraterUI, berater1, berater2);
		beraterUI.setController(controller);
	}
}
