package com.honda.galc.device.dataformat;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.vios.dto.PddaPlatformDto;

public class MbpnData extends InputData implements Serializable {
	private String productSpecCode;
	private String productId;
	private String processPointId;
	private String orderNo;
	private PddaPlatformDto pddaPlatform;
	
	public MbpnData() {
		super();
	}

	public String getProductId() {
		return StringUtils.trim(productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProcessPointId() {
		return StringUtils.trim(processPointId);
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public String getProductSpecCode() {
		return StringUtils.trim(productSpecCode);
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	public String getOrderNo() {
		return StringUtils.trim(orderNo);
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public PddaPlatformDto getPddaPlatform() {
		return pddaPlatform;
	}

	public void setPddaPlatform(PddaPlatformDto pddaPlatform) {
		this.pddaPlatform = pddaPlatform;
	}
	
	
}
