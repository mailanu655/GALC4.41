package com.honda.galc.util;

/**
 * @author Subu Kathiresan
 * @date Mar 28, 2013 
 * 
 */
public class Primitive {
	
	private Class<?> type;
	private Object value;
	
	private Primitive() {}

	public Primitive(short val) {
		type = short.class;
		value = val;
	}
	
	public Primitive(int val) {
		type = int.class;
		value = val;
	}
	
	public Primitive(long val) {
		type = long.class;
		value = val;
	}

	public Primitive(double val) {
		type = double.class;
		value = val;
	}
	
	public Primitive(float val) {
		type = float.class;
		value = val;
	}
	
	public Primitive(char val) {
		type = char.class;
		value = val;
	}
	
	public Primitive(byte val) {
		type = byte.class;
		value = val;
	}
	
	public Primitive(boolean val) {
		type = boolean.class;
		value = val;
	}
	
	public Primitive(Object obj) {
		type = obj.getClass();
		value = obj;
	}
	
	public Class<?> getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}
}
