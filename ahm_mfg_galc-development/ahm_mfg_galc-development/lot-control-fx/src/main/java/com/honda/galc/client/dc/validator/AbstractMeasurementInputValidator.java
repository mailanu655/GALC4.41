package com.honda.galc.client.dc.validator;

import com.honda.galc.device.dataformat.InputData;

/**
 * @author Subu Kathiresan
 * @date Jun 25, 2014
 */
public abstract class AbstractMeasurementInputValidator<T extends InputData> implements IMeasurementInputValidator<T> {
	
	protected T inputData;
	
	public AbstractMeasurementInputValidator(T inputData) {
		this.inputData = inputData;
	}

}
