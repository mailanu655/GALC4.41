package com.honda.galc.client.teamleader.qi.controller;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;

import com.honda.galc.client.teamleader.qi.model.ItemMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.InspectionLocationDialog;
import com.honda.galc.client.teamleader.qi.view.InspectionLocationMaintPanel;
import com.honda.galc.client.teamleader.qi.view.ReasonForChangeDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.enumtype.QiActiveStatus;
import com.honda.galc.entity.qi.QiImageSection;
import com.honda.galc.entity.qi.QiInspectionLocation;
import com.honda.galc.entity.qi.QiPartDefectCombination;
import com.honda.galc.entity.qi.QiPartLocationCombination;
import com.honda.galc.util.AuditLoggerUtil;
/**
 * 
 * <h3>InspectionPartController Class description</h3>
 * <p>
 * InspectionPartController is used to load data in TableView and perform to the action on the RadioButton (All, Activate, Inactivate )etc.
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
public class InspectionLocationController extends AbstractQiController<ItemMaintenanceModel, InspectionLocationMaintPanel> implements EventHandler<ActionEvent> {
	
	private QiInspectionLocation selectedLocation;
	private QiInspectionLocation qiInspectionLocation;
	private List<QiInspectionLocation> selectedLocationList;
	private final static String LOC_NAME_UPDATE_MESSAGE_WITH_ASSOCIATION = "The Location(s) being updated affects ";
	private final static String UPDATE_MESSAGE="Do you still want to continue?";
	private QiInspectionLocation selectedLocCloned;

	public InspectionLocationController(ItemMaintenanceModel model,InspectionLocationMaintPanel view) {
		super(model, view);
	}
	
	@Override
	public void initEventHandlers() {
		if (isFullAccess()) {
			addTableListener();
		}
	}
	/**
	 * This method is added because initEventHandlers() is called before the selection listener is triggered.
	 */
	public void addContextMenuItems()
	{
		clearDisplayMessage();
		ObjectTablePane<QiInspectionLocation> locationTablePane = getView().getLocationScreenTablePane();
		int partsCounted=locationTablePane.getTable().getSelectionModel().getSelectedItems().size();
		String[] menuItems;
		if(selectedLocation!=null){
			if(getView().getAllRadioBtn().isSelected()){
				if(partsCounted > 1) {
					menuItems = new String[] { QiConstant.CREATE};
				}else{
					menuItems = new String[] { QiConstant.CREATE, QiConstant.UPDATE };
				}
				getView().getLocationScreenTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
				
			}else if(getView().getActiveRadioBtn().isSelected())
			{
				if(partsCounted > 1) {
					menuItems = new String[] { QiConstant.INACTIVATE };
				}else{
					menuItems = new String[] { QiConstant.CREATE, QiConstant.UPDATE, QiConstant.INACTIVATE };
				}
				getView().getLocationScreenTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			}
			else
			{
				if(partsCounted > 1) {
					menuItems = new String[] { QiConstant.REACTIVATE };
				}else{
					menuItems = new String[] { QiConstant.CREATE, QiConstant.UPDATE, QiConstant.REACTIVATE };
				}
				getView().getLocationScreenTablePane().getTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			}
		}else{
			 menuItems = new String[] { QiConstant.CREATE };
		}
		locationTablePane.createContextMenu(menuItems, this);
	}

	/**
	 * This method is used to create, edit ,inactivate,reactivate Location
	 */
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource() instanceof LoggedRadioButton){
			loadStatusData();
		}
		if(actionEvent.getSource() instanceof MenuItem) {
			MenuItem menuItem = (MenuItem) actionEvent.getSource();
			if(QiConstant.CREATE.equals(menuItem.getText())) createLocation(actionEvent);
			else if(QiConstant.UPDATE.equals(menuItem.getText())) updateLocation(actionEvent);
			else if(QiConstant.INACTIVATE.equals(menuItem.getText())) inactivateLocation(actionEvent);
			else if(QiConstant.REACTIVATE.equals(menuItem.getText())) reactivateLocation(actionEvent);
		}
		if(actionEvent.getSource() instanceof UpperCaseFieldBean){
			clearDisplayMessage();
			loadStatusData();
		}
	}
	
	
	private void addTableListener(){
		getView().getLocationScreenTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiInspectionLocation>() {
			public void changed(ObservableValue<? extends QiInspectionLocation> arg0,
					QiInspectionLocation arg1, QiInspectionLocation arg2) {
				selectedLocation=getView().getLocationScreenTablePane().getSelectedItem();
				selectedLocationList = getView().getLocationScreenTablePane().getSelectedItems();
				addContextMenuItems();
			}
		});
		getView().getLocationScreenTablePane().getTable().focusedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				addContextMenuItems();
			}
		});
	}
	/**
	 * This method is used to create the Location
	 * @param event
	 */
	public void createLocation(ActionEvent event){
		InspectionLocationDialog dialog = new InspectionLocationDialog(QiConstant.CREATE, new QiInspectionLocation(), getModel(),getApplicationId());
		dialog.setScreenName(getView().getScreenName());
		dialog.showDialog();
		loadStatusData();
	}
	/**
	 * This method is used to edit the Location info. clicking on 'Update' context menu
	 * @param event
	 */
	public void updateLocation(ActionEvent event){
		InspectionLocationDialog dialog = new InspectionLocationDialog(QiConstant.UPDATE, selectedLocation, getModel(),getApplicationId());
		dialog.setScreenName(getView().getScreenName());
		dialog.showDialog();
		if(!dialog.isCancel())  {
			loadStatusData(true);
		}
	}
	/**
	 * This method is used to Reactivate the Location
	 * @param event
	 */
	public void reactivateLocation(ActionEvent event){
		updateLocationStatus(true);
	}
	
	/**
	 * This method is used to Inactivate the Part
	 * @param event
	 */
	public void inactivateLocation(ActionEvent event){
		updateLocationStatus(false);
	}
	
	/**
	 * This method is used to update the status of Part
	 * @param isActive
	 */
	private void updateLocationStatus(boolean isActive){
		qiInspectionLocation = getView().getLocationScreenTablePane().getSelectedItem();
		qiInspectionLocation.setUpdateUser(getUserId());
		ReasonForChangeDialog dialog=null;
		try {
			if(isActive){
				updateLocationStatus(dialog,isActive);
			}else{
				List<Integer> partLocationIdList= new ArrayList<Integer>();
				for (QiInspectionLocation qiInspectionLocation : selectedLocationList) {
					List<QiPartLocationCombination> qiPartLocationCombinationsList =getModel().checkLocationInPartLocCombination(qiInspectionLocation.getInspectionPartLocationName());
					for (QiPartLocationCombination qiPartLocationCombination : qiPartLocationCombinationsList) {
						partLocationIdList.add(qiPartLocationCombination.getPartLocationId());
					}
				}
				if(partLocationIdList.size()>0){
					showCountedLocationsWithAssociatedScreen(dialog,partLocationIdList,isActive);
				}else{
					updateLocationStatus(dialog,isActive);
				}
			}
		} catch (Exception e) {
			handleException("Failed to inactivate/Reactivate Location  ", "An error occurred at updateLocationStatus method ", e);
		}
	}
	/**
	 * This method is used to load data into ObjectTablePane based on status
	 */
	private void loadStatusData(){
		loadStatusData(false);
	}
	/**
	 * This method is used to load data into ObjectTablePane based on status
	 */
	private void loadStatusData(boolean isRestoreSelect){
		if(getView().getAllRadioBtn().isSelected())
			getView().reload(getView().getFilterTextData(), isRestoreSelect);
		else if(getView().getActiveRadioBtn().isSelected())
			getView().reload(getView().getFilterTextData(),QiActiveStatus.ACTIVE.getName());
		else
			getView().reload(getView().getFilterTextData(),QiActiveStatus.INACTIVE.getName());
	}
	/**
	 *  This method is used to update the status of Location without association of Location in Part location combination
	 * @param status
	 */
	private void updateLocationStatus(ReasonForChangeDialog dialog,boolean status){
		dialog = new ReasonForChangeDialog(getApplicationId());
		if(dialog.showReasonForChangeDialog(null))
		{
			try{
				for (QiInspectionLocation inspectionLocation: selectedLocationList) {
					getModel().updateLocationStatus(inspectionLocation.getInspectionPartLocationName(), status? (short) 1:(short) 0);
					selectedLocCloned=(QiInspectionLocation) inspectionLocation.deepCopy();
					inspectionLocation.setActive(status);
					//call to prepare and insert audit data
					AuditLoggerUtil.logAuditInfo(selectedLocCloned, inspectionLocation, dialog.getReasonForChangeTextArea().getText(), getView().getScreenName(),getUserId());
				}
				loadStatusData();
			}catch(Exception e){
				handleException("An error occured in updatePartStatus method " , "Failed to updatePartStatus Part Status ", e);
			}
		}
	}
	
	/**
	 * This method is used to show the count of Location with associated screens
	 * @param dialog
	 * @param partLocationIdList
	 * @param isActive
	 */
	private void showCountedLocationsWithAssociatedScreen(ReasonForChangeDialog dialog,List<Integer> partLocationIdList,boolean isActive){
		int countedPartInPartLocComb = partLocationIdList.size();
		int countedPartInImageSection=0;
		int countedPartInPartDefectComb=0;
		int countedLocationInRegionalAttribute=0;
		
		List<QiImageSection> partLocIdInImageSectionList = getModel().findPartLocationIdsInImageSection(partLocationIdList);
		List<QiPartDefectCombination> partLocIdInPartDefCombList = getModel().findPartLocationIdsInPartDefectCombination(partLocationIdList);
		List<QiPartDefectCombination> partLocationIdInPartDefComb=getModel().findAllPLCIdsByPartLocId(partLocationIdList);
		if(partLocIdInPartDefCombList!=null && partLocIdInPartDefCombList.size()>0){
			countedPartInPartDefectComb=partLocIdInPartDefCombList.size();
			countedLocationInRegionalAttribute = partLocationIdInPartDefComb.size();
		}
		
		StringBuilder message=new StringBuilder(LOC_NAME_UPDATE_MESSAGE_WITH_ASSOCIATION + countedPartInPartLocComb + " Part Loc Comb");   
		
		if((partLocIdInImageSectionList!=null && partLocIdInImageSectionList.size()>0)){
			countedPartInImageSection=partLocIdInImageSectionList.size();
			if(countedPartInPartDefectComb>0){
				message.append(countedPartInPartDefectComb + " Part Defect Comb");
			}
			if(countedLocationInRegionalAttribute>0){
				message.append(","+countedLocationInRegionalAttribute + " Regional Attribute ");
			}
			if(partLocIdInImageSectionList.size()>0)
				message.append(","+countedPartInImageSection +" Image Section");
			
			MessageDialog.showError(getView().getStage(),message.toString());
		}else{
			if(countedPartInPartDefectComb>0){
				message.append(countedPartInPartDefectComb + " Part Defect Comb");
			}
			if(countedLocationInRegionalAttribute>0){
				message.append(","+countedLocationInRegionalAttribute + " Regional Attribute ");
			}
			message.append(". "+UPDATE_MESSAGE);
			updateLocationInAllAssociatedScreens(partLocIdInPartDefCombList,message.toString(),isActive,partLocationIdList);
		}
	}
	/**
	 * This method is used to update Location with associated screens
	 * @param partLocIdInPartDefCombList
	 * @param message
	 * @param isActive
	 */
	private void updateLocationInAllAssociatedScreens(List<QiPartDefectCombination> partLocIdInPartDefCombList,String message,boolean isActive,List<Integer> partLocationIdList){
		ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
		if(dialog.showReasonForChangeDialog(message.toString()))
		{
			String returnValue=isLocalSiteImpacted(partLocationIdList,getView().getStage());
			if(returnValue.equals(QiConstant.NO_LOCAL_SITES_CONFIGURED)){
				return;
			}
			else if(returnValue.equals(QiConstant.LOCAL_SITES_IMPACTED)){
				publishErrorMessage("Inactivation of location(s) affects Local Sites");
				return;
			}
			try{
				for (QiInspectionLocation qiInspectionLocation: selectedLocationList) {
					getModel().inactivateLocation(qiInspectionLocation.getInspectionPartLocationName());
					getModel().updateLocationStatus(qiInspectionLocation.getInspectionPartLocationName(), isActive? (short) 1:(short) 0);
					selectedLocCloned=(QiInspectionLocation) qiInspectionLocation.deepCopy();
					qiInspectionLocation.setActive(isActive);
					//call to prepare and insert audit data
					AuditLoggerUtil.logAuditInfo(selectedLocCloned, qiInspectionLocation, dialog.getReasonForChangeTextArea().getText(), getView().getScreenName(),getUserId());
					
				}
				for (QiPartDefectCombination partDefectCombination: partLocIdInPartDefCombList) {
					getModel().inactivatePartinPartDefectCombination(partDefectCombination.getPartLocationId());
				}
				loadStatusData();
			}catch(Exception e){
				handleException("An error occured in updatePartStatus method " , "Failed to updatePartStatus Part Status ", e);
			}
		}
	}
	
	public QiInspectionLocation getSelectedLocation() {
		return selectedLocation;
	}

	public void setSelectedLocation(QiInspectionLocation selectedLocation) {
		this.selectedLocation = selectedLocation;
	}
}
