package com.honda.galc.entity.enumtype;
/**
 * 
 * @author hcm_adm_015809
 *
 */
public enum AbnormalType {
	DUPLICATE_PART("Duplicate Part"),
	BATTERY_EXPIRATION("Battery Expiration");
	
    private final String name;
   
    private AbnormalType(String name) {
		this.name = name;
    }
    
	public String getName() {
		return name;
	}

}
