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

import com.honda.galc.client.teamleader.ReturnToFactoryPanel;
import com.honda.galc.client.teamleader.hold.config.QsrMaintenancePropertyBean;
import com.honda.galc.client.teamleader.hold.qsr.QSRDialog;
import com.honda.galc.client.teamleader.hold.qsr.QSRReason;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LimitedLengthPlainDocument;
import com.honda.galc.client.ui.component.NumericDocument;
import com.honda.galc.client.ui.component.PropertyComboBoxRenderer;
import com.honda.galc.client.ui.component.PropertyPatternComboBoxRenderer;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.enumtype.QCAction;
import com.honda.galc.entity.product.Qsr;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class ReturnToFactoryInputPanel extends JPanel implements QSRDialog{

	private static final long serialVersionUID = 1L;

	private JLabel holdLabel;
	private JRadioButton holdAtShippingInput;

	private JLabel reasonLabel;
	private JComboBox<String> reasonInput;

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
	private JComboBox<Qsr> qsrInput;

	private JButton cancelButton;
	private JButton submitButton;

	private MainWindow parentFrame;
	private ReturnToFactoryPanel parentPanel;
	
	private ButtonGroup buttonGroup;

	private QCAction qcAction = QCAction.QCHOLD;
	
	private QSRReason qsrReason;


	public ReturnToFactoryInputPanel(ReturnToFactoryPanel parentPanel) {
		this.parentFrame = parentPanel.getMainWindow();
		this.parentPanel = parentPanel;
		
		initView();
	}

	protected void initView() {
		setLayout(null);
		setSize(700, 350);
		qsrReason = new QSRReason(this);
		
		holdLabel = createHoldLabel();
		holdAtShippingInput = createHoldAtShippingInput();

		buttonGroup = new ButtonGroup();
		buttonGroup.add(getHoldAtShippingInput());
		
		originDptComboBox = createOriginDptElement();
		
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
		
		add(getOriginDptComboBox());

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

		getQsrInput().setEnabled(false);
	}

	protected void initData() {
		getAssociateIdInput().setText(getMainWindow().getUserId().trim());
	}

	protected JLabel createHoldLabel() {
		JLabel element = new JLabel("Hold Type", JLabel.RIGHT);
		element.setSize(85, getElementHeight());
		element.setLocation(10, 20);
		element.setFont(getLabelFont());
		return element;
	}
	
	protected JRadioButton createHoldAtShippingInput() {
		JRadioButton element = new JRadioButton();
		Component base = getHoldLabel();
		element.setSize(100, getElementHeight());
		element.setLocation(base.getX()+base.getWidth() + 10, base.getY());
		element.setFont(getInputFont());
		element.setText("At Shipping");
		element.setActionCommand(QCAction.QCHOLD.getQcActionName());
		element.setName("HoldAtShippingRadioButton");
		return element;
	}
	
	private LabeledComboBox createOriginDptElement() {
		LabeledComboBox element = createDptElement("Origin Dpt");
		element.getComponent().removeItemAt(0);
		element.getComponent().setSelectedItem(this.parentPanel.getProductReturnProcPoint().getDivisionId());
		Component base = this.getHoldLabel();
		element.setLocation(base.getX()+18, base.getY()+base.getHeight());
		return element;
	}

	protected JLabel createReasonLabel() {
		JLabel element = new JLabel("Reason", JLabel.RIGHT);
		Component base = getOriginDptComboBox();
		element.setSize(85, getElementHeight());
		element.setLocation(base.getX()-18, base.getY()+base.getHeight());
		element.setFont(getLabelFont());
		return element;
	}

	protected JComboBox<String> createReasonInput() {
		JComboBox<String> element = new JComboBox<String>();
		Component base = getReasonLabel();
		element.setSize(600, getElementHeight());
		element.setLocation(base.getX()+base.getWidth()+10, base.getY());
		element.setFont(getInputFont());
		element.setEditable(true);
		Document document = new LimitedLengthPlainDocument(80 - (5 + 1));
		JTextComponent editor = (JTextComponent) element.getEditor().getEditorComponent();
		editor.setDocument(document);
		element.setSelectedIndex(-1);
		element.setName("ReasonComboBox");
		return element;
	}

	protected JLabel createAssociateIdLabel() {
		JLabel element = new JLabel("Associate ID", JLabel.RIGHT);
		Component base = getReasonLabel();
		element.setSize(85, getElementHeight());
		element.setLocation(base.getX(), base.getY()+base.getHeight()+10);
		element.setFont(getLabelFont());
		return element;
	}

	protected JTextField createAssociateIdInput() {
		JTextField element = new JTextField();
		Component base = getAssociateIdLabel();
		element.setSize(200, getElementHeight());
		element.setLocation(base.getX()+base.getWidth()+10, base.getY());
		element.setFont(getInputFont());
		Document document = new LimitedLengthPlainDocument(11);
		element.setDocument(document);
		element.setEditable(false);
		element.setName("AssocIdTextField");
		return element;
	}

	protected JLabel createAssociateNameLabel() {
		JLabel element = new JLabel("Name", JLabel.RIGHT);
		Component base = getAssociateIdInput();
		element.setSize(70, getElementHeight());
		element.setLocation(base.getX() + base.getWidth(), base.getY());
		element.setFont(getLabelFont());
		return element;
	}

	protected JTextField createAssociateNameInput() {
		JTextField element = new JTextField();
		Component base = getAssociateNameLabel();
		element.setSize(320, getElementHeight());
		element.setLocation(base.getX() + base.getWidth() + 10, base.getY());
		element.setFont(getInputFont());
		Document document = new LimitedLengthPlainDocument(30);
		element.setDocument(document);
		element.setName("AssocNameTextField");
		return element;
	}

	protected JLabel createPhoneLabel() {
		JLabel element = new JLabel("Phone", JLabel.RIGHT);
		Component base = getAssociateIdLabel();
		element.setSize(85, getElementHeight());
		element.setLocation(base.getX(), base.getY() + base.getHeight() + 10);
		element.setFont(getLabelFont());
		return element;
	}

	protected JTextField createPhoneInput() {
		JTextField element = new JTextField();
		Component base = getPhoneLabel();
		element.setSize(200, getElementHeight());
		element.setLocation(base.getX()+base.getWidth()+10, base.getY());
		element.setFont(getInputFont());		
		Document document = new NumericDocument(20);
		element.setDocument(document);
		element.setName("PhoneTextField");
		
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
		List<String> divisions = new ArrayList<String>();
		divisions.add(null);
		for(String divisionId : ServiceFactory.getDao(DivisionDao.class).findAllDivisionId()) {
			divisions.add(divisionId.trim());
		}
		element.getComponent().setModel(new DefaultComboBoxModel(new Vector<String>(divisions)));
		return element;
	}

	protected JLabel createAddToQsrLabel() {
		JLabel element = new JLabel("Add to existing", JLabel.RIGHT);
		Component base = getPhoneLabel();
		element.setSize(85, getElementHeight());
		element.setLocation(base.getX(), base.getY() + base.getHeight() + 10);
		element.setFont(getLabelFont());
		return element;
	}

	protected JCheckBox createAddToQsrInput() {
		JCheckBox element = new JCheckBox();
		Component base = getAddToQsrLabel();
		element.setSize(25, getElementHeight());
		element.setLocation(base.getX()+base.getWidth()+10, base.getY());
		element.setFont(getInputFont());
		element.setName("AddToQsrCheckBox");
		return element;
	}

	protected JLabel createQsrLabel() {
		JLabel element = new JLabel("QSR#", JLabel.RIGHT);
		Component base = this.getAddToQsrInput();
		element.setSize(60, getElementHeight());
		element.setLocation(base.getX() + base.getWidth(), getAddToQsrInput().getY());
		element.setFont(getLabelFont());
		return element;
	}

	@SuppressWarnings("unchecked")
	protected JComboBox<Qsr> createQsrInput() {
		JComboBox<Qsr> element = new JComboBox<Qsr>();
		Component base = getQsrLabel();
		element.setSize(510, getElementHeight());
		element.setLocation(base.getX() + base.getWidth() + 5, base.getY());
		element.setFont(getInputFont());
		element.setRenderer(new PropertyPatternComboBoxRenderer<Qsr>(Qsr.class, "%s - [%s] [Resp Dpt: %s]", "name", "description", "responsibleDepartment"));
		element.setEnabled(false);
		element.setName("QsrComboBox");
		return element;
	}

	protected JButton createCancelButton() {
		JButton element = new JButton();
		Component base = getQsrInput();
		element.setSize((getAssociateNameInput().getWidth() - 5) / 2, 2 * getElementHeight());
		element.setLocation(getPhoneInput().getX()+100, base.getY() + base.getHeight() + 15);
		element.setFont(getButtontFont());
		element.setText("Cancel");
		element.setMnemonic(KeyEvent.VK_C);
		element.setName("CancelButton");
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
		element.setName("SubmitButton");
		return element;
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

	public JComboBox<String> getReasonInput() {
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

	public JCheckBox getAddToQsrInput() {
		return addToQsrInput;
	}

	public JLabel getAddToQsrLabel() {
		return addToQsrLabel;
	}

	public JComboBox<Qsr> getQsrInput() {
		return qsrInput;
	}

	public JLabel getQsrLabel() {
		return qsrLabel;
	}

	public MainWindow getMainWindow() {
		return parentFrame;
	}

	public ReturnToFactoryPanel getParentPanel() {
		return parentPanel;
	}

	public Division getDivision() {
		return new Division();
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
	
	public LabeledComboBox getOriginDptComboBox() {
		return this.originDptComboBox;
	}
	
	public LabeledComboBox getRespDptComboBox() {
		return this.respDptComboBox;
	}
	
	@SuppressWarnings("rawtypes")
	public JComboBox getOriginDptComboBoxComponent() {
		return this.originDptComboBox.getComponent();
	}
	
	@SuppressWarnings("rawtypes")
	public JComboBox getRespDptComboBoxComponent() {
		return this.respDptComboBox.getComponent();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public JComboBox getLineComboBoxComponent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JComboBox getDepartmentComboBoxComponent() {
		// TODO Auto-generated method stub
		return null;
	}
}
