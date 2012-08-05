package de.htw.berater;

import de.htw.berater.szenario.Szenario;
import de.htw.berater.szenario.Szenario1;
import de.htw.berater.szenario.Szenario2;
import de.htw.berater.ui.BeraterUI;

public class Main {
	
	public static void main(String[] args) {
		String rdfPath = "inferredSmartphones.rdf";
		String namespace = "http://semantische-interoperabilitaet-projekt#";
		Berater berater = StaticFactory.getNewBerater(rdfPath, namespace);
		berater.init();
		Szenario sz1 = new Szenario1(berater);
		Szenario sz2 = new Szenario2(berater);
		BeraterUI beraterUI = StaticFactory.getNewBeraterUI();
		beraterUI.addSzenarios(sz1, sz2);
		/*beraterUI show*/
	}
}
