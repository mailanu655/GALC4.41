package com.honda.galc.client.teamleader.qi.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.model.ItemMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.InspectionPartDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.enumtype.QiFlag;
import com.honda.galc.entity.qi.QiBomQicsPartMapping;
import com.honda.galc.entity.qi.QiImageSection;
import com.honda.galc.entity.qi.QiInspectionPart;
import com.honda.galc.entity.qi.QiPartDefectCombination;
import com.honda.galc.entity.qi.QiPartLocationCombination;
import com.honda.galc.util.AuditLoggerUtil;
/**
 * 
 * <h3>InspectionPartDialogController Class description</h3>
 * <p>
 * InspectionPartDialogController is used to perform the actions like 'create' ,'update' and 'cancel' etc.
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

public class InspectionPartDialogController extends QiDialogController<ItemMaintenanceModel, InspectionPartDialog> {
	private QiInspectionPart qiInspectionPart;
	private QiInspectionPart previousInspectionPart;
	private List<QiPartLocationCombination> qiPartLocationCombinationsList;
	private final static String PART_NAME_UPDATE_MESSAGE = "Changing Part Name is not recommended. Do you still want to continue?";
	private final static String DESIRED_POSITION_MESSAGE = "Cannot modify Desired Position as Part is associated with Part Loc Comb.";
	private final static String ALLOW_MULTIPLE_MESSAGE = "Cannot modify Allow Multiple as Part is associated with Part Loc Comb.";
	private final static String INACTIVATE_MESSAGE_WITH_ASSOCIATION = " The Part(s) being updated affects ";
	private final static String UPDATE_MESSAGE="Do you still want to continue?";
	private QiInspectionPart qiInspectionPartCloned;
	public InspectionPartDialogController(ItemMaintenanceModel model, InspectionPartDialog dialog, QiInspectionPart selectedPart) {
		super();
		setModel(model);
		setDialog(dialog);
		previousInspectionPart=selectedPart;
		this.qiInspectionPartCloned =(QiInspectionPart) selectedPart.deepCopy();
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
	 * This method is used to create Part
	 * @param event
	 */
	private void createBtnAction(ActionEvent event) {
		LoggedButton createBtn = getDialog().getCreateBtn();
		UpperCaseFieldBean partNameupperCaseField=getDialog().getPartNameTextField();
		UpperCaseFieldBean partClassUpperCaseField=getDialog().getPartClassTextField();
		UpperCaseFieldBean partAbbrUpperCaseField=getDialog().getPartAbbrTextField();
		UpperCaseFieldBean partDescUpperCaseField=getDialog().getPartDescTextField();
		UpperCaseFieldBean valueUpperCaseField=getDialog().getValueTextField();
		
		/** Mandatory Check for Defect Type name */
		if(QiCommonUtil.isMandatoryFieldEmpty(partNameupperCaseField)){
			displayErrorMessage("Mandatory field is empty", "Please enter Part Name");
			return;
		}
		if(QiCommonUtil.isMandatoryFieldEmpty(partClassUpperCaseField)){
			displayErrorMessage("Mandatory field is empty", "Please enter Part Class");
			return;
		}
		if(validationOnSpecialCharacters(partNameupperCaseField,partAbbrUpperCaseField,partClassUpperCaseField,partDescUpperCaseField))return;
		
		  qiInspectionPart= new QiInspectionPart();
		  qiInspectionPart.setActive(getDialog().getActiveRadioBtn().isSelected());
		  qiInspectionPart.setPrimaryPosition(getDialog().getPrimaryRadioBtn().isSelected());
		  qiInspectionPart.setAllowMultiple(getDialog().getAllowMultiple().isSelected());
		  qiInspectionPart.setInspectionPartName(StringUtils.trim(QiCommonUtil.delMultipleSpaces(partNameupperCaseField)));
		  qiInspectionPart.setInspectionPartDescShort(StringUtils.trim(QiCommonUtil.delMultipleSpaces(partAbbrUpperCaseField)));
		  qiInspectionPart.setPartClass(StringUtils.trim(QiCommonUtil.delMultipleSpaces(partClassUpperCaseField)));
		  qiInspectionPart.setInspectionPartDescLong(StringUtils.trim(QiCommonUtil.delMultipleSpaces(partDescUpperCaseField)));
		  short hierarchyValue= (StringUtils.isBlank(valueUpperCaseField.getText())) ? (short)0 : Short.valueOf(valueUpperCaseField.getText());
		  qiInspectionPart.setHierarchy(hierarchyValue);
		  qiInspectionPart.setCreateUser(getUserId());
		  qiInspectionPart.setProductKind(getModel().getProductKind());
		  try {
			  if(((ItemMaintenanceModel)getModel()).isPartExists(qiInspectionPart.getInspectionPartName())){
				  String errorAndLoggerMessage="Failed to add new Part as the Part name " + qiInspectionPart.getInspectionPartName() +" already exists!";
				  displayErrorMessage(errorAndLoggerMessage,errorAndLoggerMessage);
			  }else{
				  ((ItemMaintenanceModel)getModel()).createPart(qiInspectionPart);
				  Stage stage= (Stage) createBtn.getScene().getWindow();
				  stage.close();
			  }
		} catch (Exception e) {
			handleException("An error occurred at createPart method ", "Unable to create the Part", e);
		}
	}
	/**
	 * This method is used to update Part
	 * @param event
	 */
	private void updateBtnAction(ActionEvent event) {
		UpperCaseFieldBean partNameupperCaseField=getDialog().getPartNameTextField();
		UpperCaseFieldBean partClassUpperCaseField=getDialog().getPartClassTextField();
		LoggedTextArea loggedTextArea=getDialog().getReasonForChangeTextArea();
		UpperCaseFieldBean partAbbrUpperCaseField=getDialog().getPartAbbrTextField();
		UpperCaseFieldBean partDescUpperCaseField=getDialog().getPartDescTextField();
		/** Mandatory Check for Defect Type name */
		if(QiCommonUtil.isMandatoryFieldEmpty(partNameupperCaseField)){
			displayErrorMessage("Mandatory field is empty", "Please enter Part Name");
			return;
		}
		if(QiCommonUtil.isMandatoryFieldEmpty(partClassUpperCaseField)){
			displayErrorMessage("Mandatory field is empty", "Please enter Part Class");
			return;
		}
		if(QiCommonUtil.isMandatoryFieldEmpty(loggedTextArea)){
			displayErrorMessage("Mandatory field is empty", "Please enter Reason for Change");
			return;
		}
		if(validationOnSpecialCharacters(partNameupperCaseField,partAbbrUpperCaseField,partClassUpperCaseField,partDescUpperCaseField))return;
		
		String previousPartName =previousInspectionPart.getInspectionPartName();
		qiPartLocationCombinationsList =((ItemMaintenanceModel)getModel()).checkPartInPartLocCombination(previousPartName);
		
		List<QiBomQicsPartMapping> bomQicsPartMappinglist =findAllBomQicsPartMappingByPartName();
		
		if(previousPartName.equals(StringUtils.trim(partNameupperCaseField.getText()))){
			updatePartInfo(false,previousInspectionPart);
		}else{
			boolean isOk=false;
			if(qiPartLocationCombinationsList.size()>0 || bomQicsPartMappinglist.size()>0){
				isOk= true;
			}else{
				isOk = MessageDialog.confirm(getDialog(),PART_NAME_UPDATE_MESSAGE);
			}
			if(isOk){
				updatePartInfo(true,previousInspectionPart);
			}
		}
			
	}
	/**
	 * This method is used to check whether user is updated the Part name or not .
	 *  if updated Part name then previous part will get deleted and current Part name will get created .
	 *  if not updated Part name then Part data will be updated .
	 * @param partNameflag
	 * @param previousInspectionPart
	 */
	private void updatePartInfo(boolean isPartNameUpdated,QiInspectionPart previousInspectionPart){
		LoggedButton updateBtn = getDialog().getUpdateButton();
		qiInspectionPart= new QiInspectionPart();
		String partAbbr =StringUtils.trim(getDialog().getPartAbbrTextField().getText());
		String partDesc = StringUtils.trim(getDialog().getPartDescTextField().getText());
		qiInspectionPart.setInspectionPartName(StringUtils.trim(QiCommonUtil.delMultipleSpaces(getDialog().getPartNameTextField())));
		qiInspectionPart.setPartClass(StringUtils.trim(QiCommonUtil.delMultipleSpaces(getDialog().getPartClassTextField())));
		partAbbr= (StringUtils.isBlank(partAbbr)) ? "" : QiCommonUtil.delMultipleSpaces(getDialog().getPartAbbrTextField());
		qiInspectionPart.setInspectionPartDescShort(partAbbr);
		partDesc= (StringUtils.isBlank(partDesc)) ? "" : QiCommonUtil.delMultipleSpaces(getDialog().getPartDescTextField());
		qiInspectionPart.setInspectionPartDescLong(partDesc);
		try {
				boolean isPopUpMessage=false;/* This flag is used to stay popUpScreen with Parent screen while getting some error/message on the popUpScreen*/
				qiInspectionPart.setActive(getDialog().getActiveRadioBtn().isSelected());
				qiInspectionPart.setPrimaryPosition(getDialog().getPrimaryRadioBtn().isSelected());
				qiInspectionPart.setAllowMultiple(getDialog().getAllowMultiple().isSelected());
				short hierarchyValue= (StringUtils.isBlank(getDialog().getValueTextField().getText())) ? (short)0 : Short.valueOf(getDialog().getValueTextField().getText());
				qiInspectionPart.setHierarchy(hierarchyValue);
				isPopUpMessage=(isPartNameUpdated)?updateUnEqualPartNameInfo(previousInspectionPart):updateEqualPartNameInfo(previousInspectionPart);
				//call to prepare and insert audit data
				AuditLoggerUtil.logAuditInfo(qiInspectionPartCloned, qiInspectionPart, getDialog().getReasonForChangeTextArea().getText(), getDialog().getScreenName(),getUserId());
				if(isPopUpMessage){
					return;
				}
				Stage stage= (Stage) updateBtn.getScene().getWindow();
				stage.close();
		} catch (Exception e) {
			handleException("An error occurred at updatePartInfo method ", "Unable to update the Part",  e);
		}
	}
	
	/**
	 * This method is used to update same part 
	 * @param inspectionPart
	 * @return
	 */
	private boolean updateEqualPartNameInfo(QiInspectionPart previousInspectionPart){
		boolean isPopUp=false;
		boolean isUpdatePartName=false;
		
		if(qiPartLocationCombinationsList.size()>0){
			/**
			 * Checking whether desired position or Allow Multiple  is changed or not. if changed displaying Error message otherwise updating PartName and Status in PartLocationCombination
			 */
			if(previousInspectionPart.getPosition().equalsIgnoreCase(qiInspectionPart.getPosition())){
				isUpdatePartName=true;
			}else{
				displayErrorMessage(DESIRED_POSITION_MESSAGE, DESIRED_POSITION_MESSAGE);
				isPopUp=true;
			}
			if(!isPopUp){
				if(!(previousInspectionPart.getAllowMltpl().equalsIgnoreCase(QiFlag.YES.getName())&& qiInspectionPart.getAllowMltpl().equalsIgnoreCase(QiFlag.NO.getName()))){
					isUpdatePartName=true;
				}else{
					if(findCountedPartsInPartLocComb()>=2){
						displayErrorMessage(ALLOW_MULTIPLE_MESSAGE, ALLOW_MULTIPLE_MESSAGE);
						isPopUp=true;
						isUpdatePartName=false;
					}
				}
			}
		}else{
			isUpdatePartName=true;
		}
		if(isUpdatePartName){
			qiInspectionPart.setCreateUser(previousInspectionPart.getCreateUser());
			qiInspectionPart.setUpdateUser(getUserId());
			qiInspectionPart.setProductKind(getModel().getProductKind());
			qiInspectionPart.setAppCreateTimestamp(previousInspectionPart.getAppCreateTimestamp());
			if (isUpdated(previousInspectionPart)) {
				return true;
			}
			isPopUp=inactivateAndshowCountedPartsWithAssociatedScreen(qiPartLocationCombinationsList,previousInspectionPart,false);
			if(!isPopUp){
				((ItemMaintenanceModel)getModel()).updatePart(qiInspectionPart);	
			}
		}
		return isPopUp;
	}
	/**
	 * This method is used to update unequal part 
	 * @param inspectionPart
	 * @return
	 */
	private boolean updateUnEqualPartNameInfo(QiInspectionPart previousInspectionPart){
		boolean isPopUp=false;
		boolean isUpdated=false;
		/*Checking whether desired position or Allow Multiple  is changed or not. if changed displaying Error message otherwise updating PartName and Status in PartLocationCombination   */
		
		if(qiPartLocationCombinationsList.size()>0){
			if(previousInspectionPart.getPosition().equals(qiInspectionPart.getPosition())){
				isUpdated=true;
			}else{
				displayErrorMessage(DESIRED_POSITION_MESSAGE, DESIRED_POSITION_MESSAGE);
				isPopUp=true;
			}
			if(!isPopUp){
				if(!(previousInspectionPart.getAllowMltpl().equalsIgnoreCase(QiFlag.YES.getName())&& qiInspectionPart.getAllowMltpl().equalsIgnoreCase(QiFlag.NO.getName()))){
					isUpdated=true;
				}else{
					if(findCountedPartsInPartLocComb()>=2){
						displayErrorMessage(ALLOW_MULTIPLE_MESSAGE, ALLOW_MULTIPLE_MESSAGE);
						isPopUp=true;
						isUpdated=false;
					}
				}
			}
		}else{
			isUpdated=true;
		}
		
		/* Updating the status in PartLocationCombination ,updating the PartName*/
		if(isUpdated){
			qiInspectionPart.setUpdateUser(getUserId());
			qiInspectionPart.setProductKind(getModel().getProductKind());
			/*Checking existing Part while updating the Part */
			if(((ItemMaintenanceModel)getModel()).isPartExists(qiInspectionPart.getInspectionPartName())){
				String errorAndLoggerMessage="Failed to add new Part as the Part name "+ qiInspectionPart.getInspectionPartName() +" already exists!";;
				displayErrorMessage(errorAndLoggerMessage,errorAndLoggerMessage);
				isPopUp=true;
			}else{
				if (isUpdated(previousInspectionPart)) {
					return true;
				}				
				isPopUp=inactivateAndshowCountedPartsWithAssociatedScreen(qiPartLocationCombinationsList,previousInspectionPart,true);
				if(!isPopUp){
					((ItemMaintenanceModel)getModel()).updatePart(qiInspectionPart,previousInspectionPart.getInspectionPartName());
					((ItemMaintenanceModel)getModel()).updatePartInBomQicsPartMapping(qiInspectionPart,previousInspectionPart.getInspectionPartName());
				}
			}
		}
		return isPopUp;
	}
	
	/** This method is used to update the PartLocationCombination and swap the Parts based on the hierarchy value
	 * @param inspectionPart
	 */
	private void updatePartLocationCombination(QiInspectionPart previousInspectionPart,boolean isPartNameUpdated) {
		for (QiPartLocationCombination qiPartLocationCombination : qiPartLocationCombinationsList) {
			if(qiPartLocationCombination.getInspectionPartName().equals(previousInspectionPart.getInspectionPartName())){
				if(isPartNameUpdated){
					qiPartLocationCombination.setInspectionPartName(qiInspectionPart.getInspectionPartName());
					qiPartLocationCombination.setActiveValue(qiInspectionPart.getActiveValue());
					qiPartLocationCombination.setUpdateUser(getUserId());
					qiPartLocationCombination=setPart1Info(qiPartLocationCombination,qiInspectionPart);
				}else{
					qiPartLocationCombination=setPart1Info(qiPartLocationCombination,qiInspectionPart);
				}
			}
			if(qiPartLocationCombination.getInspectionPart2Name().equals(previousInspectionPart.getInspectionPartName())){
				if(isPartNameUpdated){
					qiPartLocationCombination.setInspectionPart2Name(qiInspectionPart.getInspectionPartName());
					qiPartLocationCombination.setActiveValue(qiInspectionPart.getActiveValue());
					qiPartLocationCombination.setUpdateUser(getUserId());
					qiPartLocationCombination=setPart2Info(qiPartLocationCombination,qiInspectionPart);
				}else{
					qiPartLocationCombination=setPart2Info(qiPartLocationCombination,qiInspectionPart);
				}
			}
			if(qiPartLocationCombination.getInspectionPart3Name().equals(previousInspectionPart.getInspectionPartName())){
				if(isPartNameUpdated){
					qiPartLocationCombination.setInspectionPart3Name(qiInspectionPart.getInspectionPartName());
					qiPartLocationCombination.setActiveValue(qiInspectionPart.getActiveValue());
					qiPartLocationCombination.setUpdateUser(getUserId());
					qiPartLocationCombination=setPart3Info(qiPartLocationCombination,qiInspectionPart);
				}else{
					qiPartLocationCombination=setPart3Info(qiPartLocationCombination,qiInspectionPart);
				}
			}
			((ItemMaintenanceModel)getModel()).updatePartLocationCombination(qiPartLocationCombination);
		}
	}
	
	/**
	 * This method is used to check the Part in Part1 column in PartlocationCombination and swap Part1 with Part2 based on the hierarchy value
	 * @param qiPartLocationCombination
	 * @param qiInspectionPart
	 * @return
	 */
	private QiPartLocationCombination setPart1Info(QiPartLocationCombination qiPartLocationCombination ,QiInspectionPart qiInspectionPart){
			QiInspectionPart qiInspectionPart2 = findPart(qiPartLocationCombination.getInspectionPart2Name());
			if(qiInspectionPart2!=null){
				if(!qiInspectionPart.getInspectionPartName().equalsIgnoreCase(qiInspectionPart2.getInspectionPartName())){
					if(qiInspectionPart2.isPrimaryPosition()){
						short part2HierarchyValue=qiInspectionPart2.getHierarchy();
						if(qiInspectionPart.getHierarchy()>part2HierarchyValue){
							String part2=qiInspectionPart2.getInspectionPartName();
							qiPartLocationCombination=setPart1Part2Info(qiPartLocationCombination,part2,qiInspectionPart.getInspectionPartName());
						}
					}
				}
			}
			
		return qiPartLocationCombination;
	}
	/**
	 * This method is used to check the Part in Part2 column in PartlocationCombination and swap Part2 with Part1 or Part3 based on the hierarchy value
	 * @param qiPartLocationCombination
	 * @param qiInspectionPart
	 * @return
	 */
	private QiPartLocationCombination setPart2Info(QiPartLocationCombination qiPartLocationCombination ,QiInspectionPart qiInspectionPart){
		short hierarchyValue=qiInspectionPart.getHierarchy();
		String partName=qiInspectionPart.getInspectionPartName();
		if(qiInspectionPart.isPrimaryPosition()){
				QiInspectionPart qiInspectionPart1 = findPart(qiPartLocationCombination.getInspectionPartName());
				if(qiInspectionPart1!=null){
					if(!qiInspectionPart1.getInspectionPartName().equalsIgnoreCase(partName)){
						short part1HierarchyValue=qiInspectionPart1.getHierarchy();
						if(hierarchyValue<part1HierarchyValue){
							String  part1=qiInspectionPart1.getInspectionPartName();
							qiPartLocationCombination =setPart1Part2Info(qiPartLocationCombination,partName,part1);
						}
					}
				}
		}
		if(!qiInspectionPart.isPrimaryPosition()){
				QiInspectionPart qiInspectionPart3 =findPart(qiPartLocationCombination.getInspectionPart3Name());
				if(qiInspectionPart3!=null){
					if(!qiInspectionPart3.getInspectionPartName().equalsIgnoreCase(partName)){
							short part3HierarchyValue=qiInspectionPart3.getHierarchy();
							if(hierarchyValue>part3HierarchyValue){
								String part3=qiInspectionPart3.getInspectionPartName();
								qiPartLocationCombination.setInspectionPart2Name(part3);
								qiPartLocationCombination.setInspectionPart3Name(partName);
						}	
					}
				}
		}
		return qiPartLocationCombination;
	}
			
	/**
	 * This method is used to check the Part in Part3 column in PartlocationCombination and swap Part3 with Part2 based on the hierarchy value
	 * @param qiPartLocationCombination
	 * @param qiInspectionPart
	 * @return
	 */
	private QiPartLocationCombination setPart3Info(QiPartLocationCombination qiPartLocationCombination ,QiInspectionPart qiInspectionPart){
			QiInspectionPart qiInspectionPart2 = findPart(qiPartLocationCombination.getInspectionPart2Name());
			if(qiInspectionPart2!=null){
				if(!qiInspectionPart.getInspectionPartName().equalsIgnoreCase(qiInspectionPart2.getInspectionPartName())){
					if(!qiInspectionPart2.isPrimaryPosition()){
						short part2HierarchyValue=qiInspectionPart2.getHierarchy();
						if(qiInspectionPart.getHierarchy()<part2HierarchyValue){
							String part2=qiInspectionPart2.getInspectionPartName();
							qiPartLocationCombination.setInspectionPart2Name(qiInspectionPart.getInspectionPartName());
							qiPartLocationCombination.setInspectionPart3Name(part2);
						}
					}	
				}
		}
		return qiPartLocationCombination;
	}
	
	/**
	 * This method is used to swap Part1 with Part2 and vice versa
	 * @param qiPartLocationCombination
	 * @param part2
	 * @param partName
	 * @return
	 */
	private QiPartLocationCombination setPart1Part2Info(QiPartLocationCombination qiPartLocationCombination,String partName1,String partName2){
		String  part1Loc1=qiPartLocationCombination.getInspectionPartLocationName();
		String  part1Loc2 =qiPartLocationCombination.getInspectionPartLocation2Name();
		String  part2Loc1=qiPartLocationCombination.getInspectionPart2LocationName();
		String  part2Loc2 =qiPartLocationCombination.getInspectionPart2Location2Name();
		qiPartLocationCombination.setInspectionPartName(partName1);
		qiPartLocationCombination.setInspectionPartLocationName(part2Loc1);
		qiPartLocationCombination.setInspectionPartLocation2Name(part2Loc2);
		qiPartLocationCombination.setInspectionPart2Name(partName2);
		qiPartLocationCombination.setInspectionPart2LocationName(part1Loc1);
		qiPartLocationCombination.setInspectionPart2Location2Name(part1Loc2);
		return qiPartLocationCombination;
	}
	/**
	 * This method is used to find the Part
	 * @param partName
	 * @return
	 */
	private QiInspectionPart findPart(String partName){
		QiInspectionPart qiInspectionPart=null;
		if(!StringUtils.isBlank(partName) && partName!=null){
			List<QiInspectionPart> qiInspectionPartsList =((ItemMaintenanceModel)getModel()).findPartsByFilter(partName);
			if(qiInspectionPartsList.size()>0){
				qiInspectionPart= qiInspectionPartsList.get(0);
			}
		}
		return qiInspectionPart;
	}
	
	/**
	 * This method is used to close the Popup screen clicking 'cancel' button
	 * @param event
	 */
	private void cancelBtnAction(ActionEvent event) {
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
		displayValueInPartClass();
		validationForTextfield();
		addFieldListener(getDialog().getPartNameTextField(),true);
		addFieldListener(getDialog().getPartAbbrTextField(),true);
		addFieldListener(getDialog().getPartClassTextField(),true);
		addFieldListener(getDialog().getPartDescTextField(),true);
		addFieldListener(getDialog().getValueTextField(),false);
		setTextFieldListener(getDialog().getPartNameTextField());
		setTextFieldListener(getDialog().getPartAbbrTextField());
		setTextFieldListener(getDialog().getPartClassTextField());
		setTextFieldListener(getDialog().getPartDescTextField());
		setTextFieldListener(getDialog().getValueTextField());
		
		getDialog().getActiveRadioBtn().getToggleGroup().selectedToggleProperty().addListener(updateEnablerForToggle);
		getDialog().getPrimaryRadioBtn().getToggleGroup().selectedToggleProperty().addListener(updateEnablerForToggle);
		getDialog().getAllowMultiple().selectedProperty().addListener(updateEnablerForBoolean);
	}
	
	/**
	 * This method is used to perform the validation on TextFields
	 */
	private void validationForTextfield(){
		getDialog().getPartNameTextField().addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(32));
		getDialog().getPartAbbrTextField().addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(10));
		getDialog().getPartClassTextField().addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(32));
		getDialog().getPartDescTextField().addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(30));
		getDialog().getReasonForChangeTextArea().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(256));
		getDialog().getValueTextField().addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(5));
	}

	/**
	 * When user types value in Part LoggedTextField ,that value will be displayed in PartClass LoggedTextField
	 */
	private void displayValueInPartClass() {
		getDialog().getPartNameTextField().textProperty().addListener(new ChangeListener<String>() {
	            public void changed(
	                         final ObservableValue<? extends String> observableValue,
	                         final String oldValue, final String newValue) {
	            	getDialog().getPartClassTextField().setText(newValue);
	            }
	     });
	}
	
	/**
	 * This method is used to inactivate and show the count of Part(s) associated with the related screen
	 * @return
	 */
	private boolean inactivateAndshowCountedPartsWithAssociatedScreen(List<QiPartLocationCombination> contedPartInPartLocCombList,QiInspectionPart previousInspectionPart,boolean isPartNameUpdated){
		boolean isInactivate=false;
		List<Integer> partLocIdList = new ArrayList<Integer>();
		List<QiBomQicsPartMapping> bomQicsPartMappinglist=null;
		/**
		 * if PartName is updated or inactivated then finding BomQicsPartMappingByPartName
		 */
		bomQicsPartMappinglist =findAllBomQicsPartMappingByPartName();
		if((contedPartInPartLocCombList!=null && contedPartInPartLocCombList.size()>0) || (bomQicsPartMappinglist!=null && bomQicsPartMappinglist.size()>0)){
			for (QiPartLocationCombination qiPartLocationCombination : contedPartInPartLocCombList) {
				partLocIdList.add(qiPartLocationCombination.getPartLocationId());
			}
			List<QiImageSection> partLocIdInImageSectionList = getModel().findPartLocationIdsInImageSection(partLocIdList);
			List<QiPartDefectCombination> partLocationIdInPartDefComblist=getModel().findPartLocationIdsInPartDefectCombination(partLocIdList);
			List<QiPartDefectCombination> partLocationIdInPartDefComb=getModel().findAllPLCIdsByPartLocId(partLocIdList);
			isInactivate=showCountsWithAssociatedScreens(partLocIdInImageSectionList,partLocIdList,isPartNameUpdated,partLocationIdInPartDefComblist,bomQicsPartMappinglist,partLocationIdInPartDefComb);
		}
		return isInactivate;
	}	
	
	/**
	 * This method is used to show the number of counts of Associated screens (PLC,PDC and Image Section) and update the Part to the Associated screens as well
	 * @param partLocIdInImageSectionList
	 * @param partLocIdList
	 * @param isPartNameUpdated
	 * @param partLocationIdInPartDefComblist
	 * @return
	 */
	private boolean showCountsWithAssociatedScreens(List<QiImageSection> partLocIdInImageSectionList,List<Integer> partLocIdList,boolean isPartNameUpdated,List<QiPartDefectCombination> partLocationIdInPartDefComblist,List<QiBomQicsPartMapping> bomQicsPartMappinglist,List<QiPartDefectCombination> partLocationIdInPartDefComb){
		int countedPartInPartLocComb=partLocIdList.size();
		StringBuilder message = new StringBuilder();
		if(countedPartInPartLocComb>0 || bomQicsPartMappinglist.size()>0){
			 message.append(INACTIVATE_MESSAGE_WITH_ASSOCIATION);
			if(countedPartInPartLocComb>0){
				message= message.append(countedPartInPartLocComb + " Part Loc Comb, ");	
			}
		}
		  
		int countedPartInImageSection=partLocIdInImageSectionList.size();
		int countedPartInPartDefectComb= partLocationIdInPartDefComblist.size();
		int countedPartInRegionalAttribute=partLocationIdInPartDefComb.size();
		boolean isInactivate=false;
		boolean isAssociated=false;

		if(countedPartInImageSection>0){
			if(countedPartInPartDefectComb>0)
				message.append(countedPartInPartDefectComb + " Part Defect Comb");
			if(countedPartInRegionalAttribute>0)
				message.append(","+countedPartInRegionalAttribute + " Regional Attribute ");
			if(countedPartInImageSection>0)
				message.append(","+countedPartInImageSection +" Image Section ");
			if(getDialog().getInactiveRadioBtn().isSelected()){
				message.append(". Hence, inactivation of this part is not allowed.");
				MessageDialog.showError(getDialog(),message.toString());
				isInactivate=true;
			}else{
				isAssociated=true;
			}
		}else{
			if(countedPartInPartDefectComb>0)
				message.append(countedPartInPartDefectComb + " Part Defect Comb");
			if(countedPartInRegionalAttribute>0)
				message.append(","+countedPartInRegionalAttribute + " Regional Attribute ");
			if(bomQicsPartMappinglist.size()>0){
				message.append(", "+ bomQicsPartMappinglist.size() + " BOM Qics Part Association");
			}
			if(getDialog().getInactiveRadioBtn().isSelected()){
				message.append("."+UPDATE_MESSAGE);
				boolean isPartLocation = MessageDialog.confirm(getDialog(), message.toString());
				isInactivate=true;
				if(isPartLocation)
				{
					String returnValue=isLocalSiteImpacted(partLocIdList,getDialog().getScreenName(),getDialog());
					if(returnValue.equals(QiConstant.NO_LOCAL_SITES_CONFIGURED)){
						isInactivate=true;
					}
					else if(returnValue.equals(QiConstant.LOCAL_SITES_IMPACTED)){
						displayErrorMessage("The part cannot be inactivated", "Inactivation of the part affects Local Sites");
						isInactivate=true;
					}
					else
					{
					updatePartInAllAssociatedScreens(previousInspectionPart,isPartNameUpdated,partLocationIdInPartDefComblist);
					isInactivate=false;
					}
				}
			}else{
				isAssociated=true;
			}
		}
		if(isAssociated){
			if(previousInspectionPart.getInspectionPartName().equalsIgnoreCase(getDialog().getPartNameTextField().getText())){
				updatePartInAllAssociatedScreens(previousInspectionPart,isPartNameUpdated,partLocationIdInPartDefComblist);
				isInactivate=false;
			}else{
				message.append("."+UPDATE_MESSAGE);
				boolean isPartLocation = MessageDialog.confirm(getDialog(), message.toString());
				isInactivate=true;
				if(isPartLocation)
				{
					if (isUpdated(previousInspectionPart)) {
						return true;
					}
					updatePartInAllAssociatedScreens(previousInspectionPart,isPartNameUpdated,partLocationIdInPartDefComblist);
					isInactivate=false;
				}
			}
		}
	  return isInactivate;
	}
	/**
	 * This method is used to update the Part to the Associated screens as well
	 * @param previousInspectionPart
	 * @param isPartNameUpdated
	 * @param partLocationIdInPartDefectCombination
	 */
	private void updatePartInAllAssociatedScreens(QiInspectionPart previousInspectionPart,boolean isPartNameUpdated,List<QiPartDefectCombination> partLocationIdInPartDefectCombination){
		try{
			updatePartLocationCombination(previousInspectionPart,isPartNameUpdated);
			if(getDialog().getInactiveRadioBtn().isSelected()){
				((ItemMaintenanceModel)getModel()).inactivatePart(getDialog().getPartNameTextField().getText());
				for (QiPartDefectCombination partDefectCombination  : partLocationIdInPartDefectCombination) {
					getModel().inactivatePartinPartDefectCombination(partDefectCombination.getPartLocationId());
				}
			}
		}catch(Exception e){
			handleException("An error occured in updatePartStatus method " , "Failed to updatePartStatus Part Status ", e);
		}
	}
	
	/**
	 * This method is used to perform special character validation on TextFields
	 * @param partNameupperCaseField
	 * @param partAbbrUpperCaseField
	 * @param partClassUpperCaseField
	 * @param partDescUpperCaseField
	 * @return
	 */
	private boolean validationOnSpecialCharacters(UpperCaseFieldBean partNameupperCaseField,UpperCaseFieldBean partAbbrUpperCaseField,UpperCaseFieldBean partClassUpperCaseField,UpperCaseFieldBean partDescUpperCaseField){
		return (hasSpecialCharacters(partNameupperCaseField) ||hasSpecialCharacters(partAbbrUpperCaseField) || hasSpecialCharacters(partClassUpperCaseField) ||hasSpecialCharacters(partDescUpperCaseField));
	}
	/**
	 * This method is used to find the list of BomQicsPart based on PartName
	 * @return
	 */
	private List<QiBomQicsPartMapping> findAllBomQicsPartMappingByPartName(){
		List<QiBomQicsPartMapping> bomQicsPartMappinglist =getModel().findAllByPartName(previousInspectionPart.getInspectionPartName());
		return bomQicsPartMappinglist;
	}
	/**
	 * This method is used to find the count of Part1 ,Part2 and Part3 with same name(rows) in PLC and return max. count.
	 * @return
	 */
	private int findCountedPartsInPartLocComb(){
		List<Integer> countedParts=new ArrayList<Integer>();
		String previousPartName = previousInspectionPart.getInspectionPartName();
		
		for (QiPartLocationCombination qiPartLocationCombination : qiPartLocationCombinationsList) {
			int count=0;
			if(qiPartLocationCombination.getInspectionPartName().equalsIgnoreCase(previousPartName)){
				count=count+1;
			}
			if(qiPartLocationCombination.getInspectionPart2Name().equalsIgnoreCase(previousPartName)){
				count=count+1;
			}
			if(qiPartLocationCombination.getInspectionPart3Name().equalsIgnoreCase(previousPartName)){
				count=count+1;
			}
			countedParts.add(count);
		}
		return Collections.max(countedParts);
	}
}
