package com.honda.galc.client.teamleader.qi.controller;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.model.ThemeMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.ThemeMaintenanceDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.entity.qi.QiPartDefectCombination;
import com.honda.galc.entity.qi.QiTheme;
import com.honda.galc.util.AuditLoggerUtil;

public class ThemeMaintenanceDialogController extends QiDialogController<ThemeMaintenanceModel,ThemeMaintenanceDialog> {
	private QiTheme qiTheme;
	private QiTheme selectedThemeCloned;
	
	public ThemeMaintenanceDialogController(ThemeMaintenanceModel model,ThemeMaintenanceDialog themeDialog,QiTheme qiTheme) {
		super();
		setModel(model);
		setDialog(themeDialog);
		this.qiTheme = qiTheme;
		this.selectedThemeCloned =(QiTheme) qiTheme.deepCopy();
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
	 * This method is used to create theme
	 * @param event
	 */
	private void createBtnAction(ActionEvent event) {
		LoggedButton createBtn = getDialog().getCreateBtn();
		String themeName = StringUtils.trim(getDialog().getThemeNameTextField().getText());
		
		/** Mandatory Check for Theme name */
		if(isValidateThemeName()){
			displayErrorMessage("Mandatory field is empty", "Please enter Theme Name");
			return;
		}
		try{
			qiTheme = new QiTheme();
			qiTheme.setCreateUser(getUserId());
			setThemeValue(qiTheme);
			if(getModel().isThemeExists(themeName))
				displayErrorMessage("Failed to add new theme as "+themeName+" already exists!","Theme already exists!");
			else{
				getModel().createTheme(qiTheme);
				Stage stage = (Stage) createBtn.getScene().getWindow();
				stage.close();
			}
		}catch (Exception e) {
			handleException("An error occured in createTheme method " , "Failed to Create Theme ", e);
		}

	}

	/**
	 * This method is used to update theme
	 * @param event
	 */
	private void updateBtnAction(ActionEvent event)	{
		String oldThemeName = qiTheme.getThemeName();
		String themeName = StringUtils.trim(getDialog().getThemeNameTextField().getText());
		String themeDesc = StringUtils.trim(getDialog().getThemeDescriptionTextArea().getText());
		String themeStatus = StringUtils.trim(getDialog().getActiveRadioBtn().getText());
		List<QiTheme> themeToDelete = new ArrayList<QiTheme>();
		themeToDelete.add(qiTheme);
		List<QiPartDefectCombination> pdcList = getModel().findAllByThemeName(qiTheme.getThemeName());
		
		boolean confirm=true;
		if((!getDialog().getActiveRadioBtn().isSelected() && qiTheme.getActiveValue() == 1) && pdcList.size()>0){
			MessageDialog.showError(getDialog(),"Inactivating Theme is not allowed as it is assigned to " + pdcList.size() + " Part Defect Combination(s) in regional attribute.");
			return;
		}
		else if((!getDialog().getActiveRadioBtn().isSelected() && qiTheme.getActiveValue() == 1) && getDialog().getThemeGroupingTablePane().getRowCount()>0){
			confirm=MessageDialog.confirm(getDialog(), "Inactivating Theme will delete its association(s) with Theme Group.  Do you wish to continue?");
		}
		
		if (!getDialog().getActiveRadioBtn().isSelected() && qiTheme.getActiveValue() == 1 && !confirm){
			return ;
		}
		
		/** Mandatory Check for Theme name */
		if(isValidateThemeName()){
			displayErrorMessage("Mandatory field is empty", "Please enter Theme Name");
			return;
		}
		/** Mandatory Check for Reason for Change */
		if(isValidateReason()){
			displayErrorMessage("Mandatory field is empty", "Please enter Reason for Change");
			return;
		}
		try{
			if (isUpdated(selectedThemeCloned)) {
				return;
			}
			if((oldThemeName.equals(themeName)) && ((!qiTheme.getThemeDescription().equals(themeDesc)) || 
					!qiTheme.getStatus().equals(themeStatus))){
				updateTheme(oldThemeName);
			} else if(getModel().isThemeExists(themeName)) {
				displayErrorMessage("Failed to modify theme as " + themeName
						+ " already exists!", "Theme already exists!");
				return;
			} else {
				updateTheme(oldThemeName);
			}
			if(!oldThemeName.equals(themeName)) {
				updateThemeAssociation(themeName, oldThemeName);
			}
			if(!getDialog().getActiveRadioBtn().isSelected())
				getModel().deleteGroupAssociationByThemes(themeToDelete);
			if(pdcList.size() > 0)
				updatePartDefectCombination(pdcList, themeName);
			//call to prepare and insert audit data
			AuditLoggerUtil.logAuditInfo(selectedThemeCloned, qiTheme, getDialog().getReasonForChangeTextArea().getText(), getDialog().getScreenName(),getUserId());
		}
		catch (Exception e) {
			handleException("An error occured in updateTheme method " , "Failed to Update Theme ", e);
		}

	}
	
	/**
	 * Update part defect combination.
	 *
	 * @param themeList the theme list
	 */
	public void updatePartDefectCombination(List<QiPartDefectCombination> pdcList, String themeName) {
		List<QiPartDefectCombination> pdcListToUpdate = new ArrayList<QiPartDefectCombination>();
		
		for (QiPartDefectCombination qiPartDefectCombination : pdcList) {
			if(getDialog().getActiveRadioBtn().isSelected())
				qiPartDefectCombination.setThemeName(themeName);
			else
				qiPartDefectCombination.setThemeName(null);
			pdcListToUpdate.add(qiPartDefectCombination);
		}
		if(pdcListToUpdate.size() > 0){
			getModel().updatePartDefectCombination(pdcListToUpdate);
		}
	}

	/**
	 * Update theme association.
	 *
	 * @param newThemeName the new theme name
	 * @param oldThemeName the old theme name
	 */
	private void updateThemeAssociation(String newThemeName, String oldThemeName) {
		getModel().updateThemeAssociation(newThemeName, oldThemeName);
	}

	/**
	 * Update theme.
	 *
	 * @param oldThemeName the old theme name
	 * @throws Exception the exception
	 */
	private void updateTheme(String oldThemeName) throws Exception{
		qiTheme.setUpdateUser(getUserId());
		setThemeValue(qiTheme);
		Stage stage= (Stage) getDialog().getUpdateButton().getScene().getWindow();
		getModel().updateTheme(qiTheme, oldThemeName);	
		stage.close();
	}

	/**
	 * Checks if is validate theme name.
	 *
	 * @return true, if is validate theme name
	 */
	private boolean isValidateThemeName() {
		return QiCommonUtil.isMandatoryFieldEmpty(getDialog().getThemeNameTextField());
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
	 * This method is used to cancel pop up
	 * @param event
	 */
	private void cancelBtnAction(ActionEvent event) {
		LoggedButton cancelBtn = getDialog().getCancelBtn();
		try {
			Stage stage = (Stage) cancelBtn.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occured in during cancel action ", "Failed to perform cancel action", e);
		}
	}


	/**
	 *  This method is used to set theme Value inside entity to create/update
	 * @param action
	 * @param oldThemeName
	 */
	private void setThemeValue(QiTheme qiTheme){
		qiTheme.setThemeName(QiCommonUtil.delMultipleSpaces(getDialog().getThemeNameTextField()).trim());
		qiTheme.setThemeDescription(StringUtils.trim(getDialog().getThemeDescriptionTextArea().getText()));
		qiTheme.setActive(getDialog().getActiveRadioBtn().isSelected());
	}

	@Override
	public void initListeners() {
		validationForTextfield();
		addFieldListener(getDialog().getThemeNameTextField(),true);
		setTextFieldListener(getDialog().getThemeNameTextField());
		addFieldListener(getDialog().getThemeDescriptionTextArea(),true);
		setTextFieldListener(getDialog().getThemeDescriptionTextArea());
		
		getDialog().getActiveRadioBtn().getToggleGroup().selectedToggleProperty().addListener(updateEnablerForToggle);
	}

	/**
	 * This method is used for validation
	 */
	private void validationForTextfield(){
		getDialog().getThemeNameTextField().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(32));
		getDialog().getThemeDescriptionTextArea().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(128));
		getDialog().getReasonForChangeTextArea().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(256));
	}
}
