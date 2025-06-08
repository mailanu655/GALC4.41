package com.honda.galc.client.teamleader.hold.qsr.release.vinseq;

import java.awt.Component;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.Document;

import net.miginfocom.swing.MigLayout;
import com.honda.galc.client.teamleader.hold.qsr.release.ReleaseInputPanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledListBox;
import com.honda.galc.client.ui.component.LimitedLengthPlainDocument;
import com.honda.galc.client.ui.component.UpperCaseDocument;


public class ReleaseBySeqInputPanel extends ReleaseInputPanel {

	private static final long serialVersionUID = 1L;

	
	private JLabel productLabel;
	private JTextField productInput;
	
	private JLabel startVinSeqLabel;
	private JTextField startVinSeqInput;
		
	private JLabel endVinSeqLabel;
	private JTextField endVinSeqInput;
	
	private JLabel startTimeLabel;
	private JFormattedTextField startTimeInput;
	private JButton startTimeButton;

	private JLabel endTimeLabel;
	private JFormattedTextField endTimeInput;
	private JButton endTimeButton;

	private DateFormat dateFormat;
	private String datePattern;
	
	private LabeledListBox modelYearListElement;
	private LabeledListBox modelCodeListElement;
	private LabeledListBox modelTypeListElement;
	private LabeledListBox modelDestListElement;
	
	
	
	private JButton vinButton;
	
	private JButton clearButton;

	public ReleaseBySeqInputPanel(ReleaseBySeqPanel parentPanel) {
		super(parentPanel);
	}

	protected void initView() {
		super.initView();
		
		setDatePattern("yyyy-MM-dd HH:mm:ss");
		setDateFormat(new SimpleDateFormat(getDatePattern()));
		
		this.productLabel = createProductLabel();
		this.vinButton = createVinButton();
		this.productInput = createProductInput(getVinButton());
		
		this.startVinSeqLabel = createStartVinSeqLabel(getProductInput());
		this.startVinSeqInput = createStartVinSeqInput(getStartVinSeqLabel());
		this.endVinSeqLabel = createEndVinSeqLabel(getStartVinSeqInput());
		this.endVinSeqInput = createEndVinSeqInput(getEndVinSeqLabel(), getEndVinSeqLabel());
		
		this.startTimeLabel = createStartTimeLabel(getEndVinSeqInput());
		this.startTimeInput = createStartTimeInput(getStartTimeLabel());
		this.startTimeButton = createStartTimeButton(getStartTimeInput());

		this.endTimeLabel = createEndTimeLabel(getStartTimeLabel());
		this.endTimeInput = createEndTimeInput(getStartTimeInput());
		this.endTimeButton = createEndTimeButton(getEndTimeInput());
		
		this.modelYearListElement = createModelYearListElement(getQsrElement());
		this.modelCodeListElement = createModelCodeListElement(getModelYearListElement());
		this.modelTypeListElement = createModelTypeListElement(getModelCodeListElement());
		
		this.clearButton =  createClearButton();
		
		JPanel rangeSelectionPanel = new JPanel(new MigLayout());
		rangeSelectionPanel.setBorder(BorderFactory.createEtchedBorder());
			
		JPanel productIdPanel = new JPanel(new MigLayout());
		productIdPanel.add(getVinButton(),"width 50::,height 50");
		productIdPanel.add(getProductInput(),"width 150::, height 50");
		rangeSelectionPanel.add(productIdPanel);
		
		JPanel vinSeqRangePanel = new JPanel(new MigLayout());
		vinSeqRangePanel.add(getStartVinSeqLabel(),"width 50::");
		vinSeqRangePanel.add(getStartVinSeqInput(),"width 100::,wrap");
		vinSeqRangePanel.add(getEndVinSeqLabel(),"width 50::");
		vinSeqRangePanel.add(getEndVinSeqInput(), "width 100::");
		rangeSelectionPanel.add(vinSeqRangePanel,"wrap");	
		
		JPanel dateRangePanel = new JPanel(new MigLayout());
		dateRangePanel.add(getStartTimeLabel(),"width 50");
		dateRangePanel.add(getStartTimeInput(),"width 150");
		dateRangePanel.add(getStartTimeButton(),"width 10,wrap");
		
		dateRangePanel.add(getEndTimeLabel(),"width 50");
		dateRangePanel.add(getEndTimeInput(),"width 150");
		dateRangePanel.add(getEndTimeButton(),"width 10,wrap");
	
		rangeSelectionPanel.add(dateRangePanel,"east");	
		 
			
		add(rangeSelectionPanel, " span 3");

		JPanel specSelectionPanel = new JPanel(new MigLayout());
		specSelectionPanel.setBorder(BorderFactory.createEtchedBorder());
		specSelectionPanel.setSize(400, 150);
		specSelectionPanel.add(getModelYearListElement(),"width 100::");
		specSelectionPanel.add(getModelCodeListElement(),"width 100::");
		specSelectionPanel.add(getModelTypeListElement(),"width 100::");
		
		add(specSelectionPanel);
		add(getClearButton());

	}
	
	
	public JLabel getStartVinSeqLabel() {
		return startVinSeqLabel;
	}

	public JTextField getEndVinSeqInput() {
		return endVinSeqInput;
	}

	public JLabel getEndVinSeqLabel() {
		return endVinSeqLabel;
	}

	public JTextField getStartVinSeqInput() {
		return startVinSeqInput;
	}
	
	public JLabel getProductLabel() {
		return productLabel;
	}
	
	public JTextField getProductInput() {
		return productInput;
	}

	public JButton getVinButton() {
		return vinButton;
	}
	
	
	public JLabel createProductLabel() {
		JLabel element = new JLabel("Vin", JLabel.RIGHT);
		element.setFont(Fonts.DIALOG_BOLD_12);
		element.setSize(60, 25);
		element.setLocation(0, 4);
	
		return element;
	}
	
	public JTextField createProductInput(Component base) {
		JTextField element = new JTextField();
		Document document = new UpperCaseDocument(20);
		element.setDocument(document);
		element.setFont(Fonts.DIALOG_BOLD_12);
		element.setSize(220, base.getHeight());
		element.setLocation(base.getX() + base.getWidth() + 5, base.getY());
		return element;
	}
			
	public JLabel createStartVinSeqLabel(Component base) {
		JLabel element = new JLabel("Start Seq", JLabel.RIGHT);
		element.setFont(Fonts.DIALOG_BOLD_12);
		element.setSize(60, 25);
		element.setLocation(base.getX() + base.getWidth() + 5, base.getY() );
		return element;
	}
		
	public JTextField createStartVinSeqInput(Component base) {
		JTextField element = new JTextField();
		Document document = new UpperCaseDocument(20);
		element.setDocument(document);
		element.setFont(Fonts.DIALOG_BOLD_12);
		element.setSize(100, base.getHeight());
		element.setLocation(base.getX() + base.getWidth() + 5, base.getY());
		return element;
	}

	public JLabel createEndVinSeqLabel(Component base) {
		JLabel element = new JLabel("End Seq ", JLabel.CENTER);
		element.setFont(Fonts.DIALOG_BOLD_12);
		element.setSize(20, base.getHeight());
		element.setLocation(base.getX() + base.getWidth(), base.getY() );
		return element;
	}

	
	public JTextField createEndVinSeqInput(Component cx, Component cy) {
		JTextField element = new JTextField();
		Document document = new UpperCaseDocument(20);
		element.setDocument(document);
		element.setFont(Fonts.DIALOG_BOLD_12);
		element.setSize(100, cx.getHeight());
		element.setLocation(cx.getX()+cx.getWidth(), cy.getY());
		return element;
	}
	
	public LabeledListBox getModelYearListElement() {
		return modelYearListElement;
	}
	
	public LabeledListBox getModelCodeListElement() {
		return modelCodeListElement;
	}

	public LabeledListBox getModelTypeListElement() {
		return modelTypeListElement;
	}

	public LabeledListBox getModelDestListElement() {
		return modelDestListElement;
	}
	
	protected LabeledListBox createModelYearListElement(Component base) {
		LabeledListBox element = new LabeledListBox("Model Year");
		element.setFont(Fonts.DIALOG_BOLD_12);
		element.setLocation(base.getX() + 10 , base.getY());
		return element;
	}
	protected LabeledListBox createModelCodeListElement(Component base) {
		LabeledListBox element = new LabeledListBox("Model Code");
		element.setFont(Fonts.DIALOG_BOLD_12);
		element.setLocation(base.getX() + base.getWidth()+10 , base.getY());
		return element;
	}
	
	protected LabeledListBox createModelTypeListElement(Component base) {
		LabeledListBox element = new LabeledListBox("Type");
		element.setFont(Fonts.DIALOG_BOLD_12);
		element.setLocation(base.getX() + base.getWidth()+10, base.getY());
		return element;
	}
	
	public JLabel createStartProductionLotLabel(Component base) {
		JLabel element = new JLabel("From Date :", JLabel.RIGHT);
		element.setFont(Fonts.DIALOG_BOLD_12);
		element.setSize(80, 25);
		element.setLocation(base.getX() + base.getWidth(), base.getY());
		return element;
	}

	public JTextField createStartProductionLotInput(Component base) {
		JTextField element = new JTextField();
		Document document = new UpperCaseDocument(20);
		element.setDocument(document);
		element.setFont(Fonts.DIALOG_BOLD_12);
		element.setSize(140, base.getHeight());
		element.setLocation(base.getX() + base.getWidth() + 5, base.getY());
		return element;
	}

	public JLabel createEndProductionLotLabel(Component base) {
		JLabel element = new JLabel("To Date :", JLabel.RIGHT);
		element.setFont(Fonts.DIALOG_BOLD_12);
		element.setSize(base.getWidth(), base.getHeight());
		element.setLocation(base.getX(), base.getY() + base.getHeight() + 2);
		return element;
	}

	public JTextField createEndProductionLotInput(Component cx, Component cy) {
		JTextField element = new JTextField();
		Document document = new UpperCaseDocument(20);
		element.setDocument(document);
		element.setFont(Fonts.DIALOG_BOLD_12);
		element.setSize(cx.getWidth(), cx.getHeight());
		element.setLocation(cx.getX(), cy.getY());
		return element;
	}
	
	public JButton createVinButton(){
		JButton element = new JButton();
		element.setEnabled(false);
		element.setText("VIN");
		element.setSize(60, 45);
		element.setLocation(2,2);
		return element;
	}
	
	public JLabel createStartTimeLabel(Component base) {
		String label = "<HTML><p align='right'>Start Time <br> "+ "("+ getDatePattern().toUpperCase() + ") </p></HTML>";
		JLabel element = new JLabel(label, JLabel.RIGHT);
		element.setFont(Fonts.DIALOG_BOLD_12);
		element.setSize(100, 45);
		element.setLocation(base.getX() + base.getWidth() + 20, base.getY());
		return element;
	}

	public JFormattedTextField createStartTimeInput(Component base) {
		JFormattedTextField element = new JFormattedTextField(getDateFormat()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void commitEdit() throws ParseException {
				if (getText().trim().length() == 0) {
					setValue(null);
				} else {
					super.commitEdit();
				}
			}
		};
		Document document = new LimitedLengthPlainDocument(19);
		element.setDocument(document);
		element.setFont(Fonts.DIALOG_BOLD_12);
		element.setSize(275, base.getHeight());
		element.setLocation(base.getX() + base.getWidth() + 5, base.getY());
		return element;
	}

	public JButton createStartTimeButton(Component base){
		startTimeButton = new JButton();
		startTimeButton.setRolloverIcon(null);
		startTimeButton.setText("");
		startTimeButton.setIcon(new ImageIcon(getClass().getResource("/resource/com/honda/galc/client/qsr/Date-and-Time-icon.png")));
		startTimeButton.setSize(20, 15);
		startTimeButton.setLocation(base.getX() + base.getWidth() + 5, base.getY());
		return startTimeButton;
	}
	
	public JLabel createEndTimeLabel(Component cx) {
		String label = "<HTML><p align='right'>End Time <br> "+ "("+ getDatePattern().toUpperCase() + ") </p></HTML>";
		JLabel element = new JLabel(label, JLabel.RIGHT);
		element.setFont(Fonts.DIALOG_BOLD_12);
		element.setSize(100,45);
		element.setLocation(cx.getX(), cx.getY());
		return element;
	}

	public JButton createEndTimeButton(Component base){
		endTimeButton = new JButton();
		endTimeButton.setRolloverIcon(null);
		endTimeButton.setText("");
		endTimeButton.setIcon(new ImageIcon(getClass().getResource("/resource/com/honda/galc/client/qsr/Date-and-Time-icon.png")));
		endTimeButton.setSize(20, 15);
		endTimeButton.setLocation(base.getX() + base.getWidth() + 5, base.getY());
		return endTimeButton;
	}
	
	public JFormattedTextField createEndTimeInput(Component cx) {
		JFormattedTextField element = new JFormattedTextField(getDateFormat()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void commitEdit() throws ParseException {
				if (getText().trim().length() == 0) {
					setValue(null);
				} else {
					super.commitEdit();
				}
			}
		};

		Document document = new LimitedLengthPlainDocument(19);
		element.setDocument(document);
		element.setFont(Fonts.DIALOG_BOLD_12);
		element.setEditable(true);
		element.setSize(cx.getWidth(), cx.getHeight());
		element.setLocation(cx.getX(), cx.getY());
		return element;
	}

	// === get/set === //
	public JFormattedTextField getEndTimeInput() {
		return endTimeInput;
	}

	public JLabel getEndTimeLabel() {
		return endTimeLabel;
	}

	public JFormattedTextField getStartTimeInput() {
		return startTimeInput;
	}

	public JLabel getStartTimeLabel() {
		return startTimeLabel;
	}
	
	public JButton getStartTimeButton() {
		return startTimeButton;
	}

	public JButton getEndTimeButton() {
		return endTimeButton;
	}
	
	public DateFormat getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	public Date getStartTime() {
		return (Date) getStartTimeInput().getValue();
	}

	public Date getEndTime() {
		return (Date) getEndTimeInput().getValue();
	}

	public String getDatePattern() {
		return datePattern;
	}

	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

	public void setInputEnabled(boolean enabled) {
		getVinButton().setEnabled(enabled);
		getProductInput().setEnabled(enabled);
		getStartVinSeqInput().setEnabled(enabled);
		getEndVinSeqInput().setEnabled(enabled);
		getStartTimeInput().setEnabled(enabled);
		getEndTimeInput().setEnabled(enabled);
		getStartTimeButton().setEnabled(enabled);
		getEndTimeButton().setEnabled(enabled);
		
	}

	public void resetInput() {
		getProductInput().setText("");
		getStartVinSeqInput().setText("");
		getEndVinSeqInput().setText("");
		getStartTimeInput().setText("");
		getEndTimeInput().setText("");
		getModelYearListElement().getComponent().clearSelection();
		getModelCodeListElement().getComponent().clearSelection();
		getModelTypeListElement().getComponent().clearSelection();
	}
	
	public JButton createClearButton(){
		JButton element = new JButton();
		element.setFont(Fonts.DIALOG_BOLD_16);
		element.setEnabled(true);
		element.setText("Clear");
		element.setSize(120, 40);

		return element;
	}

	public JButton getClearButton() {
		return clearButton;
	}
	
	public void showFilterInput(boolean visible) {
		getStartVinSeqLabel().setVisible(visible);
		getStartVinSeqInput().setVisible(visible);
		getEndVinSeqLabel().setVisible(visible);
		getEndVinSeqInput().setVisible(visible);
		getStartTimeLabel().setVisible(visible);
		getStartTimeInput().setVisible(visible);
		getEndTimeLabel().setVisible(visible);
		getEndTimeInput().setVisible(visible);
		getStartTimeButton().setVisible(visible);
		getEndTimeButton().setVisible(visible);
		getModelYearListElement().setVisible(visible);
		getModelCodeListElement().setVisible(visible);
		getModelTypeListElement().setVisible(visible);
	}
}
