package com.honda.galc.client.teamleader.mtctomodelgroup;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.mvc.AbstractDialogController;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.CommonUtil;
import com.honda.galc.entity.product.ModelGroup;
import com.honda.galc.util.AuditLoggerUtil;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Toggle;
import javafx.scene.input.KeyEvent;

/**
 * <h3>MtcToModelGroupDialogController Class description</h3>
 * <p>
 * <code>MtcToModelGroupDialogController</code> is the controller class for
 * Model Group Dialog.
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
 * <TD>L&T Infotech</TD>
 * <TD>31/03/2017</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.0
 * @author L&T Infotech
 */
public class MtcToModelGroupDialogController extends AbstractDialogController<MtcToModelGroupMaintenanceModel, MtcToModelGroupDialog> {

	private ModelGroup selectedModel;
	private ModelGroup modelGroupClone;


	public MtcToModelGroupDialogController(MtcToModelGroupMaintenanceModel model, MtcToModelGroupDialog dialog,	ModelGroup selectedModel) {
		super();
		setModel(model);
		setDialog(dialog);
		this.selectedModel = selectedModel;
		this.modelGroupClone = (ModelGroup) selectedModel.deepCopy();
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		clearDisplayMessage();
		if (actionEvent.getSource().equals(getDialog().getCreateBtn()))
			createBtnAction(actionEvent);
		else if (actionEvent.getSource().equals(getDialog().getUpdateBtn()))
			updateBtnAction(actionEvent);
		else if (actionEvent.getSource().equals(getDialog().getCancelBtn()))
			cancelBtnAction(actionEvent);
	}

	private void cancelBtnAction(ActionEvent actionEvent) {
		try {
			getDialog().cancel();
		} catch (Exception e) {
			handleException("An error occured in during cancel action ", "Failed to perform cancel action", e);
		}

	}

	/**
	 * This method is used to update Model Group.
	 */
	private void updateBtnAction(ActionEvent actionEvent) {
		final String oldModelGroupName = selectedModel.getId().getModelGroup();
		final String newModelGroupName = CommonUtil.delMultipleSpaces(getDialog().getModelGroupName());
		final String oldModelGroupSystem = selectedModel.getId().getSystem();
		final String newModelGroupSystem = CommonUtil.delMultipleSpaces(getDialog().getModelGroupSystem());
		try {
			if (!(oldModelGroupName.equalsIgnoreCase(newModelGroupName))) {
				if (CommonUtil.isMandatoryFieldEmpty(getDialog().getModelGroupName())) {
					displayErrorMessage("Mandatory field is empty", "Please enter Model Group Name ");
					return;
				}
				if (!MessageDialog.confirm(null, "Are you sure you want to update Model Group Name")) {
					return;
				} else if (getModel().isModelGroupExist(newModelGroupName, newModelGroupSystem)) {
					displayErrorMessage("Already exists! Enter a different Model Group Name ",
							newModelGroupName + " already exists! Enter a different Name");
					return;
				}
			}
			if (!(oldModelGroupSystem.equalsIgnoreCase(newModelGroupSystem))) {
				if (CommonUtil.isMandatoryFieldEmpty(getDialog().getModelGroupSystem())) {
					displayErrorMessage("Mandatory field is empty", "Please enter System ");
					return;
				}
				if (!MessageDialog.confirm(null, "Are you sure you want to update System")) {
					return;
				}
			}
			if (checkForChangeReason()) {
				updateModelGroupId(oldModelGroupName, oldModelGroupSystem, newModelGroupName, newModelGroupSystem);
				updateSelectedModel();
				selectedModel.setUpdateUser(getUserId());
				modelGroupClone.setUpdateUser(getUserId());
				getModel().updateModelGroup(selectedModel);
				modelGroupClone.getId().setModelGroup(StringUtils.trimToEmpty(modelGroupClone.getId().getModelGroup()));
				selectedModel.getId().setModelGroup(StringUtils.trimToEmpty(selectedModel.getId().getModelGroup()));
				AuditLoggerUtil.logAuditInfo(modelGroupClone, selectedModel,
						getDialog().getReasonForChangeText().getText(), getDialog().getScreenName(), getModel().getUserId());
				getDialog().closeDialog();
			}

		} catch (Exception e) {
			handleException("An error occured in during create action ", "Failed to Update Model Group", e);
		}

	}

	/**
	 * This method is used to update Model Group Name.
	 */
	private void updateModelGroupId(String oldModelGroupName, String oldModelGroupSystem, String newModelGroupName, String newModelGroupSystem) {
		if (oldModelGroupName.equalsIgnoreCase(newModelGroupName) && oldModelGroupSystem.equalsIgnoreCase(newModelGroupSystem)) return;
		try {
			selectedModel.getId().setModelGroup(CommonUtil.delMultipleSpaces(getDialog().getModelGroupName()));
			selectedModel.getId().setSystem(CommonUtil.delMultipleSpaces(getDialog().getModelGroupSystem()));
			getModel().updateModelGroupId(newModelGroupName, newModelGroupSystem, getUserId(), oldModelGroupName, oldModelGroupSystem);
			getModel().updateModelGroupingId(newModelGroupName, newModelGroupSystem, getUserId(), oldModelGroupName, oldModelGroupSystem);
		} catch (Exception e) {
			handleException("An error occured in during update action ", "Failed to Update Model Group Name", e);
		}
	}

	/**
	 * This method is used to create Model Group.
	 */
	private void createBtnAction(ActionEvent actionEvent) {
		final UpperCaseFieldBean modelNameBean = getDialog().getModelGroupName();
		final UpperCaseFieldBean systemBean = getDialog().getModelGroupSystem();
		if (CommonUtil.isMandatoryFieldEmpty(modelNameBean)) {
			displayErrorMessage("Mandatory field is empty", "Please enter Model Group Name ");
			return;
		}
		if (CommonUtil.isMandatoryFieldEmpty(systemBean)) {
			displayErrorMessage("Mandatory field is empty", "Please enter System ");
			return;
		}
		selectedModel.getId().setModelGroup(CommonUtil.delMultipleSpaces(modelNameBean));
		selectedModel.getId().setSystem(CommonUtil.delMultipleSpaces(systemBean));
		selectedModel.setModelGroupDescription(CommonUtil.delMultipleSpaces(getDialog().getModelGroupDesc()));
		selectedModel.setProductType(StringUtils.trimToNull(getDialog().getProductTypeContextLabel().getText()));
		selectedModel.setActive((short) (getDialog().getActiveRadioBtn().isSelected() ? 1 : 0));
		selectedModel.setCreateUser(getUserId());
		selectedModel.setUpdateUser(StringUtils.EMPTY);
		if (!getModel().isModelGroupExist(CommonUtil.delMultipleSpaces(modelNameBean).trim(), CommonUtil.delMultipleSpaces(systemBean).trim())) {
			try {
				getModel().createModelGroup(selectedModel);
				getDialog().closeDialog();
			} catch (Exception e) {
				handleException("An error occured in during create action ", "Failed to Create Model Group", e);
			}
		} else {
			String errorAndLoggerMessage = "Failed to create " + modelNameBean.getText() + " already exists for system " + systemBean.getText() + "!";
			displayErrorMessage(errorAndLoggerMessage, errorAndLoggerMessage);
		}

	}

	private boolean checkForChangeReason() {
		if (CommonUtil.isMandatoryFieldEmpty(getDialog().getReasonForChangeText())) {
			displayErrorMessage("Mandatory field is empty", "Please enter Reason for Change");
			return false;
		}
		return true;
	}

	/**
	 * This method is used to update Model Group.
	 */
	private void updateSelectedModel() {
		selectedModel.setModelGroupDescription(CommonUtil.delMultipleSpaces(getDialog().getModelGroupDesc()).trim());
		selectedModel.setActive((short) (getDialog().getActiveRadioBtn().isSelected() ? 1 : 0));
	}

	/**
	 * This method is used to set listener on text fields.
	 */
	@Override
	public void initListeners() {
		validationForTextfield();
		addFieldListener(getDialog().getModelGroupName(), true);
		addFieldListener(getDialog().getModelGroupDesc(), true);
		addFieldListener(getDialog().getModelGroupSystem(), true);
		setTextFieldListener(getDialog().getModelGroupName());
		setTextFieldListener(getDialog().getModelGroupDesc());
		setTextFieldListener(getDialog().getModelGroupSystem());

		getDialog().getActiveRadioBtn().getToggleGroup().selectedToggleProperty().addListener(
				new ChangeListener<Toggle>() {
					public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
						getDialog().getUpdateBtn().setDisable(false);
					}
				});

		addTextChangeListener(getDialog().getModelGroupName());
		addTextChangeListener(getDialog().getModelGroupDesc());
		addTextChangeListener(getDialog().getModelGroupSystem());
	}

	/**
	 * This method is used for Text Field Listener
	 */
	protected void addTextChangeListener(final UpperCaseFieldBean TextField){
		TextField.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				if(getDialog().getUpdateBtn().isDisabled() && getDialog().isUpdate()) {
					getDialog().getUpdateBtn().setDisable(false);
				}

			}
		});
	}

	/**
	 * This method is used to validate the text fields.
	 */
	private void validationForTextfield() {
		getDialog().getModelGroupName().addEventFilter(KeyEvent.KEY_TYPED, CommonUtil.restrictLengthOfTextFields(32));
		getDialog().getModelGroupDesc().addEventFilter(KeyEvent.KEY_TYPED, CommonUtil.restrictLengthOfTextFields(32));
		getDialog().getModelGroupSystem().addEventFilter(KeyEvent.KEY_TYPED, CommonUtil.restrictLengthOfTextFields(32));
		getDialog().getReasonForChangeText().addEventFilter(KeyEvent.KEY_TYPED,	CommonUtil.restrictLengthOfTextFields(256));
	}

}
