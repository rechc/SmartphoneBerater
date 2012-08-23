package de.htw.berater.controller;

/**
 * 
 * Repraesentiert ein Häckchen, Radiobutton oder Texteingabe.
 * Mapping von Fließtext zu keyword für den Berater
 */
public class Choice {
	private String text;
	private String value;
	private ChoiceType choiceType;

	public Choice(String text, String value, ChoiceType choiceType) {
		this.text = text;
		this.value = value;
		this.choiceType = choiceType;
	}

	public ChoiceType getChoiceType() {
		return choiceType;
	}
	
	public String getText() {
		return text;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "Choice [text=" + text + ", value=" + value + "]";
	}
}
