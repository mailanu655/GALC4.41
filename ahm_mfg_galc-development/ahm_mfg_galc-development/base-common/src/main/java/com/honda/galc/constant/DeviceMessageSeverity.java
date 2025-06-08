package com.honda.galc.constant;

/**
 * This enum is used by the DeviceManager, View class and classes that
 * implement IDevice interface to communicate messages between them.
 *  
 * @author Subu Kathiresan
 * Feb 15, 2009
 */
public enum DeviceMessageSeverity 
{
	info(0),
	warning(1),
	success(2),
	error(3);
	
	private int _typeCode;
	
	/**
	 * 
	 */
	DeviceMessageSeverity(int messageType)	{
		_typeCode = messageType;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getTypeCode(){
		return _typeCode;
	}
}
