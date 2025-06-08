package com.honda.galc.client.teamleader.hold.qsr.release.dialog;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.Document;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.hold.config.Config;
import com.honda.galc.client.teamleader.hold.config.QsrMaintenancePropertyBean;
import com.honda.galc.client.teamleader.hold.qsr.ProcessPointSelection;
import com.honda.galc.client.teamleader.hold.qsr.QSRDialog;
import com.honda.galc.client.teamleader.hold.qsr.QSRReason;
import com.honda.galc.client.teamleader.hold.qsr.put.ProcessPointSelectionPanel;
import com.honda.galc.client.teamleader.hold.qsr.release.ReleasePanel;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LimitedLengthPlainDocument;
import com.honda.galc.client.ui.component.NumericDocument;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dto.HoldReasonMappingDto;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.enumtype.QCAction;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.entity.product.Qsr;
import com.honda.galc.service.property.PropertyService;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ReleaseDialog</code> is ...
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
 * <TD>Jan 15, 2010</TD>
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
public class ReleaseDialog extends JDialog implements ProcessPointSelection, QSRDialog {

	public static final String USER_DEFINED = "USER_DEFINED";

	private static final long serialVersionUID = 1L;

	public static final String APPROVED_TO_SHIP = "Approved To Ship";
	private ProcessPointSelectionPanel processPointPanel;
	
	private ReleasePanel parentPanel;

	private JLabel reasonLabel;
	private JComboBox reasonInput;
	
	private JLabel associateIdLabel;
	private JTextField associateIdInput;

	private JLabel associateNameLabel;
	private JTextField associateNameInput;

	private JLabel phoneLabel;
	private JTextField phoneInput;

	private JLabel approverLabel;
	private JTextField approverInput;

	private JLabel commentLabel;
	private JTextField commentInput;

	private JButton cancelButton;
	private JButton submitButton;

	private List<HoldResult> holdResults;
	private List<HoldResult> qsrHoldResults;
	
	private QCAction qcAction = QCAction.QCRELEASE;
	private QSRReason qsrReason;
	
	private QsrMaintenancePropertyBean propertyBean=null;

	public ReleaseDialog(ReleasePanel parentPanel, String title, List<HoldResult> holdResults, List<HoldResult> qsrHoldResults) {
		super(parentPanel.getMainWindow(), title, true);
		this.parentPanel = parentPanel;
		this.holdResults = holdResults;
		this.qsrHoldResults = qsrHoldResults;
		setSize(730, 320);
		propertyBean=PropertyService.getPropertyBean(QsrMaintenancePropertyBean.class, getParentPanel().getApplicationId());
		qsrReason = new QSRReason(this);
		initView();
		mapActions();
		initModel();
	}

	protected void initView() {

		setLayout(null);
		
		processPointPanel = createProcessPointPanel();

		reasonLabel = createReasonLabel();
		reasonInput = createReasonInput();

		approverLabel = createApproverLabel();
		approverInput = createApproverInput();

		associateIdLabel = createAssociateIdLabel();
		associateIdInput = createAssociateIdInput();

		associateNameLabel = createAssociateNameLabel();
		associateNameInput = createAssociateNameInput();

		phoneLabel = createPhoneLabel();
		phoneInput = createPhoneInput();

		commentLabel = createCommentLabel();
		commentInput = createCommentInput();

		cancelButton = createCancelButton();
		submitButton = createSubmitButton();
		
		add(getProcessPointPanel());

		add(getReasonLabel());
		add(getReasonInput());

		add(getApproverLabel());
		add(getApproverInput());

		add(getCommentLabel());
		add(getCommentInput());

		add(getAssociateIdLabel());
		add(getAssociateIdInput());
		add(getAssociateNameLabel());
		add(getAssociateNameInput());
		add(getPhoneLabel());
		add(getPhoneInput());

		add(getCancelButton());
		add(getSubmitButton());

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);

		getRootPane().setDefaultButton(getCancelButton());

		getCommentInput().setEnabled(isQsrCompleted());
	}

	protected void initModel() {
		initProcessPointSelection();

		getAssociateIdInput().setText(getParentPanel().getMainWindow().getUserId().trim());
		Qsr qsr = (Qsr) getParentPanel().getInputPanel().getQsrComboBox().getSelectedItem();

		if(qsr!= null) {
			String approver = qsr.getApproverName();
			String comment = qsr.getComment();
			if (approver == null) {
				approver = "";
			}
	
			if (comment == null) {
				comment = "";
			}
	
			getApproverInput().setText(approver);
			getCommentInput().setText(comment);
		

			if (getParentPanel().getCachedQsr() != null) {
				approver = getParentPanel().getCachedQsr().getApproverName();
				if (approver != null && approver.trim().length() > 0) {
					getApproverInput().setText(approver.trim());
				}
			}
		}

		if (getParentPanel().getCachedHoldResultInput() != null) {
			String reason = getParentPanel().getCachedHoldResultInput().getReleaseReason();
			String name = getParentPanel().getCachedHoldResultInput().getReleaseAssociateName();
			String phone = getParentPanel().getCachedHoldResultInput().getReleaseAssociatePhone();
			reason = reason == null ? "" : reason.trim();
			name = name == null ? "" : name.trim();
			phone = phone == null ? "" : phone.trim();

			getReasonInput().setSelectedItem(reason);
			getAssociateNameInput().setText(name);
			getPhoneInput().setText(phone);
		}
		
		if(!propertyBean.isShowProcessPoint()) {
			getProcessPointPanel().setVisible(false);
		}
	}

	protected void initProcessPointSelection() {
		if (getProcessPointPanel().isReasonsFromProperties()) {
			getProcessPointPanel().setVisible(false);
		} else {
			if(propertyBean.isShowProcessPoint()) {
				getProcessPointPanel().setComponentVisibility(getQCAction());
			}else {
				getProcessPointPanel().setVisible(false);
				
			}
		}		
	}
	
	private Config getConfig() {
		return Config.getInstance(parentPanel.getApplicationId());
	}

	public boolean isQsrCompleted() {

		List<HoldResult> list = getQsrHoldResults();
		int releasedCtr = 0;
		for (HoldResult result : list) {
			if (result.getReleaseFlag() == 1) {
				releasedCtr++;
			}
		}
		if ((getHoldResults().size() + releasedCtr) == getQsrHoldResults().size()) {
			return true;
		}
		return false;
	}

	// === action mappings ===//
	protected void mapActions() {

		ReleaseInputListener inputListener = new ReleaseInputListener(this);

		getApproverInput().getDocument().addDocumentListener(inputListener);
		getAssociateNameInput().getDocument().addDocumentListener(inputListener);
		getPhoneInput().getDocument().addDocumentListener(inputListener);
		getReasonInput().addActionListener(inputListener);

		getCancelButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		getSubmitButton().addActionListener(new ReleaseAction(this));
		getReasonInput().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object reason = getReasonInput().getSelectedItem();
				if (APPROVED_TO_SHIP.equals(reason)) {
					getApproverInput().setEnabled(true);
				} else {
					getApproverInput().setEnabled(false);
				}
			}
		});
		
		getDepartmentComboBoxComponent().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if( getDepartmentComboBoxComponent().getSelectedItem() == null) {
					getDepartmentComboBoxComponent().setModel(new DefaultComboBoxModel());
				}
				getQSRReason().populateReasonsByDiv(getQCAction());				
			}
		});
		
		getProcessPointPanel().getLineComboBox().getComponent().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getQSRReason().populateReasonsByDivAndLine(getQCAction());				
			}
		});
	}

	// === factory === //
	protected ProcessPointSelectionPanel createProcessPointPanel() {
		ProcessPointSelectionPanel element = new ProcessPointSelectionPanel(this, parentPanel.getProductType(), false);
		element.setSize(715, 30);
		element.setLocation(10, 10);
		return element;
	}
	
	protected JLabel createReasonLabel() {
		JLabel element = new JLabel("Reason");
		Component base = getProcessPointPanel();
		element.setSize(80, getElementHeight());
		element.setLocation(base.getX(), base.getY() + base.getHeight() + 10);
		element.setFont(getLabelFont());
		return element;
	}

	protected JComboBox createReasonInput() {
		JComboBox element = new JComboBox();
		Component base = getReasonLabel();
		element.setSize(200, getElementHeight());
		element.setLocation(base.getX() + base.getWidth(), base.getY());
		element.setFont(getInputFont());
		qsrReason.addDefaultReasons(element, qcAction);
		addAdditionalReleaseReasons(element);
			
		element.setSelectedIndex(-1);
		return element;
	}

	private void addAdditionalReleaseReasons(JComboBox element) {
		String[] additionalReleaseReasons = getConfig().getPropertyBean().getReleaseReasons();
		if (additionalReleaseReasons != null && additionalReleaseReasons.length > 0) {
			if(!getProcessPointPanel().isReasonsFromProperties()) {
				Division division = getParentPanel().getDivision();
				List<HoldReasonMappingDto> releaseReasons = 
						getConfig().getReasonsByQcAction(division, qcAction);
				for (HoldReasonMappingDto reason : releaseReasons) {
					element.addItem(reason.getHoldReason());
				}			
			}else {
				for (String reason : additionalReleaseReasons) {
					if (!StringUtils.isBlank(reason)) {
						element.addItem(reason);
					}
				}
			}
		}
	}

	protected JLabel createApproverLabel() {
		JLabel element = new JLabel("Approver", JLabel.RIGHT);
		Component base = getReasonInput();
		element.setSize(80, getElementHeight());
		element.setLocation(base.getX() + base.getWidth(), base.getY());
		element.setFont(getLabelFont());
		return element;
	}

	protected JTextField createApproverInput() {
		JTextField element = new JTextField();
		Component base = getApproverLabel();
		element.setSize(320, getElementHeight());
		element.setLocation(base.getX() + base.getWidth() + 5, base.getY());
		element.setFont(getInputFont());
		Document document = new LimitedLengthPlainDocument(30);
		element.setDocument(document);
		element.setEnabled(false);
		return element;
	}

	// == associate elements === //
	protected JLabel createAssociateIdLabel() {
		JLabel element = new JLabel("Associate ID");
		Component base = getReasonLabel();
		element.setSize(80, getElementHeight());
		element.setLocation(base.getX(), base.getY() + base.getHeight() + 10);
		element.setFont(getLabelFont());
		return element;
	}

	protected JTextField createAssociateIdInput() {
		JTextField element = new JTextField();
		Component base = getAssociateIdLabel();
		element.setSize(200, getElementHeight());
		element.setLocation(base.getX() + base.getWidth(), base.getY());
		element.setFont(getInputFont());
		Document document = new LimitedLengthPlainDocument(11);
		element.setDocument(document);
		element.setEditable(false);
		element.setFocusable(false);
		return element;
	}

	protected JLabel createAssociateNameLabel() {
		JLabel element = new JLabel("Name", JLabel.RIGHT);
		Component base = getAssociateIdInput();
		element.setSize(80, getElementHeight());
		element.setLocation(base.getX() + base.getWidth(), base.getY());
		element.setFont(getLabelFont());
		return element;
	}

	protected JTextField createAssociateNameInput() {
		JTextField element = new JTextField();
		Component base = getAssociateNameLabel();
		element.setSize(320, getElementHeight());
		element.setLocation(base.getX() + base.getWidth() + 5, base.getY());
		element.setFont(getInputFont());
		Document document = new LimitedLengthPlainDocument(30);
		element.setDocument(document);
		return element;
	}

	protected JLabel createPhoneLabel() {
		JLabel element = new JLabel("Phone", JLabel.LEFT);
		Component base = getAssociateIdLabel();
		element.setSize(80, getElementHeight());
		element.setLocation(base.getX(), base.getY() + base.getHeight() + 10);
		element.setFont(getLabelFont());
		return element;
	}

	protected JTextField createPhoneInput() {
		JTextField element = new JTextField();
		Component base = getPhoneLabel();
		element.setSize(200, getElementHeight());
		element.setLocation(base.getX() + base.getWidth(), base.getY());
		element.setFont(getInputFont());
		Document document = new NumericDocument(15);
		element.setDocument(document);
		return element;
	}

	protected JLabel createCommentLabel() {
		JLabel element = new JLabel("Comment");
		Component base = getPhoneLabel();
		element.setSize(80, getElementHeight());
		element.setLocation(base.getX(), base.getY() + base.getHeight() + 10);
		element.setFont(getLabelFont());
		return element;
	}

	protected JTextField createCommentInput() {
		JTextField element = new JTextField();
		Component base = getCommentLabel();
		element.setSize(605, getElementHeight());
		element.setLocation(base.getX() + base.getWidth(), base.getY());
		element.setFont(getInputFont());
		Document document = new LimitedLengthPlainDocument(80);
		element.setDocument(document);
		element.setEnabled(false);
		return element;
	}

	protected JButton createCancelButton() {
		JButton element = new JButton();
		Component base = getPhoneInput();
		element.setSize((base.getWidth() - 5) / 2, 2 * getElementHeight());
		element.setLocation(base.getX(), getCommentLabel().getY() + getCommentLabel().getHeight() + 15);
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
	


	// === get/set === //
	public List<HoldResult> getHoldResults() {
		return holdResults;
	}

	public ReleasePanel getParentPanel() {
		return parentPanel;
	}
	
	public ProcessPointSelectionPanel getProcessPointPanel() {
		return processPointPanel;
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

	public JButton getCancelButton() {
		return cancelButton;
	}

	public JTextField getPhoneInput() {
		return phoneInput;
	}

	public JLabel getPhoneLabel() {
		return phoneLabel;
	}

	public JComboBox getReasonInput() {
		return reasonInput;
	}

	public JLabel getReasonLabel() {
		return reasonLabel;
	}

	public JButton getSubmitButton() {
		return submitButton;
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

	public JTextField getApproverInput() {
		return approverInput;
	}

	public JLabel getApproverLabel() {
		return approverLabel;
	}

	public List<HoldResult> getQsrHoldResults() {
		return qsrHoldResults;
	}

	public JTextField getCommentInput() {
		return commentInput;
	}

	public JLabel getCommentLabel() {
		return commentLabel;
	}
	
	public MainWindow getMainWindow() {
		return getParentPanel().getMainWindow();
	}
	
	public QsrMaintenancePropertyBean getProperty() {
		return propertyBean;
	}
	public Division getDivision() {
		return (Division) parentPanel.getInputPanel().getDepartmentComboBox().getSelectedItem();
	}
	
	public QSRReason getQSRReason() {
		return qsrReason;
	}
	
	public QCAction getQCAction() {
		return qcAction;
	}
	
	public Logger getLogger() {
		return Logger.getLogger(getParentPanel().getApplicationId());
	}

	public JComboBox getDepartmentComboBoxComponent() {
		return getProcessPointPanel().getDepartmentComboBox().getComponent();
	}

	public JComboBox getLineComboBoxComponent() {
		return getProcessPointPanel().getLineComboBox().getComponent();
	}
}
