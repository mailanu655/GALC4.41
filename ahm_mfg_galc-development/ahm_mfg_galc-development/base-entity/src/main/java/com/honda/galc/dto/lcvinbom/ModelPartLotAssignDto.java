package com.honda.galc.dto.lcvinbom;

import java.util.List;

import com.honda.galc.dto.IDto;


public class ModelPartLotAssignDto  implements IDto {
	private static final long serialVersionUID = 1L;
	
	
	private String selectedPlanCode;
	private String selectedProductionLot;
	private List<ModelPartLotRowDto> modelPartLotDtoList;
	
	public String getSelectedPlanCode() {
		return selectedPlanCode;
	}
	public void setSelectedPlanCode(String selectedPlanCode) {
		this.selectedPlanCode = selectedPlanCode;
	}
	public String getSelectedProductionLot() {
		return selectedProductionLot;
	}
	public void setSelectedProductionLot(String selectedProductionLot) {
		this.selectedProductionLot = selectedProductionLot;
	}
	public List<ModelPartLotRowDto> getModelPartLotDtoList() {
		return modelPartLotDtoList;
	}
	public void setModelPartLotDtoList(List<ModelPartLotRowDto> modelPartLotDtoList) {
		this.modelPartLotDtoList = modelPartLotDtoList;
	}

	
}
