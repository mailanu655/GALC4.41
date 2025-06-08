package com.honda.galc.oif.dto;

import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.util.GPCSData;

/**
 * 
 * <h3>GPP306DTO Class description</h3>
 * <p> GPP306DTO description </p>
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
 * @author Larry Karpov<br>
 * Jan 07, 2014
 *
 *
 */
public class GPP306DTO implements IPlanCodeDTO {
	private String productionLot;
	private String nextProductionLot;
	@GPCSData("PLAN_CD")
	private String planCode;
	@GPCSData("PROC_LOC")
	private String processLocation;
	@GPCSData("PROD_SEQ_NO")
	private String lotNumber;
	@GPCSData("KD_LOT_NO")
	private String kdLotNumber;
	@GPCSData("MTOC")
	private String productSpecCode;
	@GPCSData("NO_OF_UNIT_CICO")
	private int lotSize;
	@GPCSData("START_STAMP_NO")
	private String startProductId;
	//it is the Part number/MFG-BASIC-PART-NO
	@GPCSData ( "MBPN" )
	private String mbpn;
	//it is the HES Color/PART CLR CD/Part color code
	@GPCSData ( "HES_COLOR" )
	private String hesColor;
	
    public GPP306DTO() {
    }
    
    public String getProductionLot() {
		return productionLot;
	}
	
	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}
	
	public int getLotSize() {
		return lotSize;
	}
	
	public void setLotSize(int lotSize) {
		this.lotSize = lotSize;
	}
	
	public String getStartProductId() {
		return startProductId;
	}
	
	public void setStartProductId(String startProductId) {
		this.startProductId = startProductId;
	}
	
	public String getNextProductionLot() {
		return nextProductionLot;
	}
	
	public void setNextProductionLot(String nextProductionLot) {
		this.nextProductionLot = nextProductionLot;
	}
	
	public String getLotNumber() {
		return lotNumber;
	}
	
	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}
	
	public String getPlanCode() {
		return planCode;
	}
	
	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}
	
	public String getProcessLocation() {
		return processLocation;
	}
	
	public void setProcessLocation(String processLocation) {
		this.processLocation = processLocation;
	}
	
	public String getProductSpecCode() {
		return productSpecCode;
	}
	
	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	public void setKdLotNumber(String kdLotNumber) {
		this.kdLotNumber = kdLotNumber;
	}

	public String getKdLotNumber() {
		return kdLotNumber;
	}

	public PreProductionLot derivePreProductionLot() {
		PreProductionLot preProductionLot = new PreProductionLot();
//		preProductionLot.setProductionLot(productionLot);
//		preProductionLot.setNextProductionLot(nextProductionLot);
		preProductionLot.setPlantCode(planCode.substring(1,3));
		preProductionLot.setLineNo(planCode.substring(5,2));
		preProductionLot.setProcessLocation(processLocation);
		preProductionLot.setProductSpecCode(productSpecCode);
		preProductionLot.setStartProductId(startProductId);
		preProductionLot.setLotSize(lotSize);
		preProductionLot.setLotNumber(lotNumber);
		preProductionLot.setHoldStatus(1);
		return preProductionLot;
	}

	public ProductionLot deriveProductionLot() {
		ProductionLot productionLot = new ProductionLot();
//		productionLot.setProductionLot(productionLot);
//		preProductionLot.setNextProductionLot(nextProductionLot);
		productionLot.setPlanCode(planCode);
		productionLot.setPlantCode(planCode.substring(1,3));
		productionLot.setLineNo(planCode.substring(5,2));
		productionLot.setProcessLocation(processLocation);
		productionLot.setProductSpecCode(productSpecCode);
		productionLot.setStartProductId(startProductId);
		productionLot.setLotSize(lotSize);
		productionLot.setLotNumber(lotNumber);
		productionLot.setKdLotNumber(kdLotNumber);
		return productionLot;
	}

	public String createProductionLot() {
		String result = "";
		if(planCode != null && planCode.length() >= 6 && processLocation != null) {  
			result = planCode.substring(0, 6) + processLocation + lotNumber;
		}
		return result;
	}

	/**
	 * @return the mbpn
	 */
	public String getMbpn() {
		return mbpn;
	}

	/**
	 * @param mbpn the mbpn to set
	 */
	public void setMbpn(String mbpn) {
		this.mbpn = mbpn;
	}

	/**
	 * @return the hesColor
	 */
	public String getHesColor() {
		return hesColor;
	}

	/**
	 * @param hesColor the hesColor to set
	 */
	public void setHesColor(String hesColor) {
		this.hesColor = hesColor;
	}
}