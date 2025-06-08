package com.honda.galc.client.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.honda.galc.entity.conf.MCPddaPlatform;

public class MCPddaPlatformDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private int platformId;
	private long revId;
	private String plantLocCode;
	private String deptCode;
	private BigDecimal modelYearDate;
	private BigDecimal productScheduleQty;
	private String productAsmLineNo;
	private String vehicleModelCode;
	private String asmProcessNo;
	private String processPointId;
	private int processSeqNum;
	
	public int getPlatformId() {
		return platformId;
	}
	public void setPlatformId(int platformId) {
		this.platformId = platformId;
	}
	public long getRevId() {
		return revId;
	}
	public void setRevId(long revId) {
		this.revId = revId;
	}
	public String getPlantLocCode() {
		return plantLocCode;
	}
	public void setPlantLocCode(String plantLocCode) {
		this.plantLocCode = plantLocCode;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public BigDecimal getModelYearDate() {
		return modelYearDate;
	}
	public void setModelYearDate(BigDecimal modelYearDate) {
		this.modelYearDate = modelYearDate;
	}
	public BigDecimal getProductScheduleQty() {
		return productScheduleQty;
	}
	public void setProductScheduleQty(BigDecimal productScheduleQty) {
		this.productScheduleQty = productScheduleQty;
	}
	public String getProductAsmLineNo() {
		return productAsmLineNo;
	}
	public void setProductAsmLineNo(String productAsmLineNo) {
		this.productAsmLineNo = productAsmLineNo;
	}
	public String getVehicleModelCode() {
		return vehicleModelCode;
	}
	public void setVehicleModelCode(String vehicleModelCode) {
		this.vehicleModelCode = vehicleModelCode;
	}
	public String getAsmProcessNo() {
		return asmProcessNo;
	}
	public void setAsmProcessNo(String asmProcessNo) {
		this.asmProcessNo = asmProcessNo;
	}
	public String getProcessPointId() {
		return processPointId;
	}
	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}
	public int getProcessSeqNum() {
		return processSeqNum;
	}
	public void setProcessSeqNum(int processSeqNum) {
		this.processSeqNum = processSeqNum;
	}
}
