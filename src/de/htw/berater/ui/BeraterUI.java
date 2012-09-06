package de.htw.berater.ui;

import java.awt.Color;
import java.util.List;

import de.htw.berater.controller.Controller;
import de.htw.berater.controller.Question;
import de.htw.berater.db.Smartphone;

public abstract class BeraterUI {

	protected Controller controller;
	
	public abstract void onFalseQuestion(String string);

	public abstract void onNewQuestion(Question question);

	public abstract void show(List<Smartphone> resultData);

	public abstract void show();

	public abstract void onNewData(List<Smartphone> resultData);
	
	public abstract void onNewStatus(String text, Color farbe, int maxSeconds);
	
	public abstract void resetStatus();

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public abstract void restart();

}
