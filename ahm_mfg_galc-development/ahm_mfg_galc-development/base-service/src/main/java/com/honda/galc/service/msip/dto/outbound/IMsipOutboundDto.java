package com.honda.galc.service.msip.dto.outbound;

import com.honda.galc.dto.IDto;

/**
 * @author Subu Kathiresan
 * @date Jun 13, 2017
 */
public interface IMsipOutboundDto extends IDto {
	
	public String getSiteName();
	public String getPlantName();
	public String getProcessPointId();
	public Boolean getIsError();
	public String getErrorMsg();
	public void setIsError(Boolean isError);
	public void setErrorMsg(String errorMsg);
	public String getVersion();
}
