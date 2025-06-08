package com.honda.galc.client.teamleader.qi.controller;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.teamleader.qi.model.LocalThemeModel;
import com.honda.galc.client.teamleader.qi.view.LocalThemeDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.entity.qi.QiLocalTheme;
import com.honda.galc.util.AuditLoggerUtil;

import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
/**
 * 
 * <h3>LocalThemeDialogController Class description</h3>
 * <p>
 * LocalThemeDialogController is used to perform the actions like 'create' ,'update' and 'cancel' etc.
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
 * </TABLE>
 * 
 * @author LnTInfotech<br>
 * 
 */

public class LocalThemeDialogController extends QiDialogController<LocalThemeModel, LocalThemeDialog> {

	private QiLocalTheme oldLocalTheme;

	@Override
	public void handle(ActionEvent actionEvent) {
		if (actionEvent.getSource() instanceof LoggedButton) {
			clearDisplayMessage();
			LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();
			if (actionEvent.getSource().equals(getDialog().getCreateButton()))
				createBtnAction(actionEvent);
			else if (actionEvent.getSource().equals(getDialog().getUpdateButton()))
				updateBtnAction(actionEvent);
			else if (actionEvent.getSource().equals(getDialog().getCancelButton())) {
				Stage stage = (Stage) loggedButton.getScene().getWindow();
				stage.close();
			}
		}

	}

	/** This method is used to update Local Theme.
	 * 
	 * @param actionEvent
	 */
	private void updateBtnAction(ActionEvent actionEvent) {
		if(QiCommonUtil.isMandatoryFieldEmpty(getDialog().getLocalThemeTextField())){
			displayErrorMessage("Mandatory field is empty.", "Please enter Local Theme Name.");
			return;
		}

		if(getDialog().getInactiveRadioBtn().isSelected()){
			if(getModel().isLocalThemeInUseByLocalDefect(getDialog().getLocalThemeTextField().getText().trim())){
				MessageDialog.showError(ClientMainFx.getInstance().getStage(null), 
						"The selected Local Theme(s) is in use. Hence, inactivate not allowed.");
				return;
			}
		}

		oldLocalTheme.setLocalTheme(oldLocalTheme.getLocalTheme().trim());

		QiLocalTheme qiLocalTheme = (QiLocalTheme) oldLocalTheme.deepCopy();
		qiLocalTheme.setLocalTheme(getDialog().getLocalThemeTextField().getText().trim());
		qiLocalTheme.setLocalThemeDescription(getDialog().getLocalThemeDescTextArea().getText().trim());
		qiLocalTheme.setActive(getDialog().getActiveRadioBtn().isSelected());

		if (oldLocalTheme.equals(qiLocalTheme)){
			displayErrorMessage("No change detected.", "No change detected.");
		}
		else {
			if(QiCommonUtil.isMandatoryFieldEmpty(getDialog().getReasonForChangeTextArea())){
				displayErrorMessage("Mandatory field is empty.", "Please enter Reason for change.");
				return;
			}
			
			qiLocalTheme.setUpdateUser(getUserId());
			try {
					if (qiLocalTheme.getLocalTheme().equals(oldLocalTheme.getLocalTheme())) {
						if (isUpdated(oldLocalTheme)) {
							return;
						}
						getModel().updateLocalTheme(qiLocalTheme);
					} else{
						if (null != getModel().findLocalThemeByName(qiLocalTheme.getLocalTheme())){
							displayErrorMessage("Local Theme already in use.", "Local Theme already in use.");
							return;
						}
						
						if (getModel().isLocalThemeInUseByLocalDefect(oldLocalTheme.getLocalTheme())) {
							if (!MessageDialog.confirm(ClientMainFx.getInstance().getStage(null),
									"The Local Theme being renamed is associated with one or more Local Defects."
											+ "Do you still want to continue?")) {
								return;
							}
							if (isUpdated(oldLocalTheme)) {
								return;
							}
							getModel().updateLocalTheme(qiLocalTheme, oldLocalTheme.getLocalTheme());
							getModel().updateLocalThemeForLocalDefects(qiLocalTheme.getLocalTheme(), getUserId(), 
									oldLocalTheme.getLocalTheme());
						}
						else{
							if (isUpdated(oldLocalTheme)) {
								return;
							}
							getModel().updateLocalTheme(qiLocalTheme, oldLocalTheme.getLocalTheme());
						}
					}
						
					// call to prepare and insert audit data
					AuditLoggerUtil.logAuditInfo(oldLocalTheme, qiLocalTheme,
							getDialog().getReasonForChangeTextArea().getText(), getDialog().getScreenName(), getUserId());
					
					Stage stage = (Stage) ((LoggedButton) actionEvent.getSource()).getScene().getWindow();
					stage.close();

			} catch (Exception e) {
				displayErrorMessage("Problem occured while updating the Local Theme.",
						"Problem occured while updating the Local Theme.");
			}
			
		}
	}

	/** This method is used to create Local Theme.
	 * 
	 * @param actionEvent
	 */
	private void createBtnAction(ActionEvent actionEvent) {
		if(QiCommonUtil.isMandatoryFieldEmpty(getDialog().getLocalThemeTextField())){
			displayErrorMessage("Mandatory field is empty", "Please enter Local Theme Name.");
			return;
		}

		QiLocalTheme qiLocalTheme = new QiLocalTheme();
		qiLocalTheme.setLocalTheme(getDialog().getLocalThemeTextField().getText().trim());
		qiLocalTheme.setLocalThemeDescription(getDialog().getLocalThemeDescTextArea().getText().trim());
		qiLocalTheme.setActive(getDialog().getActiveRadioBtn().isSelected());
		qiLocalTheme.setCreateUser(getUserId());

		try {

			if (null == getModel().findLocalThemeByName(qiLocalTheme.getLocalTheme())) {
				getModel().saveLocalTheme(qiLocalTheme);
				Stage stage = (Stage) ((LoggedButton) actionEvent.getSource()).getScene().getWindow();
				stage.close();
			} else {
				displayErrorMessage("Local Theme already in use.", "Local Theme already in use.");
			}

		} catch (Exception e) {
			displayErrorMessage("Problem occured while saving the Local Theme.", 
					"Problem occured while saving the Local Theme.");
		}
	}

	@Override
	public void initListeners() {

		getDialog().getLocalThemeTextField().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(18));
		setTextFieldListener(getDialog().getLocalThemeTextField());
		addFieldListener(getDialog().getLocalThemeTextField(), true);
		getDialog().getLocalThemeDescTextArea().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(64));
		addTextAreaListener(getDialog().getLocalThemeDescTextArea());
		getDialog().getActiveRadioBtn().getToggleGroup().selectedToggleProperty().addListener(updateEnablerForToggle);
	}

	public LocalThemeDialogController(LocalThemeModel model, LocalThemeDialog dialog, QiLocalTheme oldLocalTheme) {
		super();
		setModel(model);
		setDialog(dialog);
		this.oldLocalTheme=oldLocalTheme;
	}

}
