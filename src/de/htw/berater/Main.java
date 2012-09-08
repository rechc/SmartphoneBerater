package de.htw.berater;

import de.htw.berater.controller.Controller;
import de.htw.berater.ui.BeraterUI;

public class Main {
	public static void main(String[] args) {
		BeraterUI beraterUI = StaticFactory.getNewBeraterUI();
		beraterUI.show();
		Controller controller = new Controller(beraterUI);
		beraterUI.setController(controller);
	}
}
