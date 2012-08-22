package de.htw.berater.controller;

public class Choice {

	private String text;
	private String value;

	public Choice(String text, String value) {
		this.text = text;
		this.value = value;
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
