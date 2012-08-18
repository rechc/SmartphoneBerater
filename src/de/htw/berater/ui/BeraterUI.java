package de.htw.berater.ui;

import java.util.List;

import de.htw.berater.controller.Controller;
import de.htw.berater.db.Smartphone;

public abstract class BeraterUI {

	Controller controller;
	
	public abstract void onFalseQuestion(String string);

	public abstract void onNewQuestion(String newQuestion);

	public abstract void show(List<Smartphone> resultData);

	public abstract void show();

	public abstract void onNewData(List<Smartphone> resultData);

	public void setController(Controller controller) {
		this.controller = controller;
	}

}
