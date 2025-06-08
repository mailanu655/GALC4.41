package com.honda.galc.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class StructureDetailsDto implements IDto{
	private static final long serialVersionUID = 1L;
	
	@DtoTag(outputName = "PRODUCT_ID")
	private String productId;
	
	@DtoTag(outputName = "PRODUCTION_LOT")
	private String productionLot;
	
	@DtoTag(outputName = "PRODUCT_SPEC_CODE")
	private String productSpecCode;
	
	@DtoTag(outputName = "STRUCTURE_REVISION")
	private long structureRevision;
	
	@DtoTag(outputName = "DIVISION")
	private String division;
	
	@DtoTag(outputName = "CREATE_TIMESTAMP")
	private Date createTimestamp;
	
	@DtoTag(outputName = "PROCESS_POINT_ID")
	private String processPointId;
	
	private boolean structureCreateFlag;
	
	public String getProductId() {
		return StringUtils.trimToEmpty(productId);
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductionLot() {
		return StringUtils.trimToEmpty(productionLot);
	}
	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}
	public String getProductSpecCode() {
		return StringUtils.trimToEmpty(productSpecCode);
	}
	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}
	public long getStructureRevision() {
		return this.structureRevision;
	}
	public void setStructureRevision(long structureRevision) {
		this.structureRevision = structureRevision;
	}
	public String getDivision() {
		return StringUtils.trimToEmpty(division);
	}
	public void setDivision(String division) {
		this.division = division;
	}
	public Date getCreateTimestamp() {
		return createTimestamp;
	}
	public void setCreateTimestamp(Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}
	public String getProductionDate() {
		return (new SimpleDateFormat("MM-dd-yyyy")).format(this.createTimestamp);
	}
	public String getProcessPointId() {
		return processPointId;
	}
	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}
	public boolean isStructureCreateFlag() {
		return structureCreateFlag;
	}
	public void setStructureCreateFlag(boolean structureCreateFlag) {
		this.structureCreateFlag = structureCreateFlag;
	}
	
	
}
