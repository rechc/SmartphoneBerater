package de.htw.berater.controller;

public class FalseAnswerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2917595057982867403L;
	private Answer expected;
	
	public FalseAnswerException(Answer expected) {
		this.expected = expected;
		
	}
	
	public Answer getExpectedAnswer() {
		return expected;
	}
}
