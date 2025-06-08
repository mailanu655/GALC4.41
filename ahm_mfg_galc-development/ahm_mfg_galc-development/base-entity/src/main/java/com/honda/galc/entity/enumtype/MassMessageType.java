package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.enumtype.IdEnum;

public enum MassMessageType implements IdEnum<MassMessageType> {
	PLANT(1), DEPT(2), SAFETY(3), QUALITY(4);
	
    private final int messageType;

	
	private MassMessageType(int type) {
		this.messageType = type;
	}

	public int getId() {
		return messageType;
	}

	public int getMessageType() {
		return messageType;
	}
	
	public String getMessageWithString(){
		return toString() + " : ";
	}

    public static MassMessageType getMessageType(int messageType){
    	return EnumUtil.getType(MassMessageType.class, messageType);
    }

}
