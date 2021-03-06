package de.htw.berater.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * Komfortables erstellen von Choices, die auf der GUI angezeigt werden.
 */
public final class ChoicesBuilder {

	HashMap<Integer, List<Choice>> choices = new HashMap<Integer, List<Choice>>();

	public ChoicesBuilder add(String text, String value, ChoiceType choiceType, int group) {
		if (choices.get(group) == null) {
			choices.put(group, new LinkedList<Choice>());
		}
		choices.get(group).add(new Choice(text, value, choiceType));
		return this;
	}
	
	public ChoicesBuilder add(String text, String value, ChoiceType choiceType) {
		if (choices.get(0) == null) {
			choices.put(0, new LinkedList<Choice>());
		}
		choices.get(0).add(new Choice(text, value, choiceType));
		return this;
	}

	public HashMap<Integer, List<Choice>> build() {
		return choices;
	}

	public static HashMap<Integer, List<Choice>> yesNo() {
		return yesNo("ja", "nein");
	}

	// TODO: Das is total ... seltsam. Warum wird bei Ja/Nein-Fragen als Antwort
	// z.B. "Navi" erwartet? <== woher soll ich das wissen?
	public static HashMap<Integer, List<Choice>> yesNo(String yesText, String noText) {
		HashMap<Integer, List<Choice>> choices = new HashMap<Integer, List<Choice>>();
		if (choices.get(0) == null) {
			choices.put(0, new LinkedList<Choice>());
		}
		choices.get(0).add(new Choice(yesText, "Ja", ChoiceType.RADIO));
		choices.get(0).add(new Choice(noText, "Nein", ChoiceType.RADIO));
		return choices;
	}

}
