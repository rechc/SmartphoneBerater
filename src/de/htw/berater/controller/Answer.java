package de.htw.berater.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 
 * Die Antwort der GUI an den Berater. Liste von Keywords.
 */
public class Answer {

	private List<String> values;

	public Answer(String value) {
		values = new ArrayList<String>(1);
		values.add(value);
	}

	public Answer(Collection<String> values) {
		if (values.isEmpty())
			throw new IllegalArgumentException("Liste ist leer.");
		this.values = new ArrayList<String>(values);
	}

	public List<String> getValues() {
		return values;
	}

	/**
	 * Gibt alle Werte durch Leerzeichen getrennt als einen String zurück.
	 */
	public String getSingleValue() {
		StringBuilder sb = new StringBuilder(values.get(0));
		for (int i = 1; i < values.size(); i++)
			sb.append(" ").append(values.get(i));
		return sb.toString();
	}

	@Override
	public String toString() {
		return "Answer [values=" + values + "]";
	}

}
