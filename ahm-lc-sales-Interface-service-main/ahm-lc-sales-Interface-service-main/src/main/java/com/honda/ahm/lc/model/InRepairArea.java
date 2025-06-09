package com.honda.ahm.lc.model;

public class InRepairArea extends AuditEntry{
	public InRepairArea() {
	}

	public InRepairArea(String productId, String repairAreaName, String parkingLocation, String rejection, String responsibleDept, String actualTimestamp) {
		this.productId = productId;
		this.repairAreaName = repairAreaName;
		this.parkingLocation = parkingLocation;
		this.rejection = rejection;
		this.responsibleDept = responsibleDept;
		this.actualTimestamp = actualTimestamp;
	}

	private static final long serialVersionUID = 1L;

    private String productId;

    private String repairAreaName;
    
    
    private String parkingLocation;
    
   
    private String rejection;
      
    
    private String responsibleDept;

    
    private String actualTimestamp;


	public String getProductId() {
		return productId;
	}


	public void setProductId(String productId) {
		this.productId = productId;
	}


	public String getRepairAreaName() {
		return repairAreaName;
	}


	public void setRepairAreaName(String repairAreaName) {
		this.repairAreaName = repairAreaName;
	}


	public String getParkingLocation() {
		return parkingLocation;
	}


	public void setParkingLocation(String parkingLocation) {
		this.parkingLocation = parkingLocation;
	}


	public String getRejection() {
		return rejection;
	}


	public void setRejection(String rejection) {
		this.rejection = rejection;
	}


	public String getResponsibleDept() {
		return responsibleDept;
	}


	public void setResponsibleDept(String responsibleDept) {
		this.responsibleDept = responsibleDept;
	}


	public String getActualTimestamp() {
		return actualTimestamp;
	}


	public void setActualTimestamp(String actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}
    
    
}
