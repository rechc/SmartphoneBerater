package de.htw.berater.controller;

import java.util.List;

public class Question {

	private QuestionType type;
	private String text;
	private List<Choice> choices;

	public Question(QuestionType type, String text, List<Choice> choices) {
		this.type = type;
		this.text = text;
		this.choices = choices;
	}

	public QuestionType getType() {
		return type;
	}

	public String getText() {
		return text;
	}

	public List<Choice> getChoices() {
		return choices;
	}

	@Override
	public String toString() {
		return "Question [type=" + type + ", text=" + text + ", choices=" + choices + "]";
	}

}
