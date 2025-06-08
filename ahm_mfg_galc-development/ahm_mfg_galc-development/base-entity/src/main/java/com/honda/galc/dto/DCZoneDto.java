package com.honda.galc.dto;

import com.honda.galc.entity.conf.DCZone;

public class DCZoneDto implements IDto {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DCZone zone;
	private String processPointName;
		
	public DCZoneDto(DCZone zone, String processPointName) {
		this.zone = zone;
		this.processPointName = processPointName;
	}
		
	public String getProcessPointName() {
		return processPointName;
	}
	
	public void setProcessPointName(String processPointName) {
		this.processPointName = processPointName;
	}
		
	public String getProcessPointId() {
		return zone.getProcessPointId();
	}
	
	public void setProcessPointId(String processPointId) {
		zone.getId().setProcessPointId(processPointId);
	}
		
	public boolean getRepairable() {
		return zone.getRepairable();
	}
	
	public void setRepairable(boolean repairable) {
		zone.getId().setRepairable(repairable);
	}
		
	public DCZone getZone() {
		return zone;
	}

}
