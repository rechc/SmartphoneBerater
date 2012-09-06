package de.htw.berater.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import de.htw.berater.controller.Controller;
import de.htw.berater.controller.Question;
import de.htw.berater.db.Smartphone;

public class BeraterUIJFrame extends BeraterUI{

	private JFrame frame = new JFrame();
	
	
	private String rdfPath = "inferredSmartphones.rdf";
	private String namespace = "http://semantische-interoperabilitaet-projekt#";
	
	
	// aktuelle Frage
	private JLabel labelQuestion;

	// Panel fuer die Weiter - Taste
	private JPanel commit_panel;
	
	// Splitpanel mit den Antworten
	private JSplitPane answer_splitPane;
	
	
	// Panel für Übrige Handys
	private JPanel available_smartphones_panel;
	private JTable table;
	
	private Color statusBarColor;

	private SmartphoneTableModel tableModel;

	private JPanel answer_type2;

	private AnswerPanel answerPanel;
	
	private JTextField status_bar;

	/** Konstruktor mit Werte, initialisiert die UI
	 * 
	 * @param berater01
	 * @param berater02
	 */
	public BeraterUIJFrame() {
		frame.setBounds(100, 100, 1024, 786);
		frame.setTitle("Smartphone Berater");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// initialisiere GUI Komponenten
		initialize();
		
	}
	
	/** Initialisiert die Komponenten der View
	 * 
	 */
	private void initialize() {

		UIActions uiActions = new UIActions(this);
		JPanel szenario_mainpanel = new JPanel();
		frame.getContentPane().add(szenario_mainpanel, BorderLayout.NORTH);
		
		JPanel szenario_panel1 = new JPanel();
		szenario_mainpanel.add(szenario_panel1);
		szenario_panel1.setBounds(new Rectangle(10, 10, 10, 10));
		FlowLayout fl_szenario_panel1 = new FlowLayout(FlowLayout.CENTER, 10, 13);
		szenario_panel1.setLayout(fl_szenario_panel1);
		
		JPanel szenario_panel2 = new JPanel();
		szenario_mainpanel.add(szenario_panel2);
		szenario_panel2.setLayout(new BorderLayout(0, 0));
		
		JPanel szenario_panel3 = new JPanel();
		szenario_mainpanel.add(szenario_panel3);
		szenario_panel3.setFont(new Font("Tahoma", Font.PLAIN, 15));
		FlowLayout fl_szenario_panel3 = (FlowLayout) szenario_panel3.getLayout();
		fl_szenario_panel3.setVgap(13);
		fl_szenario_panel3.setHgap(20);
		fl_szenario_panel3.setAlignment(FlowLayout.LEFT);
		
		JButton buttonStartScenario = new JButton("Starten");
		buttonStartScenario.setFont(new Font("Tahoma", Font.PLAIN, 15));
		buttonStartScenario.setActionCommand("start");
		buttonStartScenario.addActionListener(uiActions);
		szenario_panel3.add(buttonStartScenario);
		
		JPanel mainPanel = new JPanel();
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new BorderLayout(0, 0));
				
		JPanel question_panel = new JPanel();
		question_panel.setMinimumSize(new Dimension(350, 10));
		FlowLayout fl_question_panel = (FlowLayout) question_panel.getLayout();
		fl_question_panel.setVgap(20);
		mainPanel.add(question_panel, BorderLayout.NORTH);
		
		labelQuestion = new JLabel("Wählen Sie bitte Starten");
		question_panel.add(labelQuestion);
		labelQuestion.setBounds(new Rectangle(10, 10, 10, 10));
		labelQuestion.setFont(new Font("Tahoma", Font.BOLD, 15));
		
		JPanel south_Panel = new JPanel();
		south_Panel.setLayout(new BoxLayout(south_Panel, BoxLayout.Y_AXIS));
		
		commit_panel = new JPanel();
		commit_panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		FlowLayout fl_commit_panel = (FlowLayout) commit_panel.getLayout();
		fl_commit_panel.setVgap(20);
		fl_commit_panel.setHgap(20);
		fl_commit_panel.setAlignment(FlowLayout.LEFT);
		
		commit_panel.setVisible(false);
		
		south_Panel.add(commit_panel);
		status_bar = new JTextField("");
		status_bar.setEditable(false);
		statusBarColor = status_bar.getBackground();
		south_Panel.add(status_bar);
		mainPanel.add(south_Panel, BorderLayout.SOUTH);
		
		JButton buttonGo = new JButton("Weiter");
		buttonGo.setFont(new Font("Tahoma", Font.PLAIN, 15));
		buttonGo.setActionCommand("weiter");
		buttonGo.addActionListener(uiActions);
		commit_panel.add(buttonGo);
		
		answer_type2 = new JPanel();
		answer_type2.setLayout(new BoxLayout(answer_type2, BoxLayout.Y_AXIS));
		
		//JLabel L2 = new JLabel("Wählen Sie die passenden Eigenschaften aus");
		//L2.setAlignmentX(Component.CENTER_ALIGNMENT);
		//answer_type2.add(L2);
		//L2.setBorder(null);
		//L2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setMaximumSize(new Dimension(32767, 20));
		answer_type2.add(separator_1);
		
		JScrollPane scrollLeft = new JScrollPane(answer_type2);
		
		available_smartphones_panel = new JPanel();
		available_smartphones_panel.setLayout(new BoxLayout(available_smartphones_panel, BoxLayout.PAGE_AXIS));
		
		JLabel labelAvailableSmartphones = new JLabel("Verfügbare Smartphones");
		labelAvailableSmartphones.setAlignmentX(Component.CENTER_ALIGNMENT);
		labelAvailableSmartphones.setHorizontalTextPosition(SwingConstants.CENTER);
		labelAvailableSmartphones.setHorizontalAlignment(SwingConstants.CENTER);
		labelAvailableSmartphones.setFont(new Font("Tahoma", Font.PLAIN, 15));
		available_smartphones_panel.add(labelAvailableSmartphones);

		JSeparator separator_3 = new JSeparator();
		separator_3.setMaximumSize(new Dimension(32767, 20));
		separator_3.setPreferredSize(new Dimension(0, 20));
		available_smartphones_panel.add(separator_3);

		tableModel = new SmartphoneTableModel();
		table = new JTable(tableModel);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setFillsViewportHeight(true);
		table.getColumn("Name").setPreferredWidth(150);
		JScrollPane scrollRight = new JScrollPane(table);
		available_smartphones_panel.add(scrollRight);

		answer_splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollLeft, available_smartphones_panel);
		mainPanel.add(answer_splitPane, BorderLayout.CENTER);
		answer_splitPane.setOneTouchExpandable(true);
		answer_splitPane.setMinimumSize(new Dimension(500, 500));
		answer_splitPane.setVisible(false);
		answer_splitPane.setDividerLocation(0.5);
		answer_splitPane.setResizeWeight(0.5);
		
		initializeAnswers();
	}
	
	
	/** Setzt eine neue Frage
	 * 
	 * @param newQuestion
	 */
	public void setNewQuestion(Question question){
		labelQuestion.setText(question.getText());
	}
	
	public AnswerPanel getAnswerPanel() {
		return answerPanel;
	}
	
	public void initializeAnswers(){
		commit_panel.setVisible(true);
		answer_splitPane.setVisible(true);
	}

	@Override
	public void onFalseQuestion(String string){
		JOptionPane.showMessageDialog(null, string, "Fehlerhafte Eingabe", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void onNewQuestion(Question question){
		answer_type2.removeAll();
		answer_type2.revalidate();
		answer_type2.repaint();
		answerPanel = new AnswerPanel(question);
		answer_type2.add(answerPanel);
		this.setNewQuestion(question);
	}
	
	@Override
	public void show(List<Smartphone> resultData) {
		if (resultData != null){
			JPanel tmpPanel = new JPanel();
			for (Smartphone r: resultData){
				tmpPanel.add(new JLabel(r.toString()));
			}
			this.setAvailable_smartphones_panel(tmpPanel);
		}
	}
	
	@Override
	public void onNewData(List<Smartphone> resultData) {
		tableModel.setPhoneList(resultData);
	}

	/** Funktion zum neuSetzen des Textes der Statusleiste
	 *  @param text - Der anzuzeigende Text
	 *  @param farbe - Die Hintergrundfarbe
	 *  @param maxSeconds - Die Anzeigedauer des Textes, 0 bedeutet: unbegrenzt, bleibt stehen bis es ueberschrieben wird
	 */
	@Override
	public void onNewStatus(String text, Color farbe, int maxSeconds){
		
		final String myText = text;
		final int myMaxSeconds = maxSeconds;
		if (maxSeconds > 0){
		
			Thread thread = new Thread(){
				@Override
				public void run(){
					for (int i = 0; i <= myMaxSeconds; i++){
						try {
							this.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//Prüfen ob eigener Text nicht bereits ueberschrieben wurde
						if (!status_bar.getText().equals(myText)){
							break;
						}
						//Text wieder von der Statusleiste entfernen
						if (i == myMaxSeconds) {
							status_bar.setText("");
							status_bar.setBackground(null);
						}
						
					}
				}
				
			};
			
			thread.start();
		}
		status_bar.setText(text);
		status_bar.setBackground(farbe);
		
	}
	
	/** Funktion zum setzen des Controllers
	 * 
	 * @param controller
	 */
	@Override
	public void setController(Controller controller) {
		this.controller = controller;
	}
	
	/** Funktion zum uebergeben des Controllers
	 * 
	 * @return
	 */
	public Controller getController(){
		return controller;
	}

	public JPanel getCommit_panel(){
		return commit_panel;
	}

	


	/**
	 * @return the available_smartphones_panel
	 */
	public JPanel getAvailable_smartphones_panel() {
		return available_smartphones_panel;
	}

	/**
	 * @param available_smartphones_panel the available_smartphones_panel to set
	 */
	public void setAvailable_smartphones_panel(
			JPanel available_smartphones_panel) {
		this.available_smartphones_panel = available_smartphones_panel;
	}

	@Override
	public void show() {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				frame.setVisible(true);
			}
		});
	}

	@Override
	public void resetStatus() {
		status_bar.setText("");
		status_bar.setBackground(statusBarColor);
	}

	public String getRdfPath() {
		return rdfPath;
	}

	public void setRdfPath(String rdfPath) {
		this.rdfPath = rdfPath;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
}
