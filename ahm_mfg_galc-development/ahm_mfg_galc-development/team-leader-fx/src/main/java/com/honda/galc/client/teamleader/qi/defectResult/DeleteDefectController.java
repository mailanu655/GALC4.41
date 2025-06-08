package com.honda.galc.client.teamleader.qi.defectResult;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.controller.QiDialogController;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiDefectResultHist;

/**
 * 
 * <h3>DeleteDefectController Class description</h3>
 * <p>
 * DeleteDefectController class is used delete Defects in the Defect Results table and delete Actual Problem in the Repair table.
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

public class DeleteDefectController extends QiDialogController<DefectResultMaintModel, DeleteDefectDialog>{
	
	public DeleteDefectController(DefectResultMaintModel model,DeleteDefectDialog deleteDefectDialog) {
		super();
		setModel(model);
		setDialog(deleteDefectDialog);
	} 

	/**
	 * This method is used to perform the actions like 'Apply Changes' and 'Cancel'
	 */
	@Override
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource().equals(getDialog().getCancelBtn()))
			cancelBtnAction(actionEvent);
		else if(actionEvent.getSource().equals(getDialog().getChangeBtn()))
			deleteResults(actionEvent);
	}

	/**
	 * This method is used to delete records in DefectResult
	 * @param actionEvent
	 */
	private void deleteResults(ActionEvent actionEvent) {
		if(QiCommonUtil.isMandatoryFieldEmpty(getDialog().getReasonForChange())){
			getDialog().getReasonForChangeErrorMessage().setStyle("-fx-text-background-color: red;");
			getDialog().getReasonForChangeErrorMessage().setText("Please enter Reason for change");
			return;
		}else if(QiCommonUtil.isMandatoryFieldEmpty(getDialog().getCorrectionRequesterTextField().getControl())){
			getDialog().getReasonForChangeErrorMessage().setStyle("-fx-text-background-color: red;");
			getDialog().getReasonForChangeErrorMessage().setText("Please enter Data Correction Requester Name");
			return;
		}
		try {
			List<QiDefectResultHist> defectResultHistList = new ArrayList<QiDefectResultHist>();
			for (QiDefectResult defectResult : getDialog().getCurrentDefectResultTablePane().getTable().getSelectionModel().getSelectedItems()) {
				QiDefectResultHist history=new QiDefectResultHist(defectResult);
				history.setChangeUser(getUserId());
				defectResultHistList.add(history);
				
				getModel().deleteDefectResult(defectResult.getDefectResultId(), 
						getDialog().getReasonForChange().getText(),
						getDialog().getCorrectionRequesterTextField().getText(),
						getUserId());
			}
			getModel().createDefectResultHist(defectResultHistList);
			LoggedButton changeBtn = getDialog().getChangeBtn();
			Stage stage= (Stage) changeBtn.getScene().getWindow();
			stage.close();
			getDialog().setUserOperationMessage("Deleted Successfully");
		} catch (Exception e) {
			handleException("An error occurred at updateResults method ", "Unable to update the DefectResult",  e);
		} 
	}

	@Override
	public void initListeners() {
		getDialog().getCurrentDefectResultTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiDefectResult>() {
			public void changed(ObservableValue<? extends QiDefectResult> arg0,QiDefectResult oldValue, QiDefectResult newValue) {
				if(newValue!=null){
					getDialog().getReasonForChange().setDisable(false);
					getDialog().getCorrectionRequesterTextField().setDisable(false);
					getDialog().getChangeBtn().setDisable(false);
				}
				else{
					getDialog().getReasonForChange().setDisable(true);
					getDialog().getCorrectionRequesterTextField().setDisable(true);
					getDialog().getChangeBtn().setDisable(true);
				}
			}
		});
	}
	
	/**
	 * This method is used to perform the cancel operation
	 * @param actionEvent
	 */
	private void cancelBtnAction(ActionEvent actionEvent) {
		LoggedButton cancelBtn = getDialog().getCancelBtn();
		try {
			Stage stage = (Stage) cancelBtn.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occurred during cancel action,failed to perform cancel action", StringUtils.EMPTY, e);
		}finally{
			actionEvent.consume();
		}

	}
	
}
