package com.honda.galc.client.teamleader.qi.controller;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import com.honda.galc.client.teamleader.qi.model.IqsMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.IqsCategoryDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.qi.QiIqs;
import com.honda.galc.entity.qi.QiIqsCategory;
import com.honda.galc.util.AuditLoggerUtil;

/**
 * 
 * <h3>IqsCategoryDialogController Class description</h3>
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

public class IqsCategoryDialogController extends QiDialogController<IqsMaintenanceModel, IqsCategoryDialog> {

	private QiIqsCategory iqsCategory;
	private QiIqsCategory iqsCategoryCloned;
	

	public IqsCategoryDialogController(IqsMaintenanceModel model,IqsCategoryDialog iqsCategoryDialog, QiIqsCategory iqsCategory) {
		super();
		setModel(model);
		setDialog(iqsCategoryDialog);
		this.setIqsCategory(iqsCategory);
		this.iqsCategory=iqsCategory;
		this.iqsCategoryCloned=(QiIqsCategory) iqsCategory.deepCopy();
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource() instanceof LoggedButton) {
			LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();
			if(actionEvent.getSource().equals(getDialog().getCancelBtn())) cancelBtnAction(actionEvent);
			else onClickBtnAction(actionEvent,loggedButton);
		} 
	}
	/**
	 * This method is used to Create/Update IQS Category
	 * @param actionEvent
	 * @param loggedButton
	 */
	public void onClickBtnAction(ActionEvent actionEvent, LoggedButton loggedButton) {
		String iqsCategoryInput = StringUtils.trim(getDialog().getIqsCategoryTextField().getText());
		String previousIqsCategory = iqsCategory.getIqsCategory();
		boolean isMandatoryFieldNotChecked=false;
		boolean updateAssociatedEntities = false;
		if(!isMandatoryFieldNotChecked(isMandatoryFieldNotChecked,loggedButton)){
			if(null != previousIqsCategory && previousIqsCategory.equalsIgnoreCase(iqsCategoryInput)){
				getDialog().displayValidationMessage(getDialog().getMsgLabel(), "No Change in IQS Category to update.", "error-message");
				return;
			}
			try{
				if (getModel().isIqsCategoryExists(iqsCategoryInput)){
					getDialog().displayValidationMessage(getDialog().getMsgLabel(), "IQS Category already exists. Please create a new category.", "error-message");
				}
				else {
					iqsCategory.setIqsCategory(iqsCategoryInput);
					if(loggedButton.getText().equalsIgnoreCase(QiConstant.CREATE)){
						iqsCategory.setCreateUser(getUserId());
						getModel().createIqsCategory(iqsCategory);
					}
					else{
						iqsCategory.setUpdateUser(getUserId());	
						List<QiIqs> iqsAssociation = getModel().getIqsAssociationForSelectedCategory(previousIqsCategory);
						if(!iqsAssociation.isEmpty()){
							boolean isUpdated = MessageDialog.confirm(getDialog(),"Updating IQS Category will affect its Associations. Do you still want to continue?");
							if(!isUpdated)
								return;
							updateAssociatedEntities = true;
						}
						if (isUpdated(iqsCategoryCloned)) {
							return;
						}
						if (updateAssociatedEntities) {
							for(QiIqs qiIqs : iqsAssociation){
								getModel().updateIqsAssociationCategory(qiIqs.getIqsId(),iqsCategoryInput);
							}
						}
						getModel().updateIqsCategory(iqsCategoryInput, previousIqsCategory);
						//call to prepare and insert audit data
						AuditLoggerUtil.logAuditInfo(iqsCategoryCloned, iqsCategory, getDialog().getIqsReasonTextArea().getText(),getDialog().getScreenName(),getUserId() );
					}
					iqsCategory.clear();
					Stage stage = (Stage) loggedButton.getScene().getWindow();
					stage.close();
				}
			} catch (Exception e) {
				handleException("An error occured in during onClickBtnAction method ", "Failed to Create/Update IQS Category", e);
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
		/** Mandatory Check for IQS Category */
		if(QiCommonUtil.isMandatoryFieldEmpty(getDialog().getIqsCategoryTextField())){
			getDialog().displayValidationMessage(getDialog().getMsgLabel(), "Please enter IQS Category", "error-message");
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
		getDialog().getIqsCategoryTextField().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(32));
		setTextFieldListener(getDialog().getIqsCategoryTextField());
		getDialog().getIqsReasonTextArea().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(256));
		
		addTextChangeListener(getDialog().getIqsCategoryTextField());
	}

	public QiIqsCategory getIqsCategory() {
		return iqsCategory;
	}

	public void setIqsCategory(QiIqsCategory iqsCategory) {
		this.iqsCategory = iqsCategory;
	}

}

