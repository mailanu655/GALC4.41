package com.honda.galc.client.teamleader.qi.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.model.RepairMethodMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.RepairMethodDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.qi.QiRepairMethod;
import com.honda.galc.util.AuditLoggerUtil;

import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class RepairMethodDialogController extends QiDialogController<RepairMethodMaintenanceModel, RepairMethodDialog> {
	private QiRepairMethod repairMethod;	
	private QiRepairMethod repairMethodCloned;

	LoggedButton updateBtn;
	@Override
	public void handle(ActionEvent actionEvent) {
		clearDisplayMessage();
		if(actionEvent.getSource().equals(getDialog().getCreateBtn())) createBtnAction(actionEvent);
		else if(actionEvent.getSource().equals(getDialog().getUpdateButton())) updateBtnAction(actionEvent);
		else if(actionEvent.getSource().equals(getDialog().getCancelBtn())) cancelBtnAction(actionEvent);
	}

	private void createBtnAction(ActionEvent event) {
		LoggedButton createBtn = getDialog().getCreateBtn();
		String repairMethodName = StringUtils.trim(QiCommonUtil.delMultipleSpaces(getDialog().getRepairMethodNameTextField()));
		String repairMethodDesc= StringUtils.trim(QiCommonUtil.delMultipleSpaces(getDialog().getRepairMethodDescriptionTextArea()).toUpperCase());
		if(QiCommonUtil.isMandatoryFieldEmpty(getDialog().getRepairMethodNameTextField())){
			displayErrorMessage("Mandatory field is empty", "Please enter Repair Method Name");
			return;
		}
		repairMethod = new QiRepairMethod();
		try{  
			repairMethod.setCreateUser(getUserId());
			repairMethod.setRepairMethod(repairMethodName);			
			repairMethod.setRepairMethodDescription(repairMethodDesc);
			repairMethod.setActive(getDialog().getActiveRadioBtn().isSelected());
			repairMethod.setAppCreateTimestamp(new Date());
			if(((RepairMethodMaintenanceModel)getModel()).isRepairMethodExists(repairMethodName))
				displayErrorMessage("Already exists! Enter a different Repair Method","Already exists! Enter a different Repair Method");
			else{
				((RepairMethodMaintenanceModel)getModel()).createRepairMethod(repairMethod);
				Stage stage = (Stage) createBtn.getScene().getWindow();
				stage.close();
			}
		}catch (Exception e) {
			handleException("An error occured in create Repair method Failed to Create Repair Method Name ", StringUtils.EMPTY, e);
		}
	}

	private void setRepairMethodValue(String action, String oldRepairMethodName){
		repairMethodCloned =(QiRepairMethod) repairMethod.deepCopy();
		String repairMethodName = StringUtils.trim(QiCommonUtil.delMultipleSpaces(getDialog().getRepairMethodNameTextField()));
		String repairMethodDesc= StringUtils.trim(QiCommonUtil.delMultipleSpaces(getDialog().getRepairMethodDescriptionTextArea()).toUpperCase());
		repairMethod.setRepairMethod(repairMethodName);
		repairMethod.setRepairMethodDescription(repairMethodDesc);
		repairMethod.setActive(getDialog().getActiveRadioBtn().isSelected());
		repairMethod.setAppUpdateTimestamp(new Date());
		if(action.isEmpty()){
			repairMethod.setUpdateUser(getUserId());			
			((RepairMethodMaintenanceModel)getModel()).updateRepairMethod(repairMethod,oldRepairMethodName);	
			//-----Start Audit log
			//call Auditlog utility to capture old and new data	

			AuditLoggerUtil.logAuditInfo(repairMethodCloned, repairMethod,  getDialog().getReasonForChangeTextArea().getText(), getDialog().getScreenName(),getUserId());
			//-----End of Audit Log	
			Stage stage= (Stage) updateBtn.getScene().getWindow();
			stage.close();
		}
	}	

	private void updateBtnAction(ActionEvent event)
	{
		clearDisplayMessage();
		updateBtn = getDialog().getUpdateButton();

		/** Mandatory Check for Repair Method name */
		if(QiCommonUtil.isMandatoryFieldEmpty(getDialog().getRepairMethodNameTextField())){
			displayErrorMessage("Mandatory field is empty", "Please enter Repair Method Name");
			return;
		}
		/** Mandatory Check for Reason for Change */
		if(QiCommonUtil.isMandatoryFieldEmpty(getDialog().getReasonForChangeTextArea())){
			displayErrorMessage("Mandatory field is empty", "Please enter Reason for Change");
			return;
		}
		try{
				updateAssociatedRepairMethod();
		}
		catch (Exception e) {
			handleException("An error occured in update repair method " , "Failed to Update Repair Method", e);
		}

	}

	private void cancelBtnAction(ActionEvent event) {
		LoggedButton cancelBtn = getDialog().getCancelBtn();
		try {
			Stage stage = (Stage) cancelBtn.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occured in during cancel action Failed to perform cancel action", StringUtils.EMPTY, e);
		}
	}

	private void validationForTextfield(){
		getDialog().getRepairMethodNameTextField().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(64));
		getDialog().getRepairMethodDescriptionTextArea().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(256));
		getDialog().getReasonForChangeTextArea().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(256));
	}

	@Override
	public void initListeners() {
		validationForTextfield();
		addFieldListener(getDialog().getRepairMethodNameTextField(),true);
		addTextAreaListener(getDialog().getRepairMethodDescriptionTextArea());
		setTextFieldListener(getDialog().getRepairMethodNameTextField());
		
		getDialog().getActiveRadioBtn().getToggleGroup().selectedToggleProperty().addListener(updateEnablerForToggle);
	}

	private void updateAssociatedRepairMethod(){
		String oldRepairMethodName = repairMethod.getRepairMethod();		
		String repairMethodName = StringUtils.trim(getDialog().getRepairMethodNameTextField().getText());		
		boolean isActive = getDialog().getActiveRadioBtn().isSelected();	
		boolean wasActive=repairMethod.getActive()==1?true:false;
		boolean updateAssociatedEntities = false;
		try{
			if(!isActive && wasActive){
				List<String> repairMethodList=new ArrayList<String>();
				repairMethodList.add(oldRepairMethodName);
				String returnValue=isLocalSiteImpacted(repairMethodList,getDialog().getScreenName(),getDialog());
				if(returnValue.equals(QiConstant.NO_LOCAL_SITES_CONFIGURED)){
					return;
				}
				else if(returnValue.equals(QiConstant.LOCAL_SITES_IMPACTED)){
					displayErrorMessage("Inactivation not allowed as Local Site(s) Impacted","Inactivation not allowed as Local Site(s) Impacted");
					return;
				}
			}
			else if(!isActive){
				if(getModel().isRepairMethodExists(repairMethodName) && !(oldRepairMethodName.equalsIgnoreCase(repairMethodName))){
					displayErrorMessage("Already exists! Enter a different Repair Method","Already exists! Enter a different Repair Method");
					return;
				}
			}
			else if(!(oldRepairMethodName.equalsIgnoreCase(repairMethodName)) && isActive){
				if( getModel().isRepairMethodExists(repairMethodName) && !(oldRepairMethodName.equalsIgnoreCase(repairMethodName))){
					displayErrorMessage("Already exists! Enter a different Repair Method","Already exists! Enter a different Repair Method");
					return;
				}else{		
					updateAssociatedEntities = true;	
				}
			}
			if (isUpdated(getQiRepairMethod())) {
				return;
			}
			if (updateAssociatedEntities) {
				String updateUser = getUserId();
				((RepairMethodMaintenanceModel)getModel()).updateAllRepairMethodsByRepairMethod(repairMethodName,oldRepairMethodName,updateUser);
				((RepairMethodMaintenanceModel)getModel()).updateAllLocalDefectCombinationsByRepairMethod(repairMethodName,oldRepairMethodName,updateUser);
			}
			setRepairMethodValue(StringUtils.EMPTY,oldRepairMethodName);
		}catch(Exception e){
			handleException("Error in updateAssociatedDefect method", "Falied to update Defect", e);
		}

	}

	public RepairMethodDialogController(RepairMethodMaintenanceModel model, RepairMethodDialog dialog,QiRepairMethod repairMethod) {
		super();
		setModel(model);
		setDialog(dialog);
		this.repairMethod=repairMethod;
	}

	public QiRepairMethod getQiRepairMethod() {
		return repairMethod;
	}

	public void setQiRepairMethod(QiRepairMethod repairMethod) {
		this.repairMethod = repairMethod;
	}

}
