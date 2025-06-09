package com.honda.ahm.lc.model;

import java.util.Date;

public class InProcessProduct extends AuditEntry {
    
    private String productId;

    
    private String lineId;

    
    private String nextProductId;

   
    private String productSpecCode;

    
    private String productionLot;

    
    private Date planOffDate;

    
    private String lastPassingProcessPointId;


	public String getProductId() {
		return productId;
	}


	public void setProductId(String productId) {
		this.productId = productId;
	}


	public String getLineId() {
		return lineId;
	}


	public void setLineId(String lineId) {
		this.lineId = lineId;
	}


	public String getNextProductId() {
		return nextProductId;
	}


	public void setNextProductId(String nextProductId) {
		this.nextProductId = nextProductId;
	}


	public String getProductSpecCode() {
		return productSpecCode;
	}


	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}


	public String getProductionLot() {
		return productionLot;
	}


	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}


	public Date getPlanOffDate() {
		return planOffDate;
	}


	public void setPlanOffDate(Date planOffDate) {
		this.planOffDate = planOffDate;
	}


	public String getLastPassingProcessPointId() {
		return lastPassingProcessPointId;
	}


	public void setLastPassingProcessPointId(String lastPassingProcessPointId) {
		this.lastPassingProcessPointId = lastPassingProcessPointId;
	}
    
    
}
