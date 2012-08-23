package de.htw.berater.controller;

import java.util.HashMap;
import java.util.List;

public class Question {

	private String text;
	private HashMap<Integer, List<Choice>> choices;

	public Question(String text, HashMap<Integer, List<Choice>> choices) {
		this.text = text;
		this.choices = choices;
	}

	public String getText() {
		return text;
	}

	public HashMap<Integer, List<Choice>> getChoices() {
		return choices;
	}

	@Override
	public String toString() {
		return "Question [text=" + text + ", choices=" + choices + "]";
	}

}
