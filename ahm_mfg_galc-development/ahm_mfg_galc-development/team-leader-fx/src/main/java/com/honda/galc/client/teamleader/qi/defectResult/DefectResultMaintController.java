package com.honda.galc.client.teamleader.qi.defectResult;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.controller.AbstractQiController;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.qi.QiDefectIqsScore;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiRepairResult;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.property.PropertyService;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Toggle;



/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>DefectResultMaintController</code> is the controller class for DefectResultMaintPanel Panel.
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

public class DefectResultMaintController extends AbstractQiController<DefectResultMaintModel, DefectResultMaintPanel> implements EventHandler<ActionEvent> {

	private SearchMaintenancePanel searchMaintenancePanel;
	private AdvancedSearchDialog advancedSearchDialog;
	
	public DefectResultMaintController(DefectResultMaintModel model,DefectResultMaintPanel view) {
		super(model, view);
	}

	/**
	 * This method is used to perform the actions -- Search ,AdvancedSearch and Submit
	 */
	public void handle(ActionEvent actionEvent) {
		if (actionEvent.getSource().equals(getView().getSearchBtn())) {
			reload();
			clearDisplayMessage();
		}
		
		else if (actionEvent.getSource().equals(getView().getAdvancedSearchBtn()))
			advanceSearch(actionEvent);
		else if(actionEvent.getSource().equals(getView().getResetButton())){
			clearDisplayMessage();
			
			if(searchMaintenancePanel.getEntryModelComboBox().isVisible()) {
				getView().getSearchBtn().setDisable(true);
			}
			getView().getSubmitBtn().setDisable(true);
			getSearchMaintenancePanel().resetData(actionEvent);
			getView().getSearchedDefectListView().getItems().clear();
		}
		else if(actionEvent.getSource().equals(getView().getSubmitBtn())) 
			submitAction(actionEvent);
		if(actionEvent.getSource() instanceof UpperCaseFieldBean){
			clearDisplayMessage();
			reload();
		}
	}
	
	/**
	 * This method is used to display the popUpDialog allowing user to update data
	 * @param actionEvent
	 */
	private void submitAction(ActionEvent actionEvent){
		clearDisplayMessage();
		List<String> selectedDefectResultViewList = FXCollections.observableArrayList(getView().getSearchedDefectListView().getSelectionModel().getSelectedItems());
		if(selectedDefectResultViewList.size()==0){
			getView().setUserOperationMessage("Please select a defect");
			return;
		}
		if(isDefectRelatedRadioButtonSelected()){
			displayDefectResultDialog(selectedDefectResultViewList);
			getView().getSubmitBtn().setDisable(true);
		}else if(isRepairRelatedRadioButtonSelected() ){
			displayRepairResultDialog(selectedDefectResultViewList);
			getView().getSubmitBtn().setDisable(true);

		}
		
	}
	
	
	private void entryModelAction(ActionEvent actionEvent) {
		clearDisplayMessage();
		List<String> selectedRadioBtnList = FXCollections.observableArrayList(getView().getSearchedDefectListView().getSelectionModel().getSelectedItem());
		if(getSearchMaintenancePanel().getChangeActualProblemRadioBtn().isSelected() || getSearchMaintenancePanel().getChangeDefectRadioBtn().isSelected()) {
			getView().setUserOperationMessage("Must select entry model before search will be enabled.");
		}
		else {
			clearDisplayMessage();
		}
		if(isDefectRelatedRadioButtonSelected()){
			displayDefectResultDialog(selectedRadioBtnList);
		}else if(isRepairRelatedRadioButtonSelected() ){
			displayRepairResultDialog(selectedRadioBtnList);
		}
		
	}
	/**
	 * This method is used to display the RepairResultDialog based on the selection of radio button
	 * @param selectedDefectResultViewList
	 */
	private void displayRepairResultDialog(List<String> selectedDefectResultViewList) {
		List<String> selectedPartDefectDescList = new ArrayList<String>();
		List<QiRepairResult> selectedRepairResultList = new ArrayList<QiRepairResult>();
		StringBuilder searchData = getSearchMaintenancePanel().preparedSearchQuery();
		int defectDataRange= PropertyService.getPropertyBean(QiPropertyBean.class).getDataCorrectionChangeDays();
		int searchResultLimit= PropertyService.getPropertyBean(QiPropertyBean.class).getDataCorrectionSearchListLimit();
		if(advancedSearchDialog!=null && !StringUtils.isBlank(advancedSearchDialog.getAdvancedSearchedQuery())){
			searchData = searchData.append(advancedSearchDialog.getAdvancedSearchedQuery().toString());
		}
		for (QiRepairResult repairResult : getModel().findAllSearchResultFromRepairResult(""+searchData,getView().getDefectFilterTextField().getText().trim(),defectDataRange,searchResultLimit)) {
			if(selectedDefectResultViewList.contains("("+repairResult.getDefectResultId()+")"+repairResult.getPartDefectDesc())){
				selectedRepairResultList.add(repairResult);
				selectedPartDefectDescList.add(repairResult.getPartDefectDesc());
			}
		}
		if(getSearchMaintenancePanel().getUpdateActualProblemAttributeRadioBtn().isSelected() ){
			LocalAttributeMaintDialog updateAttributeDialog = new LocalAttributeMaintDialog("Update Attribute Dialog", getApplicationId(),getModel(),
					null,QiConstant.REPAIR_TYPE,selectedRepairResultList);
			updateAttributeDialog.showAndWait();
			reload();
		}else if(getSearchMaintenancePanel().getChangeActualProblemRadioBtn().isSelected()) {
		
			clearDisplayMessage();
			UpdatePdcDialog changeDefectDialog = new UpdatePdcDialog("Change Defect Dialog", getModel(),getApplicationId(), getSearchMaintenancePanel().getProductTypeComboBox().getValue(), selectedPartDefectDescList,QiConstant.REPAIR_TYPE,null,selectedRepairResultList,getSearchMaintenancePanel().getEntryModelComboBox().getValue());
			changeDefectDialog.showAndWait();
			reload();
		}
	}

	/**
	 * This method is used to display the DefectResultDialog based on the selection of radio button
	 * @param selectedDefectResultViewList
	 * @param partDefectDescList
	 */
	private void displayDefectResultDialog( List<String> selectedDefectResultViewList) {
		List<String> selectedPartDefectDescList = new ArrayList<String>();
		List<QiDefectResult> selectedDefectResultList = new ArrayList<QiDefectResult>();
		StringBuilder searchData = getSearchMaintenancePanel().preparedSearchQuery();
		int defectDataRange= PropertyService.getPropertyBean(QiPropertyBean.class).getDataCorrectionChangeDays();
		int searchResultLimit= PropertyService.getPropertyBean(QiPropertyBean.class).getDataCorrectionSearchListLimit();
		if(advancedSearchDialog!=null && !StringUtils.isBlank(advancedSearchDialog.getAdvancedSearchedQuery())){
			searchData = searchData.append(advancedSearchDialog.getAdvancedSearchedQuery().toString());
		}
		for (QiDefectResult defectResult : getModel().findAllSearchResultFromDefectResult(""+searchData,getView().getDefectFilterTextField().getText().trim(),defectDataRange,searchResultLimit)) {
			if(selectedDefectResultViewList.contains(defectResult.getPartDefectDescWithDefectResultId())){
				QiDefectIqsScore iqs = getModel().findQiDefectIqsScore(defectResult.getDefectResultId());
				if(iqs != null) {
					defectResult.setIqsScore(iqs.getIqsScore());
				}
				selectedDefectResultList.add(defectResult);
				selectedPartDefectDescList.add(defectResult.getPartDefectDesc());
			}
		}
		if (getSearchMaintenancePanel().getUpdateAttributeRadioBtn().isSelected()){
			LocalAttributeMaintDialog updateAttributeDialog = new LocalAttributeMaintDialog("Update Attribute Dialog", getApplicationId(),getModel(),selectedDefectResultList,
					QiConstant.DEFECT_TYPE,null);
			updateAttributeDialog.showAndWait();
			reload();
		}else if(getSearchMaintenancePanel().getChangeDefectRadioBtn().isSelected()){

			clearDisplayMessage();
			UpdatePdcDialog changeDefectDialog = new UpdatePdcDialog("Change Defect Dialog", getModel(),getApplicationId(), getSearchMaintenancePanel().getProductTypeComboBox().getValue(), selectedPartDefectDescList,QiConstant.DEFECT_TYPE,selectedDefectResultList,null,getSearchMaintenancePanel().getEntryModelComboBox().getValue());
			changeDefectDialog.showAndWait();
			reload();
		}else if(getSearchMaintenancePanel().getDeleteDefectRadioBtn().isSelected()){
			DeleteDefectDialog deleteDefectDialog = new DeleteDefectDialog("Delete Actual Problem", getModel(),getApplicationId(), selectedDefectResultList);
			deleteDefectDialog.showAndWait();
			reload();
		}
	}
	
	/**
	 * This method is used to display the AdvanceSearch popUpDialog based on ChangeType
	 * @param actionEvent
	 */
	private void advanceSearch(ActionEvent actionEvent){
		getView().getSearchedDefectListView().getItems().clear();
		if(advancedSearchDialog==null){
			advancedSearchDialog= new AdvancedSearchDialog("Advanced Search", getModel(), getApplicationId(),
					getSearchMaintenancePanel().getSiteValueLabel().getText(),getSearchMaintenancePanel().getPlantComboBox().getValue(),
					getSearchMaintenancePanel().getProductTypeComboBox().getValue(),QiConstant.DATA_CORRECTION);
		}
		String searchQuery=getSearchMaintenancePanel().preparedSearchQuery().toString();
		advancedSearchDialog.showAndWait();
		if(advancedSearchDialog.getAdvancedSearchedQuery()!=null && advancedSearchDialog.getAdvancedSearchedQuery().length()>0){
			searchQuery = searchQuery+advancedSearchDialog.getAdvancedSearchedQuery().toString();
			getView().changeButtonToHyperlink();
		}else{
			searchQuery = searchQuery+StringUtils.EMPTY;
			getView().changeHyperlinkToButton();
		}

		int defectDataRange= PropertyService.getPropertyBean(QiPropertyBean.class).getDataCorrectionChangeDays();
		
		int searchResultLimit= PropertyService.getPropertyBean(QiPropertyBean.class).getDataCorrectionSearchListLimit();
		
		if (isDefectRelatedRadioButtonSelected()) {
			List<QiDefectResult> advancedSearchedDefectResultList = getModel().findAllSearchResultFromDefectResult(searchQuery,getView().getDefectFilterTextField().getText().trim(),defectDataRange,searchResultLimit);
			for (QiDefectResult defectResultObj : advancedSearchedDefectResultList) {
				getView().getSearchedDefectListView().getItems().add(defectResultObj.getPartDefectDescWithDefectResultId());
			}
		}else if(isRepairRelatedRadioButtonSelected() ){
			List<QiRepairResult> advanceSearchedRepairResultList = getModel().findAllSearchResultFromRepairResult(searchQuery,getView().getDefectFilterTextField().getText().trim(),defectDataRange,searchResultLimit);
			for (QiRepairResult repairResultObj : advanceSearchedRepairResultList) {
					String partDefectDesc = repairResultObj.getPartDefectDescWithDefectResultId();
					getView().getSearchedDefectListView().getItems().add(partDefectDesc);
			}
		}
	}
	
	@Override
	public void addContextMenuItems() {
	}
	
	/**
	 *  This method is used to add listener on main panel table.
	 */
	@Override
	public void initEventHandlers() {
		
		getSearchMaintenancePanel().getGroup().selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
			@Override
			public void changed(ObservableValue<? extends Toggle> arg0,Toggle arg1, Toggle arg2) {
				if (getSearchMaintenancePanel().getGroup().getSelectedToggle() != null) {
					getView().getSearchedDefectListView().getItems().clear();
		         }				
			}
			
			
		    
		});
		
	}
	
	private void reload() {
		clearDisplayMessage();
		StringBuilder searchData = getSearchMaintenancePanel().preparedSearchQuery();
		if(advancedSearchDialog!=null && !StringUtils.isBlank(advancedSearchDialog.getAdvancedSearchedQuery())){
			searchData = searchData.append(advancedSearchDialog.getAdvancedSearchedQuery().toString());
		}
		int defectDataRange= PropertyService.getPropertyBean(QiPropertyBean.class).getDataCorrectionChangeDays();
		int searchResultLimit= PropertyService.getPropertyBean(QiPropertyBean.class).getDataCorrectionSearchListLimit();
		getView().getSearchedDefectListView().getItems().clear();
		if(isDefectRelatedRadioButtonSelected()){
				for (QiDefectResult defectResultObj : getModel().findAllSearchResultFromDefectResult(""+searchData,getView().getDefectFilterTextField().getText().trim(),defectDataRange,searchResultLimit)) {
					getView().getSearchedDefectListView().getItems().add(defectResultObj.getPartDefectDescWithDefectResultId());
				}
		}else if(isRepairRelatedRadioButtonSelected() ){
			for (QiRepairResult repairResultObj : getModel().findAllSearchResultFromRepairResult(""+searchData,getView().getDefectFilterTextField().getText().trim(),defectDataRange,searchResultLimit)) {
					String partDefectDesc = repairResultObj.getPartDefectDescWithDefectResultId();
					getView().getSearchedDefectListView().getItems().add(partDefectDesc);
			}
		}
	}
	
	private boolean isDefectRelatedRadioButtonSelected() {
		return getSearchMaintenancePanel().getUpdateAttributeRadioBtn().isSelected() || getSearchMaintenancePanel().getChangeDefectRadioBtn().isSelected() || getSearchMaintenancePanel().getDeleteDefectRadioBtn().isSelected();
	}
	
	private boolean isChangeRelatedRadioButtonSelected() {
		return getSearchMaintenancePanel().getChangeDefectRadioBtn().isSelected()|| getSearchMaintenancePanel().getChangeActualProblemRadioBtn().isSelected();
	}
	
	private boolean isRepairRelatedRadioButtonSelected() {
		return getSearchMaintenancePanel().getUpdateActualProblemAttributeRadioBtn().isSelected() || getSearchMaintenancePanel().getChangeActualProblemRadioBtn().isSelected();
	}
	
	public SearchMaintenancePanel getSearchMaintenancePanel() {
		return searchMaintenancePanel;
	}

	public void setSearchMaintenancePanel(
			SearchMaintenancePanel searchMaintenancePanel) {
		this.searchMaintenancePanel = searchMaintenancePanel;
	}
}