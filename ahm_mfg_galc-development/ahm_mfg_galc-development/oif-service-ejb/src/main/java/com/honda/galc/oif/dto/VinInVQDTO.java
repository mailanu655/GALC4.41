package com.honda.galc.oif.dto;

import com.honda.galc.util.OutputData;
/**
 * 
 * <h3>VinInVQDTO.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> class to hold the Vin in VQ data </p>
 * <p> The record's format is like the following: </p>
 * <p> 05  SHIPPER-ID       		PIC  X(3).</p>
 * <p> 05  VERSION-NUMBER         	PIC  X(2).</p>   
 * <p> 05  VIN                 		PIC  X(17).</p>        
 * <p> 05  COND-KD-LOT				PIC  X(9).</p> 
 * <p> 05  COND-PRODUCTION-LOT		PIC  X(12).</p>          
 * <p> 05  SALES-MODEL-CODE			PIC  X(10).</p> 
 * <p> 05  SALES-MODEL-TYPE-CODE	PIC  X(4).</p>       
 * <p> 05  SALES-MODEL-OPTION-CODE	PIC  X(3).</p>            
 * <p> 05  SALES-EXT-COLOR-CODE		PIC  X(11).</p>     
 * <p> 05  SALES-INT-COLOR-CODE    	PIC  X(2).</p>           
 * <p> 05  MANF-MODEL-YEAR-CODE    	PIC  X(1).</p>         
 * <p> 05  MANF-MODEL-CODE         	PIC  X(3).</p>      
 * <p> 05  MANF-MODEL-TYPE-CODE    	PIC  X(3).</p>         
 * <p> 05  MANF-MODEL-OPTION-CODE  	PIC  X(3).</p>
 * <p> 05  MANF-EXT-COLOR-CODE		PIC  X(10).</p>
 * <p> 05  MANF-INT-COLOR-CODE		PIC  X(2).</p>
 * <p> 05  EXP-SHIP-DATE			PIC  X(8).</p>
 * <p> 05  LAST-WORKING-DATE		PIC  X(8).</p>
 * <p> 05  LAST-WORKING-TIME		PIC  X(6).</p>
 * <p> 05  FILLER					PIC  X(83).</p>
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
 * <TD>KG</TD>
 * <TD>Oct 21, 2014</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @version 0.1
 * @author KG
 * @created Oct 21, 2014
 */
public class VinInVQDTO implements IOutputFormat {
	@OutputData(value="SHIPPER_ID")
	private String shipperId;
	@OutputData(value="VERSION_NUMBER")
	private String versionNumber;
	@OutputData(value="VIN")
	private String vin;
	@OutputData(value="COND_KD_LOT")
	private String condKdLot;
	@OutputData(value="COND_PRODUCTION_LOT")
	private String condProductionLot;
	@OutputData(value="SALES_MODEL_CODE")
	private String salesModelCode;
	@OutputData(value="SALES_MODEL_TYPE_CODE")
	private String salesModelTypeCode;
	@OutputData(value="SALES_MODEL_OPTION")
	private String salesModelOption;
	@OutputData(value="SALES_EXT_COLOR_CODE")
	private String salesExtColorCode;
	@OutputData(value="SALES_INT_COLOR_CODE")
	private String salesIntColorCode;
	@OutputData(value="MANF_MODEL_CODE")
	private String manfModelCode;
	@OutputData(value="MANF_MODEL_TYPE_CODE")
	private String manfModelTypeCode;
	@OutputData(value="MANF_MODEL_OPTION_CODE")
	private String manfModelOptionCode;
	@OutputData(value="MANF_EXT_COLOR_CODE")
	private String manfExtColorCode;
	@OutputData(value="MANF_INT_COLOR_CODE")
	private String manfIntColorCode;
	@OutputData(value="EXP_SHIP_DATE")
	private String expShipDate;
	@OutputData(value="LAST_WORKING_DATE")
	private String lastWorkingDate;
	@OutputData(value="LAST_WORKING_TIME")
	private String lastWorkingTime;
	@OutputData(value="FILLER")
	private String filler;
	
	public VinInVQDTO() {
		super();
	}

	public String getShipperId() {
		return shipperId;
	}

	public void setShipperId(String shipperId) {
		this.shipperId = shipperId;
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getCondKdLot() {
		return condKdLot;
	}

	public void setCondKdLot(String condKdLot) {
		this.condKdLot = condKdLot;
	}

	public String getCondProductionLot() {
		return condProductionLot;
	}

	public void setCondProductionLot(String condProductionLot) {
		this.condProductionLot = condProductionLot;
	}

	public String getSalesModelCode() {
		return salesModelCode;
	}

	public void setSalesModelCode(String salesModelCode) {
		this.salesModelCode = salesModelCode;
	}

	public String getSalesModelTypeCode() {
		return salesModelTypeCode;
	}

	public void setSalesModelTypeCode(String salesModelTypeCode) {
		this.salesModelTypeCode = salesModelTypeCode;
	}
	
	public String getSalesModelOption() {
		return salesModelOption;
	}

	public void setSalesModelOption(String salesModelOption) {
		this.salesModelOption = salesModelOption;
	}

	public String getSalesExtColorCode() {
		return salesExtColorCode;
	}

	public void setSalesExtColorCode(String salesExtColorCode) {
		this.salesExtColorCode = salesExtColorCode;
	}

	public String getSalesIntColorCode() {
		return salesIntColorCode;
	}

	public void setSalesIntColorCode(String salesIntColorCode) {
		this.salesIntColorCode = salesIntColorCode;
	}

	public String getManfModelCode() {
		return manfModelCode;
	}

	public void setManfModelCode(String manfModelCode) {
		this.manfModelCode = manfModelCode;
	}

	public String getManfModelTypeCode() {
		return manfModelTypeCode;
	}

	public void setManfModelTypeCode(String manfModelTypeCode) {
		this.manfModelTypeCode = manfModelTypeCode;
	}

	public String getManfModelOptionCode() {
		return manfModelOptionCode;
	}

	public void setManfModelOptionCode(String manfModelOptionCode) {
		this.manfModelOptionCode = manfModelOptionCode;
	}

	public String getManfExtColorCode() {
		return manfExtColorCode;
	}

	public void setManfExtColorCode(String manfExtColorCode) {
		this.manfExtColorCode = manfExtColorCode;
	}

	public String getManfIntColorCode() {
		return manfIntColorCode;
	}

	public void setManfIntColorCode(String manfIntColorCode) {
		this.manfIntColorCode = manfIntColorCode;
	}

	public String getExpShipDate() {
		return expShipDate;
	}

	public void setExpShipDate(String expShipDate) {
		this.expShipDate = expShipDate;
	}

	public String getLastWorkingDate() {
		return lastWorkingDate;
	}

	public void setLastWorkingDate(String lastWorkingDate) {
		this.lastWorkingDate = lastWorkingDate;
	}

	public String getLastWorkingTime() {
		return lastWorkingTime;
	}

	public void setLastWorkingTime(String lastWorkingTime) {
		this.lastWorkingTime = lastWorkingTime;
	}

	public String getFiller() {
		return filler;
	}

	public void setFiller(String filler) {
		this.filler = filler;
	}
}
