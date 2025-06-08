package com.honda.galc.client.teamleader.qi.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import com.honda.galc.client.teamleader.qi.model.PdcRegionalAttributeMaintModel;
import com.honda.galc.client.teamleader.qi.view.PdcRegionalAttributeMaintDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dto.qi.QiRegionalAttributeDto;
import com.honda.galc.entity.qi.QiIqs;
import com.honda.galc.entity.qi.QiIqsVersion;
import com.honda.galc.entity.qi.QiPartDefectCombination;
import com.honda.galc.entity.qi.QiTheme;
import com.honda.galc.util.AuditLoggerUtil;
import com.honda.galc.util.KeyValue;

/**
 * 
 * <h3>PdcRegionalAttributeMaintDialogController Class description</h3>
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
public class PdcRegionalAttributeMaintDialogController  extends QiDialogController<PdcRegionalAttributeMaintModel, PdcRegionalAttributeMaintDialog> {
	
	private List<QiRegionalAttributeDto> defectCombinationDtoList;
	private List<QiPartDefectCombination> defectCombinationList;
	
	private QiIqs iqs;
	private boolean isUpdateBtn=false;
	private PdcRegionalAttributeMaintPanelController mainPanelController;
	public PdcRegionalAttributeMaintDialogController(PdcRegionalAttributeMaintModel model, PdcRegionalAttributeMaintDialog dialog, List<QiRegionalAttributeDto> defectCombinationDtoList, PdcRegionalAttributeMaintPanelController parentController) {
		super();
		setModel(model);
		setDialog(dialog);
		if(defectCombinationDtoList!=null)
			this.defectCombinationList = convertDtoToEntity(defectCombinationDtoList);
		this.defectCombinationDtoList = defectCombinationDtoList;
		mainPanelController = parentController;
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource().equals(getDialog().getAssignButton())) assignButtonAction(actionEvent);
		else if(actionEvent.getSource().equals(getDialog().getUpdateButton())){ 
			isUpdateBtn=true;
			if(defectCombinationDtoList.size()>1){
				massUpdateAction(actionEvent);
			}else{
				updateButtonAction(actionEvent);
			}
			isUpdateBtn=false;
		}
		else if(actionEvent.getSource().equals(getDialog().getCancelButton())) cancelButtonAction(actionEvent);
	}

	@Override
	public void initListeners() {
		validationForTextfield();
		
		getDialog().getReportableRadioButton().getToggleGroup().selectedToggleProperty().addListener(updateEnablerForToggle);
		getDialog().getIqsCategoryComboBox().valueProperty().addListener(updateEnablerForStringValueChange);
		getDialog().getIqsQuestionComboBox().valueProperty().addListener(updateEnablerForKeyValueChange);
		getDialog().getIqsVersionComboBox().valueProperty().addListener(updateEnablerForStringValueChange);
		getDialog().getThemeComboBox().valueProperty().addListener(updateEnablerForStringValueChange);
	}
	
	/**
	 * Adds the iqs version combo box listener.
	 */
	@SuppressWarnings({ "unchecked" })
	public void addIqsVersionComboBoxListener(){
		getDialog().getIqsVersionComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			 public void changed(ObservableValue<? extends String> ov,  String old_val, String new_val) { 
				 loadCategoryComboBox(new_val);
			 } 
		});
	}
	
	/**
	 * Adds the iqs category combo box listener.
	 */
	@SuppressWarnings({ "unchecked" })
	public void addIqsCategoryComboBoxListener(){
		getDialog().getIqsCategoryComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			 public void changed(ObservableValue<? extends String> ov,  String old_val, String new_val) { 
				 loadQestionComboBox(new_val);
			 } 
		});
	}
	
	/**
	 * Assign button action.
	 *
	 * @param actionEvent the action event
	 */
	private void assignButtonAction(ActionEvent actionEvent) {
		try {
			if(isValidateThemeName()){
				displayErrorMessage("Mandatory field Theme Name is empty", "Please Select Theme Name");
				return;
			}
			if(isValidateIqsVersion()){
				displayErrorMessage("Mandatory field Iqs Version is empty", "Please Select Iqs Version");
				return;
			}
			if(isValidateIqsCategory()){
				displayErrorMessage("Mandatory field Iqs Category is empty", "Please Select Iqs Category");
				return;
			}
			if(isValidateIqsQuestion()){
				displayErrorMessage("Mandatory field Iqs Question is empty", "Please Select Iqs Question");
				return;
			}
			if (isUpdated(defectCombinationList)) {
				return;
			}
			getModel().assignAndUpdateAttributes(setPartDefectCombinationValue(defectCombinationList,defectCombinationDtoList));
			if(defectCombinationList.size() > 0)  {
				mainPanelController.removeSelectedFromList();
			}
			Stage stage = (Stage) getDialog().getAssignButton().getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occured in Assign Attribute method " , "Failed to Assign Attribute ", e);
		}
		
	}
	
	/**
	 * Update button action.
	 *
	 * @param actionEvent the action event
	 */
	private void updateButtonAction(ActionEvent actionEvent) {
		try {
			
			if(isValidateThemeName()){
				displayErrorMessage("Mandatory field Theme Name is empty", "Please Select Theme Name");
				return;
			}
			if(isValidateIqsVersion()){
				displayErrorMessage("Mandatory field Iqs Version is empty", "Please Select Iqs Version");
				return;
			}
			if(isValidateIqsCategory()){
				displayErrorMessage("Mandatory field Iqs Category is empty", "Please Select Iqs Category");
				return;
			}
			if(isValidateIqsQuestion()){
				displayErrorMessage("Mandatory field Iqs Question is empty", "Please Select Iqs Question");
				return;
			}
			if(isValidateReason()){
				displayErrorMessage("Mandatory field is empty", "Please enter Reason for Change");
				return;
			}
			if (isUpdated(defectCombinationList)) {
				return;
			}
			getModel().assignAndUpdateAttributes(setPartDefectCombinationValue(defectCombinationList, defectCombinationDtoList));
			getModel().updateLocalAttribute(defectCombinationList);
			Stage stage = (Stage) getDialog().getUpdateButton().getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occured in updateButtonAction method " , "Failed to Update Attribute ", e);
		}
	}
	
	
	
	/**
	 * Sets the part defect combination value.
	 *
	 * @param iqs the new part defect combination value
	 */
	private List<QiPartDefectCombination> setPartDefectCombinationValue(List<QiPartDefectCombination> partDefectCombinations, List<QiRegionalAttributeDto> dtoList) {
		this.iqs = getModel().findIqs(setIqsValue(new QiIqs()));
		for (QiRegionalAttributeDto dto : dtoList) {
			dto.setIqsId(this.iqs.getIqsId());
			dto.setIqsQuestion(getDialog().getIqsQuestionComboBox().getValue().toString());
			dto.setIqsCategory(getDialog().getIqsCategoryComboBox().getValue().toString());
			dto.setIqsVersion(getDialog().getIqsVersionComboBox().getValue().toString());
			dto.setThemeName(getDialog().getThemeComboBox().getValue().toString());
			dto.setReportable(getDialog().getReportableRadioButton().isSelected()  ? (short)0 : (short)1);
		}
		for (QiPartDefectCombination partDefectCombination : partDefectCombinations) {
			QiPartDefectCombination qiPartDefectCombinationCloned = (QiPartDefectCombination) partDefectCombination.deepCopy();
			partDefectCombination.setIqsId(this.iqs.getIqsId());
			partDefectCombination.setThemeName(getDialog().getThemeComboBox().getValue().toString());
			partDefectCombination.setReportable(getDialog().getReportableRadioButton().isSelected()  ? (short)0 : (short)1);
			if(isUpdateBtn)
				partDefectCombination.setUpdateUser(getUserId());
			else
				partDefectCombination.setCreateUser(getUserId());
			String reasonForChange;
			if(!getDialog().getReasonForChangeTextArea().getText().isEmpty()){
				reasonForChange=getDialog().getReasonForChangeTextArea().getText();
			}else{
				reasonForChange=QiConstant.ASSIGN_REASON_REGIONAL_ATTRIBUTE_FOR_AUDIT;
			}
			//call to prepare and insert audit data
			AuditLoggerUtil.logAuditInfo(qiPartDefectCombinationCloned, partDefectCombination, reasonForChange,getDialog().getScreenName() ,
					getModel().getAuditPrimaryKeyValue(qiPartDefectCombinationCloned.getRegionalDefectCombinationId()),getUserId());
		}
		return partDefectCombinations;
	}
	
	/**
	 * Sets the iqs value.
	 *
	 * @param iqs the iqs
	 * @return the qi iqs
	 */
	@SuppressWarnings("unchecked")
	private QiIqs setIqsValue(QiIqs iqs) {
		iqs.setIqsVersion(getDialog().getIqsVersionComboBox().getValue().toString());
		iqs.setIqsCategory(getDialog().getIqsCategoryComboBox().getValue().toString());
		KeyValue<Integer, String> newValue = (KeyValue<Integer, String>) getDialog().getIqsQuestionComboBox().getSelectionModel().getSelectedItem();
		iqs.setIqsQuestionNo(newValue.getKey());
		iqs.setIqsQuestion(newValue.getValue());
		iqs.setUpdateUser(getUserId());
		return iqs;
	}
	
	/**
	 * Checks if is validate theme name.
	 *
	 * @return true, if is validate theme name
	 */
	private boolean isValidateThemeName() {
		return getDialog().getThemeComboBox().getValue() != null ? false : true;
	}
	
	/**
	 * Checks if is validate iqs category.
	 *
	 * @return true, if is validate iqs category
	 */
	private boolean isValidateIqsCategory() {
		return getDialog().getIqsCategoryComboBox().getValue() != null ? false : true;
	}
	
	/**
	 * Checks if is validate iqs version.
	 *
	 * @return true, if is validate iqs version
	 */
	private boolean isValidateIqsVersion() {
		return getDialog().getIqsVersionComboBox().getValue() != null ? false : true;
	}
	
	/**
	 * Checks if is validate iqs question.
	 *
	 * @return true, if is validate iqs question
	 */
	private boolean isValidateIqsQuestion() {
		return getDialog().getIqsQuestionComboBox().getSelectionModel().getSelectedItem() != null ? false : true;
	}
	
	/**
	 * Checks if is validate reason.
	 *
	 * @return true, if is validate reason
	 */
	private boolean isValidateReason() {
		return QiCommonUtil.isMandatoryFieldEmpty(getDialog().getReasonForChangeTextArea());
	}
	
	/**
	 * Sets the combo box data for update.
	 */
	@SuppressWarnings("unchecked")
	public void setComboBoxDataForUpdate(boolean isSetTheme) {
		loadCategoryComboBox(defectCombinationDtoList.get(0).getIqsVersion());
		loadQestionComboBox(defectCombinationDtoList.get(0).getIqsCategory());
		if(isSetTheme)
			getDialog().getThemeComboBox().getSelectionModel().select(defectCombinationDtoList.get(0).getThemeName());
		getDialog().getIqsVersionComboBox().getSelectionModel().select(defectCombinationDtoList.get(0).getIqsVersion());
		getDialog().getIqsCategoryComboBox().getSelectionModel().select(defectCombinationDtoList.get(0).getIqsCategory());
		getDialog().getIqsQuestionComboBox().getSelectionModel().select(getKeyValue(defectCombinationDtoList.get(0).getIqsQuestionNo(), defectCombinationDtoList.get(0).getIqsQuestion()));
		getDialog().getReportableRadioButton().setSelected(defectCombinationDtoList.get(0).getReportable() == 0);
		getDialog().getNonReportableRadioButton().setSelected(defectCombinationDtoList.get(0).getReportable() == 1);
	}
	
	/**
	 * Load combo box data.
	 */
	public void loadComboBoxData() {
		loadThemeComboxBox();
		loadIqsVersionComboxBox();
	}
	
	/**
	 * Load theme combox box.
	 */
	@SuppressWarnings("unchecked")
	private void loadThemeComboxBox() {
		getDialog().getThemeComboBox().setPromptText("Select");
		for (QiTheme t : getModel().getAllThemes()) {
			getDialog().getThemeComboBox().getItems().add(t.getThemeName());
		}
	}
	
	/**
	 * Load iqs version combox box.
	 */
	@SuppressWarnings("unchecked")
	private void loadIqsVersionComboxBox() {
		getDialog().getIqsVersionComboBox().setPromptText("Select");
		for (QiIqsVersion v : getModel().getAllIqsVersion()) {
			getDialog().getIqsVersionComboBox().getItems().add(v.getIqsVersion());
		}
	}
	
	/**
	 * Load category combo box.
	 *
	 * @param iqsVersion the iqs version
	 */
	@SuppressWarnings("unchecked")
	private void loadCategoryComboBox(String iqsVersion){
		clearComboBox(getDialog().getIqsCategoryComboBox());
		getDialog().getIqsCategoryComboBox().setPromptText("Select");
		getDialog().getIqsCategoryComboBox().getItems().addAll(getModel().findAllCategoriesByVersion(iqsVersion));
		addIqsCategoryComboBoxListener();
	}
	
	/**
	 * Load qestion combo box.
	 *
	 * @param category the category
	 */
	@SuppressWarnings("unchecked")
	private void loadQestionComboBox(String iqsCategory) {
		clearComboBox(getDialog().getIqsQuestionComboBox());
		getDialog().getIqsQuestionComboBox().setPromptText("Select");
		String iqsVersion = getDialog().getIqsVersionComboBox().getValue() != null ? getDialog().getIqsVersionComboBox().getValue().toString() : "";
		List<QiIqs> iqsQuestions = getModel().findAllByVersionAndCategory(iqsVersion, iqsCategory);
		for (QiIqs ques : iqsQuestions) {
			getDialog().getIqsQuestionComboBox().getItems().add(getKeyValue(ques.getIqsQuestionNo(), ques.getIqsQuestion()));
		}
	}
	
	/**
	 * Cancel button action.
	 *
	 * @param actionEvent the action event
	 */
	private void cancelButtonAction(ActionEvent actionEvent) {
		try {
			Stage stage = (Stage) getDialog().getCancelButton().getScene().getWindow();
			stage.close();
			getDialog().setCancel(true);
		} catch (Exception e) {
			handleException("An error occured in during cancel action ", "Failed to perform cancel action",e);
		}
	}
	
	/**
	 * Convert dto to entity.
	 *
	 * @return the qi part defect combination
	 */
	private List<QiPartDefectCombination> convertDtoToEntity(List<QiRegionalAttributeDto> defectCombinationDtoList) {
		QiPartDefectCombination defectCombinationEntity;
		List<QiPartDefectCombination> defectCombinationsList = new ArrayList<QiPartDefectCombination>();
		for (QiRegionalAttributeDto defectCombinationDto : defectCombinationDtoList) {
			defectCombinationEntity = new  QiPartDefectCombination();
			defectCombinationEntity.setRegionalDefectCombinationId(defectCombinationDto.getRegionalDefectCombinationId());
			defectCombinationEntity.setPartLocationId(defectCombinationDto.getPartLocationId());
			defectCombinationEntity.setDefectTypeName(defectCombinationDto.getDefectTypeName());
			defectCombinationEntity.setDefectTypeName2(defectCombinationDto.getDefectTypeName2());
			defectCombinationEntity.setProductKind(getModel().getProductKind());
			defectCombinationEntity.setActive(defectCombinationDto.getActive());
			defectCombinationEntity.setReportable(defectCombinationDto.getReportable());
			defectCombinationEntity.setIqsId(defectCombinationDto.getIqsId() == null ? 0 : defectCombinationDto.getIqsId());
			defectCombinationEntity.setThemeName(defectCombinationDto.getThemeName());
			defectCombinationEntity.setCreateUser(defectCombinationDto.getCreateUser());
			defectCombinationEntity.setUpdateUser(getUserId());
			if(defectCombinationDto.getUpdateTimestamp()!=null)
				defectCombinationEntity.setUpdateTimestamp(new Timestamp(QiCommonUtil.convert(defectCombinationDto.getUpdateTimestamp()).getTime()));
			defectCombinationsList.add(defectCombinationEntity);
		}
		return defectCombinationsList;
		
	}
	
	/**
	 * This method is used for validation
	 */
	private void validationForTextfield() {
		getDialog().getReasonForChangeTextArea().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(256));
	}
	
	/**
	 * Clear combo box.
	 *
	 * @param comboBox the combo box
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void clearComboBox(LoggedComboBox comboBox ) {
		clearDisplayMessage();
		comboBox.getItems().clear();
		comboBox.setValue(null);
		comboBox.getSelectionModel().clearSelection();
	}

	/**
	 * Gets the key value.
	 *
	 * @param key the key
	 * @param value the value
	 * @return the key value
	 */
	private KeyValue<Integer, String> getKeyValue(Integer key, final String value){
		KeyValue<Integer, String> kv = new KeyValue<Integer, String>(key, value){
			private static final long serialVersionUID = 1L;

			@Override
			public String toString() {
				return value;
			}
		};
		return kv;
	}
	
	/**
	 * Update button action for mass Update.
	 * @param actionEvent 
	 */
	private void massUpdateAction(ActionEvent actionEvent) {
		clearDisplayMessage();
		try {
			if(isCheckBoxSelected()){
				if(getDialog().getThemeCheckBox().isSelected()){
					if(isValidateThemeName()){
						displayErrorMessage("Mandatory field Theme Name is empty", "Please select Theme Name");
						return;
					}
				}else if(getDialog().getIqsVersionCheckBox().isSelected()){
					if(isValidateIqsVersion()){
						displayErrorMessage("Mandatory field Iqs Version is empty", "Please select Iqs Version");
						return;
					}
					else if(isValidateIqsCategory()){
						displayErrorMessage("Mandatory field Iqs Category is empty", "Please select Iqs Category");
						return;
					}
					else if(isValidateIqsQuestion()){
						displayErrorMessage("Mandatory field Iqs Question is empty", "Please select Iqs Question");
						return;
					}
				}
			}else{
				displayErrorMessage("Checkbox not selected ", "Please select an item to update");
				return;
			}
			if(isValidateReason()){
				displayErrorMessage("Mandatory field is empty", "Please enter Reason for Change");
				return;
			}
			
			Boolean isUpdateData= MessageDialog.confirm(getDialog() ,"You are performing mass update on "+defectCombinationList.size()+" part defect combinations. Do you wish to continue ?");
			if(isUpdateData){
				if (isUpdated(defectCombinationList)) {
					return;
				}
				getModel().assignAndUpdateAttributes(setPartDefectCombinationMassUpdate(defectCombinationList));
				if(getDialog().getReportableCheckBox().isSelected())
					getModel().updateLocalAttribute(defectCombinationList);
			}
			Stage stage = (Stage) getDialog().getUpdateButton().getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occured in updateButtonAction method " , "Failed to Attribute ", e);
		}
	}
	
	/**
	 * Checks if any checkbox is selected or not for mass update
	 * @return boolean
	 */
	private boolean isCheckBoxSelected() {
		if(getDialog().getReportableCheckBox().isSelected()){
			return true;	
		}
		if(getDialog().getThemeCheckBox().isSelected()){
			return true;	
		}
		if(getDialog().getIqsVersionCheckBox().isSelected()){
			return true;	
		}
		return false;
	}

	/**
	 * Sets the part defect combination value for Mass Update.
	 * @param partDefectCombinationList
	 * @return List<QiPartDefectCombination>
	 */
	private List<QiPartDefectCombination> setPartDefectCombinationMassUpdate(List<QiPartDefectCombination> partDefectCombinationList) {
		List<QiPartDefectCombination> qiPartDefectCombinationsList=new ArrayList<QiPartDefectCombination>();
		if(getDialog().getIqsVersionCheckBox().isSelected())
			this.iqs = getModel().findIqs(setIqsValue(new QiIqs()));
		for (QiPartDefectCombination partDefectCombination : partDefectCombinationList) {
			QiPartDefectCombination qiPartDefectCombinationCloned = (QiPartDefectCombination) partDefectCombination.deepCopy();
			if(getDialog().getIqsVersionCheckBox().isSelected())
				partDefectCombination.setIqsId(this.iqs.getIqsId());
			if(getDialog().getThemeCheckBox().isSelected())
				partDefectCombination.setThemeName(getDialog().getThemeComboBox().getValue().toString());
			if(getDialog().getReportableCheckBox().isSelected())
				partDefectCombination.setReportable(getDialog().getReportableRadioButton().isSelected()  ? (short)0 : (short)1);
			partDefectCombination.setUpdateUser(getUserId());
			//call to prepare and insert audit data
			AuditLoggerUtil.logAuditInfo(qiPartDefectCombinationCloned, partDefectCombination, getDialog().getReasonForChangeTextArea().getText(),getDialog().getScreenName() ,
					getModel().getAuditPrimaryKeyValue(qiPartDefectCombinationCloned.getRegionalDefectCombinationId()),getUserId());
			qiPartDefectCombinationsList.add(partDefectCombination);
		}
		return qiPartDefectCombinationsList;
	}
}
