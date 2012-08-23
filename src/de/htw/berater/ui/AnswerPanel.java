package de.htw.berater.ui;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import de.htw.berater.controller.Answer;
import de.htw.berater.controller.Choice;
import de.htw.berater.controller.Question;
import de.htw.berater.controller.ChoiceType;

public class AnswerPanel extends JPanel {

	private List<String> answer = new ArrayList<String>();
	private final Question question;

	public AnswerPanel(Question question) {
		this.question = question;
		initialize();
	}

	private void initialize() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));


		for (int group : question.getChoices().keySet()) {
			ButtonGroup buttonGroup = new ButtonGroup();
			List<Choice> choices = question.getChoices().get(group);
			for (final Choice choice : choices) {
				JComponent component = null;
				if (choice.getChoiceType() == ChoiceType.INPUT) { //wegen preis = 500 z.b
					component = new JTextField();
				} else {
					JToggleButton tbtn;
					if (choice.getChoiceType() == ChoiceType.RADIO) {
						tbtn = new JRadioButton(choice.getText());
						buttonGroup.add(tbtn);
					} else {
						tbtn = new JCheckBox(choice.getText());
					}
					tbtn.addItemListener(new ItemListener() {
						@Override
						public void itemStateChanged(ItemEvent e) {
							if (e.getStateChange() == ItemEvent.SELECTED)
								answer.add(choice.getValue());
							else
								answer.remove(choice.getValue());
						}
					});
					component = tbtn;
				}
				add(component);
			}
		}
		
	}

	public Answer getAnswer() {
		for (Component componen : this.getComponents()) {
			if (componen instanceof JTextField) {
				answer.add(((JTextField)componen).getText());
			}
		}
		return new Answer(answer);
	}

}
