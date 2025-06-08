package com.honda.galc.client.teamleader.qi.controller;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.teamleader.qi.model.ParkingLocationMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.RepairAreaRowDataDialog;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledUpperCaseTextField;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.entity.qi.QiRepairAreaRow;
import com.honda.galc.entity.qi.QiRepairAreaRowId;
import com.honda.galc.util.AuditLoggerUtil;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

/**
 * 
 * <h3>RepairAreaRowDataDialogController Class description</h3>
 * <p>
 * RepairAreaRowDataDialogController is used to create ,update ,cancel RepairAreaRow.
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
public class RepairAreaRowDataDialogController extends QiDialogController<ParkingLocationMaintenanceModel, RepairAreaRowDataDialog> {
	
	private QiRepairAreaRow qiRepairAreaRow;
	private QiRepairAreaRow qiRepairAreaRowCloned;
	public RepairAreaRowDataDialogController(ParkingLocationMaintenanceModel model,RepairAreaRowDataDialog dialog ,QiRepairAreaRow selectedRepairAreaRow) {
		setModel(model);
		setDialog(dialog);
		this.qiRepairAreaRow = selectedRepairAreaRow;
	}
	@Override
	public void handle(ActionEvent actionEvent) {
		clearDisplayMessage();
		if(actionEvent.getSource().equals(getDialog().getCreateBtn())) createRepairAreaRow();
		else if(actionEvent.getSource().equals(getDialog().getUpdateButton())) updateRepairAreaRow();
		else if(actionEvent.getSource().equals(getDialog().getCancelBtn())) cancelRepairAreaRow();
	}
	@Override
	public void initListeners() {
		addFieldListener(getDialog().getRowNoTextField().getControl(),false);

		getDialog().getSpaceFillSeqCombobox().getControl().valueProperty().addListener(updateEnablerForStringValueChange);
	}
	/**
	 * This method is used to create RepairAreaRow
	 * @param actionEvent
	 */
	private void createRepairAreaRow(){
		LoggedButton createBtn = getDialog().getCreateBtn();
		LabeledUpperCaseTextField rowNoTextField = getDialog().getRowNoTextField();
		LabeledComboBox<String> spaceFillSeqCombobox= getDialog().getSpaceFillSeqCombobox();
		
		List<Object> checkMandatoryFieldList=new ArrayList<Object>();
		checkMandatoryFieldList.add(rowNoTextField.getControl());
		checkMandatoryFieldList.add(spaceFillSeqCombobox.getControl());
		String errorMessage=QiCommonUtil.checkMandatoryFields(checkMandatoryFieldList);
		if(!errorMessage.equalsIgnoreCase("false")){
			displayErrorMessage("Mandatory field is empty", errorMessage);
			return;
		}
		qiRepairAreaRow= new QiRepairAreaRow();
		QiRepairAreaRowId id = new QiRepairAreaRowId();
		id.setRepairAreaName(getDialog().getRepairArea().getRepairAreaName());
		id.setRepairAreaRow(Integer.parseInt(rowNoTextField.getControl().getText()));
		qiRepairAreaRow.setId(id);
		if(spaceFillSeqCombobox.getControl().getValue().toString().equalsIgnoreCase("A - Ascending")){
			qiRepairAreaRow.setSpaceFillSequence('A');
		}else if(spaceFillSeqCombobox.getControl().getValue().toString().equalsIgnoreCase("D - Descending")){
			qiRepairAreaRow.setSpaceFillSequence('D');
		}
		qiRepairAreaRow.setCreateUser(getUserId());
		try {
			  if(((ParkingLocationMaintenanceModel)getModel()).isRepairAreaRowExists(id)){
				  String errorAndLoggerMessage="Repair Area Row already exists!";
				  displayErrorMessage(errorAndLoggerMessage,errorAndLoggerMessage);
			  }else{
				  ((ParkingLocationMaintenanceModel)getModel()).createRepairAreaRow(qiRepairAreaRow);
				  Stage stage= (Stage) createBtn.getScene().getWindow();
				  stage.close();
			  }
		} catch (Exception e) {
			handleException("An error occurred at createRepairAreaRow method ", "Unable to create RepairAreaRow", e);
		}
	}
	/**
	 * This method is used to update RepairAreaRow
	 * @param actionEvent
	 */
	private void updateRepairAreaRow(){
		qiRepairAreaRowCloned =(QiRepairAreaRow) qiRepairAreaRow.deepCopy();
		List<Object> checkMandatoryFieldList=new ArrayList<Object>();
		checkMandatoryFieldList.add(getDialog().getRowNoTextField().getControl());
		checkMandatoryFieldList.add(getDialog().getSpaceFillSeqCombobox().getControl());
		checkMandatoryFieldList.add(getDialog().getReasonForChangeTextArea());
		String errorMessage=QiCommonUtil.checkMandatoryFields(checkMandatoryFieldList);
		if(!errorMessage.equalsIgnoreCase("false")){
			displayErrorMessage("Mandatory field is empty", errorMessage);
			return;
		}	
		if(getDialog().getSpaceFillSeqCombobox().getControl().getValue().toString().equalsIgnoreCase("A - Ascending")){
			qiRepairAreaRow.setSpaceFillSequence('A');
		}else if(getDialog().getSpaceFillSeqCombobox().getControl().getValue().toString().equalsIgnoreCase("D - Descending")){
			qiRepairAreaRow.setSpaceFillSequence('D');
		}
		LoggedButton updateBtn = getDialog().getUpdateButton();
		qiRepairAreaRow.setUpdateUser(getUserId());
		if (isUpdated(qiRepairAreaRow)) {
			return;
		}
		getModel().updateRepairAreaRow(qiRepairAreaRow);
		//-----Start Audit log
		//call Auditlog utility to capture old and new data	

		AuditLoggerUtil.logAuditInfo(qiRepairAreaRowCloned, qiRepairAreaRow,  getDialog().getReasonForChangeTextArea().getText(), getDialog().getScreenName(),getUserId());
		//-----End of Audit Log	
		Stage stage= (Stage) updateBtn.getScene().getWindow();
		stage.close();
	}
	/**
	 * This method is used to cancel RepairAreaRow
	 * @param actionEvent
	 */
	private void cancelRepairAreaRow(){
		LoggedButton cancelBtn = getDialog().getCancelBtn();
		try {
			Stage stage = (Stage) cancelBtn.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occured during cancel action ", "Failed to perform cancel action",  e);
		}
	}
	
}
