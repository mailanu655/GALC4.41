package com.honda.galc.util;

import java.util.Vector;

import com.honda.galc.entity.product.MbpnProduct;

/**
 * @author Subu Kathiresan
 * @date Apr 1, 2013 
 * 
 */
public class ReflectionDummy {

	public int intVal = -1;
	public String strVal = "dummyString";
	public Vector<String> vector = null;
	
	public ReflectionDummy() {
	}
	
	public ReflectionDummy(int param1) {
		this.intVal = param1;
	}
	
	public ReflectionDummy(int param1, String param2) {
		this.intVal = param1 + 5;
		this.strVal = param2;
	}
	
	public ReflectionDummy(Integer param1, String param2) {
		this.intVal = param1 + 10;
		this.strVal = param2;
	}
	
	public ReflectionDummy(Integer param1, Object param2) {
		this.intVal = param1 + 20;
		this.strVal = "";
	}
	
	public ReflectionDummy(Vector<String> param1) {
		this.vector = param1;
	}
	
	public Class<?> testMethod(int intVal) {
		return(int.class);
	}
	
	public Class<?> testMethod(Integer intObj) {
		return(Integer.class);
	}
	
	public Class<?> testMethod(long intVal) {
		return(long.class);
	}
	
	public Class<?> testMethod(Long intObj) {
		return(Long.class);
	}
	
	public Class<?> testMethod(double intVal) {
		return(double.class);
	}
	
	public Class<?> testMethod(Double intObj) {
		return(Double.class);
	}
	
	public Class<?> testMethod(float intVal) {
		return(float.class);
	}
	
	public Class<?> testMethod(Float intObj) {
		return(Float.class);
	}
	
	public Class<?> testMethod(boolean booleanVal) {
		return(boolean.class);
	}

	public Class<?> testMethod(Boolean booleanObj) {
		return(Boolean.class);
	}
	
	public void testMethod(String strVal, int intVal) {
		this.strVal = "invoked method with parameters String, int";
		this.intVal = 100;
	}
	
	public void testMethod(String strVal, Integer intVal) {
		this.strVal = "invoked method with parameters String, Integer";
		this.intVal = 110;
	}
	
	public void testMethod(String strVal, double doubleVal) {
		this.strVal = "invoked method with parameters String, double";
		this.intVal = 120;
	}
	
	public void testMethod(MbpnProduct mbpnProduct, int intVal) {
		this.strVal = "invoked method with parameters MbpnProduct, int";
		this.intVal = intVal;
	}
	
	public int getIntVal(){
		return intVal;
	}
	
	public String getStrVal() {
		return strVal;
	}
}
