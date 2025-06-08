package com.honda.galc.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public enum ProductType {
	ENGINE("Engine",12), 
	FRAME("Frame",17),
	FRAME_JPN("Frame_JPN",11),
	HEAD("Head",17),
	BLOCK("Block",17),
	MISSION("Mission",12),
	MCASE("MCase",16),
	TCCASE("TCCase",16),
	FIPUCASE("FrontIpuCase",18),
	RIPUCASE("RearIpuCase",18),
	MPDR("MPDR",16),
	MPDN("MPDN",16),
	PSDR("PSDR",16),
	PSDN("PSDN",16),
	KNUCKLE("Knuckle",17,SubId.LEFT,SubId.RIGHT),
	MBPN("MBPN Product",17),
	PLASTICS("Plastics",13),
	WELD("Weld",17),
    IPU("Ipu",13),
	CRANKSHAFT("Crankshaft", 26),
	CONROD("Conrod",19),
	BUMPER("Bumper",17,SubId.FRONT,SubId.REAR),
	IPU_MBPN("IPU",17),
	TDU("TDU",17),
	BMP_MBPN("BMP_MBPN",17),
	SUBFRAME("Subframe",17),
	KNU_MBPN("KNU_MBPN", 17),
	MBPN_PART("MBPN_PART", 0);
	
	private int productIdLength;
	private String productName;
	private List<SubId> subIds =  new ArrayList<SubId>();
	private static Map<String, String> typeNames;
	
	private ProductType(String productName,int length) {
		this.productName = productName;
		this.productIdLength = length;
	}
	
	private ProductType(String productName,int length, SubId... subIds) {
		this.productName = productName;
		this.productIdLength = length;
		this.subIds.addAll(Arrays.asList(subIds));
	}

	public int getProductIdLength() {
		return productIdLength;
	}

	public void setProductIdLength(int productIdLength) {
		this.productIdLength = productIdLength;
	}
	
	public String getProductName() {
		return productName;
	}
	
	public List<SubId> getSubIds() {
		return subIds;
	}
	

	public static ProductType getType(String productName) {
		
		for(ProductType productType :values()) {
			if(productType.getProductName().equalsIgnoreCase(productName) ||
					productType.name().equalsIgnoreCase(productName))	
				return productType;
		}
		
		return null;
		
	}
	
	public static Map<String, String> getProductTypeNames(){
		if(typeNames == null){
			typeNames = new LinkedHashMap<String, String>();
			for(ProductType type : values())
				typeNames.put(type.name(), type.getProductName());
		}
		return typeNames;
		
	}
	
	public static boolean contains(String type){
		return getProductTypeNames().keySet().contains(type);
	}
	
}
