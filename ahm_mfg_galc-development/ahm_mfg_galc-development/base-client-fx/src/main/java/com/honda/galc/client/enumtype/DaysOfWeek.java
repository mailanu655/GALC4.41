/**
 * 
 */
package com.honda.galc.client.enumtype;

import java.util.TreeMap;

/**
 * @author Subu Kathiresan
 * Sep 21, 2011
 */
public enum DaysOfWeek {
	SUN(0),
	MON(1),
	TUE(2),
	WED(3),
	THU(4),
	FRI(5),
	SAT(6);
	
	int _value = 0;	// set default day to Sunday
	
	private static int DAY_MAX_VALUE = 6;
	private static int DAY_MIN_VALUE = 0;
	private static TreeMap<Integer, DaysOfWeek> _enumIntMap;
	private static TreeMap<String, DaysOfWeek> _enumStrMap;
	
	/**
	 * Create a map of all enum values with days as keys
	 */
	static {
		_enumIntMap = new TreeMap<Integer, DaysOfWeek>();
		_enumStrMap = new TreeMap<String, DaysOfWeek>();
		
		for (DaysOfWeek dayOfWeek: DaysOfWeek.values())	{
			_enumIntMap.put(new Integer(dayOfWeek.getValue()), dayOfWeek);
			_enumStrMap.put(new String(dayOfWeek.toString()), dayOfWeek);
		}
	}
	
	/**
	 * private constructor that takes the integer value of the day
	 * 
	 * @param day
	 */
	private DaysOfWeek(int value) {
		_value = value;
	}
	
	/**
	 * returns the integer representing the day
	 * 
	 * @return
	 */
	public int getValue() {
		return _value;
	}
	
	public static DaysOfWeek getDayOfWeek(int key) {
		if (key < DAY_MIN_VALUE || key > DAY_MAX_VALUE) 
			return null;
		
		return _enumIntMap.get(key);
	}
	
	public static DaysOfWeek getDayOfWeek(String key) {
		if (key == null || key.trim().length() < 3)
			return null;
		
		key = key.trim().substring(0, 3).toUpperCase();
		return _enumStrMap.get(key);
	}
	
	public static int getDay(String key) {
		if (key == null || key.trim().length() < 3)
			return -1;
		
		key = key.trim().substring(0, 3).toUpperCase();
		return getDayOfWeek(key).getValue();
	}
	
	public static String getDay(int key) {
		if (key < DAY_MIN_VALUE || key > DAY_MAX_VALUE) 
			return "";
		
		return getDayOfWeek(key).toString();
	}
}
