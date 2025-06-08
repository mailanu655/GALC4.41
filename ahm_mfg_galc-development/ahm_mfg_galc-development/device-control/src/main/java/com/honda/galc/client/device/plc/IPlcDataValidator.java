/**
 * 
 */
package com.honda.galc.client.device.plc;

/**
 * @author Subu Kathiresan
 * @date Dec 5, 2012
 */
public interface IPlcDataValidator {

	public boolean validate(PlcDataCollectionBean bean, IPlcDataField field);
		
}
