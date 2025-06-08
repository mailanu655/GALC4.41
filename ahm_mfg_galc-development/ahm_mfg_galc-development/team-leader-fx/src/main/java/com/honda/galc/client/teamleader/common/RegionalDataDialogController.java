package com.honda.galc.client.teamleader.common;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import com.honda.galc.client.teamleader.qi.controller.QiDialogController;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.entity.conf.RegionalCode;
import com.honda.galc.entity.conf.RegionalCodeId;
import com.honda.galc.entity.enumtype.RegionalCodeName;
import com.honda.galc.util.AuditLoggerUtil;

public class RegionalDataDialogController extends QiDialogController<RegionalDataMaintenanceModel, RegionalDataDialog> {

	public RegionalDataDialogController(RegionalDataMaintenanceModel model, RegionalDataDialog dialog) {
		super();
		setModel(model);
		setDialog(dialog);
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		clearDisplayMessage();
		if (actionEvent.getSource().equals(getDialog().getCreateBtn())) {
			createBtnAction(actionEvent);
		} else if (actionEvent.getSource().equals(getDialog().getUpdateBtn())) {
			updateBtnAction(actionEvent);
		} else if (actionEvent.getSource().equals(getDialog().getCancelBtn())) {
			cancelBtnAction(actionEvent);
		}
	}

	private void cancelBtnAction(ActionEvent actionEvent) {
		LoggedButton cancelBtn = getDialog().getCancelBtn();
		try {
			Stage stage = (Stage) cancelBtn.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occured in during cancel action ", "Failed to perform cancel action", e);
		}

	}

	private void updateBtnAction(ActionEvent actionEvent) {
		if (QiCommonUtil.isMandatoryFieldEmpty(getDialog().getRegionalCodeNameField())) {
			displayErrorMessage("Mandatory field is empty", "Regional Code Name is required.");
			return;
		}
	
		if (QiCommonUtil.isMandatoryFieldEmpty(getDialog().getReasonForChangeTextArea())) {
			displayErrorMessage("Mandatory field is empty", "Reason for Change is required.");
			return;
		}		
		
		boolean isUpdateValue = getDialog().getRegionalCodeNameField().isDisabled();
		
		if (isUpdateValue && QiCommonUtil.isMandatoryFieldEmpty(getDialog().getValueField())) {
			displayErrorMessage("Mandatory field is empty", "Value is required.");
			return;
		}
		
		String regionalCodeName = getDialog().getRegionalCodeNameField().getText();
		String value = getDialog().getValueField().getText();
		String name = getDialog().getNameField().getText();
		String abbreviation = getDialog().getAbbreviationField().getText();
		String description = getDialog().getDescriptionField().getText();

		String updateUser = getModel().getApplicationContext().getUserId().toUpperCase();
		String reasonForChange = getDialog().getReasonForChangeTextArea().getText();
		
		if (isUpdateValue) { //update value
			RegionalCode oldRegionalCode = getModel().getSelectedRegionalValue();
			String oldRegionalCodeName = oldRegionalCode.getId().getRegionalCodeName();
			String oldValue = oldRegionalCode.getId().getRegionalValue();
			
			if (!oldValue.equalsIgnoreCase(value)) { //If the value is used, it can't be updated. Other fields can be updated.
				//check if the old value is used in regional process point group
				if (RegionalCodeName.CATEGORY_CODE.getName().equals(oldRegionalCodeName)) { 
					//check usage in REGIONA_PROCESS_POINT_GROUP_TBX 
					if (getModel().findCountByCategoryCode(new Short(oldValue).shortValue()) > 0) {
						displayErrorMessage("Value cannot be updated as it is used", "Value cannot be updated as it is used");
						return;
					}
				} else if (RegionalCodeName.PROCESS_AREA_GROUP_NAME.getName().equals(oldRegionalCodeName)) { 
					//check usage in REGIONA_PROCESS_POINT_GROUP_TBX 
					if (getModel().findCountByprocessPointGroupName(oldValue) > 0) {
						displayErrorMessage("Value cannot be updated as it is used", "Value cannot be updated as it is used");
						return;
					}
				}
			}
			
			//check duplicates
			if (!oldValue.equalsIgnoreCase(value) && isDuplicate(regionalCodeName, value)) {
				displayErrorMessage("Duplicate Regional Code", "Duplicate Regional Code.");
				return;
			}
			
			getModel().updateRegionalValue(oldRegionalCodeName, oldValue, value, name, abbreviation, description, updateUser);
			
			RegionalCode clonedRegionalCode = (RegionalCode)oldRegionalCode.deepCopy();
			clonedRegionalCode.getId().setRegionalValue(value);
			clonedRegionalCode.setRegionalValueName(name);
			clonedRegionalCode.setRegionalValueAbbr(abbreviation);
			clonedRegionalCode.setRegionalValueDesc(description);
			AuditLoggerUtil.logAuditInfo(oldRegionalCode, clonedRegionalCode, reasonForChange, 
					getDialog().getScreenName(), updateUser);
			
			getModel().setSelectedRegionalValue(getModel().findRegionalCode(oldRegionalCodeName, value));
			
		} else { //update code
			RegionalCode oldRegionalCode = getModel().getSelectedRegionalCode();
			String oldRegionalCodeName = oldRegionalCode.getId().getRegionalCodeName();
			
			if (oldRegionalCodeName.equals(regionalCodeName)) {
				displayErrorMessage("Duplicate Regional Code", "Duplicate Regional Code.");
				return;
			}
			List<RegionalCode> regionalCodeList = getModel().findRegionalValueList(oldRegionalCodeName);
			getModel().updateRegionalCode(oldRegionalCodeName, regionalCodeName, updateUser);
			
			for (int i = 0; i < regionalCodeList.size(); i++) {
				RegionalCode clonedRegionalCode = (RegionalCode)regionalCodeList.get(i).deepCopy();
				clonedRegionalCode.getId().setRegionalCodeName(regionalCodeName);
				AuditLoggerUtil.logAuditInfo(regionalCodeList.get(i), clonedRegionalCode, reasonForChange, 
						getDialog().getScreenName(), updateUser);
			}
		}
			
		getModel().setSelectedRegionalCode((getModel().findRegionalValueList(regionalCodeName)).get(0));
		
		try {
			LoggedButton updateBtn = getDialog().getUpdateBtn();
			Stage stage = (Stage) updateBtn.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occured in during create action ", "Failed to Create Regional Code", e);
		}
	}

	/**
	 * This method is used to create Entry Model.
	 */
	private void createBtnAction(ActionEvent actionEvent) {
		if (QiCommonUtil.isMandatoryFieldEmpty(getDialog().getRegionalCodeNameField())) {
			displayErrorMessage("Mandatory field is empty", "Regional Code Name is required.");
			return;
		}
		
		if (QiCommonUtil.isMandatoryFieldEmpty(getDialog().getValueField())) {
			displayErrorMessage("Mandatory field is empty", "Value is required.");
			return;
		}
		
		String regionalCodeName = getDialog().getRegionalCodeNameField().getText();
		String value = getDialog().getValueField().getText();
		String name = getDialog().getNameField().getText();
		String abbreviation = getDialog().getAbbreviationField().getText();
		String description = getDialog().getDescriptionField().getText();
		String createUser = getModel().getApplicationContext().getUserId().toUpperCase();

		RegionalCode regionalCode = new RegionalCode(new RegionalCodeId(regionalCodeName, value));
		regionalCode.setRegionalValueName(name);
		regionalCode.setRegionalValueAbbr(abbreviation);
		regionalCode.setRegionalValueDesc(description);
		regionalCode.setCreateUser(createUser);
		
		//check duplicates
		if (isDuplicate(regionalCodeName, value)) {
			displayErrorMessage("Duplicate Regional Code", "Duplicate Regional Code.");
			return;
		}
		
		RegionalCode savedRegionalCode = getModel().createRegionalCode(regionalCode);
			
		boolean isCreateValue = getDialog().getRegionalCodeNameField().isDisabled();
			
		if (isCreateValue) {
			getModel().setSelectedRegionalValue(savedRegionalCode);
			getModel().setSelectedRegionalCode(getModel().findRegionalValueList(regionalCodeName).get(0));
		} else {
			getModel().setSelectedRegionalCode(savedRegionalCode);
		}
		
		try {
			LoggedButton createBtn = getDialog().getCreateBtn();
			Stage stage = (Stage) createBtn.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occured in during create action ", "Failed to Create Regional Code", e);
		}
	}

	/**
	 * This method is used to set listener on text fields.
	 */
	@Override
	public void initListeners() {
		validationForTextfield();
		getDialog().getValueField().textProperty().addListener(updateEnablerForStringValueChange);
		getDialog().getNameField().textProperty().addListener(updateEnablerForStringValueChange);
		getDialog().getAbbreviationField().textProperty().addListener(updateEnablerForStringValueChange);
		getDialog().getDescriptionField().textProperty().addListener(updateEnablerForStringValueChange);
	}
	/**
	 * This method is used to validate the text fields.
	 */
	private void validationForTextfield() {
		getDialog().getRegionalCodeNameField().addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(256));
		getDialog().getValueField().addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(256));
		getDialog().getNameField().addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(128));
		getDialog().getAbbreviationField().addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(64));
		getDialog().getDescriptionField().addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(256));
		getDialog().getReasonForChangeTextArea().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(1024));
	}
	
	@Override
	protected void enableUpdateButton() {
		if(getDialog().getTitle().equals(RegionalDataConstant.UPDATE_CODE) ||
				getDialog().getTitle().equals(RegionalDataConstant.UPDATE_VALUE)){
			getDialog().getUpdateBtn().setDisable(false);
		}
	}
	
	private boolean isDuplicate(String regionalCodeName, String regionalValue) {
		if (getModel().findRegionalCode(regionalCodeName, regionalValue) != null) {
			return true;
		} else {
			return false;
		}
	}
}
