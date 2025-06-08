package com.honda.galc.client.teamleader.hold.fragments;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import com.honda.galc.client.teamleader.hold.config.Config;
import com.honda.galc.client.teamleader.hold.config.QsrMaintenancePropertyBean;
import com.honda.galc.client.teamleader.hold.qsr.ProcessPointSelection;
import com.honda.galc.client.teamleader.hold.qsr.QSRDialog;
import com.honda.galc.client.teamleader.hold.qsr.QSRReason;
import com.honda.galc.client.teamleader.hold.qsr.put.HoldProductPanel;
import com.honda.galc.client.teamleader.hold.qsr.put.ProcessPointSelectionPanel;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LimitedLengthPlainDocument;
import com.honda.galc.client.ui.component.NumericDocument;
import com.honda.galc.client.ui.component.PropertyComboBoxRenderer;
import com.honda.galc.client.ui.component.PropertyPatternComboBoxRenderer;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.enumtype.QCAction;
import com.honda.galc.entity.product.HoldAccessType;
import com.honda.galc.entity.product.Qsr;
import com.honda.galc.service.property.PropertyService;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>HoldInputPanel</code> is ...
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
 * <TD>Karol Wozniak</TD>
 * <TD>Jan 7, 2010</TD>
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
public class HoldInputPanel extends JPanel implements ProcessPointSelection, QSRDialog{

	private static final long serialVersionUID = 1L;

	private JLabel holdLabel;
	private JRadioButton holdNowInput;
	private JRadioButton holdAtShippingInput;

	private JRadioButton kickoutInput;
	
	private ProcessPointSelectionPanel processPointPanel;
	
	private JLabel spindleLabel;
	private JTextField spindleInput;

	private JLabel reasonLabel;
	private JComboBox reasonInput;

	private JLabel associateIdLabel;
	private JTextField associateIdInput;

	private JLabel associateNameLabel;
	private JTextField associateNameInput;

	private JLabel phoneLabel;
	private JTextField phoneInput;
	
	private LabeledComboBox originDptComboBox;
	private LabeledComboBox respDptComboBox;

	private JLabel addToQsrLabel;
	private JCheckBox addToQsrInput;

	private JLabel qsrLabel;
	private JComboBox qsrInput;

	private JButton cancelButton;
	private JButton submitButton;

	private MainWindow parentFrame;
	private HoldProductPanel parentPanel;
	
	private ButtonGroup buttonGroup;

	private QCAction qcAction = QCAction.QCHOLD;
	
	private QSRReason qsrReason;
	
	private JLabel holdAccessTypeLabel;
	
	private JComboBox holdAccessTypeInput;


	public HoldInputPanel(HoldProductPanel parentPanel) {
		this.parentFrame = parentPanel.getMainWindow();
		this.parentPanel = parentPanel;
		
		initView();
	}

	protected void initView() {
		setLayout(null);
		setSize(700, 370);
		qsrReason = new QSRReason(this);
		holdLabel = createHoldLabel();
		holdAtShippingInput = createHoldAtShippingInput();
		holdNowInput = createHoldNowInput();
		

		kickoutInput = createKickoutInput();
		processPointPanel = createProcessPointComponent();
		
		buttonGroup = new ButtonGroup();
		buttonGroup.add(getHoldAtShippingInput());
		buttonGroup.add(getHoldNowInput());
		buttonGroup.add(getKickoutInput());

		spindleLabel = createSpindleLabel();
		spindleInput = createSpindleInput();
		
		originDptComboBox = createOriginDptElement();
		
		holdAccessTypeLabel = createHoldAccessTypeLabel();
		holdAccessTypeInput = createHoldAccessTypeInput();

		reasonLabel = createReasonLabel();
		reasonInput = createReasonInput();

		associateIdLabel = createAssociateIdLabel();
		associateIdInput = createAssociateIdInput();

		associateNameLabel = createAssociateNameLabel();
		associateNameInput = createAssociateNameInput();

		phoneLabel = createPhoneLabel();
		phoneInput = createPhoneInput();

		respDptComboBox = createRespDptElement();

		addToQsrLabel = createAddToQsrLabel();
		addToQsrInput = createAddToQsrInput();

		qsrLabel = createQsrLabel();
		qsrInput = createQsrInput();

		cancelButton = createCancelButton();
		submitButton = createSubmitButton();

		add(getHoldLabel());
		add(getHoldAtShippingInput());
		add(getHoldNowInput());
		add(getKickoutInput());
		add(processPointPanel);
		
		add(getOriginDptComboBox());

		add(getSpindleLabel());
		add(getSpindleInput());
		
		add(getHoldAccessTypeLabel());
		add(getHoldAccessTypeInput());

		add(getReasonLabel());
		add(getReasonInput());

		add(getAssociateIdLabel());
		add(getAssociateIdInput());

		add(getAssociateNameLabel());
		add(getAssociateNameInput());

		add(getPhoneLabel());
		add(getPhoneInput());
		
		add(getRespDptComboBox());

		add(getAddToQsrLabel());
		add(getAddToQsrInput());

		add(getQsrLabel());
		add(getQsrInput());

		add(getCancelButton());
		add(getSubmitButton());

		initData();

		mapHandlers();

		getQsrInput().setEnabled(false);
	}

	protected void initData() {
		getAssociateIdInput().setText(getMainWindow().getUserId().trim());
		
		getProcessPointPanel().setVisible(true);
		getKickoutInput().setVisible(true);
	}

	// === mappings === //
	protected void mapHandlers() {
		getAddToQsrInput().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (getAddToQsrInput().isSelected()) {
					getQsrInput().setEnabled(true);
				} else {
					getQsrInput().setEnabled(false);
					getQsrInput().setSelectedIndex(-1);
				}
			}
		});
	}

	protected void addActionListener(JButton element, ActionListener listener) {
		if (element == null || listener == null) {
			return;
		}

		ActionListener[] listeners = element.getActionListeners();

		for (ActionListener l : listeners) {
			element.removeActionListener(l);
		}

		element.addActionListener(listener);
		for (ActionListener l : listeners) {
			element.addActionListener(l);
		}
	}

	protected void addActionListener(JComboBox element, ActionListener listener) {
		if (element == null || listener == null) {
			return;
		}

		ActionListener[] listeners = element.getActionListeners();

		for (ActionListener l : listeners) {
			element.removeActionListener(l);
		}

		element.addActionListener(listener);
		for (ActionListener l : listeners) {
			element.addActionListener(l);
		}
	}

	// === factory methods === //

	protected JLabel createHoldLabel() {
		JLabel element = new JLabel("Hold At");
		element.setSize(90, getElementHeight());
		element.setLocation(20, 20);
		element.setFont(getLabelFont());
		return element;
	}

	protected JRadioButton createHoldNowInput() {
		JRadioButton element = new JRadioButton();
		Component base = getHoldAtShippingInput();
		element.setSize(50, getElementHeight());
		element.setLocation(base.getX() + base.getWidth() + 5, base.getY());
		element.setFont(getInputFont());
		element.setText("Now");
		element.setActionCommand(QCAction.QCHOLD.getQcActionName());
		return element;
	}
	
	protected JRadioButton createHoldAtShippingInput() {
		JRadioButton element = new JRadioButton();
		Component base = getHoldLabel();
		element.setSize(100, getElementHeight());
		element.setLocation(base.getX() + base.getWidth(), base.getY());
		element.setFont(getInputFont());
		element.setText("At Shipping");
		element.setActionCommand(QCAction.QCHOLD.getQcActionName());
		element.setSelected(true);
		return element;
	}

	protected JRadioButton createKickoutInput() {
		JRadioButton element = new JRadioButton();
		Component base = getHoldNowInput();
		element.setSize(80, getElementHeight());
		element.setLocation(base.getX() + base.getWidth(), base.getY());
		element.setFont(getInputFont());
		element.setText("Kickout");
		element.setActionCommand(QCAction.KICKOUT.getQcActionName());
		element.setSelected(true);
		return element;
	}

	// === factory methods === //
	protected ProcessPointSelectionPanel createProcessPointComponent() {
		ProcessPointSelectionPanel panel = new ProcessPointSelectionPanel(this, getMainWindow().getProductType(), false);		
		Component base = getKickoutInput();
		panel.setSize(715, 30);
		panel.setLocation(10, base.getY()+20);
		return panel;
	}

	protected JLabel createSpindleLabel() {
		JLabel element = new JLabel("Spindle #", JLabel.RIGHT);
		Component base = getHoldLabel();
		element.setSize(76, getElementHeight());
		element.setLocation(base.getX(), base.getY() + base.getHeight() + 35);
		element.setFont(getLabelFont());
		return element;
	}

	protected JTextField createSpindleInput() {
		JTextField element = new JTextField();
		Component base = createSpindleLabel();
		element.setSize(85, getElementHeight());
		element.setLocation(base.getX()+base.getWidth() +10, base.getY());
		element.setFont(getInputFont());
		Document document = new LimitedLengthPlainDocument(10);
		element.setDocument(document);
		return element;
	}
	
	private LabeledComboBox createOriginDptElement() {
		LabeledComboBox element = createDptElement("Origin Dpt");
		element.getComponent().removeItemAt(0);
		element.getComponent().setSelectedItem(this.parentPanel.getDivision());
		Component base = this.getSpindleLabel();
		element.setLocation(base.getX()+10,base.getY()+base.getHeight());
		return element;
	}

	protected JLabel createHoldAccessTypeLabel() {
		JLabel element = new JLabel("Type ", JLabel.RIGHT);
		Component base = getOriginDptComboBox();
		element.setSize(60, getElementHeight());
		element.setLocation(base.getX()+base.getWidth(), base.getY()+10);
		element.setFont(getLabelFont());
		return element;
	}

	protected JComboBox createHoldAccessTypeInput() {
		JComboBox element = new JComboBox();
		Component base = getHoldAccessTypeLabel();
		element.setSize(330, getElementHeight());
		element.setLocation(base.getX()+base.getWidth()+10, base.getY());
		element.setFont(getInputFont());
		element.setRenderer(new PropertyPatternComboBoxRenderer<HoldAccessType>(HoldAccessType.class, "%s", "description"));
		element.setEnabled(true);
		return element;
	}
	
	protected JLabel createReasonLabel() {
		JLabel element = new JLabel("Reason", JLabel.RIGHT);
		Component base = getOriginDptComboBox();
		element.setSize(66, getElementHeight());
		element.setLocation(base.getX(),base.getY() + base.getHeight()+5);
		element.setFont(getLabelFont());
		return element;
	}

	protected JComboBox createReasonInput() {
		JComboBox element = new JComboBox();
		Component base = getReasonLabel();
		element.setSize(574, getElementHeight());
		element.setLocation(base.getX()+base.getWidth()+10, base.getY());
		element.setFont(getInputFont());
		element.setEditable(true);
		// length (column name - spindle length - '-'
		Document document = new LimitedLengthPlainDocument(80 - (5 + 1));
		JTextComponent editor = (JTextComponent) element.getEditor().getEditorComponent();
		editor.setDocument(document);
		element.setSelectedIndex(-1);
		return element;
	}

	// == associate elements === //
	protected JLabel createAssociateIdLabel() {
		JLabel element = new JLabel("Associate ID", JLabel.RIGHT);
		Component base = getReasonLabel();
		element.setSize(80, getElementHeight());
		element.setLocation(base.getX()-13, base.getY() + base.getHeight()+10 );
		element.setFont(getLabelFont());
		return element;
	}

	protected JTextField createAssociateIdInput() {
		JTextField element = new JTextField();
		Component base = getAssociateIdLabel();
		element.setSize(162, getElementHeight());
		element.setLocation(base.getX()+base.getWidth()+10, base.getY());
		element.setFont(getInputFont());
		Document document = new LimitedLengthPlainDocument(11);
		element.setDocument(document);
		element.setEditable(false);
		return element;
	}

	protected JLabel createAssociateNameLabel() {
		JLabel element = new JLabel("Name", JLabel.RIGHT);
		Component base = getAssociateIdInput();
		element.setSize(60, getElementHeight());
		element.setLocation(base.getX()+base.getWidth()+10, base.getY());
		element.setFont(getLabelFont());
		return element;
	}

	protected JTextField createAssociateNameInput() {
		JTextField element = new JTextField();
		Component base = getAssociateNameLabel();
		element.setSize(330, getElementHeight());
		element.setLocation(base.getX()+base.getWidth()+10, base.getY());
		element.setFont(getInputFont());
		Document document = new LimitedLengthPlainDocument(30);
		element.setDocument(document);
		return element;
	}

	protected JLabel createPhoneLabel() {
		JLabel element = new JLabel("Phone", JLabel.RIGHT);
		Component base = getAssociateIdLabel();
		element.setSize(80, getElementHeight());
		element.setLocation(base.getX(), base.getY() + base.getHeight() + 10);
		element.setFont(getLabelFont());
		return element;
	}

	protected JTextField createPhoneInput() {
		JTextField element = new JTextField();
		Component base = getPhoneLabel();
		element.setSize(162, getElementHeight());
		element.setLocation(base.getX()+base.getWidth()+10, base.getY());
		element.setFont(getInputFont());		
		Document document = new NumericDocument(20);
		element.setDocument(document);
		
		return element;
	}
	
	private LabeledComboBox createRespDptElement() {
		LabeledComboBox element = createDptElement("Resp Dpt");
		Component base = getPhoneInput();
		element.setLocation(base.getX() + base.getWidth()+10, base.getY()-10);
		return element;
	}
	
	private LabeledComboBox createDptElement(String label) {
		LabeledComboBox element = new LabeledComboBox(label);
		element.setFont(getInputFont());
		element.setSize(250, getElementHeight()+20);
		element.getComponent().setRenderer(new PropertyComboBoxRenderer<Division>(Division.class, "divisionName"));
		List<Division> divisions = new ArrayList<Division>();
		divisions.add(new Division());
		divisions.addAll(Config.getInstance(parentPanel.getApplicationId()).getDivisions());
		element.getComponent().setModel(new DefaultComboBoxModel(new Vector<Division>(divisions)));
		return element;
	}
	
	protected JLabel createAddToQsrLabel() {
		JLabel element = new JLabel("Add to existing", JLabel.RIGHT);
		Component base = getPhoneLabel();
		element.setSize(90, getElementHeight());
		element.setLocation(base.getX()-10, base.getY() + base.getHeight() + 10);
		element.setFont(getLabelFont());
		return element;
	}

	protected JCheckBox createAddToQsrInput() {
		JCheckBox element = new JCheckBox();
		Component base = getAddToQsrLabel();
		element.setSize(20, getElementHeight());
		element.setLocation(base.getX()+base.getWidth()+7, base.getY());
		element.setFont(getInputFont());
		return element;
	}

	protected JLabel createQsrLabel() {
		JLabel element = new JLabel("QSR#", JLabel.RIGHT);
		Component base = getAddToQsrInput();
		element.setSize(60, getElementHeight());
		element.setLocation(base.getX() + base.getWidth() + 10, getAddToQsrInput().getY());
		element.setFont(getLabelFont());
		return element;
	}

	protected JComboBox createQsrInput() {
		JComboBox element = new JComboBox();
		Component base = getQsrLabel();
		element.setSize(480, getElementHeight());
		element.setLocation(base.getX() + base.getWidth() + 5, base.getY());
		element.setFont(getInputFont());
		element.setRenderer(new PropertyPatternComboBoxRenderer<Qsr>(Qsr.class, "%s-%s-[Resp Dpt: %s]", "name", "description","responsibleDepartment"));
		element.setEnabled(false);
		return element;
	}

	protected JButton createCancelButton() {
		JButton element = new JButton();
		Component base = getQsrInput();
		element.setSize((getAssociateNameInput().getWidth() - 5) / 2, 2 * getElementHeight());
		element.setLocation(getPhoneInput().getX(), base.getY() + base.getHeight() + 15);
		element.setFont(getButtontFont());
		element.setText("Cancel");
		element.setMnemonic(KeyEvent.VK_C);
		return element;
	}

	protected JButton createSubmitButton() {
		JButton element = new JButton();
		Component base = getCancelButton();
		element.setSize(base.getWidth(), 2 * getElementHeight());
		element.setLocation(base.getX() + base.getWidth() + 5, base.getY());
		element.setFont(getButtontFont());
		element.setText("Submit");
		element.setMnemonic(KeyEvent.VK_S);
		element.setEnabled(false);
		return element;
	}
	
	public LabeledComboBox getOriginDptComboBox() {
		return this.originDptComboBox;
	}

	public LabeledComboBox getRespDptComboBox() {
		return this.respDptComboBox;
	}

	public JLabel getHoldLabel() {
		return holdLabel;
	}

	public JLabel getReasonLabel() {
		return reasonLabel;
	}

	public JRadioButton getHoldAtShippingInput() {
		return holdAtShippingInput;
	}

	public JRadioButton getHoldNowInput() {
		return holdNowInput;
	}

	public JRadioButton getKickoutInput() {
		return kickoutInput;
	}
	
	public JComboBox getProcessPointComboBoxComponent() {
		return processPointPanel.getProcessPointComboBox().getComponent();
	}
	
	public JComboBox getLineComboBoxComponent() {
		return processPointPanel.getLineComboBox().getComponent();
	}
	
	public JComboBox getDepartmentComboBoxComponent() {
		return processPointPanel.getDepartmentComboBox().getComponent();
	}
	
	public ProcessPointSelectionPanel getProcessPointPanel() {
		return processPointPanel;
	}
	
	public LabeledComboBox getProcessPointLabel() {
		return processPointPanel.getProcessPointComboBox();
	}

	public JComboBox getReasonInput() {
		return reasonInput;
	}

	public JTextField getAssociateIdInput() {
		return associateIdInput;
	}

	public JLabel getAssociateIdLabel() {
		return associateIdLabel;
	}

	public JTextField getAssociateNameInput() {
		return associateNameInput;
	}

	public JLabel getAssociateNameLabel() {
		return associateNameLabel;
	}

	public JLabel getPhoneLabel() {
		return phoneLabel;
	}

	public JTextField getPhoneInput() {
		return phoneInput;
	}

	protected int getElementHeight() {
		return 25;
	}

	protected Font getLabelFont() {
		return Fonts.DIALOG_BOLD_12;
	}

	protected Font getInputFont() {
		return Fonts.DIALOG_BOLD_12;
	}

	protected Font getButtontFont() {
		return Fonts.DIALOG_BOLD_16;
	}
	
	public ButtonGroup getButtonGroup() {
		return buttonGroup;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public JButton getSubmitButton() {
		return submitButton;
	}

	public JTextField getSpindleInput() {
		return spindleInput;
	}

	public JLabel getSpindleLabel() {
		return spindleLabel;
	}

	public JCheckBox getAddToQsrInput() {
		return addToQsrInput;
	}

	public JLabel getAddToQsrLabel() {
		return addToQsrLabel;
	}

	public JComboBox getQsrInput() {
		return qsrInput;
	}

	public JLabel getQsrLabel() {
		return qsrLabel;
	}

	public MainWindow getMainWindow() {
		return parentFrame;
	}

	public HoldProductPanel getParentPanel() {
		return parentPanel;
	}

	public Division getDivision() {
		return getParentPanel().getDivision();
	}
	
	public QsrMaintenancePropertyBean getProperty() {
		return PropertyService.getPropertyBean(QsrMaintenancePropertyBean.class, getParentPanel().getApplicationId());
	}
	
	public void setQCAction(QCAction qcAction) {
		this.qcAction = qcAction;
	}
	
	public QCAction getQCAction() {
		return qcAction;
	}
	
	public QSRReason getQSRReason() {
		return qsrReason;
	}

	public Logger getLogger() {
		return Logger.getLogger(getParentPanel().getApplicationId());
	}

	public JLabel getHoldAccessTypeLabel() {
		return holdAccessTypeLabel;
	}

	public void setHoldAccessTypeLabel(JLabel holdTypeLabel) {
		this.holdAccessTypeLabel = holdTypeLabel;
	}

	public JComboBox getHoldAccessTypeInput() {
		return holdAccessTypeInput;
	}

	public void setHoldAccessTypeInput(JComboBox holdTypeInput) {
		this.holdAccessTypeInput = holdTypeInput;
	}
}