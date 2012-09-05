package de.htw.berater.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
				frame.onNewStatus("Szenario 1 gestartet", Color.YELLOW, 5);
		
			// Szenario 2 starten
			} else {
				frame.getController().getFirstQuestion(false);
				frame.onNewStatus("Szenario 2 gestartet", Color.YELLOW, 5);
			}
		}
		
		if (a.getActionCommand().equals("weiter")){
			try {
				frame.getController().answer(frame.getAnswerPanel().getAnswer());
			} catch (IllegalArgumentException e) {
				frame.onNewStatus(e.getMessage(), Color.red, 0);
			}
			return;
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
