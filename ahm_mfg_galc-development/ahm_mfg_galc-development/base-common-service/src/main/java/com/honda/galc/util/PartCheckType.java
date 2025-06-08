package com.honda.galc.util;

/**
 * @author Wade Pei <br>
 *         Jul 15, 2013
 * @author Keifer Xing
 */
public enum PartCheckType {
	//=== defects checks ===// 
	PART_NUMBER_NOT_CONTAINS_PRODUCT_ID_CHECK("Part Number Do Not Contains Product ID"),
	DUPLICATE_PART_FOR_ANTITHEFT_CHECK("Duplicate Part"),
	PART_NUMBER_NULL_CHECK("Part serial number is null"),
	PRODUCT_ID_NULL_CHECK("Product Id is null.");

	private String name;
	
	private PartCheckType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
