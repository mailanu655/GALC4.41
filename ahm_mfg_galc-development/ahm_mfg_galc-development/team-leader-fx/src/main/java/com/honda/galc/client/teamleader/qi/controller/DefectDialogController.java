package com.honda.galc.client.teamleader.qi.controller;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.model.ItemMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.DefectDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.qi.QiDefect;
import com.honda.galc.entity.qi.QiEntryScreenDefectCombination;
import com.honda.galc.entity.qi.QiPartDefectCombination;
import com.honda.galc.util.AuditLoggerUtil;

/**
 * 
 * <h3>DefectDialogController Class description</h3>
 * <p> DefectDialogController description </p>
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
 * @author L&T Infotech<br>
 * Aug 1, 2016
 *
 *
 */

public class DefectDialogController extends QiDialogController<ItemMaintenanceModel, DefectDialog>{

	private QiDefect qiDefect;
	private static final String CREATE_DEFECT = "createDefect";
	private static final String DEFECT_NAME_UPDATE_MESSAGE = "Changing Defect Name is not recommended. Do you still want to continue? ";
	private static final String DESIRED_POSITION_MESSAGE ="Can not modify the desired position as it is associated with Part Defect Combination Screen.";
	private QiDefect qiDefectCloned;
	
	public DefectDialogController(ItemMaintenanceModel model, DefectDialog dialog, QiDefect qiDefect) {
		super();
		setModel(model);
		setDialog(dialog);
		this.setQiDefect(qiDefect);
		this.qiDefect = qiDefect;
		//added for Audit Logging
		this.qiDefectCloned =(QiDefect) qiDefect.deepCopy();
		
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource().equals(getDialog().getCreateBtn())) createBtnAction(actionEvent);
		if(actionEvent.getSource().equals(getDialog().getUpdateBtn())) updateBtnAction(actionEvent);
		if(actionEvent.getSource().equals(getDialog().getCancelBtn())) cancelBtnAction(actionEvent);
	}
	/**
	 * When user clicks on create button in the popup screen createDefect method gets called.
	 */
	public void createBtnAction(ActionEvent event) {
		LoggedButton createBtn = getDialog().getCreateBtn();
		String defectName = StringUtils.trim(getDialog().getDefectNameTextField().getText());
		/** Mandatory Check for Defect Type name */
		if(QiCommonUtil.isMandatoryFieldEmpty(getDialog().getDefectNameTextField())){
			displayErrorMessage("Mandatory field is empty", "Please enter Defect Name");
			return;
		}
		qiDefect = new QiDefect();
		try{  
			qiDefect.setCreateUser(getUserId());
			qiDefect.setProductKind(getModel().getProductKind());
			setDefectValue(CREATE_DEFECT, StringUtils.EMPTY);
			if(((ItemMaintenanceModel)getModel()).isDefectExists(defectName))
				displayErrorMessage("Failed to add new defect as the defect name "+defectName+" already exists!","Failed to add new defect as the defect name already exists!");
			else{
				((ItemMaintenanceModel)getModel()).createDefect(qiDefect);
				Stage stage = (Stage) createBtn.getScene().getWindow();
				stage.close();
			}
		}catch (Exception e) {
			handleException("An error occured in createDefect method " , "Failed to Create Defect ", e);
		}

	}

	/**
	 * When user clicks on update button in the popup screen updateDefect method gets called.
	 */
	public void updateBtnAction(ActionEvent event)
	{
		boolean updateDefectName = false;
		String oldDefectName = qiDefect.getDefectTypeName();
		String defectName = StringUtils.trim(getDialog().getDefectNameTextField().getText());
		/** Mandatory Check for Defect Type name */
		if(QiCommonUtil.isMandatoryFieldEmpty(getDialog().getDefectNameTextField())){
			displayErrorMessage("Mandatory field is empty", "Please enter Defect Name");
			return;
		}
		/** Mandatory Check for Reason for Change */
		if(QiCommonUtil.isMandatoryFieldEmpty(getDialog().getReasonForChangeTextArea())){
			displayErrorMessage("Mandatory field is empty", "Please enter Reason for Change");
			return;
		}
		try{
			List<QiPartDefectCombination> qiPartDefectCombinationsList;
			qiPartDefectCombinationsList = getModel().findDefectInPartDefectCombination(oldDefectName);
			if(!qiPartDefectCombinationsList.isEmpty()){
				updateAssociatedDefect(qiPartDefectCombinationsList);
				
			}else{
				if(!(oldDefectName.equalsIgnoreCase(defectName))){
					updateDefectName = MessageDialog.confirm(getDialog(), DEFECT_NAME_UPDATE_MESSAGE);
					if(!updateDefectName)
						return;
					else if(updateDefectName && getModel().isDefectExists(defectName)){
						displayErrorMessage("Failed to modify defect as the defect name "+defectName+" already exists!","Failed to modify defect as the defect name already exists!");
						return;
					}
				}
				if (isUpdated(qiDefectCloned)) {
					return;
				}
				setDefectValue(StringUtils.EMPTY,oldDefectName);
			}
			
		}
		catch (Exception e) {
			handleException("An error occured in updateDefect method " , "Failed to Update Defect ", e);
		}
	}
	/**
	 * When user clicks on cancel button in the popup screen cancelDefect method gets called.
	 */
	public void cancelBtnAction(ActionEvent event) {
		LoggedButton cancelBtn = getDialog().getCancelBtn();
		try {
			Stage stage = (Stage) cancelBtn.getScene().getWindow();
			getDialog().setCancel(true);
			stage.close();
		} catch (Exception e) {
			handleException("An error occured in during cancel action ", "Failed to perform cancel action", e);
		}
	}
	/**
	 * This method is used to set Defect Value inside entity to create/update
	 * @param action
	 * @param oldDefectName
	 */
	private void setDefectValue(String action, String oldDefectName){
		LoggedButton updateBtn = getDialog().getCreateBtn();
		qiDefect.setDefectTypeName(QiCommonUtil.delMultipleSpaces(getDialog().getDefectNameTextField()).trim());
		qiDefect.setDefectTypeDescriptionShort(QiCommonUtil.delMultipleSpaces(getDialog().getDefectAbbrTextField()).trim());
		qiDefect.setDefectTypeDescriptionLong(QiCommonUtil.delMultipleSpaces(getDialog().getDefectDescTextField()).trim());
		qiDefect.setDefectCategoryName((String) getDialog().getDefectCategoryComboBox().getSelectionModel().getSelectedItem());
		qiDefect.setActive(getDialog().getActiveRadioBtn().isSelected());
		qiDefect.setPrimaryPosition(getDialog().getPrimaryRadioBtn().isSelected());
		if(action.isEmpty()){
			qiDefect.setUpdateUser(getUserId());
			Stage stage= (Stage) updateBtn.getScene().getWindow();
			((ItemMaintenanceModel)getModel()).updateDefect(qiDefect,oldDefectName);	
			//call to prepare and insert audit data
			AuditLoggerUtil.logAuditInfo(qiDefectCloned, qiDefect, getDialog().getReasonForChangeTextArea().getText(), getDialog().getScreenName(),getUserId());
			stage.close();
		}
	}

	@Override
	public void initListeners() {
		addFieldListener(getDialog().getDefectNameTextField(),true);
		addFieldListener(getDialog().getDefectAbbrTextField(),true);
		addFieldListener(getDialog().getDefectDescTextField(),true);
		setTextFieldListener(getDialog().getDefectNameTextField());
		setTextFieldListener(getDialog().getDefectAbbrTextField());
		setTextFieldListener(getDialog().getDefectDescTextField());
		getDialog().getDefectNameTextField().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(45));
		getDialog().getDefectAbbrTextField().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(10));
		getDialog().getDefectDescTextField().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(30));
		getDialog().getReasonForChangeTextArea().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(256));
		
		getDialog().getDefectCategoryComboBox().valueProperty().addListener(updateEnablerForStringValueChange);
		getDialog().getActiveRadioBtn().getToggleGroup().selectedToggleProperty().addListener(updateEnablerForToggle);
		getDialog().getPrimaryRadioBtn().getToggleGroup().selectedToggleProperty().addListener(updateEnablerForToggle);
	}

	public QiDefect getQiDefect() {
		return qiDefect;
	}

	public void setQiDefect(QiDefect qiDefect) {
		this.qiDefect = qiDefect;
	}

	/**
	 * This method is used to modify defect which is associated with other screen
	 */
	private void updateAssociatedDefect(List<QiPartDefectCombination> qiPartDefectCombinationsList){
		List<String> entryScreenList = new ArrayList<String>();
		List<Integer> partDefectIdList = new ArrayList<Integer>();
		String oldDefectName = qiDefect.getDefectTypeName();
		String defectName = StringUtils.trim(getDialog().getDefectNameTextField().getText());
		boolean previousPosition = qiDefect.isPrimaryPostion();
		boolean currentPostion = getDialog().getPrimaryRadioBtn().isSelected();
		boolean wasActive = qiDefect.isActive();
		boolean isActive = getDialog().getActiveRadioBtn().isSelected();
		boolean posChanged = false;
		if(previousPosition!=currentPostion){
			MessageDialog.showError(getDialog(),DESIRED_POSITION_MESSAGE);
			posChanged = true;
		}
		/** Inspection Location can not be updated if we try to change desired position */
		if(!posChanged){
			try{
				boolean isDefectUpdated = false;
				boolean updateCombination = false;
				if(!(oldDefectName.equalsIgnoreCase(defectName)) || (!isActive && wasActive)){
					for(QiPartDefectCombination qiPartDefectCombination : qiPartDefectCombinationsList){
						partDefectIdList.add(qiPartDefectCombination.getRegionalDefectCombinationId());
						for(QiEntryScreenDefectCombination qiEntryScreenDefectCombination : getModel().findDefectInEntryScreenDefectCombination(qiPartDefectCombination.getRegionalDefectCombinationId())){
							if(!entryScreenList.contains(qiEntryScreenDefectCombination.getId().getEntryScreen())){
								entryScreenList.add(qiEntryScreenDefectCombination.getId().getEntryScreen());
							}
						}
					}
					List<QiPartDefectCombination> regionalAttributeList= getModel().findAllRegionalAttributesByPartDefectId(partDefectIdList);
					
					isDefectUpdated = MessageDialog.confirm(getDialog(), QiCommonUtil.getMessage("defect(s)",qiPartDefectCombinationsList.size(),regionalAttributeList.size(),0).toString());

					if(!isDefectUpdated)
						return;
					else if(isDefectUpdated && getModel().isDefectExists(defectName) && !(oldDefectName.equalsIgnoreCase(defectName))){
						displayErrorMessage("Failed to modify defect as the defect name "+defectName+" already exists!","Failed to modify defect as the defect name already exists!");
						return;
					}
					else if (isDefectUpdated &&  (!isActive && wasActive)){
						String returnValue=isLocalSiteImpacted(partDefectIdList,getDialog().getScreenName(),getDialog());
						if(returnValue.equals(QiConstant.NO_LOCAL_SITES_CONFIGURED)){
							return;
						}
						else if(returnValue.equals(QiConstant.LOCAL_SITES_IMPACTED)){
							displayErrorMessage("Inactivation of the defect affects Local Sites.", "Inactivation of the defect affects Local Sites.");
							return;
						}
						else
						{
							updateCombination = true;
						}
					}
					else{
						updateCombination = true;
					}
				}
				if (isUpdated(qiDefectCloned)) {
					return;
				}
				if (updateCombination) {
					updatePartDefCombination(qiDefect,qiPartDefectCombinationsList);
				}
				setDefectValue(StringUtils.EMPTY,oldDefectName);
			}catch(Exception e){
				handleException("Error in updateAssociatedDefect method", "Falied to update Defect", e);
			}
		}
	}

	/**
	 *  This method is called when user wants to update Defect Name
	 *  @param qiDefect
	 */

	private void updatePartDefCombination(QiDefect qiDefect,List<QiPartDefectCombination> qiPartDefectCombinationsList){
		String defectName = StringUtils.trim(QiCommonUtil.delMultipleSpaces(getDialog().getDefectNameTextField()));
		String preDefectName =qiDefect.getDefectTypeName();
		qiDefect.setActive(getDialog().getActiveRadioBtn().isSelected());

		for (QiPartDefectCombination qiPartDefectCombination : qiPartDefectCombinationsList) {
			if(qiPartDefectCombination.getDefectTypeName().equals(preDefectName)){
				qiPartDefectCombination.setDefectTypeName(defectName);
			}
			if(qiPartDefectCombination.getDefectTypeName2().equals(preDefectName)){
				qiPartDefectCombination.setDefectTypeName2(defectName);
			}
			qiPartDefectCombination.setActive(qiDefect.getActiveValue());
			qiPartDefectCombination.setUpdateUser(getUserId());
			getModel().updatePartDefectCombination(qiPartDefectCombination);
		}
	}
}
