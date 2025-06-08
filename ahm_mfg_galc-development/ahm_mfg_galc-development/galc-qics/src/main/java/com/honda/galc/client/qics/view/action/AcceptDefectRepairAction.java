package com.honda.galc.client.qics.view.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.Action;
import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMain;
import com.honda.galc.client.qics.property.DefaultQicsPropertyBean;
import com.honda.galc.client.qics.validator.QicsValidator;
import com.honda.galc.client.qics.view.screen.DefectRepairPanel;
import com.honda.galc.client.qics.view.screen.QicsPanel;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.qics.DefectRepairResult;
import com.honda.galc.entity.qics.DefectResult;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.ValidatorUtils;

/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * Implementation of <code>Action</code> interface for <i>Accept</i> button
 * on qics defect repair panel.
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
public class AcceptDefectRepairAction extends AbstractPanelAction {

	private static final long serialVersionUID = 1L;

	public AcceptDefectRepairAction(QicsPanel qicsPanel) {
		super(qicsPanel);
		init();
	}

	protected void init() {
		putValue(Action.NAME, "Accept");
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_P);
	}

	@Override
	protected void execute(ActionEvent e) {

		DefectResult repairResultData = getQicsPanel().getSelectedItem();
		DefectStatus selectedDefectStatus = DefectStatus.getType(getQicsPanel().getDefectStatusPanel().getSelectedStatus());

		if (DefectStatus.REPAIRED.equals(selectedDefectStatus)) {
			List<String> validationMessages = validateInput();
			if (!validationMessages.isEmpty()) {
				String validationMessage = ValidatorUtils.formatMessages(validationMessages);
				JOptionPane.showMessageDialog(getQicsPanel(), validationMessage, "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			repairResultData = setRepairData(repairResultData);
			getQicsController().cacheAssociateNumber(repairResultData.getRepairAssociateNo());
		} 
		else {
			repairResultData.getDefectRepairResults().clear();
		}		
		repairResultData = setCommonData(repairResultData);

		if (repairResultData.getId().getDefectResultId() > 0) {
			boolean alreadyAdded = false;
			for (DefectResult data : getQicsPanel().getProductModel().getUpdatedDefects()) {
				if (data == null) {
					continue;
				}
				if (data.getId().getDefectResultId() == repairResultData.getId().getDefectResultId()) {
					alreadyAdded = true;
					break;
				}
			}
			if (!alreadyAdded) {
				getQicsPanel().getProductModel().addUpdatedDefect(repairResultData);
			}
		}

		getQicsPanel().resetDefectTable();
	}

	@Override
	protected DefectRepairPanel getQicsPanel() {
		return (DefectRepairPanel) super.getQicsPanel();
	}

	protected List<String> validateInput() {
		List<String> messages = new ArrayList<String>();
		Object repairTime = getQicsPanel().getInputPane().getTimeSpinner().getValue();
		Object associateNumber = getQicsPanel().getInputPane().getAssociateNumberComboBox().getSelectedItem();
		String comments=getQicsPanel().getInputPane().getCommentInputElement().getText().trim();
		if(getQicsController().getQicsPropertyBean().isDefectActualProbRepairMethodEnabled())
		{
			Object repairProblem = getQicsPanel().getInputPane().getActualProblemComboBox().getSelectedItem();
			Object repairMethod = getQicsPanel().getInputPane().getRepairMethodComboBox().getSelectedItem();
			messages.addAll(QicsValidator.validateRepairProblem(repairProblem));
			messages.addAll(QicsValidator.validateRepairMethod(repairMethod));
		}
		messages.addAll(QicsValidator.validateRepairTime(repairTime));
		messages.addAll(QicsValidator.validateAssociateNumber(associateNumber));
		if(getQicsController().getQicsPropertyBean().isRepairCommentRequired())
			messages.addAll(QicsValidator.validateComment(comments));
		return messages;
	}

	protected DefectResult setCommonData(DefectResult repairResultData) {
		// JH
//		repairResultData.isChangeAtRepair = true;
		repairResultData.setDefectStatus((short)getQicsPanel().getDefectStatusPanel().getSelectedStatus());
		DefaultQicsPropertyBean qicsProperty = PropertyService.getPropertyBean(DefaultQicsPropertyBean.class);
		if (qicsProperty.isOutstandingFlagChangable()) {
			repairResultData.setOutstandingFlag(repairResultData.isOutstandingStatus());
		}
		repairResultData.setWriteUpDepartment(getQicsController().getClientModel().getProcessPoint().getDivisionName());
		return repairResultData;
	}

	protected String getRepairAssociateNo() {
		String associateNumber = null;
		if (StringUtils.isNotBlank(getAuthorizationGroup())) {
			associateNumber = ClientMain.getInstance().getAccessControlManager().getUserName();
		} else {
			associateNumber = (String) getQicsPanel().getInputPane().getAssociateNumberComboBox().getSelectedItem();
		}
		return associateNumber;
	}
	
	protected DefectResult setRepairData(DefectResult defectResult) {

		Object t = getQicsPanel().getInputPane().getTimeSpinner().getValue();
		int repairTime = t == null ? 0 : Integer.valueOf(t.toString());
		String associateNumber = getRepairAssociateNo();
		Object value = getQicsPanel().getInputPane().getActualProblemComboBox().getSelectedItem();
		String actualProblem = value == null ? null : value.toString();
		value = getQicsPanel().getInputPane().getRepairMethodComboBox().getSelectedItem();
		String repairMethod = value == null ? null : value.toString();
		String comment = getQicsPanel().getInputPane().getCommentInputElement().getText().trim();
		
		Timestamp now = new Timestamp(new Date().getTime());
		DefectRepairResult defectRepairResult = defectResult.getDefectRepairResult();
		defectRepairResult.setRepairTime(repairTime);
		defectRepairResult.setRepairAssociateNo(associateNumber);
		defectRepairResult.setActualProblemName(actualProblem);
		defectRepairResult.setRepairMethodName(repairMethod);
		defectRepairResult.setComment(comment);
		defectRepairResult.setRepairDept(getQicsController().getClientModel().getProcessPoint().getDivisionId());
		defectRepairResult.setRepairProcessPointId(getQicsController().getProcessPointId());
		defectRepairResult.setActualTimestamp(now);
		defectRepairResult.setUpdateTimestamp(now);
		defectResult.setRepairAssociateNo(associateNumber);
		defectResult.setRepairTimestamp(now);
		defectResult.setUpdateTimestamp(now);
		return defectResult;
	}
}
