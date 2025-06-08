package com.honda.galc.client.teamleader.qi.controller;

import java.util.List;

import com.honda.galc.client.teamleader.qi.model.PdcLocalAttributeMaintModel;
import com.honda.galc.client.teamleader.qi.view.UpdatePddaModelYearDialog;
import com.honda.galc.dto.qi.PdcRegionalAttributeMaintDto;
import com.honda.galc.entity.qi.QiLocalDefectCombination;
import com.honda.galc.entity.qi.QiPddaResponsibility;
import com.honda.galc.entity.qi.QiResponsibleLevel;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

/**
 * 
 * <h3>UpdatePddaModelYearController Class description</h3>
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
public class UpdatePddaModelYearController  extends QiDialogController<PdcLocalAttributeMaintModel, UpdatePddaModelYearDialog> {
	
	private List<PdcRegionalAttributeMaintDto> selectedDataList;
	
	public UpdatePddaModelYearController(PdcLocalAttributeMaintModel  pdcLocalAttributeMaintModel, UpdatePddaModelYearDialog updatePddaModelYearDialog,
			List<PdcRegionalAttributeMaintDto> selectedDataList) {
		super();
		setModel(pdcLocalAttributeMaintModel);
		setDialog(updatePddaModelYearDialog);
		this.selectedDataList=selectedDataList;
	}

	public void loadInitialData() {
		getDialog().getModelYearComboBox().getControl().getItems().addAll(getModel().findAllModelYear());
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource().equals(getDialog().getUpdateButton())) updateButtonAction();
		else if(actionEvent.getSource().equals(getDialog().getCancelButton())) cancelButtonAction();
	}

	@Override
	public void initListeners() {
		getDialog().getModelYearComboBox().getControl().valueProperty().addListener(updateEnablerForBigDecimalValueChange);
	}
	
	/**
	 * This method defines action for Done button .
	 */
	private void updateButtonAction() {
		for(PdcRegionalAttributeMaintDto dto : selectedDataList){
			if(dto.getPddaResponsibilityId()!=0 && dto.getPddaResponsibilityId()!=null){
				QiPddaResponsibility pddaRespForPdc=getModel().findByPddaId(dto.getPddaResponsibilityId());
				QiLocalDefectCombination selectedPdc=getModel().findByLocalDefectCombId(dto.getLocalAttributeId());
				// check if both unit number & process number are valid or only unit number is valid
				List<QiPddaResponsibility> pddaResponsibilityListWithNewModelYear = 
						getModel().findByNewModelYear(getDialog().getModelYearComboBox().getControl().getSelectionModel().getSelectedItem(), pddaRespForPdc);
				
				if (pddaResponsibilityListWithNewModelYear != null && pddaResponsibilityListWithNewModelYear.size() > 0) {
					QiPddaResponsibility pddaResponsibilityWithNewModelYear = pddaResponsibilityListWithNewModelYear.get(0);
					for (int i = 1; i < pddaResponsibilityListWithNewModelYear.size(); i++) {
						QiPddaResponsibility qiPddaResponsibility = pddaResponsibilityListWithNewModelYear.get(i);
						if (pddaRespForPdc.getProcessNumber().equals(qiPddaResponsibility.getProcessNumber())) { //process number matched
							pddaResponsibilityWithNewModelYear = qiPddaResponsibility;
							break;
						}
					}
					//update PDDA responsibility
					selectedPdc.setPddaResponsibilityId(pddaResponsibilityWithNewModelYear.getPddaResponsibilityId());
						
					//update Resp level 1
					QiResponsibleLevel level = getModel().findLevel1BySitePlantDepartmentLevelNameAndLevel(pddaResponsibilityWithNewModelYear.getResponsibleSite(),
							pddaResponsibilityWithNewModelYear.getResponsiblePlant(),pddaResponsibilityWithNewModelYear.getResponsibleDept(),pddaResponsibilityWithNewModelYear.getResponsibleLevel1());
					if (level != null) {
						selectedPdc.setResponsibleLevelId(level.getResponsibleLevelId());
					}
				} else {
					selectedPdc.setPddaResponsibilityId(null);
				}
				getModel().updatePddaRespId(selectedPdc);
			}
		}
		Stage stage = (Stage) getDialog().getUpdateButton().getScene().getWindow();
		stage.close();
		
	}
	
	/**
	 * This method defines action for Cancel button .
	 */
	private void cancelButtonAction() {
		try {
			Stage stage = (Stage) getDialog().getCancelButton().getScene().getWindow();
			getDialog().setCancel(true);
			stage.close();
		} catch (Exception e) {
			handleException("An error occured during cancel action ", "Failed to perform cancel action",e);
		}
	}
	
}
