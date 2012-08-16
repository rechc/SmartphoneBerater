package de.htw.berater.ui;

import java.util.List;

import de.htw.berater.controller.Controller;
import de.htw.berater.db.ResultData;

public abstract class BeraterUI {

	Controller controller;
	
	public abstract void onFalseQuestion(String string);

	public abstract void onNewQuestion(String newQuestion);

	public abstract void show(List<ResultData> resultData);

	public abstract void show();

	public abstract void onNewData(List<ResultData> resultData);

	public void setController(Controller controller) {
		this.controller = controller;
	}

}
