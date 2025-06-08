package com.honda.galc.dto;

import com.honda.galc.util.ToStringUtil;

public class ProcessPointEfficiencyDto implements IDto {

	private static final long serialVersionUID = 1L;
	@DtoTag(outputName = "PROCESS_POINT_ID")
	private String processPointId;
	
	@DtoTag(outputName = "HOST_NAME")
	private String terminalId;
	
	@DtoTag(outputName = "UNIT_TIME")
	private double unitTime;
	
	private double processPointEfficiency;
	
	public String getProcessPointId() {
		return processPointId;
	}
	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public double getUnitTime() {
		return unitTime;
	}
	public void setUnitTime(double unitTime) {
		this.unitTime = unitTime;
	}
	public double getProcessPointEfficiency() {
		return processPointEfficiency;
	}
	public void setProcessPointEfficiency(double processPointEfficiency) {
		this.processPointEfficiency = processPointEfficiency;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((processPointId == null) ? 0 : processPointId.hashCode());
		result = prime * result
				+ ((terminalId == null) ? 0 : terminalId.hashCode());
		long temp;
		temp = Double.doubleToLongBits(unitTime);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(processPointEfficiency);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ProcessPointEfficiencyDto other = (ProcessPointEfficiencyDto) obj;
		if (processPointId == null) {
			if (other.processPointId != null) {
				return false;
			}
		} else if (!processPointId.equals(other.processPointId)) {
			return false;
		}
		if (terminalId == null) {
			if (other.terminalId != null) {
				return false;
			}
		} else if (!terminalId.equals(other.terminalId)) {
			return false;
		}
		if (Double.doubleToLongBits(unitTime) != Double
				.doubleToLongBits(other.unitTime)) {
			return false;
		}
		if (Double.doubleToLongBits(processPointEfficiency) != Double
				.doubleToLongBits(other.processPointEfficiency)) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
	
}
