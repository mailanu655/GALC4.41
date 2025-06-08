package com.honda.galc.client.teamleader.qi.defectTagging;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.controller.AbstractQiController;
import com.honda.galc.client.teamleader.qi.defectResult.AdvancedSearchDialog;
import com.honda.galc.client.teamleader.qi.defectResult.DefectResultMaintModel;
import com.honda.galc.client.teamleader.qi.defectResult.SearchMaintenancePanel;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dto.qi.QiDefectResultDto;
import com.honda.galc.util.CommonUtil;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Hyperlink;


/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>DefectTaggingMaintenanceController</code> is the controller class for DefectTaggingMaintenance Panel.
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>L&T Infotech</TD>
 * <TD>14/07/2016</TD>
 * <TD>1.0.0</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.0
 * @author L&T Infotech
 */
public class DefectTaggingMaintenanceController extends AbstractQiController<DefectResultMaintModel, DefectTaggingMaintenancePanel> implements EventHandler<ActionEvent> {

	private SearchMaintenancePanel searchMaintenancePanel;
	private AdvancedSearchDialog advancedSearchDialog;
	
	public DefectTaggingMaintenanceController(DefectResultMaintModel model,DefectTaggingMaintenancePanel view) {
		super(model, view);
	}
	
	ChangeListener<String> incidentTypeComboBoxChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> ov,  String old_val, String new_val) { 
			clearDisplayMessage();
			getView().getIncidentUpdateBtn().setDisable(!isFullAccess());
		} 
	};
	
	@Override
	public void initEventHandlers() {
		addIncidentComboBoxListener();
		disableButtons(!isFullAccess());
	}
	
	/**
	 * This method is used to disable buttons
	 * @param isDisable
	 */
	private void disableButtons(boolean isDisable) {
		getView().getAddTaggingBtn().setDisable(isDisable);
		getView().getDeleteTaggingBtn().setDisable(isDisable);
		getView().getIncidentCreateBtn().setDisable(isDisable);
	}
	
	public void handle(ActionEvent actionEvent) {
		if (actionEvent.getSource().equals(getView().getSearchBtn()))
			searchBtnAction();
		else if (actionEvent.getSource().equals(getView().getAdvancedSearchBtn()))
			advanceSearch(actionEvent);
		else if(actionEvent.getSource().equals(getView().getResetButton())){
			clearDisplayMessage();
			getSearchMaintenancePanel().resetData(actionEvent);
			getView().getDefectResultsTablePane().getTable().getItems().clear();
		}
		else if(actionEvent.getSource().equals(getView().getAddTaggingBtn())) 
			addTaggingActionEvent(actionEvent);
		else if(actionEvent.getSource().equals(getView().getDeleteTaggingBtn())) 
			deleteTaggingActionEvent(actionEvent);
		else if(actionEvent.getSource().equals(getView().getIncidentCreateBtn())) 
			createIncidentActionEvent(actionEvent);
		else if(actionEvent.getSource().equals(getView().getIncidentUpdateBtn())) 
			updateIncidentActionEvent(actionEvent);
		if(actionEvent.getSource() instanceof UpperCaseFieldBean){
			clearDisplayMessage();
			searchBtnAction();
		}
	}

	/**
	 * This method is used to perform the Search operation based on selected parameters
	 * @param event
	 */ 
	private void searchBtnAction() {
		clearDisplayMessage();
		StringBuilder searchData = getSearchMaintenancePanel().preparedSearchQuery();
		if(advancedSearchDialog!=null && !StringUtils.isBlank(advancedSearchDialog.getAdvancedSearchedQuery())){
			searchData = searchData.append(advancedSearchDialog.getAdvancedSearchedQuery().toString());
		}
		if (!StringUtils.isBlank(searchData.toString())){
			getView().getDefectResultsTablePane().setData(getModel().findAllDefectResults(StringUtils.trimToEmpty(searchData.toString()),getView().getFilterTextData()));
		}
	}
	
	/**
	 * This method is used to display the AdvanceSearch popUpDialog
	 * @param actionEvent
	 */
	private void advanceSearch(ActionEvent actionEvent){
		if(advancedSearchDialog==null){
			advancedSearchDialog = new AdvancedSearchDialog("Advanced Search", getModel(), getApplicationId(),getSearchMaintenancePanel().getSiteValueLabel().getText(),getSearchMaintenancePanel().getPlantComboBox().getValue(),getSearchMaintenancePanel().getProductTypeComboBox().getValue(),QiConstant.DEFECT_TAGGING);
		}
		advancedSearchDialog.showAndWait();
		String searchQuery=getSearchMaintenancePanel().preparedSearchQuery().toString();
		if(advancedSearchDialog.getAdvancedSearchedQuery()!=null && advancedSearchDialog.getAdvancedSearchedQuery().length()>0){
			searchQuery = searchQuery+advancedSearchDialog.getAdvancedSearchedQuery().toString();
			Hyperlink link = new Hyperlink();
			link.setText("Advanced Search ");
			getView().getAdvancedSearchBtn().setText("");
			getView().getAdvancedSearchBtn().setGraphic(link); 
			getView().getAdvancedSearchBtn().setPrefHeight(35);
			getView().getAdvancedSearchBtn().setFocusTraversable(true);
		}else{
			searchQuery = searchQuery+StringUtils.EMPTY;
		}
		getView().getDefectResultsTablePane().setData(getModel().findAllDefectResults(StringUtils.trimToEmpty(searchQuery),""));
	}
	
	/**
	 * This method is used to display the DefectTagging Dialog for creating Incident
	 * @param actionEvent
	 */
	@SuppressWarnings("unchecked")
	private void createIncidentActionEvent(ActionEvent actionEvent) {
		clearDisplayMessage();
		DefectTaggingDialog defectTaggingDialog = new DefectTaggingDialog("Defect Tagging Creation", getModel(), this.getModel().getApplicationId(),null);
		defectTaggingDialog.showAndWait();
		getView().getAvailableIncidentType().getItems().clear();
		getView().getAvailableIncidentType().getItems().addAll(getModel().findAllQiIncidentTitle());
		getView().getIncidentUpdateBtn().setDisable(true);
		actionEvent.consume();
	}
	
	/**
	 * This method is used to display the DefectTagging Dialog for updating Incident
	 * @param actionEvent
	 */
	@SuppressWarnings("unchecked")
	private void updateIncidentActionEvent(ActionEvent actionEvent) {
		clearDisplayMessage();
		String incidentType= getView().getAvailableIncidentType().getSelectionModel().getSelectedItem().toString();
		DefectTaggingDialog defectTaggingDialog = new DefectTaggingDialog("Defect Tagging Updation", getModel(), this.getModel().getApplicationId(),incidentType);
		defectTaggingDialog.showAndWait();
		getView().getAvailableIncidentType().getItems().clear();
		getView().getAvailableIncidentType().getItems().addAll(getModel().findAllQiIncidentTitle());
		getView().getIncidentUpdateBtn().setDisable(true);
		StringBuilder searchData = getSearchMaintenancePanel().preparedSearchQuery();
		if (searchData!=null)
			getView().getDefectResultsTablePane().setData(getModel().findAllDefectResults(StringUtils.trimToEmpty(searchData.toString()),getView().getFilterTextData()));
		actionEvent.consume();
	}


	/**
	 * This method is used to delete Incident
	 * @param actionEvent
	 */

	private void deleteTaggingActionEvent(ActionEvent actionEvent) {
		clearDisplayMessage();
		List<QiDefectResultDto> selectedList=getView().getDefectResultsTablePane().getSelectedItems();
		String defectResultIdSet="(";
		if (selectedList.size()>0){
			int i=0;
			for(QiDefectResultDto selectedQiDefectResult:selectedList) {
				if(selectedQiDefectResult.getIncidentId()==0)
				{
					getView().setUserOperationErrorMessage("No tagging found to delete for selected record(s).");
					return;
				}
				defectResultIdSet+="'"+selectedQiDefectResult.getDefectResultId()+"'";
				if (i!=selectedList.size()-1){
					defectResultIdSet+=",";
				}
				i++;
			}
			defectResultIdSet+=")";
			getModel().updateIncidentIdToQiDefectResult(null,getModel().getUserId(),defectResultIdSet,CommonUtil.getTimestampNow());
			getModel().updateIncidentIdToQiRepairResult(null,getModel().getUserId(),defectResultIdSet,CommonUtil.getTimestampNow());
			StringBuilder searchData = getSearchMaintenancePanel().preparedSearchQuery();
			if (searchData!=null)
				getView().getDefectResultsTablePane().setData(getModel().findAllDefectResults(StringUtils.trimToEmpty(searchData.toString()),getView().getFilterTextData()));
			getView().setUserOperationMessage("Delete Tagging completed sucessfully");
		}
		else
		{
			getView().setUserOperationErrorMessage("Please select record(s) to delete tagging.");
		}
	}

	/**
	 *  This method is used to add Incident
	 * @param actionEvent
	 */
	private void addTaggingActionEvent(ActionEvent actionEvent) {
		clearDisplayMessage();
		List<QiDefectResultDto> selectedList= new ArrayList<QiDefectResultDto>();
		selectedList.addAll(getView().getDefectResultsTablePane().getSelectedItems());
		if (selectedList.size()>0 && getView().getAvailableIncidentType().getValue()!=null){
			String incidentTypeData=getView().getAvailableIncidentType().getValue().toString();
			String incidentType=incidentTypeData.contains("(")?incidentTypeData.split("\\(")[0].trim():incidentTypeData;
			String incidentDate=incidentTypeData.contains("(")?incidentTypeData.split("\\(")[1].split("\\)")[0].trim():"";
			String defectResultIdSet="(";
			int qiIncidentId=getModel().findByIncidentTitleAndDate(incidentType, incidentDate).getIncidentId();
			int i=0;
			for(QiDefectResultDto selectedQiDefectResult:selectedList) {
				defectResultIdSet+="'"+selectedQiDefectResult.getDefectResultId()+"'";
				if (i!=selectedList.size()-1){
					defectResultIdSet+=",";
				}
				i++;
			}
			defectResultIdSet+=")";
			getModel().updateIncidentIdToQiDefectResult(qiIncidentId,getModel().getUserId(),defectResultIdSet, CommonUtil.getTimestampNow());
			getModel().updateIncidentIdToQiRepairResult(qiIncidentId,getModel().getUserId(),defectResultIdSet,CommonUtil.getTimestampNow());
			StringBuilder searchData = getSearchMaintenancePanel().preparedSearchQuery();
			if (searchData!=null)
				getView().getDefectResultsTablePane().setData(getModel().findAllDefectResults(StringUtils.trimToEmpty(searchData.toString()),getView().getFilterTextData()));
			getView().setUserOperationMessage("Add Tagging completed sucessfully");
		}
		else
		{
			getView().setUserOperationErrorMessage("Please select defectTag and record to add tagging.");
		}
		
	}
	
	@Override
	public void addContextMenuItems() {

	}
	
	@SuppressWarnings("unchecked")
	private void addIncidentComboBoxListener() {
		getView().getAvailableIncidentType().getSelectionModel().selectedItemProperty().addListener(incidentTypeComboBoxChangeListener);
	}
	
	public SearchMaintenancePanel getSearchMaintenancePanel() {
		return searchMaintenancePanel;
	}
	public void setSearchMaintenancePanel(
			SearchMaintenancePanel searchMaintenancePanel) {
		this.searchMaintenancePanel = searchMaintenancePanel;
	}
}
