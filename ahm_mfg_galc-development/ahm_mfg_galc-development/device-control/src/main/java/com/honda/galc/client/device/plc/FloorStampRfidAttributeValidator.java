/**
 * 
 */
package com.honda.galc.client.device.plc;

/**
 * @author Subu Kathiresan
 * @date Dec 5, 2012
 */
public class FloorStampRfidAttributeValidator extends AttributeValidator {

	@Override
	public boolean validate(PlcDataCollectionBean bean, IPlcDataField field) {
		if (!super.validate(bean, field)) {
			//TODO set info code & info message in bean
			return false;
		}
		return true;
	}
}
