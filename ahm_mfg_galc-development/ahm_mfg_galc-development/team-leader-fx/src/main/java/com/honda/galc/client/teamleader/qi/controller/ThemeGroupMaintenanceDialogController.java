package com.honda.galc.client.teamleader.qi.controller;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.model.ThemeMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.ThemeGroupMaintenanceDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.entity.qi.QiThemeGroup;
import com.honda.galc.util.AuditLoggerUtil;

public class ThemeGroupMaintenanceDialogController extends QiDialogController<ThemeMaintenanceModel,ThemeGroupMaintenanceDialog> {

	private QiThemeGroup qiThemeGroup;
	private QiThemeGroup selectedThemeGroupCloned;
	
	public ThemeGroupMaintenanceDialogController(ThemeMaintenanceModel model,ThemeGroupMaintenanceDialog themeGroupDialog,QiThemeGroup qiThemeGroup) {
		super();
		setModel(model);
		setDialog(themeGroupDialog);
		this.qiThemeGroup = qiThemeGroup;
		this.selectedThemeGroupCloned =(QiThemeGroup) qiThemeGroup.deepCopy();
	}
	
	/**
	 * This method is to map the action event to loggedbutton
	 */
	@Override
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource() instanceof LoggedButton) {
			if(actionEvent.getSource().equals(getDialog().getCreateBtn())) createBtnAction(actionEvent);
			else if(actionEvent.getSource().equals(getDialog().getUpdateButton())) updateBtnAction(actionEvent);
			else if(actionEvent.getSource().equals(getDialog().getCancelBtn())) cancelBtnAction(actionEvent);
		} 	
		
	}
	
	/**
	 * This method is used to create theme group
	 * @param event
	 */
	private void createBtnAction(ActionEvent actionEvent) {
		String themeGroupName = StringUtils.trim(getDialog().getThemeGroupNameTextField().getText());
		/** Mandatory Check for Theme name */
		if(isValidateThemeGroupName()){
			displayErrorMessage("Mandatory field is empty", "Please enter Theme Group Name");
			return;
		}
		try{  
			qiThemeGroup = new QiThemeGroup();
			qiThemeGroup.setCreateUser(getUserId());
			setThemeGroupValue(qiThemeGroup);
			if(getModel().isThemeGroupExists(themeGroupName))
				displayErrorMessage("Failed to add new theme group as  "+themeGroupName+" already exists!","Theme group already exists!");
			else{
				getModel().createThemeGroup(qiThemeGroup);
				Stage stage = (Stage) getDialog().getCreateBtn().getScene().getWindow();
				stage.close();
			}
		}catch (Exception e) {
			handleException("An error occured in createThemeGroup method " , "Failed to Create Theme Group ", e);
		}
	}
	
	/**
	 * This method is used to update theme group
	 * @param event
	 */
	private void updateBtnAction(ActionEvent actionEvent) {
		String oldThemeGroupName = qiThemeGroup.getThemeGroupName();
		String themeGroupName = StringUtils.trim(getDialog().getThemeGroupNameTextField().getText());
		String themeGroupDesc = StringUtils.trim(getDialog().getThemeGroupDescriptionTextArea().getText());
		String themeGroupStatus = StringUtils.trim(getDialog().getActiveRadioBtn().getText());
		List<QiThemeGroup> qiThemeGroupList = new ArrayList<QiThemeGroup>();
		qiThemeGroupList.add(qiThemeGroup);
	
		boolean confirm=true;
		if((!getDialog().getActiveRadioBtn().isSelected() && qiThemeGroup.getActiveValue() == 1) && getDialog().getThemeGroupingTablePane().getRowCount()>0){
			confirm=MessageDialog.confirm(getDialog(), "Inactivating Theme Group will delete any existing association(s) with Theme. Do you wish to continue?");
		}
		if (!getDialog().getActiveRadioBtn().isSelected() && qiThemeGroup.getActiveValue() == 1 && !confirm){
			return ;
		}
		
		/** Mandatory Check for Theme name */
		if(isValidateThemeGroupName()){
			displayErrorMessage("Mandatory field is empty", "Please enter Theme Group Name");
			return;
		}
		/** Mandatory Check for Reason for Change */
		if(isValidateReason()){
			displayErrorMessage("Mandatory field is empty", "Please enter Reason for Change");
			return;
		}
		try{
			if (isUpdated(selectedThemeGroupCloned)) {
				return;
			}			
			if ((oldThemeGroupName.equals(themeGroupName))
					&& ((!qiThemeGroup.getThemeGroupDescription().equals(
							themeGroupDesc)) || !qiThemeGroup.getStatus()
							.equals(themeGroupStatus))) {
				updateThemeGroup(oldThemeGroupName);
			} else if(getModel().isThemeGroupExists(themeGroupName)) {
				displayErrorMessage("Theme Group name already exists.",
						"Theme Group name already exists.");
				return;
			} else {
				updateThemeGroup(oldThemeGroupName);
			}
			if(!oldThemeGroupName.equals(themeGroupName)) {
				updateThemeGroupAssociation(themeGroupName, oldThemeGroupName);
			}
			if(!getDialog().getActiveRadioBtn().isSelected())
				getModel().deleteGroupAssociationByThemeGroups(qiThemeGroupList);
			//call to prepare and insert audit data
			AuditLoggerUtil.logAuditInfo(selectedThemeGroupCloned, qiThemeGroup, getDialog().getReasonForChangeTextArea().getText(), getDialog().getScreenName(),getUserId());					
			
		}
		catch (Exception e) {
			handleException("An error occured in updateTheme method " , "Failed to Update Theme ", e);
		}
	}
	
	private void updateThemeGroupAssociation(String newThemeGroupName, String oldThemeGroupName) {
		getModel().updateThemeGroupAssociation(newThemeGroupName, oldThemeGroupName);
	}
	
	/**
	 * Update theme group.
	 *
	 * @param oldThemeGroupName the old theme group name
	 * @throws Exception the exception
	 */
	private void updateThemeGroup(String oldThemeGroupName) throws Exception{
		qiThemeGroup.setUpdateUser(getUserId());
		setThemeGroupValue(qiThemeGroup);
		Stage stage= (Stage) getDialog().getUpdateButton().getScene().getWindow();
		getModel().updateThemeGroup(qiThemeGroup, oldThemeGroupName);	
		
		stage.close();
	}
	
	/**
	 * Checks if is validate theme group.
	 *
	 * @return true, if is validate theme group
	 */
	private boolean isValidateThemeGroupName() {
		return QiCommonUtil.isMandatoryFieldEmpty(getDialog().getThemeGroupNameTextField());
	}
	
	/**
	 * Checks if is validate reason.
	 *
	 * @return true, if is validate reason
	 */
	private boolean isValidateReason() {
		return QiCommonUtil.isMandatoryFieldEmpty(getDialog().getReasonForChangeTextArea());
	}
	
	/**
	 * This method is used to set theme group Value inside entity to create/update
	 * @param action
	 * @param oldThemeGroupName
	 */
	private void setThemeGroupValue(QiThemeGroup themeGroup){
		qiThemeGroup.setThemeGroupName(QiCommonUtil.delMultipleSpaces(getDialog().getThemeGroupNameTextField()).trim());
		qiThemeGroup.setThemeGroupDescription(StringUtils.trim(getDialog().getThemeGroupDescriptionTextArea().getText()));
		qiThemeGroup.setActive(getDialog().getActiveRadioBtn().isSelected());
	}
	
	/**
	 * This method is used to cancel pop up
	 * @param event
	 */
	private void cancelBtnAction(ActionEvent actionEvent) {
		try {
			Stage stage = (Stage) getDialog().getCancelBtn().getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occured in during cancel action ", "Failed to perform cancel action",e);
		}
	}
	
	@Override
	public void initListeners() {
		validationForTextfield();
		addFieldListener(getDialog().getThemeGroupNameTextField(),true);
		setTextFieldListener(getDialog().getThemeGroupNameTextField());
		addFieldListener(getDialog().getThemeGroupDescriptionTextArea(),true);
		setTextFieldListener(getDialog().getThemeGroupDescriptionTextArea());
		
		getDialog().getActiveRadioBtn().getToggleGroup().selectedToggleProperty().addListener(updateEnablerForToggle);
		
	}
	
	/**
	 * This method is used for validation
	 */
	private void validationForTextfield(){
		getDialog().getThemeGroupNameTextField().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(32));
		getDialog().getThemeGroupDescriptionTextArea().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(128));
		getDialog().getReasonForChangeTextArea().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(256));
	}
}
