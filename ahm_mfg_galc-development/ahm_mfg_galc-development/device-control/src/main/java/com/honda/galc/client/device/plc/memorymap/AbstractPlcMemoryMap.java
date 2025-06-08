/**
 * 
 */
package com.honda.galc.client.device.plc.memorymap;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.lang.reflect.Method;

/**
 * @author Subu Kathiresan
 * Oct 31, 2011
 */
public abstract class AbstractPlcMemoryMap {
	
	/**
	 * sets provided value to the bean by invoking
	 * the appropriate method
	 * 
	 * @param methodName
	 * @param value
	 * @return
	 */
	public boolean setValue(String methodName, String value) {
		Method targetMethod;
		try {
			targetMethod = this.getClass().getDeclaredMethod(methodName, String.class);
			targetMethod.invoke(this, value);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error("setValue failure: Unable to invoke method setter: " + methodName );
		}
		return false;
	}
	
	public boolean setValue(String methodName, StringBuilder value) {
		Method targetMethod;
		try {
			targetMethod = this.getClass().getDeclaredMethod(methodName, StringBuilder.class);
			targetMethod.invoke(this, value);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error("setValue failure: Unable to invoke method setter: " + methodName );
		}
		return false;
	}
}
