package com.honda.galc.client.teamleader.qi.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.model.HeadlessDefectEntryModel;
import com.honda.galc.client.teamleader.qi.view.HeadlessDefectEntryDialog;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.dto.DefectMapDto;
import com.honda.galc.entity.qi.QiExternalSystemDefectMap;
import com.honda.galc.entity.qi.QiLocalDefectCombination;
import com.honda.galc.util.AuditLoggerUtil;

/**
 * 
 * <h3>HeadlessDefectEntryDialogController Class description</h3>
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
public class HeadlessDefectEntryDialogController  extends QiDialogController<HeadlessDefectEntryModel, HeadlessDefectEntryDialog> {
	
	private DefectMapDto panelSelectedData;
	private HeadlessDefectEntryDialog view;

	
	public HeadlessDefectEntryDialogController(HeadlessDefectEntryModel  headlessDefectEntryModel, HeadlessDefectEntryDialog headlessDefectEntryDialog, DefectMapDto defectMapDto) {
		super();
		setModel(headlessDefectEntryModel);
		setDialog(headlessDefectEntryDialog);
		this.panelSelectedData = defectMapDto;
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource().equals(getDialog().getCreateButton())) createButtonAction(actionEvent);
		else if(actionEvent.getSource().equals(getDialog().getUpdateButton())) updateButtonAction(actionEvent);
		else if(actionEvent.getSource().equals(getDialog().getCancelButton())) cancelButtonAction(actionEvent);
	}

	@Override
	public void initListeners() {
		clearDisplayMessage();
		addListener();
	}
	
	
	/**
	 * This method adds the listener.
	 */
	public void addListener() {
		addExternalPartCodeListerner();
		addFieldListener(getDialog().getExternalPartCode(),true,false);
		addTextEntryMenuListener();
		addExternalDefectCodeListerner();
		addQicsCheckBoxListener();
		addExtRepairCheckBoxListener();
		addFieldListener(getDialog().getExternalDefectCode(),true,false);
		getDialog().getExternalPartCode().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(64));
		getDialog().getExternalDefectCode().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(64));
	
		addUpdateEnablerForTableChangeListener(getDialog().getPartDefectCombTablePane());
	}
	
	/**
	 * This method adds External part code event handler.
	 */
	private void addExternalPartCodeListerner(){
		getDialog().getExternalPartCode().addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>()
				{
			public void handle(KeyEvent event) {
				if ( !StringUtils.isEmpty(getDialog().getExternalDefectCode().getText()) && !StringUtils.isEmpty(getDialog().getExternalPartCode().getText())) {
					getDialog().getExternalSystemCode().setText(getDialog().getExternalPartCode().getText()+"-"+getDialog().getExternalDefectCode().getText());
				}else{
					getDialog().getExternalSystemCode().setText(StringUtils.EMPTY);
				}
				event.consume();
			}
		});
		
	}

	/**
	 * This method adds External defect code event handler.
	 */
	private void addExternalDefectCodeListerner(){
		getDialog().getExternalDefectCode().addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>()
		{
			public void handle(KeyEvent event) {
				if ((event.getEventType() == KeyEvent.KEY_RELEASED ) && !StringUtils.isEmpty(getDialog().getExternalDefectCode().getText()) && !StringUtils.isEmpty(getDialog().getExternalPartCode().getText())) {
					getDialog().getExternalSystemCode().setText(getDialog().getExternalPartCode().getText()+"-"+getDialog().getExternalDefectCode().getText());
				}else{
					getDialog().getExternalSystemCode().setText(StringUtils.EMPTY);
				}
				event.consume();
			}
		});
	}
	
	/**
	 * This method adds the Text entry menu listener.
	 */
	private void addTextEntryMenuListener() {
		getDialog().getTextentryTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<DefectMapDto>() {
			public void changed(
					ObservableValue<? extends DefectMapDto> arg0, DefectMapDto oldValue, DefectMapDto newValue) {
				loadPartDefectCombData(newValue);
			}
		});
	}
	
	
	/**
	 * This method adds the Part Defect Combination related check box listener.
	 */
	private void loadPartDefectCombData(DefectMapDto qiExternalSystemDefectMapDto) {
		getDialog().getPartDefectCombTablePane().setData(getModel().findAllPartDefectCombByEntryScreenModelMenu(getDialog().getEntryScreen().getText(), getDialog().getEntryModel().getText(),getDialog().getTextentryTablePane().getTable().getSelectionModel().getSelectedItem().getTextEntryMenu()));
	}

	
	/**
	 * This method defines action for create button .
	 * @param actionEvent the action event
	 */
	private void createButtonAction(ActionEvent actionEvent) {
		try {
			String errorMesg = validateMandatoryFields(false);
			if(!errorMesg.equalsIgnoreCase(StringUtils.EMPTY)){
				displayErrorMessage("Mandatory field is empty", errorMesg);
				return;
			}
			getModel().addExternalSystemData(setDataToAdd());
			Stage stage = (Stage) getDialog().getCreateButton().getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occured in create button Action Method" , "Failed to Create", e);
		}
		
	}
	
	/**
	 *This method defines action for update button .
	 * @param actionEvent 
	 */
	private void updateButtonAction(ActionEvent actionEvent) {
		try {
			String errorMesg = validateMandatoryFields(true);
			if(!StringUtils.isEmpty(errorMesg)){
				displayErrorMessage("Mandatory field is empty", errorMesg);
				return;
			}
			QiExternalSystemDefectMap qiExternalSystemDefectMapOld = getModel().findByPartAndDefectCodeExternalSystemAndEntryModel(panelSelectedData.getExternalPartCode(), panelSelectedData.getExternalDefectCode(),panelSelectedData.getExternalSystemName(), panelSelectedData.getEntryModel());
			QiExternalSystemDefectMap qiExternalSystemDefectMap = setDataToUpdate();
			if(qiExternalSystemDefectMap!= null) {
				if(getPanelSelectedData().getUpdateTimestamp()!=null)
				   qiExternalSystemDefectMap.setUpdateTimestamp(new Timestamp(QiCommonUtil.convert(getPanelSelectedData().getUpdateTimestamp()).getTime()));
				if (isUpdated(qiExternalSystemDefectMap)) {
					return;
				}
				getModel().updateExternalSystemData(qiExternalSystemDefectMap);
			}
			QiLocalDefectCombination qiLocalDefectCombinationOld = null;
			if(qiExternalSystemDefectMapOld.getLocalDefectCombinationId() > 0)  {
				qiLocalDefectCombinationOld = getModel().findbyLocalCombinationId(qiExternalSystemDefectMapOld.getLocalDefectCombinationId());
			}
			int rdcId = 0;
			if(qiLocalDefectCombinationOld != null)  {
				rdcId = qiLocalDefectCombinationOld.getRegionalDefectCombinationId();				
			}
			//creating primary key for audit log
			String auditPrimaryKeyValue = qiExternalSystemDefectMapOld.getExternalSystemName()+" "+qiExternalSystemDefectMapOld.getExternalPartCode()+" "+
					qiExternalSystemDefectMapOld.getExternalDefectCode()+" "+ (rdcId > 0 ? getModel().getAuditPrimaryKeyValue(rdcId) : "NotFound-" + System.currentTimeMillis());
			AuditLoggerUtil.logAuditInfo(qiExternalSystemDefectMapOld, qiExternalSystemDefectMap, getDialog().getReasonForChangeTextArea().getText(), getDialog().getScreenName(),auditPrimaryKeyValue);
			Stage stage = (Stage) getDialog().getUpdateButton().getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occured in Update button Action Method" , "Failed to Update", e);
		}
		
	}
	
	/**
	 * This method defines action for Cancel button .
	 * @param actionEvent the action event
	 */
	private void cancelButtonAction(ActionEvent actionEvent) {
		try {
			Stage stage = (Stage) getDialog().getCancelButton().getScene().getWindow();
			getDialog().setCancel(true);
			stage.close();
		} catch (Exception e) {
			handleException("An error occured in during cancel action ", "Failed to perform cancel action",e);
		}
	}
	
	/**
	 * This method validate mandatory fields 
	 * @param isupdateBtn 
	 * @return String
	 */
	
	private String validateMandatoryFields(boolean isupdateBtn) {
		String errorMesg=StringUtils.EMPTY;
		//validation for part code
		if( QiCommonUtil.isMandatoryFieldEmpty(getDialog().getExternalPartCode())){
			errorMesg = "Please enter External Part Code";
			return errorMesg;
		}
		//validation for defect  code
		if( QiCommonUtil.isMandatoryFieldEmpty(getDialog().getExternalDefectCode())){
			errorMesg = "Please enter External Defect Code ";
			return errorMesg;
		}
		//validation for text entry Menu
		if(!getDialog().getIsImageEntryScreen()){
			if(getDialog().getTextentryTablePane().getTable().getSelectionModel().getSelectedItems()==null || 
					getDialog().getTextentryTablePane().getTable().getSelectionModel().getSelectedItems().size()<1){
				errorMesg = "Please select Text Entry Menu ";
				return errorMesg;
			}	
		}
		//validation for part defect combination
		if(getDialog().getPartDefectCombTablePane().getTable().getSelectionModel().getSelectedItems()==null ||  
				getDialog().getPartDefectCombTablePane().getTable().getSelectionModel().getSelectedItems().size()<1){
			errorMesg = "Please select QICS Part Defect Combination ";
			return errorMesg;
		}
		//validation for reason for change
		if(isupdateBtn){
			if( QiCommonUtil.isMandatoryFieldEmpty(getDialog().getReasonForChangeTextArea())){
				errorMesg="Please enter Reason for Change ";
				return errorMesg;
			}
		}
		//check for external part code and defect code combination already exist in db 
		QiExternalSystemDefectMap qiExternalSystemDefectMap = getModel().findByPartAndDefectCodeExternalSystemAndEntryModel(getDialog().getExternalPartCode().getText(),getDialog().getExternalDefectCode().getText(),panelSelectedData.getExternalSystemName(), panelSelectedData.getEntryModel());
		if(!isupdateBtn){
			if(qiExternalSystemDefectMap!= null){
				errorMesg = "External Part Code and Defect Code combination already exists";
				return errorMesg;
			}			
		}else{
			if(qiExternalSystemDefectMap!= null && !(panelSelectedData.getExternalPartCode().toString().equalsIgnoreCase(getDialog().getExternalPartCode().getText())
					&& panelSelectedData.getExternalDefectCode().toString().equalsIgnoreCase(getDialog().getExternalDefectCode().getText()))){
				errorMesg = "External Part Code and Defect Code combination already exists";
				return errorMesg;
			}
		}
		return errorMesg;
	}

	
	/**
	 * This method sets the external system defect map data to add.
	 * @return QiExternalSystemDefectMap
	 */
	private QiExternalSystemDefectMap setDataToAdd() {
		QiExternalSystemDefectMap qiExternalSystemDefectMap = new QiExternalSystemDefectMap();
		qiExternalSystemDefectMap.setExternalSystemName(panelSelectedData.getExternalSystemName()); 
		qiExternalSystemDefectMap.setEntryModel(panelSelectedData.getEntryModel()); 
		qiExternalSystemDefectMap.setExternalPartCode(StringUtils.trimToEmpty(getDialog().getExternalPartCode().getText()));
		qiExternalSystemDefectMap.setExternalDefectCode(StringUtils.trimToEmpty(getDialog().getExternalDefectCode().getText()));
		qiExternalSystemDefectMap.setLocalDefectCombinationId(getDialog().getPartDefectCombTablePane().getTable().getSelectionModel().getSelectedItem().getLocalDefectCombinationId());
		qiExternalSystemDefectMap.setCreateUser(getUserId());
		qiExternalSystemDefectMap.setIsQicsRepairReqd((short) (getDialog().getQicsRepairCheckBox().isSelected() ? 1 : 0));
		qiExternalSystemDefectMap.setIsExtSysRepairReqd((short) (getDialog().getExtSysRepairCheckBox().isSelected() ? 1 : 0));
		return qiExternalSystemDefectMap;
	}
	
	/**
	 *This method sets the external system defect data to update.
	 * @return QiExternalSystemDefectMap
	 */
	
	private QiExternalSystemDefectMap setDataToUpdate() {
		QiExternalSystemDefectMap qiExternalSystemDefectMap = getModel().findByPartAndDefectCodeExternalSystemAndEntryModel(panelSelectedData.getExternalPartCode().trim(),panelSelectedData.getExternalDefectCode().trim(),panelSelectedData.getExternalSystemName(), panelSelectedData.getEntryModel());		
		qiExternalSystemDefectMap.setExternalPartCode(StringUtils.trimToEmpty(getDialog().getExternalPartCode().getText()));
		qiExternalSystemDefectMap.setExternalDefectCode(StringUtils.trimToEmpty(getDialog().getExternalDefectCode().getText()));
		qiExternalSystemDefectMap.setLocalDefectCombinationId(getDialog().getPartDefectCombTablePane().getTable().getSelectionModel().getSelectedItem().getLocalDefectCombinationId());
		qiExternalSystemDefectMap.setUpdateUser(getUserId());
		qiExternalSystemDefectMap.setIsQicsRepairReqd((short) (getDialog().getQicsRepairCheckBox().isSelected() ? 1 : 0));
		qiExternalSystemDefectMap.setIsExtSysRepairReqd((short) (getDialog().getExtSysRepairCheckBox().isSelected() ? 1 : 0));
		return qiExternalSystemDefectMap;
	}
	
	/**
	 * @return the panelSelectedData
	 */
	public DefectMapDto getPanelSelectedData() {
		return panelSelectedData;
	}

	/**
	 * @param panelSelectedData the panelSelectedData to set
	 */
	public void setPanelSelectedData(DefectMapDto panelSelectedData) {
		this.panelSelectedData = panelSelectedData;
	}

	public void addQicsCheckBoxListener() {
		getDialog().getQicsRepairCheckBox().selectedProperty().addListener(new ChangeListener<Boolean>()
        {
	        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		    	if(newValue)  {
		    		getDialog().getQicsRepairCheckBox().setSelected(true);
		    	}       
		    	else  {
		    		getDialog().getQicsRepairCheckBox().setSelected(false);
		    	}
		    	if(getDialog().isUpdate())  {
		    		enableUpdateButton();
		    	}
	        }
        });
	
	}

	public void addExtRepairCheckBoxListener() {
		getDialog().getExtSysRepairCheckBox().selectedProperty().addListener(new ChangeListener<Boolean>()
	    {
	        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		    	if(newValue)  {
		    		getDialog().getExtSysRepairCheckBox().setSelected(true);
		    	}       
		    	else  {
		    		getDialog().getExtSysRepairCheckBox().setSelected(false);
		    	}       
		    	if(getDialog().isUpdate())  {
		    		enableUpdateButton();
		    	}
	        }
	    });
	
	}
}

