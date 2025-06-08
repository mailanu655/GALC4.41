package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/*
 * 
 * @author Gangadhararao Gadde 
 * @ Feb 06, 2014
 */
public enum InspectionSamplingType implements IdEnum<InspectionSamplingType> {
	SAMPLING_NOTHING(0,"Sampling Nothing"), SAMPLING_EMISSION(1,"Sampling Test"),SAMPLING_DIMENSION(2,"Spec Check Test"),SAMPLING_BOTH(3,"Sampling Test and Spec Check Test") ;

    private final int id;
    private String typeString;

    private InspectionSamplingType(int id,String typeString) {
        this.id = id;
        this.typeString= typeString;
    }

    public int getId() {
        return id;
    }
    
    public String getTypeString() {
    	return typeString;
    }

    public static InspectionSamplingType getType(int id) {
        return EnumUtil.getType(InspectionSamplingType.class, id);
    }
}
