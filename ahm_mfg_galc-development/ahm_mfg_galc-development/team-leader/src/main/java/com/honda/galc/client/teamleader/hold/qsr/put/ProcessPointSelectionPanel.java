package com.honda.galc.client.teamleader.hold.qsr.put;

import java.awt.GridBagLayout;
import java.io.Serializable;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;

import com.honda.galc.client.teamleader.hold.HoldUtils;
import com.honda.galc.client.teamleader.hold.config.Config;
import com.honda.galc.client.teamleader.hold.qsr.ProcessPointSelection;
import com.honda.galc.client.teamleader.hold.qsr.release.defect.ProcessPointSelectionListener;
import com.honda.galc.client.ui.component.ComboBoxModel;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.PropertyComboBoxRenderer;
import com.honda.galc.client.ui.component.PropertyPatternComboBoxRenderer;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.QCAction;

public class ProcessPointSelectionPanel extends JPanel implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String USER_DEFINED = "USER_DEFINED";
	private static final String DIVISION_SELECTED = "DIVISION_SELECTED";
	private static final String LINE_SELECTED = "LINE_SELECTED";
	private static final String PROCESS_POINT_SELECTED = "PROCESS_POINT_SELECTED";
	private LabeledComboBox department = new LabeledComboBox("Department", true);
	private LabeledComboBox line = new LabeledComboBox("Line", true);
	private LabeledComboBox processPoint = new LabeledComboBox("ProcessPoint", true);

	private ProcessPointSelection dialog;
	private ProductType productType;
	private QCAction qcAction;
	boolean isIncludeProcessPoint = true;

	public ProcessPointSelectionPanel(ProcessPointSelection dialog, ProductType productType, boolean isIncludeProcessPoint) {
		super();
		this.isIncludeProcessPoint = isIncludeProcessPoint;
		this.dialog = dialog;
		this.productType = productType;
		initComponents();
		addActionListeners();
		initModel();
	}

	private void initComponents() {
		setLayout(new GridBagLayout());
		add(createDepartmentComboBox(), HoldUtils.createHorizontalConstraint(0.3, 0));
		add(createLineComboBox(), HoldUtils.createHorizontalConstraint(0.3, 1));
		add(createProcessPointComboBox(), HoldUtils.createHorizontalConstraint(0.3, 2));
	}

	private void addActionListeners() {
		ProcessPointSelectionListener listener = new ProcessPointSelectionListener(this);
		getDepartmentComboBox().getComponent().addActionListener(listener);
		getLineComboBox().getComponent().addActionListener(listener);
		getProcessPointComboBox().getComponent().addActionListener(listener);
	}

	public void initModel() {
		this.qcAction = dialog.getQCAction();
		clearSelection();

		Division division = dialog.getDivision();
		List<Division> divisions;
		
		Config config = Config.getInstance(dialog.getMainWindow().getApplication().getApplicationId());

		if (isReasonsFromProperties()) {
			divisions =config.getDivisions(productType);
		} else {
			divisions = config.getDivisionsWithReasons(qcAction);
		}
		setDepartmentModel(divisions);
		if(divisions.contains(division)) {
			getDepartmentComboBox().getComponent().setSelectedItem(division);
		} else {
			getDepartmentComboBox().getComponent().setSelectedIndex(-1);
		}
	}

	private LabeledComboBox createDepartmentComboBox() {
		department.getComponent().setRenderer(new PropertyComboBoxRenderer<Division>(Division.class, "divisionName"));
		department.getComponent().setActionCommand(DIVISION_SELECTED);
		return department;
	}

	private LabeledComboBox createLineComboBox() {
		line.getComponent().setRenderer(new PropertyPatternComboBoxRenderer<Line>(Line.class, "%s ( %s )", "lineName", "lineId"));
		line.getComponent().setActionCommand(LINE_SELECTED);
		return line;
	}

	private LabeledComboBox createProcessPointComboBox() {
		processPoint.getComponent().setRenderer(new PropertyPatternComboBoxRenderer<ProcessPoint>
		(ProcessPoint.class, "%s ( %s )", "processPointName", "processPointId"));
		processPoint.getComponent().setActionCommand(PROCESS_POINT_SELECTED);
		return processPoint;
	}

	public void setDepartmentModel(List<Division> elements) {
		ComboBoxModel<Division> model = new ComboBoxModel<Division>(elements);
		getDepartmentComboBox().getComponent().setModel(model);
	}

	public void setLineModel(List<Line> elements) {
		DefaultComboBoxModel model = new DefaultComboBoxModel(new Vector<Line>(elements));
		getLineComboBox().getComponent().setModel(model);
	}

	public void setProcessPointModel(List<ProcessPoint> elements) {
		ComboBoxModel<ProcessPoint> model = new ComboBoxModel<ProcessPoint>(elements);
		getProcessPointComboBox().getComponent().setModel(model);
	}

	public void clearSelection() {
		getDepartmentComboBox().getComponent().setSelectedIndex(-1);
		getLineComboBox().getComponent().setSelectedIndex(-1);
		getProcessPointComboBox().getComponent().setSelectedIndex(-1);
	}

	public void setComponentVisibility(QCAction qcAction) {
		if(qcAction.getQcActionId().equals(QCAction.QCHOLD.getQcActionId()) || 
				qcAction.getQcActionId().equals(QCAction.QCRELEASE.getQcActionId())) {
			setVisible(true);
			getProcessPointComboBox().getComponent().setVisible(false);
			getProcessPointComboBox().getLabel().setVisible(false);
		} else if(qcAction.getQcActionId().equals(QCAction.KICKOUT.getQcActionId())) {
			setVisible(true);
			getProcessPointComboBox().getComponent().setVisible(true);
			getProcessPointComboBox().getLabel().setVisible(true);
		} else {
			setVisible(false);
			getProcessPointComboBox().getComponent().setVisible(false);
			getProcessPointComboBox().getLabel().setVisible(false);
		}
	}

	public boolean isReasonsFromProperties() {
		if (getQCAction().getQcActionId().equals(QCAction.QCHOLD.getQcActionId()))
			return !(getDialog().getProperty().getHoldReasons()[0].equalsIgnoreCase(USER_DEFINED));
		else if (getQCAction().getQcActionId().equals(QCAction.KICKOUT.getQcActionId()))
			return !(getDialog().getProperty().getKickoutReasons()[0].equalsIgnoreCase(USER_DEFINED));
		else if (getQCAction().getQcActionId().equals(QCAction.SCRAP.getQcActionId()))
			return !(getDialog().getProperty().getMassScrapReasons()[0].equalsIgnoreCase(USER_DEFINED));
		else if (getQCAction().getQcActionId().equals(QCAction.QCRELEASE.getQcActionId()))
			return !(getDialog().getProperty().getReleaseReasons()[0].equalsIgnoreCase(USER_DEFINED));
		else return !(getDialog().getProperty().getHoldReasons()[0].equalsIgnoreCase(USER_DEFINED));
	}

	public LabeledComboBox getDepartmentComboBox() {
		return department;
	}

	public LabeledComboBox getLineComboBox() {
		return line;
	}

	public LabeledComboBox getProcessPointComboBox() {
		return processPoint;
	}
	
	public ProductType getProductType() {
		return productType;
	}

	public ProcessPointSelection getDialog() {
		return dialog;
	}

	public QCAction getQCAction() {
		return qcAction;
	}
	public void setQCAction(QCAction qcAction) {
		this.qcAction = qcAction;
	}
}
