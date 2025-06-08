package com.honda.galc.entity.enumtype;
/**
 * 
 * @author Gangadhararao Gadde
 * @date May 22, 2014
 */
public enum ProdLotDemandType {
	
	MP("MP");

	private String type;

	private ProdLotDemandType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

}