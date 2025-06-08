package com.honda.galc.client.teamleader.hold.qsr.release.defect;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.honda.galc.client.teamleader.hold.config.QsrMaintenancePropertyBean;
import com.honda.galc.client.teamleader.hold.qsr.ProcessPointSelection;
import com.honda.galc.client.teamleader.hold.qsr.QSRDialog;
import com.honda.galc.client.teamleader.hold.qsr.QSRReason;
import com.honda.galc.client.teamleader.hold.qsr.put.ProcessPointSelectionPanel;
import com.honda.galc.client.teamleader.hold.qsr.release.ReleasePanel;
import com.honda.galc.client.teamleader.hold.qsr.release.defect.qics.TextDefectSelectionComponent;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.QCAction;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.entity.qics.InspectionPart;
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

public class DefectDialog extends JDialog implements ProcessPointSelection, QSRDialog {

	private static final long serialVersionUID = 1L;

	private ReleasePanel parentPanel;
	private ProcessPointSelectionPanel processPointPanel;
	private TextDefectSelectionComponent defectSelectionPanel;
	private DefectReleaseInputPanel releaseInputPanel;

	// === model === //
	private List<HoldResult> qsrHoldResults;
	private List<HoldResult> holdResults;
	private Map<String, Object> selectionCache;

	private QsrMaintenancePropertyBean propertyBean;
	
	private QCAction qcAction = QCAction.SCRAP;
	private QSRReason qsrReason;

	public DefectDialog(ReleasePanel parentPanel, String title, List<HoldResult> qsrHoldResults, List<HoldResult> holdResults, Map<String, Object> selectionCache) {
		super(parentPanel.getMainWindow(), title, true);
		this.parentPanel = parentPanel;
		this.qsrHoldResults = qsrHoldResults;
		this.holdResults = holdResults;
		this.selectionCache = selectionCache == null ? new HashMap<String, Object>() : selectionCache;
		qsrReason = new QSRReason(this);
		propertyBean=PropertyService.getPropertyBean(QsrMaintenancePropertyBean.class, getParentPanel().getApplicationId());
		setSize(1000, 730);
		initView();
		mapActions();
		initModel();
		initState();
	}

	protected void initView() {
		setLayout(null);

		processPointPanel = createProcessPointComponent();
		add(processPointPanel);

		Component base = processPointPanel;
		defectSelectionPanel = createDefectTextSelectionPanel();
		getDefectSelectionPanel().setLocation(base.getX(), base.getY() + base.getHeight() + 5);
		add(getDefectSelectionPanel());

		releaseInputPanel = createReleaseInputPanel();
		base = getDefectSelectionPanel();
		getReleaseInputPanel().setLocation(base.getX(), base.getY() + base.getHeight() + 5);
		add(getReleaseInputPanel());

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		getAssociateIdInput().setText(getMainWindow().getUserId().trim());
		getRootPane().setDefaultButton(getCancelButton());
		getReasonInput().setVisible(false);
		getReasonLabel().setVisible(false);
	}

	protected void mapActions() {
		getCancelButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		ListSelectionListener inputListener = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					return;
				}
				InspectionPart otherPart = getDefectSelectionPanel().getOtherPartPane().getSelectedItem();
				boolean inputValid = otherPart != null;
				getSubmitButton().setEnabled(inputValid);
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

		getDefectSelectionPanel().getOtherPartPane().getTable().getSelectionModel().addListSelectionListener(inputListener);
		getSubmitButton().addActionListener(new CreateDefectAction(this));
	}

	protected void initModel() {
		getAssociateIdInput().setText(getParentPanel().getMainWindow().getUserId().trim());
		qsrReason.populateReasonsByDiv(qcAction);
	}

	protected void initState() {
		setSelectionFromCache();
	}

	protected void setSelectionFromCache() {
		Division division = (Division) getSelectionCache().get("division");
		if (division != null) {
			getDepartmentComboBoxComponent().setSelectedItem(division);
			ProcessPoint processPoint = (ProcessPoint) getSelectionCache().get("processPoint");
			if (processPoint != null) {
				getProcessPointComboBoxComponent().setSelectedItem(processPoint);
				getDefectSelectionPanel().setSelectionFromCache();
			} else {
				getProcessPointComboBoxComponent().setSelectedIndex(-1);
			}
		} else {
			getDepartmentComboBoxComponent().setSelectedIndex(-1);
		}
	}

	public void setSelectionToCache() {

		Division division = (Division) getDepartmentComboBoxComponent().getSelectedItem();
		ProcessPoint processPoint = (ProcessPoint) getProcessPointComboBoxComponent().getSelectedItem();

		if (division != null) {
			getSelectionCache().put("division", division);
			if (processPoint != null) {
				getSelectionCache().put("processPoint", processPoint);
				getDefectSelectionPanel().setSelectionToCache();
			}
		}
	}

	// === factory methods === //
	protected ProcessPointSelectionPanel createProcessPointComponent() {
		ProcessPointSelectionPanel panel = new ProcessPointSelectionPanel(this, getParentPanel().getProductType(), false);
		panel.setLocation(10, 10);
		panel.setSize(980, 40);
		return panel;
	}

	protected TextDefectSelectionComponent createDefectTextSelectionPanel() {
		TextDefectSelectionComponent panel = new TextDefectSelectionComponent(getParentPanel(), getSelectionCache());
		panel.setSize(980, 500);
		return panel;
	}

	protected DefectReleaseInputPanel createReleaseInputPanel() {
		DefectReleaseInputPanel panel = new DefectReleaseInputPanel();
		panel.setSize(980, 3 * panel.getElementHeight() + 60);
		return panel;
	}

	// === get/set === //
	public List<HoldResult> getHoldResults() {
		return holdResults;
	}

	public ReleasePanel getParentPanel() {
		return parentPanel;
	}

	public List<HoldResult> getQsrHoldResults() {
		return qsrHoldResults;
	}
	
	public JLabel getReasonLabel() {
		return getReleaseInputPanel().getReasonLabel();
	}
	
	public JComboBox getReasonInput() {
		return getReleaseInputPanel().getReasonInput();
	}

	public JTextField getAssociateIdInput() {
		return getReleaseInputPanel().getAssociateIdInput();
	}

	public JTextField getAssociateNameInput() {
		return getReleaseInputPanel().getAssociateNameInput();
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

	public TextDefectSelectionComponent getDefectSelectionPanel() {
		return defectSelectionPanel;
	}
	
	public ProcessPointSelectionPanel getProcessPointPanel() {
		return processPointPanel;
	}
	
	public DefectReleaseInputPanel getReleaseInputPanel() {
		return releaseInputPanel;
	}

	public JComboBox getDepartmentComboBoxComponent() {
		return processPointPanel.getDepartmentComboBox().getComponent();
	}
	
	public JComboBox getLineComboBoxComponent() {
		return processPointPanel.getLineComboBox().getComponent();
	}

	public JComboBox getProcessPointComboBoxComponent() {
		return processPointPanel.getProcessPointComboBox().getComponent();
	}

	public Map<String, Object> getSelectionCache() {
		return selectionCache;
	}

	public Division getDivision() {
		return (Division) getParentPanel().getInputPanel().getDepartmentComboBox().getSelectedItem();
	}
	
	public MainWindow getMainWindow() {
		return getParentPanel().getMainWindow();
	}
	
	public QsrMaintenancePropertyBean getProperty() {
		return propertyBean;
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
}
