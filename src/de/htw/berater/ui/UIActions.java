package de.htw.berater.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.htw.berater.Berater;
import de.htw.berater.controller.FalseAnswerException;
import de.htw.berater.db.SQLClient;
import de.htw.berater.ui.BeraterUIJFrame.ComboboxData;

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
			
			String answer = "";
			
			switch (actualAnswerPanel){
				case 0: // Direkteingabe-View ist aktiv
						answer = frame.getTxtAnswer();
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
									if (tmpChkBox.isEnabled()){
										answer = tmpChkBox.getName();
										tmpChkBox.setEnabled(false);
									}
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
											if (tmpTextField.isEnabled()){
												answer = tmpTextField.getText();
												tmpTextField.setEnabled(false);
											}
										}
									}
									
								} 
								// aktuelles Objekt == JComboBox?
								if (tmpPanel.getComponent(tmpPanel.getComponentCount() -1) instanceof JComboBox){
									JComboBox tmpComboBox = (JComboBox) tmpPanel.getComponent(tmpPanel.getComponentCount() -1);
									if (!((ComboboxData)tmpComboBox.getSelectedItem()).getName().equals("")){
										if (tmpComboBox.isEnabled()){
											answer = ((ComboboxData)tmpComboBox.getSelectedItem()).getName();
											tmpComboBox.setEnabled(false);
										}
									}
									
								}
							}
						}
					
				default:
					break;
			
			}
			// Antwort an Controller uebergeben
			try {
				frame.getController().answer(answer);
			} catch (FalseAnswerException e) {
				e.printStackTrace();
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

}