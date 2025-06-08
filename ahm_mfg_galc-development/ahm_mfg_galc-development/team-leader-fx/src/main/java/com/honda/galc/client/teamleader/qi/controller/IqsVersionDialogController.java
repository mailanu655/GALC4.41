package com.honda.galc.client.teamleader.qi.controller;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import com.honda.galc.client.teamleader.qi.model.IqsMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.IqsVersionDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.qi.QiIqs;
import com.honda.galc.entity.qi.QiIqsVersion;
import com.honda.galc.util.AuditLoggerUtil;

/**
 * 
 * <h3>IqsDialogController Class description</h3>
 * <p> QIIqsVersionDao description </p>
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
 * @author L&T Infotech<br>
 * Sep 15 2016
 *
 *
 */

public class IqsVersionDialogController extends QiDialogController<IqsMaintenanceModel, IqsVersionDialog> {

	private QiIqsVersion qiIqsVersion;
	private QiIqsVersion qiIqsVersionCloned;

	public IqsVersionDialogController(IqsMaintenanceModel model,IqsVersionDialog iqsVersionDialog, QiIqsVersion qiIqsVersion) {
		super();
		setModel(model);
		setDialog(iqsVersionDialog);
		this.setQiIqsVersion(qiIqsVersion);
		this.qiIqsVersion=qiIqsVersion;
		this.qiIqsVersionCloned=(QiIqsVersion) qiIqsVersion.deepCopy();
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource() instanceof LoggedButton) {
			LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();

			if(actionEvent.getSource().equals(getDialog().getCancelBtn())) cancelBtnAction(actionEvent);
			else
				onClickBtnAction(actionEvent,loggedButton);
		} 
	}
	/**
	 * This method is used to Create/Update IQS Version
	 * @param actionEvent
	 * @param loggedButton
	 */
	public void onClickBtnAction(ActionEvent actionEvent, LoggedButton loggedButton) {
		String iqsVersionInput = StringUtils.trim(getDialog().getIqsVersionTextField().getText());
		String previousIqsVersion = qiIqsVersion.getIqsVersion();
		boolean isMandatoryFieldNotChecked=false;
		boolean updateAssociatedEntities = false;
		if(!isMandatoryFieldNotChecked(isMandatoryFieldNotChecked,loggedButton)){
			if(null != previousIqsVersion && previousIqsVersion.equalsIgnoreCase(iqsVersionInput)){
				getDialog().displayValidationMessage(getDialog().getMsgLabel(), "No Change in IQS Version to update.", "error-message");
				return;
			}
			try{
				if (getModel().isIqsVersionExists(iqsVersionInput))
					getDialog().displayValidationMessage(getDialog().getMsgLabel(), "IQS Version already exists. Please create a new version.", "error-message");
				else {
					qiIqsVersion.setIqsVersion(iqsVersionInput);
					if(loggedButton.getText().equalsIgnoreCase(QiConstant.CREATE)){
						qiIqsVersion.setCreateUser(getUserId());
						getModel().createIqsVersion(qiIqsVersion);
					}
					else{
						qiIqsVersion.setUpdateUser(getUserId());	
						List<QiIqs> iqsAssociation = getModel().getIqsAssociationForSelectedVersion(previousIqsVersion);
						if(!iqsAssociation.isEmpty()){
							boolean isUpdated = MessageDialog.confirm(getDialog(),"Updating IQS Version will affect its Associations. Do you still want to continue?");
							if(!isUpdated)
								return;
							updateAssociatedEntities = true;
						}
						if (isUpdated(qiIqsVersionCloned)) {
							return;
						}
						if (updateAssociatedEntities) {
							for(QiIqs qiIqs : iqsAssociation){
								getModel().updateIqsAssociationVersion(qiIqs.getIqsId(),iqsVersionInput);
							}
						}
						getModel().updateIqsVersion(iqsVersionInput, previousIqsVersion);
						//call to prepare and insert audit data
						AuditLoggerUtil.logAuditInfo(qiIqsVersionCloned, qiIqsVersion, getDialog().getIqsReasonTextArea().getText(),getDialog().getScreenName() ,getUserId());
					}
					qiIqsVersion.clear();
					Stage stage = (Stage) loggedButton.getScene().getWindow();
					stage.close();
				}
			} catch (Exception e) {
				handleException("An error occured in onClickBtnAction method ", "Failed to Create/Update IQS Version", e);
			}
		}
	}
	/**
	 * Method to check whether mandatory fields are filled
	 * @param isMandatoryFieldChecked
	 * @param button
	 * @return
	 */
	private boolean isMandatoryFieldNotChecked(boolean isMandatoryFieldChecked, LoggedButton button) {
		/** Mandatory Check for IQS Version */
		if(QiCommonUtil.isMandatoryFieldEmpty(getDialog().getIqsVersionTextField())){
			getDialog().displayValidationMessage(getDialog().getMsgLabel(), "Please enter IQS Version", "error-message");
			isMandatoryFieldChecked = true;
		}
		/** Mandatory Check for Reason for Change */
		else if(button.getText().equalsIgnoreCase(QiConstant.UPDATE) && QiCommonUtil.isMandatoryFieldEmpty(getDialog().getIqsReasonTextArea())){
			getDialog().displayValidationMessage(getDialog().getMsgLabel(), "Please enter Reason for Change", "error-message");
			isMandatoryFieldChecked = true;
		}
		return isMandatoryFieldChecked;
	}

	/**
	 * When user clicks on cancel button in the popup screen cancelDefect method gets called.
	 */
	public void cancelBtnAction(ActionEvent event) {
		LoggedButton cancelBtn = getDialog().getCancelBtn();
		try {
			Stage stage = (Stage) cancelBtn.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occured in during cancel action ", "Failed to perform cancel action", e);
		}
	}

	@Override
	public void initListeners() {
		addFieldListener(getDialog().getIqsVersionTextField(),true);
		getDialog().getIqsVersionTextField().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(16));
		setTextFieldListener(getDialog().getIqsVersionTextField());
		getDialog().getIqsReasonTextArea().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(256));
	}

	public QiIqsVersion getQiIqsVersion() {
		return qiIqsVersion;
	}

	public void setQiIqsVersion(QiIqsVersion qiIqsVersion) {
		this.qiIqsVersion = qiIqsVersion;
	}
}

