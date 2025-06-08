package com.honda.galc.client.qics.view.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.Timestamp;
import java.util.Date;

import javax.swing.Action;

import com.honda.galc.client.qics.view.dialog.SelectAssociateNumberDialog;
import com.honda.galc.client.qics.view.screen.DefectTextInputPanel;
import com.honda.galc.client.qics.view.screen.QicsPanel;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.entity.qics.DefectRepairResult;
import com.honda.galc.entity.qics.DefectResult;
/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * Implementation of <code>Action</code> interface for <i>Accept</i> button
 * on qics text input defect panel.
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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

public class AcceptNewDefectAction extends AbstractPanelAction {

	private static final long serialVersionUID = 1L;

	public AcceptNewDefectAction(QicsPanel qicsPanel) {
		super(qicsPanel);
		init();
	}

	protected void init() {
		putValue(Action.NAME, "Accept");
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_P);
	}

	@Override
	protected void execute(ActionEvent e) {

		DefectResult repairResultData = getQicsPanel().getRepairResultData();
	
		String associateNumber = getQicsFrame().getUserId();
		repairResultData.setAssociateNo(associateNumber);
		if (repairResultData.isRepairedStatus() || repairResultData.isScrapStatus()) {
			if (getQicsController().getQicsPropertyBean().isOverrideRepairAssociateEnabled()) {
				SelectAssociateNumberDialog dialog = new SelectAssociateNumberDialog(getQicsFrame(), "Enter Repair Associate ID", true);
				dialog.setLocationRelativeTo(getQicsFrame());
				dialog.loadComboBox(getQicsController().getAssociateNumbers().toArray());
				dialog.setVisible(true);
				boolean cancelled = dialog.isCancelled();

				if (cancelled) {
					getQicsPanel().getOtherPartPane().clearSelection();
					return;
				} else {
					associateNumber = String.valueOf(dialog.getReturnValue());
					getQicsFrame().getQicsController().cacheAssociateNumber(associateNumber);
				}
			}
			DefectRepairResult defectRepairResult = repairResultData.getDefectRepairResult();
			Timestamp now = new Timestamp(new Date().getTime());
			repairResultData.setRepairTimestamp(now);
			defectRepairResult.setRepairAssociateNo(associateNumber);
			defectRepairResult.setActualTimestamp(now);
			defectRepairResult.setActualProblemName(repairResultData.getDefectTypeName());
			defectRepairResult.setRepairDept(getQicsController().getClientModel().getProcessPoint().getDivisionId());
			defectRepairResult.setRepairProcessPointId(getQicsController().getProcessPointId());
			defectRepairResult.setComment("QUICK REPAIR");
			repairResultData.setRepairAssociateNo(associateNumber);
		}
		
		if(getQicsController().getProductModel().isDuplicatedTextDefect(repairResultData)){
			Boolean duplicatesAllowed = this.getQicsPanel().getQicsPropertyBean().isDuplicateDefectAllowed();
			Boolean duplicatesCheckOn = this.getQicsPanel().getQicsPropertyBean().isDuplicateDefectCheckOn();
			
			if (!duplicatesAllowed){
				this.getQicsPanel().getOtherPartPane().clearSelection();
				this.setErrorMessage("Duplicate defects not allowed.");
				return;
			}
			
			if (duplicatesCheckOn && !MessageDialog.confirm(this.getQicsPanel(),
			"Defect already exists. Do you want to enter duplicate entry?")){
				this.getQicsPanel().getOtherPartPane().clearSelection();
				return;
			}
		}
		getQicsController().getProductModel().addNewDefect(repairResultData);
		getQicsPanel().setButtonsState();
		getQicsPanel().getDefectPane().clearSelection();

	}

	@Override
	protected DefectTextInputPanel getQicsPanel() {
		return (DefectTextInputPanel) super.getQicsPanel();
	}
}
