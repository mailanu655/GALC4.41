package com.honda.galc.client.teamleader.qi.controller;

import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.model.IqsMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.IqsQuestionDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.qi.QiIqs;
import com.honda.galc.entity.qi.QiIqsQuestion;
import com.honda.galc.entity.qi.QiIqsQuestionId;
import com.honda.galc.util.AuditLoggerUtil;

/**
 * 
 * <h3>IqsQuestionDialogController Class description</h3>
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

public class IqsQuestionDialogController extends QiDialogController<IqsMaintenanceModel, IqsQuestionDialog> {

	private QiIqsQuestion iqsQuestion;
	private static final String QUES_VALIDATION = "IQS Question # and IQS Question combination already exists.";
	private QiIqsQuestion iqsQuestionCloned;

	public IqsQuestionDialogController(IqsMaintenanceModel model,IqsQuestionDialog iqsQuestionDialog, QiIqsQuestion iqsQuestion) {
		super();
		setModel(model);
		setDialog(iqsQuestionDialog);
		this.setIqsQuestion(iqsQuestion);
		this.iqsQuestion=iqsQuestion;
		this.iqsQuestionCloned=(QiIqsQuestion) iqsQuestion.deepCopy();
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
	 * This method is used to Create/Update IQS Question
	 * @param actionEvent
	 * @param loggedButton
	 */
	public void onClickBtnAction(ActionEvent actionEvent, LoggedButton button) {
		String iqsQuestionNo = StringUtils.trim(getDialog().getIqsQuestionNoTextField().getText());
		String iqsQuestionInput = StringUtils.trim(getDialog().getIqsQuestionTextArea().getText());
		Integer previousQuestionNo;
		String previousIqsQuestion;
		if(button.getText().equalsIgnoreCase(QiConstant.CREATE)){
			previousQuestionNo =0;
			previousIqsQuestion =StringUtils.EMPTY;
		}
		else{
			previousQuestionNo = iqsQuestion.getId().getIqsQuestionNo();
			previousIqsQuestion = iqsQuestion.getId().getIqsQuestion();
		}
		boolean isMandatoryFieldNotChecked=false;
		boolean updateAssociatedEntities = false;
		if(!isMandatoryFieldNotChecked(isMandatoryFieldNotChecked,button)){
			if((Long.valueOf(iqsQuestionNo) > Integer.MAX_VALUE) || (Long.valueOf(iqsQuestionNo)<1)){
				getDialog().displayValidationMessage(getDialog().getMsgLabel(), "Please enter value between 1 to 2147483647 in Question # field.", "error-message");
				return;
			}
			else if(previousQuestionNo==(Integer.parseInt(iqsQuestionNo))){
				if(previousIqsQuestion.equalsIgnoreCase(iqsQuestionInput)){
					getDialog().displayValidationMessage(getDialog().getMsgLabel(), "No Change in IQS Question #/Question to update.", "error-message");
					return;
				}
				else if(getModel().isIqsQuestionExists(Integer.parseInt(iqsQuestionNo), iqsQuestionInput)){
					getDialog().displayValidationMessage(getDialog().getMsgLabel(), QUES_VALIDATION, "error-message");
					return;
				}
			}
			else if(previousIqsQuestion.equalsIgnoreCase(iqsQuestionInput)){
				if(getModel().isIqsQuestionExists(Integer.parseInt(iqsQuestionNo),iqsQuestionInput)){
					getDialog().displayValidationMessage(getDialog().getMsgLabel(), QUES_VALIDATION, "error-message");
					return;
				}
			}else{
				if(getModel().isIqsQuestionExists(Integer.parseInt(iqsQuestionNo),iqsQuestionInput)){
					getDialog().displayValidationMessage(getDialog().getMsgLabel(), QUES_VALIDATION, "error-message");
					return;
				}
			}
			List<QiIqs> iqsAssociation = getModel().getIqsAssociationForSelectedQuestionNo(previousQuestionNo,previousIqsQuestion);
			if(!iqsAssociation.isEmpty()){
				boolean isUpdated = MessageDialog.confirm(getDialog(),"Updating IQS Question # will affect its Associations. Do you still want to continue?");
				if(!isUpdated)
					return;
				updateAssociatedEntities = true;
			}
			
			if(button.getText().equalsIgnoreCase(QiConstant.UPDATE)) {
				if (isUpdated(iqsQuestionCloned)) {
					return;
				}
			}
			if (updateAssociatedEntities) {
				for(QiIqs qiIqs : iqsAssociation){
					getModel().updateQuestionNoAndQuestion(qiIqs.getIqsId(),Integer.parseInt(iqsQuestionNo),iqsQuestionInput);
				}
			}
			setQuestionValue(button, iqsQuestionNo, iqsQuestionInput,
					previousQuestionNo, previousIqsQuestion);
		}
	}
	/**
	 * Method to check whether mandatory fields are filled
	 * @param isMandatoryFieldChecked
	 * @param button
	 * @return
	 */
	private boolean isMandatoryFieldNotChecked(boolean isMandatoryFieldChecked, LoggedButton button) {
		/** Mandatory Check for Question # */
		if(QiCommonUtil.isMandatoryFieldEmpty(getDialog().getIqsQuestionNoTextField())){
			getDialog().displayValidationMessage(getDialog().getMsgLabel(), "Please enter IQS Question #", "error-message");
			isMandatoryFieldChecked = true;
		}
		/** Mandatory Check for Question */
		else if(QiCommonUtil.isMandatoryFieldEmpty(getDialog().getIqsQuestionTextArea())){
			getDialog().displayValidationMessage(getDialog().getMsgLabel(), "Please enter IQS Question", "error-message");
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
	 * This method is used to set the Question Value
	 * @param button
	 * @param iqsQuestionNo
	 * @param iqsQuestionInput
	 * @param previousQuestionNo
	 * @param previousIqsQuestion
	 */
	private void setQuestionValue(LoggedButton button, String iqsQuestionNo,
			String iqsQuestionInput, Integer previousQuestionNo,
			String previousIqsQuestion) {
		try{
			if(button.getText().equalsIgnoreCase(QiConstant.CREATE)){
				QiIqsQuestionId iqsQuestionId = new QiIqsQuestionId();
				iqsQuestion.setId(iqsQuestionId);
				setIqsQuestion(iqsQuestionNo, iqsQuestionInput);
				iqsQuestion.setCreateUser(getUserId());
				getModel().createIqsQuestion(iqsQuestion);
			}
			else{
				setIqsQuestion(iqsQuestionNo, iqsQuestionInput);
				iqsQuestion.setUpdateUser(getUserId());	
				getModel().updateIqsQuestion(Integer.parseInt(iqsQuestionNo),iqsQuestionInput,previousQuestionNo, previousIqsQuestion);
				//call to prepare and insert audit data
				AuditLoggerUtil.logAuditInfo(iqsQuestionCloned, iqsQuestion, getDialog().getIqsReasonTextArea().getText(),getDialog().getScreenName() ,getUserId());
			}
			iqsQuestion.getId().clear();
			Stage stage = (Stage) button.getScene().getWindow();
			stage.close();

		} catch (Exception e) {
			handleException("An error occured in onClickBtnAction method ", "Failed to Create/Update IQS Question", e);
		}
	}

	/**
	 * this method is used to set IQS Question
	 * @param iqsQuestionNo
	 * @param iqsQuestionInput
	 */
	private void setIqsQuestion(String iqsQuestionNo, String iqsQuestionInput) {
		iqsQuestion.getId().setIqsQuestionNo(Integer.parseInt(iqsQuestionNo));
		iqsQuestion.getId().setIqsQuestion(iqsQuestionInput);
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
		addTextFieldCommonListener(getDialog().getIqsQuestionNoTextField(),false);
		setTextFieldListener(getDialog().getIqsQuestionNoTextField());
		addIqsQuestionListener(getDialog().getIqsQuestionTextArea());
		getDialog().getIqsQuestionNoTextField().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(10));
		getDialog().getIqsQuestionTextArea().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(256));
		getDialog().getIqsReasonTextArea().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(256));
		
		addTextChangeListener(getDialog().getIqsQuestionNoTextField());
		addTextAreaChangeListener(getDialog().getIqsQuestionTextArea());
	}
	public QiIqsQuestion getIqsQuestion() {
		return iqsQuestion;
	}

	public void setIqsQuestion(QiIqsQuestion iqsQuestion) {
		this.iqsQuestion = iqsQuestion;
	}
	
	public void addIqsQuestionListener(final LoggedTextArea textArea){
		textArea.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				textArea.setText(textArea.getText().toUpperCase());
			}
		});
	}

}

