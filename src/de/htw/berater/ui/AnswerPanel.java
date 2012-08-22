package de.htw.berater.ui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import de.htw.berater.controller.Answer;
import de.htw.berater.controller.Choice;
import de.htw.berater.controller.Question;
import de.htw.berater.controller.QuestionType;

public class AnswerPanel extends JPanel {

	private JTextField input;
	private List<String> answer = new ArrayList<String>();
	private final Question question;

	public AnswerPanel(Question question) {
		this.question = question;
		initialize();
	}

	private void initialize() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		if (question.getType() == QuestionType.INPUT) {
			input = new JTextField();
			add(input);
		} else {
			ButtonGroup buttonGroup = new ButtonGroup();
			for (final Choice choice : question.getChoices()) {
				JToggleButton btn;
				if (question.getType() == QuestionType.CHOICE)
					btn = new JRadioButton(choice.getText());
				else
					btn = new JCheckBox(choice.getText());
				btn.addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED)
							answer.add(choice.getValue());
						else
							answer.remove(choice.getValue());
					}
				});
				add(btn);
				if (question.getType() == QuestionType.CHOICE)
					buttonGroup.add(btn);
			}
		}
	}

	public Answer getAnswer() {
		if (question.getType() == QuestionType.INPUT)
			return new Answer(input.getText());
		return new Answer(answer);
	}

}
