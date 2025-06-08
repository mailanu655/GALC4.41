package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

@Entity
@Table(name="TGA3051_TBX")
public class HostPriorityPlan extends AuditEntry {
	@EmbeddedId
	private HostPriorityPlanId id;

	@Column(name="DEMAND_TYPE")
	private String demandType;

	@Column(name="KD_LOT_NO")
	private String kdLotNo;

	@Column(name="MODEL_YEAR_CODE")
	private String modelYearCode;

	@Column(name="MODEL_CODE")
	private String modelCode;

	@Column(name="MODEL_TYPE_CODE")
	private String modelTypeCode;

	@Column(name="MODEL_OPTION")
	private String modelOption;

	@Column(name="EXT_COLOUR_CODE")
	private String extColourCode;

	@Column(name="INT_COLOUR_CODE")
	private String intColourCode;

	@Column(name="NO_OF_UNITS")
	private int noOfUnits;

	@Column(name="START_VIN_NUMBER")
	private String startVinNumber;

	@Column(name="REMAKE_FLAG")
	private String remakeFlag;

	@Column(name="Y_N_FLAG")
	private String yNFlag;

	@Column(name="CARRY_I_O_FLAG")
	private String carryIOFlag;

	@Column(name="NO_OF_UNITS_CICO")
	private int noOfUnitsCico;

	@Column(name="PLAN_CREATED")
	private String planCreated;

	@Column(name="ROW_PROCESSED")
	private String rowProcessed;

	private static final long serialVersionUID = 1L;

	public HostPriorityPlan() {
		super();
	}

	public HostPriorityPlanId getId() {
		return this.id;
	}

	public void setId(HostPriorityPlanId id) {
		this.id = id;
	}

	public String getDemandType() {
		return StringUtils.trim(this.demandType);
	}

	public void setDemandType(String demandType) {
		this.demandType = demandType;
	}

	public String getKdLotNo() {
		return StringUtils.trim(this.kdLotNo);
	}

	public void setKdLotNo(String kdLotNo) {
		this.kdLotNo = kdLotNo;
	}

	public String getModelYearCode() {
		return StringUtils.trim(this.modelYearCode);
	}

	public void setModelYearCode(String modelYearCode) {
		this.modelYearCode = modelYearCode;
	}

	public String getModelCode() {
		return StringUtils.trim(this.modelCode);
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getModelTypeCode() {
		return StringUtils.stripEnd(this.modelTypeCode, null);
	}

	public void setModelTypeCode(String modelTypeCode) {
		this.modelTypeCode = modelTypeCode;
	}

	public String getModelOption() {
		return StringUtils.trim(this.modelOption);
	}

	public void setModelOption(String modelOption) {
		this.modelOption = modelOption;
	}

	public String getExtColourCode() {
		return StringUtils.trim(this.extColourCode);
	}

	public void setExtColourCode(String extColourCode) {
		this.extColourCode = extColourCode;
	}

	public String getIntColourCode() {
		return StringUtils.trim(this.intColourCode);
	}

	public void setIntColourCode(String intColourCode) {
		this.intColourCode = intColourCode;
	}

	public int getNoOfUnits() {
		return this.noOfUnits;
	}

	public void setNoOfUnits(int noOfUnits) {
		this.noOfUnits = noOfUnits;
	}

	public String getStartVinNumber() {
		return StringUtils.trim(this.startVinNumber);
	}

	public void setStartVinNumber(String startVinNumber) {
		this.startVinNumber = startVinNumber;
	}

	public String getRemakeFlag() {
		return StringUtils.trim(this.remakeFlag);
	}

    public boolean isRemake() {
        return getRemakeFlag().equalsIgnoreCase("Y");
    }
    
 	public void setRemakeFlag(String remakeFlag) {
		this.remakeFlag = remakeFlag;
	}

	public String getYNFlag() {
		return StringUtils.trim(this.yNFlag);
	}

	public void setYNFlag(String yNFlag) {
		this.yNFlag = yNFlag;
	}

	public String getCarryIOFlag() {
		return StringUtils.trim(this.carryIOFlag);
	}

	public void setCarryIOFlag(String carryIOFlag) {
		this.carryIOFlag = carryIOFlag;
	}

	public int getNoOfUnitsCico() {
		return this.noOfUnitsCico;
	}

	public void setNoOfUnitsCico(int noOfUnitsCico) {
		this.noOfUnitsCico = noOfUnitsCico;
	}

	public String getPlanCreated() {
		return StringUtils.trim(this.planCreated);
	}

	public void setPlanCreated(String planCreated) {
		this.planCreated = planCreated;
	}

	public String getRowProcessed() {
		return StringUtils.trim(this.rowProcessed);
	}
	
	public boolean isRowProcessed() {
		return "Y".equalsIgnoreCase(this.rowProcessed);
	}
	
	public void setRowProcessed(String rowProcessed) {
		this.rowProcessed = rowProcessed;
	}
	
	public void setRowProcessed (Boolean flag) {
		this.rowProcessed = flag ? "Y" : "N";
	}
	
	public String getProductSpecCode() {
		return this.modelYearCode + this.modelCode + this.modelTypeCode + this.modelOption + this.extColourCode + this.intColourCode;
	}

	public ProductionLot deriveProductionLot() {
		return deriveProductionLot(getId().deriveProductionLot(), getId().getPlanProcLoc());
	}
	
	public ProductionLot deriveProductionLot(String processLocation){
		return deriveProductionLot(getId().deriveProductionLot(processLocation), processLocation);
	}
	
	public ProductionLot deriveProductionLot(String prodLot, String processLocation) {
		ProductionLot productionLot = new ProductionLot();
		
		productionLot.setProductionLot(prodLot);
		productionLot.setPlantCode(getId().getPlantCode());
		productionLot.setLineNo(getId().getLineNumber());
		productionLot.setProcessLocation(processLocation);
		productionLot.setWeLineNo(getId().getWeLineNumber());
		productionLot.setWeProcessLocation(getId().getWePlanProcLoc());
		productionLot.setPaLineNo(getId().getPaLineNumber());
		productionLot.setPaProcessLocation(getId().getPaPlanProcLoc());
		productionLot.setProductSpecCode(getProductSpecCode());
		productionLot.setStartProductId(getStartVinNumber());
		productionLot.setKdLotNumber(getKdLotNo());
		productionLot.setLotSize(getNoOfUnits());
		productionLot.setDemandType(getDemandType());
		productionLot.setPlanOffDate(getId().getAfaeOffDate());
		productionLot.setProductionDate(getId().getAfaeOffDate());
		productionLot.setLotNumber(getId().getProdSeqNumber());
		productionLot.setPlanCode(getId().getPlanCode());
		productionLot.setProdLotKd(prodLot);
		
		return productionLot;
	}
	
	public ProductionLot deriveKnuckleProductionLot() {
		
		ProductionLot productionLot = new ProductionLot();
		
		productionLot.setProductionLot(getId().deriveKnuckleProductionLot());
		productionLot.setPlantCode(getId().getPlantCode());
		productionLot.setLineNo(getId().getLineNumber());
		productionLot.setProcessLocation(PreProductionLot.PROCESS_LOCATION_KNUCKLE);
		productionLot.setWeLineNo(getId().getWeLineNumber());
		productionLot.setWeProcessLocation(getId().getWePlanProcLoc());
		productionLot.setPaLineNo(getId().getPaLineNumber());
		productionLot.setPaProcessLocation(getId().getPaPlanProcLoc());
		productionLot.setProductSpecCode(getProductSpecCode());
		productionLot.setKdLotNumber(getKdLotNo());
		productionLot.setLotSize(getNoOfUnits());
		productionLot.setDemandType(getDemandType());
		productionLot.setPlanOffDate(getId().getAfaeOffDate());
		productionLot.setProductionDate(getId().getAfaeOffDate());
		productionLot.setLotNumber(getId().getProdSeqNumber());
		productionLot.setPlanCode(getId().getPlanCode());
		
		return productionLot;
	}
	
	public PreProductionLot derivePreProductionLot(String processLocation){
		return derivePreProductionLot(getId().deriveProductionLot(processLocation), processLocation);
	}
	
	public PreProductionLot derivePreProductionLot(String productionLot,String processLocation) {
		
		PreProductionLot prodLot = new PreProductionLot();
		
		prodLot.setProductionLot(productionLot);
		prodLot.setLotSize(getNoOfUnits());
		prodLot.setStartProductId(getStartVinNumber());
		prodLot.setPlantCode(getId().getPlantCode());
		prodLot.setProcessLocation(processLocation);
		prodLot.setKdLotNumber(getKdLotNo());
		prodLot.setLotNumber(getId().getProdSeqNumber());
		prodLot.setLineNo(getId().getLineNumber());
		prodLot.setProductSpecCode(getProductSpecCode());
		prodLot.setHoldStatus(1);
		
		return prodLot;
	}
	
	public PreProductionLot derivePreProductionLot() {
		return derivePreProductionLot(getId().deriveProductionLot(),getId().getPlanProcLoc());
	}
	
	public PreProductionLot deriveKnucklePreProductionLot() {
		
		PreProductionLot prodLot = new PreProductionLot();
		
		prodLot.setProductionLot(getId().deriveKnuckleProductionLot());
		prodLot.setLotSize(getNoOfUnits());
		prodLot.setStartProductId(getStartVinNumber());
		prodLot.setPlantCode(getId().getPlantCode());
		prodLot.setProcessLocation(PreProductionLot.PROCESS_LOCATION_KNUCKLE);
		prodLot.setKdLotNumber(getKdLotNo());
		prodLot.setLotNumber(getId().getProdSeqNumber());
		prodLot.setLineNo(getId().getLineNumber());
		prodLot.setProductSpecCode(getProductSpecCode());
		prodLot.setHoldStatus(1);
		
		return prodLot;
	}
	
	public boolean isSameKdLot(HostPriorityPlan hostPriorityPlan) {
		
		return getKdLotNo().substring(0,getKdLotNo().length() -1).equals(
				hostPriorityPlan.getKdLotNo().substring(0,hostPriorityPlan.getKdLotNo().length() -1));
		
	}
	
	public String getKdLotSequence() {
		return getId().getProdSeqNumber() + getKdLotNo();
	}
	
	public String toString() {
		return getKdLotNo();
	}
	

}
