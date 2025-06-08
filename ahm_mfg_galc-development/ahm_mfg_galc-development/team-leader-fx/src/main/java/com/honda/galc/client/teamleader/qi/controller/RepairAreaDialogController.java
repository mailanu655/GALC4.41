package com.honda.galc.client.teamleader.qi.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.model.ParkingLocationMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.RepairAreaDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.entity.qi.QiRepairArea;
import com.honda.galc.util.AuditLoggerUtil;

import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


/**
 * 
 * <h3>RepairAreaDialogController Class description</h3>
 * <p>
 * RepairAreaDialogController is used to create ,update ,cancel RepairArea.
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
 * 
 * 
 * </Table>
 * @author LnTInfotech<br>
 * 
 */
public class RepairAreaDialogController extends QiDialogController<ParkingLocationMaintenanceModel, RepairAreaDialog>{

	private QiRepairArea repairArea;
	private QiRepairArea repairAreaCloned;
	private String plantName;
	private LoggedButton updateBtn;


	public RepairAreaDialogController(ParkingLocationMaintenanceModel model, RepairAreaDialog dialog,QiRepairArea repairArea,String plantName) {
		super();
		setModel(model);
		setDialog(dialog);
		this.repairArea=repairArea;
		this.plantName=plantName;
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
		String repairAreaName = StringUtils.trim(QiCommonUtil.delMultipleSpaces(getDialog().getRepairAreaNameTextField().getControl()));
		String repairAreaDesc= StringUtils.trim(QiCommonUtil.delMultipleSpaces(getDialog().getRepairAreaDescTextField().getControl()).toUpperCase());
		String priority= StringUtils.trim(QiCommonUtil.delMultipleSpaces(getDialog().getPriorityTextField().getControl()));
		List<Object> checkMandatoryFieldList=new ArrayList<Object>();
		checkMandatoryFieldList.add(getDialog().getRepairAreaNameTextField().getControl());
		checkMandatoryFieldList.add(getDialog().getRepairAreaDescTextField().getControl());
		checkMandatoryFieldList.add(getDialog().getLocationCombobox());
		checkMandatoryFieldList.add(getDialog().getPriorityTextField().getControl());
		checkMandatoryFieldList.add(getDialog().getRowFillSequenceCombobox());
		String errorMessage=QiCommonUtil.checkMandatoryFields(checkMandatoryFieldList);
		if(!errorMessage.equalsIgnoreCase("false")){
			displayErrorMessage("Mandatory field is empty", errorMessage);
			return;
		}
		repairArea = new QiRepairArea();
		try{  
			if (getDialog().getLocationCombobox().getValue().equals("T - InTransit")) {
				if (getModel().isIntransitRepairAreaExist(getModel().getSiteName(),plantName)) {
					displayErrorMessage("In transit repair area already exist.",
							"In transit repair area already exist.");
					return;
				}
			}
				repairArea.setCreateUser(getUserId());
				repairArea.setSiteName(getModel().getSiteName());
				repairArea.setPlantName(plantName);
				repairArea.setRepairAreaName(repairAreaName);
				repairArea.setRepairAreaDescription(repairAreaDesc);
				repairArea.setLocation(
						getDialog().getLocationCombobox().getSelectionModel().getSelectedItem().toString().charAt(0));
				repairArea.setPriority(Short.parseShort(priority));
				repairArea.setRowFillSeq(getDialog().getRowFillSequenceCombobox().getSelectionModel().getSelectedItem()
						.toString().charAt(0));
				repairArea.setDivName(StringUtils.trimToEmpty(getDialog().getDivCombobox().getSelectionModel().getSelectedItem()));
				if (((ParkingLocationMaintenanceModel) getModel()).isRepairAreaExists(repairAreaName))
					displayErrorMessage("Already exists",
							"Already exists.");
				else {
					((ParkingLocationMaintenanceModel) getModel()).createRepairArea(repairArea);
					Stage stage = (Stage) createBtn.getScene().getWindow();
					stage.close();
				} 
		}catch (Exception e) {
			handleException("Failed to Create Repair Area Name ", StringUtils.EMPTY, e);
		}
	}
	

	private void updateBtnAction(ActionEvent event)
	{
		clearDisplayMessage();
		updateBtn = getDialog().getUpdateButton();
		boolean updateRepairAreaName;
		String oldRepairAreaName = repairArea.getRepairAreaName();		
		String repairAreaName = StringUtils.trim(getDialog().getRepairAreaNameTextField().getControl().getText());
		List<Object> checkMandatoryFieldList=new ArrayList<Object>();
		checkMandatoryFieldList.add(getDialog().getRepairAreaNameTextField().getControl());
		checkMandatoryFieldList.add(getDialog().getRepairAreaDescTextField().getControl());
		checkMandatoryFieldList.add(getDialog().getLocationCombobox());
		checkMandatoryFieldList.add(getDialog().getPriorityTextField().getControl());
		checkMandatoryFieldList.add(getDialog().getRowFillSequenceCombobox());
		checkMandatoryFieldList.add(getDialog().getReasonForChangeTextArea());
		String errorMessage=QiCommonUtil.checkMandatoryFields(checkMandatoryFieldList);
		if(!errorMessage.equalsIgnoreCase("false")){
			displayErrorMessage("Mandatory field is empty", errorMessage);
			return;
		}
		try{
			if (getDialog().getLocationCombobox().getValue().equals("T - InTransit")) {
				if (getModel().isIntransitRepairAreaExist(getModel().getSiteName(),plantName)) {
					displayErrorMessage("In transit repair area already exist.",
							"In transit repair area already exist.");
					return;
				}
			}
			boolean isRepairAreaUsedInLocalDefectCombination = getModel().isRepairAreaUsedInLocalDefectCombination(oldRepairAreaName);
			boolean isRepairAreaUsedInRepairAreaRow = getModel().isRepairAreaUsedInRepairAreaRow(oldRepairAreaName);
			boolean isRepairAreaUsedInRepairAreaSpace = getModel().isRepairAreaUsedInRepairAreaSpace(oldRepairAreaName);
			boolean isRepairAreaUsedInDefectResult = getModel().isRepairAreaUsedInDefectResult(oldRepairAreaName);
			if(isRepairAreaUsedInLocalDefectCombination || isRepairAreaUsedInRepairAreaRow || isRepairAreaUsedInRepairAreaSpace || isRepairAreaUsedInDefectResult){
				updateAssociatedRepairArea();
			}else{
				if(!(oldRepairAreaName.equalsIgnoreCase(repairAreaName))){
					updateRepairAreaName = MessageDialog.confirm(null, "Are you sure you want to update Repair Area Name?");
					if(!updateRepairAreaName)
						return;
					else if(updateRepairAreaName && getModel().isRepairAreaExists(repairAreaName)){
						displayErrorMessage("Already exists! Enter a different Repair Area","Already exists! Enter a different Repair Area ");
						return;
					}
				}
				if (isUpdated(repairArea)) {
					return;
				}
				setRepairAreaValue(oldRepairAreaName);	
			}
		}
		catch (Exception e) {
			handleException("An error occured in update repair area " , "Failed to Update Repair Area", e);
		}

	}

	private void updateAssociatedRepairArea(){
		String oldRepairAreaName = repairArea.getRepairAreaName();		
		String repairAreaName = StringUtils.trim(QiCommonUtil.delMultipleSpaces(getDialog().getRepairAreaNameTextField().getControl()));
		boolean updateRepairAreaName;
		boolean updateAssociatedEntities = false;
		try{
			if(!(oldRepairAreaName.equalsIgnoreCase(repairAreaName))){
				updateRepairAreaName = MessageDialog.confirm(null, "Are you sure you want to update Repair Area Name?");
				if(!updateRepairAreaName)
					return;
				else if(updateRepairAreaName && getModel().isRepairAreaExists(repairAreaName)){
					displayErrorMessage("Already exists! Enter a different Repair Area","Already exists! Enter a different Repair Area ");
					return;
				}else{
					updateAssociatedEntities = true;
				}
			}
			if (isUpdated(repairArea)) {
				return;
			}
			if (updateAssociatedEntities) {
				getModel().updateAllByRepairAreaName(repairAreaName,oldRepairAreaName,getUserId());
			}
			setRepairAreaValue(oldRepairAreaName);			
		}catch(Exception e){
			handleException("Error in updateAssociatedRepairArea() method", "Falied to update Repair Area", e);
		}
	}

	private void setRepairAreaValue(String oldRepairAreaName){
		repairAreaCloned =(QiRepairArea) repairArea.deepCopy();
		String repairAreaName = StringUtils.trim(QiCommonUtil.delMultipleSpaces(getDialog().getRepairAreaNameTextField().getControl()));
		String repairAreaDesc= StringUtils.trim(QiCommonUtil.delMultipleSpaces(getDialog().getRepairAreaDescTextField().getControl()).toUpperCase());
		String priority= StringUtils.trim(QiCommonUtil.delMultipleSpaces(getDialog().getPriorityTextField().getControl()));
		repairArea.setSiteName(getModel().getSiteName());
		repairArea.setPlantName(plantName);
		repairArea.setRepairAreaName(repairAreaName);
		repairArea.setRepairAreaDescription(repairAreaDesc);
		repairArea.setLocation(getDialog().getLocationCombobox().getSelectionModel().getSelectedItem().toString().charAt(0));			
		repairArea.setPriority(Short.parseShort(priority));
		repairArea.setRowFillSeq(getDialog().getRowFillSequenceCombobox().getSelectionModel().getSelectedItem().toString().charAt(0));
		repairArea.setDivName(getDialog().getDivCombobox().getSelectionModel().getSelectedItem().toString());
		repairArea.setUpdateUser(getUserId());			
		((ParkingLocationMaintenanceModel)getModel()).updateRepairArea(repairArea,oldRepairAreaName);	
		//-----Start Audit log
		//call Auditlog utility to capture old and new data	

		AuditLoggerUtil.logAuditInfo(repairAreaCloned, repairArea,  getDialog().getReasonForChangeTextArea().getText(), getDialog().getScreenName(),getUserId());
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
			handleException("An error occured during cancel action Failed to perform cancel action", StringUtils.EMPTY, e);
		}
	}

	private void validationForTextfield(){
		getDialog().getRepairAreaNameTextField().getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(20));
		getDialog().getRepairAreaDescTextField().getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(30));
		getDialog().getPriorityTextField().getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(5));
	}

	@Override
	public void initListeners() {
		validationForTextfield();
		addFieldListener(getDialog().getRepairAreaNameTextField().getControl(),true);
		addFieldListener(getDialog().getRepairAreaDescTextField().getControl(),true);
		setTextFieldListener(getDialog().getRepairAreaNameTextField().getControl());
		setTextFieldListener(getDialog().getRepairAreaDescTextField().getControl());
		addFieldListener(getDialog().getPriorityTextField().getControl(),false);
		
		getDialog().getLocationCombobox().valueProperty().addListener(updateEnablerForStringValueChange);
		getDialog().getRowFillSequenceCombobox().valueProperty().addListener(updateEnablerForStringValueChange);
		getDialog().getDivCombobox().valueProperty().addListener(updateEnablerForStringValueChange);
	}	
}
