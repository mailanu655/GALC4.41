/**
 * 
 */
package com.honda.galc.client.teamleader.hold.qsr.release.defect;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.teamleader.hold.qsr.release.ReleasePanel;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.entity.qics.DefectDescription;
import com.honda.galc.entity.qics.DefectResult;
import com.honda.galc.entity.qics.DefectResultId;
import com.honda.galc.entity.qics.DefectType;
import com.honda.galc.entity.qics.InspectionPart;
import com.honda.galc.entity.qics.InspectionPartLocation;
import com.honda.galc.entity.qics.PartGroup;

/**
 * 
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>CreateDefectAction</code> is ... .
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
public class CreateDefectAction extends BaseListener<ReleasePanel> implements ActionListener {
	private static final long serialVersionUID = 1L;

	private DefectDialog dialog;

	public CreateDefectAction(DefectDialog dialog) {
		super(dialog.getParentPanel());
		this.dialog = dialog;
	}

	@Override
	public void executeActionPerformed(ActionEvent e) {

		int productCount = getDialog().getHoldResults().size();
		StringBuilder msg = new StringBuilder();
		msg.append("Are you sure you want create defect for ").append(productCount).append(productCount == 1 ? " Product" : " Products");
		int retCode = JOptionPane.showConfirmDialog(getView(), msg, "Inline Defect", JOptionPane.YES_NO_OPTION);

		if (retCode != JOptionPane.YES_OPTION) {
			return;
		}

		try {

			ProcessPoint processPoint = (ProcessPoint) getDialog().getProcessPointComboBoxComponent().getSelectedItem();
			DefectDescription defectDescription = getDefectDescription();

			List<DefectResult> defects = new ArrayList<DefectResult>();
			Date now = new Date();
			DailyDepartmentSchedule schedule = getDailyDepartmentScheduleDao().find(processPoint.getDivisionId(), new Timestamp(System.currentTimeMillis()));
			for (HoldResult holdResult : getDialog().getHoldResults()) {
				DefectResult defectResult = assemble(processPoint, holdResult, defectDescription, schedule, now);
				defects.add(defectResult);
			}

			getDefectResultDao().saveInlineDefects(getView().getProductType(), defects);

			String message = "Request processed succesfully";
			JOptionPane.showMessageDialog(getView(), message);

			getDialog().setSelectionToCache();
		} finally {
			getDialog().dispose();
			getView().getInputPanel().getCommandButton().doClick();
		}
		return;
	}

	protected DefectDescription getDefectDescription() {
		PartGroup partGroup = getDialog().getDefectSelectionPanel().getPartGroupPane().getSelectedItem();
		InspectionPart part = getDialog().getDefectSelectionPanel().getPartPane().getSelectedItem();
		InspectionPartLocation location = getDialog().getDefectSelectionPanel().getLocationPane().getSelectedItem();
		DefectType defect = getDialog().getDefectSelectionPanel().getDefectPane().getSelectedItem();
		InspectionPart otherPart = getDialog().getDefectSelectionPanel().getOtherPartPane().getSelectedItem();
		DefectDescription defectDescription = getDialog().getDefectSelectionPanel().getDefectDescription(partGroup, part, location, defect, otherPart);
		return defectDescription;
	}

	protected DefectResult assemble(ProcessPoint processPoint, HoldResult holdResult, DefectDescription defectDescription, DailyDepartmentSchedule schedule, Date currentTime) {

		DefectResult defectResult = new DefectResult();
		String entryStationId = trim(getView().getMainWindow().getApplication().getApplicationId());
		defectResult.setNewDefect(true);
		defectResult.setId(new DefectResultId());
		defectResult.getId().setApplicationId(entryStationId);
		defectResult.getId().setProductId(holdResult.getId().getProductId());
		defectResult.getId().setInspectionPartName(defectDescription.getId().getInspectionPartName());
		defectResult.getId().setInspectionPartLocationName(defectDescription.getId().getInspectionPartLocationName());
		defectResult.getId().setDefectTypeName(defectDescription.getId().getDefectTypeName());
		defectResult.getId().setSecondaryPartName(defectDescription.getId().getSecondaryPartName());
		defectResult.getId().setTwoPartPairPart(defectDescription.getId().getTwoPartPairPart());
		defectResult.getId().setTwoPartPairLocation(defectDescription.getId().getTwoPartPairLocation());

		defectResult.setEntryStation(entryStationId);
		// Use Entry Department and Write Up Department Name instead of ID
		defectResult.setEntryDept(processPoint.getDivisionName());
		defectResult.setWriteUpDepartment(processPoint.getDivisionName());
		defectResult.setDefectStatus(DefectStatus.OUTSTANDING);
		defectResult.setOutstandingFlag(true);
		defectResult.setIqsCategoryName(defectDescription.getIqsCategoryName());
		defectResult.setIqsItemName(defectDescription.getIqsItemName());
		defectResult.setRegressionCode(defectDescription.getRegressionCode());
		defectResult.setResponsibleDept(defectDescription.getResponsibleDept());
		defectResult.setResponsibleLine(defectDescription.getResponsibleLine());
		defectResult.setResponsibleZone(defectDescription.getResponsibleZone());
		defectResult.setTwoPartDefectFlag(defectDescription.getTwoPartDefectFlag());
		defectResult.setEngineFiring(defectDescription.getEngineFiringFlag());
		if (schedule != null) {
			defectResult.setDate(schedule.getId().getProductionDate());
			defectResult.setShift(schedule.getId().getShift());
		}
		defectResult.setAssociateNo(getView().getMainWindow().getUserId().trim());
		defectResult.setCreateTimestamp(new Timestamp(currentTime.getTime()));
		defectResult.setActualTimestamp(new Timestamp(currentTime.getTime()));
		return defectResult;
	}

	public DefectDialog getDialog() {
		return dialog;
	}
}
