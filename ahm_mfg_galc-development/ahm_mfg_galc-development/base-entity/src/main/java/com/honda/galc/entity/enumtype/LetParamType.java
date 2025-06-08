/**
 * 
 */
package com.honda.galc.entity.enumtype;
import com.honda.galc.enumtype.EnumUtil;


/**
 * @author vf031824
 *
 */

import com.honda.galc.enumtype.IdEnum;

/**
 * @author vf031824
 *
 */
public enum LetParamType implements IdEnum<LetParamType>{
	NONE(0),
	SERIAL_NUMBER(1),
	STATUS(2);
	
	private int id;
	
	private LetParamType(int id){
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public static LetParamType getType(int id) {
		return EnumUtil.getType(LetParamType.class, id);
	}
}