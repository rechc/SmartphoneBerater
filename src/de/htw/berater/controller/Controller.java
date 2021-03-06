package de.htw.berater.controller;

import java.awt.Color;
import java.util.List;

import javax.swing.SwingUtilities;

import de.htw.berater.Berater;
import de.htw.berater.StaticFactory;
import de.htw.berater.db.DBException;
import de.htw.berater.db.Smartphone;
import de.htw.berater.db.SQLClient;
import de.htw.berater.ui.BeraterUI;

/**
 * 
 *	Vermittler zwischen GUI und Berater
 */
public class Controller {
	private Berater berater;
	
	private BeraterUI beraterUI;
	

	
	public Controller(BeraterUI beraterUI) {
		this.beraterUI = beraterUI;
	}
	
	
	public void start(final String rdfPath, final String nameSpace) {
		new Thread() {
			@Override
			public void run() {
				berater = StaticFactory.getNewBerater(rdfPath, nameSpace);
				berater.setController(Controller.this);
				//berater.reset();
				Question question = berater.generateQuestion();
				try {
					informUI(question);
				} catch (DBException e) {
					beraterUI.onNewStatus(e.getMessage(), Color.RED, 0);
					e.printStackTrace();
				}
				try {
					SQLClient.getInstance().closeConnection();
				} catch (DBException e) {
					beraterUI.onNewStatus(e.getMessage(), Color.RED, 0);
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	public void answer(final Answer answer) {
		beraterUI.resetStatus();
		new Thread() {
			@Override
			public void run(){
				try {
					berater.evaluateAnswer(answer);
					Question question = berater.generateQuestion();
					informUI(question);
				} catch (final Exception e) {
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							beraterUI.onNewStatus(e.getMessage(), Color.RED, 0);
						}
					});
					e.printStackTrace();
				}
				try {
					SQLClient.getInstance().closeConnection();
				} catch (final DBException e) {
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							beraterUI.onNewStatus(e.getMessage(), Color.RED, 0);
						}
					});
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	private void informUI(Question question) throws DBException {
		String sql = berater.getSQLString();
		System.out.println(sql);
		List<Smartphone> resultData = SQLClient.getInstance().getSmartphones(sql);
		if (resultData.size() == 0) {
			berater.goBack();
			sql = berater.getSQLString();
			resultData = SQLClient.getInstance().getSmartphones(sql);
			question = berater.generateQuestion();
			SwingUtilities.invokeLater(new Runnable () {

				@Override
				public void run() {
					beraterUI.onNewStatus("Keine Smartphones bei der letzten Auswahl mehr. Die Frage wird wiederholt.", Color.YELLOW, 0);		
				}
				
			});
			
		}
		
		printResults(resultData, question);
		
	}


	private void printResults(final List<Smartphone> resultData, final Question question) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				beraterUI.onNewData(resultData);
				if (question == null) {
					beraterUI.finishSzenario();
				} else {
					beraterUI.onNewQuestion(question);
				}
			}
		});
	}


	public void setBerater(Berater berater) {
		this.berater = berater;
	}
	
}
