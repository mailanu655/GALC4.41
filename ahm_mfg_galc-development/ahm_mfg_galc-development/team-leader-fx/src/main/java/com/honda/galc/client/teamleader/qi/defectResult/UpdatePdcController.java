package com.honda.galc.client.teamleader.qi.defectResult;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.controller.QiDialogController;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dto.qi.PdcRegionalAttributeMaintDto;
import com.honda.galc.entity.qi.QiDefectIqsScore;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiPddaResponsibility;
import com.honda.galc.entity.qi.QiRepairResult;
import com.honda.galc.entity.qi.QiResponsibleLevel;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

/**
 * 
 * <h3>UpdatePdcController Class description</h3>
 * <p>
 * UpdatePdcController class is used to update update values in the "Part Defect Combination" for gathered defects in the Defect Results table,Part Defect Combination for the actual problems in the Repair table.
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

public class UpdatePdcController extends QiDialogController<DefectResultMaintModel, UpdatePdcDialog>{
	
	private String typeOfChange;
	private List<QiDefectResult> selectedDefectResultList;
	private List<QiRepairResult> selectedRepairResultList;
	
	public UpdatePdcController(DefectResultMaintModel model,
			UpdatePdcDialog updatePdcRegionalLocalAttrDialog,String productType, 
			String typeOfChange,List<QiDefectResult> selectedDefectResultList,List<QiRepairResult> selectedRepairResultList) {
		super();
		setModel(model);
		setDialog(updatePdcRegionalLocalAttrDialog);
		this.typeOfChange= typeOfChange;
		this.selectedDefectResultList = selectedDefectResultList;
		this.selectedRepairResultList= selectedRepairResultList;
	}
	
	@SuppressWarnings("unused")
	private void loadEntryModelValues(String productType) {
		getDialog().getEntryScreenComboBox().getItems().clear();
	}
	
	@Override
	public void handle(ActionEvent actionEvent) {
		clearDisplayMessage();
		if( actionEvent.getSource().equals(getDialog().getChangeBtn())) verifyChangeDefectsResults(actionEvent);
		else if( actionEvent.getSource().equals(getDialog().getCancelBtn())) cancelBtnAction(actionEvent);
		else if(actionEvent.getSource() instanceof UpperCaseFieldBean) {
			getDialog().reload(getDialog().getEntryScreenComboBox().getValue().toString().trim(), 
					getDialog().getEntryModelText().trim(), getDialog().getFilterTextValue().trim());
		}
	}
	
	ChangeListener<String> entryModelComboBoxChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> ov,  String old_val, String new_val) { 
			loadEntryScreenComboBox(new_val);
		}
	};
	
	ChangeListener<String> entryScreenComboBoxChangeListener = new ChangeListener<String>() {
		public void changed(ObservableValue<? extends String> ov,  String old_val, String new_val) { 
			String entryModel = getDialog().getEntryModelText(); 
			getDialog().reload(new_val, entryModel, getDialog().getFilterTextValue());
		}
	};
	
	@Override
	public void initListeners() {
		addEntryScreenComboBoxListener();
		addTablePaneListener();
	}

	private void addTablePaneListener() {
		getDialog().getLocalAttributeMaintTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<PdcRegionalAttributeMaintDto>() {
			@Override
			public void changed(ObservableValue<? extends PdcRegionalAttributeMaintDto> dto,PdcRegionalAttributeMaintDto oldValue,PdcRegionalAttributeMaintDto newValue) {
				if(newValue!=null){
				getDialog().getChangeBtn().setDisable(false);
				}
				else
				{
					getDialog().getChangeBtn().setDisable(true);
				}
			}
			
		});
		
	}

	
	private void addEntryScreenComboBoxListener() {
		getDialog().getEntryScreenComboBox().getSelectionModel().selectedItemProperty().addListener(entryScreenComboBoxChangeListener);
	}

	private void loadEntryScreenComboBox(String entryModel) {
		getDialog().getEntryScreenComboBox().getItems().clear();
		getDialog().getEntryScreenComboBox().setPromptText("Select");
		getDialog().getEntryScreenComboBox().getItems().addAll(getModel().findAllByEntryModel(entryModel));
	}

	public void attachEvent(){
		getDialog().getChangeBtn().setOnAction(getDialog().getController());
		getDialog().getCancelBtn().setOnAction(getDialog().getController());
	}
	
	private void verifyChangeDefectsResults(ActionEvent actionEvent) {
		PdcRegionalAttributeMaintDto attributeMaintDto = getDialog().getLocalAttributeMaintTablePane().getSelectedItem();
		List<QiDefectResult> updatedDefectRepairResultList = setDefectRepairResult(attributeMaintDto);
		if (updatedDefectRepairResultList == null) {
			displayErrorMessage("There is invalid defect or actual problem.", "There is invalid defect or actual problem.");
			return;
		}
		DefectDataReviewDialog defectDataReviewDialog = new DefectDataReviewDialog("Review/Verification", getModel(), "",
				selectedDefectResultList, updatedDefectRepairResultList, selectedRepairResultList, typeOfChange, false);
		defectDataReviewDialog.show();
		defectDataReviewDialog.scrollCurrentAndUpdateTablePane(typeOfChange);
		Stage stage = (Stage) getDialog().getChangeBtn().getScene().getWindow();
		stage.close();
		defectDataReviewDialog.toFront();
		actionEvent.consume();
	}
	
	
	private List<QiDefectResult> setDefectRepairResult(PdcRegionalAttributeMaintDto pdcRegionalAttributeMaintDto) {
		List<QiDefectResult> updatedDefectRepairResultList = new ArrayList<QiDefectResult>();
		if (typeOfChange.equals(QiConstant.DEFECT_TYPE)){
			for (QiDefectResult defectResultObj : selectedDefectResultList) {
				QiDefectResult updatedDefectRepairResult = createDefectResultObject(pdcRegionalAttributeMaintDto, defectResultObj.getDefectResultId());
				if (updatedDefectRepairResult == null) {
					return null;
				}
				updatedDefectRepairResultList.add(updatedDefectRepairResult);
			}
		} else if (typeOfChange.equals(QiConstant.REPAIR_TYPE)){
			for (QiRepairResult repairResultObj : selectedRepairResultList) {
				QiDefectResult updatedDefectRepairResult = createDefectResultObject(pdcRegionalAttributeMaintDto, repairResultObj.getDefectResultId());
				if (updatedDefectRepairResult == null) {
					return null;
				}
				updatedDefectRepairResultList.add(updatedDefectRepairResult);
			}
		}
		return updatedDefectRepairResultList;	
	}

	private QiDefectResult createDefectResultObject(PdcRegionalAttributeMaintDto pdcRegionalAttributeMaintDto, long defectResultId) {
		QiDefectResult defectResult = getModel().findDefectResultById(defectResultId);
		if (defectResult == null) return null;
		QiDefectIqsScore iqs = getModel().findQiDefectIqsScore(defectResultId);
		if (iqs != null)
			defectResult.setIqsScore(iqs.getIqsScore());
		defectResult.setInspectionPartName(pdcRegionalAttributeMaintDto.getInspectionPartName());
		defectResult.setInspectionPartLocationName(pdcRegionalAttributeMaintDto.getInspectionPartLocationName());
		defectResult.setInspectionPartLocation2Name(pdcRegionalAttributeMaintDto.getInspectionPartLocation2Name());
		defectResult.setInspectionPart2Name(pdcRegionalAttributeMaintDto.getInspectionPart2Name());
		defectResult.setInspectionPart2LocationName(pdcRegionalAttributeMaintDto.getInspectionPart2LocationName());
		defectResult.setInspectionPart2Location2Name(pdcRegionalAttributeMaintDto.getInspectionPart2Location2Name());
		defectResult.setInspectionPart3Name(pdcRegionalAttributeMaintDto.getInspectionPart3Name());
		defectResult.setDefectTypeName(pdcRegionalAttributeMaintDto.getDefectTypeName());
		defectResult.setDefectTypeName2(pdcRegionalAttributeMaintDto.getDefectTypeName2());
		defectResult.setReportable(pdcRegionalAttributeMaintDto.getReportable());
		defectResult.setEntryModel(getDialog().getEntryModelText().trim());
		defectResult.setEntryScreen(getDialog().getEntryScreenComboBox().getValue().toString().trim());
		defectResult.setDefectCategoryName(pdcRegionalAttributeMaintDto.getDefectCategory());

		if (getDialog().getUpdateAttribute().isSelected()) {
			if (pdcRegionalAttributeMaintDto.getResponsibleLevelId()!=null) {
				QiResponsibleLevel respLevel=getModel().findByResponsibleLevelId(pdcRegionalAttributeMaintDto.getResponsibleLevelId());
				if(respLevel!=null){
					defectResult.setResponsibleSite(respLevel.getSite());
					defectResult.setResponsiblePlant(respLevel.getPlant());
					defectResult.setResponsibleDept(respLevel.getDepartment());
					defectResult.setResponsibleLevel1(respLevel.getResponsibleLevelName());
					Integer responsibleLevel2 = respLevel.getUpperResponsibleLevelId();
					if (responsibleLevel2 != null) {
						QiResponsibleLevel respLevel2 = getModel().findByResponsibleLevelId(responsibleLevel2);
						if (respLevel2 != null) {
							defectResult.setResponsibleLevel2(respLevel2.getResponsibleLevelName());
							Integer responsibleLevel3 = respLevel2.getUpperResponsibleLevelId();
							if (responsibleLevel3 != null) {
								QiResponsibleLevel respLevel3 = getModel().findByResponsibleLevelId(responsibleLevel3);
								if (respLevel3 != null) {
									defectResult.setResponsibleLevel3(respLevel3.getResponsibleLevelName());
								} else {
									defectResult.setResponsibleLevel3("");
								}
							} else {
								defectResult.setResponsibleLevel3("");
							}
						} else {
							defectResult.setResponsibleLevel2("");
							defectResult.setResponsibleLevel3("");
						}
					} else {
						defectResult.setResponsibleLevel2("");
						defectResult.setResponsibleLevel3("");
					}
				}
			}
			
			if(pdcRegionalAttributeMaintDto.getPddaResponsibilityId()!=null){
				QiPddaResponsibility pddaResp=getModel().findByPddaResponsibilityId(pdcRegionalAttributeMaintDto.getPddaResponsibilityId());
				if(pddaResp!=null){
					defectResult.setUnitNo(pddaResp.getUnitNumber());
					defectResult.setUnitDesc(pddaResp.getUnitDescription());
					defectResult.setProcessName(pddaResp.getProcessName());
					defectResult.setProcessNo(pddaResp.getProcessNumber());
				}
			}
			defectResult.setEntrySiteName(pdcRegionalAttributeMaintDto.getEntrySite());
			defectResult.setEntryPlantName(pdcRegionalAttributeMaintDto.getEntryPlant());
			defectResult.setEntryProdLineNo(getModel().getCurrentWorkingProdLineNo(defectResult.getApplicationId()));
			defectResult.setRepairArea(pdcRegionalAttributeMaintDto.getRepairArea());
			defectResult.setRepairMethodNamePlan(pdcRegionalAttributeMaintDto.getRepairMethod());
			defectResult.setRepairTimePlan(pdcRegionalAttributeMaintDto.getRepairTime());
			defectResult.setEngineFiringFlag(pdcRegionalAttributeMaintDto.getEngineFiringFlag());
			defectResult.setIqsVersion(pdcRegionalAttributeMaintDto.getIqsVersion());
			defectResult.setIqsCategoryName(pdcRegionalAttributeMaintDto.getIqsCategory());
			defectResult.setIqsQuestionNo(pdcRegionalAttributeMaintDto.getIqsQuestionNo());
			defectResult.setIqsQuestion(pdcRegionalAttributeMaintDto.getIqsQuestion());
			defectResult.setThemeName(pdcRegionalAttributeMaintDto.getThemeName());
			defectResult.setLocalTheme(pdcRegionalAttributeMaintDto.getLocalTheme());
		}
		return defectResult;
	}
	
	private void cancelBtnAction(ActionEvent actionEvent) {
		try {
			Stage stage = (Stage) getDialog().getCancelBtn().getScene().getWindow();
			stage.close();
			} catch (Exception e) {
			handleException("An error occurred during cancel action,failed to perform cancel action", StringUtils.EMPTY, e);
			}
	}
	
}
