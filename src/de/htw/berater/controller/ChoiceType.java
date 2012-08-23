package de.htw.berater.controller;

/**
 * 
 *	Alle moeglichen GUI Elemente, ausser FINISHED. Wird bei jeder Choice mitgegeben.
 */
public enum ChoiceType {

	/** Einfache Auswahl. */
	RADIO,

	/** Mehrfachauswahl. */
	CHECK,

	/** Benutzereingabe. */
	INPUT,

	/** Ende. */
	FINISHED;
}
