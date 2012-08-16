package de.htw.berater.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.htw.berater.db.ResultData;
import de.htw.berater.db.SQLClient;

public class UIActions implements ActionListener {

	/**
	 * Action Listener fuer Start und Weiter
	 */
	@Override
	public void actionPerformed(ActionEvent a) {
		if (a.getActionCommand().equals("start")){
			
			// noch kein Szenario initialisiert? dann jetzt Panels aktivieren 
			if (!BeraterUI.getCommit_panel().isVisible()){
				BeraterUI.initializeAnswers();
			}
			
			// Szenario 1 starten
			if (BeraterUI.getRadioButtonScenario1().isSelected()){
				BeraterUI.setBerater(BeraterUI.getBerater1());
				BeraterUI.getController().setBerater(BeraterUI.getBerater());
				BeraterUI.setNewQuestion(BeraterUI.getBerater().askFirstQuestionZweck());
		
			// Szenario 2 starten
			} else {
				BeraterUI.setBerater(BeraterUI.getBerater2());
				BeraterUI.getController().setBerater(BeraterUI.getBerater());
				BeraterUI.setNewQuestion(BeraterUI.getBerater().askFirstQuestionGeneral());
			}
		}
		
		if (a.getActionCommand().equals("weiter")){
			//getSelectedIndex()
			//View tmpView = siGui.getView();
			int actualAnswerPanel = BeraterUI.getTabbedPane().getSelectedIndex();
			
			String answer = null;
			
			switch (actualAnswerPanel){
				case 0: // Direkteingabe-View ist aktiv
						answer = BeraterUI.getTxtAnswer();
						String newQuestion = BeraterUI.getBerater().evaluateAndAskNewQuestion(answer);
						if (newQuestion != null) { 
							BeraterUI.setNewQuestion(newQuestion);
							BeraterUI.onNewData(SQLClient.getInstance().getResultData(BeraterUI.getBerater().getSQLString()));
						}
						break;
						
				case 1: // Direktauswahl-View ist aktiv
						answer = BeraterUI.getAnswerBox().getSelectedItem().toString();
						break;
						
				case 2: // Detailauswahl-View ist aktiv
						JPanel tmpAnswers = BeraterUI.getAnswer_panel_a3();
						
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
			BeraterUI.getRadioButtonScenario2().setSelected(false);
		}
		// Radiobutton fuer Szenario 2 gewaehlt
		if (a.getActionCommand().equals("radioButtonScenario2")){
			BeraterUI.getRadioButtonScenario1().setSelected(false);
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