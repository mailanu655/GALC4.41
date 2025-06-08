package com.honda.galc.client.teamleader.qi.controller;

import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import com.honda.galc.client.teamleader.qi.model.ParkingLocationMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.RepeatRowDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.entity.qi.QiRepairAreaRow;
import com.honda.galc.entity.qi.QiRepairAreaRowId;
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

public class RepeatRowDialogController extends QiDialogController<ParkingLocationMaintenanceModel, RepeatRowDialog> {
	private List<QiRepairAreaRow> repairAreaRowlist;
	public RepeatRowDialogController(ParkingLocationMaintenanceModel model, RepeatRowDialog dialog,List<QiRepairAreaRow> repairAreaRowlist) {
		super();
		setModel(model);
		setDialog(dialog);
		this.repairAreaRowlist =repairAreaRowlist;
	}
	/**
	 * This method is used to perform the actions like 'create' ,'update' and 'cancel' on the popup screen
	 */
	@Override
	public void handle(ActionEvent actionEvent) {
			clearDisplayMessage();
			if(actionEvent.getSource().equals(getDialog().getOkBtn())) createRepeatRow(actionEvent);
			else if(actionEvent.getSource().equals(getDialog().getCancelBtn())) cancelBtnAction(actionEvent);
	}
	/**
	 * This method is used to create Part
	 * @param event
	 */
	private void createRepeatRow(ActionEvent event) {
		UpperCaseFieldBean rowData =getDialog().getRowTextField();
		UpperCaseFieldBean repeatData = getDialog().getRepeatTextField();
		UpperCaseFieldBean incrementData = getDialog().getIncrementTextField();
		
		if(QiCommonUtil.isMandatoryFieldEmpty(rowData)){
			displayErrorMessage("Mandatory field is empty", "Please enter Row");
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
		
		if(repairAreaRowlist.size()>0){
			String repairAreaName = repairAreaRowlist.get(0).getId().getRepairAreaName();
			char spaceFillSequence = repairAreaRowlist.get(0).getSpaceFillSequence();
			int row = Integer.parseInt(rowData.getText());
			int repeat = Integer.parseInt(repeatData.getText());
			int increment = Integer.parseInt(incrementData.getText());
			QiRepairAreaRow repairAreaRow;
			QiRepairAreaRowId areaRowId;
			List<QiRepairAreaRow> areaRowsList = new ArrayList<QiRepairAreaRow>();
			for (int i = 0; i <repeat; i++) {
				repairAreaRow = new QiRepairAreaRow();
				areaRowId = new QiRepairAreaRowId();
				areaRowId.setRepairAreaName(repairAreaName);
				areaRowId.setRepairAreaRow(row);
				repairAreaRow.setId(areaRowId);
				repairAreaRow.setSpaceFillSequence(spaceFillSequence);
				repairAreaRow.setCreateUser(getUserId());
				if(getModel().isRepairAreaRowExists(areaRowId)){
					String errorAndLoggerMessage="Failed to add as" + areaRowId.getRepairAreaRow() +" already exists!";
					displayErrorMessage(errorAndLoggerMessage,errorAndLoggerMessage);
					return;
				}else{
					areaRowsList.add(repairAreaRow);
				}
				row=row+increment;
			}
			if(areaRowsList.size()>0){
				getModel().saveAllRepairAreaRow(areaRowsList);
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
		addFieldListener(getDialog().getRowTextField(),false);
		addFieldListener(getDialog().getRepeatTextField(),false);
		addFieldListener(getDialog().getIncrementTextField(),false);
	}
	
	/**
	 * This method is used to perform the validation on TextFields
	 */
	private void validationForTextfield(){
		getDialog().getRowTextField().addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(5));
		getDialog().getRepeatTextField().addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(5));
		getDialog().getIncrementTextField().addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(5));
	}
}
