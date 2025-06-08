package com.honda.galc.client.teamleader.qi.defectTagging;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.controller.QiDialogController;
import com.honda.galc.client.teamleader.qi.defectResult.DefectResultMaintModel;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.qi.QiIncident;

/**
 * 
 * <h3>DefectTaggingController Class description</h3>
 * <p>DefectTaggingController is used to create new incident type for Add/Delete tagging to Defect result</p>
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

public class DefectTaggingController extends QiDialogController<DefectResultMaintModel, DefectTaggingDialog>{
	QiIncident qiIncident;

	public DefectTaggingController(DefectResultMaintModel model,DefectTaggingDialog defectTaggingDialog, String productType,String incidentType) {
		super();
		setModel(model);
		setDialog(defectTaggingDialog);
		loadIncidentData(incidentType);
	}

	private void loadIncidentData(String defectTagValue) {
		if(defectTagValue!=null){
			String incidentTit = defectTagValue.contains("(")?defectTagValue.split("\\(")[0].trim():defectTagValue;
			String incidentDate = defectTagValue.contains("(")?defectTagValue.split("\\(")[1].split("\\)")[0].trim():defectTagValue;
			qiIncident=getModel().findByIncidentTitleAndDate(incidentTit, incidentDate);
		}
		
		
	}

	/**
	 * This is used to create Incident and perform the cancel operation
	 */
	@Override
	public void handle(ActionEvent actionEvent) {
		if (actionEvent.getSource() instanceof LoggedButton) {
			if(((LoggedButton) actionEvent.getSource()).getId().equals(getDialog().getCreateButton().getId())){
				createIncidentActionEvent(actionEvent);
			}else if(((LoggedButton) actionEvent.getSource()).getId().equals(getDialog().getUpdateButton().getId())){
				updateButtonAction(actionEvent);
			}else if(((LoggedButton) actionEvent.getSource()).getId().equals(getDialog().getCancelButton().getId())){
				cancelButtonAction(actionEvent);
			}
		}
	}

	@Override
	public void initListeners() {
		addUIScreenComponentListener();
		addFieldListener(getDialog().getIncidentTitle().getControl(),true);
		

		if (getDialog().getTitle().equals("Defect Tagging Updation")) {
			addTextChangeListener(getDialog().getIncidentTitle().getControl());
			addTextChangeListener(getDialog().getDocumentControlId().getControl());
			addTextChangeListener(getDialog().getDocumentControlType().getControl());
			addTextAreaChangeListener(getDialog().getIncidentCause());
			getDialog().getCriticalRadioButton().getToggleGroup().selectedToggleProperty().addListener(updateEnablerForToggle);
		}
	}

	/**
	 * This method is used to attach event on button
	 * 
	 */
	public void attachEvent(){
		getDialog().getCreateButton().setOnAction(getDialog().getController());
		getDialog().getUpdateButton().setOnAction(getDialog().getController());
		getDialog().getCancelButton().setOnAction(getDialog().getController());
	}
	
	/**
	 * This method is used to handle create button action event
	 * 
	 */
	
	private void createIncidentActionEvent(ActionEvent actionEvent){
		try {
			boolean validationPassed=validationOfUiComponentInputToCreate();
			Stage stage = (Stage) getDialog().getCreateButton().getScene().getWindow();
			if (validationPassed){
				createIncident();
				stage.close();
			}
		} catch (Exception e) {
			handleException("An error occured during create action", StringUtils.EMPTY, e);
		}
		finally{
			actionEvent.consume();
		}
	}
	
	private void updateButtonAction(ActionEvent actionEvent){
		try {
			boolean validationPassed=validationOfUiComponentInputToUpdate();
			Stage stage = (Stage) getDialog().getUpdateButton().getScene().getWindow();
			if (validationPassed){
				//validation for reason for change
				if( QiCommonUtil.isMandatoryFieldEmpty(getDialog().getReasonForChangeTextArea())){
					displayErrorMessage("Please enter Reason for Change ","Please enter Reason for Change ");
					return ;
				}
				if (isUpdated(qiIncident)) {
					return;
				}
				updateIncident();
				stage.close();
			}
		} catch (Exception e) {
			handleException("An error occured during create action", StringUtils.EMPTY, e);
		}
		finally{
			actionEvent.consume();
		}
	}
	
	
	/**
	 * This method is used to handle validation of UiComponentInput
	 * 
	 */
	private boolean validationOfUiComponentInputToCreate() {
		boolean validationpassed=true;
		if (StringUtils.trimToEmpty(getDialog().getIncidentTitle().getControl().getText()).length()<1) {
			displayErrorMessage("Incident Title is empty","Incident Title is empty");
			return false;
		}else if (isIncidentTitleExist(getDialog().getIncidentTitle().getControl().getText(),getDialog().getIncidentDate().getText())) {
			displayErrorMessage("Incident Title exists for selected Incident Date","Incident Title exists for selected Incident Date");
			return false;
		}else if (StringUtils.trimToEmpty(getDialog().getIncidentCause().getText()).length()<1) {
			displayErrorMessage("Incident Cause is empty","Incident Cause is empty");
			return false;
		}
		return validationpassed;
	}

	/**
	 * This method is used to handle validation of UiComponentInput
	 * 
	 */
	private boolean isIncidentTitleExist(String incidentTitle,String incidentDate) {
		boolean searchResult=false;
		QiIncident qiIncident=getModel().findByIncidentTitleAndDate(incidentTitle,incidentDate.split(" ")[0]);
		if(qiIncident!=null){
			searchResult=true;
		}
		return searchResult;
	}

	/**
	 * This method is used to create new Incident
	 * 
	 */
	private void createIncident() {
		QiIncident newQiIncident=new QiIncident();
		newQiIncident.setIncidentDate(getDialog().getCurrentTime());
		newQiIncident.setCreateUser(getModel().getUserId());
		newQiIncident.setIncidentTitle(StringUtils.trimToEmpty(getDialog().getIncidentTitle().getControl().getText()));
		newQiIncident.setIncidentCause(StringUtils.trimToEmpty(getDialog().getIncidentCause().getText()));
		newQiIncident.setDocumentControlType(StringUtils.trimToEmpty(getDialog().getDocumentControlType().getText()));
		newQiIncident.setDocumentControlId(StringUtils.trimToEmpty(getDialog().getDocumentControlId().getText()));
		if (getDialog().getCriticalRadioButton().isSelected()){
			newQiIncident.setIncidentType(QiConstant.CRITICAL);
		}else if (getDialog().getComplexRadioButton().isSelected()){
			newQiIncident.setIncidentType(QiConstant.COMPLEX);
		}
		getModel().createQiIncident(newQiIncident);
	}
	
	private void updateIncident() {
		qiIncident.setUpdateUser(getModel().getUserId());
		qiIncident.setIncidentTitle(StringUtils.trimToEmpty(getDialog().getIncidentTitle().getControl().getText()));
		qiIncident.setIncidentCause(StringUtils.trimToEmpty(getDialog().getIncidentCause().getText()));
		qiIncident.setDocumentControlType(StringUtils.trimToEmpty(getDialog().getDocumentControlType().getText()));
		qiIncident.setDocumentControlId(StringUtils.trimToEmpty(getDialog().getDocumentControlId().getText()));
		if (getDialog().getCriticalRadioButton().isSelected()){
			qiIncident.setIncidentType(QiConstant.CRITICAL);
		}else if (getDialog().getComplexRadioButton().isSelected()){
			qiIncident.setIncidentType(QiConstant.COMPLEX);
		}
		getModel().updateQiIncident(qiIncident);
	}

	/**
	 * This method is used to handle cancel button action event
	 * 
	 */
	private void cancelButtonAction(ActionEvent actionEvent) {
		try {
			Stage stage = (Stage) getDialog().getCancelButton().getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occured during cancel action", StringUtils.EMPTY, e);
		}
		finally{
			actionEvent.consume();
		}
	}
	
	
	/**
	 * This method is used to apply UIScreen Component Listener
	 * 
	 */
	private void addUIScreenComponentListener() {
		addTextFieldChangeListener();
		getDialog().getIncidentCause().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(256));
		getDialog().getIncidentTitle().getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(64));
		getDialog().getDocumentControlId().getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(32));
		getDialog().getDocumentControlType().getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(32));
	}
	
	/**
	 * This method is used to apply TextField Change Listener
	 * 
	 */
	private void addTextFieldChangeListener() {
		final ChangeListener<String> changeListener= new ChangeListener<String>() {
			public void changed(final ObservableValue<? extends String> value, final String oldValue, final String newValue) {
				getDialog().clearStatusMessage();
			}
		};
		getDialog().getIncidentTitle().getControl().textProperty().addListener(changeListener);
		getDialog().getIncidentTitle().getControl().textProperty().addListener(changeListener);
		getDialog().getDocumentControlId().getControl().textProperty().addListener(changeListener);
		getDialog().getDocumentControlType().getControl().textProperty().addListener(changeListener);
		getDialog().getIncidentCause().textProperty().addListener(changeListener);
	}
	
	/**
	 * This method is used to handle validation of UiComponentInput
	 * 
	 */
	private boolean validationOfUiComponentInputToUpdate() {
		boolean validationpassed=true;
		if (StringUtils.trimToEmpty(getDialog().getIncidentTitle().getControl().getText()).length()<1) {
			displayErrorMessage("Incident Title is empty","Incident Title is empty");
			return false;
		}else if (!getDialog().getIncidentTitle().getControl().getText().equalsIgnoreCase(qiIncident.getIncidentTitle())
				&& isIncidentTitleExist(getDialog().getIncidentTitle().getControl().getText(),getDialog().getIncidentDate().getText())) {
			displayErrorMessage("Incident Title exists for selected Incident Date","Incident Title exists for selected Incident Date");
			return false;
		}else if (StringUtils.trimToEmpty(getDialog().getIncidentCause().getText()).length()<1) {
			displayErrorMessage("Incident Cause is empty","Incident Cause is empty");
			return false;
		}
		//validation for reason for change
		else if( QiCommonUtil.isMandatoryFieldEmpty(getDialog().getReasonForChangeTextArea())){
			displayErrorMessage("Please enter Reason for Change ","Please enter Reason for Change ");
			return false ;
		}
		return validationpassed;
	}

}
