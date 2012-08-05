package de.htw.berater.szenario;

import de.htw.berater.Berater;

public class Szenario2 extends Szenario {

	

	public Szenario2(Berater berater) {
		super(berater);
	}

	@Override
	public void start() {
		beraterUI.onStart(berater.askFirstQuestion(GENERAL_FIRST_QUESTION));
	}

	@Override
	public String toString() {
		return "Szenario zwei";
	}

	@Override
	public void cases(String answer) {
		switch(progress) {
		case 0:
			if (answer.contains("")) {
				progress++;
			} else {
				falseQuestion();
			}
			break;
		case 1:
			if (answer.contains("")) {
				progress++;
			} else {
				falseQuestion();
			}
			break;
		case 2:
			if (answer.contains("")) {
				progress++;
			} else {
				falseQuestion();
			}
			break;
		case 3:
			if (answer.contains("")) {
				progress++;
			} else {
				falseQuestion();
			}
			break;
		case 4:
			if (answer.contains("")) {
				progress++;
			} else {
				falseQuestion();
			}
			break;
		case 5:
			if (answer.contains("")) {
				progress++;
			} else {
				falseQuestion();
			}
			break;
		case 6:
			if (answer.contains("")) {
				progress++;
			} else {
				falseQuestion();
			}
			break;
		case 7:
			if (answer.contains("")) {
				progress++;
			} else {
				falseQuestion();
			}
			break;
		case 8:
			if (answer.contains("")) {
				progress++;
			} else {
				falseQuestion();
			}
			break;
		case 9:
			if (answer.contains("")) {
				progress++;
			} else {
				falseQuestion();
			}
			break;
		}
	}

	@Override
	public int maxProgress() {
		return 10;
	}
	
}
