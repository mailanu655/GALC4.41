package com.honda.galc.dto;

import org.apache.commons.lang.StringUtils;

public class MCPendingProcessDto implements IDto {
	private static final long serialVersionUID = 1L;

	@DtoTag
	private long revId;
	
	@DtoTag
	private String asmProcNo;
	
	public long getRevId() {
		return revId;
	}
	public void setRevId(Long revId) {
		this.revId = revId;
	}
	public String getAsmProcNo() {
		return StringUtils.trim(asmProcNo);
	}
	public void setAsmProcNo(String asmProcNo) {
		this.asmProcNo = asmProcNo;
	}
}