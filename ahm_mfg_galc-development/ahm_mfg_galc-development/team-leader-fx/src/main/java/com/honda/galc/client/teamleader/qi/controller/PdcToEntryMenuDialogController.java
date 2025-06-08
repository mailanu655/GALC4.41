package com.honda.galc.client.teamleader.qi.controller;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import com.honda.galc.client.teamleader.qi.model.PdcToEntryScreenAssignmentModel;
import com.honda.galc.client.teamleader.qi.view.PdcToEntryMenuDialog;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.dto.qi.QiEntryScreenDto;
import com.honda.galc.entity.qi.QiLocalDefectCombination;
import com.honda.galc.entity.qi.QiTextEntryMenu;
import com.honda.galc.entity.qi.QiTextEntryMenuId;
import com.honda.galc.util.AuditLoggerUtil;


public class PdcToEntryMenuDialogController extends QiDialogController<PdcToEntryScreenAssignmentModel,PdcToEntryMenuDialog> {
	
	private QiEntryScreenDto qiEntryModelDto;
	private QiTextEntryMenu selectedComb;
	public PdcToEntryMenuDialogController(PdcToEntryScreenAssignmentModel model, PdcToEntryMenuDialog dialog, QiTextEntryMenu selectedComb,QiEntryScreenDto qiEntryModelDto) {
		super();
		setModel(model);
		setDialog(dialog);
		this.selectedComb = selectedComb;
		this.qiEntryModelDto = qiEntryModelDto;
	}
		
	
	public void handleException(String loggerMsg, String errMsg,
			String parentScreen, Exception e) {
		getLogger().error(e, new LogRecord(errMsg));
		EventBusUtil.publish(new StatusMessageEvent(errMsg, StatusMessageEventType.ERROR));
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource().equals(getDialog().getCreateBtn())) createBtnAction(actionEvent);
		else if(actionEvent.getSource().equals(getDialog().getUpdateButton())) updateBtnAction(actionEvent);
		else if(actionEvent.getSource().equals(getDialog().getCancelBtn())) cancelBtnAction(actionEvent);
	}
	
	public void createBtnAction(ActionEvent actionEvent) {
		UpperCaseFieldBean menuNameBean = getDialog().getMenuNameTxtFld();
		if(QiCommonUtil.isMandatoryFieldEmpty(menuNameBean)){
			displayErrorMessage("Mandatory field is empty", "Please enter menu Name ");
			return;
		}

		QiTextEntryMenu qiTextEntryMenu = new QiTextEntryMenu(qiEntryModelDto.getEntryScreen(), menuNameBean.getText().trim(), qiEntryModelDto.getEntryModel(), qiEntryModelDto.getIsUsedVersion());
		qiTextEntryMenu.setTextEntryMenuDesc(getDialog().getMenuDescTxtFld().getText().trim());
		qiTextEntryMenu.setCreateUser(getUserId());
		LoggedButton createBtn = getDialog().getCreateBtn();

		if(!isValidMenu(new QiTextEntryMenuId(qiEntryModelDto.getEntryScreen().trim(), getDialog().getMenuNameTxtFld().getText().trim(), qiEntryModelDto.getEntryModel(), (short)1))) {
			try {
				getModel().createTextEntryMenu(qiTextEntryMenu);
				Stage stage = (Stage) createBtn.getScene().getWindow();
				stage.close();
			} catch (Exception e) {
				handleException("An error occured in during create action ", "Failed to Create Part Text Entry Menu", e);
			}
		}else{
			String errorAndLoggerMessage= menuNameBean.getText()+" Menu already exists!";
			displayErrorMessage(errorAndLoggerMessage,errorAndLoggerMessage);
		}


	}
	
	private boolean isValidMenu(QiTextEntryMenuId qiTextEntryMenu) {
		boolean  isMenuExist = false ;
		try {
			isMenuExist = getModel().isTextEntryMenuExist(qiTextEntryMenu);
		} catch (Exception e) {
			handleException("An error occured in during create action ", "Failed to Create Part Text Entry Menu", e);
		}
		
		return isMenuExist;
	}




	public void updateBtnAction(ActionEvent actionEvent){	

		boolean updateTextEntryMenu  = false;
		LoggedButton updateBtn = getDialog().getUpdateButton();
		String oldTextEntryMenu = selectedComb.getId().getTextEntryMenu();
		
		if(null!=qiEntryModelDto){
			
			try{
				QiTextEntryMenu qiTextEntryMenuCloned =  getModel().findTextEntryByKey(selectedComb.getId());
				
				QiTextEntryMenu qiTextEntryMenu = new QiTextEntryMenu();
				QiTextEntryMenuId entryMenuId = new QiTextEntryMenuId();
				
				
				qiTextEntryMenu.setUpdateUser(getUserId());
				qiTextEntryMenu.setTextEntryMenuDesc(getDialog().getMenuDescTxtFld().getText());
				
				entryMenuId.setEntryModel(qiEntryModelDto.getEntryModel().trim());
				entryMenuId.setEntryScreen(qiEntryModelDto.getEntryScreen().trim());
				entryMenuId.setIsUsed(qiEntryModelDto.getIsUsedVersion());
				entryMenuId.setTextEntryMenu(getDialog().getMenuNameTxtFld().getText().trim());
				qiTextEntryMenu.setId(entryMenuId);
				
				if(!(oldTextEntryMenu.equalsIgnoreCase(getDialog().getMenuNameTxtFld().getText().trim()))){
					if(QiCommonUtil.isMandatoryFieldEmpty(getDialog().getMenuNameTxtFld())){
						displayErrorMessage("Mandatory field is empty", "Please enter Text Entry Menu name ");
						return;
					}
					if(QiCommonUtil.isMandatoryFieldEmpty(getDialog().getReasonForChangeTextArea())){
						displayErrorMessage("Mandatory field is empty", "Please enter Reason for change");
						return;
					}
					updateTextEntryMenu = MessageDialog.confirm(null, "Are you sure you want to Update Text Entry Menu?");
					if(!updateTextEntryMenu)
						return;
					else if(updateTextEntryMenu && getModel().isTextEntryMenuExist(entryMenuId)){
						displayErrorMessage("Already exists! Enter a different Text Entry Menu Name ", entryMenuId.getTextEntryMenu() + " already exists! Enter a different Name");
						return;
					}else{
						List<QiLocalDefectCombination> localDefectCombinationList = getModel().findAllByTextMenuAndEntryScreen(qiEntryModelDto.getEntryScreen(), oldTextEntryMenu,qiEntryModelDto.getEntryModel().trim());
						getModel().updateTextEntryMenu(qiTextEntryMenu,oldTextEntryMenu);
						getModel().updateEntryScreenDefectCombination(qiTextEntryMenu, oldTextEntryMenu);
						//updates all local combination id
					    if(localDefectCombinationList !=null && localDefectCombinationList.size()>0)
					    	getModel().updateAllLocalCombId(localDefectCombinationList,qiTextEntryMenu.getId().getTextEntryMenu().trim());

					}
				}else{
					if(QiCommonUtil.isMandatoryFieldEmpty(getDialog().getReasonForChangeTextArea())){
						displayErrorMessage("Mandatory field is empty", "Please enter Reason for change");
						return;
					}
					getModel().updateTextEntryMenu(qiTextEntryMenu);
				}
				
				//call to prepare and insert audit data
				if(null!=qiTextEntryMenuCloned){
					AuditLoggerUtil.logAuditInfo(qiTextEntryMenuCloned, qiTextEntryMenu, getDialog().getReasonForChangeTextArea().getText(), getDialog().getScreenName(),getUserId());
				}
				Stage stage = (Stage) updateBtn.getScene().getWindow();
				stage.close();
			}
			catch (Exception e) {
				handleException("An error occured in update entry screen " , "Failed to Update Entry Screen ", e);
			}

		}else{
			displayErrorMessage("Select a Text entry to update", "Select a Text entry to update");
		}


	}
	public void cancelBtnAction(ActionEvent actionEvent){

		LoggedButton cancelBtn = getDialog().getCancelBtn();
		try {
			Stage stage = (Stage) cancelBtn.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occured in during create action ", "Failed to Create Part Text Entry Menu", e);
		}
	}

	@Override
	public void initListeners() {
		validationForTextfield();
		addFieldListener(getDialog().getMenuNameTxtFld(),true);
		addTextAreaListener(getDialog().getMenuDescTxtFld());
		setTextFieldListener(getDialog().getMenuNameTxtFld());
	}
	
	private void validationForTextfield(){
		if(null!=getDialog().getMenuNameTxtFld()){
			getDialog().getMenuNameTxtFld().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(32));
		}if(null!=getDialog().getMenuDescTxtFld()){
			getDialog().getMenuDescTxtFld().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(64));
		}if(null!=getDialog().getReasonForChangeTextArea()){
			getDialog().getReasonForChangeTextArea().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(256));			
		}
		
	}
	


}
