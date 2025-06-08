package com.honda.galc.client.teamleader.hold.qsr.put.process;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.Document;

import net.miginfocom.swing.MigLayout;

import com.honda.galc.client.teamleader.hold.InputPanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LimitedLengthPlainDocument;
import com.honda.galc.client.ui.component.PropertyPatternComboBoxRenderer;
import com.honda.galc.client.ui.component.UpperCaseDocument;
import com.honda.galc.entity.conf.ProcessPoint;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ProcessInputPanel</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class ProcessInputPanel extends InputPanel {

	private static final long serialVersionUID = 1L;

	private LabeledComboBox processPointElement;

	private JLabel startProductLabel;
	private JTextField startProductInput;
	private JButton startVinButton;

	private JLabel endProductLabel;
	private JTextField endProductInput;
	private JButton endVinButton;

	private JLabel startTimeLabel;
	private JFormattedTextField startTimeInput;
	private JButton startTimeButton;

	private JLabel endTimeLabel;
	private JFormattedTextField endTimeInput;
	private JButton endTimeButton;

	private DateFormat dateFormat;
	private String datePattern;

	private JButton commandButton;
	private LabeledComboBox modelElement;

	public ProcessInputPanel(ProcessPanel parentPanel) {
		super(parentPanel);
	}

	public void setInputEnabled(boolean enabled) {
		getStartProductInput().setEnabled(enabled);
		getEndProductInput().setEnabled(enabled);
		getStartTimeInput().setEnabled(enabled);
		getEndTimeInput().setEnabled(enabled);
		getStartVinButton().setEnabled(enabled);
		getEndVinButton().setEnabled(enabled);
		getStartTimeButton().setEnabled(enabled);
		getEndTimeButton().setEnabled(enabled);
	}

	public void resetInput() {
		getStartProductInput().setText("");
		getEndProductInput().setText("");
		getStartTimeInput().setText("");
		getEndTimeInput().setText("");
	}

	@Override
	protected void initView() {
		super.initView();
		setDatePattern("yyyy-MM-dd HH:mm:ss");
		setDateFormat(new SimpleDateFormat(getDatePattern()));

		this.processPointElement = createProcessPointElement(getProductTypeElement());
		this.commandButton = createCommandButton(getProcessPointElement());
		this.modelElement = createModelCodeElement(getProductTypeElement());
		this.startProductLabel = createStartProductLabel();
		this.startVinButton = createStartVinButton(getStartProductLabel());
		this.startProductInput = createStartProductInput(getStartVinButton());

		this.startTimeLabel = createStartTimeLabel(getStartProductInput());
		this.startTimeInput = createStartTimeInput(getStartTimeLabel());
		this.startTimeButton = createStartTimeButton(getStartTimeInput());

		this.endProductLabel = createEndProductLabel(getStartProductLabel());
		this.endVinButton = createEndVinButton(getEndProductLabel());
		this.endProductInput = createEndProductInput(getStartProductInput(), getEndProductLabel());

		this.endTimeLabel = createEndTimeLabel(getStartTimeLabel(), getEndProductLabel());
		this.endTimeInput = createEndTimeInput(getStartTimeInput(), getEndProductLabel());
		this.endTimeButton = createEndTimeButton(getEndTimeInput());

		remove(getDepartmentElement());
		remove(getProductTypeElement());
		JPanel panel = new JPanel(new MigLayout("insets 0, gap 0", "[220!,fill][fill][fill][max,fill][150!,fill]", ""));
		panel.add(getDepartmentElement(), "width 200::");
		panel.add(getProductTypeElement(), "width 200::");
		panel.add(getModelElement(), "width 200::");
		panel.add(getProcessPointElement());
		panel.add(getCommandButton(), "height 40");
		panel.setSize(1200, 50);
		add(panel);

		JPanel rangeSelectionPanel = new JPanel(new GridBagLayout());
		rangeSelectionPanel.setBorder(BorderFactory.createEtchedBorder());
		rangeSelectionPanel.setLayout(null);
		rangeSelectionPanel.setSize(990, 60);
		rangeSelectionPanel.setLocation(getDepartmentElement().getX() + 10, getDepartmentElement().getY() + getDepartmentElement().getHeight());
		rangeSelectionPanel.add(getStartProductLabel());
		rangeSelectionPanel.add(getStartVinButton());
		rangeSelectionPanel.add(getStartProductInput());
		rangeSelectionPanel.add(getStartTimeLabel());
		rangeSelectionPanel.add(getStartTimeInput());
		rangeSelectionPanel.add(getStartTimeButton());
		rangeSelectionPanel.add(getEndProductLabel());
		rangeSelectionPanel.add(getEndVinButton());
		rangeSelectionPanel.add(getEndProductInput());
		rangeSelectionPanel.add(getEndTimeLabel());
		rangeSelectionPanel.add(getEndTimeInput());
		rangeSelectionPanel.add(getEndTimeButton());
		add(rangeSelectionPanel);
	}

	// === mappings === //
	@Override
	protected void mapActions() {
		super.mapActions();
	}

	// === factory methods === //
	protected LabeledComboBox createProcessPointElement(Component base) {
		LabeledComboBox element = new LabeledComboBox("Process Point");
		element.setSize(650, 50);
		element.setLocation(base.getX() + base.getWidth(), base.getY());
		element.setFont(Fonts.DIALOG_BOLD_12);
		element.getComponent().setRenderer(new PropertyPatternComboBoxRenderer<ProcessPoint>(ProcessPoint.class, "%s ( %s )", "processPointName", "processPointId"));
		return element;
	}

	public JButton createCommandButton(Component base) {
		JButton button = new JButton();
		button.setEnabled(false);
		button.setFont(Fonts.DIALOG_BOLD_16);
		button.setSize(120, 40);
		button.setLocation(base.getX() + base.getWidth(), base.getY() + 5);
		return button;
	}

	public JLabel createStartProductLabel() {
		JLabel element = new JLabel("Start Product Number", JLabel.RIGHT);
		element.setFont(Fonts.DIALOG_BOLD_12);
		element.setSize(130, 25);
		element.setLocation(5, 4);
		return element;
	}
	
	
	protected LabeledComboBox createModelCodeElement(Component base) {
		LabeledComboBox element = new LabeledComboBox("Model");
		element.setSize(160, 50);
		element.setFont(Fonts.DIALOG_BOLD_12);
		element.setLocation(base.getX() + base.getWidth(), base.getY());
		return element;
	}

	public JButton createStartVinButton(Component base){
		JButton element = new JButton();
		element.setEnabled(false);
		element.setText("VIN");
		element.setSize(60, 25);
		element.setLocation(base.getX() + base.getWidth() + 5, base.getY());
		return element;
	}
	
	public JTextField createStartProductInput(Component base) {
		JTextField element = new JTextField();
		Document document = new UpperCaseDocument(20);
		element.setDocument(document);
		element.setFont(Fonts.DIALOG_BOLD_12);
		element.setSize(220, base.getHeight());
		element.setLocation(base.getX() + base.getWidth() + 5, base.getY());
		return element;
	}

	public JLabel createStartTimeLabel(Component base) {
		JLabel element = new JLabel("Start Time (" + getDatePattern().toUpperCase() + ")", JLabel.RIGHT);
		element.setFont(Fonts.DIALOG_BOLD_12);
		element.setSize(220, base.getHeight());
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
		startTimeButton.setSize(28, 25);
		startTimeButton.setLocation(base.getX() + base.getWidth() + 5, base.getY());
		return startTimeButton;
	}
	
	public JLabel createEndProductLabel(Component base) {
		JLabel element = new JLabel("  End Product Number", JLabel.RIGHT);
		element.setFont(Fonts.DIALOG_BOLD_12);
		element.setSize(base.getWidth(), base.getHeight());
		element.setLocation(base.getX(), base.getY() + base.getHeight() + 2);
		return element;
	}

	public JButton createEndVinButton(Component base){
		JButton element = new JButton();
		element.setEnabled(false);
		element.setText("VIN");
		element.setSize(60,25);
		element.setLocation(base.getX() + base.getWidth() + 5, base.getY());
		return element;
	}
	
	public JTextField createEndProductInput(Component cx, Component cy) {
		JTextField element = new JTextField();
		Document document = new UpperCaseDocument(20);
		element.setDocument(document);
		element.setFont(Fonts.DIALOG_BOLD_12);
		element.setSize(cx.getWidth(), cx.getHeight());
		element.setLocation(cx.getX(), cy.getY());
		return element;
	}

	public JLabel createEndTimeLabel(Component cx, Component cy) {
		JLabel element = new JLabel("  End Time (" + getDatePattern().toUpperCase() + ")", JLabel.RIGHT);
		element.setFont(Fonts.DIALOG_BOLD_12);
		element.setSize(cx.getWidth(), cx.getHeight());
		element.setLocation(cx.getX(), cy.getY());
		return element;
	}

	public JButton createEndTimeButton(Component base){
		endTimeButton = new JButton();
		endTimeButton.setRolloverIcon(null);
		endTimeButton.setText("");
		endTimeButton.setIcon(new ImageIcon(getClass().getResource("/resource/com/honda/galc/client/qsr/Date-and-Time-icon.png")));
		endTimeButton.setSize(28, 25);
		endTimeButton.setLocation(base.getX() + base.getWidth() + 5, base.getY());
		return endTimeButton;
	}
	
	public JFormattedTextField createEndTimeInput(Component cx, Component cy) {
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
		element.setLocation(cx.getX(), cy.getY());
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

	public JLabel getStartProductLabel() {
		return startProductLabel;
	}

	public JButton getStartVinButton(){
		return startVinButton;
	}
	
	public JTextField getEndProductInput() {
		return endProductInput;
	}

	public JButton getEndVinButton(){
		return endVinButton;
	}
	
	public JButton getStartTimeButton() {
		return startTimeButton;
	}

	public JButton getEndTimeButton() {
		return endTimeButton;
	}
	
	public JLabel getEndProductLabel() {
		return endProductLabel;
	}

	public JTextField getStartProductInput() {
		return startProductInput;
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

	public JButton getCommandButton() {
		return commandButton;
	}

	public JComboBox getProcessPointComboBox() {
		return getProcessPointElement().getComponent();
	}

	public LabeledComboBox getProcessPointElement() {
		return processPointElement;
	}
	public LabeledComboBox getModelElement() {
		return modelElement;
	}

	public JComboBox getModelComboBox() {
		return getModelElement().getComponent();
	}
}