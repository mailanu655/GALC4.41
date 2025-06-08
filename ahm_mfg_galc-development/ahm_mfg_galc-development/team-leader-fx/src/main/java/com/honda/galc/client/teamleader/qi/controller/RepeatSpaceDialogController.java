package com.honda.galc.client.teamleader.qi.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.honda.galc.client.teamleader.qi.model.ParkingLocationMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.RepeatSpaceDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.entity.qi.QiRepairAreaSpace;
import com.honda.galc.entity.qi.QiRepairAreaSpaceId;

import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
/**
 * 
 * <h3>RepeatRowDialogController Class description</h3>
 * <p>
 * RepeatRowDialogController is used to perform the actions like 'Repeat Row' etc.
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

public class RepeatSpaceDialogController extends QiDialogController<ParkingLocationMaintenanceModel, RepeatSpaceDialog> {
	private List<QiRepairAreaSpace> repairAreaSpacelist;
	public RepeatSpaceDialogController(ParkingLocationMaintenanceModel model, RepeatSpaceDialog dialog,List<QiRepairAreaSpace> repairAreaRowlist) {
		super();
		setModel(model);
		setDialog(dialog);
		this.repairAreaSpacelist =repairAreaRowlist;
	}
	/**
	 * This method is used to perform the actions like 'create' ,'update' and 'cancel' on the popup screen
	 */
	@Override
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource() instanceof LoggedButton) {
			clearDisplayMessage();
			if(actionEvent.getSource().equals(getDialog().getOkBtn())) createRepeatRow(actionEvent);
			else if(actionEvent.getSource().equals(getDialog().getCancelBtn())) cancelBtnAction(actionEvent);
			
		} 		
	}
	/**
	 * This method is used to create Part
	 * @param event
	 */
	private void createRepeatRow(ActionEvent event) {
		UpperCaseFieldBean spaceData =getDialog().getSpaceTextField();
		UpperCaseFieldBean repeatData = getDialog().getRepeatTextField();
		UpperCaseFieldBean incrementData = getDialog().getIncrementTextField();
		
		if(QiCommonUtil.isMandatoryFieldEmpty(spaceData)){
			displayErrorMessage("Mandatory field is empty", "Please enter Space");
			return;
		}
		if(QiCommonUtil.isMandatoryFieldEmpty(repeatData)){
			displayErrorMessage("Mandatory field is empty", "Please enter # of times to be repeat");
			return;
		}
		if(QiCommonUtil.isMandatoryFieldEmpty(incrementData)){
			displayErrorMessage("Mandatory field is empty", "Please enter Increment");
			return;
		}
		
		if(repairAreaSpacelist.size()>0){
			String repairAreaName = repairAreaSpacelist.get(0).getId().getRepairAreaName();
			int repairAreaRow = repairAreaSpacelist.get(0).getId().getRepairArearRow();
			int space = Integer.parseInt(spaceData.getText());
			int repeat = Integer.parseInt(repeatData.getText());
			int increment = Integer.parseInt(incrementData.getText());
			QiRepairAreaSpace repairAreaSpace;
			QiRepairAreaSpaceId areaRowId;
			List<QiRepairAreaSpace> areaRowsList = new ArrayList<QiRepairAreaSpace>();
			for (int i = 0; i <repeat; i++) {
				repairAreaSpace = new QiRepairAreaSpace();
				areaRowId = new QiRepairAreaSpaceId();
				areaRowId.setRepairAreaName(repairAreaName);
				areaRowId.setRepairArearRow(repairAreaRow);
				areaRowId.setRepairArearSpace(space);
				repairAreaSpace.setId(areaRowId);
				repairAreaSpace.setActive(repairAreaSpacelist.get(0).isActive());
				repairAreaSpace.setAppCreateTimestamp(new Date());
				repairAreaSpace.setCreateUser(getUserId());
				if(getModel().isRepairSpaceExists(repairAreaSpace)){
					String errorAndLoggerMessage="Failed to add as " + repairAreaSpace.getId().getRepairArearSpace() +" already exists!";
					displayErrorMessage(errorAndLoggerMessage,errorAndLoggerMessage);
					return;
				}else{
					areaRowsList.add(repairAreaSpace);
				}
				space=space+increment;
			}
			if(areaRowsList.size()>0){
				getModel().saveAllRepairAreaSpace(areaRowsList);
			}
			
			Stage stage= (Stage) getDialog().getOkBtn().getScene().getWindow();
			stage.close();
		}
	}
	
	/**
	 * This method is used to close the Popup screen clicking 'cancel' button
	 * @param event
	 */
	private void cancelBtnAction(ActionEvent event) {
		LoggedButton cancelBtn = getDialog().getCancelBtn();
		try {
			Stage stage = (Stage) cancelBtn.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occured in during cancel action ", "Failed to perform cancel action",  e);
		}
	}
	/**
	 * This method is used to perform the validation on controls
	 */
	@Override
	public void initListeners() {
		validationForTextfield();
		addFieldListener(getDialog().getSpaceTextField(),false);
		addFieldListener(getDialog().getRepeatTextField(),false);
		addFieldListener(getDialog().getIncrementTextField(),false);
	}
	
	/**
	 * This method is used to perform the validation on TextFields
	 */
	private void validationForTextfield(){
		getDialog().getSpaceTextField().addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(5));
		getDialog().getRepeatTextField().addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(5));
		getDialog().getIncrementTextField().addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(5));
	}
}
