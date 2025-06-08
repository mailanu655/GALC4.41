package com.honda.galc.client.teamleader.qi.defectResult;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.controller.QiDialogController;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.IqsScoreUtils;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dto.qi.QiAppliedRepairMethodDto;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.IqsScore;
import com.honda.galc.entity.enumtype.QiReportable;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiLocalTheme;
import com.honda.galc.entity.qi.QiRepairResult;
import com.honda.galc.entity.qi.QiResponsibleLevel;
import com.honda.galc.util.KeyValue;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>LocalAttributeMaintController</code> is the controller class for LocalAttributeMaintDialog Dialog.
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

public class LocalAttributeMaintController extends QiDialogController<DefectResultMaintModel, LocalAttributeMaintDialog>{

	private List<QiDefectResult> selectedDefectResultList;
	private List<QiRepairResult> selectedRepairResultList;
	private String typeOfChange;
	private String applicationId;
	
	public LocalAttributeMaintController(DefectResultMaintModel model, String applicationId, LocalAttributeMaintDialog dialog,List<QiDefectResult> selectedDefectResultList,String typeOfChange,List<QiRepairResult> selectedRepairResultList) {
		super();
		setModel(model);
		setDialog(dialog);
		this.applicationId=applicationId;
		this.selectedDefectResultList=selectedDefectResultList;
		this.selectedRepairResultList = selectedRepairResultList;
		this.typeOfChange = typeOfChange;
	}

	public void loadInitialValues() {
		loadLocalThemeComboBox(); 
		loadOriginalDefectStatusComboBox();
		loadIqsComboBox();
		enableDisableOriginalDefectStatus();
	}
	
	private void enableDisableOriginalDefectStatus() {
		boolean enableNoProblemFoundCheckBox = true;
		if (typeOfChange.equals(QiConstant.REPAIR_TYPE)) {
			 getDialog().getInitialStatusComboBox().setDisable(true);
			 getDialog().getActualProblemsAssigned().setText("Actual Problems are assigned to selected Defect(s).");
			 getDialog().getActualProblemsAssigned().setStyle("-fx-text-background-color: red;-fx-font-size: 12px;");
			 
			 for (QiRepairResult repairResult : selectedRepairResultList) {
				 List<QiAppliedRepairMethodDto> appliedRepairMethodList = getModel().findAppliedMethodByRepairId(repairResult.getRepairId());
				 if (appliedRepairMethodList == null || appliedRepairMethodList.size() == 0) {
					 //if repair result/actual problem doesn't have repair method, disable No Problem Found
					 enableNoProblemFoundCheckBox = false;
					 break;
				 }
				 for (QiAppliedRepairMethodDto appliedRepairMethod : appliedRepairMethodList) {
					 if (appliedRepairMethod.getIsCompletelyFixed() == 1) {
						 //if repair method completely fix repair result/actual problem, disable No Problem Found
						 enableNoProblemFoundCheckBox = false;
						 break;
					 }
				 }
				 if (!enableNoProblemFoundCheckBox) {
					 break;
				 }
			 }
		 } else if (typeOfChange.equals(QiConstant.DEFECT_TYPE)) {
			 getDialog().getInitialStatusComboBox().setDisable(false);
			 getDialog().getActualProblemsAssigned().setText("");
			 
			 for (QiDefectResult defectResult : selectedDefectResultList) {
				 //if defect status is not not-fixed, disable No Problem Found
				 if (defectResult.getCurrentDefectStatus() != DefectStatus.NOT_FIXED.getId()) {
					 enableNoProblemFoundCheckBox = false;
				 }
				 if (getModel().findAllByDefectResultId(defectResult.getDefectResultId()).size() > 0) {
					 //if defect result has repair result/actual problem, disable No Problem Found
					 getDialog().getActualProblemsAssigned().setStyle("-fx-text-background-color: red;-fx-font-size: 12px;");
					 getDialog().getActualProblemsAssigned().setText("Actual Problems are assigned to selected Defect(s).");
					 getDialog().getInitialStatusComboBox().setDisable(true);
					 enableNoProblemFoundCheckBox = false;
					 break;
				 } 
			 }
		 }
		 getDialog().getNoProblemFoundCheckBox().setDisable(!enableNoProblemFoundCheckBox);
	}

	private void loadLocalThemeComboBox() {
		getDialog().getLocalThemeComboBox().getItems().clear();
		List<QiLocalTheme> localThemeList = getModel().findAllActiveLocalTheme();
		for (QiLocalTheme localTheme : localThemeList) {
			getDialog().getLocalThemeComboBox().getItems().add(localTheme.getLocalTheme());
		}
	}
	
	private void loadOriginalDefectStatusComboBox() {
		getDialog().getInitialStatusComboBox().getItems().clear();
		getDialog().getInitialStatusComboBox().getItems().addAll(DefectStatus.REPAIRED.getName(),DefectStatus.NOT_REPAIRED.getName());
	}

	private void loadIqsComboBox() {
		getDialog().getIqsComboBox().getControl().getItems().clear();
		List<Double> scores = new ArrayList<Double>();
		for(IqsScore i : IqsScoreUtils.getActiveScores()) {
			scores.add(i.getScore());
		}
		getDialog().getIqsComboBox().getControl().getItems().addAll(scores);
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource().equals(getDialog().getChangeBtn())) {
			changeBtnAction(actionEvent);
		}else if(actionEvent.getSource().equals(getDialog().getCancelBtn())) {
			cancelBtnAction(actionEvent);
		}else if(actionEvent.getSource().equals(getDialog().getCancelDefectBtn())) {
			cancelBtnAction(actionEvent);
		}else if(actionEvent.getSource().equals(getDialog().getUpdateDefectBtn())){
			updateDefectBtnAction(actionEvent);
		}
	}
	
	private void updateDefectBtnAction(ActionEvent actionEvent) {
		LoggedButton changeBtn = getDialog().getChangeBtn();
		String gdpDefects = StringUtils.EMPTY;;
		String trpuDefects = StringUtils.EMPTY;;
		if(getDialog().getGdPRadioButton().isSelected()){
			gdpDefects = "Yes";
		}else if(getDialog().getNonGdPRadioButton().isSelected()){
			gdpDefects = "No";
		}
		
		if(getDialog().getTrpuRadioButton().isSelected()){
			trpuDefects = "Yes";
		}else if(getDialog().getNonTrpuRadioButton().isSelected()){
			trpuDefects = "No";
		}

		if(gdpDefects.equals(StringUtils.EMPTY) || trpuDefects.equals(StringUtils.EMPTY)){
				displayErrorMessage("Error","Mandatory Defects field is empty");
				return;
		}
		List<QiRepairResult> repairResultList = new ArrayList<>();
		List<QiDefectResult> repairDefectList = new ArrayList<>();
		getDialog().getMessage().setText(StringUtils.EMPTY);
		if(typeOfChange.equals(QiConstant.DEFECT_TYPE)){
			repairDefectList = selectedDefectResultList;
		}else{
			repairResultList = selectedRepairResultList;
		}
		UpdateDefectDataReviewDialog ldapUserDefectDataReviewDialog = new UpdateDefectDataReviewDialog("Update GDP/TRPU Defects", applicationId, getModel(),repairResultList,typeOfChange,repairDefectList,gdpDefects,trpuDefects,getDialog());
			ldapUserDefectDataReviewDialog.show();		
			try {
				Stage stage = (Stage) changeBtn.getScene().getWindow(); 
				ldapUserDefectDataReviewDialog.toFront();
			} catch (Exception e) {
				handleException("An error occured in during cancel action Failed to perform cancel action", StringUtils.EMPTY, e);
			}
	}
	private void changeBtnAction(ActionEvent actionEvent) {
		if(!isValidateSite()){
			String responsibilityErrorMessage = isValidateResponsibility();
			if(!responsibilityErrorMessage.equals(QiConstant.EMPTY)) {
				displayErrorMessage("Mandatory field is empty", responsibilityErrorMessage);
				return;
			}
		}
		LoggedButton changeBtn = getDialog().getChangeBtn();
		String siteValue = StringUtils.trimToEmpty(getDialog().getResSiteComboBox().getValue());
		String plantValue = StringUtils.trimToEmpty(getDialog().getPlantComboBox().getValue());
		String deptValue = StringUtils.trimToEmpty(getDialog().getDepartmentComboBox().getValue());
		String responsibilitLevel_1_Value = getDialog().getResponsibleLevel1ComboBox().getValue()==null?"":StringUtils.trimToEmpty(getDialog().getResponsibleLevel1ComboBox().getValue().getValue());
		String responsibilitLevel_2_Value =getDialog().getResponsibleLevel2ComboBox().getValue()==null?"": StringUtils.trimToEmpty(getDialog().getResponsibleLevel2ComboBox().getValue().getValue());
		String responsibilitLevel_3_Value =getDialog().getResponsibleLevel3ComboBox().getValue()==null?"": StringUtils.trimToEmpty(getDialog().getResponsibleLevel3ComboBox().getValue().getValue());
		String localThemeValue = StringUtils.trimToEmpty(getDialog().getLocalThemeComboBox().getValue());
		String originalDefectStatus = StringUtils.trimToEmpty(getDialog().getInitialStatusComboBox().getValue());
		String currentDefectStatus = StringUtils.EMPTY;
		boolean noProblemFound = getDialog().getNoProblemFoundCheckBox().isSelected();
		Double selectedIqsScore = getDialog().getIqsComboBox().getControl().getSelectionModel().getSelectedItem();

		String reportableValue=StringUtils.EMPTY;
		String gdpValue = StringUtils.EMPTY;
		String trpuValue = StringUtils.EMPTY;
		if(getDialog().getGdPRadioButton().isSelected()){
			gdpValue = "Yes";
		}else if(getDialog().getNonGdPRadioButton().isSelected()){
			gdpValue = "No";
		}
		if(getDialog().getTrpuRadioButton().isSelected()){
			trpuValue = "Yes";
		}else if(getDialog().getNonTrpuRadioButton().isSelected()){
			trpuValue = "No";
		}
		
		
		/* this list is used to set the data from popup screen*/
		List<QiDefectResult> updatedDefectRepairResultList = setDefectRepairResult(siteValue,plantValue, deptValue, responsibilitLevel_1_Value,responsibilitLevel_2_Value, responsibilitLevel_3_Value, reportableValue, gdpValue, trpuValue, localThemeValue, originalDefectStatus,currentDefectStatus,typeOfChange, selectedIqsScore);
		
		if(siteValue.isEmpty() && plantValue.isEmpty() && deptValue.isEmpty() && responsibilitLevel_1_Value.isEmpty() && responsibilitLevel_2_Value.isEmpty() 
				&& responsibilitLevel_3_Value.isEmpty() && reportableValue.isEmpty() && localThemeValue.isEmpty()
				&& originalDefectStatus.isEmpty() && !noProblemFound && selectedIqsScore == null){
				getDialog().getMessage().setStyle("-fx-text-background-color: red;-fx-font-size: 12px;");
				getDialog().getMessage().setText("No changes done");
				return;
		}
		
		if (noProblemFound) {
			for (QiDefectResult defectResult : updatedDefectRepairResultList) {
				defectResult.setCurrentDefectStatus((short) DefectStatus.FIXED.getId());
				defectResult.setReportable((short)QiReportable.NON_REPORTABLE_BY_NO_PROBLEM_FOUND.getId());
			}
		}
		
		getDialog().getMessage().setText(StringUtils.EMPTY);
		DefectDataReviewDialog defectDataReviewDialog = new DefectDataReviewDialog("Review/Verification", getModel(),"",selectedDefectResultList,updatedDefectRepairResultList,selectedRepairResultList,typeOfChange, noProblemFound);
		defectDataReviewDialog.show();
		defectDataReviewDialog.scrollCurrentAndUpdateTablePane(typeOfChange);
		try {
			Stage stage = (Stage) changeBtn.getScene().getWindow(); 
			stage.close();
			defectDataReviewDialog.toFront();
		} catch (Exception e) {
			handleException("An error occured in during cancel action Failed to perform cancel action", StringUtils.EMPTY, e);
		}
	}

	/**
	 * This method is used to set the Defect and Repair result
	 * @param plantValue
	 * @param deptValue
	 * @param responsibilitLevel_1_Value
	 * @param responsibilitLevel_2_Value
	 * @param responsibilitLevel_3_Value
	 * @param reportableValue
	 * @param localThemeValue
	 * @param originalDefectStatus
	 * @param currentDefectStatus
	 * @return
	 */
	private List<QiDefectResult> setDefectRepairResult(String siteValue ,String plantValue,String deptValue, String responsibilitLevel_1_Value,String responsibilitLevel_2_Value,String responsibilitLevel_3_Value, String reportableValue,String gdpValue, String trpuValue, String localThemeValue, String originalDefectStatus,String currentDefectStatus,String typeOfChange, Double iqsScore) {
		QiDefectResult updatedDefectRepairResult;
		List<QiDefectResult> defectResultsList = new ArrayList<QiDefectResult>();
		if(typeOfChange.equals(QiConstant.DEFECT_TYPE)){
			for (QiDefectResult defectResult : selectedDefectResultList) {
				updatedDefectRepairResult = new QiDefectResult();
				try {
					updatedDefectRepairResult = defectResult.clone();
				} catch (CloneNotSupportedException e) {
					handleException("An error occured during setting updated defect repair result", StringUtils.EMPTY, e);
				}
				
				if(!siteValue.isEmpty()){
					updatedDefectRepairResult.setResponsibleSite(siteValue);  
					updatedDefectRepairResult.setResponsiblePlant(plantValue);  
					updatedDefectRepairResult.setResponsibleDept(deptValue);
					updatedDefectRepairResult.setResponsibleLevel1(responsibilitLevel_1_Value);
					updatedDefectRepairResult.setResponsibleLevel2(responsibilitLevel_2_Value);
					updatedDefectRepairResult.setResponsibleLevel3(responsibilitLevel_3_Value);
					updatedDefectRepairResult.setProcessName("");
					updatedDefectRepairResult.setProcessNo("");
					updatedDefectRepairResult.setUnitDesc("");
					updatedDefectRepairResult.setUnitNo("");
					updatedDefectRepairResult.setResponsibleAssociate("");
				}
				
				if(!reportableValue.isEmpty()){
					updatedDefectRepairResult.setReportable((short)QiReportable.getId(reportableValue));
				}
				
				if(!localThemeValue.isEmpty()){
					updatedDefectRepairResult.setLocalTheme(localThemeValue);
				}
				
				if(!originalDefectStatus.isEmpty()){
					String defectStatus = StringUtils.EMPTY;
					if(originalDefectStatus.equals(DefectStatus.REPAIRED.getName()) && 
							defectResult.getOriginalDefectStatus()!=DefectStatus.REPAIRED.getId()){
						updatedDefectRepairResult.setGdpDefect((short)0);
						defectStatus=DefectStatus.REPAIRED.getName();
					}
					else if(originalDefectStatus.equals(DefectStatus.NOT_REPAIRED.getName())){
						updatedDefectRepairResult.setGdpDefect((short)1);
						defectStatus=DefectStatus.NOT_REPAIRED.getName();
					}
					updatedDefectRepairResult.setDefectStatus(defectStatus);
				}

				if(iqsScore != null) {
					updatedDefectRepairResult.setIqsScore(iqsScore);
				}
				
				defectResultsList.add(updatedDefectRepairResult);
			}
		}else if(typeOfChange.equals(QiConstant.REPAIR_TYPE)){
			for (QiRepairResult repairResult : selectedRepairResultList) {
				updatedDefectRepairResult = new QiDefectResult(repairResult);
				
				if(!siteValue.isEmpty()){
					updatedDefectRepairResult.setResponsibleSite(siteValue);  
					updatedDefectRepairResult.setResponsiblePlant(plantValue);  
					updatedDefectRepairResult.setResponsibleDept(deptValue);
					updatedDefectRepairResult.setResponsibleLevel1(responsibilitLevel_1_Value);
					updatedDefectRepairResult.setResponsibleLevel2(responsibilitLevel_2_Value);
					updatedDefectRepairResult.setResponsibleLevel3(responsibilitLevel_3_Value);
					updatedDefectRepairResult.setProcessName("");
					updatedDefectRepairResult.setProcessNo("");
					updatedDefectRepairResult.setUnitDesc("");
					updatedDefectRepairResult.setUnitNo("");
					updatedDefectRepairResult.setResponsibleAssociate("");
				}
				
				if(!reportableValue.isEmpty()){
					updatedDefectRepairResult.setReportable((short)QiReportable.getId(reportableValue));
				}
				
				if(!localThemeValue.isEmpty()){
					updatedDefectRepairResult.setLocalTheme(localThemeValue);
				}
				
				if(!originalDefectStatus.isEmpty()){
					String defectStatus = StringUtils.EMPTY;
					if(originalDefectStatus.equals(DefectStatus.REPAIRED.getName()) && 
							repairResult.getOriginalDefectStatus()!=DefectStatus.REPAIRED.getId()){
						updatedDefectRepairResult.setGdpDefect((short)0);
						defectStatus=DefectStatus.REPAIRED.getName();
					}
					else if(originalDefectStatus.equals(DefectStatus.NOT_REPAIRED.getName())){
						updatedDefectRepairResult.setGdpDefect((short)1);
						defectStatus=DefectStatus.NOT_REPAIRED.getName();
					}
					updatedDefectRepairResult.setDefectStatus(defectStatus);
				}
				
				defectResultsList.add(updatedDefectRepairResult);
			}
		}
		return defectResultsList;
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

	@Override
	public void initListeners() {
		loadInitialValues();
		addRespSiteComboBoxListener();
		addPlantComboBoxListener();
		addDepartmentComboBoxListener(); 
		addResponsibleLevel1ComboBoxListener();
		addResponsibleLevel2ComboBoxListener();
	}
	
	
	ChangeListener<String> resSiteComboBoxChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> ov,  String old_val, String new_val) {
			clearDisplayMessage();
			loadRespPlantComboBox(new_val);
		} 
	};
	
	/**
	 * Load ResPlant based on site combo box.
	 *
	 * @param productType
	 */
	public void loadRespPlantComboBox(String siteName) {
		getDialog().getPlantComboBox().getItems().clear();
		getDialog().getPlantComboBox().getItems().addAll(getModel().findAllActivePlantsbySite(StringUtils.trimToEmpty(siteName)));
	}
	
	/**
	 * This method is event listener for resSiteComboBoxChangeListener
	 */
	private void addRespSiteComboBoxListener(){
		clearDisplayMessage();
		getDialog().getResSiteComboBox().valueProperty().addListener(resSiteComboBoxChangeListener);
	}
	
	/**
	 * Adds the plant combo box listener.
	 */
	private void addPlantComboBoxListener() {
		getDialog().getPlantComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			 public void changed(ObservableValue<? extends String> ov,  String old_val, String new_val) { 
				 clearDisplayMessage();
				 loadDepartmentComboBox(new_val);
			 } 
		});
	}
	
	/**
	 * Adds the department combo box listener.
	 */
	private void addDepartmentComboBoxListener() {
		getDialog().getDepartmentComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			 public void changed(ObservableValue<? extends String> ov,  String old_val, String new_val) { 
				 clearDisplayMessage();
				 loadResponsibleLevel1ComboBox(new_val);
			 } 
		});
	}
	/**
	 * Adds the responsible level1 combo box listener.
	 */
	private void addResponsibleLevel1ComboBoxListener() {
		getDialog().getResponsibleLevel1ComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<KeyValue<Integer, String>>() {
			 public void changed(ObservableValue<? extends KeyValue<Integer, String>> ov,  KeyValue<Integer, String> old_val, KeyValue<Integer, String> new_val) { 
				 clearDisplayMessage();
				 loadResponsibleLevel2ComboBox(new_val);
			 } 
		});
	}
	
	/**
	 * Adds the responsible level2 combo box listener.
	 */
	private void addResponsibleLevel2ComboBoxListener() {
		getDialog().getResponsibleLevel2ComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<KeyValue<Integer, String>>() {
			 public void changed(ObservableValue<? extends KeyValue<Integer, String>> ov,  KeyValue<Integer, String> old_val, KeyValue<Integer, String> new_val) { 
				 clearDisplayMessage();
				 loadResponsibleLevel3ComboBox(new_val);
			 } 
		});
	}
	
	/**
	 * Load department combo box.
	 *
	 * @param plant
	 */
	public void loadDepartmentComboBox(String plant) {
		getDialog().getDepartmentComboBox().getItems().clear();
		getDialog().getDepartmentComboBox().getItems().addAll(getModel().findAllBySiteAndPlant(getDialog().getResSiteComboBox().getValue(), plant));

	}
	
	/**
	 * Load responsible level1 combo box.
	 * @param department
	 */
	public void loadResponsibleLevel1ComboBox(String department) {
		getDialog().getResponsibleLevel3ComboBox().getItems().clear();
		getDialog().getResponsibleLevel2ComboBox().getItems().clear();
		getDialog().getResponsibleLevel1ComboBox().getItems().clear();
		String plant = getDialog().getPlantComboBox().getValue() != null ? getDialog().getPlantComboBox().getValue().toString() : "";
		
		List<QiResponsibleLevel> responsibleLevel1List = getModel().findAllBySitePlantDepartmentLevel(StringUtils.trimToEmpty(getDialog().getResSiteComboBox().getValue()), plant, department,(short)1);
		for (QiResponsibleLevel responsibleLevel1 : responsibleLevel1List) {
			getDialog().getResponsibleLevel1ComboBox().getItems().add(getKeyValue(responsibleLevel1.getResponsibleLevelId(), responsibleLevel1.getResponsibleLevelName()));
		}
	}
	
	/**
	 * Gets the key value.
	 *
	 * @param key the key
	 * @param value the value
	 * @return the key value
	 */
	public KeyValue<Integer, String> getKeyValue(Integer key, final String value){
		KeyValue<Integer, String> keyValue = new KeyValue<Integer, String>(key, value){
			private static final long serialVersionUID = 1L;
			@Override
			public String toString() {
				return value;
			}
		};
		return keyValue;
	}
	
	/**
	 * Load responsible level3 combo box.
	 *
	 * @param department the department
	 */
	public void loadResponsibleLevel3ComboBox(KeyValue<Integer, String> new_val) {
		getDialog().getResponsibleLevel3ComboBox().getItems().clear();
		QiResponsibleLevel responsibleLevel2 = getModel().findByResponsibleLevelId(new_val.getKey());
		 if(responsibleLevel2!=null && responsibleLevel2.getUpperResponsibleLevelId()!=0){
			 QiResponsibleLevel responsibleLevel3 = getModel().findByResponsibleLevelId(responsibleLevel2.getUpperResponsibleLevelId());
				 if(responsibleLevel3!=null) {
						getDialog().getResponsibleLevel3ComboBox().getItems().add(getKeyValue(responsibleLevel3.getResponsibleLevelId(), responsibleLevel3.getResponsibleLevelName()));
				}
			}
		
	}
	
	/**
	 * Load responsible level2 and level2 combo box based on plant site and dept.
	 *
	 * @param new_val the new_val
	 */
	public void loadResponsibleLevel2ComboBox(KeyValue<Integer, String> new_val) {
		getDialog().getResponsibleLevel2ComboBox().getItems().clear();
		getDialog().getResponsibleLevel3ComboBox().getItems().clear();
		QiResponsibleLevel responsibleLevel1=getModel().findByResponsibleLevelId(new_val.getKey());
		if(responsibleLevel1!=null && responsibleLevel1.getUpperResponsibleLevelId()!=0){
			QiResponsibleLevel responsibleLevel2=getModel().findByResponsibleLevelId(responsibleLevel1.getUpperResponsibleLevelId());
			if(responsibleLevel2 !=null) {
				getDialog().getResponsibleLevel2ComboBox().getSelectionModel().select(getKeyValue(responsibleLevel2.getResponsibleLevelId(), responsibleLevel2.getResponsibleLevelName()));
				getDialog().getResponsibleLevel2ComboBox().getItems().add(getKeyValue(responsibleLevel2.getResponsibleLevelId(), responsibleLevel2.getResponsibleLevelName()));
			}
			QiResponsibleLevel responsibleLevel3=getModel().findByResponsibleLevelId(responsibleLevel2.getUpperResponsibleLevelId());
			if(responsibleLevel3!= null){
				getDialog().getResponsibleLevel3ComboBox().getSelectionModel().select(getKeyValue(responsibleLevel3.getResponsibleLevelId(), responsibleLevel3.getResponsibleLevelName()));
			}
		}
	}
	
	
	/**
	 * Checks if is validate responsibility.
	 *
	 * @return the string
	 */
	private String isValidateResponsibility() {
		String errorMsg = "";
		 if(isValidatePlant()){
			errorMsg = "Please Select Plant.";
		} else if(isValidateDepartment()){
			errorMsg = "Please Select Department.";
		} else if(isValidateResponsibleLevel1()){
			errorMsg = "Please Select Responsible Level1.";
		}
		return errorMsg;
	}
	
	/**
	 * Checks if is validate site.
	 *
	 * @return true, if is validate site
	 */
	private boolean isValidateSite() {
		return getDialog().getResSiteComboBox().getValue() != null && 
				!getDialog().getResSiteComboBox().getValue().toString().equals(QiConstant.EMPTY) ? false : true;
	}
	
	/**
	 * Checks if is validate plant.
	 *
	 * @return true, if is validate plant
	 */
	private boolean isValidatePlant() {
		return getDialog().getPlantComboBox().getValue() != null && 
				!getDialog().getPlantComboBox().getValue().toString().equals(QiConstant.EMPTY) ? false : true;
	}
	
	/**
	 * Checks if is validate department.
	 *
	 * @return true, if is validate department
	 */
	private boolean isValidateDepartment() {
		return getDialog().getDepartmentComboBox().getValue() != null && 
				!getDialog().getDepartmentComboBox().getValue().toString().equals(QiConstant.EMPTY) ? false : true;
	}
	
	/**
	 * Checks if is validate responsible level1.
	 *
	 * @return true, if is validate responsible level1
	 */
	private boolean isValidateResponsibleLevel1() {
		return getDialog().getResponsibleLevel1ComboBox().getValue() != null && 
				!getDialog().getResponsibleLevel1ComboBox().getValue().toString().equals(QiConstant.EMPTY) ? false : true;
	}
}
