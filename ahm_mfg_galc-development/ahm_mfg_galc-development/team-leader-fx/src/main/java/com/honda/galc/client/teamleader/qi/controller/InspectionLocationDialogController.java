package com.honda.galc.client.teamleader.qi.controller;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.model.ItemMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.InspectionLocationDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.qi.QiImageSection;
import com.honda.galc.entity.qi.QiInspectionLocation;
import com.honda.galc.entity.qi.QiPartDefectCombination;
import com.honda.galc.entity.qi.QiPartLocationCombination;
import com.honda.galc.util.AuditLoggerUtil;

public class InspectionLocationDialogController extends QiDialogController<ItemMaintenanceModel, InspectionLocationDialog> {

	private QiInspectionLocation qiInspectionLocation;
	private List<QiPartLocationCombination> qiPartLocationCombinationsList;
	private static final String CREATE_LOC="CreateLoc";
	private static final String LOC_NAME_UPDATE_MESSAGE = "Changing Location Name is not recommended. Do you still want to continue? ";
	private static final String DESIRED_POSITION_MESSAGE ="Can not modify the desired position as it is associated with the Inspection Part in Combination Screen.";
	private final static String LOC_NAME_UPDATE_MESSAGE_WITH_ASSOCIATION = "The Location(s) being updated affects ";
	private final static String UPDATE_MESSAGE="Do you still want to continue?";
	private final static String ERROR_MESSAGE= "Failed to add new location as the location name ";
	private QiInspectionLocation selectedLocCloned;
	
	public InspectionLocationDialogController(ItemMaintenanceModel model, InspectionLocationDialog dialog, QiInspectionLocation selectedLocation) {
		super();
		setModel(model);
		setDialog(dialog);
		this.qiInspectionLocation = selectedLocation;
		this.selectedLocCloned =(QiInspectionLocation) selectedLocation.deepCopy();
	}
	/**
	 * This method is used to perform the actions like 'create' ,'update' and 'cancel' on the popup screen
	 */
	@Override
	public void handle(ActionEvent actionEvent) {
		clearDisplayMessage();
		if(actionEvent.getSource().equals(getDialog().getCreateBtn())) createBtnAction(actionEvent);
		else if(actionEvent.getSource().equals(getDialog().getUpdateButton())) updateBtnAction(actionEvent);
		else if(actionEvent.getSource().equals(getDialog().getCancelBtn())) cancelBtnAction(actionEvent);
	}
	/**
	 * This method is used to create Location
	 * @param event
	 */
	public void createBtnAction(ActionEvent event) {
		LoggedButton createBtn = getDialog().getCreateBtn();
		UpperCaseFieldBean locationName=getDialog().getLocationNameTextField();
		UpperCaseFieldBean locationAbbr=getDialog().getLocationAbbrTextField();
		UpperCaseFieldBean locationDesc=getDialog().getLocationDescTextField();
		
		String locName = locationName.getText();
		/** Mandatory Check for Defect Type name */
		if(QiCommonUtil.isMandatoryFieldEmpty(locationName)){
			displayErrorMessage("Mandatory field is empty", "Please enter Location Name");
			return;
		}
		if(validationOnSpecialCharacters(locationName,locationAbbr,locationDesc))return;
		try{ 
			qiInspectionLocation = new QiInspectionLocation();
			qiInspectionLocation.setProductKind(getModel().getProductKind());			  
			qiInspectionLocation.setCreateUser(getUserId());
			setInspectionLocation(CREATE_LOC,StringUtils.EMPTY);
			if(((ItemMaintenanceModel)getModel()).isLocationExists(StringUtils.trim(locName)))
				displayErrorMessage(ERROR_MESSAGE+locName+" already exists!",ERROR_MESSAGE+locName+" already exists!");
			else{
				((ItemMaintenanceModel)getModel()).createLocation(qiInspectionLocation);
				Stage stage= (Stage) createBtn.getScene().getWindow();
				stage.close();
			}
		}catch (Exception e) {
			handleException("An error occured in onCreateLocationAction method ", "Falied to Create Inspection Location", e);
		}
		 
	}
	/**
	 *  This is used to set Inspection Location 
	 *  @param updateLocName
	 *  @param actionName
	 */
	private void setInspectionLocation(String actionName, String previousLocName){
		qiInspectionLocation.setInspectionPartLocationName(StringUtils.trim(QiCommonUtil.delMultipleSpaces(getDialog().getLocationNameTextField())));
		qiInspectionLocation.setInspectionPartLocDescShort(StringUtils.trim(QiCommonUtil.delMultipleSpaces(getDialog().getLocationAbbrTextField())));
		qiInspectionLocation.setInspectionPartLocDescLong(StringUtils.trim(QiCommonUtil.delMultipleSpaces(getDialog().getLocationDescTextField())));

		qiInspectionLocation.setActive(getDialog().getActiveRadioBtn().isSelected());	
		if(StringUtils.isBlank(getDialog().getValueTextField().getText()))
			qiInspectionLocation.setHierarchy((short) 0); 
		else
			qiInspectionLocation.setHierarchy(Short.valueOf(getDialog().getValueTextField().getText()));
		qiInspectionLocation.setPrimaryPosition(getDialog().getPrimaryRadioBtn().isSelected());
		if(actionName.isEmpty()){
			qiInspectionLocation.setUpdateUser(getUserId());
			((ItemMaintenanceModel)getModel()).updateLocation(qiInspectionLocation,previousLocName);
			LoggedButton updateBtn = getDialog().getUpdateButton();
			Stage stage= (Stage) updateBtn.getScene().getWindow();
			stage.close();
		}
	}
	
	/**
	 *  This is used when user clicks on update button
	 */
	public void updateBtnAction(ActionEvent event) {
		UpperCaseFieldBean locationName=getDialog().getLocationNameTextField();
		UpperCaseFieldBean locationAbbr=getDialog().getLocationAbbrTextField();
		UpperCaseFieldBean locationDesc=getDialog().getLocationDescTextField();
		/** Mandatory Check for Location name */
		if(QiCommonUtil.isMandatoryFieldEmpty(locationName)){
			displayErrorMessage("Mandatory field is empty", "Please enter Location Name");
			return;
		}
			
		/** Mandatory Check for Reason for Change */
		if(QiCommonUtil.isMandatoryFieldEmpty(getDialog().getReasonForChangeTextArea())){
			displayErrorMessage("Mandatory field is empty", "Please enter Reason for Change");
			return;
		}
		if(validationOnSpecialCharacters(locationName,locationAbbr,locationDesc))return;
		try{
			qiInspectionLocation= ((ItemMaintenanceModel)getModel()).getQiLocation();
			String previousLocName =qiInspectionLocation.getInspectionPartLocationName();
			qiPartLocationCombinationsList =((ItemMaintenanceModel)getModel()).checkLocationInPartLocCombination(previousLocName);
			if(qiPartLocationCombinationsList.isEmpty()){
				updateNotAssociatedLocation();
			}else{
				updateAssociatedLocation();
			}
			//call to prepare and insert audit data
			AuditLoggerUtil.logAuditInfo(selectedLocCloned, qiInspectionLocation, getDialog().getReasonForChangeTextArea().getText(), getDialog().getScreenName(),getUserId());
		}
		catch (Exception e) {
			handleException("An error occured in onUpdateLocationAction method ", "Failed to Update Inspection Location", e);
		}
	}
	/**
	 * This method is used to check the exiting location and update the location info if location is not associated with other screens
	 */
	private void updateNotAssociatedLocation(){

		String locName = QiCommonUtil.delMultipleSpaces(getDialog().getLocationNameTextField());
		String previousLocName =qiInspectionLocation.getInspectionPartLocationName();
		boolean updateLocName = false;
		try{
			if(!(previousLocName.equalsIgnoreCase(locName.trim()))){
				updateLocName = MessageDialog.confirm(getDialog(), LOC_NAME_UPDATE_MESSAGE);
				if(!updateLocName){
					return;
				}else if(updateLocName && ((ItemMaintenanceModel)getModel()).isLocationExists(StringUtils.trim(locName))){
					displayErrorMessage(ERROR_MESSAGE+StringUtils.trim(locName)+" already exists!",ERROR_MESSAGE+StringUtils.trim(locName)+" already exists!");
					return;
				}
			}
			if (isUpdated(qiInspectionLocation)) {
				return;
			}
			setInspectionLocation(StringUtils.EMPTY,previousLocName);
		}catch(Exception e){
			handleException("Error in updateNotAssociatedLocation method", "Falied to update Location",  e);
		}
	}
	/**
	 * This method is used to check the exiting location and Location Name is getting changes and update the location info if location is  associated with other screens
	 */
	private void updateAssociatedLocation(){
		String previousLocName =qiInspectionLocation.getInspectionPartLocationName();
		String locName = StringUtils.trim(getDialog().getLocationNameTextField().getText());
		boolean previousPosition = qiInspectionLocation.isPrimaryPosition();
		boolean currentPostion = getDialog().getPrimaryRadioBtn().isSelected();
		short currentHierarchyValue = Short.valueOf((getDialog().getValueTextField().getText().equals(""))?"0":getDialog().getValueTextField().getText());
		boolean posChanged = false;
		if(previousPosition!=currentPostion){
			MessageDialog.showError(getDialog(),DESIRED_POSITION_MESSAGE);
			posChanged = true;
		}
		/** Inspection Location can not be updated if we try to change desired position */
		if(!posChanged){
			try{
				boolean updateLocName = false;
				/** If Inspection Location name is getting updated */
				if(!(previousLocName.equalsIgnoreCase(QiCommonUtil.delMultipleSpaces(getDialog().getLocationNameTextField())))){
					updateLocName=showCountedLocationWithAssociatedScreens(previousLocName,true);
					
					if(!updateLocName)
						return;
					else if(updateLocName && ((ItemMaintenanceModel)getModel()).isLocationExists(locName)){
						displayErrorMessage(ERROR_MESSAGE+locName+" already exists!",ERROR_MESSAGE+locName+" already exists!");
						return;
					}else if(updateLocName && !((ItemMaintenanceModel)getModel()).isLocationExists(locName))
					{
						if (isUpdated(qiInspectionLocation)) {
							return;
						}
						updatePartLocCombination(qiInspectionLocation);
						setInspectionLocation(StringUtils.EMPTY,previousLocName);
					}
					return;					
				}
				
				if (isUpdated(qiInspectionLocation)) {
					return;
				}
				if(getDialog().getInactiveRadioBtn().isSelected()){
					inactivateInspectionLocation(previousLocName);
				}/** If Hierarchy value is getting updated */
				else if(currentHierarchyValue!=qiInspectionLocation.getHierarchy()){
					updateHierarchyValue(qiInspectionLocation,previousLocName);
				}else{
					setInspectionLocation(StringUtils.EMPTY,previousLocName);
				}
			}catch(Exception e){
				handleException("Error in updateAssociatedLocation method", "Falied to update Location", e);
			}
		}
	}
	
	/**
	 *  This method is called when user wants to update Location Name
	 *  @param inspectionLocation
	 */
	private void updatePartLocCombination(QiInspectionLocation inspectionLocation){
		String locName = StringUtils.trim(QiCommonUtil.delMultipleSpaces(getDialog().getLocationNameTextField()));
		String preLocName =inspectionLocation.getInspectionPartLocationName();
		qiInspectionLocation.setActive(getDialog().getActiveRadioBtn().isSelected());
		short currentHierarchyValue  =Short.valueOf((getDialog().getValueTextField().getText().equals(""))?"0":getDialog().getValueTextField().getText());

		for (QiPartLocationCombination qiPartLocationCombination : qiPartLocationCombinationsList) {
			if(qiPartLocationCombination.getInspectionPartLocationName().equals(preLocName)){
				qiPartLocationCombination.setInspectionPartLocationName(locName);
				updatePartLocationName(currentHierarchyValue,qiPartLocationCombination,1, qiPartLocationCombination.getInspectionPartLocation2Name());
			}
			if(qiPartLocationCombination.getInspectionPartLocation2Name().equals(preLocName)){
				qiPartLocationCombination.setInspectionPartLocation2Name(locName);
				updatePartLocationName(currentHierarchyValue,qiPartLocationCombination,2,qiPartLocationCombination.getInspectionPartLocationName());
			}
			if(qiPartLocationCombination.getInspectionPart2LocationName().equals(preLocName)){
				qiPartLocationCombination.setInspectionPart2LocationName(locName);
				updatePartLocationName(currentHierarchyValue,qiPartLocationCombination,3,qiPartLocationCombination.getInspectionPart2Location2Name());
			}
			if(qiPartLocationCombination.getInspectionPart2Location2Name().equals(preLocName)){
				qiPartLocationCombination.setInspectionPart2Location2Name(locName);
				updatePartLocationName(currentHierarchyValue,qiPartLocationCombination,4,qiPartLocationCombination.getInspectionPart2LocationName());
			}
			qiPartLocationCombination.setActiveValue(qiInspectionLocation.getActiveValue());
			qiPartLocationCombination.setUpdateUser(getUserId());
			((ItemMaintenanceModel)getModel()).updatePartLocationCombination(qiPartLocationCombination);
		}
	}
	
	
	/**
	 *  This method updates the Part Location Combination based on Hierarchy value
	 *  @param qiInspectionLocation
	 */
	private void updateHierarchyValue(QiInspectionLocation qiInspectionLocation,String previousLocName){
		updatePartLocCombination(qiInspectionLocation);
		setInspectionLocation(StringUtils.EMPTY,previousLocName);
	}
	/**
	 * Update PartLocationName
	 * @param currentHierarchyValue
	 * @param qiPartLocationCombination
	 */
	private void updatePartLocationName(short currentHierarchyValue,QiPartLocationCombination qiPartLocationCombination, Integer caseCheck, String partLocName) {
		QiInspectionLocation qiNewInspectionLocation;
		if(currentHierarchyValue!= qiInspectionLocation.getHierarchy() && getDialog().getPrimaryRadioBtn().isSelected()){
			qiNewInspectionLocation=((ItemMaintenanceModel)getModel()).getDetailsForLocName(partLocName);
			if(qiNewInspectionLocation!=null){
				switch(caseCheck){
				case 1:
					if(qiNewInspectionLocation.isPrimaryPosition() && qiNewInspectionLocation.getHierarchy()<currentHierarchyValue){
						String tempLoc = qiPartLocationCombination.getInspectionPartLocation2Name();
						qiPartLocationCombination.setInspectionPartLocation2Name(qiPartLocationCombination.getInspectionPartLocationName());
						qiPartLocationCombination.setInspectionPartLocationName(tempLoc);
					}
					break;
				case 2:
					if(qiNewInspectionLocation.isPrimaryPosition() && qiNewInspectionLocation.getHierarchy()>currentHierarchyValue){
						String tempLoc1 = qiPartLocationCombination.getInspectionPartLocationName();
						qiPartLocationCombination.setInspectionPartLocationName(qiPartLocationCombination.getInspectionPartLocation2Name());
						qiPartLocationCombination.setInspectionPartLocation2Name(tempLoc1);
					}
					break;
				case 3:
					if(qiNewInspectionLocation.isPrimaryPosition() && qiNewInspectionLocation.getHierarchy()<currentHierarchyValue){
						String tempLoc2 = qiPartLocationCombination.getInspectionPart2Location2Name();
						qiPartLocationCombination.setInspectionPart2Location2Name(qiPartLocationCombination.getInspectionPart2LocationName());
						qiPartLocationCombination.setInspectionPart2LocationName(tempLoc2);
					}
					break;
				case 4:
					if(qiNewInspectionLocation.isPrimaryPosition() && qiNewInspectionLocation.getHierarchy()>currentHierarchyValue){
						String tempLoc3 = qiPartLocationCombination.getInspectionPart2LocationName();
						qiPartLocationCombination.setInspectionPart2LocationName(qiPartLocationCombination.getInspectionPart2Location2Name());
						qiPartLocationCombination.setInspectionPart2Location2Name(tempLoc3);
					}
					break;
				}
			}
		}
	}
	/**
	 *  This method is used to inactivate location 
	 */
	private void inactivateInspectionLocation(String previousLocName){
		if(getDialog().getInactiveRadioBtn().isSelected()){
			boolean updateStatus=showCountedLocationWithAssociatedScreens(getDialog().getLocationNameTextField().getText(),false);
			if(updateStatus){
				if (isUpdated(qiInspectionLocation)) {
					return;
				}				
				((ItemMaintenanceModel)getModel()).inactivateLocation(StringUtils.trim(getDialog().getLocationNameTextField().getText()));
				List<Integer> partLocationIdList= new ArrayList<Integer>();
				List<QiPartLocationCombination> qiPartLocationCombinationsList =getModel().checkLocationInPartLocCombination(StringUtils.trim(getDialog().getLocationNameTextField().getText()));
				for (QiPartLocationCombination qiPartLocationCombination : qiPartLocationCombinationsList) {
					partLocationIdList.add(qiPartLocationCombination.getPartLocationId());
				}
				List<QiPartDefectCombination> partLocationIdInPartDefComblist=getModel().findPartLocationIdsInPartDefectCombination(partLocationIdList);
				for (QiPartDefectCombination partDefectCombination: partLocationIdInPartDefComblist) {
					getModel().inactivatePartinPartDefectCombination(partDefectCombination.getPartLocationId());
				}
				setInspectionLocation(StringUtils.EMPTY,previousLocName);
			}else{
				return;
			}
		}
	}
	public void cancelBtnAction(ActionEvent event) {
		LoggedButton cancelBtn = getDialog().getCancelBtn();
		try {
			Stage stage = (Stage) cancelBtn.getScene().getWindow();
			getDialog().setCancel(true);
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
		addFieldListener(getDialog().getLocationNameTextField(),true);
		addFieldListener(getDialog().getLocationAbbrTextField(),true);
		addFieldListener(getDialog().getLocationDescTextField(),true);
		addFieldListener(getDialog().getValueTextField(),false);
		setTextFieldListener(getDialog().getLocationNameTextField());
		setTextFieldListener(getDialog().getLocationAbbrTextField());
		setTextFieldListener(getDialog().getLocationDescTextField());
		setTextFieldListener(getDialog().getValueTextField());
		
		getDialog().getActiveRadioBtn().getToggleGroup().selectedToggleProperty().addListener(updateEnablerForToggle);
		getDialog().getPrimaryRadioBtn().getToggleGroup().selectedToggleProperty().addListener(updateEnablerForToggle);
	}
	
	/**
	 *  This is used to validate Text Field
	 */
	private void validationForTextfield(){
		getDialog().getLocationNameTextField().addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(20));
		getDialog().getLocationAbbrTextField().addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(10));
		getDialog().getLocationDescTextField().addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(30));
		getDialog().getReasonForChangeTextArea().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(256));
		getDialog().getValueTextField().addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(5));
	}
	
	/**
	 * This method is used to show the number of counts of Associated screens (PLC,PDC and Image Section) 
	 * @param previousLocName
	 * @return
	 */
	private boolean showCountedLocationWithAssociatedScreens(String previousLocName,boolean isLocationUpdated){
		boolean updateLocName=false;
		int countedPartInImageSection=0;
		int countedPartInPartLocComb=0;
		int countedPartInPartDefectComb=0;
		int countedLocationInRegionalAttribute=0;
		List<Integer> partLocationIdList= new ArrayList<Integer>();
		List<QiPartLocationCombination> qiPartLocationCombinationsList =getModel().checkLocationInPartLocCombination(previousLocName);
		for (QiPartLocationCombination qiPartLocationCombination : qiPartLocationCombinationsList) {
			partLocationIdList.add(qiPartLocationCombination.getPartLocationId());
		}
		if(partLocationIdList.size()>0){
			countedPartInPartLocComb=partLocationIdList.size();
		}
		List<QiPartDefectCombination> partLocationIdInPartDefComblist=getModel().findPartLocationIdsInPartDefectCombination(partLocationIdList);
		List<QiPartDefectCombination> partLocationIdInPartDefComb=getModel().findAllPLCIdsByPartLocId(partLocationIdList);
		if(partLocationIdInPartDefComblist!=null && partLocationIdInPartDefComblist.size()>0){
			countedPartInPartDefectComb=partLocationIdInPartDefComblist.size();
			countedLocationInRegionalAttribute = partLocationIdInPartDefComb.size();
		}
		
		
		List<QiImageSection> partLocIdInImageSectionList = getModel().findPartLocationIdsInImageSection(partLocationIdList);
		StringBuilder message=new StringBuilder(LOC_NAME_UPDATE_MESSAGE_WITH_ASSOCIATION + countedPartInPartLocComb + " Part Loc Comb");   
		if((partLocIdInImageSectionList!=null && partLocIdInImageSectionList.size()>0)){
			countedPartInImageSection=partLocIdInImageSectionList.size();
			if(countedPartInPartDefectComb>0){
				message.append(countedPartInPartDefectComb + " Part Defect Comb");
			}
			if(countedLocationInRegionalAttribute>0){
				message.append(","+countedLocationInRegionalAttribute + " Regional Attribute ");
			}
			if(countedPartInImageSection>0)
				message.append(", "+countedPartInImageSection +" Image Section");
			
			if(isLocationUpdated && getDialog().getActiveRadioBtn().isSelected()){
				updateLocName = MessageDialog.confirm(getDialog(), message.toString());
			}else{
				MessageDialog.showError(getDialog(),message.toString());
			}
			
		}else{
			if(countedPartInPartDefectComb>0){
				message.append(countedPartInPartDefectComb + " Part Defect Comb");
			}
			if(countedLocationInRegionalAttribute>0){
				message.append(","+countedLocationInRegionalAttribute + " Regional Attribute ");
			}
			message.append("."+UPDATE_MESSAGE);
			updateLocName = MessageDialog.confirm(getDialog(), message.toString());
			if(updateLocName){
				String returnValue=isLocalSiteImpacted(partLocationIdList,getDialog().getScreenName(),getDialog());
				if(returnValue.equals(QiConstant.NO_LOCAL_SITES_CONFIGURED)){
					updateLocName=false;
				}
				else if(returnValue.equals(QiConstant.LOCAL_SITES_IMPACTED)){
					displayErrorMessage("The location cannot be inactivated", "Inactivation of the defect affects Local Sites");
					updateLocName=false;
				}
			}
		}
		return updateLocName;
	}
	/**
	 * This method is used to perform special character validation on TextFields
	 * @param locationNameUpperCaseField
	 * @param locationAbbrUpperCaseField
	 * @param locationDescUpperCaseField
	 * @return
	 */
	private boolean validationOnSpecialCharacters(UpperCaseFieldBean locationNameUpperCaseField,UpperCaseFieldBean locationAbbrUpperCaseField,UpperCaseFieldBean locationDescUpperCaseField){
		return (hasSpecialCharacters(locationNameUpperCaseField) ||hasSpecialCharacters(locationAbbrUpperCaseField) || hasSpecialCharacters(locationDescUpperCaseField));
	}
}
