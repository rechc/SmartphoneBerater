package de.htw.berater;

import de.htw.berater.ui.BeraterUI;
import de.htw.berater.ui.BeraterUIJFrame;

public class StaticFactory {
	public static BeraterUI getNewBeraterUI() {
		return new BeraterUIJFrame();
	}		


	public static Berater getNewBerater1(String rdfPath, String namespace) {
		return new Berater1(rdfPath, namespace);
	}


	public static Berater getNewBerater2(String rdfPath, String namespace) {
		return new Berater2(rdfPath, namespace);
	}
}
