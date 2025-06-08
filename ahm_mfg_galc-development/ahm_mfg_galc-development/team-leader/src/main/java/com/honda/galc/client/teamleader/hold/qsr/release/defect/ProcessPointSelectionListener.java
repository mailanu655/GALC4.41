package com.honda.galc.client.teamleader.hold.qsr.release.defect;

import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.teamleader.hold.config.Config;
import com.honda.galc.client.teamleader.hold.config.QsrMaintenancePropertyBean;
import com.honda.galc.client.teamleader.hold.qsr.ProcessPointSelection;
import com.honda.galc.client.teamleader.hold.qsr.put.ProcessPointSelectionPanel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.ProcessPoint;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ProcessPointSelectionListener</code> is ...
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
 * <TD>Jan 26, 2010</TD>
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
public class ProcessPointSelectionListener extends BaseListener<ProcessPointSelectionPanel> implements ItemListener {

	private static final String SELECTED = " is selected";


	private QsrMaintenancePropertyBean propertyBean = null;

	private ProcessPointSelectionPanel panel;

	private ProcessPointSelection dialog;

	public ProcessPointSelectionListener(ProcessPointSelectionPanel processPointSelectionPanel) {
		super(processPointSelectionPanel);
		this.panel = processPointSelectionPanel;
		this.dialog = panel.getDialog();
		propertyBean= dialog.getProperty();

	}

	@Override
	public void executeActionPerformed(ActionEvent e) {

		if (e.getSource().equals(getPanel().getDepartmentComboBox().getComponent())) {
			Division division = selectedDivision();
			if(division == null) {
				getLineComboBox().setModel(new DefaultComboBoxModel());

				return;
			}
			Logger.getLogger().info(selectedDivision().getDivisionId() + SELECTED);               
			
			List<Line> lines;
			if(getPanel().isReasonsFromProperties()) {
				lines = Config.getInstance().getLinesByDivision((Division) selectedDivision());
			} else {
				lines = Config.getInstance().getLinesWithReasons((Division) selectedDivision(), getPanel().getQCAction());
			}
			getPanel().setLineModel(lines);
			getLineComboBox().setSelectedIndex(-1);

		} else if (e.getSource().equals(getPanel().getLineComboBox().getComponent())) {
			Line line = selectedLine();
			if (line == null) {
				getProcessPointComboBox().setModel(new DefaultComboBoxModel());
				return;
			}
			Logger.getLogger().info(selectedLine().getLineId() + SELECTED);               

			Division division = selectedDivision();

			List<ProcessPoint> processPoints = Config.getInstance().getProcessPoints(division, line, getPanel().getProductType());
			if (processPoints == null) {
				processPoints = new ArrayList<ProcessPoint>();
			}
			getPanel().setProcessPointModel(processPoints);
			getProcessPointComboBox().setSelectedIndex(-1);

		} else if (e.getSource().equals(getPanel().getProcessPointComboBox().getComponent())) {		
			Logger.getLogger().info(selectedProcessPoint() + SELECTED);               
		}
	}

	public Division selectedDivision() {
		Division selectedDivision = (Division) getDepartmentComboBox().getSelectedItem();
		return selectedDivision == null ? null : selectedDivision;
	}

	public Line selectedLine() {
		Line  selectedLine  = (Line) getLineComboBox().getSelectedItem();
		return selectedLine == null ? null : selectedLine;
	}

	public String selectedProcessPoint() {
		ProcessPoint selectedProcessPoint = (ProcessPoint) getProcessPointComboBox().getSelectedItem();
		return selectedProcessPoint == null ? null : selectedProcessPoint.getDivisionId();
	}

	public ProcessPointSelectionPanel getPanel() {
		return panel;
	}

	public QsrMaintenancePropertyBean getProperty() {
		return propertyBean;
	}

	public JComboBox getDepartmentComboBox() {
		return getPanel().getDepartmentComboBox().getComponent();
	}

	public JComboBox getLineComboBox() {
		return getPanel().getLineComboBox().getComponent();
	}

	public JComboBox getProcessPointComboBox() {
		return getPanel().getProcessPointComboBox().getComponent();
	}
}
