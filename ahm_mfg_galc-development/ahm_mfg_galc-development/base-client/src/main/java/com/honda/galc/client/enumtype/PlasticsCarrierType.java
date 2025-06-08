/**
 * 
 */
package com.honda.galc.client.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

/**
 * @author Subu Kathiresan
 * @date Nov 9, 2012
 */
public enum PlasticsCarrierType implements IdEnum<PlasticsCarrierType> {
	
	Bumper (1, "Bumper"),
	InstrumentPanel (2, "InstrumentPanel");
	
    private final int _id;
    private String _typeString;

    private PlasticsCarrierType(int id, String typeString) {
        _id = id;
        _typeString = typeString;
    }
    
    public int getId() {
    	return _id;
    }
    
    public String getTypeString() {
    	return _typeString;
    }

    public static PlasticsCarrierType getType(int id) {
        return EnumUtil.getType(PlasticsCarrierType.class, id);
    }
}
