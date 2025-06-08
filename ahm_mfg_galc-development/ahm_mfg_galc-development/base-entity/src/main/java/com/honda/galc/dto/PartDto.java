package com.honda.galc.dto;

import org.apache.commons.lang.StringUtils;

public class PartDto implements IDto {
	private static final long serialVersionUID = 1L;
	private String partNo;
	private String partMask;
	
	public PartDto(String partNo, String partMask) {
		super();
		this.partNo = partNo;
		this.partMask = partMask;
	}
	
	public String getPartNo() {
		return StringUtils.trimToEmpty(partNo);
	}
	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}
	public String getPartMask() {
		return StringUtils.trimToEmpty(partMask);
	}
	public void setPartMask(String partMask) {
		this.partMask = partMask;
	}

	@Override
	public String toString() {
		return "Part [partNo=" + partNo + ", partMask=" + partMask + "]";
	}
}
