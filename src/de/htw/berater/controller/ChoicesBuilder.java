package de.htw.berater.controller;

import java.util.ArrayList;
import java.util.List;

public final class ChoicesBuilder {

	List<Choice> choices = new ArrayList<Choice>();

	public ChoicesBuilder add(String text, String value) {
		choices.add(new Choice(text, value));
		return this;
	}

	public List<Choice> build() {
		return choices;
	}

	public static List<Choice> yesNo() {
		return yesNo("ja", "nein");
	}

	// TODO: Das is total ... seltsam. Warum wird bei Ja/Nein-Fragen als Antwort
	// z.B. "Navi" erwartet?
	public static List<Choice> yesNo(String yesValue, String noValue) {
		List<Choice> choices = new ArrayList<Choice>();
		choices.add(new Choice("Ja", yesValue));
		choices.add(new Choice("Nein", noValue));
		return choices;
	}

}
