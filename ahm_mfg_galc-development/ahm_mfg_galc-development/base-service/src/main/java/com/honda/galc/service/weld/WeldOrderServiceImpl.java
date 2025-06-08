package com.honda.galc.service.weld;

import com.honda.galc.dto.rest.ProductTrackDTO;

/**
 * 
 * @author Wade Pei <br>
 * @date Oct 22, 2013
 */
public class WeldOrderServiceImpl implements WeldOrderService {
	private WeldMbpnProductProcessor productProcessor;
	
	public WeldOrderServiceImpl() {
		productProcessor = new WeldMbpnProductProcessor();
	}
	
	public ProductTrackDTO trackProduct(ProductTrackDTO trackDTO) {
		productProcessor.processItem(trackDTO);
		return productProcessor.getItem();
	}
}
