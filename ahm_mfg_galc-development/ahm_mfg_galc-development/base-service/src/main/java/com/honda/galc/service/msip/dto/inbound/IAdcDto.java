package com.honda.galc.service.msip.dto.inbound;

import java.sql.Timestamp;

/**
 * @author Anusha Gopalan
 * @date May, 2017
 */
public interface IAdcDto extends IMsipInboundDto {
	
	/**
	 * @return the productId
	 */
	public String getProductId();
	public void setProductId(String productId);
	public String getMessageDate();
	public String getMessageTime();
	public Timestamp getActualTimestamp();
	public void setActualTimestamp(Timestamp productId);
	public String getVin();
	public String getFrame();
	public String getTranType();
}



