package de.htw.berater.ui;

import java.util.List;

import de.htw.berater.controller.Controller;
import de.htw.berater.db.ResultData;

public interface BeraterUI {

	void onFalseQuestion(String string);

	void onNewQuestion(String newQuestion);

	void show(List<ResultData> resultData);

	void show();

	void onNewData(List<ResultData> resultData);

	void setController(Controller controller);
	
}
