package de.htw.berater;

import de.htw.berater.ui.BeraterUI;
import de.htw.berater.ui.BeraterUIJFrame;

/**
 * 
 * Verlagerung der Objekterzeugung.
 */
public class StaticFactory {
	public static BeraterUI getNewBeraterUI() {
		return new BeraterUIJFrame();
	}		


	public static Berater getNewBerater1(String rdfPath, String namespace) {
		return new Berater1(rdfPath, namespace);
	}
	
	public static Berater getNewBerater(String rdfPath, String namespace) {
		return new Berater(rdfPath, namespace, false);
	}


	public static Berater getNewBerater2(String rdfPath, String namespace) {
		return new Berater2(rdfPath, namespace);
	}
}
