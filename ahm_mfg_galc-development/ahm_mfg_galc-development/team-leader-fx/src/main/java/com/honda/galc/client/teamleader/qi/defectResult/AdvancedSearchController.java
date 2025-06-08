package com.honda.galc.client.teamleader.qi.defectResult;



import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.controller.QiDialogController;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.QiReportable;
/**
 * 
 * <h3>AdvancedSearchController Class description</h3>
 * <p>
 * AdvancedSearchController is used to perform the advance search based on the components
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

public class AdvancedSearchController extends QiDialogController<DefectResultMaintModel, AdvancedSearchDialog>{
	
	public AdvancedSearchController(DefectResultMaintModel model, AdvancedSearchDialog dialog) {
		super();
		setModel(model);
		setDialog(dialog);
	}
	
	/**
	 * This method is used to handle action events .
	 */
	@Override
	public void handle(ActionEvent actionEvent) {
		if (actionEvent.getSource() instanceof LoggedButton) {
			LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();
			if(QiConstant.OK.equalsIgnoreCase(loggedButton.getText())) okButtonAction(actionEvent);
			else if(QiConstant.CANCEL.equals(loggedButton.getText())) cancelButtonAction(actionEvent);
			else if(QiConstant.RESET.equals(loggedButton.getText())) getDialog().resetBtnAction(actionEvent);
		}
	}

	@Override
	public void initListeners() {
		addLimitFieldListener(getDialog().getAssemblySeqTextField().getControl(),99999);
		addLimitFieldListener(getDialog().getThruTextField().getControl(),99999);
		if(!getDialog().getControllerType().equalsIgnoreCase(QiConstant.DATA_CORRECTION))
	     	addEntryScreenListener();
	}
	
	/**
	 * This method is used to close the Popup screen clicking 'ok' button
	 * @param event
	 */
	private void okButtonAction(ActionEvent event) {
		try {
			
			StringBuilder searchData= new StringBuilder();
			
			String writeUpDeptValue=StringUtils.trimToEmpty(getDialog().getWriteUpDepartmentComboBox().getControl().getValue()); 
			String entryScreenValue =StringUtils. trimToEmpty(getDialog().getEntryScreenComboBx().getControl().getValue()); 
			String processPointValue=StringUtils.trimToEmpty (getDialog().getNodeIdComboBox().getControl().getValue()); 
			String reportableValue = StringUtils.trimToEmpty(getDialog().getReportableComboBox().getControl().getValue()); 
			String defectTagValue = StringUtils.trimToEmpty(getDialog().getDefectTagComboBox().getControl().getValue()); 
            String trackingCodeValue = StringUtils.trimToEmpty(getDialog().getLocalThemeComboBox().getControl().getValue()); 
            String currentStatusValue = StringUtils.trimToEmpty(getDialog().getCurrentStatusComboBox().getControl().getValue()); 
            String productIdStartValue =StringUtils.trimToEmpty (getDialog().getProductIdStartTextField().getText()); 
            String productIdEndValue = StringUtils.trimToEmpty(getDialog().getProductIdEndTextField().getText()); 
            String assembleSeq=StringUtils.trimToEmpty (getDialog().getAssemblySeqTextField().getText()); 
            String thruValue =StringUtils.trimToEmpty(getDialog().getThruTextField().getText()); 

			if(writeUpDeptValue.length()>0)searchData.append(" AND WRITE_UP_DEPARTMENT = '"+writeUpDeptValue+"'");
			if(entryScreenValue.length()>0)searchData.append(" AND ENTRY_SCREEN = '"+entryScreenValue+"'");
			if(processPointValue.length()>0)searchData.append(" AND APPLICATION_ID ='"+processPointValue+"'");
			if(reportableValue.length()>0){
				searchData.append(" AND REPORTABLE ="+QiReportable.getId(reportableValue)+"");
			}
			String incidentType=defectTagValue.contains("(")?defectTagValue.split("\\(")[0].trim():defectTagValue;
			String incidentDate=defectTagValue.contains("(")?defectTagValue.split("\\(")[1].split("\\)")[0].trim():defectTagValue;
			if(defectTagValue.length()>0)searchData.append(" AND  INCIDENT_TITLE ='"+incidentType+"'" +" AND INCIDENT_DATE like '"+"%"+incidentDate+"%"+"'" );
			if(trackingCodeValue.length()>0)searchData.append(" AND LOCAL_THEME ='"+trackingCodeValue+"'" );
			if(currentStatusValue.length()>0){
				int defectStatus=-1;
				if(currentStatusValue.equals(DefectStatus.FIXED.getName()))
					defectStatus=DefectStatus.FIXED.getId();
				else if(currentStatusValue.equals(DefectStatus.NOT_FIXED.getName()))
					defectStatus=DefectStatus.NOT_FIXED.getId();
				else if(currentStatusValue.equals(DefectStatus.NON_REPAIRABLE.getName()))
					defectStatus=DefectStatus.NON_REPAIRABLE.getId();
				searchData.append(" AND CURRENT_DEFECT_STATUS = "+defectStatus+"");
			}
			if(productIdStartValue.length()>0 || productIdEndValue.length()>0){
				if(productIdStartValue.length()>0 && productIdEndValue.length()>0){
					searchData.append(" AND PRODUCT_ID between '"+productIdStartValue+"'"+ " AND '"+productIdEndValue+"'");
				}else if(productIdStartValue.length()>0){
					searchData.append(" AND PRODUCT_ID = '"+productIdStartValue+"'");
				}else if(productIdEndValue.length()>0){
					searchData.append(" AND PRODUCT_ID = '"+productIdEndValue+"'");
				}
			}
			if(assembleSeq.length()>0 || thruValue.length()>0){
				if(assembleSeq.length()>0 && thruValue.length()>0)searchData.append(" AND AF_ON_SEQUENCE_NUMBER between " +assembleSeq + " AND " + thruValue);
				else if(thruValue.length()>0)searchData.append(" and AF_ON_SEQUENCE_NUMBER =" +thruValue);
				else if(assembleSeq.length()>0)searchData.append(" and AF_ON_SEQUENCE_NUMBER =" +assembleSeq);
			}
			getDialog().setAdvancedSearchedQuery(searchData.toString());
			Stage stage = (Stage) getDialog().getOkButton().getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occurred  during cancel action ", "Failed to perform Ok action",  e);
		}
		
	}
	
	
	/**
	 * This method id used to perform Cancel operation
	 * @param actionEvent
	 */
	private void cancelButtonAction(ActionEvent actionEvent) {
		LoggedButton cancelBtn = getDialog().getCancelButton();
		try {
			Stage stage = (Stage) cancelBtn.getScene().getWindow();
			stage.close(); 
		} catch (Exception e) {
			handleException("An error occured in during cancel action Failed to perform cancel action", StringUtils.EMPTY, e);
		}
	}
	
	/**
	 * This method is event listener for plantComboBox
	 */
	private void addEntryScreenListener() {
		getDialog().getEntryScreenComboBx().getControl().valueProperty().addListener(entryScreenComboBoxChangeListener);
	}

	ChangeListener<String> entryScreenComboBoxChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> ov,  String old_val, String new_val) { 
			loadProcessPoint(new_val);
		}
	};
	
	private void loadProcessPoint(String entryScreen){
		getDialog().getNodeIdComboBox().getControl().getItems().clear();
		getDialog().getNodeIdComboBox().getControl().getItems().addAll(getModel().findAllProcessPointByEntryScreen(entryScreen));
	}
}
