package de.htw.berater.ui;

import java.util.List;

import de.htw.berater.db.ResultData;
import de.htw.berater.szenario.Szenario;

public interface BeraterUI {


	void onFinish(String string);

	void onReset();

	void onFalseQuestion(String string);

	void onNewQuestion(String newQuestion);

	void onStart(String string);

	void addSzenario(Szenario szenario);

	/*hier soll void setBeraterUI(BeraterUI beraterUI) aufgerufen werden*/
	void startSzenario(Szenario szenario);
	
	/*szenario dem man folgen möchte.*/
	void followSzenario(Szenario szenario);

	void addSzenarios(Szenario... sz);

	void show(List<ResultData> resultData);
	
}
