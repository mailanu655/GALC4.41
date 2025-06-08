package com.honda.galc.client.teamleader.qi.controller;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.model.EntryModelMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.EntryModelDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.entity.qi.QiEntryModel;
import com.honda.galc.entity.qi.QiEntryModelId;
import com.honda.galc.util.AuditLoggerUtil;

import javafx.scene.input.KeyEvent;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import com.honda.galc.client.ui.MessageDialog;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>EntryModelDialogController</code> is the controller class for Entry Model Dialog.
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
 * <TD>L&T Infotech</TD>
 * <TD>10/09/2016</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.0
 * @author L&T Infotech
 */
public class EntryModelDialogController extends QiDialogController<EntryModelMaintenanceModel, EntryModelDialog> {

	private QiEntryModel selectedModel;
	private QiEntryModel qiEntryModelClone;

	public EntryModelDialogController(EntryModelMaintenanceModel model, EntryModelDialog dialog,
			QiEntryModel selectedModel) {
		super();
		setModel(model);
		setDialog(dialog);
		this.selectedModel = selectedModel;
		this.qiEntryModelClone=(QiEntryModel)selectedModel.deepCopy();
	}

	@Override
	public void handle(ActionEvent actionEvent) {
            clearDisplayMessage();
			if (actionEvent.getSource().equals(getDialog().getCreateBtn()))
				createBtnAction(actionEvent);
			else if (actionEvent.getSource().equals(getDialog().getUpdateButton()))
				updateBtnAction(actionEvent);
			else if (actionEvent.getSource().equals(getDialog().getCancelBtn()))
				cancelBtnAction(actionEvent);
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
	/**
	 * This method is used to update Entry Model.
	 */
	private void updateBtnAction(ActionEvent actionEvent) {
		LoggedButton updateBtn = getDialog().getUpdateButton();
		String oldEntryModelName = selectedModel.getId().getEntryModel();
		QiEntryModelId newQiEntryModelId = new QiEntryModelId();
		newQiEntryModelId.setEntryModel(QiCommonUtil.delMultipleSpaces(getDialog().getEntryModelName()).trim());
		newQiEntryModelId.setIsUsed(selectedModel.getId().getIsUsed());
		
		try {
			boolean updateEntryModelName = false;
			if (!(oldEntryModelName.equalsIgnoreCase(newQiEntryModelId.getEntryModel()))) {
				if(selectedModel.getId().getIsUsed() == (short)0 && getModel().isVersionCreated(oldEntryModelName)) {
					displayErrorMessage("Cannot update Name. Version exists", "Cannot update Name. Version exists");
					return;
				}
				if (QiCommonUtil.isMandatoryFieldEmpty(getDialog().getEntryModelName())) {
					displayErrorMessage("Mandatory field is empty", "Please enter Entry Model Name ");
					return;
				}
				if (QiCommonUtil.isMandatoryFieldEmpty(getDialog().getReasonForChangeText())) {
					displayErrorMessage("Mandatory field is empty", "Please enter Reason for Change");
					return;
				}
				if (!MessageDialog.confirm(null, "Are you sure you want to update Entry Model Name")) {
					return;
				}
				else if (getModel().isEntryModelExist(newQiEntryModelId.getEntryModel())) {
					displayErrorMessage("Already exists! Enter a different Entry Model Name ",
							newQiEntryModelId.getEntryModel() + " already exists! Enter a different Name");
					return;
				} else {
					updateEntryModelName = true;
				}
			} 
			if (isUpdated(qiEntryModelClone)) {
				return;
			}
			if (updateEntryModelName) {
				updateEntryModelName(oldEntryModelName, newQiEntryModelId);
			}
			updateSelectedModel();
			selectedModel.setUpdateUser(getUserId());
			getModel().updateEntryModel(selectedModel);
			// call to prepare and insert audit data
			qiEntryModelClone.getId().setEntryModel(qiEntryModelClone.getId().getEntryModel().trim());
			selectedModel.getId().setEntryModel(selectedModel.getId().getEntryModel().trim());
			AuditLoggerUtil.logAuditInfo(qiEntryModelClone, selectedModel,
					getDialog().getReasonForChangeText().getText(), getDialog().getScreenName(), getUserId());
			Stage stage = (Stage) updateBtn.getScene().getWindow();
			stage.close();

		}catch (Exception e) {
				handleException("An error occured in during create action ", "Failed to Update Entry Model", e);
			}
		
	}
	/**
	 * This method is used to update Entry Model Name.
	 */
	private void updateEntryModelName(String oldEntryModelName, QiEntryModelId newQiEntryModelId) {
		try{
				selectedModel.getId().setEntryModel(QiCommonUtil.delMultipleSpaces(getDialog().getEntryModelName()).trim());
				getModel().updateEntryModelName(newQiEntryModelId.getEntryModel(), getUserId(), oldEntryModelName, newQiEntryModelId.getIsUsed());
				getModel().updateEntryModelGroupingId(newQiEntryModelId.getEntryModel(), getUserId(), oldEntryModelName, newQiEntryModelId.getIsUsed());
				
				//update new model name in all associate table
				getModel().updateModelInAssociateTable(newQiEntryModelId.getEntryModel(), oldEntryModelName, getUserId(), newQiEntryModelId.getIsUsed());

		}catch (Exception e) {
			handleException("An error occured in during upadte action ", "Failed to Update Entry Model Name", e);
		}
	}
	/**
	 * This method is used to create Entry Model.
	 */
	private void createBtnAction(ActionEvent actionEvent) {
		UpperCaseFieldBean modelNameBean = getDialog().getEntryModelName();
		if (QiCommonUtil.isMandatoryFieldEmpty(modelNameBean)) {
			displayErrorMessage("Mandatory field is empty", "Please enter Entry Model Name ");
			return;
		}
		QiEntryModelId qiEntryModelId = new QiEntryModelId();
		qiEntryModelId.setEntryModel(QiCommonUtil.delMultipleSpaces(getDialog().getEntryModelName()).trim());
		qiEntryModelId.setIsUsed((short)0);
		selectedModel.setId(qiEntryModelId);
		selectedModel.setEntryModelDescription(QiCommonUtil.delMultipleSpaces(getDialog().getEntryModelDesc()).trim());
		selectedModel.setProductType(StringUtils.trimToNull(getDialog().getProductTypeContextLabel().getText()));
		selectedModel.setActive((short) (getDialog().getActiveRadioBtn().isSelected() ? 1 : 0));
		selectedModel.setCreateUser(getUserId());
		selectedModel.setUpdateUser(StringUtils.EMPTY);

		if (!getModel().isEntryModelExist(qiEntryModelId.getEntryModel())) {
			try {
				getModel().createEntryModel(selectedModel);
				LoggedButton createBtn = getDialog().getCreateBtn();
				Stage stage = (Stage) createBtn.getScene().getWindow();
				stage.close();
			} catch (Exception e) {
				handleException("An error occured in during create action ", "Failed to Create Entry Model", e);
			}
		} else {
			String errorAndLoggerMessage = "Failed to create " + modelNameBean.getText() + " already exists!";
			displayErrorMessage(errorAndLoggerMessage, errorAndLoggerMessage);
		}

	}

	/**
	 * This method is used to update Entry Model.
	 */
	private void updateSelectedModel() {	
		selectedModel.setEntryModelDescription(QiCommonUtil.delMultipleSpaces(getDialog().getEntryModelDesc()).trim());
		selectedModel.setActive((short)(getDialog().getActiveRadioBtn().isSelected() ? 1 : 0));
	}
	/**
	 * This method is used to set listener on text fields.
	 */
	@Override
	public void initListeners() {
		validationForTextfield();
		addFieldListener(getDialog().getEntryModelName(), true);
		addFieldListener(getDialog().getEntryModelDesc(), true);
		setTextFieldListener(getDialog().getEntryModelName());
		setTextFieldListener(getDialog().getEntryModelDesc());
		
		getDialog().getActiveRadioBtn().getToggleGroup().selectedToggleProperty().addListener(updateEnablerForToggle);
	}
	/**
	 * This method is used to validate the text fields.
	 */
	private void validationForTextfield() {
		getDialog().getEntryModelName().addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(32));
		getDialog().getEntryModelDesc().addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(32));
		getDialog().getReasonForChangeText().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(256));
	}

}
