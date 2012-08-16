package de.htw.berater.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.htw.berater.Berater;
import de.htw.berater.controller.FalseAnswerException;
import de.htw.berater.db.SQLClient;

public class UIActions implements ActionListener {

	private BeraterUIJFrame frame;
	
	public UIActions(BeraterUIJFrame frame) {
		this.frame = frame;
	}
	
	/**
	 * Action Listener fuer Start und Weiter
	 */
	@Override
	public void actionPerformed(ActionEvent a) {
		if (a.getActionCommand().equals("start")){
			
			// noch kein Szenario initialisiert? dann jetzt Panels aktivieren 
			if (!frame.getCommit_panel().isVisible()){
				frame.initializeAnswers();
			}
			
			// Szenario 1 starten
			if (frame.getRadioButtonScenario1().isSelected()){
				frame.getController().getFirstQuestion(true);
		
			// Szenario 2 starten
			} else {
				frame.getController().getFirstQuestion(false);
			}
		}
		
		if (a.getActionCommand().equals("weiter")){
			//getSelectedIndex()
			//View tmpView = siGui.getView();
			int actualAnswerPanel = frame.getTabbedPane().getSelectedIndex();
			
			String answer = null;
			
			switch (actualAnswerPanel){
				case 0: // Direkteingabe-View ist aktiv
						answer = frame.getTxtAnswer();
						try {
							frame.getController().answer(answer);
						} catch (FalseAnswerException e) {
							e.printStackTrace();
						}
						break;
						
				case 1: // Direktauswahl-View ist aktiv
						answer = frame.getAnswerBox().getSelectedItem().toString();
						break;
						
				case 2: // Detailauswahl-View ist aktiv
						JPanel tmpAnswers = frame.getAnswer_panel_a3();
						
						for (Object o: tmpAnswers.getComponents()){
							
							// aktuelles Objekt == Checkbox?
							if (o instanceof JCheckBox){
								JCheckBox tmpChkBox = (JCheckBox)o;
								if (tmpChkBox.isSelected()){
									addToAnswer(answer, tmpChkBox.getName());
								}
							}
							// aktuelles Objekt == JPanel?
							if (o instanceof JPanel){
								JPanel tmpPanel = (JPanel) o;
								
								// aktuelles (Unter) Objekt == JTextField?
								if (tmpPanel.getComponent(tmpPanel.getComponentCount() -1) instanceof JTextField){
								
									JTextField tmpTextField = (JTextField)tmpPanel.getComponent(tmpPanel.getComponentCount() -1);
									
									// Inhalt nur in Antwort aufnehmen wenn es nicht leer ist
									if (tmpTextField != null){
										if (!tmpTextField.getText().equals("")){
											
											addToAnswer(answer, tmpTextField.getText());
										}
									}
									
								}
							}
						}
					
				default:
					break;
			
			}
			
			
		} 
		// Radiobutton fuer Szenario 1 gewaehlt
		if (a.getActionCommand().equals("radioButtonScenario1")){
			frame.getRadioButtonScenario2().setSelected(false);
		}
		// Radiobutton fuer Szenario 2 gewaehlt
		if (a.getActionCommand().equals("radioButtonScenario2")){
			frame.getRadioButtonScenario1().setSelected(false);
		}
		
	}

	/** Funktion zum sauberen hinzufuegen von Attributen
	 * 
	 * @param oldString
	 * @param newString
	 */
	private void addToAnswer(String oldString, String newString){
		if (oldString == null){
			oldString = newString;
		} else {
			oldString = oldString + ", " + newString;
		}
	}
}