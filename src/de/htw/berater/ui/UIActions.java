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
			frame.getController().start(frame.getRdfPath(), frame.getNamespace());
			frame.resetStatus();
		}
		if (a.getActionCommand().equals("weiter")){
			try {
				frame.getController().answer(frame.getAnswerPanel().getAnswer());
			} catch (IllegalArgumentException e) {
				frame.onNewStatus(e.getMessage(), Color.red, 0);
			}
			return;
		}
	}

}
