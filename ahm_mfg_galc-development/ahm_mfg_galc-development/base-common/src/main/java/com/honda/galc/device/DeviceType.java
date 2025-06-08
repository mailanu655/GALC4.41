package com.honda.galc.device;


import java.util.TreeMap;

/**
 * @author Subu Kathiresan
 * Feb 4, 2009
 */
public enum DeviceType 
{
	Unknown(0),
	Parallel(1),
	TCPSocket(2),
	Serial(3),
	PlcTCPDevice(4),
	PlcUDPDevice(5),
	EiDevice(6),
	Printer(7),
	MQPrinter(8);

	private int _value;
	private static TreeMap<Integer, DeviceType> _enumMap;
	
	/**
	 * 
	 * @param typeId
	 */
	DeviceType(int typeId)
	{
		_value = typeId;
	}
	
	/**
	 * 
	 */
	static 
	{
		// Create a map of all enum values with typeId as keys
		_enumMap = new TreeMap<Integer, DeviceType>();
		
		for (DeviceType deviceType: DeviceType.values())
		{
			_enumMap.put(new Integer(deviceType.value()), deviceType);
		}
	}
	
	/**
	 * 
	 * @param typeId
	 * @return
	 */
	public static DeviceType getDeviceType(Integer typeId)
	{
		try
		{
			if (_enumMap.containsKey(typeId))
				return _enumMap.get(typeId);
		}
		catch(Exception ex)
		{
			// TODO add proper logging
	    	ex.printStackTrace();
		}
		
		return DeviceType.Unknown;
	}
	
	/**
	 * 
	 */
	public int value()
	{
		return _value;
	}
}
