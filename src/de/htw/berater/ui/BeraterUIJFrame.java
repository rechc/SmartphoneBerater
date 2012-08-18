package de.htw.berater.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import de.htw.berater.Berater;
import de.htw.berater.controller.Controller;
import de.htw.berater.db.Smartphone;

public class BeraterUIJFrame extends BeraterUI{

	private JFrame frame = new JFrame();
	
	private static final long serialVersionUID = 1L;

	// Antwortmoeglichkeit 1 Textfeld
	private JTextField txtAnswer;

	// Antwortmoeglichkeit 2 drop down
	private JComboBox answerBox;

	// Antwortmoeglichkeit 3 container mit Elementen
	private JPanel answer_panel_a3;
	
	// Enthaelt alle 3 Tabs
	private JTabbedPane tabbedPane;

	// aktuelle Frage
	private JLabel labelQuestion;

	// Antwortmoeglichkeiten (für Antwortmoeglichkeit 2)
	private String[] answer;

	// Panel fuer die Weiter - Taste
	private JPanel commit_panel;
	
	// Splitpanel mit den Antworten
	private JSplitPane answer_splitPane;
	
	// Radio Button Szenario 1
	private JRadioButton radioButtonScenario1;
	
	// Radio Button Szenario 2
	private JRadioButton radioButtonScenario2;
	
	// Panel für Übrige Handys
	private JPanel available_smartphones_panel;

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

		answer = new String[] {"Telefonieren, SMS, Internet, E-Mail, Spiele"};
		UIActions uiActions = new UIActions(this);
		JPanel szenario_mainpanel = new JPanel();
		frame.getContentPane().add(szenario_mainpanel, BorderLayout.NORTH);
		
		JPanel szenario_panel1 = new JPanel();
		szenario_mainpanel.add(szenario_panel1);
		szenario_panel1.setBounds(new Rectangle(10, 10, 10, 10));
		FlowLayout fl_szenario_panel1 = new FlowLayout(FlowLayout.CENTER, 10, 13);
		szenario_panel1.setLayout(fl_szenario_panel1);
		
		JLabel labelChooseScenario = new JLabel("Szenario ausw\u00E4hlen");
		labelChooseScenario.setFont(new Font("Tahoma", Font.PLAIN, 15));
		szenario_panel1.add(labelChooseScenario);
		
		JPanel szenario_panel2 = new JPanel();
		szenario_mainpanel.add(szenario_panel2);
		szenario_panel2.setLayout(new BorderLayout(0, 0));
		
		radioButtonScenario1 = new JRadioButton("Szenario 1 (Kunde hat keine Ahnung)");
		radioButtonScenario1.setSelected(true);
		radioButtonScenario1.setActionCommand("radioButtonScenario1");
		radioButtonScenario1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		radioButtonScenario1.addActionListener(uiActions);
		szenario_panel2.add(radioButtonScenario1, BorderLayout.NORTH);
		
		radioButtonScenario2 = new JRadioButton("Szenario 2 (Update auf leistungsst\u00E4rkeres Ger\u00E4t)");
		radioButtonScenario2.setActionCommand("radioButtonScenario2");
		radioButtonScenario2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		radioButtonScenario2.addActionListener(uiActions);
		szenario_panel2.add(radioButtonScenario2, BorderLayout.SOUTH);
		
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
		
		labelQuestion = new JLabel("<html>W&uuml;hlen Sie bitte das Szenario aus und klicken Sie anschlie&szlig;end auf Starten</hmtl>");
		question_panel.add(labelQuestion);
		labelQuestion.setBounds(new Rectangle(10, 10, 10, 10));
		labelQuestion.setFont(new Font("Tahoma", Font.BOLD, 15));
		
		commit_panel = new JPanel();
		commit_panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		FlowLayout fl_commit_panel = (FlowLayout) commit_panel.getLayout();
		fl_commit_panel.setVgap(20);
		fl_commit_panel.setHgap(20);
		fl_commit_panel.setAlignment(FlowLayout.LEFT);
		mainPanel.add(commit_panel, BorderLayout.SOUTH);
		commit_panel.setVisible(false);
		
		JButton buttonGo = new JButton("Weiter");
		buttonGo.setFont(new Font("Tahoma", Font.PLAIN, 15));
		buttonGo.setActionCommand("weiter");
		buttonGo.addActionListener(uiActions);
		commit_panel.add(buttonGo);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		available_smartphones_panel = new JPanel();
		JScrollPane scrollLeft = new JScrollPane(tabbedPane);
		
		JScrollPane scrollRight = new JScrollPane(available_smartphones_panel);
		
		answer_splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollLeft, scrollRight);
		mainPanel.add(answer_splitPane, BorderLayout.CENTER);
		answer_splitPane.setOneTouchExpandable(true);
		answer_splitPane.setMinimumSize(new Dimension(500, 500));
		answer_splitPane.setVisible(false);

		
		JPanel answer_type1 = new JPanel();
		tabbedPane.addTab("Direkteingabe", null, answer_type1, null);
		answer_type1.setAlignmentX(Component.LEFT_ALIGNMENT);
		answer_type1.setMinimumSize(new Dimension(250, 10));
		answer_type1.setLayout(new BoxLayout(answer_type1, BoxLayout.PAGE_AXIS));
		
		JLabel L1 = new JLabel("Geben Sie hier die Antwort des Kunden ein");
		L1.setAlignmentX(Component.CENTER_ALIGNMENT);
		answer_type1.add(L1);
		L1.setHorizontalTextPosition(SwingConstants.LEFT);
		L1.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		L1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		JSeparator separator = new JSeparator();
		separator.setMaximumSize(new Dimension(32767, 20));
		answer_type1.add(separator);
		
		//Direkt-Eingabe der Antwort
		txtAnswer = new JTextField();
		txtAnswer.setMaximumSize(new Dimension(250, 25));
		txtAnswer.setMinimumSize(new Dimension(250, 25));
		txtAnswer.setPreferredSize(new Dimension(250, 25));
		txtAnswer.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtAnswer.setText("");
		answer_type1.add(txtAnswer);
		txtAnswer.setColumns(1);
		
		
		//Direkt-Auswahl der Antwort
		JPanel answer_type2 = new JPanel();
		tabbedPane.addTab("Direkt-Auswahl", null, answer_type2, null);
		answer_type2.setLayout(new BoxLayout(answer_type2, BoxLayout.Y_AXIS));
		
		JLabel L2 = new JLabel("<html>W&auml;hlen Sie bitte die passende Antwort aus</html>");
		L2.setAlignmentX(Component.CENTER_ALIGNMENT);
		answer_type2.add(L2);
		L2.setBorder(null);
		L2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setMaximumSize(new Dimension(32767, 20));
		answer_type2.add(separator_1);
		
		answerBox = new JComboBox();
		answerBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		answerBox.setMaximumSize(new Dimension(321, 30));
		answerBox.setModel(new DefaultComboBoxModel(answer));
		answerBox.setPreferredSize(new Dimension(250, 30));
		answer_type2.add(answerBox);
		
		JPanel answer_type3 = new JPanel();
		tabbedPane.addTab("Detail-Auswahl", null, answer_type3, null);
		answer_type3.setLayout(new BoxLayout(answer_type3, BoxLayout.PAGE_AXIS));
		
		JLabel L3 = new JLabel("W\u00E4hlen Sie die passenden Eigenschaften aus");
		answer_type3.add(L3);
		L3.setAlignmentX(Component.CENTER_ALIGNMENT);
		L3.setHorizontalTextPosition(SwingConstants.LEFT);
		L3.setHorizontalAlignment(SwingConstants.LEFT);
		L3.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setPreferredSize(new Dimension(0, 20));
		separator_2.setMinimumSize(new Dimension(0, 20));
		separator_2.setMaximumSize(new Dimension(32767, 20));
		answer_type3.add(separator_2);
		
		answer_panel_a3 = getPropertyPanel();
		answer_type3.add(answer_panel_a3);
		
		available_smartphones_panel.setLayout(new BoxLayout(available_smartphones_panel, BoxLayout.PAGE_AXIS));
	
		
		JLabel labelAvailableSmartphones = new JLabel("Verf\u00FCgbare Smartphones");
		labelAvailableSmartphones.setAlignmentX(Component.CENTER_ALIGNMENT);
		labelAvailableSmartphones.setHorizontalTextPosition(SwingConstants.CENTER);
		labelAvailableSmartphones.setHorizontalAlignment(SwingConstants.CENTER);
		labelAvailableSmartphones.setFont(new Font("Tahoma", Font.PLAIN, 15));
		available_smartphones_panel.add(labelAvailableSmartphones);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setMaximumSize(new Dimension(32767, 20));
		separator_3.setPreferredSize(new Dimension(0, 20));
		available_smartphones_panel.add(separator_3);
		
	}
	
	/* erstellt ein JPanel mit allen Propertys der Antwortmöglickeit 3
	 * 
	 */
	private JPanel getPropertyPanel(){
		JPanel tmpPanel = new JPanel();
		tmpPanel.setPreferredSize(new Dimension(300, 450));
		tmpPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JCheckBox chkbxTelefonieren = new JCheckBox("Telefonieren", true);
		chkbxTelefonieren.setName("telefonieren");
		chkbxTelefonieren.setSelected(true);
		chkbxTelefonieren.setEnabled(false);
		tmpPanel.add(chkbxTelefonieren);
		
		JCheckBox chkbxSms = new JCheckBox("SMS", true);
		chkbxSms.setName("sms");
		chkbxSms.setSelected(true);
		chkbxSms.setEnabled(false);
		tmpPanel.add(chkbxSms);
		
		JCheckBox chkbxInternet = new JCheckBox("Internet", true);
		chkbxInternet.setName("internet");
		chkbxInternet.setName("sms");
		chkbxInternet.setSelected(true);
		chkbxInternet.setEnabled(false);
		tmpPanel.add(chkbxInternet);
		
		// email
		JCheckBox chkbxEmail = new JCheckBox("E-Mail", true);
		chkbxEmail.setName("email");
		chkbxEmail.setSelected(true);
		chkbxEmail.setEnabled(false);
		tmpPanel.add(chkbxEmail);
		
		// Outdoor Checkbox
		JCheckBox chkbxOutdoor = new JCheckBox("Outdoor-Handy");
		chkbxOutdoor.setName("outdoor");
		tmpPanel.add(chkbxOutdoor);
		
		// Navi Checkbox
		JCheckBox chkbxNavi = new JCheckBox("Navigation");
		chkbxNavi.setName("navi");
		tmpPanel.add(chkbxNavi);
		
		// Spiele Checkbox
		JCheckBox chkbxSpiele = new JCheckBox("Spiele");
		chkbxSpiele.setName("spiele");
		tmpPanel.add(chkbxSpiele);

		// Spiele-Auswahl Combobox
		JPanel spielePanel = new JPanel();
		spielePanel.add(new JLabel("Spiele-Auswahl:"));
		
		JComboBox comboSpiele = new JComboBox();
		ComboboxData[] spieleData = new ComboboxData[3];
		spieleData[0] = new ComboboxData("", "");
		spieleData[1] = new ComboboxData("normale Spiele", "normales");
		spieleData[2] = new ComboboxData("gute Spiele", "gutes");
		comboSpiele.setModel(new DefaultComboBoxModel(spieleData));
		spielePanel.add(comboSpiele);
		tmpPanel.add(spielePanel);
		
		//Displaygroesse
		JPanel panelDisplaygroesse = new JPanel();
		panelDisplaygroesse.add(new JLabel("Displaygroesse"));

		ComboboxData[] displaygroesseData = new ComboboxData[3];
		displaygroesseData[0] = new ComboboxData("", "");
		displaygroesseData[1] = new ComboboxData("klein", "kleines");
		displaygroesseData[2] = new ComboboxData("<html>gro&szlig;</html>", "grosses");
		JComboBox comboDisplaygroesse = new JComboBox();
		comboDisplaygroesse.setModel(new DefaultComboBoxModel(displaygroesseData));
		panelDisplaygroesse.add(comboDisplaygroesse);
		tmpPanel.add(panelDisplaygroesse);

		//Hardwaretastatur
		JPanel panelHardwaretastatur = new JPanel();
		panelHardwaretastatur.add(new JLabel("Hardwaretastatur"));

		ComboboxData[] hardwaretastaturData = new ComboboxData[3];
		hardwaretastaturData[0] = new ComboboxData("", "");
		hardwaretastaturData[1] = new ComboboxData("ja", "hardwaretastaur");
		hardwaretastaturData[2] = new ComboboxData("nein", "keinetastatur");
		JComboBox comboHardwaretastatur = new JComboBox();
		comboHardwaretastatur.setModel(new DefaultComboBoxModel(hardwaretastaturData));
		panelHardwaretastatur.add(comboHardwaretastatur);
		tmpPanel.add(panelHardwaretastatur);

		//Kamera
		JPanel panelKamera = new JPanel();
		panelKamera.add(new JLabel("Kamera"));

		ComboboxData[] kameraData = new ComboboxData[3];
		kameraData[0] = new ComboboxData("", "");
		kameraData[1] = new ComboboxData("normale Kamera", "kamera");
		kameraData[2] = new ComboboxData("gute Kamera", "gutekamera");
		JComboBox comboKamera = new JComboBox();
		comboKamera.setModel(new DefaultComboBoxModel(kameraData));
		panelKamera.add(comboKamera);
		tmpPanel.add(panelKamera);

		/**
		//Aufloesung
		JPanel panelAufloesung = new JPanel();
		panelAufloesung.add(new JLabel("<html>Aufl&ouml;sung</html>"));

		JTextField txtAufloesung = new JTextField();
		txtAufloesung.setName("aufloesung");
		txtAufloesung.setColumns(10);
		panelAufloesung.add(txtAufloesung);
		tmpPanel.add(panelAufloesung);
		 **/
		
		//Betriebssystem
		JPanel panelBetriebssystem = new JPanel();
		panelBetriebssystem.add(new JLabel("Betriebssystem"));

		ComboboxData[] betriebssystemData = new ComboboxData[6];
		betriebssystemData[0] = new ComboboxData("", "");
		betriebssystemData[1] = new ComboboxData("Android", "android");
		betriebssystemData[2] = new ComboboxData("IOS", "ios");
		betriebssystemData[3] = new ComboboxData("Bada", "bada");
		betriebssystemData[4] = new ComboboxData("Symbian", "symbian");
		betriebssystemData[5] = new ComboboxData("Anderes", "other");
		JComboBox comboBetriebssystem = new JComboBox();
		comboBetriebssystem.setModel(new DefaultComboBoxModel(betriebssystemData));
		panelBetriebssystem.add(comboBetriebssystem);
		tmpPanel.add(panelBetriebssystem);

		//Marke
		JPanel panelMarke = new JPanel();
		panelMarke.add(new JLabel("Marke"));

		JTextField txtMarke = new JTextField();
		txtMarke.setName("marke");
		txtMarke.setColumns(10);
		panelMarke.add(txtMarke);
		tmpPanel.add(panelMarke);

		// Name
		JPanel panelName = new JPanel();
		panelName.add(new JLabel("Name"));
				
		JTextField txtName = new JTextField();
		txtName.setName("name");
		txtName.setColumns(10);
		panelName.add(txtName);
		
		tmpPanel.add(panelName);
		
		
		
		return tmpPanel;
	}
	
	/** Setzt eine neue Frage
	 * 
	 * @param newQuestion
	 */
	public void setNewQuestion(String newQuestion){
		labelQuestion.setText(newQuestion);
	}
	
	/** Setzt EINE neue Antwort
	 *  
	 * @param newAnswer
	 */
	public void setNewAnswer(String newAnswer){
		answer = new String[1];
		answer[0] = newAnswer;
		answerBox.setModel(new DefaultComboBoxModel(answer));
	}
	
	/** Setzt mehrere neue Antworten
	 * 
	 * @param newAnswer
	 */
	public void setNewAnswer(String[] newAnswer){
		answer = newAnswer;
		answerBox.setModel(new DefaultComboBoxModel(answer));
	}

	/** gibt das Pane mit den Tabs zurueck
	 * 
	 * @return
	 */
	public JTabbedPane getTabbedPane(){
		return tabbedPane;
	}
	
	/** Gibt die Antwort zurueck, falls Antwortmoeglichkeit 1 ausgewaehlt
	 * 
	 * @return
	 */
	public String getTxtAnswer(){
		return txtAnswer.getText();
	}
	
	/** Gibt die Comboboy mit Antworten zurueck, falls Antwortmoeglichkeit 2 ausgewaehlt
	 * 
	 * @return
	 */
	public JComboBox getAnswerBox(){
		return answerBox;
	}
	
	/** Gibt das Panel mit allen Eigenschaften zurueck, falls Antwortmoeglichkeit 3 ausgewaehlt
	 * 
	 * @return
	 */
	public JPanel getAnswer_panel_a3(){
		return answer_panel_a3;
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
	public void onNewQuestion(String newQuestion){
		this.setNewQuestion(newQuestion);
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
		//System.out.println("werde aufgerufen");
		if (resultData != null){
			//JPanel tmpPanel = new JPanel();
			for (Smartphone r: resultData){
				//tmpPanel.add(new JLabel(r.toString()));
				available_smartphones_panel.add(new JLabel(r.toString()));
			}
			//this.setAvailable_smartphones_panel(tmpPanel);
		}
	}

	/** Funktion zum setzen des Controllers
	 * 
	 * @param controller
	 */
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
	 * @return the radioButtonScenario1
	 */
	public JRadioButton getRadioButtonScenario1() {
		return radioButtonScenario1;
	}

	/**
	 * @param radioButtonScenario1 the radioButtonScenario1 to set
	 */
	public void setRadioButtonScenario1(JRadioButton radioButtonScenario1) {
		this.radioButtonScenario1 = radioButtonScenario1;
	}

	/**
	 * @return the radioButtonScenario2
	 */
	public JRadioButton getRadioButtonScenario2() {
		return radioButtonScenario2;
	}

	/**
	 * @param radioButtonScenario2 the radioButtonScenario2 to set
	 */
	public void setRadioButtonScenario2(JRadioButton radioButtonScenario2) {
		this.radioButtonScenario2 = radioButtonScenario2;
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
	

	public class ComboboxData{
		private String text;
		private String name;
		
		public ComboboxData(String text, String name){
			this.text = text;
			this.name = name;
		}
		
		public String getText(){
			return text;
		}
		public String getName(){
			return name;
		}
		
		public String toString(){
			return text;
		}
}
}
