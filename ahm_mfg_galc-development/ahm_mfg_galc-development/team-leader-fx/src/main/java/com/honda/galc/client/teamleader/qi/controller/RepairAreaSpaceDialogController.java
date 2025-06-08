package com.honda.galc.client.teamleader.qi.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.model.ParkingLocationMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.RepairAreaSpaceDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.entity.qi.QiRepairArea;
import com.honda.galc.entity.qi.QiRepairAreaRow;
import com.honda.galc.entity.qi.QiRepairAreaSpace;
import com.honda.galc.entity.qi.QiRepairAreaSpaceId;
import com.honda.galc.util.AuditLoggerUtil;

import javafx.event.ActionEvent;
import javafx.stage.Stage;


/**
 * 
 * <h3>RepairAreaSpaceDialogController Class description</h3>
 * <p>
 * RepairAreaSpaceDialogController is used to create ,update ,cancel RepairAreaSpace.
 * </p>
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
 * </Table>
 * @author LnTInfotech<br>
 * 
 */
public class RepairAreaSpaceDialogController extends QiDialogController<ParkingLocationMaintenanceModel, RepairAreaSpaceDialog>{
	private QiRepairAreaSpace repairMethodCloned;
	LoggedButton updateBtn;
	QiRepairArea repairArea;
	String plantName;
	QiRepairAreaRow repairAreaRow;
	QiRepairAreaSpace repairAreaSpace;

	QiRepairAreaSpaceId id;

	public RepairAreaSpaceDialogController(ParkingLocationMaintenanceModel model, RepairAreaSpaceDialog dialog,QiRepairAreaRow repairAreaRow,String plantName,QiRepairArea repairArea,QiRepairAreaSpace repairAreaSpace) {
		super();
		setModel(model);
		setDialog(dialog);
		this.repairArea=repairArea;
		this.plantName=plantName;
		this.repairAreaRow=repairAreaRow;
		this.repairAreaSpace=repairAreaSpace;
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		clearDisplayMessage();
		if(actionEvent.getSource().equals(getDialog().getCreateBtn())) createBtnAction(actionEvent);
		else if(actionEvent.getSource().equals(getDialog().getUpdateButton())) updateBtnAction(actionEvent);
		else if(actionEvent.getSource().equals(getDialog().getCancelBtn())) cancelBtnAction(actionEvent);
	}

	private void createBtnAction(ActionEvent event) {
		LoggedButton createBtn = getDialog().getCreateBtn();
		String spaceNumber= StringUtils.trim(QiCommonUtil.delMultipleSpaces(getDialog().getSpaceNoTextField().getControl()));
		String repairAreaName=repairAreaRow.getId().getRepairAreaName();
		int repairAreaRowNo=repairAreaRow.getId().getRepairAreaRow();
		List<Object> checkMandatoryFieldList=new ArrayList<Object>();
		checkMandatoryFieldList.add(getDialog().getSpaceNoTextField().getControl());
		String errorMessage=QiCommonUtil.checkMandatoryFields(checkMandatoryFieldList);
		if(!errorMessage.equalsIgnoreCase("false")){
			displayErrorMessage("Mandatory field is empty", errorMessage);
			return;
		}	
		repairAreaSpace = new QiRepairAreaSpace();
		id=new QiRepairAreaSpaceId();
		id.setRepairAreaName(repairAreaName);
		id.setRepairArearRow(repairAreaRowNo);
		id.setRepairArearSpace(Integer.parseInt(spaceNumber));

		try{
			repairAreaSpace.setId(id);
			repairAreaSpace.setCreateUser(getUserId());
			repairAreaSpace.setActive(getDialog().getActiveRadioBtn().isSelected());
			repairAreaSpace.setAppCreateTimestamp(new Date());
			if(((ParkingLocationMaintenanceModel)getModel()).isRepairSpaceExists(repairAreaSpace))
				displayErrorMessage("Already exists! Enter a different Repair Space","Already exists! Enter a different Repair Space ");
			else{
				((ParkingLocationMaintenanceModel)getModel()).createRepairAreaSpace(repairAreaSpace);
				Stage stage = (Stage) createBtn.getScene().getWindow();
				stage.close();
			}
		}catch (Exception e) {
			handleException("Failed to Create Repair Area Space", StringUtils.EMPTY, e);
		}
	}

	private void updateBtnAction(ActionEvent event)
	{
		clearDisplayMessage();
		repairMethodCloned =(QiRepairAreaSpace) repairAreaSpace.deepCopy();
		updateBtn = getDialog().getUpdateButton();
		List<Object> checkMandatoryFieldList=new ArrayList<Object>();
		checkMandatoryFieldList.add(getDialog().getSpaceNoTextField().getControl());
		checkMandatoryFieldList.add(getDialog().getReasonForChangeTextArea());
		String errorMessage=QiCommonUtil.checkMandatoryFields(checkMandatoryFieldList);
		if(!errorMessage.equalsIgnoreCase("false")){
			displayErrorMessage("Mandatory field is empty", errorMessage);
			return;
		}
		repairAreaSpace.setActive(getDialog().getActiveRadioBtn().isSelected());
		repairAreaSpace.setUpdateUser(getUserId());	
		repairAreaSpace.setAppUpdateTimestamp(new Date());
		((ParkingLocationMaintenanceModel)getModel()).updateRepairAreaSpace(repairAreaSpace);
		//-----Start Audit log
		//call Auditlog utility to capture old and new data	

		AuditLoggerUtil.logAuditInfo(repairMethodCloned, repairAreaSpace,  getDialog().getReasonForChangeTextArea().getText(), getDialog().getScreenName(),getUserId());
		//-----End of Audit Log	
		Stage stage= (Stage) updateBtn.getScene().getWindow();
		stage.close();


	}

	private void cancelBtnAction(ActionEvent event) {
		LoggedButton cancelBtn = getDialog().getCancelBtn();
		try {
			Stage stage = (Stage) cancelBtn.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("Failed to perform cancel action", StringUtils.EMPTY, e);
		}
	}

	@Override
	public void initListeners() {
		addFieldListener(getDialog().getSpaceNoTextField().getControl(),false);
		
		getDialog().getActiveRadioBtn().getToggleGroup().selectedToggleProperty().addListener(updateEnablerForToggle);
	}

}
