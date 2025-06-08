package com.honda.galc.client.teamleader.qi.defectResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.teamleader.qi.controller.QiDialogController;
import com.honda.galc.client.ui.ElevatedLoginDialog;
import com.honda.galc.client.ui.ElevatedLoginResult;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiDefectResultHist;
import com.honda.galc.entity.qi.QiRepairResult;
import com.honda.galc.entity.qi.QiRepairResultHist;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.service.property.PropertyService;

import javafx.event.ActionEvent;
import javafx.stage.Stage;


public class UpdateDefectController extends QiDialogController<DefectResultMaintModel, UpdateDefectDataReviewDialog>{
	private List<QiDefectResult> selectedDefectResultList;
	private List<QiRepairResult> selectedRepairResultList;
	private String typeOfChange;
	private DefectResultMaintModel model;
	private LocalAttributeMaintDialog localAttributeMaintDialog;
	public UpdateDefectController(DefectResultMaintModel model, UpdateDefectDataReviewDialog elevatedDefectDataReviewDialog, List<QiRepairResult> selectedRepairResultList,String typeOfChange,List<QiDefectResult> selectedDefectResultList, LocalAttributeMaintDialog localAttributeMaintDialog) {
		super();
		this.model=model;
		setDialog(elevatedDefectDataReviewDialog);
		this.typeOfChange = typeOfChange;
		this.selectedDefectResultList=selectedDefectResultList;
		this.selectedRepairResultList = selectedRepairResultList;
		this.localAttributeMaintDialog=localAttributeMaintDialog;
	}
	
	public DefectResultMaintModel getModel() {
		return model;
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource().equals(getDialog().getCancelButton()))
			cancelBtnAction(actionEvent);
		else if(actionEvent.getSource().equals(getDialog().getChangeButton()))
			updateResults(actionEvent);
	}

	/**
	 * This method is used to update records in DefectResult and 
	 * @param actionEvent
	 */
	private void updateResults(ActionEvent actionEvent) {
		if(QiCommonUtil.isMandatoryFieldEmpty(getDialog().getReasonForChange())){
			getDialog().getReasonForChangeErrorMessage().setStyle("-fx-text-background-color: red;");
			getDialog().getReasonForChangeErrorMessage().setText("Please enter Reason for Change.");
			return;
		}else if(QiCommonUtil.isMandatoryFieldEmpty(getDialog().getApproverTextField().getControl())){
			getDialog().getReasonForChangeErrorMessage().setStyle("-fx-text-background-color: red;");
			getDialog().getReasonForChangeErrorMessage().setText("Please enter Correction Requester Name.");
			return;
		}
		try {
			if(typeOfChange.equals(QiConstant.DEFECT_TYPE)){
				for(QiDefectResult qi : selectedDefectResultList) {
					
					//1 - Update in history defect_type - QI_DEFECT_RESULT_HIST_TBX
					List<QiDefectResult> currentDefectResultList = new ArrayList<QiDefectResult>();
					currentDefectResultList.add(qi);
					updateDefectResultHistory(currentDefectResultList);
					
					qi.setReasonForChange(getDialog().getReasonForChangeText());
					qi.setCorrectionRequestBy(getDialog().getApproverText());
					
					//2 - Setting GDP DEFECTS and TRPU DEFECTS value.
					short gdpValue = (short) ((getDialog().getGdpDefects().equals("Yes")) ? 1 : 0);
					short trpuValue = (short) ((getDialog().getTrpuDefects().equals("Yes")) ? 1 : 0);
					qi.setGdpDefect(gdpValue);
					qi.setTrpuDefect(trpuValue);
					
					// Adding into the database .
					getModel().updateDefectResult(qi);
				}
			}else if(typeOfChange.equals(QiConstant.REPAIR_TYPE)){
				for(QiRepairResult qi : selectedRepairResultList) {
					//1 - Update in history repair_type - QI_REPAIR_RESULT_HIST_TBX
					List<QiRepairResult> currentDefectResultList = new ArrayList<QiRepairResult>();
					currentDefectResultList.add(qi);
					updateRepairResultHistory(currentDefectResultList);
					
					
					qi.setReasonForChange(getDialog().getReasonForChangeText());
					qi.setCorrectionRequestBy(getDialog().getApproverText());
					
					//2 - Setting GDP DEFECTS and TRPU DEFECTS value.
					short gdpValue = (short) ((getDialog().getGdpDefects().equals("Yes")) ? 1 : 0);
					short trpuValue = (short) ((getDialog().getTrpuDefects().equals("Yes")) ? 1 : 0);
					qi.setGdpDefect(gdpValue);
					qi.setTrpuDefect(trpuValue);
					
					//3 - Adding into the database .
					getModel().updateRepairResult(qi);	
				}
			}	
			LoggedButton changeBtn = getDialog().getChangeButton();
			Stage stage= (Stage) changeBtn.getScene().getWindow();
			LoggedButton localAttributeMaintDialogChangeBtn = localAttributeMaintDialog.getChangeBtn();
			Stage localAttributeMaintDialogstage = (Stage) localAttributeMaintDialogChangeBtn.getScene().getWindow();
			stage.close();
			localAttributeMaintDialogstage.close();
			getDialog().setUserOperationMessage("Updated Successfully");
		} catch (Exception e) {
			handleException("An error occurred at updateResults method ", "Unable to update the DefectResult",  e);
		}
	}	
	
	public void updateDefectResultHistory(List<QiDefectResult> updatedDefectResultList) {
		try {
			List<QiDefectResultHist> defectResultHistList = new ArrayList<QiDefectResultHist>();
			for (QiDefectResult defectResult : updatedDefectResultList) {
				QiDefectResultHist history=new QiDefectResultHist(defectResult);
				history.setChangeUser(getUserId());
				defectResultHistList.add(history);			
			}
			getModel().createDefectResultHist(defectResultHistList);
			getDialog().setUserOperationMessage("Added in History Table Successfully");
		} catch (Exception e) {
			handleException("An error occurred at updateDefectResultHistory method ", "Unable to update the DefectResult",  e);
		}
	}
	
	public void updateRepairResultHistory(List<QiRepairResult> updatedDefectResultList) {
		try {
			List<QiRepairResultHist> repairResultHistList = new ArrayList<QiRepairResultHist>();
			for (QiRepairResult repairResult : updatedDefectResultList) {
				QiRepairResultHist history=new QiRepairResultHist(repairResult);
				history.setChangeUser(getUserId());
				repairResultHistList.add(history);
			}
			getModel().createRepairResultHist(repairResultHistList);
			getDialog().setUserOperationMessage("Added in History Table Successfully");
		} catch (Exception e) {
			handleException("An error occurred at updateRepairResultHistory method ", "Unable to update the DefectResult",  e);
		}
	}
	
	@Override
	public void initListeners() {
		
	}
	/**
	 * This method is used to perform the cancel operation
	 * @param actionEvent
	 */
	private void cancelBtnAction(ActionEvent actionEvent) {
		LoggedButton cancelButton = getDialog().getCancelButton();
		try {
			Stage stage = (Stage) cancelButton.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occured in during cancel action.", "Failed to perform cancel action.", e);
		}finally{
			actionEvent.consume();
		}
	}
}
