package com.honda.galc.client.teamleader.hold.qsr.put.dialog;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import com.honda.galc.client.teamleader.hold.HoldPanel;
import com.honda.galc.client.teamleader.hold.config.QsrMaintenancePropertyBean;
import com.honda.galc.client.teamleader.hold.qsr.ProcessPointSelection;
import com.honda.galc.client.teamleader.hold.qsr.QSRDialog;
import com.honda.galc.client.teamleader.hold.qsr.QSRReason;
import com.honda.galc.client.teamleader.hold.qsr.put.ProcessPointSelectionPanel;
import com.honda.galc.client.teamleader.hold.qsr.put.listener.MassScrapInputListener;
import com.honda.galc.client.teamleader.hold.qsr.release.defect.DefectReleaseInputPanel;
import com.honda.galc.client.teamleader.hold.qsr.release.defect.qics.TextDefectSelectionComponent;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LimitedLengthPlainDocument;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.QCAction;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.service.property.PropertyService;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>DefectDialog</code> is ... .
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

public class MassScrapDialog extends JDialog implements ProcessPointSelection, QSRDialog{

	private static final long serialVersionUID = 1L;
	private static final int ELEMENT_HEIGHT = 25;

	private HoldPanel parentPanel;
	private ProcessPointSelectionPanel processPointPanel;
	private TextDefectSelectionComponent defectSelectionPanel;
	private DefectReleaseInputPanel releaseInputPanel;

	private QCAction qcAction;
	private QSRReason qsrReason;
	
	// === model === //
	private List<BaseProduct> products;
	
	private QsrMaintenancePropertyBean propertyBean=null;

	public MassScrapDialog(HoldPanel parentPanel, String title, List<BaseProduct> products) {
		super(parentPanel.getMainWindow(), title, true);
		this.parentPanel = parentPanel;
		this.products = products;
		this.qcAction = QCAction.SCRAP;
		setSize(1000, 725);
		propertyBean=PropertyService.getPropertyBean(QsrMaintenancePropertyBean.class, getParentPanel().getApplicationId());
		initView();
		mapActions();
		initModel();
		
		getAssociateNameInput().setEnabled(true);
		getPhoneInput().setEnabled(true);
		getCommentInput().setEnabled(true);
	}

	protected void initView() {
		qsrReason = new QSRReason(this);

		setLayout(null);

		processPointPanel = createProcessPointComponent();

		Component base = processPointPanel;
		defectSelectionPanel = createDefectTextSelectionPanel();
		getDefectSelectionPanel().setLocation(base.getX(), base.getY() + base.getHeight() + 5);


		releaseInputPanel = createReleaseInputPanel();
		base = getDefectSelectionPanel();
		releaseInputPanel.setLocation(base.getX(), base.getY() + base.getHeight() + 5);
		
		add(processPointPanel);
		add(getDefectSelectionPanel());
		add(releaseInputPanel);

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);

		getRootPane().setDefaultButton(getCancelButton());
	}

	protected void mapActions() {
		getProcessPointPanel().getDepartmentComboBox().getComponent().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if( getProcessPointPanel().getLineComboBox().getComponent().getSelectedItem() == null) {
					getProcessPointPanel().getProcessPointComboBox().getComponent().setModel(new DefaultComboBoxModel());
				}
				qsrReason.populateReasonsByDiv(qcAction);				
			}
		});
		
		getProcessPointPanel().getLineComboBox().getComponent().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				qsrReason.populateReasonsByDivAndLine(qcAction);				
			}
		});

		getCancelButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		final JTextComponent reasonTextField = (JTextComponent) getReasonInput().getEditor().getEditorComponent();
		MassScrapInputListener editReasonInputListener = new MassScrapInputListener(this) {
			@Override
			protected String getReason() {
				return reasonTextField.getText();
			}
		};
		
		getProcessPointPanel().getProcessPointComboBox().getComponent().addItemListener(new ItemListener() {
		public void itemStateChanged(ItemEvent ie) {
			if (ItemEvent.DESELECTED == ie.getStateChange()) {
				getDefectSelectionPanel().setProcessPoint(null);
			} else if (ItemEvent.SELECTED == ie.getStateChange()) {
				ProcessPoint processPoint = (ProcessPoint) getProcessPointPanel().getProcessPointComboBox().getComponent().getSelectedItem();
				getDefectSelectionPanel().setProcessPoint(processPoint);
			}				
		}
	});
		
		reasonTextField.getDocument().addDocumentListener(editReasonInputListener);

		MassScrapInputListener inputListener = new MassScrapInputListener(this);
		getDefectSelectionPanel().getOtherPartPane().getTable().getSelectionModel().addListSelectionListener(inputListener);

		getAssociateNameInput().getDocument().addDocumentListener(inputListener);
		getPhoneInput().getDocument().addDocumentListener(inputListener);
		getCommentInput().getDocument().addDocumentListener(inputListener);
		getProcessPointPanel().getProcessPointComboBox().getComponent().addActionListener(inputListener);

		getSubmitButton().addActionListener(new MassScrapAction(this));
	}

	protected void initModel() {
		getAssociateIdInput().setText(getParentPanel().getMainWindow().getUserId().trim());
		qsrReason.populateReasonsByDiv(qcAction);
	}

	// === factory methods === //
	protected ProcessPointSelectionPanel createProcessPointComponent() {
		ProcessPointSelectionPanel panel = new ProcessPointSelectionPanel(this, getParentPanel().getProductType(), false);		
		panel.setSize(980, 40);
		return panel;
	}

	protected TextDefectSelectionComponent createDefectTextSelectionPanel() {
		TextDefectSelectionComponent panel = new TextDefectSelectionComponent(getParentPanel(), new HashMap<String, Object>());
		panel.setSize(980, 500);
		return panel;
	}

	protected DefectReleaseInputPanel createReleaseInputPanel() {	
		DefectReleaseInputPanel panel = new DefectReleaseInputPanel();
		panel.setSize(985, 3 * ELEMENT_HEIGHT + 60);
		return panel;
	}

	protected JLabel createReasonLabel() {
		JLabel element = new JLabel("Reason");
		element.setSize(80, ELEMENT_HEIGHT);
		element.setLocation(10, 10);
		element.setFont(getLabelFont());
		return element;
	}

	protected JComboBox createReasonInput() {
		JComboBox element = new JComboBox();
		Component base = getReasonLabel();
		element.setSize(480, ELEMENT_HEIGHT);
		element.setLocation(base.getX() + base.getWidth(), base.getY());
		element.setFont(getInputFont());
		element.setEditable(true);
		Document document = new LimitedLengthPlainDocument(80 - (5 + 1));
		JTextComponent editor = (JTextComponent) element.getEditor().getEditorComponent();
		editor.setDocument(document);
		element.setSelectedIndex(-1);
		return element;
	}

	// === get/set === //
	public List<BaseProduct> getProducts() {
		return products;
	}

	public HoldPanel getParentPanel() {
		return parentPanel;
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
	
	public ProcessPointSelectionPanel getProcessPointPanel() {
		return processPointPanel;
	}
	
	public JLabel getReasonLabel() {
		return getReleaseInputPanel().getReasonLabel();
	}
	
	public JComboBox getReasonInput() {
		return getReleaseInputPanel().getReasonInput();
	}
	
	public DefectReleaseInputPanel getReleaseInputPanel() {
		return releaseInputPanel;
	}
	
	public JTextField getAssociateIdInput() {
		return getReleaseInputPanel().getAssociateIdInput();
	}

	public JTextField getAssociateNameInput() {
		return getReleaseInputPanel().getAssociateNameInput();
	}

	public JLabel getAssociateNameLabel() {
		return getReleaseInputPanel().getAssociateNameLabel();
	}

	public JButton getCancelButton() {
		return getReleaseInputPanel().getCancelButton();
	}

	public JTextField getPhoneInput() {
		return getReleaseInputPanel().getPhoneInput();
	}

	public JButton getSubmitButton() {
		return getReleaseInputPanel().getSubmitButton();
	}

	public JTextField getCommentInput() {
		return getReleaseInputPanel().getCommentInput();
	}

	public JLabel getCommentLabel() {
		return getReleaseInputPanel().getCommentLabel();
	}

	public TextDefectSelectionComponent getDefectSelectionPanel() {
		return defectSelectionPanel;
	}
	
	public QsrMaintenancePropertyBean getProperty() {
		return propertyBean;
	}
	
	public Division getDivision() {
		return getParentPanel().getDivision();
	}
	
	public Logger getLogger() {
		return Logger.getLogger(getParentPanel().getApplicationId());
	}

	public MainWindow getMainWindow() {
		return getParentPanel().getMainWindow();
	}
	
	public JComboBox getDepartmentComboBoxComponent() {
		return getProcessPointPanel().getDepartmentComboBox().getComponent();
	}
	
	public JComboBox getLineComboBoxComponent() {
		return getProcessPointPanel().getLineComboBox().getComponent();
	}
	
	public QCAction getQCAction() {
		return qcAction;
	}
}
