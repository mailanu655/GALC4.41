package com.honda.galc.client.device.ubisense;

import com.honda.galc.device.dataformat.ProductId;

/**
 * Ubisense (ACS) next expected product ID telegram packet
 * 
 * @author Bernard Leong
 * @date Jun 21, 2017
 */
public class UbisenseProductId extends ProductId {
	private static final long serialVersionUID = 1L;

	public UbisenseProductId() {
		productId = "";
	}
	
	public UbisenseProductId(String telegram) {
		productId = telegram.substring(39, 56);
	}
	
	public UbisenseProductId parseTelegram(String telegram) {
		productId = telegram.substring(39, 56);
		return this;
	}
}
