package de.htw.berater.szenario;

import de.htw.berater.Berater;
import de.htw.berater.ui.BeraterUI;


public abstract class Szenario {
	protected int progress;
	protected BeraterUI beraterUI;
	protected Berater berater;
	
	public static final String GENERAL_FIRST_QUESTION = "general first question";
	
	public Szenario(Berater berater) {
		this.berater = berater;
	}
	
	public void setBeraterUI(BeraterUI beraterUI) {
		this.beraterUI = beraterUI;
	}
	
	public void finish() {
		progress = 0;
		if (beraterUI != null) {
			beraterUI.onFinish("Szenario ist fertig bei Frage " + progress);
			beraterUI.onReset();
		}
		berater.reset();
	}

	protected void falseQuestion() {
		if (beraterUI != null) {
			beraterUI.onFalseQuestion("Falsche Frage für Frage Nummer " + progress);
		}
	}
	
	/* Wird von der UI aufgerufen */
	public void onAnser(String answer) {
		if (progress == maxProgress()) {
			finish();
		} else {
			cases(answer);
		}
	}
	
	
	protected abstract void cases(String answer);
	/* Wird von der UI aufgerufen */
	public abstract void start();
	protected abstract int maxProgress();
}
