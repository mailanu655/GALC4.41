package com.honda.galc.client.teamleader.qi.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.model.EntryScreenMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.EntryScreenDialog;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dto.qi.QiEntryScreenDto;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.enumtype.QiEntryModelVersioningStatus;
import com.honda.galc.entity.qi.QiEntryScreen;
import com.honda.galc.entity.qi.QiEntryScreenDefectCombination;
import com.honda.galc.entity.qi.QiEntryScreenDept;
import com.honda.galc.entity.qi.QiEntryScreenId;
import com.honda.galc.entity.qi.QiLocalDefectCombination;
import com.honda.galc.entity.qi.QiStationEntryScreen;
import com.honda.galc.entity.qi.QiTextEntryMenu;
import com.honda.galc.util.AuditLoggerUtil;
/**
 * 
 *    
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017
 * Simulation changes
 */

public class EntryScreenDialogController extends QiDialogController<EntryScreenMaintenanceModel,EntryScreenDialog> {

	private QiEntryScreenDto entryScreenDto;
	public EntryScreenDialogController(EntryScreenMaintenanceModel model, EntryScreenDialog dialog, QiEntryScreenDto entryScreenDto) {
		super();
		setModel(model);
		setDialog(dialog);
		this.entryScreenDto = entryScreenDto;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initListeners() {
		validationForTextfield();
		addFieldListener(getDialog().getEntryScreenNameTextField(),true);
		addFieldListener(getDialog().getEntryScreenDescriptionTextField(),true);
		setTextFieldListener(getDialog().getEntryScreenNameTextField());
		setTextFieldListener(getDialog().getEntryScreenDescriptionTextField());
		addProductTypeComboboxListener();
		addEntryModelComboboxListener();
		addPlantComboboxListener();
		addEntryDepartmentListViewListener();
		
		getDialog().getProductTypeCombobox().valueProperty().addListener(updateEnablerForStringValueChange);
		getDialog().getPlantCombobox().valueProperty().addListener(updateEnablerForStringValueChange);
		getDialog().getEntryModelCombobox().valueProperty().addListener(updateEnablerForStringValueChange);
		
		getDialog().getActiveRadioBtn().getToggleGroup().selectedToggleProperty().addListener(updateEnablerForToggle);
		getDialog().getImageRadioButton().getToggleGroup().selectedToggleProperty().addListener(updateEnablerForToggle);
		
		getDialog().getEntryDepartmentListViewMultiSelection().getSelectionModel().selectedItemProperty().addListener(updateEnablerForStringValueChange);
	}

	public void handleException(String loggerMsg, String errMsg,
			String parentScreen, Exception e) {
		getLogger().error(e, new LogRecord(errMsg));
		EventBusUtil.publish(new StatusMessageEvent(errMsg, StatusMessageEventType.ERROR));
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource() instanceof LoggedButton) {
			LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();			
			if(QiConstant.CREATE.equals(loggedButton.getText())) createBtnAction(actionEvent);
			else if(QiConstant.UPDATE.equals(loggedButton.getText())) updateBtnAction(actionEvent);
			else if(QiConstant.CANCEL.equals(loggedButton.getText())) cancelBtnAction(actionEvent);
		} 
	}

	private void createBtnAction(ActionEvent actionEvent) {
		LoggedButton createBtn = getDialog().getCreateBtn();
		String entryScreenName=StringUtils.trim(getDialog().getEntryScreenNameTextField().getText());
		String entryModelName=StringUtils.trim(getDialog().getEntryModelCombobox().getValue().toString());		

		if(QiCommonUtil.isMandatoryFieldEmpty(getDialog().getProductTypeCombobox())){
			displayErrorMessage("Mandatory field is empty", "Please select Product Type");
			return;
		}

		if(QiCommonUtil.isMandatoryFieldEmpty(getDialog().getEntryModelCombobox())){
			displayErrorMessage("Mandatory field is empty", "Please select Entry Model");
			return;
		}

		if(QiCommonUtil.isMandatoryFieldEmpty(getDialog().getPlantCombobox())){
			displayErrorMessage("Mandatory field is empty", "Please select a Plant");
			return;
		}

		if(QiCommonUtil.isMandatoryFieldEmpty(getDialog().getEntryDepartmentListViewMultiSelection())){
			displayErrorMessage("Mandatory field is empty", "Please select Entry Department");
			return;
		}

		if(QiCommonUtil.isMandatoryFieldEmpty(getDialog().getEntryScreenNameTextField())){
			displayErrorMessage("Mandatory field is empty", "Please enter Entry Screen Name");
			return;
		}
		short version = (short) QiEntryModelVersioningStatus.getId(getDialog().getEntryModelVersionLabelText().getText());
		QiEntryScreen entryScreen = new QiEntryScreen(entryScreenName, entryModelName, version);
		try{  
			entryScreen.setCreateUser(getUserId());
			entryScreen.setActive(getDialog().getActiveRadioBtn().isSelected());
			entryScreen.setProductType(getDialog().getProductTypeCombobox().getSelectionModel().getSelectedItem().toString());
			entryScreen.setEntryScreenDescription(StringUtils.trim(getDialog().getEntryScreenDescriptionTextField().getText().toUpperCase()));
			entryScreen.setImage(getDialog().getImageRadioButton().isSelected());
			List<String> selectedEntryDepartmentList;
			List<QiEntryScreenDept> newselectedRepairMethodList =new ArrayList<QiEntryScreenDept>();
			selectedEntryDepartmentList=getDialog().getEntryDepartmentListViewMultiSelection().getSelectionModel().getSelectedItems();		
			for(String entryDept : selectedEntryDepartmentList){
				QiEntryScreenDept entryScreenDept=new QiEntryScreenDept(entryScreenName, entryDept, entryModelName, version);
				entryScreenDept.setCreateUser(getUserId());	
				newselectedRepairMethodList.add(entryScreenDept);
			}
			if(((EntryScreenMaintenanceModel)getModel()).isEntryScreenExists(entryScreen.getId()))
				displayErrorMessage("Entry Screen already exists!","Entry Screen already exists!");
			else{
				((EntryScreenMaintenanceModel)getModel()).createEntryScreen(entryScreen);				
				((EntryScreenMaintenanceModel)getModel()).createEntryScreenDept(newselectedRepairMethodList);
				Stage stage = (Stage) createBtn.getScene().getWindow();
				stage.close();
			}
		}catch (Exception e) {
			handleException("An error occured in create Repair method Failed to Create Entry Screen Name ", StringUtils.EMPTY, e);
		}
	}




	private void updateBtnAction(ActionEvent actionEvent) {
		boolean updateEntryScreenName;

		String oldEntryScreenName = entryScreenDto.getEntryScreen();
		String entryScreenName=StringUtils.trim(getDialog().getEntryScreenNameTextField().getText());
		String entryModelName=StringUtils.trim(getDialog().getEntryModelCombobox().getValue().toString());			
		if(QiCommonUtil.isMandatoryFieldEmpty(getDialog().getProductTypeCombobox())){
			displayErrorMessage("Mandatory field is empty", "Please select Product Type");
			return;
		}
		
		if(StringUtils.isEmpty(getDialog().getEntryModelCombobox().getValue().toString())) {
			displayErrorMessage("Mandatory field is empty", "Please select Entry Model");
			return;
		}


		if(QiCommonUtil.isMandatoryFieldEmpty(getDialog().getPlantCombobox())){
			displayErrorMessage("Mandatory field is empty", "Please select a Plant");
			return;
		}

		if(QiCommonUtil.isMandatoryFieldEmpty(getDialog().getEntryDepartmentListViewMultiSelection())){
			displayErrorMessage("Mandatory field is empty", "Please select Entry Department");
			return;
		}

		if(QiCommonUtil.isMandatoryFieldEmpty(getDialog().getEntryScreenNameTextField())){
			displayErrorMessage("Mandatory field is empty", "Please enter Entry Screen Name");
			return;
		}

		if(QiCommonUtil.isMandatoryFieldEmpty(getDialog().getReasonForChangeTextArea())){
			displayErrorMessage("Mandatory field is empty", "Please enter Reason for Change");
			return;
		}
		
		if((!oldEntryScreenName.equalsIgnoreCase(entryScreenName)) && entryScreenDto.getIsUsedVersion() == (short)0 
				&& getModel().isVersionCreated(entryScreenDto.getEntryModel())) {
			displayErrorMessage("Cannot update the Name as the version exists.", "Cannot update the Name as the version exists.");
			return;
		}

		try{	
			QiEntryScreenId entryScreenId = new QiEntryScreenId(entryScreenName, entryModelName, entryScreenDto.getIsUsedVersion());
			List<QiEntryScreenDefectCombination> entryScreenDefectCombList = getModel().findAllAssociatedPartsByEntryScreen(entryScreenDto);
			List<QiTextEntryMenu> entryScreenMenuCombList = getModel().findAllAssociatedMenusByEntryScreen(entryScreenDto);	
			List<QiStationEntryScreen> entryScreenStationCombList = getModel().findAllAssociatedStationsByEntryScreen(entryScreenDto);
			List<QiLocalDefectCombination> entryScreenLocalDefectCombList = getModel().findAllAssociatedLocalDefectsByEntryScreen(entryScreenDto);
			if(!entryScreenDefectCombList.isEmpty() || !entryScreenMenuCombList.isEmpty() || !entryScreenStationCombList.isEmpty() || !entryScreenLocalDefectCombList.isEmpty()){
				updateAssociatedEntryScreens(entryScreenDefectCombList, entryScreenMenuCombList, entryScreenStationCombList, entryScreenLocalDefectCombList);
			}else{
				if(!(oldEntryScreenName.equalsIgnoreCase(entryScreenName))){
					updateEntryScreenName = MessageDialog.confirm(getDialog(), "Are you sure you want to update Entry Screen?");
					if(!updateEntryScreenName)
						return;
					else if(updateEntryScreenName && getModel().isEntryScreenExists(entryScreenId)){
						displayErrorMessage("Entry Screen already exists!","Entry Screen already exists!");
						return;
					}
				}			
				if (isUpdated(convertDtoToEntryScreen(entryScreenDto))) {
					return;
				}
				setEntryScreenValue(StringUtils.EMPTY,oldEntryScreenName);
			}			
		}
		catch (Exception e) {
			handleException("An error occured in update entry screen " , "Failed to Update Entry Screen ", e);
		}
	}

	private void setEntryScreenValue(String action, String oldEntryScreenName) {
		String entryScreenName = StringUtils.trim(getDialog().getEntryScreenNameTextField().getText());
		String entryModelName = StringUtils.trim(getDialog().getEntryModelCombobox().getValue().toString());
		QiEntryScreenDept entryScreenDeptCloned = convertDtoToEntryScreenDept(entryScreenDto);
		LoggedButton updateBtn = getDialog().getUpdateButton();
		List<String> selectedEntryDepartmentList;	
		List<String> entryDeptOldList = new ArrayList<String>();	
		List<QiEntryScreenDept> newSelectedEntryScreenList = new ArrayList<QiEntryScreenDept>();
		List<String> newSelectedDeptList = new ArrayList<String>();
		QiEntryScreen entryScreen = getEntryScreenObject(entryScreenName, entryModelName);
		selectedEntryDepartmentList = getDialog().getEntryDepartmentListViewMultiSelection().getSelectionModel().getSelectedItems();
		ObservableList<String> entryDepartmentDtoList = FXCollections.observableArrayList(Arrays.asList(entryScreenDto.getDivisionId().split(",")));						
		for(String allEntryDepartments : entryDepartmentDtoList){
			entryDeptOldList.add(allEntryDepartments.trim());
		}

		for(String entryScreenList:selectedEntryDepartmentList){
			QiEntryScreenDept entryScreenDept=new QiEntryScreenDept(entryScreenName, entryScreenList , entryModelName, entryScreenDto.getIsUsedVersion());
			entryScreenDept.setUpdateUser(getUserId());	
			//call to prepare and insert audit data
			AuditLoggerUtil.logAuditInfo(entryScreenDeptCloned, entryScreenDept, getDialog().getReasonForChangeTextArea().getText(), getDialog().getScreenName(),getUserId());
			newSelectedEntryScreenList.add(entryScreenDept);	
		} 

		for(QiEntryScreenDept entryScreenList : newSelectedEntryScreenList){
			String entryItems = entryScreenList.getId().getDivisionId();		
			newSelectedDeptList.add(entryItems);	
		} 

		if(action.isEmpty()){
			
			QiEntryScreenId oldEntryScreenId = new QiEntryScreenId();
			oldEntryScreenId.setEntryModel(entryScreenDto.getEntryModel());
			oldEntryScreenId.setEntryScreen(entryScreenDto.getEntryScreen());
			oldEntryScreenId.setIsUsed(entryScreenDto.getIsUsedVersion());
			QiEntryScreen oldEntryScreen = getModel().findEntryScreenByKey(oldEntryScreenId);
			
			if(!oldEntryScreen.getImageName().equals(StringUtils.EMPTY) && getDialog().getImageRadioButton().isSelected())
				entryScreen.setImageName(oldEntryScreen.getImageName());
			
			((EntryScreenMaintenanceModel)getModel()).updateByEntryScreen(entryScreen, entryScreenDto);
			//call to prepare and insert audit data)
			AuditLoggerUtil.logAuditInfo(oldEntryScreen, entryScreen, getDialog().getReasonForChangeTextArea().getText(), 
					getDialog().getScreenName(),getUserId());	

			updateEntryScreenDept(entryScreenName, entryModelName, entryDeptOldList,
					newSelectedEntryScreenList, newSelectedDeptList);
			newSelectedEntryScreenList.clear();

			deleteEntryScreenDept(oldEntryScreenName, entryScreenName, entryModelName, entryDeptOldList,
					newSelectedEntryScreenList, newSelectedDeptList);
			newSelectedEntryScreenList.clear();

			createEntryScreenDept(entryScreenName, entryModelName, entryDeptOldList, newSelectedEntryScreenList,
					newSelectedDeptList);

			Stage stage= (Stage) updateBtn.getScene().getWindow();
			stage.close();
		}
	}

	private QiEntryScreen getEntryScreenObject(String entryScreenName, String entryModelName) {
		QiEntryScreen entryScreen = new QiEntryScreen(entryScreenName, entryModelName, entryScreenDto.getIsUsedVersion());	
		entryScreen.setUpdateUser(getUserId());
		entryScreen.setActive(getDialog().getActiveRadioBtn().isSelected());
		entryScreen.setProductType(getDialog().getProductTypeCombobox().getSelectionModel().getSelectedItem().toString());
		entryScreen.setEntryScreenDescription(QiCommonUtil.delMultipleSpaces(getDialog().getEntryScreenDescriptionTextField()));
		entryScreen.setImage(getDialog().getImageRadioButton().isSelected());		
		return entryScreen;
	}
	
	private void createEntryScreenDept(String entryScreenName, String entryModelName, List<String> entryDeptOldList,
			List<QiEntryScreenDept> newSelectedEntryScreenList, List<String> newSelectedDeptList) {
		List<String> inNewButNotInOldList=new ArrayList<String>(newSelectedDeptList);
		inNewButNotInOldList.removeAll(entryDeptOldList);			
		for(String inNewButNotInOldListT:inNewButNotInOldList){
			QiEntryScreenDept entryScreenDept=new QiEntryScreenDept(entryScreenName, inNewButNotInOldListT, entryModelName, entryScreenDto.getIsUsedVersion());
			entryScreenDept.setCreateUser(getUserId());	
			newSelectedEntryScreenList.add(entryScreenDept);
		} 
		if(newSelectedEntryScreenList.size() > 0)
			((EntryScreenMaintenanceModel)getModel()).createEntryScreenDept(newSelectedEntryScreenList);
	}

	private void deleteEntryScreenDept(String oldEntryScreenName, String entryScreenName, String entryModelName,
			List<String> entryDeptOldList, List<QiEntryScreenDept> newSelectedEntryScreenList,
			List<String> newSelectedDeptList) {
		
		List<String> inOldButNotInNewList = new ArrayList<String>(entryDeptOldList);
		inOldButNotInNewList.removeAll(newSelectedDeptList);
		for (String inOldButNotInNewListT : inOldButNotInNewList) {
			QiEntryScreenDept entryScreenDept = new QiEntryScreenDept(entryScreenName, inOldButNotInNewListT, 
					entryModelName, entryScreenDto.getIsUsedVersion());
			newSelectedEntryScreenList.add(entryScreenDept);
		}
		if (newSelectedEntryScreenList.size() > 0)
			((EntryScreenMaintenanceModel) getModel()).deleteAllEntryScreenDept(newSelectedEntryScreenList);
	}

	private void updateEntryScreenDept(String entryScreenName, String entryModelName, List<String> entryDeptOldList,
			List<QiEntryScreenDept> newSelectedEntryScreenList, List<String> newSelectedDeptList) {
		
		List<String> inBothTheLists = new ArrayList<String>(entryDeptOldList);
		inBothTheLists.retainAll(newSelectedDeptList);
		newSelectedEntryScreenList.clear();
		for (String deptInBothTheList : inBothTheLists) {
			QiEntryScreenDept entryScreenDept = new QiEntryScreenDept(entryScreenName, deptInBothTheList,
					entryModelName, entryScreenDto.getIsUsedVersion());
			entryScreenDept.setUpdateUser(getUserId());
			newSelectedEntryScreenList.add(entryScreenDept);
		}
		if (newSelectedEntryScreenList.size() > 0)
			if (!entryScreenDto.getEntryScreen().equalsIgnoreCase(entryScreenName)
					|| !entryScreenDto.getEntryModel().equalsIgnoreCase(entryModelName))
				((EntryScreenMaintenanceModel) getModel()).updateAllEntryScreenDept(newSelectedEntryScreenList, entryScreenDto);
	}

	private void cancelBtnAction(ActionEvent actionEvent) {
		LoggedButton cancelBtn = getDialog().getCancelBtn();
		try {
			Stage stage = (Stage) cancelBtn.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occured in during cancel action Failed to perform cancel action", StringUtils.EMPTY, e);
		}

	}

	@SuppressWarnings("unchecked")
	private void addProductTypeComboboxListener() {
		getDialog().getProductTypeCombobox().valueProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				clearDisplayMessage();				
				getDialog().getEntryModelCombobox().getItems().clear();
				loadEntryModelComboboxList();				
			}
		});		
	}

	@SuppressWarnings("unchecked")
	private void addPlantComboboxListener() {
		getDialog().getPlantCombobox().valueProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				clearDisplayMessage();				
				getDialog().getEntryDepartmentListViewMultiSelection().getItems().clear();
				loadEntryDepartmentListViewMultiSelection();
			}
		});		
	}
	
	@SuppressWarnings("unchecked")
	private void addEntryModelComboboxListener() {
		getDialog().getEntryModelCombobox().valueProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				clearDisplayMessage();				
				String version = getModel().findEntryModelVersionStatus(newValue);
				getDialog().getEntryModelVersionLabelText().setText(version);
			}
		});		
	}

	@SuppressWarnings("unchecked")
	private void loadEntryModelComboboxList() {
		try {
			List<String> entryModelList = getModel().findEntryModelByProductType((String)getDialog().getProductTypeCombobox().getValue());
			if (entryModelList.size() > 0) {
				getDialog().getEntryModelCombobox().getItems().addAll(entryModelList);
				getDialog().getEntryModelCombobox().getSelectionModel().select(0);
				String version = getModel().findEntryModelVersionStatus((String)getDialog().getEntryModelCombobox().getSelectionModel().getSelectedItem());
				getDialog().getEntryModelVersionLabelText().setText(version);
			}
		} catch (Exception e) {
			handleException("An error occurred while fetching Entry Model list", "Failed to get Entry Model", e);
		}
	}

	private void loadEntryDepartmentListViewMultiSelection() {
		List<String> entryDepartmentNewList;
		try {
			List<Division> entryDepartmentList = getModel().findAllDivisionByPlant(getModel().getSiteName(),(String)getDialog().getPlantCombobox().getValue());

			if (entryDepartmentList.size() > 0) {
				entryDepartmentNewList = new ArrayList<String>();
				for(Division division:entryDepartmentList){
					entryDepartmentNewList.add(division.getDivisionId().trim());
				}
				getDialog().getEntryDepartmentListViewMultiSelection().getItems().addAll(entryDepartmentNewList);
			}
		} catch (Exception e) {
			handleException("An error occurred while fetching Entry Model list", "Failed to get Entry Department", e);
		}
	}

	private void validationForTextfield(){
		getDialog().getEntryScreenNameTextField().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(34));
		getDialog().getEntryScreenDescriptionTextField().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(80));
		getDialog().getReasonForChangeTextArea().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(256));		
	}

	private void addEntryDepartmentListViewListener() {
		getDialog().getEntryDepartmentListViewMultiSelection().getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				Logger.getLogger().check("Entry Department "+getDialog().getEntryDepartmentListViewMultiSelection().getSelectionModel().getSelectedItem() + " selected") ;
			}
		});
	}

	private QiEntryScreenDept convertDtoToEntryScreenDept(QiEntryScreenDto qiEntryScreenDto) {
		QiEntryScreenDept entryScreenDept =new QiEntryScreenDept(qiEntryScreenDto.getEntryScreen(), qiEntryScreenDto.getDivisionId(),qiEntryScreenDto.getEntryModel(),qiEntryScreenDto.getIsUsedVersion());
		return entryScreenDept;
	}
	
	private QiEntryScreen convertDtoToEntryScreen(QiEntryScreenDto dto) {
		QiEntryScreen entity = new QiEntryScreen(dto.getEntryScreen(), dto.getEntryModel(),dto.getIsUsedVersion());
		if(dto.getUpdateTimestamp()!=null)
			entity.setUpdateTimestamp(dto.getUpdateTimestamp());
		return entity;
	}

	private void updateAssociatedEntryScreens(List<QiEntryScreenDefectCombination> entryScreenDefectCombList,List<QiTextEntryMenu> entryScreenMenuCombList,List<QiStationEntryScreen> entryScreenStationCombList,List<QiLocalDefectCombination> entryScreenLocalDefectCombList){
		String oldEntryScreenName = entryScreenDto.getEntryScreen();
		String entryScreenName = StringUtils.trim(getDialog().getEntryScreenNameTextField().getText());
		String entryModelName = StringUtils.trim(getDialog().getEntryModelCombobox().getValue().toString());
		boolean isActive = getDialog().getActiveRadioBtn().isSelected();
		QiEntryScreenId entryScreenId = new QiEntryScreenId(entryScreenName, entryModelName,
				entryScreenDto.getIsUsedVersion());
		
		try{
			boolean isEntryScreenUpdated = false;	
			if(!isActive){
				StringBuilder message = getMessage(entryScreenDefectCombList, entryScreenMenuCombList,
						entryScreenStationCombList, entryScreenLocalDefectCombList);
				MessageDialog.showError(message.toString());				
			}
			else if(!(oldEntryScreenName.equalsIgnoreCase(entryScreenName)) && isActive && (entryScreenDto.isImage()?getDialog().getImageRadioButton().isSelected():getDialog().getTextRadioButton().isSelected())){
				StringBuilder message = getMessage(entryScreenDefectCombList, entryScreenMenuCombList,
						entryScreenStationCombList, entryScreenLocalDefectCombList);
				message.append(". Do you still want to continue?");
				isEntryScreenUpdated = MessageDialog.confirm(getDialog(),message.toString());
				if(!isEntryScreenUpdated)
					return;
				else if(isEntryScreenUpdated && getModel().isEntryScreenExists(entryScreenId) && !(oldEntryScreenName.equalsIgnoreCase(entryScreenName))){
					displayErrorMessage("Entry Screen already exists!","Entry Screen already exists!");
					return;
				}else{
					if(changeStateOfEntryScreen(entryScreenDefectCombList, entryScreenMenuCombList, entryScreenStationCombList, entryScreenLocalDefectCombList))
						((EntryScreenMaintenanceModel)getModel()).updateAllByEntryScreen(entryScreenId, entryScreenDto);
					else
						return;
				}


				setEntryScreenValue(StringUtils.EMPTY,oldEntryScreenName);
			}
			else {
				if (changeStateOfEntryScreen(entryScreenDefectCombList, entryScreenMenuCombList, entryScreenStationCombList, entryScreenLocalDefectCombList)) {
					((EntryScreenMaintenanceModel) getModel()).updateAllByEntryScreen(entryScreenId, entryScreenDto);
					setEntryScreenValue(StringUtils.EMPTY, oldEntryScreenName);
				} else
					return;
			}

		}catch(Exception e) {
			handleException("Error in updateAssociatedParts method", "Falied to update Parts", e);
		}

	}

	private StringBuilder getMessage(List<QiEntryScreenDefectCombination> entryScreenDefectCombList,
			List<QiTextEntryMenu> entryScreenMenuCombList, List<QiStationEntryScreen> entryScreenStationCombList,
			List<QiLocalDefectCombination> entryScreenLocalDefectCombList) {
		boolean flag=false;
		StringBuilder message=new StringBuilder("The Entry Screen(s) being updated is associated with "); 
		if(entryScreenMenuCombList.size()>0){
			message.append(entryScreenMenuCombList.size() + " Menu(s), ");
		}
		if(entryScreenDefectCombList.size()>0){
			message.append(entryScreenDefectCombList.size() + " Part Defect Comb(s)");
			flag=true; 
		}		
		if(entryScreenStationCombList.size()>0){
			if(flag)
				message.append(", ");
			message.append(entryScreenStationCombList.size() + " Qics Station Entry Screen(s)");
		}
		if(entryScreenLocalDefectCombList.size()>0){
			if(flag)
				message.append(", ");
			message.append(entryScreenLocalDefectCombList.size() + " Local Defect Comb(s)");
		}
		return message;
	}

	private boolean changeStateOfEntryScreen(List<QiEntryScreenDefectCombination> entryScreenDefectCombList,List<QiTextEntryMenu> entryScreenMenuCombList,List<QiStationEntryScreen> entryScreenStationCombList,List<QiLocalDefectCombination> entryScreenLocalDefectCombList) {
		boolean isEntryScreenUpdated = false;
		if (entryScreenDto.isImage()) {
			if (getDialog().getTextRadioButton().isSelected()) {
				isEntryScreenUpdated = getMessageForTypeConfirm(entryScreenDefectCombList, entryScreenMenuCombList);
				if(!isEntryScreenUpdated)
					return false;				
				else{					
					if (isUpdated(convertDtoToEntryScreen(entryScreenDto))) {
						return false;
					}
					((EntryScreenMaintenanceModel)getModel()).removeAllPartsByEntryScreen(entryScreenDto);
					getModel().updateEntryScreenForImageName(null,getUserId(),entryScreenDto);
					return true;
				}
			}
		}	
		else{
			if (getDialog().getImageRadioButton().isSelected()) {
				isEntryScreenUpdated = getMessageForTypeConfirm(entryScreenDefectCombList, entryScreenMenuCombList);			
				if(!isEntryScreenUpdated)
					return false;				
				else{					
					if (isUpdated(convertDtoToEntryScreen(entryScreenDto))) {
						return false;
					}
					((EntryScreenMaintenanceModel)getModel()).removeAllPartsByEntryScreen(entryScreenDto);
					((EntryScreenMaintenanceModel)getModel()).removeAllMenusByEntryScreen(entryScreenDto);
					return true;
				}
			}
		}
		if (isUpdated(convertDtoToEntryScreen(entryScreenDto))) {
			return false;
		}
		return true;
	}

	private boolean getMessageForTypeConfirm(List<QiEntryScreenDefectCombination> entryScreenDefectCombList,
			List<QiTextEntryMenu> entryScreenMenuCombList) {
		boolean isEntryScreenUpdated;
		StringBuilder message = new StringBuilder("");
		if(entryScreenMenuCombList.size()>0 || entryScreenDefectCombList.size()>0){
			message.append("The Entry Screen(s) being updated is associated with "); 		
			if(entryScreenMenuCombList.size()>0){
				message.append(entryScreenMenuCombList.size() + " Menu(s), ");
			}
			if(entryScreenDefectCombList.size()>0){
				message.append(entryScreenDefectCombList.size() + " Part Defect Comb(s)");
			}
			message.append(". ");
		}
		message.append("Menus and Entry Screen to PDC associations will be deleted. Do you still want to continue?");
		isEntryScreenUpdated = MessageDialog.confirm(getDialog(),message.toString());
		return isEntryScreenUpdated;
	}	
}
