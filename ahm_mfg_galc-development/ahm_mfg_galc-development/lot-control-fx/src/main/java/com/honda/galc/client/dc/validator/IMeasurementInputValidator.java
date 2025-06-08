package com.honda.galc.client.dc.validator;

import com.honda.galc.device.dataformat.InputData;
import com.honda.galc.entity.conf.MCOperationMeasurement;

/**
 * @author Subu Kathiresan
 * @date Jun 23, 2014
 */
public interface IMeasurementInputValidator<T extends InputData> extends InputValidator<T> {
	
	public boolean validate(T data, MCOperationMeasurement mSpec);
}
