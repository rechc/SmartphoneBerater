package de.htw.berater.ui;

import java.util.List;

import de.htw.berater.Berater;
import de.htw.berater.controller.Controller;
import de.htw.berater.db.ResultData;

public abstract class BeraterUI {

	Berater berater1;
	Berater berater2;
	
	public BeraterUI(Berater berater1, Berater berater2) {
		this.berater1 = berater1;
		this.berater2 = berater2;
	}

	public abstract void onFalseQuestion(String string);

	public abstract void onNewQuestion(String newQuestion);

	public abstract void show(List<ResultData> resultData);

	public abstract void show();

	public abstract void onNewData(List<ResultData> resultData);

	public abstract void setController(Controller controller);
	
}
