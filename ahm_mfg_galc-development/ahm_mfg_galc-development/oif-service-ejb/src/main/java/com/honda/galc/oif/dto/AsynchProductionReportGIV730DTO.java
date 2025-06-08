package com.honda.galc.oif.dto;

import com.honda.galc.util.OutputData;
/**
 * 
 * <h3>AsynchProductionReportGIV730DTO.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> POJO class to hold the asynchronous production report data </p>
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
 * <TD>Justin Jiang</TD>
 * <TD>Nov 24, 2014</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @version 0.1
 * @author Justin Jiang
 * @created Nov 24, 2014
 */
public class AsynchProductionReportGIV730DTO implements IOutputFormat {
	@OutputData(value="PLAN_CODE")
	private String planCode;
	
//	PRODUCT_SPEC_CODE=MBPN+COLOR_CODE, 
//	use PRODUCT_SPEC_CODE instead of MBPN+COLOR_COLOR
	@OutputData(value="PRODUCT_SPEC_CODE")
	private String productSpecCode;
	@OutputData(value="PRODUCTION_QUANTITY")
	private String productionQuantity;
	@OutputData(value="PRODUCTION_DATE")
	private String productionDate;
	@OutputData(value="PRODUCTION_TIME")
	private String productionTime;
	@OutputData(value="LOT_NUMBER")
	private String lotNumber;
	@OutputData(value="FILLER")
	private String filler;
	
	public AsynchProductionReportGIV730DTO() {
		super();
	}

	public String getPlanCode() {
		return planCode;
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public String getProductSpecCode() {
		return productSpecCode;
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	public String getProductionQuantity() {
		return productionQuantity;
	}

	public void setProductionQuantity(String productionQuantity) {
		this.productionQuantity = productionQuantity;
	}

	public String getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}

	public String getProductionTime() {
		return productionTime;
	}

	public void setProductionTime(String productionTime) {
		this.productionTime = productionTime;
	}

	/**
	 * @return the lotNumber
	 */
	public String getLotNumber() {
		return lotNumber;
	}

	/**
	 * @param lotNumber the lotNumber to set
	 */
	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	public String getFiller() {
		return filler;
	}

	public void setFiller(String filler) {
		this.filler = filler;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AsynchProductionReportGIV730DTO [planCode=" + planCode + ", productSpecCode=" + productSpecCode
				+ ", productionQuantity=" + productionQuantity + ", productionDate=" + productionDate
				+ ", productionTime=" + productionTime + ", lotNumber=" + lotNumber + "]";
	}
}
