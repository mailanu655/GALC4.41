package com.honda.galc.data;

/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Apr 30, 2012
 */

public enum HoldByNonProductType {
	
	AF_ON_SEQ_NUM("AfOnSeq");
		
	private String productName;
	
	private HoldByNonProductType(String productName) {
		this.productName = productName;	
	}
	
	public String getProductName() {
		return productName;
	}
	
	public static HoldByNonProductType getType(String productName) {
		
		for(HoldByNonProductType nonProductType :values()) {
			if(nonProductType.getProductName().equalsIgnoreCase(productName) ||
					nonProductType.name().equalsIgnoreCase(productName))	
				return nonProductType;
		}
		
		return null;
		
	}
	
}
