package com.honda.galc.vios.dto;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class SafetyErgos {

	private SimpleStringProperty unitNo = null;
	
	private SimpleStringProperty unitOfOperationName = null;
	
	private SimpleStringProperty safetyErgoPt = null;
	
	private SimpleStringProperty safetyErgoInst = null;
	
	private SimpleIntegerProperty maintenanceId = null;
	
	public SafetyErgos() {
		unitNo = new SimpleStringProperty();
		unitOfOperationName = new SimpleStringProperty();
		safetyErgoPt = new SimpleStringProperty();
		safetyErgoInst = new SimpleStringProperty();
		maintenanceId = new SimpleIntegerProperty();
	}

	public String getUnitNo() {
		return unitNo.get();
	}

	public void setUnitNo(String unitNo) {
		this.unitNo.set(unitNo);
	}

	public String getUnitOfOperationName() {
		return unitOfOperationName.get();
	}

	public void setUnitOfOperationName(String unitOfOperationName) {
		this.unitOfOperationName.set(unitOfOperationName);
	}

	public String getSafetyErgoPt() {
		return safetyErgoPt.get();
	}

	public void setSafetyErgoPt(String safetyErgoPt) {
		this.safetyErgoPt.set(safetyErgoPt);
	}

	public String getSafetyErgoInst() {
		return safetyErgoInst.get();
	}

	public void setSafetyErgoInst(String safetyErgoInst) {
		this.safetyErgoInst.set(safetyErgoInst);
	}

	public void setMaintenanceId(int maintenanceId) {
		this.maintenanceId.set(maintenanceId);
	}

	public int getMaintenanceId() {
		return maintenanceId.get();
	}
	
	

}
