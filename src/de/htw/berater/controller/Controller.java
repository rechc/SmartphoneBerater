package de.htw.berater.controller;

import java.awt.Color;
import java.util.List;

import javax.swing.SwingUtilities;

import de.htw.berater.Berater;
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
	private Berater berater1;
	private Berater berater2;
	
	private BeraterUI beraterUI;
	
	public Controller(BeraterUI beraterUI, Berater berater1, Berater berater2) {
		this.beraterUI = beraterUI;
		this.berater1 = berater1;
		this.berater2 = berater2;
	}
	
	
	public void finish() {
		berater.reset();
	}
	
	public void getFirstQuestion(boolean szenario1) {
		this.berater = szenario1 ? berater1 : berater2;
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
	
	private void informUI(final Question question) throws DBException {
		String sql = berater.getSQLString();
		final List<Smartphone> resultData;
		System.out.println(sql);
		resultData = SQLClient.getInstance().getSmartphones(sql);
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				beraterUI.onNewData(resultData);
				beraterUI.onNewQuestion(question);
			}
		});
	}
}
